package com.applehealth.androidApp.ui.fragments.dotPuzzle

import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.applehealth.androidApp.R
import com.applehealth.androidApp.data.models.DotProjection
import com.applehealth.androidApp.databinding.DotPuzzleFragmentBinding
import com.applehealth.androidApp.ui.fragments.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.random.Random

class DotPuzzleFragment : BaseFragment<DotPuzzleFragmentBinding>(R.layout.dot_puzzle_fragment) {

    private val viewModel: DotPuzzleViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DotPuzzleFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        viewModel.getNumberOfDots()
    }

    private fun initObservers() {
        viewModel.numberOfDots.observe(viewLifecycleOwner, Observer {
            binding.dotPuzzleView.post {
                for (i in 0 until it) {
                    binding.dotPuzzleView.addPoint(
                        DotProjection(
                            findRandomPoint(),
                            i,
                            isClicked = false
                        )
                    )
                }
            }
        })
    }

    private fun findRandomPoint(): Point {
        val randomX = Random.nextInt(binding.dotPuzzleView.width)
        val randomY = Random.nextInt(binding.dotPuzzleView.height)
        return Point(randomX, randomY)
    }

}