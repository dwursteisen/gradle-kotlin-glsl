plugins {
    `java-library`
    `maven-publish`
    kotlin("jvm") version "1.3.70"
    id("java-gradle-plugin")
}

repositories {
    maven {
        url = uri("https://dl.bintray.com/dwursteisen/minigdx")
        content {
            includeGroup("com.salakheev")
            includeGroup("com.dwursteisen")
        }
    }
    mavenCentral()
    jcenter()
}

group = "com.github.dwursteisen.gradle.glsl"
version = project.properties["version"] ?: "1.0-SNAPSHOT"

if (version == "unspecified") {
    version = "1.0-SNAPSHOT"
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

dependencies {
    api(gradleApi())
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.salakheev:kotlin-glsl:1.1")

    testImplementation(gradleTestKit())
    testImplementation("junit:junit:4.12")
}


configure<GradlePluginDevelopmentExtension> {
    this.plugins {
        create("kotlin-glsl-gradle-plugin") {
            this.id = "com.github.dwursteisen.gradle.glsl.kotlin-glsl"
            this.implementationClass = "com.github.dwursteisen.gradle.glsl.GlslPlugin"
        }
    }
}

