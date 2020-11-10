package com.applehealth.androidApp.di

import com.applehealth.androidApp.data.source.camera.CameraRepository
import com.applehealth.androidApp.data.source.camera.CameraRepositoryImp
import com.applehealth.androidApp.data.source.camera.local.CameraLocalSource
import com.applehealth.androidApp.data.source.camera.local.CameraLocalSourceImp
import com.applehealth.androidApp.data.source.camera.remote.CameraRemoteSource
import com.applehealth.androidApp.data.source.camera.remote.CameraRemoteSourceImp
import com.applehealth.androidApp.ui.fragments.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    viewModel { MainViewModel(get()) }
    factory<CameraLocalSource> { CameraLocalSourceImp() }
    factory<CameraRemoteSource> { CameraRemoteSourceImp() }
    factory<CameraRepository> { CameraRepositoryImp(get(), get()) }
}