plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "de.itbw18"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.javalin:javalin:6.1.3")
    annotationProcessor("io.javalin.community.openapi:openapi-annotation-processor:6.1.3")
    implementation("io.javalin.community.openapi:javalin-openapi-plugin:6.1.3")
    implementation("io.javalin.community.openapi:javalin-swagger-plugin:6.1.3")

    implementation("org.slf4j:slf4j-simple:2.0.10")
    implementation("com.google.code.gson:gson:2.10.1")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.shadowJar {
    archiveClassifier.set("")

    manifest {
        manifest {
            attributes["Main-Class"] = "de.itbw18.istream.cmd.Main"
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<Exec>("npmBuild") {
    workingDir = file("$rootDir/frontend")

    commandLine = if (System.getProperty("os.name").lowercase().contains("win")) {
        listOf("$rootDir/frontend/build.bat")
    } else {
        listOf("bash", "$rootDir/frontend/build.sh")
    }
}

tasks.register<Copy>("copyFrontendToBackend") {
    dependsOn("npmBuild")
    from("$rootDir/frontend/dist")
    into("$rootDir/src/main/resources/frontend")

    doLast {
        println("Copying files from: $rootDir/frontend/dist")
        println("Copying files to: $rootDir/src/main/resources/frontend")
        file("$rootDir/src/main/resources/frontend").mkdirs()
    }
}

tasks.compileJava {
    options.encoding = Charsets.UTF_8.name()

    options.release.set(17)
}