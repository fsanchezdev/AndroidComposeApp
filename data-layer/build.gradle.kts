plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("conventions.android")
}

android {
    namespace = libs.versions.nameSpaceData.get()
    compileSdk = 34

    defaultConfig {
        minSdk = 29
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
    flavorDimensions += "mode"
    productFlavors {
        create("pre") {
            buildConfigField("String", "BASE_URL", "\"https://picsum.photos/\"")
        }
        create("prod") {
            buildConfigField("String", "BASE_URL", "\"https://picsum.photos/\"")
        }
    }
    buildFeatures {
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.java.sdk.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.java.sdk.get())
    }
    kotlinOptions {
        jvmTarget = libs.versions.java.sdk.get()
    }
}

dependencies {
    implementation(projects.domainLayer)
    implementation(libs.appcompat)
    // implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.testExtJunit)
    implementation(libs.arrow.core)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.serialization)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.logging)
}
