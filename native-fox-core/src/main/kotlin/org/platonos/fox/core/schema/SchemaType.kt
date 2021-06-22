package org.platonos.fox.core.schema

class SchemaType(override val name: String, map: Map<String, String>): Type {

    val type = "object"
    private val _properties = mutableMapOf<String, String>()

    val properties: Map<String, String>
    get() = _properties

    constructor(name: String): this(name, emptyMap())

    init {
        _properties += map
    }

    fun addProperty(name: String, type: String) {
        _properties[name] = type
    }
}