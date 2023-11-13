package com.niji.claudio.common

import androidx.lifecycle.ViewModel
import com.niji.claudio.common.ui.MediasViewModel

class AndroidMediasViewModel(val mVm: MediasViewModel = MediasViewModel()) : ViewModel() {
    var isCheckedLaunched = false
}