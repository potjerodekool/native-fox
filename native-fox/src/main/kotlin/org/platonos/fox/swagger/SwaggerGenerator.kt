package org.platonos.fox.swagger

import org.platonos.fox.core.definition.ControllerDefinition
import org.platonos.fox.core.schema.Schema
import org.platonos.fox.core.type.TypeKind
import org.platonos.fox.swagger.file.SwaggerFile
import org.springframework.http.MediaType
import java.lang.StringBuilder

class SwaggerGenerator {

    fun generate(controllerDefinitions: List<ControllerDefinition>,
                 schema: Schema): SwaggerFile {
        val swaggerFile = SwaggerFile()
        swaggerFile.swagger = "2.0"
        swaggerFile.host = "\$HOST"

        processControllerTags(controllerDefinitions, swaggerFile)

        val paths = processControllers(controllerDefinitions)
        swaggerFile.paths = paths.toMutableMap()

        processSchema(schema, swaggerFile)

        return swaggerFile
    }

    private fun processControllerTags(controllerDefinitions: List<ControllerDefinition>,
                                      swaggerFile: SwaggerFile) {
        controllerDefinitions.forEach { controllerDefinition ->
            val tagMap = mapOf(
                "name" to toKebabCase(controllerDefinition.simpleName),
                "description" to toSpaceCase(controllerDefinition.simpleName)
            )
            swaggerFile.tags += tagMap
        }
    }

    private fun processControllers(controllerDefinitions: List<ControllerDefinition>): MutableMap<String, Map<String, ApiDefinition>> {
        val paths = mutableMapOf<String, Map<String, ApiDefinition>>()

        controllerDefinitions.forEach {
            paths.putAll(processController(it))
        }

        return paths
    }

    private fun processController(controller: ControllerDefinition): Map<String, Map<String, ApiDefinition>> {
        val classConsumes: List<String> = controller.consumes ?: listOf(MediaType.APPLICATION_JSON_VALUE)
        val classProduces: List<String> = controller.produces ?: listOf(MediaType.APPLICATION_JSON_VALUE)
        val controllerPath: String = controller.path ?: ""


        // Path = [method = ApiDefinition]
        val pathToApiMappings = mutableMapOf<String, MutableMap<String, ApiDefinition>>()

        controller.apiOperations.forEach { apiOperation ->
            val methods = apiOperation.methods

            methods.forEach { method ->
                apiOperation.path.forEach { path ->

                    val apiPath = absolutePath(controllerPath, path)
                    val tags = listOf(toKebabCase(controller.simpleName))
                    val consumes = apiOperation.consumes ?: classConsumes
                    val produces = apiOperation.produces ?: classProduces
                    val returnType = apiOperation.returnJType

                    val okMap = mutableMapOf<String, Any>("description" to "OK")

                    if (returnType.kind != TypeKind.VOID) {
                        okMap["schema"] = mapOf(
                            "\$ref" to "#/definitions/$returnType"
                        )
                    }

                    val responses = mutableMapOf<String, Map<String, Any>>()
                    responses["200"] = okMap
                    responses["204"] = mapOf("description" to "No Content")
                    responses["401"] = mapOf("description" to "Unauthorized")
                    responses["403"] = mapOf("description" to "Forbidden")

                    val apiDefinitionMap = pathToApiMappings.computeIfAbsent(apiPath) { mutableMapOf()}

                    apiDefinitionMap[method] = ApiDefinition(
                        tags = tags,
                        summary = apiOperation.name,
                        operationId = "${apiOperation.name}Using${method.toUpperCase()}",
                        consumes = consumes,
                        produces = produces,
                        responses = responses
                    )
                }
            }
        }

        return pathToApiMappings
    }

    private fun processSchema(schema: Schema, swaggerFile: SwaggerFile) {
        schema.types.forEach { (typeName, schemaType) ->
            val mutableMap = mutableMapOf<String, Any>()
            mutableMap["type"] = schemaType.type

            val propertiesMap = mutableMapOf<String, Map<String, String>>()

            schemaType.properties.forEach { (propertyName, propertyType) ->
                val map = mutableMapOf<String, String>()
                map["type"] = propertyType
                propertiesMap[propertyName] = map
            }

            mutableMap["properties"] = propertiesMap

            swaggerFile.definitions[typeName] = mutableMap
        }
    }

    private fun absolutePath(controllerPath: String?, value: String?): String {
        val subPath: String =
            when {
                value.isNullOrEmpty() -> "/"
                value.startsWith('/') -> value
                else -> "/$value"
            }

        return if (controllerPath == null) {
            subPath
        } else {
            val rootPath = if (controllerPath.endsWith('/')) controllerPath.substring(0, controllerPath.length - 1)
            else controllerPath
            rootPath + subPath
        }
    }

    private fun toKebabCase(name: String): String {
        val builder = StringBuilder()

        name.forEach { char ->
            if (builder.isNotEmpty() && char.isUpperCase()) {
                builder.append("-")
            }
            builder.append(char.toLowerCase())
        }

        return builder.toString()
    }

    private fun toSpaceCase(name: String): String {
        val builder = StringBuilder()

        name.forEach { char ->
            if (builder.isNotEmpty() && char.isUpperCase()) {
                builder.append(" ")
            }
            builder.append(char)
        }

        return builder.toString()
    }
}

class ApiDefinition(
    val tags: List<String>,
    val summary: String,
    val operationId: String,
    val consumes: List<String>,
    val produces: List<String>,
    val responses: Map<String, Map<String, Any>>
)