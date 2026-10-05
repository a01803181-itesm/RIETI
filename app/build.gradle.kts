import java.util.Properties
import kotlin.text.replace

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "itesm.rieti"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "itesm.rieti"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        manifestPlaceholders["MAPS_API_KEY"] = localProperties.getProperty("MAPS_API_KEY") ?: ""
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
}

tasks.register("generateAmplifyConfig") {
    description = "Generate amplifyconfiguration.json"

    val templateFile = layout.projectDirectory.file("src/main/res/raw/amplifyconfiguration_template.json").asFile
    val outputFile = layout.projectDirectory.file("src/main/res/raw/amplifyconfiguration.json").asFile
    val localPropFile = layout.projectDirectory.file("../local.properties").asFile

    inputs.file(templateFile)
    inputs.file(localPropFile).optional()
    outputs.file(outputFile)

    doLast {
        val props = Properties()
        if (localPropFile.exists()) {
            localPropFile.inputStream().use { stream ->
                props.load(stream)
            }
        }

        if (templateFile.exists()) {
            var content = templateFile.readText()

            content = content.replace("\${COGNITO_POOL_ID}", props.getProperty("COGNITO_POOL_ID", ""))
            content = content.replace("\${COGNITO_APP_CLIENT_ID}", props.getProperty("COGNITO_APP_CLIENT_ID", ""))
            content = content.replace("\${COGNITO_DOMAIN}", props.getProperty("COGNITO_DOMAIN", ""))
            content = content.replace("\${COGNITO_REGION}", props.getProperty("COGNITO_REGION", ""))
            content = content.replace("\${COGNITO_SIGN_IN_URI}", props.getProperty("COGNITO_SIGN_IN_URI", ""))
            content = content.replace("\${COGNITO_SIGN_OUT_URI}", props.getProperty("COGNITO_SIGN_OUT_URI", ""))

            outputFile.writeText(content)
            println("Amplify configuration generated successfully in raw/amplifyconfiguration.json")
        }
    }
}

tasks.named("preBuild") {
    dependsOn("generateAmplifyConfig")
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    implementation(libs.maps.compose)
    implementation(libs.play.services.maps)

    implementation(libs.retrofit)
    implementation(libs.gson)

    implementation(libs.aws.auth.cognito)
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    implementation(libs.google.play.services.location)
    debugImplementation(libs.androidx.ui.test.manifest)

    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
