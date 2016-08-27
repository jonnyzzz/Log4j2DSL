package example_v0_6

import org.apache.log4j.Appender
import org.apache.log4j.ConsoleAppender
import org.apache.log4j.Layout
import org.apache.log4j.PatternLayout
import kotlin.reflect.KClass


/*
log4j.rootLogger=ERROR,stdout
log4j.logger.corp.mega=INFO
# meaningful comment goes here
log4j.logger.corp.mega.itl.web.metrics=INFO

log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%p\t%d{ISO8601}\t%r\t%c\t[%t]\t%m%n
*/


val x =
log4j {
  //use this
  val stdout = appender("stdout", ConsoleAppender::class) {
    layout<PatternLayout> {
      conversionPattern = "%p\t%d{ISO8601}\t%r\t%c\t[%t]\t%m%n"
    }
  }

  //use this
  rootLogger {
    level = ERROR
    appenders += stdout
  }

  logger("corp.mega") {
    additivity = false
    level = INFO
  }

  logger("corp.mega.d2", level = INFO) >= stdout


  param("log4j.logger.corp.mega", "INFO")
  param("log4j.additivity.corp.mega", "false")
}

enum class Log4JLevel {
  DEBUG, INFO, WARN, ERROR
}

fun Log4J.logger(category: String, builder : Log4JLogger.() -> Unit) {
  param("aaa", "vbb$category$builder")
}

fun Log4J.rootLogger(builder : Log4JLogger.() -> Unit) {
  param("aaa", "vbb$builder")

}

object X {
  operator fun compareTo(app : Log4jAppenderRef) : Int {
    return 0
  }
}


fun Log4J.logger(category: String,
                 additivity : Boolean = true,
                 level : Log4JLevel? = null,
                 appeners : List<Log4jAppenderRef>? = null) {
  param("aaa", "vbb$category$additivity$level$appeners")
}

fun Log4J.logger(category: String,
                 additivity : Boolean = true,
                 level : Log4JLevel? = null) : X {
  param("aaa", "vbb$category$additivity$level")
  return X
}

fun Log4J.rootLogger(level : Log4JLevel? = null,
                     appeners : List<Log4jAppenderRef>? = null,
                     builder : Log4JLogger.() -> Unit = { }) {
  param("aaa", "vbb")

}

interface Log4JLogger {
  var additivity : Boolean?
  var level : Log4JLevel?
  var appenders : List<Log4jAppenderRef>




}


val String.bytesSize : Int
  get() = toByteArray(Charsets.UTF_8).size


val xzxxx =
        "sas".bytesSize

val Log4J.INFO : Log4JLevel
  get()= Log4JLevel.INFO

val Log4J.ERROR : Log4JLevel
  get() = Log4JLevel.ERROR


interface Log4jAppenderRef {

}

interface Log4JAppender {
  var type: String
  fun layout(type : String, layout : Log4JLayout.() -> Unit)
  fun <T : Layout> layout(type : KClass<T>, layout : T.() -> Unit)
}

inline fun <reified T : Layout> Log4JAppender.layout(crossinline l : T.() -> Unit) {
  val type = T::class ///allowed for reified generics
  layout(type) {
    this.l()
  }
}

inline fun <reified T : Appender> Log4J.appender(name : String, crossinline l : T.() -> Unit) {
  appender(name, T::class) {
  }
}


fun <T : Appender> Log4J.appender(name : String, type : KClass<T>, builder : Log4JAppender.() -> Unit) : Log4jAppenderRef {
  return object : Log4jAppenderRef {}
}

fun Log4J.appender(name : String, type : String, builder : Log4JAppender.() -> Unit) : Log4jAppenderRef {
  param("log4j.appender.name", "")
  return object : Log4jAppenderRef {}
}



interface Log4JLayout {
  var type : String
}


var Log4J.rootLogger_old : String
  set(l : String) = param("log4j.rootLogger", l)
  get() = throw Error()


interface Log4J {
  fun comment(text : String)
  fun param(name: String, value : String) : Unit                  // or "$this"


  fun <T : Appender> Log4J.appender(name : String, type : KClass<T>, builder : Log4JAppender.() -> Unit) : Log4jAppenderRef {
    return object : Log4jAppenderRef {}
  }
}

fun log4j(builder : Log4J.() -> Unit) {

}


fun Log4J.x() {
  toString()
}



