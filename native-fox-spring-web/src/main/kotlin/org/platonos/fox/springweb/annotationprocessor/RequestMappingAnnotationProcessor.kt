package org.platonos.fox.springweb.annotationprocessor

import org.platonos.fox.core.SupportedAnnotation
import org.platonos.fox.core.SupportedAnnotations
import org.platonos.fox.core.annotationprocessor.AnnotationProcessor
import org.platonos.fox.core.definition.RequestMappingDefinition

@SupportedAnnotations(
    values = [
        SupportedAnnotation("org.springframework.web.bind.annotation.GetMapping"),
        SupportedAnnotation("org.springframework.web.bind.annotation.RequestMapping")
    ]
)
class RequestMappingAnnotationProcessor: AnnotationProcessor {

    override fun process(annotationClassName: String,
                         attributes: MutableMap<String, Any?>): Any {
        val map = mutableMapOf<String, Any?>()
        map.putAll(attributes)

        if (map.containsKey("path").not()) {
            val value = map.remove("value")

            if (value != null) {
                map["path"] = value
            }
        }

        val method = resolveMethod(annotationClassName)

        if (method != null) {
            map["methods"] = listOf(method)
        }

        return RequestMappingDefinition(map)
    }

    private fun resolveMethod(annotationClassName: String): String? {
        return when(annotationClassName) {
            "org.springframework.web.bind.annotation.GetMapping" -> "get"
            else -> null
        }
    }

}