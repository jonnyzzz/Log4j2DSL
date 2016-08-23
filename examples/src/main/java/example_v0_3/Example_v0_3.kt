package example_v0_3


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
  val stdout = appender("stdout", "org.apache.log4j.ConsoleAppender") {
    layout("org.apache.log4j.PatternLayout") {
      param("ConversionPattern", "%p\t%d{ISO8601}\t%r\t%c\t[%t]\t%m%n")
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


fun Log4J.logger(category: String,
                 additivity : Boolean = true,
                 level : Log4JLevel? = null,
                 appeners : List<Log4jAppenderRef>? = null,
        builder : Log4JLogger.() -> Unit = { }) {
  param("aaa", "vbb")
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



  val ERROR : Log4JLevel
    get() = Log4JLevel.ERROR

  val INFO : Log4JLevel
    get()= Log4JLevel.INFO
}

interface Log4jAppenderRef {

}

interface Log4JAppender {
  var type: String
  fun layout(type : String, layout : Log4JLayout.() -> Unit)
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
}

fun log4j(builder : Log4J.() -> Unit) {

}


fun Log4J.x() {
  toString()
}



