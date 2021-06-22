package org.platonos.fox.swagger.file

class SwaggerFile {
    var swagger: String? = null
    var info: SwaggerInfo? = null
    var host: String? = null
    var tags: List<Map<String, String>> = emptyList()
    var paths = mutableMapOf<String, Map<String, Any>>()
    val definitions = mutableMapOf<String, Map<String, Any>>()
}