package example_v0_1


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
  rootLogger = "ERROR,stdout"
  //instead of that
  this.param("log4j.rootLogger", "ERROR,stdout")

  param("log4j.logger.corp.mega", "INFO")
  comment("meaningful comment goes here")
  param("log4j.logger.corp.mega.itl.web.metrics", "INFO")

  //use this
  appender("stdout", "org.apache.log4j.ConsoleAppender") {
    layout("org.apache.log4j.PatternLayout") {
      param("ConversionPattern", "%p\t%d{ISO8601}\t%r\t%c\t[%t]\t%m%n")
    }
  }

  //instead of
  param("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender")
  param("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout")
  param("log4j.appender.stdout.layout.ConversionPattern", "%p\t%d{ISO8601}\t%r\t%c\t[%t]\t%m%n")
}

interface Log4JAppender {
  var type: String
  fun layout(type : String, layout : Log4JLayout.() -> Unit)
}

fun Log4J.appender(name : String, type : String, builder : Log4JAppender.() -> Unit) {
  param("log4j.appender.name", "")
}



interface Log4JLayout {
  var type : String
}


var Log4J.rootLogger : String
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



