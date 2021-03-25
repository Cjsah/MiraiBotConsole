plugins {
    kotlin("jvm") version "1.4.31"
    id("net.mamoe.kotlin-jvm-blocking-bridge") version "1.10.3"
    id("com.github.johnrengelman.shadow") version "6.1.0"
    maven
}

group = "net.cjsah.bot.console"
version = "0.2"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation(kotlin("stdlib"))
    val miraiVersion = "2.4.2"
    api("net.mamoe", "mirai-core-api", miraiVersion)
    runtimeOnly("net.mamoe", "mirai-core", miraiVersion)
    api("org.hydev:HyLogger:2.1.0.378")
    api("com.google.code.gson:gson:2.8.5")
    api("com.github.salomonbrys.kotson:kotson:2.5.0")
    api("com.github.HyDevelop:HyConfigLib:3.1.52")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

tasks.withType<Jar> {
    manifest {
        attributes(Pair("Main-Class", "net.cjsah.console.MainKt"))
    }
}