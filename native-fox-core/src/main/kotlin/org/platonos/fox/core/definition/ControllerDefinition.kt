package org.platonos.fox.core.definition

import org.springframework.http.MediaType

class ControllerDefinition(
    val qualifiedName: String,
    val simpleName: String,
    attributes: Map<String, Any?>,
    val apiOperations: MutableList<ApiOperationDefinition>
) {

    val path: String? = attributes["value"] as String?
    val consumes: List<String> = attributes["consumes"] as List<String>? ?: listOf(MediaType.APPLICATION_JSON_VALUE)
    val produces: List<String> = attributes["produces"] as List<String>? ?: listOf(MediaType.APPLICATION_JSON_VALUE)

}