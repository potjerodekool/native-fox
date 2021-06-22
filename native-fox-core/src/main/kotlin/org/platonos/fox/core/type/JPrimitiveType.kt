package org.platonos.fox.core.type

class JPrimitiveType private constructor(override val kind : TypeKind, override val name: String): JType {

    companion object {
        val BOOLEAN = JPrimitiveType(TypeKind.BOOLEAN, "boolean")
    }

}