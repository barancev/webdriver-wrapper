/*
 * Copyright 2013 Software Freedom Conservancy.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package ru.stqa.selenium.logging;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import uk.org.lidalia.slf4jtest.LoggingEvent;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

class LoggingWebDriverTest {

  private TestLogger logger = TestLoggerFactory.getTestLogger("WebDriver");

  @Test
  void logsWrapperInitialization() {
    final WebDriver mockedDriver = mock(WebDriver.class);

    final WebDriver driver = new LoggingWebDriver(mockedDriver).getActivated();

    List<LoggingEvent> log = logger.getLoggingEvents();
    assertThat(log.size(), is(1));
    String message = log.get(0).getMessage();
    assertThat(message, startsWith("Init tracer"));
  }

  @Test
  void testLogging() {
    final WebDriver mockedDriver = mock(WebDriver.class);

    final WebDriver driver = new LoggingWebDriver(mockedDriver).getActivated();

    driver.get("http://localhost/");

    verify(mockedDriver, times(1)).get("http://localhost/");

    List<LoggingEvent> log = logger.getLoggingEvents();
    assertThat(log.size(), is(3));
    assertThat(log.get(1).getMessage(), startsWith("-> get(\"http://localhost/\")"));
    assertThat(log.get(2).getMessage(), startsWith("<- get(\"http://localhost/\") = null"));
  }

  @AfterEach
  void clearLogger() {
    TestLoggerFactory.clear();
  }

}
