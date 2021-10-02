package com.example.skincancerdetector

import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.theartofdev.edmodo.cropper.CropImageView


class PhotoEditFragment : Fragment() {

    private lateinit var container: ConstraintLayout
    private lateinit var backButton: ImageButton
    private lateinit var confirmeButton: Button
    private lateinit var cropImageView: CropImageView

    private var imageCameraBITMAP: Uri? = null
    private var imageCameraURI: Uri? = null
    private var imageCropped: Bitmap? = null

    private var croppedImage: Bitmap? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_photo_edit, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        container = view as ConstraintLayout
        backButton = container.findViewById(R.id.back_to_camera_button)
        confirmeButton = container.findViewById(R.id.confirme_button)
        cropImageView = container.findViewById(R.id.cropImageView)
        cropImageView.setAspectRatio(1, 1)

        //Receive the image from the camera or the gallery and set the ImageView from this fragment.
        imageCameraURI = arguments?.getParcelable("image_from_camera")
        imageCameraBITMAP = arguments?.getParcelable("selected_image_from_gallery")
        imageCropped = arguments?.getParcelable("cropped_image")

        if (imageCropped != null) {
            cropImageView.setImageBitmap(imageCropped)
        } else if (imageCameraURI != null) {
                    cropImageView.setImageUriAsync(imageCameraURI)
        } else if (imageCameraBITMAP != null) {
                    cropImageView.setImageUriAsync(imageCameraBITMAP)
        }

        cropImageView.post{
            updateUI()
        }
    }

    private fun updateUI() {
        backButton.setOnClickListener {
            Navigation.findNavController(
                requireActivity(), R.id.fragment_container
            ).navigate(R.id.action_photoEditFragment_to_cameraFragment)
        }
        confirmeButton.setOnClickListener {
            croppedImage = cropImageView.croppedImage
            val imageName = arguments?.getString("image_file_name")
            val bundle = bundleOf("cropped_image" to croppedImage, "image_file_name" to imageName)
            container.findNavController().navigate(R.id.action_photoEditFragment_to_bodyPartFragment, bundle)
        }
    }
}
