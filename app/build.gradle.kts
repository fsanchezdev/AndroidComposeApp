import com.android.build.api.variant.VariantOutputConfiguration
import java.util.Properties

plugins {
    id(libs.plugins.android.application.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    // TODO // id(libs.plugins.google.services.get().pluginId)
    // TODO // id(libs.plugins.google.firebase.crashlytics.get().pluginId)
    // TODO // id(libs.plugins.google.firebase.appDistribution.get().pluginId)
    // TODO // id(libs.plugins.playPublisher.get().pluginId)
    id(libs.plugins.kotlinter.get().pluginId)
    id(libs.plugins.dagger.hilt.get().pluginId)
    id(libs.plugins.google.ksp.get().pluginId)
}

android {
    namespace = libs.versions.nameSpace.get()
    // TODO: Uncomment after creating keystore.
    // signingConfigs {
    //     create("release") {
    //         storeFile = file("app.keystore")
    //         storePassword = System.getenv("STORE_PASSWORD")
    //         keyAlias = System.getenv("KEY_ALIAS")
    //         keyPassword = System.getenv("KEY_PASSWORD")
    //         enableV1Signing = true
    //         enableV2Signing = true
    //         enableV3Signing = true
    //         enableV4Signing = true
    //     }
    // }
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        applicationId = libs.versions.applicationId.get()
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        testInstrumentationRunnerArguments["clearPackageData"] = "true"
    }
    buildTypes {
        getByName("debug") {
            versionNameSuffix = "-DEBUG"
        }
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // TODO: Uncomment after creating keystore.
            // signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.java.sdk.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.java.sdk.get())
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidx.compose.compiler.get()
        useLiveLiterals = true
    }
    flavorDimensions += "mode"
    productFlavors {
        create("pre")
        create("prod")
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
    jvmToolchain(libs.versions.java.sdk.get().toInt())
    compilerOptions {
        allWarningsAsErrors.set(true)
    }
}

val versionProperties = Properties().apply {
    load(rootProject.file("version.properties").inputStream())
}

androidComponents {
    onVariants { variant ->
        variant.outputs.single {
            it.outputType == VariantOutputConfiguration.OutputType.SINGLE
        }.apply {

            versionCode.set(
                versionProperties.getProperty("BUILD_NUMBER").toIntOrNull() ?: 0
            )
            versionName.set(
                "${versionProperties.getProperty("VERSION_MAJOR").toIntOrNull() ?: 0}.${
                    versionProperties.getProperty("VERSION_MINOR").toIntOrNull() ?: 0
                }.${versionProperties.getProperty("BUILD_NUMBER").toIntOrNull() ?: 0}"
            )
        }
    }
}

// TODO Enable when ready to deploy to play store.
//
// play {
//     serviceAccountCredentials.set(file("gradle-play-publisher-key.json"))
//     defaultToAppBundles.set(true)
//     track.set("internal")
//     releaseStatus.set(ReleaseStatus.DRAFT)
// }

// TODO mirar tema para que este las buildvariasnts, mirar  retrofit

dependencies {
    implementation(projects.dataLayer)
    implementation(projects.domainLayer)
    implementation(projects.presentationLayer)
    implementation(libs.androidx.activityCompose)
    implementation(libs.androidx.lifecycleRuntime)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.bundles.androidx.compose)
    implementation(libs.google.daggerHiltAndroid)
    // debugImplementation(libs.square.leakcanary)
    implementation(platform(libs.compose.bom))
    // debugImplementation(libs.androidx.composeUiTestManifest)
    // debugImplementation(libs.androidx.composeUiTooling)
    ksp(libs.google.daggerHiltCompiler)
    testImplementation(libs.junit)

    // not required in app?
    androidTestImplementation(libs.androidx.composeUiTestJunit4)
    androidTestImplementation(libs.androidx.testExtJunit)
    androidTestImplementation(platform(libs.compose.bom))
    // TODO check what is needed to test
}
