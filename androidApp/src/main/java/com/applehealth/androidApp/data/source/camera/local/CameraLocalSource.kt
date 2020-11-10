package com.applehealth.androidApp.data.source.camera.local

interface CameraLocalSource {
    suspend fun storeCapturedPhoto()
    suspend fun storeCapturedVideo()
}