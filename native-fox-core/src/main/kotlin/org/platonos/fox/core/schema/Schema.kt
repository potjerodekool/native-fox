package org.platonos.fox.core.schema

class Schema {

    private val _types = mutableMapOf<String, SchemaType>()

    val types: Map<String, SchemaType>
    get() = _types

    fun addType(type: SchemaType): SchemaType {
        val existingType = types[type.name]

        if (existingType == null) {
            _types[type.name] = type
            return type
        } else {
            val mergedProperties = mutableMapOf<String, String>()
            mergedProperties += existingType.properties
            mergedProperties += type.properties
            val mergedType = SchemaType(type.name, mergedProperties)
            _types[mergedType.name] = mergedType
            return mergedType
        }
    }

}