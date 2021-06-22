package org.platonos.fox.core

import org.platonos.fox.core.definition.ApiOperationDefinition
import org.platonos.fox.core.definition.ControllerDefinition
import org.platonos.fox.core.reader.ControllerInfo

interface Visitor {

    fun pushValue(attributeName: String, value: Any?) {
    }

    fun pushControllerInfo(controllerInfo: ControllerInfo) {
    }

    fun pushApiOperation(apiOperationDefinition: ApiOperationDefinition) {
    }
}