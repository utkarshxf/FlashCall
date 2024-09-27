  plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hilt)
    id("kotlin-kapt")
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
}

android {
    namespace = "com.example.myapplication.myapplication.flashcall"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myapplication.myapplication.flashcall"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isCrunchPngs = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.crashlytics)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.core.splashscreen)
    implementation(libs.dagger.hilt)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    implementation(libs.ohteepee)

    implementation(libs.coroutine.core)
    implementation(libs.coroutine.android)
    implementation(libs.compose.runtime.lifecycle)
    implementation(libs.retrofit)
    implementation(libs.moshi)
    implementation(libs.moshi.converter)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp.logger)
    implementation(libs.okhttp)

    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation ("io.github.vanpra.compose-material-dialogs:datetime:0.9.0")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

    implementation("io.coil-kt:coil-compose:2.6.0")

    implementation("com.maxkeppeler.sheets-compose-dialogs:core:1.3.0")
    implementation ("com.maxkeppeler.sheets-compose-dialogs:calendar:1.3.0")

    implementation ("co.hyperverge:hyperkyc:0.13.0")

    implementation ("androidx.activity:activity-ktx:1.9.0")
    implementation ("androidx.fragment:fragment-ktx:1.7.1")

    implementation("io.getstream:stream-video-android-ui-compose:0.5.8")
    implementation ("io.getstream:stream-video-android-core:0.5.8")

    implementation(libs.sealedx.core)
    kapt(libs.sealedx.processor)
    implementation(libs.accompanist.permissions)
    implementation(libs.preference.datastore)

    implementation("androidx.media3:media3-exoplayer:1.3.1")
    implementation("androidx.media3:media3-ui:1.3.1")
//    implementation("androidx.compose.material:material-icons-extended:1.3.0-beta04")
    implementation("androidx.compose.material:material-icons-extended:1.5.0")
    implementation ("androidx.compose.material3:material3:1.0.1") // Check latest version on Google's site


    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation("com.google.firebase:firebase-analytics")

    implementation("com.google.accompanist:accompanist-insets:0.28.0")




//    implementation("com.google.accompanist:accompanist-insets-ui.0.24.7-alpha")
}