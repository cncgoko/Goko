/**
 * Logback: the reliable, generic, fast and flexible logging framework.
 * Copyright (C) 1999-2013, QOS.ch. All rights reserved.
 *
 * This program and the accompanying materials are dual-licensed under
 * either the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation
 *
 *   or (per the licensee's choosing)
 *
 * under the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation.
 */
package chapters.appenders.conf.programmatic;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.ConsoleAppender;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: ceki
 * Date: 10.09.12
 * Time: 17:27
 * To change this template use File | Settings | File Templates.
 */
public class LogbackOnConsole {

  public static void main(String[] args) {
    LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
    loggerContext.reset(); // optionally forget previous configuration

    PatternLayoutEncoder patternLayoutEncoder = new PatternLayoutEncoder();
    patternLayoutEncoder.setPattern("%-4relative [%thread] %-5level %logger{35} - %msg %n");
    patternLayoutEncoder.setContext(loggerContext);
    patternLayoutEncoder.start();

    ConsoleAppender consoleAppender = new ConsoleAppender();
    consoleAppender.setContext(loggerContext);
    consoleAppender.setName("CON");
    consoleAppender.setEncoder(patternLayoutEncoder);
    consoleAppender.start();

    Logger root = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
    root.setLevel(Level.DEBUG);
    root.addAppender(consoleAppender);


    Logger testLogger = loggerContext.getLogger(LogbackOnConsole.class);
    testLogger.debug("hello world");
  }
}
