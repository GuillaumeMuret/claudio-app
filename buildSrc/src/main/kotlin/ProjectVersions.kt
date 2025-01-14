object ProjectVersions {
    const val APP_NAME = "Claudio"
    const val PACKAGE_NAME = "com.niji.claudio"
    const val PACKAGE_NAME_SHARED = "${PACKAGE_NAME}.common"
    const val PACKAGE_NAME_ANDROID = "${PACKAGE_NAME}.android"
    private const val APP_VERSION_CODE = 99999999
    private const val APP_VERSION_NAME = "1.0.0"
    private const val GITHUB_REF_NAME = "GITHUB_REF_NAME"
    private const val GITHUB_RUN_NUMBER = "GITHUB_RUN_NUMBER"

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
