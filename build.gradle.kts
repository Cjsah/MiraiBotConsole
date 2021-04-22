plugins {
    kotlin("jvm") version "1.4.31"
    id("com.github.johnrengelman.shadow") version "6.1.0"
    maven
}

group = "net.cjsah.bot.console"
version = "1.4"
val miraiCoreVersion = "2.6.2"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation(kotlin("stdlib"))
    api("net.mamoe", "mirai-core-api", miraiCoreVersion)
    runtimeOnly("net.mamoe", "mirai-core", miraiCoreVersion)
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
        attributes(Pair("Main-Class", "net.cjsah.bot.console.MainKt"))
    }
}

sourceSets {
    getting {
        plugins {
            id("net.mamoe.kotlin-jvm-blocking-bridge") version "1.10.3"
        }

        dependencies {
            api("org.hydev:HyLogger:2.1.0.378")
            api("com.google.code.gson:gson:2.8.5")
            api("com.github.salomonbrys.kotson:kotson:2.5.0")
            api("com.github.HyDevelop:HyConfigLib:3.1.52")
        }
    }
}