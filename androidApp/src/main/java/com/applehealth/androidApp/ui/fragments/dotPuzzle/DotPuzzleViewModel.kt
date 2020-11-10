package com.applehealth.androidApp.ui.fragments.dotPuzzle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applehealth.androidApp.data.source.puzzle.PuzzlesRepository
import kotlinx.coroutines.launch

class DotPuzzleViewModel(
    private val puzzlesRepository: PuzzlesRepository
) : ViewModel() {

    private val _numberOfDots: MutableLiveData<Int> = MutableLiveData()
    val numberOfDots: LiveData<Int> = _numberOfDots

    fun getNumberOfDots() {
        viewModelScope.launch {
            val res = puzzlesRepository.fetchNumberOfDots()
            _numberOfDots.postValue(
                res
            )
        }
    }

}