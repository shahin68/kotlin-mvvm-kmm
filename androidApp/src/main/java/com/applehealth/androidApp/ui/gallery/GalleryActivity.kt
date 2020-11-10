package com.applehealth.androidApp.ui.gallery

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.applehealth.androidApp.R

const val ACTION_GET_CONTENT_TYPE = "type"
const val REQUEST_CODE = "request"

/**
 * Empty Activity Handling Gallery Intent
 */
class GalleryActivity : AppCompatActivity() {

    private var contentType = "*/*" /*default type*/
    private var requestCode = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent?.let {
            if (it.hasExtra(ACTION_GET_CONTENT_TYPE)) {
                contentType = it.getStringExtra(ACTION_GET_CONTENT_TYPE) ?: contentType
            }
            if (it.hasExtra(REQUEST_CODE)) {
                requestCode = it.getIntExtra(REQUEST_CODE, 0)
            }
        }
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            val uri: Uri = FileProvider.getUriForFile(
                this@GalleryActivity,
                getString(R.string.file_provider),
                getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
            )
            setDataAndType(
                uri,
                contentType
            )
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, requestCode)
        } else {
            Toast.makeText(this, "No Gallery APP installed", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        setResult(resultCode, data)
        finish()
    }
}