package com.niji.claudio.common.tool

import javax.sound.sampled.AudioSystem
import javax.sound.sampled.BooleanControl
import javax.sound.sampled.CompoundControl
import javax.sound.sampled.Control
import javax.sound.sampled.FloatControl
import javax.sound.sampled.Line
import javax.sound.sampled.LineUnavailableException
import javax.sound.sampled.Mixer
import kotlin.math.max
import kotlin.math.min


actual object DeviceUtils {

    private const val TAG = "DeviceUtils"
    private const val MAX_SOUND = 1F
    private const val MIN_SOUND = 0F
    private const val INCREASE_DECREASE_SOUND = 0.1F

    actual fun vibrate() {
        LogUtils.d(TAG, getHierarchyInfo())
        LogUtils.d(TAG, "" + getMasterOutputVolume())
    }

    actual fun raiseVolume() {
        setMasterOutputVolume(
            min(
                (getMasterOutputVolume() ?: MAX_SOUND).plus(
                    INCREASE_DECREASE_SOUND
                ), MAX_SOUND
            )
        )
    }

    actual fun lowerVolume() {
        setMasterOutputVolume(
            max(
                (getMasterOutputVolume() ?: MIN_SOUND).minus(
                    INCREASE_DECREASE_SOUND
                ), MIN_SOUND
            )
        )
    }

    actual fun maxVolume() {
        setMasterOutputVolume(MAX_SOUND)
    }

    actual fun minVolume() {
        setMasterOutputVolume(MIN_SOUND)
    }

    private fun setMasterOutputVolume(value: Float) {
        require(!(value < 0 || value > 1)) { "Volume can only be set to a value from 0 to 1. Given value is illegal: $value" }
        val line = getMasterOutputLine()
            ?: throw RuntimeException("Master output port not found")
        val opened = open(line)
        try {
            val control = getVolumeControl(line)
                ?: throw RuntimeException(
                    "Volume control not found in master port: " + toString(
                        line
                    )
                )
            (control as? FloatControl)?.value = value
        } finally {
            if (opened) line.close()
        }
    }

    private fun getMasterOutputVolume(): Float? {
        val line = getMasterOutputLine() ?: return null
        val opened = open(line)
        return try {
            val control = getVolumeControl(line) ?: return null
            (control as? FloatControl)?.value
        } finally {
            if (opened) line.close()
        }
    }

    fun setMasterOutputMute(value: Boolean) {
        val line = getMasterOutputLine()
            ?: throw RuntimeException("Master output port not found")
        val opened = open(line)
        try {
            val control = getMuteControl(line)
                ?: throw RuntimeException("Mute control not found in master port: " + toString(line))
            (control as? BooleanControl)?.value = value
        } finally {
            if (opened) line.close()
        }
    }

    fun getMasterOutputMute(): Boolean? {
        val line = getMasterOutputLine() ?: return null
        val opened = open(line)
        return try {
            val control = getMuteControl(line) ?: return null
            (control as? BooleanControl)?.value
        } finally {
            if (opened) line.close()
        }
    }

    private fun getMasterOutputLine(): Line? {
        getMixers().forEach { mixer ->
            getAvailableOutputLines(mixer).forEach { line ->
                if (line.lineInfo.toString().contains("Master")) return line
            }
        }
        return null
    }

    private fun getVolumeControl(line: Line): Control? {
        if (!line.isOpen) throw RuntimeException("Line is closed: " + toString(line))
        return findControl(FloatControl.Type.VOLUME, *line.controls)
    }

    private fun getMuteControl(line: Line): Control? {
        if (!line.isOpen) throw RuntimeException("Line is closed: " + toString(line))
        return findControl(BooleanControl.Type.MUTE, *line.controls)
    }

    private fun findControl(type: Control.Type, vararg controls: Control): Control? {
        if (controls.isEmpty()) return null
        controls.forEach { control ->
            if (control.type == type) return control
            if (control is CompoundControl) {
                val member = findControl(type, *control.memberControls)
                if (member != null) return member
            }
        }
        return null
    }

    private fun getMixers(): List<Mixer> {
        val infos: Array<Mixer.Info> = AudioSystem.getMixerInfo()
        val mixers: MutableList<Mixer> = ArrayList(infos.size)
        infos.forEach {
            mixers.add(AudioSystem.getMixer(it))
        }
        return mixers
    }

    private fun getAvailableOutputLines(mixer: Mixer): List<Line> {
        return getAvailableLines(mixer, mixer.targetLineInfo)
    }

    fun getAvailableInputLines(mixer: Mixer): List<Line> {
        return getAvailableLines(mixer, mixer.sourceLineInfo)
    }

    private fun getAvailableLines(mixer: Mixer, lineInfos: Array<Line.Info>): List<Line> {
        val lines: MutableList<Line> = ArrayList(lineInfos.size)
        lineInfos.forEach { lineInfo ->
            val line: Line? = getLineIfAvailable(mixer, lineInfo)
            if (line != null) lines.add(line)
        }
        return lines
    }

    private fun getLineIfAvailable(mixer: Mixer, lineInfo: Line.Info?): Line? {
        return try {
            mixer.getLine(lineInfo)
        } catch (ex: LineUnavailableException) {
            null
        }
    }

    private fun getHierarchyInfo(): String {
        val sb = StringBuilder()
        getMixers().forEach { mixer ->
            sb.append("Mixer: ").append(toString(mixer)).append("\n")
            getAvailableOutputLines(mixer).forEach { line ->
                sb.append("  OUT: ").append(toString(line)).append("\n")
                val opened = open(line)
                line.controls.forEach { control ->
                    sb.append("    Control: ").append(toString(control)).append("\n")
                    if (control is CompoundControl) {
                        control.memberControls.forEach { subControl ->
                            sb.append("      Sub-Control: ").append(toString(subControl))
                                .append("\n")
                        }
                    }
                }
                if (opened) line.close()
            }
            getAvailableOutputLines(mixer).forEach { line ->
                sb.append("  IN: ").append(toString(line)).append("\n")
                val opened = open(line)
                line.controls.forEach { control ->
                    sb.append("    Control: ").append(toString(control)).append("\n")
                    if (control is CompoundControl) {
                        control.memberControls.forEach {
                            sb.append("      Sub-Control: ").append(toString(it))
                                .append("\n")
                        }
                    }
                }
                if (opened) line.close()
            }
            sb.append("\n")
        }
        return sb.toString()
    }

    private fun open(line: Line): Boolean {
        if (line.isOpen) return false
        try {
            line.open()
        } catch (ex: LineUnavailableException) {
            return false
        }
        return true
    }

    private fun toString(control: Control?): String? {
        return if (control == null) null else ("$control (" + control.type
            .toString()) + ")"
    }

    private fun toString(line: Line?): String? {
        if (line == null) return null
        val info = line.lineInfo
        return info.toString()
    }

    private fun toString(mixer: Mixer?): String? {
        if (mixer == null) return null
        val sb = StringBuilder()
        val info: Mixer.Info = mixer.mixerInfo
        sb.append(info.name)
        sb.append(" (").append(info.description).append(")")
        sb.append(if (mixer.isOpen) " [open]" else " [closed]")
        return sb.toString()
    }
}
