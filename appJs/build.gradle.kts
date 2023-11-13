plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

val copyJsResources = tasks.create("copyJsResourcesWorkaround", Copy::class.java) {
    from(project(":common").file("src/commonMain/resources"))
    into("build/processedResources/js/main")
}

kotlin {
    js(IR) {
        browser()
        binaries.executable()
    }
    sourceSets {
        val jsMain by getting  {
            dependencies {
                implementation(project(":common"))
                implementation(compose.ui)
                implementation(compose.foundation)
            }
        }
    }
}

compose.experimental {
    web.application {}
}
