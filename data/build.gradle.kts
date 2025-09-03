import java.util.Properties
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.ktlint)
	alias(libs.plugins.hilt)
	alias(libs.plugins.ksp)
	alias(libs.plugins.serialization)
	alias(libs.plugins.kapt)
}

android {
	namespace = "com.superapps.data"
	compileSdk = 36

	defaultConfig {
		minSdk = 24

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		consumerProguardFiles("consumer-rules.pro")

		val localProperties = Properties()
		val localPropertiesFile = rootProject.file("local.properties")
		if (localPropertiesFile.exists()) {
			localProperties.load(localPropertiesFile.inputStream())
		}

		buildConfigField("String", "CAT_API_KEY", "\"${localProperties.getProperty("CAT_API_KEY", "")}\"")
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
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}

	buildFeatures {
		buildConfig = true
	}
}

kotlin {
	compilerOptions {
		jvmTarget = JvmTarget.JVM_11
	}
}

dependencies {
	implementation(project(":domain"))
	implementation(project(":common"))
	implementation(libs.hilt)
	kapt(libs.hilt.compiler)

	implementation(libs.room)
	implementation(libs.room.ktx)
	implementation(libs.room.paging)
	implementation(libs.serialization)
	ksp(libs.room.compiler)

	implementation(libs.retrofit)
	implementation(libs.serialization.converter)

	implementation(libs.paging)
	implementation(libs.paging.compose)
	implementation(libs.paging.common)
	testImplementation(libs.paging.testing)

	implementation(libs.jakewharton.timber)

	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.appcompat)
	implementation(libs.material)

	testImplementation(libs.truth)
	testImplementation(libs.mockito)
	testImplementation(libs.mockito.kotlin)
	testImplementation(libs.turbine)
	testImplementation(libs.junit)
	testImplementation(libs.kotlinx.coroutines.test)

	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)
	androidTestImplementation(libs.kotlinx.coroutines.test)
	androidTestImplementation(libs.truth)
	androidTestImplementation(libs.hilt.android.testing)
	kaptAndroidTest(libs.hilt.compiler)
}
