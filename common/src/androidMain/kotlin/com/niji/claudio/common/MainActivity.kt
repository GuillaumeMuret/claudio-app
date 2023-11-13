package com.niji.claudio.common

import android.Manifest.permission.POST_NOTIFICATIONS
import android.Manifest.permission.RECORD_AUDIO
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.provider.OpenableColumns
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.app.ActivityCompat
import com.niji.claudio.common.data.feature.user.usecase.GetUserUseCase
import com.niji.claudio.common.data.feature.user.usecase.TokenReceivedUseCase
import com.niji.claudio.common.data.model.Device
import com.niji.claudio.common.tool.LogUtils
import com.niji.claudio.common.tool.UserPreferencesUtils
import com.niji.claudio.common.ui.ClaudioApp
import com.niji.claudio.common.ui.theme.ClaudioTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class MainActivity : ComponentActivity() {

    private val androidMvm: AndroidMediasViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this
        setContent {
            val requestPermissionsDialogState: Boolean by androidMvm.mVm.requestPermissionsDialogState.collectAsState()
            ClaudioTheme {
                ClaudioApp(androidMvm.mVm, this@MainActivity, ::launchFileChooserIntent)
                if (requestPermissionsDialogState) {
                    requestPermission()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val serviceIntent = Intent(this, MqttService::class.java)
        startService(serviceIntent)

        CoroutineScope(Dispatchers.IO).launch {
            if (!androidMvm.isCheckedLaunched) {
                androidMvm.isCheckedLaunched = true
                androidMvm.mVm.checkPermissions()
                val currentUser = GetUserUseCase().execute()
                val pushToken =
                    UserPreferencesUtils.getString(
                        applicationContext,
                        UserPreferencesUtils.DEVICE_PUSH_TOKEN_PREF
                    )
                LogUtils.d(TAG, "pushToken = $pushToken")
                LogUtils.d(TAG, "current user = $currentUser")
                if (pushToken != null
                    && (currentUser.mDeviceServerId == null
                            || currentUser.mDeviceServerId == Device.NO_SERVER_ID
                            || pushToken != currentUser.mPushToken)
                ) {
                    LogUtils.d(TAG, "must register = $pushToken")
                    TokenReceivedUseCase(pushToken).execute()
                }
                androidMvm.isCheckedLaunched = false
            }
        }
    }

    private fun launchFileChooserIntent() {
        androidMvm.mVm.addMediaFilePath.value = ""
        val intent = Intent().setAction(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        val mimetypes = arrayOf("audio/*", "video/*")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes)
        resultFileChooser.launch(intent)
    }

    private var resultFileChooser =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let {
                    androidMvm.mVm.addMediaFilename = getFileName(it) ?: ""
                    androidMvm.mVm.addMediaFilePath.value = result?.data?.data?.toString() ?: ""
                }
            }
        }

    private fun requestPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory(Intent.CATEGORY_DEFAULT)
                intent.data = Uri.parse(String.format("package:%s", applicationContext.packageName))
                startActivity(intent)
            } catch (e: Exception) {
                try {
                    val intent = Intent()
                    intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(
                        this@MainActivity,
                        "Too bad :( you will NOT have the permission to manage files",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(RECORD_AUDIO, POST_NOTIFICATIONS),
                1
            )
        } else {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(WRITE_EXTERNAL_STORAGE, RECORD_AUDIO),
                1
            )
        }
    }

    @SuppressLint("Range")
    private fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
            cursor.use { safeCursor ->
                if (safeCursor != null && safeCursor.moveToFirst()) {
                    result =
                        safeCursor.getString(safeCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != -1) {
                result = result?.substring(cut?.plus(1) ?: 0)
            }
        }
        return result
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }

    companion object {
        private const val TAG = "MaMainActivity"
        var instance: MainActivity? = null
    }
}