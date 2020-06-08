package com.github.dwursteisen.gradle.glsl

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.SourceSetContainer

class GlslPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val exts = project.container(GlslExtension::class.java)
        project.extensions.add("glsl", exts)

        // Configure Kotlin
        if (!project.plugins.hasPlugin("org.jetbrains.kotlin.jvm")) {
            project.plugins.apply("org.jetbrains.kotlin.jvm")
        }

        // Create main glsl task
        val glsl = project.tasks.register("glsl")

        // Create glsl source set.  Source directory could be configured by the extension.
        val sourceSets = project.extensions.getByName("sourceSets") as SourceSetContainer
        exts.all { ext ->
            val sourceSet = sourceSets.register(ext.name) {
                it.java {
                    it.srcDir("src/${ext.name}/kotlin/")
                }
            }

            // Add kotlin-glsl in kapt classpath
            project.dependencies.add("${ext.name}Implementation", "com.salakheev:kotlin-glsl:1.1")
            project.dependencies.add("${ext.name}Implementation", "org.jetbrains.kotlin:kotlin-stdlib")

            val task = project.tasks.register("${ext.name}GenerateGlsl", JavaExec::class.java) { exec ->
                if (ext.output.isPresent) {
                    project.mkdir(ext.output.get().asFile)
                }

                exec.classpath = sourceSet.get().runtimeClasspath
                exec.main = "com.salakheev.shaderbuilderkt.ShaderBuilderConverter"
                exec.args = listOf(ext.output.get().asFile.absolutePath)
            }
            glsl.configure {
                it.dependsOn(task.get())
            }
        }
    }
}
