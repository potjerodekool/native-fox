package org.platonos.fox.core

object StringUtils {

    fun descriptorToClassName(descriptor: String): String {
        if (descriptor.startsWith("L").not()) {
            TODO()
        } else {
            return descriptor.substring(1, descriptor.length - 1).replace('/', '.')
        }
    }
}