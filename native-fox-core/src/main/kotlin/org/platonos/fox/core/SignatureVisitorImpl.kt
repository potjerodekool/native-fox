package org.platonos.fox.core

import org.objectweb.asm.signature.SignatureVisitor
import org.platonos.fox.core.type.JDeclaredType
import org.platonos.fox.core.type.JType
import org.platonos.fox.core.type.JVoidType

class SignatureVisitorImpl(api: Int, private val parent: SignatureVisitorImpl? = null) : SignatureVisitor(api) {

    private var position = SignaturePosition.NONE

    private var declaredType: JDeclaredType? = null

    private var type: JType? = null

    var returnType: JType? = null

    override fun visitFormalTypeParameter(name: String?) {
        TODO()
    }

    override fun visitClassBound(): SignatureVisitor {
        return SignatureVisitorImpl(api, this)
    }

    override fun visitInterfaceBound(): SignatureVisitor {
        return SignatureVisitorImpl(api, this)
    }

    override fun visitSuperclass(): SignatureVisitor {
        return SignatureVisitorImpl(api, this)
    }

    override fun visitInterface(): SignatureVisitor {
        return SignatureVisitorImpl(api, this)
    }

    override fun visitParameterType(): SignatureVisitor {
        return SignatureVisitorImpl(api, this)
    }

    override fun visitReturnType(): SignatureVisitor {
        position = SignaturePosition.RETURN_TYPE
        return SignatureVisitorImpl(api, this)
    }

    override fun visitExceptionType(): SignatureVisitor {
        return SignatureVisitorImpl(api, this)
    }

    override fun visitBaseType(descriptor: Char) {
        val basicType = when(descriptor) {
            'V' -> JVoidType.INSTANCE
            else -> TODO()
        }

        type = basicType

        if (parent != null) {
            parent.pushType(basicType)
        }
    }

    override fun visitTypeVariable(name: String?) {
        TODO()
    }

    override fun visitArrayType(): SignatureVisitor {
        return SignatureVisitorImpl(api, this)
    }

    override fun visitClassType(name: String?) {
        this.declaredType = JDeclaredType(name!!)
    }

    override fun visitInnerClassType(name: String?) {
        TODO()
    }

    override fun visitTypeArgument() {
        TODO()
    }

    override fun visitTypeArgument(wildcard: Char): SignatureVisitor {
        return SignatureVisitorImpl(api, this)
    }

    fun pushType(JType: JType) {
        if (position == SignaturePosition.RETURN_TYPE) {
            returnType = JType
        } else {
            declaredType = declaredType?.withTypeArgument(JType as JDeclaredType)
        }
    }

    override fun visitEnd() {
        if (parent != null) {
            val type = declaredType

            if (type != null) {
                parent.pushType(type)
            } else {
                TODO()
            }
        }
    }
}

enum class SignaturePosition {
    RETURN_TYPE,
    NONE
}