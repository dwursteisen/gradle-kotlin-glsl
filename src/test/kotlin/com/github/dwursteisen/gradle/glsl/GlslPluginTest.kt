package com.github.dwursteisen.gradle.glsl

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class GlslPluginTest {

    @Rule
    @JvmField
    val temporaryFolder = TemporaryFolder()

    @Test
    fun `apply | it apply default glsl plugin`() {
        val buildFile = temporaryFolder.newFile("build.gradle.kts")
        buildFile.writeText(
            """
            plugins {
                kotlin("jvm") version "1.3.70"
                id("com.github.dwursteisen.gradle.glsl.kotlin-glsl")
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
            
            project.extensions.configure<NamedDomainObjectContainer<com.github.dwursteisen.gradle.glsl.GlslExtension>>("glsl") {
                this.create("glsl") {
                    this.output.set(project.buildDir.resolve("glsl"))
                }
            }
        """.trimIndent()
        )

        generateShaderClass()

        val result = GradleRunner.create()
            .withProjectDir(temporaryFolder.root)
            .withArguments(":glsl")
            .withPluginClasspath()
            .forwardOutput()
            .build()

        assertEquals(TaskOutcome.SUCCESS, result.task(":glsl")?.outcome)
        assertTrue(temporaryFolder.root.resolve("build")
            .resolve("glsl")
            .resolve("Shader.glsl")
            .exists()
        )
    }

    private fun generateShaderClass() {
        val folder = temporaryFolder.newFolder("src", "glsl", "kotlin")

        File(GlslPlugin::class.java.getResource("/MyShader.kt").toURI()).copyTo(folder.resolve("MyShader.kt"))
    }
}
