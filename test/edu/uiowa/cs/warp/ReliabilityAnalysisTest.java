package edu.uiowa.cs.warp;

import static org.junit.Assert.assertFalse;
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
import edu.uiowa.cs.warp.WarpDSL.InstructionParameters;


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
	private Program program;


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
		program = warp.toProgram();
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
	
	@Test
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	void testVerifyReliabilities() {
	
		
		assertTrue(ra.verifyReliablities());
	}
	
	
	@Test
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	void testVerifyReliabilitiesStressTest4() {
	
		
		nChannels = 16;
		workLoad = new WorkLoad(0.9, 0.99, "StressTest4.txt");
		schedulerSelected = SystemAttributes.ScheduleChoices.PRIORITY;
	    
		warp = SystemFactory.create(workLoad, nChannels, schedulerSelected);
		ra = warp.toReliabilityAnalysis();
		program = warp.toProgram();
		
		assertTrue(ra.verifyReliablities());
	}
	
	@Test
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	void testVerifyReliabilitiesExample4NumFaults() {
	
		workLoad = new WorkLoad(1, MIN_LQ, E2E, INPUT_FILE);
		warp = SystemFactory.create(workLoad, nChannels, schedulerSelected);
		ra = warp.toReliabilityAnalysis();
		program = warp.toProgram();
		
		assertFalse(ra.verifyReliablities());
	}
	
	@Test
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	void testCreateHeaderRowExample4() {
		ArrayList<String> expectedHeaderRow = new ArrayList<>();
		expectedHeaderRow.add("F0:A");
		expectedHeaderRow.add("F0:B");
		expectedHeaderRow.add("F0:C");
		expectedHeaderRow.add("F0:D");
		expectedHeaderRow.add("F1:C");
		expectedHeaderRow.add("F1:B");
		expectedHeaderRow.add("F1:A");
		
		ArrayList<String> actualHeaderRow = ra.createHeaderRow();
		
		
		assertEquals(expectedHeaderRow, actualHeaderRow);
	}
	
	@Test
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	void testCreateHeaderRowStressTest() {
		
		
		nChannels = 16;
		workLoad = new WorkLoad(1, 0.9, 0.99, "StressTest4.txt");
		schedulerSelected = SystemAttributes.ScheduleChoices.PRIORITY;
	    
		warp = SystemFactory.create(workLoad, nChannels, schedulerSelected);
		ra = warp.toReliabilityAnalysis();
		program = warp.toProgram();
		
		ArrayList<String> expectedHeaderRow = new ArrayList<>();
		//just realized the test case will be massive..
		ArrayList<String> actualHeaderRow = ra.createHeaderRow();
		
		
		
		//assertEquals(expectedHeaderRow, actualHeaderRow);
	}
	
	/**
	 * Test for the createHeaderRow method that takes as input a custom file "ExampleCustomInput1.txt", where the name of the third node in flow F0 contains the keyword "push".
	 * This assesses the edgecase in which the header may be created incorrectly due to node names containing words that overlap with keywords used by instructions.
	 */
	@Test
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	void testCreateHeaderRowPush_ExampleCustomInput1() {
		/*Initialization block for custom input.*/
		nChannels = 16;
		workLoad = new WorkLoad(0.9, 0.99, "ExampleCustomInput1.txt");
		schedulerSelected = SystemAttributes.ScheduleChoices.PRIORITY;	    
		warp = SystemFactory.create(workLoad, nChannels, schedulerSelected);
		ra = warp.toReliabilityAnalysis();
		program = warp.toProgram();
		
		ArrayList<String> expectedHeaderRow = new ArrayList<>();
		expectedHeaderRow.add("F0:A");
		expectedHeaderRow.add("F0:B");
		expectedHeaderRow.add("F0:pushC");
		expectedHeaderRow.add("F0:D");
		
		ArrayList<String> actualHeaderRow = ra.createHeaderRow();
		
		assertEquals(expectedHeaderRow, actualHeaderRow);
	}
	
	/**
	 * Test for the createHeaderRow method that takes as input a custom file "ExampleCustomInput2.txt"; this input adds a second flow, and more potential problem nodes containing "push" and "pull".
	 * This assesses the edgecase in which the header may be created incorrectly due to node names containing words that overlap with keywords used by instructions.
	 */
	@Test
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	void testCreateHeaderRowPushAndPull_ExampleCustomInput2() {
		/*Initialization block for custom input.*/
		nChannels = 16;
		workLoad = new WorkLoad(0.9, 0.99, "ExampleCustomInput2.txt");
		schedulerSelected = SystemAttributes.ScheduleChoices.PRIORITY;	    
		warp = SystemFactory.create(workLoad, nChannels, schedulerSelected);
		ra = warp.toReliabilityAnalysis();
		program = warp.toProgram();
		
		ArrayList<String> expectedHeaderRow = new ArrayList<>();
		expectedHeaderRow.add("F0:A");
		expectedHeaderRow.add("F0:B");
		expectedHeaderRow.add("F0:pushC");
		expectedHeaderRow.add("F0:D");
		expectedHeaderRow.add("F1:pullC");
		expectedHeaderRow.add("F1:pushC");
		expectedHeaderRow.add("F1:A");
		
		ArrayList<String> actualHeaderRow = ra.createHeaderRow();
		
		assertEquals(expectedHeaderRow, actualHeaderRow);
	}
	
	/**
	 * Test for the createHeaderRow method that takes as input a custom file "ExampleCustomInput3.txt"; this input has a flow with nodes containing other potential problem keywords ("sleep"/"unused"/"wait").
	 * This assesses the edgecase in which the header may be created incorrectly due to node names containing words that overlap with keywords used by instructions.
	 */
	@Test
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	void testCreateHeaderRowOtherKeywords_ExampleCustomInput3() {
		/*Initialization block for custom input.*/
		nChannels = 16;
		workLoad = new WorkLoad(0.9, 0.99, "ExampleCustomInput3.txt");
		schedulerSelected = SystemAttributes.ScheduleChoices.PRIORITY;	    
		warp = SystemFactory.create(workLoad, nChannels, schedulerSelected);
		ra = warp.toReliabilityAnalysis();
		program = warp.toProgram();
		
		ArrayList<String> expectedHeaderRow = new ArrayList<>();
		expectedHeaderRow.add("F0:sleepA");
		expectedHeaderRow.add("F0:unusedB");
		expectedHeaderRow.add("F0:pushC");
		expectedHeaderRow.add("F0:waitD");
		
		ArrayList<String> actualHeaderRow = ra.createHeaderRow();
		
		assertEquals(expectedHeaderRow, actualHeaderRow);
	}
	
	@Test
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	void testHeaderRowHashMapExample4() {
		ArrayList<String> headerRow = new ArrayList<>();
		headerRow.add("F0:A");
		headerRow.add("F0:B");
		headerRow.add("F0:C");
		headerRow.add("F0:D");
		headerRow.add("F1:C");
		headerRow.add("F1:B");
		headerRow.add("F1:A");

		HashMap<String, Integer> headerRowMap = ra.createHeaderRowHashMap(headerRow);
		
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
	
	@Test
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	void testCreateDummyRow() {
		int columnHeaderSize = workLoad.createHeader().size();
	
		ArrayList<Double> dummyRow = ra.buildDummyRow(columnHeaderSize);

		ArrayList<Double> expectedDummyRow = new ArrayList<Double>();
		expectedDummyRow.add(1.0);
		
		for (int i = 1; i < columnHeaderSize; i++) {
			expectedDummyRow.add(0.0);
		}
		
		//assertEquals(expectedDummyRow, dummyRow);
		

	}
	
	// Do not make into a test, just a playground for exploring how INstructionParameters worked
	void testing_for_creating_CreateFirstRow() {
		System.out.println(workLoad.getFlowNamesInPriorityOrder());
		ArrayList<String> flowNames = workLoad.getFlowNamesInPriorityOrder();
 		for(String flowName: flowNames) {
 			System.out.println(workLoad.getFlows().get(flowName).getNodes());
 		}


		
		Table<String,InstructionTimeSlot> scheduleTable = program.getSchedule();
		//System.out.println(scheduleTable);
		ReliabilityRow firstRow = new ReliabilityRow();

		WarpDSL dsl = new WarpDSL();	
		System.out.println(scheduleTable.getNumColumns());
		for(int row = 0; row < scheduleTable.getNumRows(); row++) {
			
			for(int col = 0; col < scheduleTable.getNumColumns(); col++) {
				String instruction = scheduleTable.get(row,col);
				
				System.out.println(instruction);
				InstructionParameters instructionObject;
	
				ArrayList<InstructionParameters> instructionsArray = new ArrayList<InstructionParameters>();
				instructionsArray = dsl.getInstructionParameters(instruction);
				
				//System.out.println(instructionsArray.get(0).getName());
				instructionObject = instructionsArray.get(0);
				// get flow should tell us whether it is UNUSED or not
				String flowName = instructionObject.getFlow();
				String snk = instructionObject.getSnk();
				
				// if it is a push or a pull, and not waiting or sleeping
				if (!flowName.equals(instructionObject.unused())) {
					// creates the HashMap value to get the current column index (the snk node)
					firstRow.add(1.0);
				}
				else {
					firstRow.add(0.0);
				}
			}
			System.out.println(firstRow);
			firstRow.clear();
			System.out.println();
		}
		
		
		ReliabilityRow expectedFirstRow = new ReliabilityRow();
		
		//in-progress
	}
	
	@Test
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	void testCreateFirstRowExample4() {
		ArrayList<String> headerRow;
		headerRow = ra.createHeaderRow();
		System.out.println(headerRow);
		
		HashMap<String,Integer> headerRowHashMap = ra.createHeaderRowHashMap(headerRow);
		System.out.println(headerRowHashMap);

		
		Table<String,InstructionTimeSlot> scheduleTable = program.getSchedule();


		ReliabilityRow expectedFirstRow = new ReliabilityRow();
		expectedFirstRow.add(1.0);
		expectedFirstRow.add(0.9);
		expectedFirstRow.add(0.0);
		expectedFirstRow.add(0.0);
		expectedFirstRow.add(1.0);
		expectedFirstRow.add(0.0);
		expectedFirstRow.add(0.0);
		System.out.println(expectedFirstRow);

		

		ReliabilityRow actualFirstRow = ra.createFirstRow(scheduleTable, headerRowHashMap);
		System.out.println(actualFirstRow);

		
		assertEquals(expectedFirstRow, actualFirstRow);

	}

	@Test
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	void testNumTxPerLinkAndTotalTxCost() {
		
	
		//ArrayList<String> actualTxPerLinkAndTotalCost = 
		
		
		
	}
	
	@Test
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	void testGetFixedTxPerLinkAndTotalTxCost() {
		
//		Flow flow = ;
//		int nNodesInFlowExpected = 
//		ArrayList<String> expectedTxPerLinkAndTotalCost = new ArrayList<String>();
//		
		
		
	}
	
	
	/**
	 * Testing the sink node state for the 
	 * F0:3, third time slot
	 */
	@Test
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	void testCalculateNewSinkNodeState() {
		
		//1.0	0.9	0.0	0.0	1.0	0.0	0.0
		ReliabilityTable reliabilities = new ReliabilityTable();
		
		double expectedNewSinkNodeState = 0.81;
		double actualNewSinkNodeState = ra.calculateNewSinkNodeState(MIN_LQ,0.0,0.9, E2E);
		
		
		assertEquals(expectedNewSinkNodeState, actualNewSinkNodeState);
		
	}
	
	/**
	 * Tests calculating a sink node 
	 * 1 row after the first period recycle
	 *
	 * 	Example4
	 *  F0:C in row 11
	 *  
	 */
	@Test
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	void testCalculateNewSinkNodeStateAfterPeriod() {
		
		//1.0	0.9	0.0	0.0	1.0	0.0	0.0
		ReliabilityTable reliabilities = new ReliabilityTable();
		
		double expectedNewSinkNodeState = 0.81;
		double actualNewSinkNodeState = ra.calculateNewSinkNodeState(MIN_LQ,0.0,0.9, E2E);
		
		System.out.println(actualNewSinkNodeState);
		
		assertEquals(expectedNewSinkNodeState, actualNewSinkNodeState);
		
	}
	
	
	@Test
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	void testCalculateNewSinkNodeStateLastEntry() {
		
		//1.0	0.9	0.0	0.0	1.0	0.0	0.0
		ReliabilityTable reliabilities = new ReliabilityTable();
		
		double expectedNewSinkNodeState = 0.9963;
		double actualNewSinkNodeState = ra.calculateNewSinkNodeState(MIN_LQ,0.9963,0.999, E2E);
		
		System.out.println(actualNewSinkNodeState);
		
		//assertEquals(expectedNewSinkNodeState, actualNewSinkNodeState);
		
	}
	
	
	@Test
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	void getFlowSizeExample4() {
		
		ArrayList<String> flowNames = workLoad.getFlowNamesInPriorityOrder();
		int expectedGetFlowSize = 4;
		int actualGetFlowSize = ra.getFlowSize(flowNames,0);
		
		assertEquals(expectedGetFlowSize, actualGetFlowSize);
		
		
	}
	
	
	@Test
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	void getFlowSizeExample1a() {
		
		
		nChannels = 16;
		workLoad = new WorkLoad(0.9, 0.99, "Example1a.txt");
		schedulerSelected = SystemAttributes.ScheduleChoices.PRIORITY;
	    
		warp = SystemFactory.create(workLoad, nChannels, schedulerSelected);
		//ReliabilityVisualization reliabilityVisualization = new ReliabilityVisualization(warp);		
		
		ra = warp.toReliabilityAnalysis();
		program = warp.toProgram();
		
		ArrayList<String> flowNames = workLoad.getFlowNamesInPriorityOrder();
		int expectedGetFlowSize = 3;
		int actualGetFlowSize = ra.getFlowSize(flowNames,0);
		
		assertEquals(expectedGetFlowSize, actualGetFlowSize);
		
		
	}
	
	
	@Test
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	void getFlowSizeStressTest4FirstFlow() {
		
		
		nChannels = 16;
		workLoad = new WorkLoad(0.9, 0.99, "StressTest4.txt");
		schedulerSelected = SystemAttributes.ScheduleChoices.PRIORITY;
	    
		warp = SystemFactory.create(workLoad, nChannels, schedulerSelected);
		//ReliabilityVisualization reliabilityVisualization = new ReliabilityVisualization(warp);		
		ra = warp.toReliabilityAnalysis();

		
		ArrayList<String> flowNames = workLoad.getFlowNamesInPriorityOrder();
		int expectedGetFlowSize = 3;
		int actualGetFlowSize = ra.getFlowSize(flowNames,0);
		
		assertEquals(expectedGetFlowSize, actualGetFlowSize);
		
		
	}
	
	@Test
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	void getFlowSizeStressTest4LastFlow() {
		
		
		nChannels = 16;
		workLoad = new WorkLoad(0.9, 0.99, "StressTest4.txt");
		schedulerSelected = SystemAttributes.ScheduleChoices.PRIORITY;
	    
		warp = SystemFactory.create(workLoad, nChannels, schedulerSelected);
		//ReliabilityVisualization reliabilityVisualization = new ReliabilityVisualization(warp);		
		ra = warp.toReliabilityAnalysis();

		
		ArrayList<String> flowNames = workLoad.getFlowNamesInPriorityOrder();
		int expectedGetFlowSize = 6;
		int actualGetFlowSize = ra.getFlowSize(flowNames,9);
		
		assertEquals(expectedGetFlowSize, actualGetFlowSize);
		
		
	}
	
	/**
	 * Tests getting the flow size of F5 in
	 * StressTest4. It is the middle flow (last flow is F10)
	 * 
	 */
	@Test
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	void getFlowSizeStressTest4MiddleFlow() {
		
		
		nChannels = 16;
		workLoad = new WorkLoad(0.9, 0.99, "StressTest4.txt");
		schedulerSelected = SystemAttributes.ScheduleChoices.PRIORITY;
	    
		warp = SystemFactory.create(workLoad, nChannels, schedulerSelected);
		//ReliabilityVisualization reliabilityVisualization = new ReliabilityVisualization(warp);		
		ra = warp.toReliabilityAnalysis();

		
		ArrayList<String> flowNames = workLoad.getFlowNamesInPriorityOrder();
		int expectedGetFlowSize = 5;
		int actualGetFlowSize = ra.getFlowSize(flowNames,4);
		
		assertEquals(expectedGetFlowSize, actualGetFlowSize);
		
		
	}
	
	@Test
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	void testCheckRowForPeriod() {
		
		ReliabilityTable actualReliabilityTable = ra.buildReliabilityTable();
		
		ArrayList<String> expectedResetPeriodFlowNames = new ArrayList<String>();
		
		expectedResetPeriodFlowNames.add("F0");
		
		
		ArrayList<String> actualResetPeriodFlowNames = ra.checkRowForPeriod(10,actualReliabilityTable.get(9), 0.99);
		
		
		assertEquals(expectedResetPeriodFlowNames, actualResetPeriodFlowNames);
	
	}
	
	
	@Test
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	void testCheckRowForPeriodStressTest4_Period1() {
		
		
		nChannels = 16;
		schedulerSelected = SystemAttributes.ScheduleChoices.PRIORITY;
		workLoad = new WorkLoad(MIN_LQ, E2E, "StressTest4.txt");
		
	    
		warp = SystemFactory.create(workLoad, nChannels, schedulerSelected);
		//ReliabilityVisualization reliabilityVisualization = new ReliabilityVisualization(warp);		
		ra = warp.toReliabilityAnalysis();
		program = warp.toProgram();

		ReliabilityTable actualReliabilityTable = ra.buildReliabilityTable();
		
		ArrayList<String> expectedResetPeriodFlowNames = new ArrayList<String>();
		
		expectedResetPeriodFlowNames.add("F1");
		
		
		ArrayList<String> actualResetPeriodFlowNames = ra.checkRowForPeriod(20,actualReliabilityTable.get(19), 0.99);
		
		
		assertEquals(expectedResetPeriodFlowNames, actualResetPeriodFlowNames);
	
	}
	
	
	/**
	 * Tests if checkRowForPeriod
	 * works for when the multiples of
	 * several flow periods occur - should
	 * return all the flows that repeat their cycle
	 */
	@Test
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	void testCheckRowForPeriodStressTest4_PeriodForMultipleFlows() {
		
		
		nChannels = 16;
		schedulerSelected = SystemAttributes.ScheduleChoices.PRIORITY;
		workLoad = new WorkLoad(MIN_LQ, E2E, "StressTest4.txt");
		
	    
		warp = SystemFactory.create(workLoad, nChannels, schedulerSelected);
		//ReliabilityVisualization reliabilityVisualization = new ReliabilityVisualization(warp);		
		ra = warp.toReliabilityAnalysis();
		program = warp.toProgram();

		ReliabilityTable actualReliabilityTable = ra.buildReliabilityTable();
		
		ArrayList<String> expectedResetPeriodFlowNames = new ArrayList<String>();
		
		expectedResetPeriodFlowNames.add("F1");
		expectedResetPeriodFlowNames.add("F2");
		expectedResetPeriodFlowNames.add("F3");
		expectedResetPeriodFlowNames.add("F7");
		expectedResetPeriodFlowNames.add("F8");
		expectedResetPeriodFlowNames.add("F9");
		expectedResetPeriodFlowNames.add("F10");
		
		
		ArrayList<String> actualResetPeriodFlowNames = ra.checkRowForPeriod(100,actualReliabilityTable.get(99), 0.99);
		
		
		assertEquals(expectedResetPeriodFlowNames, actualResetPeriodFlowNames);
	
	}
	
	
	
	
	@Test
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	void testBuildReliabilityTable() {
		

		ReliabilityTable actualReliabilityTable = ra.buildReliabilityTable();
		
		ReliabilityRow actualFirstRow = actualReliabilityTable.getFirst();
		
		
		ReliabilityRow expectedFirstRow = new ReliabilityRow();
		
		//1.0	0.9	0.0	0.0	1.0	0.0	0.0
		
		expectedFirstRow.add(1.0);
		expectedFirstRow.add(0.9);
		expectedFirstRow.add(0.0);
		expectedFirstRow.add(0.0);
		expectedFirstRow.add(1.0);
		expectedFirstRow.add(0.0);
		expectedFirstRow.add(0.0);
		
		
		ReliabilityRow actualLastRow = actualReliabilityTable.getLast();
		
		ReliabilityRow expectedLastRow = new ReliabilityRow();
		
		//1.0	0.999	0.99873	0.993627	1.0	0.999	0.9963
		
		expectedLastRow.add(1.0);
		expectedLastRow.add(0.999);
		expectedLastRow.add(0.99873);
		expectedLastRow.add(0.993627);
		expectedLastRow.add(1.0);
		expectedLastRow.add(0.999);
		expectedLastRow.add(0.9963);
		

		assertEquals(expectedFirstRow, actualFirstRow);
		assertEquals(expectedLastRow, actualLastRow);
		
		
		
	}
	
	/**
	 * 
	 * 
	 * 
	 */
	@Test
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	void testBuildReliabilityTablePeriodCheckExample4() {
	//1.0	0.9	0.0	0.0	1.0	0.999	0.9963
		
		ReliabilityTable actualReliabilityTable = ra.buildReliabilityTable();
		

		ReliabilityRow actualPeriodRow = actualReliabilityTable.get(10);
		ReliabilityRow expectedPeriodRow = new ReliabilityRow();
		
		
		expectedPeriodRow.add(1.0);
		expectedPeriodRow.add(0.9);
		expectedPeriodRow.add(0.0);
		expectedPeriodRow.add(0.0);
		expectedPeriodRow.add(1.0);
		expectedPeriodRow.add(0.999);
		expectedPeriodRow.add(0.9963);
		
		
		assertEquals(expectedPeriodRow, actualPeriodRow);
	
	}
	
	/**
	 * Test that takes as input a custom file "ExampleCustomInput1.txt", where the name of the third node in flow F0 contains the keyword "push".
	 * This assesses the edgecase in which the buildReliabilityTable method may incorrectly retrieve information from the instruction string due to node names
	 * containing certain words, e.g.: "push"/"pull". We check to ensure the final rows of values within the ReliabilityTable are as they should be.
	 */
	@Test
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	void testBuildReliabilityTableCheckNodeNamesWithPush_ExampleCustomInput1() {
		/*Initialization block for custom input.*/
		nChannels = 16;
		workLoad = new WorkLoad( 0.9, 0.99, "ExampleCustomInput1.txt");
		schedulerSelected = SystemAttributes.ScheduleChoices.PRIORITY;	    
		warp = SystemFactory.create(workLoad, nChannels, schedulerSelected);
		ra = warp.toReliabilityAnalysis();
		program = warp.toProgram();
		
		ReliabilityTable actualReliabilityTable = ra.buildReliabilityTable();
		ReliabilityRow actualRow = actualReliabilityTable.get(9);
		
		ReliabilityRow expectedRow = new ReliabilityRow();
		expectedRow.add(1.0);
		expectedRow.add(0.999);
		expectedRow.add(0.99873);
		expectedRow.add(0.993627);
		
		assertEquals(expectedRow, actualRow);
	}
	
	
	@Test
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	void testBuildReliabilitiyTableStressTest4FirstRow() {
		
		
		nChannels = 16;
		schedulerSelected = SystemAttributes.ScheduleChoices.PRIORITY;
		workLoad = new WorkLoad(MIN_LQ, E2E, "StressTest4.txt");
		
	    
		warp = SystemFactory.create(workLoad, nChannels, schedulerSelected);
		//ReliabilityVisualization reliabilityVisualization = new ReliabilityVisualization(warp);		
		ra = warp.toReliabilityAnalysis();
		program = warp.toProgram();

		
		ReliabilityTable actualReliabilityTable = ra.buildReliabilityTable();
		
			//In the correct ra file, row 163 corresponds with 170
			

			ReliabilityRow actualPeriodRow = actualReliabilityTable.get(0);
		
			ReliabilityRow expectedPeriodRow = new ReliabilityRow();
			
			expectedPeriodRow.add(1.0);
			expectedPeriodRow.add(0.9);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(1.0);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(1.0);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(1.0);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(1.0);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(1.0);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(1.0);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(1.0);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(1.0);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(0.0);

	}
	
	

	/**
	 * Instruction in row 163
	 * in StressTest4.dsl has a unique
	 * case: 
	 * if has(F1) push(F1: C -> D, #12) else pull(F5: B -> C, #12)
	 * 
	 * This test checks if the corresponding entry
	 * in the ra value is still computing the correct
	 * value after having different flows for the instruction
	 * (F1 vs. F5) 
	 * 
	 * Correct value for the 163rd time slot
	 * of F1:C should be 0.999
	 * 
	 */
	@Test
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	void testBuildReliabilitiyTablePullFlowNameDiffForPushName() {
		
		
		nChannels = 16;
		schedulerSelected = SystemAttributes.ScheduleChoices.PRIORITY;
		workLoad = new WorkLoad(MIN_LQ, E2E, "StressTest4.txt");
		
	    
		warp = SystemFactory.create(workLoad, nChannels, schedulerSelected);
		//ReliabilityVisualization reliabilityVisualization = new ReliabilityVisualization(warp);		
		ra = warp.toReliabilityAnalysis();
		program = warp.toProgram();

		
		ReliabilityTable actualReliabilityTable = ra.buildReliabilityTable();
		
			//In the correct ra file, row 163 corresponds with 170
			

			ReliabilityRow actualPeriodRow = actualReliabilityTable.get(163);
			
			// 1.0	0.999	0.9963	1.0	0.999	0.99873	0.9986571	0.9984529799999999	0.9980461979999999	0.9944323991999999	1.0	0.999	0.998001	0.9710279999999999	0.7282710000000001	0.0	1.0	0.999	0.9989001	0.0	0.0	0.0	0.0	0.0	1.0	0.999	0.8991	0.0	0.0	1.0	0.0	0.0	1.0	0.999	0.9989001	0.99880020999	0.998440605954	1.0	0.999	0.99873	0.9986571	0.9984529799999999	0.9980461979999999	0.9944323991999999	1.0	0.999	0.9989001	0.9988901109989999	0.9988891121088889	0.9988837181022895	0.9988667269815014	0.9985965681609708	1.0	0.999	0.99873	0.998001	0.99786249	0.995507091
			ReliabilityRow expectedPeriodRow = new ReliabilityRow();
			
			expectedPeriodRow.add(1.0);
			expectedPeriodRow.add(0.999);
			expectedPeriodRow.add(0.9963);
			expectedPeriodRow.add(1.0);
			expectedPeriodRow.add(0.999);
			expectedPeriodRow.add(0.99873);
			expectedPeriodRow.add(0.9986571);
			expectedPeriodRow.add(0.9984529799999999);
			expectedPeriodRow.add(0.9980461979999999);
			expectedPeriodRow.add(0.9944323991999999);
			expectedPeriodRow.add(1.0);
			expectedPeriodRow.add(0.999);
			expectedPeriodRow.add(0.998001);
			expectedPeriodRow.add(0.9710279999999999);
			expectedPeriodRow.add(0.7282710000000001);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(1.0);
			expectedPeriodRow.add(0.999);
			expectedPeriodRow.add(0.9989001);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(1.0);
			expectedPeriodRow.add(0.999);
			expectedPeriodRow.add(0.8991);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(1.0);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(0.0);
			expectedPeriodRow.add(1.0);
			expectedPeriodRow.add(0.999);
			expectedPeriodRow.add(0.9989001);
			expectedPeriodRow.add(0.99880020999);
			expectedPeriodRow.add(0.998440605954);
			expectedPeriodRow.add(1.0);
			expectedPeriodRow.add(0.999);
			expectedPeriodRow.add(0.99873);
			expectedPeriodRow.add(0.9986571);
			expectedPeriodRow.add(0.9984529799999999);
			expectedPeriodRow.add(0.9980461979999999);
			expectedPeriodRow.add(0.9944323991999999);
			expectedPeriodRow.add(1.0);
			expectedPeriodRow.add(0.999);
			expectedPeriodRow.add(0.9989001);
			expectedPeriodRow.add(0.9988901109989999);
			expectedPeriodRow.add(0.9988891121088889);
			expectedPeriodRow.add(0.9988837181022895);
			expectedPeriodRow.add(0.9988667269815014);
			expectedPeriodRow.add(0.9985965681609708);
			expectedPeriodRow.add(1.0);
			expectedPeriodRow.add(0.999);
			expectedPeriodRow.add(0.99873);
			expectedPeriodRow.add(0.998001);
			expectedPeriodRow.add(0.99786249);
			expectedPeriodRow.add(0.995507091);

			
			assertEquals(expectedPeriodRow.get(1), actualPeriodRow.get(1));
			assertEquals(expectedPeriodRow, actualPeriodRow);

	
	}

	
	
	
	
	@Test
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	void testBuildReliabilityExample4NumFaultsRow0() {

		nChannels = 16;
		schedulerSelected = SystemAttributes.ScheduleChoices.PRIORITY;
		workLoad = new WorkLoad(1, MIN_LQ, E2E, "Example4.txt");
		
	    
		warp = SystemFactory.create(workLoad, nChannels, schedulerSelected);
		//ReliabilityVisualization reliabilityVisualization = new ReliabilityVisualization(warp);		
		ra = warp.toReliabilityAnalysis();
		program = warp.toProgram();
		
		
		ReliabilityTable actualReliabilityTable = ra.buildReliabilityTable();
		
	
		

		ReliabilityRow actualFirstRow = actualReliabilityTable.get(0);
		ReliabilityRow expectedFirstRow = new ReliabilityRow();
		//1.0	0.9	0.0	0.0	1.0	0.0	0.0
		expectedFirstRow.add(1.0);
		expectedFirstRow.add(0.9);
		expectedFirstRow.add(0.0);
		expectedFirstRow.add(0.0);
		expectedFirstRow.add(1.0);
		expectedFirstRow.add(0.0);
		expectedFirstRow.add(0.0);
		

		
		assertEquals(expectedFirstRow, actualFirstRow);
		
		
		
	}
	
	
	@Test
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	void testBuildReliabilityExample4NumFaultsLast() {

		nChannels = 16;
		schedulerSelected = SystemAttributes.ScheduleChoices.PRIORITY;
		workLoad = new WorkLoad(1, MIN_LQ, E2E, "Example4.txt");
		
	    
		warp = SystemFactory.create(workLoad, nChannels, schedulerSelected);
		//ReliabilityVisualization reliabilityVisualization = new ReliabilityVisualization(warp);		
		ra = warp.toReliabilityAnalysis();
		program = warp.toProgram();
		
		
		ReliabilityTable actualReliabilityTable = ra.buildReliabilityTable();
		
	
		

		ReliabilityRow actualLastRow = actualReliabilityTable.get(19);
		ReliabilityRow expectedLastRow = new ReliabilityRow();
		//1.0	0.99	0.972	0.9477	1.0	0.99	0.972
		expectedLastRow.add(1.0);
		expectedLastRow.add(0.99);
		expectedLastRow.add(0.972);
		expectedLastRow.add(0.9477);
		expectedLastRow.add(1.0);
		expectedLastRow.add(0.99);
		expectedLastRow.add(0.972);


		
		assertEquals(expectedLastRow, actualLastRow);
		
		
		
	}
	
	
	@Test
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	void testBuildReliabilityStressTest4_M_95() {

		nChannels = 16;
		schedulerSelected = SystemAttributes.ScheduleChoices.PRIORITY;
		workLoad = new WorkLoad(0.95, E2E, "StressTest4.txt");
		
	    
		warp = SystemFactory.create(workLoad, nChannels, schedulerSelected);
		//ReliabilityVisualization reliabilityVisualization = new ReliabilityVisualization(warp);		
		ra = warp.toReliabilityAnalysis();
		program = warp.toProgram();
			
		ReliabilityTable actualReliabilityTable = ra.buildReliabilityTable();
		

		ReliabilityRow actualLastRow = actualReliabilityTable.get(299);
		ReliabilityRow expectedLastRow = new ReliabilityRow();
		

		//1.0	0.9975	0.9927500000000001	1.0	0.999875	0.9995187499999999	0.9988418749999999	0.9987882890625	0.9986610224609375	0.9965089442285155	1.0	0.999875	0.9995187499999999	0.9988418749999999	0.9987882890625	0.997210183203125	1.0	0.999875	0.9995187499999999	0.9988418749999999	0.9987882890625	0.9986610224609375	0.9984385604414062	0.995654399905664	1.0	0.9975	0.9974994062500001	0.997493171878711	0.9971318900625098	1.0	0.9975	0.9927500000000001	1.0	0.9975	0.9974996882812501	0.9974934539081983	0.997132171989849	1.0	0.999875	0.9995187499999999	0.9988418749999999	0.9987882890625	0.9986610224609375	0.9965089442285155	1.0	0.999875	0.999750015625	0.9996250468730469	0.9996072388258936	0.9995725576540625	0.9995493989138763	0.9985455781541018	1.0	0.999875	0.9995187499999999	0.9993938101562498	0.9993760062285155	0.9986825432432617

		expectedLastRow.add(1.0);
		expectedLastRow.add(0.9975);
		expectedLastRow.add(0.9927500000000001);
		expectedLastRow.add(1.0);
		expectedLastRow.add(0.999875);
		expectedLastRow.add(0.9995187499999999);
		expectedLastRow.add(0.9988418749999999);
		expectedLastRow.add(0.9987882890625);
		expectedLastRow.add(0.9986610224609375);
		expectedLastRow.add(0.9965089442285155);
		expectedLastRow.add(1.0);
		expectedLastRow.add(0.999875);
		expectedLastRow.add(0.9995187499999999);
		expectedLastRow.add(0.9988418749999999);
		expectedLastRow.add(0.9987882890625);
		expectedLastRow.add(0.997210183203125);
		expectedLastRow.add(1.0);
		expectedLastRow.add(0.999875);
		expectedLastRow.add(0.9995187499999999);
		expectedLastRow.add(0.9988418749999999);
		expectedLastRow.add(0.9987882890625);
		expectedLastRow.add(0.9986610224609375);
		expectedLastRow.add(0.9984385604414062);
		expectedLastRow.add(0.995654399905664);
		expectedLastRow.add(1.0);
		expectedLastRow.add(0.9975);
		expectedLastRow.add(0.9974994062500001);
		expectedLastRow.add(0.997493171878711);
		expectedLastRow.add(0.9971318900625098);
		expectedLastRow.add(1.0);
		expectedLastRow.add(0.9975);
		expectedLastRow.add(0.9927500000000001);
		expectedLastRow.add(1.0);
		expectedLastRow.add(0.9975);
		expectedLastRow.add(0.9974996882812501);
		expectedLastRow.add(0.9974934539081983);
		expectedLastRow.add(0.997132171989849);
		expectedLastRow.add(1.0);
		expectedLastRow.add(0.999875);
		expectedLastRow.add(0.9995187499999999);
		expectedLastRow.add(0.9988418749999999);
		expectedLastRow.add(0.9987882890625);
		expectedLastRow.add(0.9986610224609375);
		expectedLastRow.add(0.9965089442285155);
		expectedLastRow.add(1.0);
		expectedLastRow.add(0.999875);
		expectedLastRow.add(0.999750015625);
		expectedLastRow.add(0.9996250468730469);
		expectedLastRow.add(0.9996072388258936);
		expectedLastRow.add(0.9995725576540625);
		expectedLastRow.add(0.9995493989138763);
		expectedLastRow.add(0.9985455781541018);
		expectedLastRow.add(1.0);
		expectedLastRow.add(0.999875);
		expectedLastRow.add(0.9995187499999999);
		expectedLastRow.add(0.9993938101562498);
		expectedLastRow.add(0.9993760062285155);
		expectedLastRow.add(0.9986825432432617);

		
		
		assertEquals(expectedLastRow, actualLastRow);
		
		
		
		
	}
	
	/**
     * Test for the buildReliabilityTable method. Samples from table rows diagonally and
     * verifies that they contain the appropriate reliability values. This test is meant
     * to be a rigorous assessment of values in the table overall, as opposed to specific
     * individual values, hence the diagonal sampling pattern. 
     */
	@Test
    @Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
    void testBuildReliabilitiyTableExample4DiagonalRow() {
        ReliabilityTable actualReliabilityTable = ra.buildReliabilityTable();

        ReliabilityRow actualDiagonalRow = new ReliabilityRow();
        actualDiagonalRow.add(actualReliabilityTable.get(0).get(0));
        actualDiagonalRow.add(actualReliabilityTable.get(1).get(1));
        actualDiagonalRow.add(actualReliabilityTable.get(2).get(2));
        actualDiagonalRow.add(actualReliabilityTable.get(3).get(3));
        actualDiagonalRow.add(actualReliabilityTable.get(4).get(4));
        actualDiagonalRow.add(actualReliabilityTable.get(5).get(5));
        actualDiagonalRow.add(actualReliabilityTable.get(6).get(6));
        actualDiagonalRow.add(actualReliabilityTable.get(7).get(0));
        actualDiagonalRow.add(actualReliabilityTable.get(8).get(1));
        actualDiagonalRow.add(actualReliabilityTable.get(9).get(2));
        actualDiagonalRow.add(actualReliabilityTable.get(10).get(3));
        actualDiagonalRow.add(actualReliabilityTable.get(11).get(4));
        actualDiagonalRow.add(actualReliabilityTable.get(12).get(5));
        actualDiagonalRow.add(actualReliabilityTable.get(13).get(6));
        actualDiagonalRow.add(actualReliabilityTable.get(14).get(0));
        actualDiagonalRow.add(actualReliabilityTable.get(15).get(1));
        actualDiagonalRow.add(actualReliabilityTable.get(16).get(2));
        actualDiagonalRow.add(actualReliabilityTable.get(17).get(3));
        actualDiagonalRow.add(actualReliabilityTable.get(18).get(4));
        actualDiagonalRow.add(actualReliabilityTable.get(19).get(5));

        ReliabilityRow expectedDiagonalRow = new ReliabilityRow();
        expectedDiagonalRow.add(1.0);
        expectedDiagonalRow.add(0.99);
        expectedDiagonalRow.add(0.972);
        expectedDiagonalRow.add(0.9477);
        expectedDiagonalRow.add(1.0);
        expectedDiagonalRow.add(0.0);
        expectedDiagonalRow.add(0.0);
        expectedDiagonalRow.add(1.0);
        expectedDiagonalRow.add(0.999);
        expectedDiagonalRow.add(0.99873);
        expectedDiagonalRow.add(0.0);
        expectedDiagonalRow.add(1.0);
        expectedDiagonalRow.add(0.999);
        expectedDiagonalRow.add(0.9963);
        expectedDiagonalRow.add(1.0);
        expectedDiagonalRow.add(0.999);
        expectedDiagonalRow.add(0.99873);
        expectedDiagonalRow.add(0.993627);
        expectedDiagonalRow.add(1.0);
        expectedDiagonalRow.add(0.999);

        assertEquals(expectedDiagonalRow, actualDiagonalRow);

    }
		
	
	

	
}
	