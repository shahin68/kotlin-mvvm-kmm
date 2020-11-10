package com.applehealth.androidApp.data.source.puzzle.local

import com.applehealth.androidApp.data.NUMBER_OF_DOTS

class PuzzlesLocalSourceImp: PuzzlesLocalSource {
    override suspend fun getNumberOfPoints(): Int {
        return NUMBER_OF_DOTS
    }
}