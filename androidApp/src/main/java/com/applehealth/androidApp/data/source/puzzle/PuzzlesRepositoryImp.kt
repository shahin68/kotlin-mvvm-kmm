package com.applehealth.androidApp.data.source.puzzle

import com.applehealth.androidApp.data.source.puzzle.local.PuzzlesLocalSource
import com.applehealth.androidApp.data.source.puzzle.remote.PuzzlesRemoteSource

class PuzzlesRepositoryImp(
    private val puzzlesLocalSource: PuzzlesLocalSource,
    private val puzzlesRemoteSource: PuzzlesRemoteSource
): PuzzlesRepository {
    override suspend fun fetchNumberOfDots(): Int {
        return puzzlesLocalSource.getNumberOfPoints()
    }
}