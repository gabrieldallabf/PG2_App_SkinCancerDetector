package com.example.skincancerdetector

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.skincancerdetector.classificador.Classificador
import com.example.skincancerdetector.utils.Utils
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class ResultFragment : Fragment() {
    private lateinit var backButton: ImageButton
    private lateinit var homeButton: Button
    private lateinit var  resultTextCancer: TextView
    private lateinit var  resultTextMelanoma: TextView
    private lateinit var  confidenceCancer: TextView
    private lateinit var  confidenceMelanoma: TextView
    private lateinit var imageView: ImageView
    private lateinit var container: ConstraintLayout
    private lateinit var outputDirectory: File

    private var imageCropped: Bitmap? = null
    private var metadataArray20: FloatArray? = null
    private var rCancer: String? = null
    private var rMelanoma: String? = null
    private var cCancer: Float = 0.0F
    private var cMelanoma: Float = 0.0F

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_result, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        container = view as ConstraintLayout
        backButton = container.findViewById(R.id.back_to_arguments)
        homeButton = container.findViewById(R.id.back_home)
        resultTextCancer = container.findViewById(R.id.result_text_cancer)
        resultTextMelanoma = container.findViewById(R.id.result_text_melanoma)
        confidenceCancer = container.findViewById(R.id.confidence_cancer)
        confidenceMelanoma = container.findViewById(R.id.confidence_melanoma)
        imageView = container.findViewById(R.id.final_image)
        imageCropped = arguments?.getParcelable("cropped_image")
        imageView.setImageBitmap(imageCropped)

        outputDirectory = MainActivity.getOutputDirectory(requireContext())

        metadataArray20 = arguments?.getFloatArray("metadata_array20")
        var metadataString = ""
        for (value in metadataArray20!!) {
            metadataString += "${value.toInt()},"
        }
        Log.v("META", "20 META RESULT: " + metadataString)

        val classifCancer = Classificador()
        val pathCancer = Utils.assetFilePath(this.requireContext(), "mobilenet_v2_39_accuracy__cpu_meta_concat20.pt")
        classifCancer.classifier(pathCancer)
        val predCancer = classifCancer.predict(imageCropped, metadataArray20!!, "C")
        resultTextCancer.text = predCancer
        val finalScoreCancer = classifCancer.confidenceScore(imageCropped, metadataArray20!!)
        confidenceCancer.text = "$finalScoreCancer %"
        rCancer = predCancer
        cCancer = finalScoreCancer

        val classifMelanoma = Classificador()
        val pathMelanoma = Utils.assetFilePath(this.requireContext(), "mobilenet_v3_6_recall_precision__cpu_meta_concat30.pt")
        classifMelanoma.classifier(pathMelanoma)
        val predMelanoma = classifMelanoma.predict(imageCropped, metadataArray20!!, "M")
        resultTextMelanoma.text = predMelanoma
        val finalScoreMelanoma = classifMelanoma.confidenceScore(imageCropped, metadataArray20!!)
        confidenceMelanoma.text = "$finalScoreMelanoma %"
        rMelanoma = predMelanoma
        cMelanoma = finalScoreMelanoma

    }

    override fun onResume() {
        super.onResume()

        saveTextFile(rCancer!!, rMelanoma!!, cCancer, cMelanoma)

        backButton.setOnClickListener{
            imageCropped = arguments?.getParcelable("cropped_image")
            val imageName = arguments?.getString("image_file_name")
            val metadataArray14 = arguments?.getFloatArray("metadata_array14")
            val bundle = bundleOf("cropped_image" to imageCropped, "metadata_array14" to metadataArray14, "image_file_name" to imageName)
            container.findNavController().navigate(R.id.action_resultFragment_to_argumentsFragment, bundle)
        }

        homeButton.setOnClickListener {
            Navigation.findNavController(
                    requireActivity(), R.id.fragment_container
            ).navigate(R.id.action_resultFragment_to_cameraFragment)
        }
    }

    private fun saveTextFile(predCancer: String, predMelanoma: String, confCancer: Float, confMelanoma: Float) {
        val imageName = arguments?.getString("image_file_name")
        val header = "ImageID,ARM,NECK,FACE,HAND,FOREARM,CHEST,NOSE,THIGH,SCALP,EAR,BACK,FOOT,ABDOMEN,LIP,ITCH,GREW,HURT,CHANGED,BLEED,ELEVATION,RESULT_CANCER,CONFIDENCE_CANCER,RESULT_MELANOMA,CONFIDENCE_MELANOMA\n" +
                "ImageID,BRACO,PESCOCO,ROSTO,MAO,ANTEBRACO,PEITO,NARIZ,PERNA(COXA),ESCALPO,ORELHA,COSTAS,PE,ABDOMEM,LABIO,COCA,CRESCEU,DOI,MUDOU,SANGRA,ELEVACAO,RESULTADO_CANCER,CONFIANCA_CANCER,RESULTADO_MELANOMA,CONFIANCA_MELANOMA\n"
        var metadataString = ""
        for (value in metadataArray20!!) {
            metadataString += "${value.toInt()},"
        }
        val results = "$imageName,$metadataString$predCancer,$confCancer,$predMelanoma,$confMelanoma\n"
        try {
            val fileName = "${SimpleDateFormat("yyyy-MM-dd", Locale.US).format(System.currentTimeMillis())}.txt"
            Log.d("TXT", "FILE NAME: ${fileName}")
            val file = File(outputDirectory, fileName)
            if (file.exists()) {
                //FileOutputStream(file)
                file.appendText(results)
                //display file saved message
                Toast.makeText(this.requireContext(), "Dados salvos!", Toast.LENGTH_SHORT).show()
            }else{
                // Arquivo ainda não existe, criar e inserir header
                //FileOutputStream(file)
                file.appendText(header)
                file.appendText(results)
                //display file saved message
                Toast.makeText(this.requireContext(), "Arquivo criado e dados salvos!", Toast.LENGTH_SHORT).show()
            }
        }
        catch (e: FileNotFoundException){
            e.printStackTrace()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
