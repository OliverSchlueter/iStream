plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
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

tasks{
    test {
        useJUnitPlatform()
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()

        options.release.set(17)
    }
}