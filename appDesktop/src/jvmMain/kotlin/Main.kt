import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.niji.claudio.common.data.feature.user.usecase.GetUserUseCase
import com.niji.claudio.common.tool.MqttClientProvider
import com.niji.claudio.common.ui.ClaudioApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Claudio",
        state = WindowState(WindowPlacement.Maximized),
        // icon = painterResource(Res.drawable.ic_launcher)
    ) {
        ClaudioApp(window = window)
    }
    CoroutineScope(Dispatchers.IO).launch {
        while (true) {
            val user = GetUserUseCase().execute()
            user.mDeviceServerId?.let {
                MqttClientProvider.initAndRunClient(it)
            }
            delay(1_000)
        }
    }
}
