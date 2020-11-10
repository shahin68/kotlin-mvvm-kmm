package com.applehealth.androidApp.di

import com.applehealth.androidApp.data.source.puzzle.PuzzlesRepository
import com.applehealth.androidApp.data.source.puzzle.PuzzlesRepositoryImp
import com.applehealth.androidApp.data.source.puzzle.local.PuzzlesLocalSource
import com.applehealth.androidApp.data.source.puzzle.local.PuzzlesLocalSourceImp
import com.applehealth.androidApp.data.source.puzzle.remote.PuzzlesRemoteSource
import com.applehealth.androidApp.data.source.puzzle.remote.PuzzlesRemoteSourceImp
import com.applehealth.androidApp.ui.fragments.dotPuzzle.DotPuzzleViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val puzzlesModule = module {
    viewModel { DotPuzzleViewModel(get()) }
    factory<PuzzlesLocalSource> { PuzzlesLocalSourceImp() }
    factory<PuzzlesRemoteSource> { PuzzlesRemoteSourceImp() }
    factory<PuzzlesRepository> { PuzzlesRepositoryImp(get(), get()) }
}