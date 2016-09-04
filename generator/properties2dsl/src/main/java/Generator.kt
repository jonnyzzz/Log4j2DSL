package org.jonnyzzz.log4j2dsl.generator

import org.jonnyzzz.log4j2dsl.Log4J
import org.jonnyzzz.log4j2dsl.Log4JBase
import org.jonnyzzz.log4j2dsl.Log4JLogger
import org.jonnyzzz.log4j2dsl.PropertiesModel
import java.io.File

private fun usage() {
  println()
  println("Usage:")
  println(" <generator> <log4j.properties> <target directory>")
  println("")
  println("    log4j.properties  - input file to generate Kotlin DSL")
  println("    target directory  - directory to generate, WILL BE WIPED")
  println("")
  System.exit(1)
}

fun main(args: Array<String>) {
  try {
    mainImpl(args)
  } catch (t : Throwable) {
    println("Failed. ${t.message}")
    t.printStackTrace()
    System.exit(2)
  }
}

private fun mainImpl(args: Array<String>) {
  println("Jonnyzzz.com Log4j .properties to DSL generator")
  println()

  val propertiesFile = File(args[0]).canonicalFile
  if (!propertiesFile.isFile) {
    println("Properties file: $propertiesFile not found")
    return usage()
  }

  val targetDirectory = File(args[1]).canonicalFile
  targetDirectory.deleteRecursively()
  targetDirectory.mkdirs()

  if (!targetDirectory.isDirectory) {
    println("Failed to create target directory. $targetDirectory")
    return usage()
  }

  val model = PropertiesModel().apply { load(propertiesFile) }


}

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

    val appenders = model
            .allProperties(prefix)
            .map { it.key.substring(prefix.length) to it.value }
            .groupBy { it.first.split(".", limit = 2)[0] }
            .map { it.key to it.value.map { v -> v.first.substring(it.key.length + 1) to v.second } }

    println("Detected appenders: ${appenders.map { it.first }.toSortedSet() }")

    appenders.forEach { appender ->
      declareBuilder(appender.first, "${Log4J::appender.name}(${appender.first.quote()}, TYPE )") {
        appender.second.forEach { param ->
          appendln("${Log4JBase::param.name}(${param.first.quote()}, ${param.second.quote()})")
        }
      }

      appendln()
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
            .map { it.key.substring(loggerPrefix.length) to it.value }


    println("Detected loggers: ${loggers.map { it.first }.toSortedSet() }")

    loggers.forEach { logger ->

      block("logger(${logger.first.quote()}") {
        appendln("${Log4JBase::param.name}(\"\", ${logger.second.quote()})")

        additivity.find{ it.first == logger.first}?.let {
          appendln("${Log4JLogger::additivity.name} = $it")
        }
      }

      appendln()
    }
  }

}

