package com.rainbow.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.rainbow.common.App
import com.rainbow.common.ui.RainbowTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RainbowTheme {
                App()
            }
        }
    }
}