package org.platonos.fox.core.definition

import org.platonos.fox.core.type.JType
import org.platonos.fox.core.type.JVoidType
import org.springframework.http.MediaType

class ApiOperationDefinition(
    val name: String,
    val returnJType: JType = JVoidType.INSTANCE,
    requestMappingDefinition: RequestMappingDefinition) {

    val path: List<String>
    val methods: List<String>
    val consumes: List<String>?
    val produces: List<String>?

    init {
        val attributes = requestMappingDefinition.attributes
        val path = attributes["path"]

        this.path = if (path is List<*>) path as List<String> else emptyList()
        methods = attributes["methods"] as List<String>? ?: emptyList()

        consumes = attributes["consumes"] as List<String>? ?: listOf(MediaType.APPLICATION_JSON_VALUE)
        produces = attributes["produces"] as List<String>? ?: listOf(MediaType.APPLICATION_JSON_VALUE)
    }
}