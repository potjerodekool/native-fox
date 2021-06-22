package org.platonos.fox.core.type

class JVoidType private constructor(): JType {

    companion object {
        val INSTANCE = JVoidType()
    }

    override val kind: TypeKind = TypeKind.VOID
    override val name: String = "void"
}