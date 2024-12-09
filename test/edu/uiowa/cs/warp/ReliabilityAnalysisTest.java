package edu.uiowa.cs.warp;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.concurrent.TimeUnit;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import java.util.HashMap;

import edu.uiowa.cs.warp.SystemAttributes.ScheduleChoices;


class ReliabilityAnalysisTest {

//

	private Integer nChannels = 16;
	private ScheduleChoices schedulerSelected = SystemAttributes.ScheduleChoices.PRIORITY;
	private WarpInterface warp;
	private static final Double MIN_LQ = 0.9;
	private static final Double E2E = 0.99;
	private static final String INPUT_FILE = "Example4.txt";
	private static final long TIMEOUT_IN_MILLISECONDS = 10000;
	private WorkLoad workLoad;
	private ReliabilityAnalysis ra;


	/**
	 * Start each test with an unmodified WorkLoad created from the specified MIN_LQ, E2E, and INPUT_FILE parameters; also creates a WarpInterface using the previously 
	 * created WorkLoad, specified nChannels, and ScheduleChoices.
	 * m = 0.9 and e2e = 0.99 by default, as specified in Warp.java.
	 */
	@BeforeEach
	void setUp() {
	    //Initialize workload with default values given in WorkLoad.java comment
		workLoad = new WorkLoad(MIN_LQ, E2E, INPUT_FILE);
		warp = SystemFactory.create(workLoad, nChannels, schedulerSelected);
		ra = warp.toReliabilityAnalysis();
	 }
//
//
//	private Integer nChannels = 16;
//
//	private ScheduleChoices schedulerSelected = SystemAttributes.ScheduleChoices.PRIORITY;
//	private WarpInterface warp;
//	
//	
//	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
//	@Test
//	void testGetTotalNumberOfNodes() {
//		
//		nChannels = 16;
//		workLoad = new WorkLoad(0.9, 0.99, "Example1a.txt");
//		schedulerSelected = SystemAttributes.ScheduleChoices.PRIORITY;
//	    
//		warp = SystemFactory.create(workLoad, nChannels, schedulerSelected);
//		
//		ReliabilityAnalysis reliabilityAnalysis = new ReliabilityAnalysis(warp);		
//		
//		ArrayList<String> flowNames = new ArrayList<String>();
//		flowNames.add("F0");
//		flowNames.add("F1");
//		
//		System.out.println(reliabilityAnalysis.getTotalNumberOfNodes(flowNames, workLoad));
//		
//		int actualNodes = reliabilityAnalysis.getTotalNumberOfNodes(flowNames, workLoad);
//		
//		assertEquals(6, actualNodes);
//		
	
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	@Test
	void testHeaderRowHashMap() {
		ArrayList<String> headerRow = new ArrayList<>();
		headerRow.add("F0:A");
		headerRow.add("F0:B");
		headerRow.add("F0:C");
		headerRow.add("F0:D");
		headerRow.add("F1:C");
		headerRow.add("F1:B");
		headerRow.add("F1:A");

		HashMap<String, Integer> headerRowMap = ra.headerRowHashMap(headerRow);
		System.out.println(headerRowMap);
		
		HashMap<String, Integer> expectedHeaderRowMap = new HashMap<>();
		expectedHeaderRowMap.put("F0:A",0);
		expectedHeaderRowMap.put("F0:B",1);
		expectedHeaderRowMap.put("F0:C",2);
		expectedHeaderRowMap.put("F0:D",3);
		expectedHeaderRowMap.put("F1:C",4);
		expectedHeaderRowMap.put("F1:B",5);
		expectedHeaderRowMap.put("F1:A",6);
		
		assertEquals(expectedHeaderRowMap, headerRowMap);
	}
}
	