package com.applehealth.androidApp.ui.fragments.audioRecorder

import android.Manifest
import android.content.res.ColorStateList
import android.graphics.Color
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.applehealth.androidApp.R
import com.applehealth.androidApp.databinding.AudioRecorderFragmentBinding
import com.applehealth.androidApp.ui.fragments.BaseFragment
import com.applehealth.androidApp.utils.PermissionRequest
import kotlinx.coroutines.*
import java.io.File
import java.io.IOException

class AudioRecorderFragment :
    BaseFragment<AudioRecorderFragmentBinding>(R.layout.audio_recorder_fragment) {

    private var fileName: String = ""
    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    val scope = CoroutineScope(Job() + Dispatchers.Main)
    private var job: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AudioRecorderFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recordPermission = PermissionRequest(this, Manifest.permission.RECORD_AUDIO)
        recordPermission.runOnGrant {
            val writePermission = PermissionRequest(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            writePermission.runOnGrant {
                makeAudioFile()
                initActions()
            }
            writePermission.onDeny {
                findNavController().popBackStack()
            }
        }
        recordPermission.onDeny {
            findNavController().popBackStack()
        }
    }

    private fun makeAudioFile() {
        val storageDir = File(Environment.getExternalStorageDirectory(), "HealthApp/")
        if (!storageDir.exists()) {
            storageDir.mkdir()
        }
        fileName = File(
            storageDir, "audiorecordtest.3gp"
        ).path
    }

    private var mStartRecording = true
    private var mStartPlaying = true
    private fun initActions() {
        binding.recordBtn.setOnClickListener {
            onRecord(mStartRecording)
            binding.recordBtn.text = when (mStartRecording) {
                true -> getString(R.string.stop_recording)
                false -> getString(R.string.start_recording)
            }
            mStartRecording = !mStartRecording
        }
        binding.playBtn.setOnClickListener {
            onPlay(mStartPlaying)
            binding.playImg.setImageResource(
                when (mStartPlaying) {
                    true -> R.drawable.ic_pause
                    false -> R.drawable.ic_play
                }
            )
            mStartPlaying = !mStartPlaying
        }
    }

    private fun onRecord(start: Boolean) = if (start) {
        startRecording()
    } else {
        stopRecording()
    }

    private fun onPlay(start: Boolean) = if (start) {
        startPlaying()
    } else {
        pausePlaying()
    }

    private fun startPlaying() {
        if (player != null) {
            player?.start()
            runProgressBar()
            return
        }
        player = MediaPlayer().apply {
            try {
                setDataSource(fileName)
                prepare()
                setOnCompletionListener {
                    binding.recordBtn.isEnabled = true
                    binding.recordBtn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.colorAccent))
                    binding.playImg.setImageResource(R.drawable.ic_stop)
                    stopPlaying()
                }
                start()
                binding.recordBtn.isEnabled = false
                binding.recordBtn.backgroundTintList = ColorStateList.valueOf(Color.GRAY)
                binding.progressbar.max = this.duration
                runProgressBar()
            } catch (e: IOException) {
            }
        }
    }

    private fun runProgressBar() {
        job = scope.launch {
            while (isActive) {
                delay(50)
                withContext(Dispatchers.Main) {
                    if (isAdded && isVisible)
                    binding.progressbar.progress = (player?.currentPosition ?: 0)
                }
            }
        }
    }

    private fun pausePlaying() {
        player?.pause()
        binding.recordBtn.isEnabled = true
        binding.recordBtn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.colorAccent))
    }

    private fun stopPlaying() {
        player?.release()
        player = null
        job?.cancel()
        binding.recordBtn.isEnabled = true
        binding.recordBtn.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.colorAccent))
        binding.progressbar.progress = 0
    }

    private fun startRecording() {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(fileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                prepare()
            } catch (e: IOException) {
            }

            start()
            binding.playBtn.isEnabled = false
            binding.playBtn.setCardBackgroundColor(Color.GRAY)
            binding.playImg.setImageResource(R.drawable.ic_play)
        }
    }

    private fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
        binding.playBtn.isEnabled = true
        binding.playBtn.setCardBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorAccent
            )
        )
    }

    override fun onPause() {
        super.onPause()
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
        player?.pause()
        job?.cancel()
        binding.recordBtn?.isEnabled = true
        binding.recordBtn?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.colorAccent))
        binding.recordBtn?.text = getString(R.string.start_recording)
        binding.playBtn?.isEnabled = true
        binding.playBtn?.setCardBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorAccent
            )
        )
        binding.playImg?.setImageResource(R.drawable.ic_play)
        if (!mStartRecording) {
            mStartRecording = !mStartRecording
        }
        if (!mStartPlaying) {
            mStartPlaying = !mStartPlaying
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        recorder?.release()
        recorder = null
        player?.release()
        player = null
        job?.cancel()
    }
}