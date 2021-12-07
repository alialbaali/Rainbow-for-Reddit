package com.rainbow.app.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class Model {
    protected val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
}