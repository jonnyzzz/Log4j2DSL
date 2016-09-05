package org.jonnyzzz.log4j2dsl.generator

import org.jonnyzzz.log4j2dsl.PropertiesModel
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized


@RunWith(Parameterized::class)
class GeneratorTest(val name: String, val code: String) {
  companion object {
    @JvmStatic
    @Parameterized.Parameters(name = "{0}")
    fun data() : Collection<Array<Any>> {

      val names = listOf(

              "example_01.log4j.properties",
              "example_02.log4j.properties",
              "example_03.log4j.properties",
              "example_04.log4j.properties",
              "example_05.log4j.properties",
              "example_06.log4j.properties",
              "example_07.log4j.properties",
              "example_08.log4j.properties"
      )

      return names
              .map { it to GeneratorTest::class.java.classLoader.getResourceAsStream(it).use { it.readBytes() } }
              .map { it.first to it.second.toString(Charsets.UTF_8) }
              .map { arrayOf<Any>(it.first, it.second) }
              .toList()
    }
  }

  @Test
  fun testGenerator() {

    val model = PropertiesModel().apply { load(code.toByteArray().inputStream()) }
    val text = TheGenerator.generate(model)

    println("code:")
    println(code)
    println()
    println("kotlin:")
    println(text)
    println()


  }

}