package org.jonnyzzz.log4j2dsl.generator

import org.jonnyzzz.log4j2dsl.PropertiesModel
import org.jonnyzzz.log4j2dsl.div
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
  val code = TheGenerator.generate(model)

  val targetFile = targetDirectory / "log4j.kt"
  targetFile.parentFile?.mkdirs()
  targetFile.writeText(code)
}

