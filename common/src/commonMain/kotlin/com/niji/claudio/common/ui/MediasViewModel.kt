package com.niji.claudio.common.ui

import com.niji.claudio.common.data.feature.device.usecase.DeleteDeviceUseCase
import com.niji.claudio.common.data.feature.device.usecase.GetDevicesUseCase
import com.niji.claudio.common.data.feature.log.usecase.GetDataLogs
import com.niji.claudio.common.data.feature.media.usecase.AddMediaUseCase
import com.niji.claudio.common.data.feature.media.usecase.DeleteMediaUseCase
import com.niji.claudio.common.data.feature.media.usecase.DownloadMediaUseCase
import com.niji.claudio.common.data.feature.media.usecase.GetLocalMediasUseCase
import com.niji.claudio.common.data.feature.media.usecase.GetMediasUseCase
import com.niji.claudio.common.data.feature.media.usecase.SetFavoriteMediaUseCase
import com.niji.claudio.common.data.feature.player.usecase.KillPlayerUseCase
import com.niji.claudio.common.data.feature.player.usecase.PlayMediaLocalUseCase
import com.niji.claudio.common.data.feature.player.usecase.PlayMediaOnRemoteUseCase
import com.niji.claudio.common.data.feature.player.usecase.SendTtsUseCase
import com.niji.claudio.common.data.feature.player.usecase.SendVibrateUseCase
import com.niji.claudio.common.data.feature.player.usecase.VolumeLowerPlayerUseCase
import com.niji.claudio.common.data.feature.player.usecase.VolumeMaxPlayerUseCase
import com.niji.claudio.common.data.feature.player.usecase.VolumeMinPlayerUseCase
import com.niji.claudio.common.data.feature.player.usecase.VolumeRaisePlayerUseCase
import com.niji.claudio.common.data.feature.user.usecase.GetUserCurrentDevice
import com.niji.claudio.common.data.feature.user.usecase.GetUserMediaDisplayPreference
import com.niji.claudio.common.data.feature.user.usecase.GetUserSelectedDevice
import com.niji.claudio.common.data.feature.user.usecase.GetUserUseCase
import com.niji.claudio.common.data.feature.user.usecase.SetMustReloadUseCase
import com.niji.claudio.common.data.feature.user.usecase.SetSelectedDeviceUseCase
import com.niji.claudio.common.data.feature.user.usecase.SetUserMediaDisplayPreference
import com.niji.claudio.common.data.feature.user.usecase.SetUserNameUseCase
import com.niji.claudio.common.data.feature.user.usecase.SetUserSleepingMode
import com.niji.claudio.common.data.model.DataLog
import com.niji.claudio.common.data.model.Device
import com.niji.claudio.common.data.model.Media
import com.niji.claudio.common.data.model.User
import com.niji.claudio.common.getPlatformName
import com.niji.claudio.common.tool.CoroutineDispatcherProvider
import com.niji.claudio.common.tool.LogUtils
import com.niji.claudio.common.tool.PermissionUtils
import com.niji.claudio.common.tool.UiUtils
import com.niji.claudio.common.tool.VoiceRecordService
import com.niji.claudio.common.ui.state.AppViewState
import com.niji.claudio.common.ui.widget.DialogAddMediaViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class MediasViewModel {
    var showDeviceDialogState = MutableStateFlow(false)
    var showOverAppDialogState = MutableStateFlow(false)
    var requestPermissionsDialogState = MutableStateFlow(false)
    var recordAudioState = MutableStateFlow(false)
    var showErrorDialogState = MutableStateFlow(false)
    var showOperationInProgress = MutableStateFlow(false)
    var isSleepingMode = MutableStateFlow(false)
    var isAdminState = MutableStateFlow(false)
    var isFavoriteMode = MutableStateFlow(false)
    var mediasState = MutableStateFlow<List<Media>>(listOf())
    var devicesState = MutableStateFlow<List<Device>>(listOf())
    var dataLogsState = MutableStateFlow<List<DataLog>>(listOf())
    var currentDevice = MutableStateFlow<Device?>(null)
    var selectedDevice = MutableStateFlow<Device?>(null)
    var appViewState = MutableStateFlow<AppViewState?>(null)
    var currentDialogDeviceName = ""
    var ttsText = "Bonjour ${getPlatformName()} Niji"
    var query = MutableStateFlow("")
    var dialogAddMediaState =
        MutableStateFlow<DialogAddMediaViewState>(DialogAddMediaViewState.Hide)
    var addMediaFilePath = MutableStateFlow("")
    var addMediaFilename = ""


    fun initMediaScreen() {
        CoroutineScope(CoroutineDispatcherProvider.io()).launch {
            showOperationInProgress.value = true
            appViewState.value =
                UiUtils.getMediaStateClass(GetUserMediaDisplayPreference().execute())
            mediasState.value = GetLocalMediasUseCase(query.value, isFavoriteMode.value).execute()
            val user = GetUserUseCase().execute()
            LogUtils.d(TAG, "user = $user")
            LogUtils.d(TAG, "user device id = ${user.mDeviceServerId}")
            showDeviceDialogState.value = user.name == null
            isAdminState.value = user.isAdmin == true
            isSleepingMode.value = user.isSleepingMode == true
            devicesState.value = GetDevicesUseCase().execute()
            currentDevice.value = GetUserCurrentDevice().execute()
            selectedDevice.value = GetUserSelectedDevice().execute()
            mediasState.value = getMedias()
            dataLogsState.value = GetDataLogs().execute()
            showOperationInProgress.value = false
            checkPermissions()
        }
    }

    suspend fun checkPermissions() {
        LogUtils.d(TAG, "Is permission OK ? ${PermissionUtils.isPermissionsGranted()}")
        if (PermissionUtils.isPermissionsGranted()) {
            requestPermissionsDialogState.value = false
            checkDisplayOverApp()
        } else {
            requestPermissionsDialogState.value = true
        }
    }

    suspend fun checkDisplayOverApp(user: User? = null) {
        val safeUser = user ?: GetUserUseCase().execute()
        showOverAppDialogState.value =
            (safeUser.isAdmin == false || safeUser.isAdmin == null) && !PermissionUtils.isOverlayPermissionGranted()
    }

    private fun initDeviceScreen() {
        CoroutineScope(CoroutineDispatcherProvider.io()).launch {
            val user = GetUserUseCase().execute()
            isAdminState.value = user.isAdmin == true
            devicesState.value = GetDevicesUseCase().execute()
        }
    }

    private suspend fun getMedias() = GetMediasUseCase(query.value, isFavoriteMode.value).execute()

    fun deleteMedia(media: Media) {
        CoroutineScope(CoroutineDispatcherProvider.io()).launch {
            DeleteMediaUseCase(media).execute()
            mediasState.value = getMedias()
        }
    }

    fun downloadMedia(media: Media) {
        CoroutineScope(CoroutineDispatcherProvider.io()).launch {
            DownloadMediaUseCase(media).execute()
        }
    }

    fun deleteDevice(device: Device) {
        CoroutineScope(CoroutineDispatcherProvider.io()).launch {
            val list = DeleteDeviceUseCase(device).execute()
            showErrorDialogState.value = false
            devicesState.value = list
        }
    }

    fun onOverAppDialogConfirm() {
        PermissionUtils.showOverlayPermission()
    }

    fun onOverAppDialogDismiss() {
        CoroutineScope(CoroutineDispatcherProvider.io()).launch { checkPermissions() }
    }

    fun onErrorDialogDismiss() {
        showErrorDialogState.value = false
    }

    fun onDeviceDialogConfirm() {
        LogUtils.d(TAG, "onDeviceDialogConfirm")
        if (currentDialogDeviceName.isNotEmpty()) {
            LogUtils.d(TAG, "currentDialogDeviceName.isNotEmpty()")
            showDeviceDialogState.value = false
            CoroutineScope(CoroutineDispatcherProvider.io()).launch {
                LogUtils.d(TAG, "SetUserNameUseCase")
                SetUserNameUseCase(currentDialogDeviceName).execute()
                devicesState.value = GetDevicesUseCase().execute()
                currentDevice.value = GetUserCurrentDevice().execute()
                selectedDevice.value = GetUserSelectedDevice().execute()
            }
        } else {
            LogUtils.d(TAG, "currentDialogDeviceName is empty")
            showDeviceDialogState.value = true
        }
    }

    fun onDeviceDialogDismiss() {
    }

    fun refreshMedia() {
        SetMustReloadUseCase().execute()
        initMediaScreen()
    }

    fun refreshDevices() {
        SetMustReloadUseCase().execute()
        initDeviceScreen()
    }

    fun refreshDataLogs() {
        SetMustReloadUseCase().execute()
        CoroutineScope(CoroutineDispatcherProvider.default()).launch {
            dataLogsState.value = GetDataLogs().execute()
        }
    }

    fun killPlayer() {
        CoroutineScope(CoroutineDispatcherProvider.io()).launch {
            KillPlayerUseCase(GetUserUseCase().execute()).execute()
        }
    }

    fun volumeMax() {
        CoroutineScope(CoroutineDispatcherProvider.io()).launch {
            VolumeMaxPlayerUseCase(GetUserUseCase().execute()).execute()
        }
    }

    fun volumeMin() {
        CoroutineScope(CoroutineDispatcherProvider.io()).launch {
            VolumeMinPlayerUseCase(GetUserUseCase().execute()).execute()
        }
    }

    fun volumeRaise() {
        CoroutineScope(CoroutineDispatcherProvider.io()).launch {
            VolumeRaisePlayerUseCase(GetUserUseCase().execute()).execute()
        }
    }

    fun volumeLower() {
        CoroutineScope(CoroutineDispatcherProvider.io()).launch {
            VolumeLowerPlayerUseCase(GetUserUseCase().execute()).execute()
        }
    }

    fun mediaPlay(media: Media) {
        CoroutineScope(CoroutineDispatcherProvider.io()).launch {
            val user = GetUserUseCase().execute()
            if (user.mDeviceServerId == null) {
                user.mDeviceServerId = Device.NO_SERVER_ID
            }
            LogUtils.d(TAG, "${user.mDeviceServerId} == ${user.selectedServerIdDevice} ??")
            if (user.mDeviceServerId == user.selectedServerIdDevice) {
                LogUtils.d(TAG, "PlayMedia ${media.serverId}")
                PlayMediaLocalUseCase(media).execute()
                mediasState.value = getMedias()
            } else {
                LogUtils.d(TAG, "PlayMediaOnRemote ${media.serverId}")
                PlayMediaOnRemoteUseCase(media, user).execute()
            }
        }
    }

    fun launchTts() {
        CoroutineScope(CoroutineDispatcherProvider.io()).launch {
            SendTtsUseCase(ttsText, GetUserUseCase().execute()).execute()
        }
    }

    fun toggleDisplay() {
        if (appViewState.value is AppViewState.MediaDisplayGrid) {
            appViewState.value = AppViewState.MediaDisplayColumn
        } else if (appViewState.value is AppViewState.MediaDisplayColumn) {
            appViewState.value = AppViewState.MediaDisplayGrid
        }
        CoroutineScope(CoroutineDispatcherProvider.io()).launch {
            SetUserMediaDisplayPreference(
                UiUtils.getMediaStateString(
                    appViewState.value ?: AppViewState.MediaDisplayGrid
                )
            ).execute()
        }
    }

    fun displayDataLogs() {
        appViewState.value = AppViewState.DataLog
        refreshDataLogs()
    }

    fun displayDevicesScreen() {
        appViewState.value = AppViewState.DeviceColumn
    }

    fun displayMediasScreen() {
        CoroutineScope(CoroutineDispatcherProvider.io()).launch {
            appViewState.value =
                UiUtils.getMediaStateClass(GetUserMediaDisplayPreference().execute())
        }
    }

    fun onDeviceSelected(device: Device) {
        selectedDevice.value = device
        CoroutineScope(CoroutineDispatcherProvider.io()).launch {
            SetSelectedDeviceUseCase(device).execute()
        }
    }

    fun onQueryChanged(query: String) {
        this.query.value = query
        CoroutineScope(CoroutineDispatcherProvider.io()).launch {
            mediasState.value = getMedias()
        }
    }

    fun onExecuteSearch() {}

    fun setFavoriteMedia(media: Media) {
        CoroutineScope(CoroutineDispatcherProvider.io()).launch {
            SetFavoriteMediaUseCase(media).execute()
            mediasState.value = getMedias()
        }
    }

    fun toggleFavoriteMode() {
        isFavoriteMode.value = !(isFavoriteMode.value)
        CoroutineScope(CoroutineDispatcherProvider.io()).launch {
            mediasState.value = getMedias()
        }
    }

    fun toggleSleepingMode() {
        isSleepingMode.value = !(isSleepingMode.value)
        CoroutineScope(CoroutineDispatcherProvider.io()).launch {
            SetUserSleepingMode(isSleepingMode.value).execute()
        }
    }

    fun addMedia() {
        dialogAddMediaState.value = DialogAddMediaViewState.Show
    }

    fun onAddMediaDialogCancel() {
        LogUtils.d(TAG, "onAddMediaDialogCancel")
        dialogAddMediaState.value = DialogAddMediaViewState.Hide
    }

    fun onAddMediaDialogOk(title: String, category: String, filePath: String, filename: String) {
        LogUtils.d(TAG, "onAddMediaDialogOk")
        dialogAddMediaState.value = DialogAddMediaViewState.Loading
        CoroutineScope(CoroutineDispatcherProvider.io()).launch {
            val media = AddMediaUseCase(
                media = Media(
                    title = title,
                    category = category,
                    filePath = filePath,
                    filename = filename
                )
            ).execute()
            dialogAddMediaState.value = if (media == null) {
                DialogAddMediaViewState.Error()
            } else {
                mediasState.value = getMedias()
                DialogAddMediaViewState.Hide
            }
        }
    }

    fun startRecording() {
        LogUtils.d(TAG, "startRecording")
        VoiceRecordService.startRecording()
        recordAudioState.value = true
    }

    fun stopRecording() {
        LogUtils.d(TAG, "stopRecording")
        VoiceRecordService.stopRecording()
        recordAudioState.value = false
    }

    fun vibrate() {
        CoroutineScope(CoroutineDispatcherProvider.io()).launch {
            SendVibrateUseCase(GetUserUseCase().execute()).execute()
        }
    }

    companion object {
        private const val TAG = "MediasViewModel"
    }
}