package org.platonos.fox.core

import org.objectweb.asm.Type
import org.platonos.fox.core.type.*

object MethodTypeParser {

    fun parse(methodType: Type): MethodType {
        val returnType = parseType(methodType.returnType)
        return MethodType(returnType)
    }

    private fun parseType(type: Type): JType {
        return when(type.sort) {
            Type.VOID -> JVoidType.INSTANCE
            Type.BOOLEAN -> JPrimitiveType.BOOLEAN
            Type.OBJECT -> JDeclaredType(type.className)
            else -> TODO()
        }
    }
}