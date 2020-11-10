package com.applehealth.androidApp.data.source.camera

import com.applehealth.androidApp.data.source.camera.local.CameraLocalSource
import com.applehealth.androidApp.data.source.camera.remote.CameraRemoteSource

class CameraRepositoryImp(
    private val cameraLocalSource: CameraLocalSource,
    private val cameraRemoteSource: CameraRemoteSource
): CameraRepository {
    override suspend fun storeCapturedPhoto() {
        cameraLocalSource.storeCapturedPhoto()
        cameraRemoteSource.sendCapturedToServer()
    }

    override suspend fun storeCapturedVideo() {
        cameraLocalSource.storeCapturedVideo()
        cameraRemoteSource.sendCapturedToServer()
    }
}