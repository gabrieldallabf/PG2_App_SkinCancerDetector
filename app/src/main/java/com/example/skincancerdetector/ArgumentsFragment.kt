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
import android.widget.Switch
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.navigation.findNavController


class ArgumentsFragment : Fragment() {

    private lateinit var container: ConstraintLayout
    private lateinit var itchSwitch: Switch
    private lateinit var grewSwitch: Switch
    private lateinit var changedSwitch: Switch
    private lateinit var hurtsSwitch: Switch
    private lateinit var bleedsSwitch: Switch
    private lateinit var elevationSwitch: Switch
    private lateinit var backButton: ImageButton
    private lateinit var confirmeButton: Button

    var itch: Float = 0.0F
    var grew: Float = 0.0F
    var hurts: Float = 0.0F
    var changed: Float = 0.0F
    var bleeds: Float = 0.0F
    var elevation: Float = 0.0F

    private var metadataArray6: FloatArray? = null
    private var metadataArray14: FloatArray? = null
    private var metadataArray20: FloatArray? = null

    private var imageCropped: Bitmap? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_arguments, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        container = view as ConstraintLayout
        backButton = container.findViewById(R.id.back_to_camera_button)
        confirmeButton = container.findViewById(R.id.confirme_button)

        metadataArray14 = arguments?.getFloatArray("metadata_array14")

        itchSwitch = container.findViewById(R.id.itch_switch)
        grewSwitch = container.findViewById(R.id.grew_switch)
        hurtsSwitch = container.findViewById(R.id.hurts_switch)
        changedSwitch = container.findViewById(R.id.changed_switch)
        bleedsSwitch = container.findViewById(R.id.bleeds_switch)
        elevationSwitch = container.findViewById(R.id.elevation_switch)
    }

    override fun onResume() {
        super.onResume()

        itchSwitch.setOnCheckedChangeListener { _, isChecked ->
            itch = if (isChecked) 1.0F else 0.0F
        }
        grewSwitch.setOnCheckedChangeListener { _, isChecked ->
            grew = if (isChecked) 1.0F else 0.0F
        }
        hurtsSwitch.setOnCheckedChangeListener { _, isChecked ->
            hurts = if (isChecked) 1.0F else 0.0F
        }
        changedSwitch.setOnCheckedChangeListener { _, isChecked ->
            changed = if (isChecked) 1.0F else 0.0F
        }
        bleedsSwitch.setOnCheckedChangeListener { _, isChecked ->
            bleeds = if (isChecked) 1.0F else 0.0F
        }
        elevationSwitch.setOnCheckedChangeListener { _, isChecked ->
            elevation = if (isChecked) 1.0F else 0.0F
        }

        backButton.setOnClickListener{
            imageCropped = arguments?.getParcelable("cropped_image")
            val imageName = arguments?.getString("image_file_name")
            val bundle = bundleOf("cropped_image" to imageCropped, "image_file_name" to imageName)
            container.findNavController().navigate(R.id.action_argumentsFragment_to_bodyPartFragment, bundle)
        }

        confirmeButton.setOnClickListener{
            metadataArray6 = floatArrayOf(itch,grew,hurts,changed,bleeds,elevation)
            metadataArray20 = metadataArray14!! + metadataArray6!!
            imageCropped = arguments?.getParcelable("cropped_image")
            val imageName = arguments?.getString("image_file_name")
            val bundle = bundleOf("cropped_image" to imageCropped,
                    "metadata_array14" to metadataArray14,
                    "metadata_array20" to metadataArray20,
                    "image_file_name" to imageName)
            container.findNavController().navigate(R.id.action_argumentsFragment_to_resultFragment, bundle)
        }
    }

    override fun onStop() {
        super.onStop()
        try {
            var metadataString = ""
            for (value in metadataArray6!!) {
                metadataString += "${value.toInt()},"
            }
            Log.v("META", "6 META: " + metadataString)

            var metadataString2 = ""
            for (value in metadataArray20!!) {
                metadataString2 += "${value.toInt()},"
            }
            Log.v("META", "20 META: " + metadataString2)
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

