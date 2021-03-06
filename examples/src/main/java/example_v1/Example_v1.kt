package example_v1


/*
log4j.rootLogger=ERROR,stdout
log4j.logger.corp.mega=INFO
# meaningful comment goes here
log4j.logger.corp.mega.itl.web.metrics=INFO

log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%p\t%d{ISO8601}\t%r\t%c\t[%t]\t%m%n
*/


val x = log4j {
  rootLogger = "ERROR,stdout"
  logger("corp.mega") {
    level = INFO
  }
  comment("meaningful comment goes here")
  logger("corp.mega.itl.web.metrics") {
    level = INFO
  }

  appender("stdout") {
    type = "org.apache.log4j.ConsoleAppender"
    layout {
      type = "org.apache.log4j.PatternLayout"
      param("ConversionPattern", "%p\t%d{ISO8601}\t%r\t%c\t[%t]\t%m%n")
    }
  }
}




interface Log4J {
  var rootLogger : String

  fun logger(category : String, builder : Log4JCategory.() -> Unit)
  fun appender(name : String, builder : Log4jJAppender.() -> Unit)

  fun comment(text : String)
}

enum class Log4jLevel {
  INFO
}

interface Log4JCategory {
  val INFO : Log4jLevel
          get() = Log4jLevel.INFO

  var level : Log4jLevel
}

interface Log4jJAppender {
  var type : String

  fun layout(builder : Log4jJLayout.() -> Unit)
  fun param(name : String, value : String)

}

interface Log4jJLayout {
  var type : String

  fun param(name : String, value : String)

}

fun log4j(builder : Log4J.() -> Unit) {

}
