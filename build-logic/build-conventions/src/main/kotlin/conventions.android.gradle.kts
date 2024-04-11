/*
 * Kotlin (Android) library module (convention plugin).
 */

val libs: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jmailen.kotlinter")
    id("com.google.devtools.ksp")
}

android {
    compileSdk = libs.findVersion("android.compileSdk").get().toString().toInt()
    defaultConfig {
        minSdk = libs.findVersion("android.minSdk").get().toString().toInt()
        testInstrumentationRunnerArguments["clearPackageData"] = "true"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.findVersion("java.sdk").get())
        targetCompatibility = JavaVersion.toVersion(libs.findVersion("java.sdk").get())
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.findVersion(
            "androidx.compose.compiler"
        ).get().toString()
        useLiveLiterals = true
    }
    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
        animationsDisabled = true
    }
    lint {
        warningsAsErrors = true
        disable += "GradleDependency"
    }
}

kotlin {
    explicitApi()
    jvmToolchain(libs.findVersion("java.sdk").get().toString().toInt())
    compilerOptions {
        allWarningsAsErrors.set(true)
        freeCompilerArgs.addAll(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=${project.layout.buildDirectory.get().asFile.absolutePath}/compose_metrics",
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=${project.layout.buildDirectory.get().asFile.absolutePath}/compose_reports"
        )
    }
}

dependencies {
    implementation(libs.findBundle("androidx.compose").get())
    implementation(libs.findLibrary("androidx.activityCompose").get())
    implementation(libs.findLibrary("androidx.hiltNavCompose").get())
    implementation(libs.findLibrary("androidx.lifecycleRuntimeCompose").get())
    implementation(libs.findLibrary("androidx.lifecycleViewModelCompose").get())
    implementation(libs.findLibrary("androidx.navigation.compose").get())
    implementation(libs.findLibrary("google.daggerHiltAndroid").get())
    implementation(libs.findLibrary("kotlinx.coroutinesAndroid").get())
    implementation(platform(libs.findLibrary("compose.bom").get()))
    debugImplementation(libs.findLibrary("androidx-composeUiTestManifest").get())
    debugImplementation(libs.findLibrary("androidx.composeUiTooling").get())
    ksp(libs.findLibrary("google.daggerHiltCompiler").get())
    testImplementation(libs.findLibrary("androidx.testExtJunit").get())
    testImplementation(libs.findLibrary("junit").get())
    testImplementation(libs.findLibrary("kotlin.test").get())
    testImplementation(libs.findLibrary("kotlin.test.junit").get())
    testImplementation(libs.findLibrary("kotlinx.coroutinesTest").get())
    androidTestImplementation(libs.findLibrary("androidx.composeUiTestJunit4").get())
    androidTestImplementation(libs.findLibrary("androidx.testExtJunit").get())
    androidTestImplementation(libs.findLibrary("androidx.testRules").get())
    androidTestImplementation(libs.findLibrary("androidx.testRunner").get())
    androidTestImplementation(libs.findLibrary("junit").get())
    androidTestImplementation(libs.findLibrary("kotlin.test").get())
    // androidTestImplementation(libs.findLibrary("kotlin.test.junit").get())
    // androidTestImplementation(libs.findLibrary("kotlinx.coroutinesTest").get())
    // androidTestUtil(libs.findLibrary("androidx.testOrchestrator").get())
    implementation(libs.findLibrary("arrow.core").get())
    testImplementation(libs.findLibrary("mockk").get())
    androidTestImplementation(libs.findLibrary("mockkAndroid").get())
    androidTestImplementation(libs.findLibrary("mockkAgent").get())
}
