package org.platonos.fox.core

import org.platonos.fox.core.annotationprocessor.AnnotationProcessor
import java.util.*

object AnnotationProcessorRegistry {

    private val annotationProcessorMap = mutableMapOf<String, AnnotationProcessor>()

    init {
        val loader = ServiceLoader.load(AnnotationProcessor::class.java)

        loader.iterator().forEach { annotationProcessor ->
            annotationProcessor::class.annotations.forEach { annotation ->
                if (annotation.annotationClass == SupportedAnnotation::class) {
                    val supportedAnnotation = annotation as SupportedAnnotation
                    processSupportedAnnotation(supportedAnnotation, annotationProcessor)
                } else if (annotation.annotationClass == SupportedAnnotations::class) {
                    val supportedAnnotations = annotation as SupportedAnnotations
                    supportedAnnotations.values.forEach { supportedAnnotation ->
                        processSupportedAnnotation(supportedAnnotation, annotationProcessor)
                    }
                }
            }
        }
    }

    private fun processSupportedAnnotation(supportedAnnotation: SupportedAnnotation,
                                           annotationProcessor: AnnotationProcessor) {
        if (annotationProcessorMap.containsKey(supportedAnnotation.className).not()) {
            annotationProcessorMap[supportedAnnotation.className] = annotationProcessor
        }
    }

    fun getAnnotationProcessorFor(className: String): AnnotationProcessor? {
        return annotationProcessorMap[className]
    }

}