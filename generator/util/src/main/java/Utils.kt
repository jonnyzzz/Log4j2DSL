package org.jonnyzzz.log4j2dsl

import java.io.Closeable
import java.io.File

operator fun File?.div(s : String) : File = File(this, s)
operator fun File.plus(s : String) : File = File(this.path + s)

fun File.deleteAll(): Boolean {
  val files = listFiles()
  if (files != null) {
    for (child in files) {
      if (!child.deleteAll()) return false;
    }
  }

  for (i in 1..10) {
    if (delete() || !exists() ) return true;
    Thread.sleep(10) ;
  }
  return false;
}

inline fun suppressing(action : () -> Unit) : Unit {
  try {
    action()
  } catch (t: Throwable) {
    //NOP
  }
}

inline fun <T: Closeable, Y> using(handler : T, action : T.() -> Y) : Y {
  try {
    return handler.action()
  } finally {
    closeIt(handler)
  }
}

fun closeIt(handler: Closeable) {
  try {
    handler.close()
  } catch (t: Throwable) {
    //NOP
  }
}

