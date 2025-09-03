import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.kotlin.compose)
	alias(libs.plugins.ktlint)
	alias(libs.plugins.hilt)
	alias(libs.plugins.ksp)
	alias(libs.plugins.serialization)
	alias(libs.plugins.kapt)
}

android {
	namespace = "com.superapps.cats"
	compileSdk = 36

	defaultConfig {
		minSdk = 24

		testInstrumentationRunner = "com.superapps.cats.utils.CatsTestRunner"
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
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
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
	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.appcompat)

	implementation(libs.androidx.activity.compose)
	implementation(libs.navigation)
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.ui)
	implementation(libs.androidx.ui.graphics)
	implementation(libs.androidx.ui.tooling.preview)
	implementation(libs.androidx.material3)

	implementation(libs.coil)
	implementation(libs.coil.network)

	implementation(libs.hilt)
	implementation(libs.hilt.navigation)
	kapt(libs.hilt.compiler)

	implementation(libs.paging)
	implementation(libs.paging.compose)
	implementation(libs.paging.common)

	implementation(libs.jakewharton.timber)

	testImplementation(libs.truth)
	testImplementation(libs.mockito)
	testImplementation(libs.mockito.kotlin)
	testImplementation(libs.turbine)
	testImplementation(libs.junit)
	testImplementation(libs.kotlinx.coroutines.test)

	debugImplementation(libs.ui.test.manifest)
	debugImplementation(libs.androidx.ui.tooling)

	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)
	androidTestImplementation(libs.ui.test.junit4)
	androidTestImplementation(libs.androidx.navigation.testing)
	androidTestImplementation(libs.hilt.android.testing)
	kaptAndroidTest(libs.hilt.compiler)
}
