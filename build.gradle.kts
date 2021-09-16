plugins {
    kotlin("jvm") version "1.5.10"
    id("com.github.johnrengelman.shadow") version "6.1.0"
    id("maven-publish")
}

group = "net.cjsah.bot.console"
version = "1.11"
val ENV: MutableMap<String, String> = System.getenv()
val build_number = project.properties["build_number"]
if (ENV.containsKey("BUILD_NUMBER")) version = "$version+build.${ENV["BUILD_NUMBER"]}"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation(kotlin("stdlib"))
    api("net.mamoe", "mirai-core", "2.7.0")
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

task("makedir") {
    val f = file("run")
    if (!f.exists()) f.mkdirs()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group as String
            artifactId = rootProject.name
            version = version
            from(components["java"])
        }
    }

    if (ENV.containsKey("MAVEN_URL")) {
        repositories.maven {
            url = uri(ENV["MAVEN_URL"] as String)
            credentials {
                username = ENV["MAVEN_USERNAME"]
                password = ENV["MAVEN_PASSWORD"]
            }
        }
    }

}
