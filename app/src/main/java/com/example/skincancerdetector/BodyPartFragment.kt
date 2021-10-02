package com.example.skincancerdetector

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.RadioGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.navigation.findNavController

class BodyPartFragment : Fragment() {

    private lateinit var container: ConstraintLayout
    private lateinit var backButton: ImageButton
    private lateinit var confirmeButton: Button

    private var imageCropped: Bitmap? = null

    private lateinit var radioGroup: RadioGroup
    private lateinit var radioGroup2: RadioGroup

    private var bodyRegionArray: FloatArray = floatArrayOf(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)
    private var bodyRegionArray2: FloatArray = floatArrayOf(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)

    private var metadataArray14: FloatArray? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_body_part, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        container = view as ConstraintLayout
        backButton = container.findViewById(R.id.back_to_camera_button)
        confirmeButton = container.findViewById(R.id.confirme_button)

        radioGroup = container.findViewById(R.id.radioGroup)
        radioGroup2 = container.findViewById(R.id.radioGroup2)

    }

    override fun onResume() {
        super.onResume()
        radioGroup.setOnCheckedChangeListener { _, _ ->
            radioGroup2.clearCheck()
        }
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_Arm -> {
                    bodyRegionArray = floatArrayOf(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)
                    radioGroup2.clearCheck()
                }
                R.id.radio_Neck -> {
                    bodyRegionArray = floatArrayOf(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)
                    radioGroup2.clearCheck()
                }
                R.id.radio_Face -> {
                    bodyRegionArray = floatArrayOf(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F)
                    radioGroup2.clearCheck()
                }
                R.id.radio_Hand -> {
                    bodyRegionArray = floatArrayOf(0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F)
                    radioGroup2.clearCheck()
                }
                R.id.radio_Forearm -> {
                    bodyRegionArray = floatArrayOf(0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F)
                    radioGroup2.clearCheck()
                }
                R.id.radio_Chest -> {
                    bodyRegionArray = floatArrayOf(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F)
                    radioGroup2.clearCheck()
                }
                R.id.radio_Nose -> {
                    bodyRegionArray = floatArrayOf(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F)
                    radioGroup2.clearCheck()
                }
                else -> bodyRegionArray = floatArrayOf(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)
            }
        }
        radioGroup2.setOnCheckedChangeListener { _, _ ->
            radioGroup.clearCheck()
        }
        radioGroup2.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_Thigh -> {
                    bodyRegionArray2 = floatArrayOf(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)
                    radioGroup.clearCheck()
                }
                R.id.radio_Scalp -> {
                    bodyRegionArray2 = floatArrayOf(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)
                    radioGroup.clearCheck()
                }
                R.id.radio_Ear -> {
                    bodyRegionArray2 = floatArrayOf(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F)
                    radioGroup.clearCheck()
                }
                R.id.radio_Back -> {
                    bodyRegionArray2 = floatArrayOf(0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F)
                    radioGroup.clearCheck()
                }
                R.id.radio_Foot -> {
                    bodyRegionArray2 = floatArrayOf(0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F)
                    radioGroup.clearCheck()
                }
                R.id.radio_Abdomen -> {
                    bodyRegionArray2 = floatArrayOf(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F)
                    radioGroup.clearCheck()
                }
                R.id.radio_Lip -> {
                    bodyRegionArray2 = floatArrayOf(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F)
                    radioGroup.clearCheck()
                }
                else -> bodyRegionArray2 = floatArrayOf(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)
            }
        }

        backButton.setOnClickListener{
            imageCropped = arguments?.getParcelable("cropped_image")
            val imageName = arguments?.getString("image_file_name")
            val bundle = bundleOf("cropped_image" to imageCropped, "image_file_name" to imageName)
            container.findNavController().navigate(R.id.action_bodyPartFragment_to_photoEditFragment, bundle)
        }

        confirmeButton.setOnClickListener{
            metadataArray14 = bodyRegionArray + bodyRegionArray2
            imageCropped = arguments?.getParcelable("cropped_image")
            val imageName = arguments?.getString("image_file_name")
            print(bodyRegionArray)
            val bundle = bundleOf("cropped_image" to imageCropped
                    ,   "metadata_array14" to metadataArray14
                    ,   "image_file_name" to imageName)
            container.findNavController().navigate(R.id.action_bodyPartFragment_to_argumentsFragment, bundle)
        }
    }

    override fun onStop() {
        super.onStop()
        //metadataArray14 = bodyRegionArray + bodyRegionArray2
        try {
            var metadataString = ""
            for (value in metadataArray14!!) {
                metadataString += "${value.toInt()},"
            }
            Log.v("META", "14 META: " + metadataString)
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }
}