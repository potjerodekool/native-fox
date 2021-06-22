package org.platonos.fox

import org.junit.jupiter.api.Test

import org.platonos.fox.core.ClassScanner
import java.io.File

internal class ClassScannerTest {

    @Test
    fun scan() {
        val scanner = ClassScanner.getInstance()
        val file = File("target/test-classes/org/platonos/fox/TestController.class")
        val controllerDefinition = scanner.scan(file.inputStream(), "org.platonos.fox.TestController")
        println(controllerDefinition)
    }
}