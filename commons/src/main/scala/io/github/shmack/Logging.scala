package io.github.shmack

/**
  * @author Sampath sree kumar, Kolluru
  * @version %I% %G%
  * @since 1.0
  */

import java.io.{PrintWriter, StringWriter}

import org.slf4j.{Logger, LoggerFactory}


/**
  * Utility trait for classes that want to log data. Creates a SLF4J logger for the class and allows
  * logging messages at different levels using methods that only evaluate parameters lazily if the
  * log level is enabled.
  *
  *
  * This is a copy of what Spark Previously held in org.apache.spark.Logging. That class is
  * now private so we will expose similar functionality here.
  */
trait Logging {


  // Make the log field transient so that objects with Logging can
  // be serialized and used on another machine
  @transient private var _log: Logger = _

  // Method to get the logger name for this object
  protected def logName: String = {
    // Ignore trailing $'s in the class names for Scala objects
    this.getClass.getName.stripSuffix("$")
  }

  // Method to get or create the logger for this object
  protected def log: Logger = {
    if (_log == null) {
      _log = LoggerFactory.getLogger(logName)
    }
    _log
  }

  // Log methods that take only a String
  protected def logInfo(msg: => String): Unit = {
    if (log.isInfoEnabled) log.info(msg)
  }

  protected def logDebug(msg: => String): Unit = {
    if (log.isDebugEnabled) log.debug(msg)
  }

  protected def logTrace(msg: => String): Unit = {
    if (log.isTraceEnabled) log.trace(msg)
  }

  protected def logWarning(msg: => String): Unit = {
    if (log.isWarnEnabled) log.warn(msg)
  }

  protected def logError(msg: => String): Unit = {
    if (log.isErrorEnabled) log.error(msg)
  }

  // Log methods that take Throwables (Exceptions/Errors) too
  protected def logInfo(msg: => String, throwable: Throwable): Unit = {
    if (log.isInfoEnabled) log.info(msg, throwable)
  }

  protected def logDebug(msg: => String, throwable: Throwable): Unit = {
    if (log.isDebugEnabled) log.debug(msg, throwable)
  }

  protected def logTrace(msg: => String, throwable: Throwable): Unit = {
    if (log.isTraceEnabled) log.trace(msg, throwable)
  }

  protected def logWarning(msg: => String, throwable: Throwable): Unit = {
    if (log.isWarnEnabled) log.warn(msg, throwable)
  }

  protected def logError(msg: => String, throwable: Throwable): Unit = {
    if (log.isErrorEnabled) log.error(msg, throwable)
  }

  protected def isTraceEnabled: Boolean = {
    log.isTraceEnabled
  }

  /**
    * Return a nice string representation of the exception. It will call "printStackTrace" to
    * recursively generate the stack trace including the exception and its causes.
    *
    * replicated Spark internal [[org.apache.spark.util.Utils.exceptionString]]
    * [[https://github.com/apache/spark/pull/11182]]
    */
  private[this] def exceptionString(e: Throwable): String = {
    if (e == null) {
      ""
    }
    else {
      // Use e.printStackTrace here because e.getStackTrace doesn't include the cause
      val stringWriter = new StringWriter()
      e.printStackTrace(new PrintWriter(stringWriter))
      stringWriter.toString
    }
  }

  protected def getErrorCause(ex: Throwable, customErrorMessage: String = "Error", getStackTrace: Boolean = false): String = {
    if (getStackTrace) s"$customErrorMessage: ${exceptionString(ex)}"
    else s"$customErrorMessage: ${ex.getMessage}"
  }


}