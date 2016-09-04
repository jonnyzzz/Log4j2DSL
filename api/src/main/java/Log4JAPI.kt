package org.jonnyzzz.log4j2dsl

import org.apache.log4j.Level


/**
 * Entry point for the DSL
 */
fun log4j(builder : Log4J.() -> Unit) {

}


/**
 * Low level API to access generation of properties
 */
interface Log4JBase {
  /**
   * creates a property
   */
  fun param(key: String, value: String)

  /**
   * creates a comment
   */
  fun comment(line: String)
}

enum class Log4JLevel(val level : Level) {
  OFF(Level.OFF),
  FATAL(Level.FATAL),
  ERROR(Level.ERROR),
  WARN(Level.WARN),
  INFO(Level.INFO),
  DEBUG(Level.DEBUG),
  ALL(Level.ALL),
}

interface Log4J : Log4JBase {
  /**
   * Configures root logger
   */
  fun rootLogger(builder: Log4JAppender.() -> Unit = {})

  /**
   * defines and configures appender
   */
  fun appender(name: String, type: String, builder: Log4JAppender.() -> Unit = {})


  /**
   * defines and configures logger
   */
  fun logger(category: String, builder : Log4JLogger.() -> Unit = {})

  /**
   * This is required to have identical map
   * between .properties file and generated DSL
   */
  fun propertiesOrder(names : List<String>)
}

/**
 * A reference type for declared appender
 */
interface Log4JAppenderRef {
  val name: String
}

/**
 * Builder and view of appender
 */
interface Log4JAppender {
  val name: String
  val type: String
}

/**
 * Builder and view of logger
 */
interface Log4JLogger {
  val category: String

  var additivity : Boolean
  var level : Log4JLevel
  var appenders : List<Log4JAppenderRef>
}
