package com.applehealth.androidApp.data.source.camera

interface CameraRepository {
    suspend fun storeCapturedPhoto()
    suspend fun storeCapturedVideo()
}