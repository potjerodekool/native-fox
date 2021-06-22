package org.platonos.fox.core.type

class JDeclaredType(override val name: String,
                    val typeArguments: List<JDeclaredType> = emptyList()
    ): JType {

    override val kind: TypeKind = TypeKind.DECLARED

    fun withTypeArgument(type: JDeclaredType): JDeclaredType {
        val typeArgs = mutableListOf<JDeclaredType>()
        typeArgs += typeArguments
        typeArgs += type
        return JDeclaredType(name, typeArgs)
    }

    override fun toString(): String {
        if (typeArguments.isEmpty()) {
            return name
        } else {
            val typeArgStr = typeArguments.joinToString(prefix = "<", separator = ",", postfix = ">")
            return name + typeArgStr
        }
    }
}