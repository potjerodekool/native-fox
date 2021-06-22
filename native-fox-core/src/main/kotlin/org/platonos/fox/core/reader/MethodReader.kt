package org.platonos.fox.core.reader

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.MethodVisitor
import org.platonos.fox.core.ClassScanner
import org.platonos.fox.core.StringUtils
import org.platonos.fox.core.Visitor
import org.platonos.fox.core.definition.ApiOperationDefinition
import org.platonos.fox.core.definition.RequestMappingDefinition

class MethodReader(api: Int,
                   private val methodInfo: MethodInfo,
                   private val parent: ClassScanner
) : MethodVisitor(api), Visitor {

    private var requestMappingDefinition: RequestMappingDefinition? = null

    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
        val annotationClassName = StringUtils.descriptorToClassName(descriptor!!)

        return AnnotationReader(
            api = api,
            annotationClassName = annotationClassName,
            attributeName = null,
            parent = this
        )
    }

    override fun pushValue(attributeName: String, value: Any?) {
        if (value is RequestMappingDefinition) {
            this.requestMappingDefinition = value
        } else {
            TODO()
        }
    }

    override fun visitEnd() {
        val requestMappingDef = requestMappingDefinition

        if (requestMappingDef != null) {
            val apiOperation = ApiOperationDefinition(
                methodInfo.name,
                methodInfo.returnJType,
                requestMappingDef
            )
            parent.pushApiOperation(apiOperation)
        }
        super.visitEnd()
    }
}