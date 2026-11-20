plugins {
    java
    application
    jacoco
}

group = "de.reilo"
version = "1.0.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(libs.versions.java.get())
    }
}

repositories {
    mavenCentral()
}

application {
    mainClass = "de.reilo.countdown.Main"
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter(libs.versions.junit.get())
        }
    }
}

jacoco {
    toolVersion = libs.versions.jacoco.get()
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = true
        html.required = true
    }
    classDirectories.setFrom(
        files(classDirectories.files.map {
            fileTree(it) {
                exclude("de/reilo/countdown/Main.class")
            }
        })
    )
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "de.reilo.countdown.Main"
    }
}
