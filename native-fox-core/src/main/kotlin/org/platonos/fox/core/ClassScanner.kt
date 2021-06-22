package org.platonos.fox.core;

import org.objectweb.asm.*
import org.objectweb.asm.signature.SignatureReader
import org.platonos.fox.core.definition.ApiOperationDefinition
import org.platonos.fox.core.definition.ControllerDefinition
import org.platonos.fox.core.reader.AnnotationReader
import org.platonos.fox.core.reader.ControllerInfo
import org.platonos.fox.core.reader.MethodInfo
import org.platonos.fox.core.reader.MethodReader
import org.platonos.fox.core.type.JType
import java.io.InputStream

class ClassScanner private constructor() : ClassVisitor(Opcodes.ASM9), Visitor {

    private var controllerInfo: ControllerInfo? = null
    private var apiOperationDefinitions = mutableListOf<ApiOperationDefinition>()

    companion object {
        fun getInstance(): ClassScanner {
            return ClassScanner()
        }
    }

    fun scan(inputStream: InputStream, qualififedClassName: String): ControllerDefinition? {
        val reader = ClassReader(inputStream)
        reader.accept(this, ClassReader.SKIP_CODE)

        val info = controllerInfo

        if (info != null) {
            return ControllerDefinition(
                qualififedClassName,
                getSimpleName(qualififedClassName),
                info.attributes,
                apiOperationDefinitions
            )
        } else {
            return null
        }
    }

    private fun getSimpleName(name: String): String {
        val sep = name.lastIndexOf('.')
        return if (sep < 0) name else name.substring(sep + 1)
    }

    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
        val className = StringUtils.descriptorToClassName(descriptor!!)

        return AnnotationReader(
            api = api,
            annotationClassName = className,
            parent = this
        )
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?): MethodVisitor? {

        if (controllerInfo != null) {
            val visitor = SignatureVisitorImpl(api)
            val returnType: JType

            if (signature != null) {
                val reader = SignatureReader(signature)
                reader.accept(visitor)
                returnType = visitor.returnType!!
            } else {
                val methodType = MethodTypeParser.parse(Type.getMethodType(descriptor))
                returnType = methodType.returnType
            }

            val methodInfo = MethodInfo(name!!, returnType)

            return MethodReader(
                api = api,
                methodInfo = methodInfo,
                parent = this
            )
        } else {
            return null
        }
    }

    override fun pushControllerInfo(controllerInfo: ControllerInfo) {
        this.controllerInfo = controllerInfo
    }

    override fun pushApiOperation(apiOperationDefinition: ApiOperationDefinition) {
        apiOperationDefinitions.add(apiOperationDefinition)
    }
}
