package com.applehealth.androidApp.ui.fragments.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import com.applehealth.androidApp.R
import com.applehealth.androidApp.data.CAMERA_REQUEST_CODE
import com.applehealth.androidApp.data.GALLERY_REQUEST_CODE
import com.applehealth.androidApp.databinding.MainFragmentBinding
import com.applehealth.androidApp.ui.fragments.BaseFragment
import com.applehealth.androidApp.ui.gallery.ACTION_GET_CONTENT_TYPE
import com.applehealth.androidApp.ui.gallery.GalleryActivity
import com.applehealth.androidApp.ui.gallery.REQUEST_CODE
import com.applehealth.androidApp.utils.PermissionRequest
import com.applehealth.androidApp.views.volumeKnob.AngleListener
import com.applehealth.androidApp.views.volumeKnob.VolumeMeasure
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainFragment : BaseFragment<MainFragmentBinding>(R.layout.main_fragment) {
    private val viewModel: MainViewModel by viewModel()

    private lateinit var capturedFile: File

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mainVolumeKnob.setCustomObjectListener(object : AngleListener {
            override fun onAngleChanged(volumeMeasure: VolumeMeasure) {
                switchActions(volumeMeasure)
            }
        })

        binding.mainIcMic.setOnClickListener {
            binding.mainVolumeKnob.setAngle(VolumeMeasure.ANGLE_0)
        }

        binding.mainIcRec.setOnClickListener {
            binding.mainVolumeKnob.setAngle(VolumeMeasure.ANGLE_90)
        }

        binding.mainIcCam.setOnClickListener {
            binding.mainVolumeKnob.setAngle(VolumeMeasure.ANGLE_180)
        }

        binding.mainIcPage.setOnClickListener {
            binding.mainVolumeKnob.setAngle(VolumeMeasure.ANGLE_270)
        }

        binding.mainBtnOpenGallery.setOnClickListener {
            openGallery()
        }
    }

    private fun switchActions(volumeMeasure: VolumeMeasure) {
        when (volumeMeasure) {
            VolumeMeasure.ANGLE_0 -> {
                binding.mainIcMic.isSelected = true
                binding.mainIcRec.isSelected = false
                binding.mainIcCam.isSelected = false
                binding.mainIcPage.isSelected = false
                navigateToAudioRecorder()
            }
            VolumeMeasure.ANGLE_90 -> {
                binding.mainIcMic.isSelected = false
                binding.mainIcRec.isSelected = true
                binding.mainIcCam.isSelected = false
                binding.mainIcPage.isSelected = false
                openVideoCamera()
            }
            VolumeMeasure.ANGLE_180 -> {
                binding.mainIcMic.isSelected = false
                binding.mainIcRec.isSelected = false
                binding.mainIcCam.isSelected = true
                binding.mainIcPage.isSelected = false
                openPhotoCamera()
            }
            VolumeMeasure.ANGLE_270 -> {
                binding.mainIcMic.isSelected = false
                binding.mainIcRec.isSelected = false
                binding.mainIcCam.isSelected = false
                binding.mainIcPage.isSelected = true
                navigateToPuzzleFragment()
            }
        }
    }

    private fun navigateToPuzzleFragment() {
        findNavController().navigate(
            R.id.dotPuzzleFragment
        )
    }

    private fun createImageFile(): File? {
        val timeStamp: String = SimpleDateFormat(
            getString(R.string.file_name_pattern),
            Locale.getDefault()
        ).format(Date())
//        val storageDir: File? = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val storageDir = File(Environment.getExternalStorageDirectory(), "HealthApp/")
        if (!storageDir.exists()) {
            storageDir.mkdir()
        }
        val imageFile = File(
            storageDir, "JPEG_${timeStamp}_.jpg"
        )
        return when (imageFile.createNewFile()) {
            true -> imageFile
            false -> null
        }
    }

    private fun openPhotoCamera() {
        PermissionRequest(this, Manifest.permission.CAMERA).runOnGrant {
            PermissionRequest(this, Manifest.permission.WRITE_EXTERNAL_STORAGE).runOnGrant {
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
                    intent.resolveActivity(requireActivity().packageManager)?.also {
                        val photoFile: File? = try {
                            createImageFile()
                        } catch (ex: IOException) {
                            null
                        }
                        photoFile?.also {
                            capturedFile = it
                            val photoURI: Uri = FileProvider.getUriForFile(
                                requireContext(),
                                getString(R.string.file_provider),
                                it
                            )
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                            startActivityForResult(intent, CAMERA_REQUEST_CODE)
                        }
                    }
                }
            }
        }
    }

    private fun createVideoFileFile(): File? {
        val timeStamp: String = SimpleDateFormat(
            getString(R.string.file_name_pattern),
            Locale.getDefault()
        ).format(Date())
//        val storageDir: File? = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val storageDir = File(Environment.getExternalStorageDirectory(), "HealthApp/")
        if (!storageDir.exists()) {
            storageDir.mkdir()
        }
        val videoFile = File(storageDir, "VIDEO_${timeStamp}_.mp4")
        return when (videoFile.createNewFile()) {
            true -> videoFile
            false -> null
        }
    }

    private fun openVideoCamera() {
        PermissionRequest(this, Manifest.permission.CAMERA).runOnGrant {
            Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { intent ->
                intent.resolveActivity(requireActivity().packageManager)?.also {
                    val photoFile: File? = try {
                        createVideoFileFile()
                    } catch (ex: IOException) {
                        null
                    }
                    photoFile?.also {
                        capturedFile = it
                        val photoURI: Uri = FileProvider.getUriForFile(
                            requireContext(),
                            getString(R.string.file_provider),
                            it
                        )
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(intent, CAMERA_REQUEST_CODE)
                    }
                }
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(requireActivity(), GalleryActivity::class.java)
        intent.putExtra(ACTION_GET_CONTENT_TYPE, "*/*") /*type not important at this moment*/
        intent.putExtra(REQUEST_CODE, GALLERY_REQUEST_CODE)
        startActivityForResult(
            intent,
            GALLERY_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // not important to process results at the moment
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode != Activity.RESULT_OK) {
                return
            }
        } else if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode != Activity.RESULT_OK) {
                if (capturedFile.length() == 0L) {
                    capturedFile.delete()
                }
                return
            }
        }
        if (capturedFile.exists()) {
            requireActivity().sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(capturedFile)))
        }
    }

    private fun navigateToAudioRecorder() {
        findNavController().navigate(
            R.id.audioRecorderFragment
        )
    }
}