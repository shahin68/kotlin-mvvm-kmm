package com.applehealth.androidApp.data.source.camera.remote

interface CameraRemoteSource {
    suspend fun sendCapturedToServer()
}