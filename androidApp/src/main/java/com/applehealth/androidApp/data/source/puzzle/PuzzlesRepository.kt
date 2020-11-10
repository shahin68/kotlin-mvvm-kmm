package com.applehealth.androidApp.data.source.puzzle

interface PuzzlesRepository {
    suspend fun fetchNumberOfDots(): Int
}