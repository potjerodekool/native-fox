package org.platonos.fox.core.reader

import org.platonos.fox.core.AnnotationProcessorRegistry
import org.platonos.fox.core.Visitor
import org.platonos.fox.core.definition.RequestMappingDefinition

class AnnotationReader(
    api: Int,
    annotationClassName: String? = null,
    attributeName: String? = null,
    parent: Visitor? = null,
) : AbstractAnnotationReader(
    api = api,
    annotationClassName = annotationClassName,
    attributeName = attributeName,
    parent = parent) {

    override fun visitEnd() {
        if (annotationClassName != null) {
            val annotationProcessor = AnnotationProcessorRegistry.getAnnotationProcessorFor(annotationClassName)

            if (annotationProcessor != null) {
                val result = annotationProcessor.process(annotationClassName, attributes)

                if (parent != null) {
                    if (result is ControllerInfo) {
                        parent.pushControllerInfo(result)
                    } else if (result is RequestMappingDefinition) {
                        parent.pushValue(annotationClassName, result)
                    }
                }
            }
        }
    }
}