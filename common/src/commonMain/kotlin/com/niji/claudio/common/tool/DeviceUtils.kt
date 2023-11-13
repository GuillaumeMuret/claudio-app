package com.niji.claudio.common.tool

expect object DeviceUtils {
    fun vibrate()
    fun raiseVolume()
    fun lowerVolume()
    fun maxVolume()
    fun minVolume()
}