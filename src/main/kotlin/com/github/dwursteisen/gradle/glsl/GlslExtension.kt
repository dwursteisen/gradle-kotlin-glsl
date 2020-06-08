package com.github.dwursteisen.gradle.glsl

import org.gradle.api.model.ObjectFactory
import javax.inject.Inject


open class GlslExtension @Inject constructor(val name: String, objectFactory: ObjectFactory) {

    val output = objectFactory.directoryProperty()
}
