package org.jonnyzzz.log4j2dsl.generator

import org.jonnyzzz.log4j2dsl.*

object TheGenerator {
  fun generate(model : PropertiesModel) : String = kotlinWriter {
    generateFileHeader("Log4J2DSL", "0.0.1")
    appendln()

    declareBuilder("file", "log4j") {
      generateAppenders(model)

      appendln()

      generateLoggers(model)
    }
  }

  private fun KotlinWriter.generateAppenders(model : PropertiesModel) {
    val prefix = "log4j.appender."
    val layoutPrefix = ".layout"

    val appenders = model
            .allProperties(prefix)
            .map { it.key.substring(prefix.length) to it.value }
            .groupBy { it.first.split(".", limit = 2)[0] }
            .map { it.key to it.value.map { v -> v.first.substring(it.key.length) to v.second } }

    println("Detected appenders: ${appenders.map { it.first }.toSortedSet() }")

    appenders.forEach { appender ->

      val type = appender.second.asSequence().map{it}.find { it.first.isEmpty() }?.second ?: "UNKNOWN_APPENDER_TYPE"

      declareBuilder(appender.first, "${Log4J::appender.name}(${appender.first.quote()}, ${type.quote()})") {
        val layout = mutableListOf<Pair<String, String>>()

        for ((key, value) in appender.second) {
          if (!key.startsWith('.')) continue

          if (key.startsWith(layoutPrefix)) {
            layout += key.substring(layoutPrefix.length) to value
            continue
          }

          val name = key.substring(1)
          appendln("${Log4JBase::param.name}(${name.quote()}, ${value.quote()})")
        }

        if (layout.isNotEmpty()) {
          appendln()
          generateAppenderLayout(model, layout)
        }
      }

      appendln()
    }
  }

  private fun KotlinWriter.generateAppenderLayout(model : PropertiesModel, appender: List<Pair<String, String>>) {
    val type = appender.find { it.first.isEmpty() }?.second ?: "UNKNOWN_LAYOUT_TYPE"
    block("layout(${type.quote()})", omitEmpty = true) {

      for ((key, value) in appender) {
        if (!key.startsWith('.')) continue

        val name = key.substring(1)
        appendln("${Log4JBase::param.name}(${name.quote()}, ${value.quote()})")
      }
    }
  }

  private fun KotlinWriter.generateLoggers(model : PropertiesModel) {
    val loggerPrefix = "log4j.logger."
    val additivityPrefix = "log4j.additivity."

    val loggers = model
            .allProperties(loggerPrefix)
            .map { it.key.substring(loggerPrefix.length) to it.value }

    val additivity = model
            .allProperties(additivityPrefix)
            .map { it.key.substring(additivityPrefix.length) to it.value.trim().toBoolean() }

    println("Detected loggers: ${loggers.map { it.first }.toSortedSet() }")

    loggers.forEach { logger ->
      val loggerCategory = logger.first

      block("logger(${loggerCategory.quote()})") {
        logger.second.split(",").map { it.trim() }.forEach { lap ->
          val logLevel = Log4JLevel.values().find { it.name.equals(lap, ignoreCase = true) }

          if (logLevel != null) {
            appendln("${Log4JLogger::level.name} = ${logLevel.name}")
          } else {
            appendln("appenders += ${lap}")
          }
        }

        additivity.find{ it.first == loggerCategory }?.let {
          appendln("${Log4JLogger::additivity.name} = ${it.second}")
        }
      }

      appendln()
    }
  }

}
