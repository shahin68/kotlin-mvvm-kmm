package com.applehealth.androidApp.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import java.util.ArrayList
import java.util.HashMap
import kotlin.math.roundToInt

class PermissionRequest {
    private val request: Request

    companion object {

        @SuppressLint("UseSparseArrays")
        private val requests = HashMap<Int, Request>()

        fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
            if (requests.containsKey(requestCode)) {
                if (isGrantedRequests(grantResults)) {
                    requests[requestCode]!!.granted()
                } else {
                    if (requests[requestCode]!!.repeat > 0) {
                        requests[requestCode]!!.resend()
                    } else {
                        requests[requestCode]!!.notGranted()
                    }
                }
            }
        }

        private fun isGrantedRequests(grantResults: IntArray): Boolean {
            for (g in grantResults) {
                if (g != PermissionChecker.PERMISSION_GRANTED) return false
            }
            return true
        }

        fun isGranted(context: Context, vararg permissions: String): Boolean {
            for (p in permissions) {
                if (ContextCompat.checkSelfPermission(context, p) != PackageManager.PERMISSION_GRANTED) return false
            }
            return true
        }
    }

    constructor(fragment: Fragment, vararg permissions: String) {
        request = Request(permissions, fragment)
    }

    constructor(activity: Activity, vararg permissions: String) {
        request = Request(permissions, activity)
    }

    fun isGranted(): Boolean {
        for (p in request.permissions) {
            if (ActivityCompat.checkSelfPermission(
                    request.context,
                    p
                ) != PackageManager.PERMISSION_GRANTED
            ) return false
        }
        return true
    }

    fun runOnGrant(callback: () -> Unit) {
        if (isGranted()) {
            callback()
        } else {
            send()
        }
        onGrant(callback)
    }

    @JvmOverloads
    fun send(repeatCount: Int = 1) {
        request.send(repeatCount - 1)
    }

    fun onGrant(onGrant: () -> Unit): PermissionRequest {
        request.onGrant = onGrant
        return this
    }

    fun onDeny(onDeny: () -> Unit): PermissionRequest {
        request.onDeny = onDeny
        return this
    }


    private class Request {
        private var activity: Activity? = null
        private var fragment: Fragment? = null
        internal val permissions: Array<out String>
        internal var onGrant: (() -> Unit)? = null
        internal var onDeny: (() -> Unit)? = null
        internal var repeat: Int = 0
        val code: Int = (Math.random() * 10000).roundToInt()
        val context: Context
            get() = activity ?: fragment!!.requireContext()

        constructor(permissions: Array<out String>, fragment: Fragment) {
            this.fragment = fragment
            this.permissions = permissions
        }

        constructor(permissions: Array<out String>, activity: Activity) {
            this.activity = activity
            this.permissions = permissions
        }


        fun send(repeatCount: Int) {
            var notGranted: MutableList<String>? = null
            for (p in permissions) {
                if (ActivityCompat.checkSelfPermission(context, p) != PackageManager.PERMISSION_GRANTED) {
                    if (notGranted == null) notGranted = ArrayList()
                    notGranted.add(p)
                }
            }
            if (notGranted == null) {
                granted()
                return
            }

            repeat = repeatCount
            if (!requests.containsKey(code)) requests[code] = this
            if (activity != null) {
                ActivityCompat.requestPermissions(activity!!, notGranted.toTypedArray(), code)
            } else {
                fragment!!.requestPermissions(notGranted.toTypedArray(), code)
            }
        }

        fun resend() {
            send(repeat - 1)
        }

        fun granted() {
            if (!requests.containsKey(code)) requests.remove(code)
            onGrant?.let { it() }
        }

        fun notGranted() {
            if (!requests.containsKey(code)) requests.remove(code)
            onDeny?.let { it() }
        }
    }
}