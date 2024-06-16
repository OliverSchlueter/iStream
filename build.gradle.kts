plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

allprojects {
    group = "de.itbw18"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenLocal()
        mavenCentral()
    }
}

dependencies {
    implementation(project(":backend"))
}

tasks {
    shadowJar {
        archiveFileName.set("iStream.jar")
        archiveClassifier.set("")

        manifest {
            manifest {
                attributes["Main-Class"] = "de.itbw18.istream.cmd.Main"
            }
        }
    }
}

tasks.register<Copy>("copyFrontendToBackend") {
    dependsOn("frontend:npm_run_build")
    file("$rootDir/backend/src/main/resources/public").mkdirs()

    from("$rootDir/frontend/dist/frontend/browser")
    into("$rootDir/backend/src/main/resources/public")
}
