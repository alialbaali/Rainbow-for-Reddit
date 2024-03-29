package com.rainbow.desktop.state

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class StateHolder {
    protected val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
}