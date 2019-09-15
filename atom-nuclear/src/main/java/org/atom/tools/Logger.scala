package org.atom.tools

import org.slf4j.LoggerFactory

class Logger {

  private var logger: org.slf4j.Logger = _

  private def this(clazz: Class[_]) {
    this()
    this.logger = LoggerFactory.getLogger(clazz)
  }

  private def getDebugInfo = {
    val lvStacks = Thread.currentThread.getStackTrace
    "Class Name：[ " + lvStacks(4).getClassName + " ],Function Name：[ " + lvStacks(4).getMethodName + " ],Line：[ " + lvStacks(4).getLineNumber + " ]\n"
  }

  def debug(message: String, params: Any*): Unit = {
    logger.debug(message, params)
  }

  def info(message: String, params: Any*): Unit = {
    logger.info(message, params)
  }

  def warn(message: String, params: Any*): Unit = {
    logger.warn(getMessage(message), params)
  }

  def warn(message: String, t: Throwable): Unit = {
    logger.warn(message, t)
  }

  def error(message: String, params: Any*): Unit = {
    logger.error(getMessage(message), params)
  }

  private def getMessage(message: String) = getDebugInfo + message

}

object Logger {
  def newInstance(clazz: Class[_]) = new Logger(clazz)
}

