package org.platonos.fox.core.reader

import org.objectweb.asm.AnnotationVisitor
import org.platonos.fox.core.Visitor

abstract class AbstractAnnotationReader(api: Int,
                                        val annotationClassName: String? = null,
                                        val attributeName: String? = null,
                                        val parent: Visitor? = null) : AnnotationVisitor(api), Visitor {

    val attributes = mutableMapOf<String, Any?>()

    override fun visit(name: String?, value: Any?) {
        if (name == null) {
            parent!!.pushValue(attributeName!!, value)
        } else {
            pushValue(name, value)
        }
    }

    override fun visitEnum(name: String?, descriptor: String?, value: String?) {
        TODO()
    }

    override fun visitAnnotation(name: String?, descriptor: String?): AnnotationVisitor {
        TODO()
    }

    override fun visitArray(name: String?): AnnotationVisitor {
        attributes[name!!] = mutableListOf<Any?>()

        return AnnotationReader(
            api = api,
            attributeName = name,
            parent = this
        )
    }

    override fun pushValue(attributeName: String, value: Any?) {
        val currentValue = attributes[attributeName]

        if (currentValue is MutableList<*>) {
            val list = currentValue as MutableList<Any?>
            list.add(value)
        } else {
            attributes[attributeName] = value
        }
    }
}