import androidx.compose.ui.window.ComposeUIViewController
import com.niji.claudio.common.ui.ClaudioApp
import com.niji.claudio.common.ui.MediasViewModel
import platform.UIKit.UIViewController

@Suppress("FunctionName", "unused")
fun MainViewController(): UIViewController =
    ComposeUIViewController {
        ClaudioApp(MediasViewModel())
    }
