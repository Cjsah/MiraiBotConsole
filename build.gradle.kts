plugins {
    kotlin("jvm") version "1.4.31"
    id("com.github.johnrengelman.shadow") version "6.1.0"
    maven
}

group = "net.cjsah.bot.console"
version = "1.9"
val miraiCoreVersion = "2.6.4"

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

//(tasks.getByName("processResources") as ProcessResources).apply {
//    exclude("**/*.*")
//}

tasks.withType<Jar> {
    from("LICENSE")
//    var classPath = ""
//    configurations.runtimeClasspath.get().forEach { classPath += "libs/${it.name} " }
//
    manifest {
        attributes(Pair("Main-Class", "net.cjsah.bot.console.MainKt"))
//        attributes(Pair("Class-Path", classPath.subSequence(0, classPath.length-1)))
//    }
//    into("libs") {
//        from(configurations.runtimeClasspath.get()/*.map { if (it.isDirectory) it else zipTree(it) }*/)
    }
}

task("rundir") {
    val f = file("run")
    if (!f.exists()) f.mkdirs()
}