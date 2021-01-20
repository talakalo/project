package com.automation.systemtest.testbase.rules;

import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;


public class StopwatchRule extends Stopwatch {
    
    private static final Logger logger = LoggerFactory.getLogger(StopwatchRule.class);
    
    private static void logStopwatchInfo(Description description, String status, long nanos) {
        String testName = description.getMethodName();
        
        long elapsedTime = 0;
        String timeUnit = null;
        
        long seconds = TimeUnit.NANOSECONDS.toSeconds(nanos);
        
        if (seconds == 0) {
            elapsedTime = TimeUnit.NANOSECONDS.toMicros(nanos);
            timeUnit = "microseconds";
            
        } else if (seconds < 60) {
            elapsedTime = seconds;
            timeUnit = "seconds";
            
        } else if (seconds >= 60) {
            elapsedTime = TimeUnit.NANOSECONDS.toMinutes(nanos);
            timeUnit = "minutes";
        }
        logger.info(String.format("Test '%s' %s, spent %d %s.", testName, status, elapsedTime, timeUnit));
        
    }
    
    /**
     * Invoked when a test succeeded
     */
    @Override
    protected void succeeded(long nanos, Description description) {
        logStopwatchInfo(description, "succeeded", nanos);
    }
    
    /**
     * Invoked when a test fails
     */
    @Override
    protected void failed(long nanos, Throwable e, Description description) {
        logStopwatchInfo(description, "failed", nanos);
    }
    
    
}
