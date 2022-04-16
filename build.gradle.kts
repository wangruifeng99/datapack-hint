plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.5.2"
}

group = "org.intellij.sdk"
version = "2.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("junit:junit:4.13.2")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version.set("2021.2.4")
    plugins.set(listOf("com.intellij.java"))
}

tasks {
    buildSearchableOptions {
        enabled = false
    }

    patchPluginXml {
        version.set("${project.version}")
        sinceBuild.set("211")
        untilBuild.set("213.*")
    }

    test {
        // Set idea.home.path to the absolute path to the intellij-community source
        // on your local machine.
        systemProperty("idea.home.path", "/Users/jhake/Documents/source/comm")
    }
}