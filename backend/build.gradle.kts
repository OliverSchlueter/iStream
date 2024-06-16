plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

dependencies {
    val javalinVersion = "6.1.6"

    implementation("io.javalin:javalin:$javalinVersion")
    annotationProcessor("io.javalin.community.openapi:openapi-annotation-processor:$javalinVersion")
    implementation("io.javalin.community.openapi:javalin-openapi-plugin:$javalinVersion")
    implementation("io.javalin.community.openapi:javalin-swagger-plugin:$javalinVersion")

    implementation("org.xerial:sqlite-jdbc:3.45.3.0")

    implementation("org.slf4j:slf4j-simple:2.0.10")
    implementation("com.google.code.gson:gson:2.10.1")

    testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("io.javalin:javalin-testtools:$javalinVersion")
}

tasks {
    shadowJar {
        archiveFileName.set("iStream.jar")

        manifest {
            attributes["Main-Class"] = "de.itbw18.istream.cmd.Main"
        }
    }

    test {
        useJUnitPlatform()
        maxHeapSize = "1G"
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()

        options.release.set(21)
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}