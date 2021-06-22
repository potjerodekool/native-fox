package org.platonos.fox

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.platonos.fox.core.ClassScanner
import org.platonos.fox.core.definition.ControllerDefinition
import org.platonos.fox.core.schema.Schema
import org.platonos.fox.swagger.SwaggerGenerator
import java.io.File
import kotlin.test.assertEquals

class SwaggerGeneratorTest {

    private val objectMapper = ObjectMapper()

    @Test
    fun test() {
        val scanner = ClassScanner.getInstance()
        val file = File("target/test-classes/org/platonos/fox/TestController.class")

        val controllerDefinitions = mutableListOf<ControllerDefinition>()

        val basicErrorControllerInputStream = javaClass.classLoader.getResourceAsStream("org/springframework/boot/autoconfigure/web/servlet/error/BasicErrorController.class")

        val basicErrorControllerDefinition = scanner.scan(basicErrorControllerInputStream, "org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController")

        if (basicErrorControllerDefinition != null) {
            controllerDefinitions += basicErrorControllerDefinition
        }

        val testControllerDefinition = scanner.scan(file.inputStream(), "org.platonos.fox.TestController")

        if (testControllerDefinition != null) {
            controllerDefinitions += testControllerDefinition
        }

        val generator = SwaggerGenerator()
        val schema = Schema()
        val swaggerFile = generator.generate(controllerDefinitions, schema)

        val actualJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(swaggerFile)

        val expected = objectMapper.readTree(File("src/test/resources/swagger1.json"))
        val actual = objectMapper.readTree(actualJson)

        println(actualJson)

        assertEquals(expected, actual)
    }
}