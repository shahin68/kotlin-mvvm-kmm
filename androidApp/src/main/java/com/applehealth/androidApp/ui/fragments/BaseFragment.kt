package com.applehealth.androidApp.ui.fragments

import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.applehealth.androidApp.utils.PermissionRequest

abstract class BaseFragment<Binding : ViewBinding>(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {
    protected var _binding : Binding? = null
    protected  val binding : Binding get() = _binding!!

    override fun onDetach() {
        super.onDetach()
        _binding = null
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionRequest.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}