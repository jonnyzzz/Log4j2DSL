package org.jonnyzzz.log4j2dsl

import java.io.File
import java.io.StringWriter
import java.util.*

class PropertiesModel {
  private val data = LinkedHashMap<String, String>()

  fun load(propertiesFile : File) {
    object: Properties() {
      override fun put(key: Any?, value: Any?): Any? {
        data.put(key as String, value as String)
        return super.put(key, value)
      }
    }.apply { propertiesFile.reader().use { load(it) } }
  }

  fun save(propertiesFile : File) {
    val fakeOutput = StringWriter()
    val data = LinkedHashMap(this.data)
    object: Properties() {
      override fun keys(): Enumeration<Any> {
        return Vector<Any>(data.keys).elements()
      }
    }       .apply { putAll(data) }
            .apply { fakeOutput.use { store(it, null) } }

    val textWithoutTimeComment = fakeOutput.toString().split("\n").drop(1).joinToString("\n")
    propertiesFile.writeText(textWithoutTimeComment)
  }

  fun allProperties(prefix : String = "" ) = data.filter { it.key.startsWith(prefix) }

}
