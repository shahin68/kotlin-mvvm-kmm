package com.applehealth.androidApp.ui.fragments.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applehealth.androidApp.data.source.camera.CameraRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val cameraRepository: CameraRepository
) : ViewModel() {

    fun storeCapturedPhoto() {
        viewModelScope.launch {
            cameraRepository.storeCapturedPhoto()
        }
    }

    fun storeCapturedVideo() {
        viewModelScope.launch {
            cameraRepository.storeCapturedVideo()
        }
    }
}