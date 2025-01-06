package com.niji.claudio.common.ui.state

open class Platform {
    class Desktop : Platform()
    class Mobile(val type: Type) : Platform()
    class Web : Platform()
}

sealed class Type {
    data object Ios : Type()
    data object Android : Type()
}