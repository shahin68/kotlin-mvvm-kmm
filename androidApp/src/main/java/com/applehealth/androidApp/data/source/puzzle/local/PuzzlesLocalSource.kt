package com.applehealth.androidApp.data.source.puzzle.local

interface PuzzlesLocalSource {
    suspend fun getNumberOfPoints(): Int
}