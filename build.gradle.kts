plugins {
    kotlin("jvm") version "1.4.31"
    id("net.mamoe.kotlin-jvm-blocking-bridge") version "1.10.3"
}

group = "net.cjsah"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    val miraiVersion = "2.4.2"
    api("net.mamoe", "mirai-core-api", miraiVersion)
    runtimeOnly("net.mamoe", "mirai-core", miraiVersion)
    implementation("org.hydev:HyLogger:2.1.0.378")
    implementation("com.google.code.gson:gson:2.8.5")
    implementation("com.github.salomonbrys.kotson:kotson:2.5.0")
    implementation("com.github.HyDevelop:HyConfigLib:3.1.52")
}
