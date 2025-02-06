plugins {
    id(libs.plugins.java.library.get().pluginId)
    alias(libs.plugins.org.jetbrains.kotlin.jvm)
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "18"
    }
}
java {
    sourceCompatibility = JavaVersion.VERSION_18
    targetCompatibility = JavaVersion.VERSION_18
}
java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(18))
}