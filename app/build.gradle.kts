plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    //id("kotlin-kapt")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.forsythe.smartpoultry"
   // compileSdk = 34

    defaultConfig {
        applicationId = "com.forsythe.smartpoultry"
       // minSdk = 24
        //targetSdk = 34
        versionCode = 6
        versionName = "1.2.0"

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    /*compileOptions {
        //isCoreLibraryDesugaringEnabled = true
        //sourceCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        //targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "17"
    }*/

    buildFeatures {
        compose = true
    }

    /*composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"

        }
    }*/
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    //firebase
    implementation (platform("com.google.firebase:firebase-bom:33.4.0"))
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-functions")
    implementation("com.google.firebase:firebase-crashlytics-ktx")


    //viewModel for compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.test.ext:junit-ktx:1.2.1")
    testImplementation("org.testng:testng:6.9.6")
    androidTestImplementation("junit:junit:4.12")


    //Vico Charts
    val vicoVersion = "1.14.0"
    // For Jetpack Compose.
    implementation("com.patrykandpatrick.vico:compose:$vicoVersion")
    // For `compose`. Creates a `ChartStyle` based on an M2 Material Theme.
    //implementation("com.patrykandpatrick.vico:compose-m2:1.13.1")
    // For `compose`. Creates a `ChartStyle` based on an M3 Material Theme.
    implementation("com.patrykandpatrick.vico:compose-m3:$vicoVersion")
    // Houses the core logic for charts and other elements. Included in all other modules.
    implementation("com.patrykandpatrick.vico:core:$vicoVersion")

    //date picker library
    implementation("io.github.vanpra.compose-material-dialogs:datetime:0.9.0")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

    //Compose destinations "Ramcosta"
    val composeDestinationsVersion = "1.10.0"
    implementation("io.github.raamcosta.compose-destinations:core:$composeDestinationsVersion")
    ksp("io.github.raamcosta.compose-destinations:ksp:$composeDestinationsVersion")

    //Hilt dependency injection
    implementation("com.google.dagger:hilt-android:2.50")
    ksp("com.google.dagger:hilt-android-compiler:2.50")
    implementation ("androidx.hilt:hilt-navigation-compose:1.1.0") // For hiltViewModel()
    ksp ("androidx.hilt:hilt-compiler:1.1.0") // Or a newer stable version
    implementation ("androidx.hilt:hilt-work:1.2.0") // for work manager


    //Room database
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
   // annotationProcessor("androidx.room:room-compiler:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    implementation("androidx.room:room-paging:$roomVersion")


    //navigation compose
    implementation("androidx.navigation:navigation-compose:2.7.6")

    //work Manager
    //val workVersion = "2.9.0"
    implementation("androidx.work:work-runtime-ktx:2.9.0")

    //Datastore -Preferences
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    //SplashScreen api
    implementation("androidx.core:core-splashscreen:1.0.1")


    //Splash Screen
    implementation ("androidx.core:core-splashscreen:1.0.1")

    //Poi for exporting excel documents
    implementation("org.apache.poi:poi:5.3.0") {// For `.xls` files
       // exclude(group = "org.apache.logging.log4j", module="log4j-api") //removed coz it supports only aPIs 26

    }
    implementation("org.apache.poi:poi-ooxml:5.3.0"){// For `.xlsx` files
      //  exclude(group = "org.apache.logging.log4j", module="log4j-api") //removed coz it supports only aPIs 26
    }


    //Play Billing
    implementation("com.android.billingclient:billing-ktx:7.0.0")

    //ads
    implementation("com.google.android.gms:play-services-ads:23.4.0")

    //compose paging
    val paging_version = "3.3.2"
    implementation("androidx.paging:paging-runtime-ktx:$paging_version")
    implementation("androidx.paging:paging-compose:$paging_version")

}