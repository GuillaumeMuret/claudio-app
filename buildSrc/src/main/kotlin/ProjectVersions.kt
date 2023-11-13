import java.net.URI

object ProjectVersions {
    const val COMPILE_SDK = 34
    const val MIN_SDK = 24
    const val TARGET_SDK = 34
    const val APP_VERSION_CODE = 99999999
    const val APP_NAME = "Claudio"
    const val PACKAGE_NAME = "com.niji.claudio"
    const val PACKAGE_NAME_SHARED = "com.niji.claudio.common"
    const val PACKAGE_NAME_ANDROID = "com.niji.claudio.android"
    const val APP_VERSION_NAME = "1.0.0"
    const val GITHUB_REF_NAME = "GITHUB_REF_NAME"
    const val GITHUB_RUN_NUMBER = "GITHUB_RUN_NUMBER"

    val MAVEN_JETBRAINS_COMPOSE: URI = URI.create("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    val MAVEN_JITPACK: URI = URI.create("https://jitpack.io")

    fun getAppVersionName() = if (System.getenv(GITHUB_REF_NAME) != null && System.getenv(GITHUB_REF_NAME)
            .matches("^v(\\d+\\.)?(\\d+\\.)?(\\*|\\d+)\$".toRegex())
    ) {
        System.getenv(GITHUB_REF_NAME).substring(1)
    } else {
        APP_VERSION_NAME
    }

    fun getAppVersionCode(): Int = if (System.getenv(GITHUB_RUN_NUMBER) != null) {
        System.getenv(GITHUB_RUN_NUMBER).toInt()
    } else {
        APP_VERSION_CODE
    }
}
