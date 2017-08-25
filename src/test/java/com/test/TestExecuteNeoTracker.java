package com.test;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by avorugan on 8/25/17.
 */
public class TestExecuteNeoTracker {

    @Test
    public void testExecuteFeedApi(){
        ExecuteNeoTracker executeNeoTracker = new ExecuteNeoTracker("");
        boolean result = executeNeoTracker.executeFeedApi();
        Assert.assertEquals("Feed API is successful: ", true, result);
    }

    @Test
    public void testFindNearestNeo(){
        ExecuteNeoTracker executeNeoTracker = new ExecuteNeoTracker("");
        boolean result = executeNeoTracker.findNearestNEO();
        Assert.assertEquals("Finding nearest NEO: ", true, result);
    }

    @Test
    public void testFindLargestNeo(){
        ExecuteNeoTracker executeNeoTracker = new ExecuteNeoTracker("");
        boolean result = executeNeoTracker.findLargestNeo();
        Assert.assertEquals("Finding largest NEO: ", true, result);
    }
}
