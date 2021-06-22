package org.platonos.fox.springweb.annotationprocessor

import org.platonos.fox.core.SupportedAnnotation
import org.platonos.fox.core.annotationprocessor.AnnotationProcessor
import org.platonos.fox.core.reader.ControllerInfo

@SupportedAnnotation(className = "org.springframework.stereotype.Controller")
class ControllerAnnotationProcessor: AnnotationProcessor {

    override fun process(annotationClassName: String,
                         attributes: MutableMap<String, Any?>): Any {
        return ControllerInfo(attributes.toMap())
    }
}