import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import com.niji.claudio.common.ui.ClaudioApp
import com.niji.claudio.common.ui.MediasViewModel
import org.jetbrains.skiko.wasm.onWasmReady

fun main() {
    onWasmReady {
        Window("Claudio") {
            Column(modifier = Modifier.fillMaxSize()) {
                ClaudioApp(MediasViewModel(), this)
            }
        }
    }
}
