plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id(libs.plugins.dagger.hilt.get().pluginId)
    id("conventions.android")
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = libs.versions.nameSpacePresentation.get()
    compileSdk = 34

    defaultConfig {
        minSdk = 29
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    // TODO add dokka to documentate app
    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.java.sdk.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.java.sdk.get())
    }
    kotlinOptions {
        jvmTarget = libs.versions.java.sdk.get()
    }
}

hilt {
    enableAggregatingTask = true
}

dependencies {
    implementation(projects.domainLayer)
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    // implementation(libs.material)
    testImplementation(libs.junit)
    // androidTestImplementation(libs.androidx.testextjunit)
    // androidTestImplementation(libs.androidx.testEspressoCore)
    testImplementation(libs.square.turbine)
    implementation(libs.androidx.coreSplashscreen)
    implementation(libs.google.daggerHiltAndroid)
}
