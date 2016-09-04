package org.jonnyzzz.log4j2dsl

import org.junit.Assert
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder


class PropertiesModelTest {
  @Rule @JvmField
  val temp = TemporaryFolder()

  private fun testLoadSave(properties : String) {
    val file = temp.newFile("test.properties")
    file.writeText(properties)

    val m = PropertiesModel()
    m.load(file)

    val newFile = temp.newFile()
    m.save(newFile)


    val newText = newFile.readText()
    //TODO: avoid trim here
    Assert.assertEquals(properties.trim(), newText.trim())
  }


  @Test
  fun test_empty_properties() {
    testLoadSave("")
  }

  @Test
  fun test_key_value() {
    testLoadSave("a=b")
  }

  @Test
  fun test_two_key_value() {
    testLoadSave("a=b\nc=d")
  }

  @Test
  @Ignore
  fun test_comment() {
    Assert.fail("Not implemented")
  }
}

