import androidx.compose.ui.window.application
import com.rainbow.common.App
import com.rainbow.common.utils.RainbowStrings
import com.rainbow.desktop.RainbowWindow

fun main() = application {
    RainbowWindow(RainbowStrings.Rainbow) {
        App()
    }
}