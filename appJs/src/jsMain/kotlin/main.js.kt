import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.CanvasBasedWindow
import com.niji.claudio.common.ui.ClaudioApp
import com.niji.claudio.common.ui.MediasViewModel
import org.jetbrains.skiko.wasm.onWasmReady

fun main() {
    onWasmReady {
        @OptIn(ExperimentalComposeUiApi::class)
        CanvasBasedWindow("Claudio") {
            Column(modifier = Modifier.fillMaxSize()) {
                ClaudioApp(MediasViewModel(), this)
            }
        }
    }
}
