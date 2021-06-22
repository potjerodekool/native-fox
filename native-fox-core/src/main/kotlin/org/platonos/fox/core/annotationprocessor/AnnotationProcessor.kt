package org.platonos.fox.core.annotationprocessor

interface AnnotationProcessor {

    fun process(annotationClassName: String,
                attributes: MutableMap<String, Any?>): Any?

}