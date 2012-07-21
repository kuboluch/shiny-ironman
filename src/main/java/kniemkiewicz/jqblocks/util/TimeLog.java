package kniemkiewicz.jqblocks.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * User: knie
 * Date: 7/21/12
 */
public class TimeLog {

  Log logger = LogFactory.getLog(TimeLog.class);

  long startMills = System.currentTimeMillis();

  public void logTimeAndRestart(String message) {
    long current = System.currentTimeMillis();
    logger.info(message + " : " + String.valueOf(current - startMills) + "ms");
    startMills = current;
  }
}
