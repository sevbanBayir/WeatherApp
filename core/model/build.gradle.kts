plugins {
    id(libs.plugins.java.library.get().pluginId)
    alias(libs.plugins.org.jetbrains.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
//    toolchain.languageVersion.set(JavaLanguageVersion.of(11))
}