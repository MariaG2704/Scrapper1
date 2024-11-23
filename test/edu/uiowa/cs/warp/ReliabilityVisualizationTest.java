package edu.uiowa.cs.warp;

import static org.junit.Assert.assertArrayEquals;
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

import edu.uiowa.cs.warp.SystemAttributes.ScheduleChoices;



class ReliabilityVisualizationTest {
	

//	private static final String String = null;
	private GuiVisualization gVis;
	
	/*
	 public GuiVisualization displayVisualization() {
		return new GuiVisualization(createTitle(), createColumnHeader(), createVisualizationData());
	}
	 */
	private static final Double MIN_LQ = 0.9;
	private static final Double E2E = 0.99;
	private static final String INPUT_FILE = "StressTest4.txt";
	private static final long TIMEOUT_IN_MILLISECONDS = 10000;
	
	private WorkLoad workLoad;

	private Integer nChannels = 16;
	private ScheduleChoices schedulerSelected = SystemAttributes.ScheduleChoices.PRIORITY;
	private WarpInterface warp;

	
	 
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
		
	
	 }
		
	
	
	/**
	 * Test for the displayVisualization() method checking that output of method is not null.
	 * Since the method just returns a GuiVisualization created with parameters provided by createTitle(), createColumnHeader(), and createVisualizationData() methods, 
	 * testing these methods individually should be sufficient for guaranteeing the correctness of the output; 
	 * testing the GuiVisualization constructor is outside of the scope of ReliabilityVisualization testing.
	 */
	@Test
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	void testDisplayVisualization_NotNull() {
		// gVis = new GuiVisualization(String s, String[] a, String[][] b);
		ReliabilityVisualization reliabilityVisualization = new ReliabilityVisualization(warp);
		assertNotNull(reliabilityVisualization.displayVisualization());
	}
	
	
	/**
	 * Test for the displayVisualization() method checking that output of method is a GuiVisualization object.
	 * Since the method just returns a GuiVisualization created with parameters provided by createTitle(), createColumnHeader(), and createVisualizationData() methods, 
	 * testing these methods individually should be sufficient for guaranteeing the correctness of the output; 
	 * testing the GuiVisualization constructor is outside of the scope of ReliabilityVisualization testing.
	 */
	@Test
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	void testDisplayVisualization_InstanceOfGuiVisualization() {
		// gVis = new GuiVisualization(String s, String[] a, String[][] b);
		ReliabilityVisualization reliabilityVisualization = new ReliabilityVisualization(warp);
		assertTrue(reliabilityVisualization.displayVisualization() instanceof GuiVisualization);
	}
	
	/**
	 * Test for the createHeader method. Verifies that the createHeader() method outputs a Description-type object.
	 */
	@Test
	void testCreateHeader_IsDescription() {
		ReliabilityVisualization reliabilityVisualization = new ReliabilityVisualization(warp);
		assertTrue(reliabilityVisualization.createHeader() instanceof Description);	
		
	}
	
	/**
	 * Test for the createHeader method. Verifies that the header created from ReliabilityVisualization 
	 * made from input warp contains the appropriate contents and formatting.
	 */
	@Test
	void testCreateHeader_Contents() {	
		

		ReliabilityVisualization reliabilityVisualization = new ReliabilityVisualization(warp);
	
		//System.out.println(reliabilityVisualization.createHeader());
		
		Description desc = new Description();
		desc.add("Reliability Analysis for graph StressTest4\n");
		desc.add("Scheduler Name: Priority\n");
		desc.add("M: 0.9\n");
		desc.add("E2E: 0.99\n");
		desc.add("nChannels: 16\n");
	
		assertEquals(desc, reliabilityVisualization.createHeader());		
	}
	
	/**
	 * Test for the createHeader method. Basic test verifies that the method returns an object that is not Null.
	 */
	@Test
	void testCreateHeader_IsNotNull() {
		
		ReliabilityVisualization reliabilityVisualization = new ReliabilityVisualization(warp);
		
		assertNotNull(reliabilityVisualization.createHeader());
		
	
	}
	
	
	/**
	 * Testing without numFaultmodel for input file Example1a.
	 */
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	@Test
	void testCreateHeader_Example1a() {
		
		nChannels = 16;
		workLoad = new WorkLoad(0.9, 0.99, "Example1a.txt");
		schedulerSelected = SystemAttributes.ScheduleChoices.PRIORITY;  
		warp = SystemFactory.create(workLoad, nChannels, schedulerSelected);
		ReliabilityVisualization reliabilityVisualization = new ReliabilityVisualization(warp);
	
		
		Description desc = new Description();
		desc.add("Reliability Analysis for graph Example1A\n");
		desc.add("Scheduler Name: Priority\n");
		desc.add("M: 0.9\n");
		desc.add("E2E: 0.99\n");
		desc.add("nChannels: 16\n");
		
		
		//System.out.println(reliabilityVisualization.createHeader());
		
		assertEquals(desc, reliabilityVisualization.createHeader(), "Header was not created properly for Example1a.txt , no numFaults" );
		
		
	}
	

	/**
	 * Testing with a different M value for StressTest4
	 */
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	@Test
	void testCreateHeader_StressTest4_M() {
		
		nChannels = 16;
		workLoad = new WorkLoad(0.95, 0.99, "StressTest4.txt");
		schedulerSelected = SystemAttributes.ScheduleChoices.PRIORITY;  
		warp = SystemFactory.create(workLoad, nChannels, schedulerSelected);
		ReliabilityVisualization reliabilityVisualization = new ReliabilityVisualization(warp);
	
		
		Description desc = new Description();
		desc.add("Reliability Analysis for graph StressTest4\n");
		desc.add("Scheduler Name: Priority\n");
		desc.add("M: 0.95\n");
		desc.add("E2E: 0.99\n");
		desc.add("nChannels: 16\n");
		
		
		//System.out.println(reliabilityVisualization.createHeader());
		
		assertEquals(desc, reliabilityVisualization.createHeader(), 
				"Header was not created properly for StressTest4, testing with a input file with a different M value (0.95)" );
	
		
	}
	
	
	/**
	 * Testing creating header with num Fault model for Example1a
	 */
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	@Test
	void testCreateHeader_Example1a_NumFaultModel() {
		
		nChannels = 16;
		workLoad = new WorkLoad(1, 0.9, 0.99, "Example1a.txt");
		schedulerSelected = SystemAttributes.ScheduleChoices.PRIORITY;	    
		warp = SystemFactory.create(workLoad, nChannels, schedulerSelected);
		ReliabilityVisualization reliabilityVisualization = new ReliabilityVisualization(warp);		
		
		Description desc = new Description();
		desc.add("Reliability Analysis for graph Example1A\n");
		desc.add("Scheduler Name: Priority\n");
		desc.add("numFaults: 1\n");
		desc.add("M: 0.9\n");
		desc.add("E2E: 0.99\n");
		desc.add("nChannels: 16\n");
		
		//System.out.println(reliabilityVisualization.createHeader());
		
		assertEquals(desc, reliabilityVisualization.createHeader(), 
				"Header was not created properly for Example1a.txt - numFault model is on");
		
		
	}
	
	
	
	/**
	 * NumFault Model on
	 */
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	@Test
	void testCreateColumnHeader_Example1a() {
		
		nChannels = 16;
		workLoad = new WorkLoad(1, 0.9, 0.99, "Example1a.txt");
		schedulerSelected = SystemAttributes.ScheduleChoices.PRIORITY;   
		warp = SystemFactory.create(workLoad, nChannels, schedulerSelected);		
		ReliabilityVisualization reliabilityVisualization = new ReliabilityVisualization(warp);		


		ArrayList<String> desc = new ArrayList<String>();
		
		desc.add("F0:A");
		desc.add("F0:B");
		desc.add("F0:C");
		desc.add("F1:C");
		desc.add("F1:B");
		desc.add("F1:A");
		

		String actualColumnHeaderString = Arrays.toString(reliabilityVisualization.createColumnHeader());
		String expectedColumnHeaderString = desc.toString();
		
		
		assertEquals(expectedColumnHeaderString, actualColumnHeaderString, 
				"ColumnHeader was not created properly for Example1a.txt - testing with NumFault model");
		
		
	}
	
	
		
	/**
	 * Tests creating a column header for a different file
	 * with a relatively large amount of columns
	 */
	void testCreateColumnHeader_StressTest() {
			
			ReliabilityVisualization reliabilityVisualization = new ReliabilityVisualization(warp);		
		
		
			//Description desc = new Description();
			ArrayList<String> desc = new ArrayList<String>();
			
			desc.add("F1:B");
			desc.add("F1:C");
			desc.add("F1:D");
			desc.add("F2:C");
			desc.add("F2:D");
			desc.add("F2:E");
			desc.add("F2:F");
			desc.add("F2:G");
			desc.add("F2:H");
			desc.add("F2:I");
			desc.add("F3:C");
			desc.add("F3:D");
			desc.add("F3:E");
			desc.add("F3:J");
			desc.add("F3:K");
			desc.add("F3:L");
			desc.add("F4:A");
			desc.add("F4:B");
			desc.add("F4:C");
			desc.add("F4:D");
			desc.add("F4:E");
			desc.add("F4:J");
			desc.add("F4:K");
			desc.add("F4:L");
			desc.add("F5:A");
			desc.add("F5:B");
			desc.add("F5:C");
			desc.add("F5:D");
			desc.add("F5:E");
			desc.add("F6:B");
			desc.add("F6:C");
			desc.add("F6:D");
			desc.add("F7:A");
			desc.add("F7:B");
			desc.add("F7:C");
			desc.add("F7:D");
			desc.add("F7:E");
			desc.add("F8:C");
			desc.add("F8:D");
			desc.add("F8:E");
			desc.add("F8:F");
			desc.add("F8:G");
			desc.add("F8:H");
			desc.add("F8:I");
			desc.add("F9:A");
			desc.add("F9:B");
			desc.add("F9:C");
			desc.add("F9:D");
			desc.add("F9:E");
			desc.add("F9:J");
			desc.add("F9:K");
			desc.add("F9:L");
			desc.add("F10:C");
			desc.add("F10:D");
			desc.add("F10:E");
			desc.add("F10:J");
			desc.add("F10:K");
			desc.add("F10:L");

			String actualColumnHeaderString = Arrays.toString(reliabilityVisualization.createColumnHeader());
			String expectedColumnHeaderString = desc.toString();
			
			
			assertEquals(expectedColumnHeaderString, actualColumnHeaderString, 
					"ColumnHeader was not created properly for StressTest4.txt");
			
			
		}
	
	
	
	
	
	
	/**
	 * Testing a file without a numfault model
	 */
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	@Test
	void testCreateVisualizationDataDummyTable_Example1a() {
		
		nChannels = 16;
		workLoad = new WorkLoad(1, 0.9, 0.99, "Example1a.txt");
		schedulerSelected = SystemAttributes.ScheduleChoices.PRIORITY; 
		warp = SystemFactory.create(workLoad, nChannels, schedulerSelected);
		ReliabilityVisualization reliabilityVisualization = new ReliabilityVisualization(warp);		
		
		System.out.println("test");
		
		//System.out.println(Arrays.toString(reliabilityVisualization.createVisualizationData()));
		
		
		int numRows = 20;
		int numColumns = 7;
		
		
		Program program = warp.toProgram();
		
		//String[][] expectedData = new String[numRows][numColumns];
		
		String[][] createdData = reliabilityVisualization.createVisualizationData();
		
		int actualNumRows = createdData.length;
		int actualNumColumns = createdData[0].length;

		for(int i = 0; i < numRows; i++) {
			assertEquals("1.0", createdData[i][0], "First entry in each row should be 1.0");
		}	
		assertEquals(numRows, actualNumRows, "Number of rows not properly calculated for Example1a.txt");
		assertEquals(numColumns, actualNumColumns, "Number of columns not properly calculated for Example1a.txt");
	
		
			
			
	}
	
	/**
	 * Tests making the dummy table for a larger input file
	 */
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	@Test
	void testCreateVisualizationDataDummyTable_StressTest4() {
		
		ReliabilityVisualization reliabilityVisualization = new ReliabilityVisualization(warp);		
		
		
		//System.out.println(Arrays.toString(reliabilityVisualization.createVisualizationData()));
		
		//Counted from output file, posted on ICON
		int numRows = 300;
		int numColumns = 59;
		
		
		Program program = warp.toProgram();
		
		String[][] createdData = reliabilityVisualization.createVisualizationData();
		
		int actualNumRows = createdData.length;
		int actualNumColumns = createdData[0].length;
		
		for(int i = 0; i < numRows; i++) {
			assertEquals("1.0", createdData[i][0], "First entry in each row should be 1.0");
		}	
		assertEquals(numRows, actualNumRows, "Number of rows not properly calculated for StressTest4.txt");
		assertEquals(numColumns, actualNumColumns, "Number of columns not properly calculated for StressTest4.txt");
			
			
	}
	
	
	/**
	 * Makes sure that even with a NumFaultModel, the number of rows is 
	 * properly counted - risk of a "off-by-one" situation is possible since
	 * it takes an extra line to print the numFaults: 1
	 */
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	@Test
	void testCreateVisualizationDataDummyTable_Example1a_NumFault() {
		
		nChannels = 16;
		workLoad = new WorkLoad(0.9, 0.99, "Example4.txt");
		schedulerSelected = SystemAttributes.ScheduleChoices.PRIORITY;
	    
		warp = SystemFactory.create(workLoad, nChannels, schedulerSelected);
		
		ReliabilityVisualization reliabilityVisualization = new ReliabilityVisualization(warp);		
		
		
		//System.out.println(Arrays.toString(reliabilityVisualization.createVisualizationData()));
		
		//Counted from output file, posted on ICON
		int numRows = 20;
		int numColumns = 8;
		
		
		Program program = warp.toProgram();
		
		String[][] createdData = reliabilityVisualization.createVisualizationData();
		
		int actualNumRows = createdData.length;
		int actualNumColumns = createdData[0].length;
		
		for(int i = 0; i < numRows; i++) {
			assertEquals("1.0", createdData[i][0], "First entry in each row should be 1.0");
		}	
		assertEquals(numRows, actualNumRows, "Number of rows not properly calculated for Example4.txt- inputFile is a NumFault model");
		assertEquals(numColumns, actualNumColumns, "Number of columns not properly calculated for Example4.txt - inputFile is a NumFault model");
			
			
	}
	


	
	/**
	 * Not numfault model
	 * 
	 */
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	@Test
	void testCreateTitle_Example1a(){
		
		nChannels = 16;
		workLoad = new WorkLoad(0.9, 0.99, "Example1a.txt");
		schedulerSelected = SystemAttributes.ScheduleChoices.PRIORITY;
	    
		warp = SystemFactory.create(workLoad, nChannels, schedulerSelected);
		
		ReliabilityVisualization reliabilityVisualization = new ReliabilityVisualization(warp);	
		
		String actualTitle = reliabilityVisualization.createTitle();
		
		//Really not sure if this one is correct because this could result in extra spaces between lines
		String expectedTitle = "Reliability Analysis for graph Example1A\n";
		
		
		System.out.print(actualTitle);
		System.out.print(expectedTitle);
		
		
		assertEquals(expectedTitle, actualTitle, 
				"Titles either don't match or there is a newline error for Example1a.txt - not a numFaultmodel");
		
	
	}
	
	/**
	 * Not a numfault model
	 * Tests the create title for a different, much larger, input file
	 */
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	@Test
	void testCreateTitle_StressTest4(){
		
	
		ReliabilityVisualization reliabilityVisualization = new ReliabilityVisualization(warp);	
		
		String actualTitle = reliabilityVisualization.createTitle();
		//Really not sure if this one is correct because this could result in extra spaces between lines
		String expectedTitle = "Reliability Analysis for graph StressTest4\n";
		
		
		System.out.print(actualTitle);
		System.out.print(expectedTitle);
		
		
		assertEquals(expectedTitle, actualTitle, 
				"Titles either don't match or there is a newline error for StressTest4 - testing with a larger input");
		
	
	}
	
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	@Test
	void testCreateTitle_(){
		
		nChannels = 16;
		workLoad = new WorkLoad(1, 0.9, 0.99, "Example4.txt");
		schedulerSelected = SystemAttributes.ScheduleChoices.PRIORITY;
	    
		warp = SystemFactory.create(workLoad, nChannels, schedulerSelected);
		
		ReliabilityVisualization reliabilityVisualization = new ReliabilityVisualization(warp);	
		
		String actualTitle = reliabilityVisualization.createTitle();
		
		//Really not sure if this one is correct because this could result in extra spaces between lines
		String expectedTitle = "Reliability Analysis for graph Example4\n";
		
		System.out.print(actualTitle);
		System.out.print(expectedTitle);
		
		
		assertEquals(expectedTitle, actualTitle, 
				"Titles either don't match or there is a newline error for Example4.txt - testing with a NumFault model");
		
	
	}
	
	
	

	/* Saving this test for when reliabilityAnalysis gets implemented
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	@Test
	void testCreateVisualizationData() {
		
		nChannels = 16;
		workLoad = new WorkLoad(1, 0.9, 0.99, "Example1a.txt");
		schedulerSelected = SystemAttributes.ScheduleChoices.PRIORITY;
	    
		warp = SystemFactory.create(workLoad, nChannels, schedulerSelected);
		
		ReliabilityVisualization reliabilityVisualization = new ReliabilityVisualization(warp);		
		
		System.out.println("test");
		
		//System.out.println(Arrays.toString(reliabilityVisualization.createVisualizationData()));
		
		int numRows = 20;
		int numColumns = 6;
		
		String[][] table = new String[numRows][numColumns];
		
		//reliabilitytable vs source table 
		// entries, some kind of value, matches what you got from reliabilityTable 
		// dont test for a good or bad value,
		// table built should match for that entry  fort he reliabilityAnalysis
		// print system error 
		
		String[] row1 = {"1.0", "0.9", "0.0", "1.0", "0.0", "0.0"};
		String[] row2 = {"1.0", "0.99", "0.81", "1.0", "0.0", "0.0"};
		String[] row3 = {"1.0", "0.999", "0.972", "1.0", "0.0", "0.0"};
		String[] row4 = {"1.0", "0.999", "0.9963", "1.0", "0.0", "0.0"};
		String[] row5 = {"1.0", "0.999", "0.9963", "1.0", "0.9", "0.0"};
		String[] row6 = {"1.0", "0.999", "0.9963", "1.0", "0.99", "0.81"};
		String[] row7 = {"1.0", "0.999", "0.9963", "1.0", "0.999", "0.972"};
		String[] row8 = {"1.0", "0.999", "0.9963", "1.0", "0.999", "0.9963"};
		String[] row9 = {"1.0", "0.999", "0.9963", "1.0", "0.999", "0.9963"};
		String[] row10 = {"1.0", "0.999", "0.9963", "1.0", "0.999", "0.9963"};
		String[] row11 = {"1.0", "0.9", "0.0", "1.0", "0.999", "0.9963"};
		String[] row12 = {"1.0", "0.99", "0.81", "1.0", "0.999", "0.9963"};
		String[] row13 = {"1.0", "0.999", "0.972", "1.0", "0.999", "0.9963"};
		String[] row14 = {"1.0", "0.999", "0.9963", "1.0", "0.999", "0.9963"};
		String[] row15 = {"1.0", "0.999", "0.9963", "1.0", "0.999", "0.9963"};
		String[] row16 = {"1.0", "0.999", "0.9963", "1.0", "0.999", "0.9963"};
		String[] row17 = {"1.0", "0.999", "0.9963", "1.0", "0.999", "0.9963"};
		String[] row18 = {"1.0", "0.999", "0.9963", "1.0", "0.999", "0.9963"};
		String[] row19 = {"1.0", "0.999", "0.9963", "1.0", "0.999", "0.9963"};
		String[] row20 = {"1.0", "0.999", "0.9963", "1.0", "0.999", "0.9963"};
		
		table[0] = row1;
		table[1] = row2;
		table[2] = row3;
		table[3] = row4;
		table[4] = row5;
		table[5] = row6;
		table[6] = row7;
		table[7] = row8;
		table[8] = row9;
		table[9] = row10;
		table[10] = row11;
		table[11] = row12;
		table[12] = row13;
		table[13] = row14;
		table[14] = row15;
		table[15] = row16;
		table[16] = row17;
		table[17] = row18;
		table[18] = row19;
		table[19] = row20;

		
		
		System.out.println("Testing visualization data below");
		
		for(int row = 0 ; row < numRows; row++) {
			
			for(int col = 0; col < numColumns; col++) {
				
				System.out.print(table[row][col] + ", "); 	
				
			}
			System.out.println("");
			
		}
		
		//assertEquals(Arrays.toString(table), Arrays.toString(reliabilityVisualization.createVisualizationData()));
		//assertArrayEquals(table, reliabilityVisualization.createVisualizationData());
			
	}
	*/
	
	
}
