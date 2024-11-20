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

import edu.uiowa.cs.warp.SystemAttributes.ScheduleChoices;

class ReliabilityVisualizationTest {
	

//	private static final String String = null;
	private GuiVisualization gVis;
	
	
	/*
	 public GuiVisualization displayVisualization() {
		return new GuiVisualization(createTitle(), createColumnHeader(), createVisualizationData());
	}
	 */
	

	 private static final long TIMEOUT_IN_MILLISECONDS = 10000;
	 private WorkLoad workLoad;


	private Integer nChannels = 16;

	private ScheduleChoices schedulerSelected = SystemAttributes.ScheduleChoices.PRIORITY;
	private WarpInterface warp;
	 
	
	@BeforeEach
	void setUp() throws Exception {
	    //Initialize workload with default values given in WorkLoad.java comment
		workLoad = new WorkLoad(0.9, 0.99, "StressTest4.txt");
	    
		warp = SystemFactory.create(workLoad, nChannels, schedulerSelected);
	
	 }
	
	@Test
	void testDisplayVisualization() {
		
		// gVis = new GuiVisualization(String s, String[] a, String[][] b);
		
		//not done
	}
	
	@Test
	void testCreateHeader() {	
		/*
		 * Reliability Analysis for graph StressTest
			Scheduler Name: RateMonotonic
			M: 0.9
			E2E: 0.99
			nChannels: 3
		 */
		
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
	
	@Test
	void testCreateHeaderNotNull() {
		
		ReliabilityVisualization reliabilityVisualization = new ReliabilityVisualization(warp);
		
		assertNotNull(reliabilityVisualization.createHeader());
		
		
	}
	
	
	//do metrics change based on the fault model we use
	
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	@Test
	void testCreateHeader2() {
		
		nChannels = 16;
		workLoad = new WorkLoad(0.9, 0.99, "Example1a.txt");
		schedulerSelected = SystemAttributes.ScheduleChoices.PRIORITY;
	    
		warp = SystemFactory.create(workLoad, nChannels, schedulerSelected);
		
		Description desc = new Description();
		desc.add("Reliability Analysis for graph Example1A\n");
		desc.add("Scheduler Name: Priority\n");
		desc.add("M: 0.9\n");
		desc.add("E2E: 0.99\n");
		desc.add("nChannels: 16\n");
		
		ReliabilityVisualization reliabilityVisualization = new ReliabilityVisualization(warp);		
		
		System.out.println(reliabilityVisualization.createHeader());
		
		assertEquals(desc, reliabilityVisualization.createHeader());
		
		
	}
	
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	@Test
	void testCreateHeaderWithNumFaultModel() {
		
		nChannels = 16;
		workLoad = new WorkLoad(1, 0.9, 0.99, "Example1a.txt");
		schedulerSelected = SystemAttributes.ScheduleChoices.PRIORITY;
	    
		warp = SystemFactory.create(workLoad, nChannels, schedulerSelected);
		
		Description desc = new Description();
		desc.add("Reliability Analysis for graph Example1A\n");
		desc.add("Scheduler Name: Priority\n");
		desc.add("numFaults: 1\n");
		desc.add("M: 0.9\n");
		desc.add("E2E: 0.99\n");
		desc.add("nChannels: 16\n");
		
		ReliabilityVisualization reliabilityVisualization = new ReliabilityVisualization(warp);		
		
		System.out.println(reliabilityVisualization.createHeader());
		
		assertEquals(desc, reliabilityVisualization.createHeader());
		
		
	}
	
	
	@Timeout(value = TIMEOUT_IN_MILLISECONDS, unit = TimeUnit.MILLISECONDS)
	@Test
	void testGetTotalNumberOfNodes() {
		
		nChannels = 16;
		workLoad = new WorkLoad(0.9, 0.99, "Example1a.txt");
		schedulerSelected = SystemAttributes.ScheduleChoices.PRIORITY;
	    
		warp = SystemFactory.create(workLoad, nChannels, schedulerSelected);
		
		ReliabilityVisualization reliabilityVisualization = new ReliabilityVisualization(warp);		
		
		ArrayList<String> flowNames = new ArrayList<String>();
		flowNames.add("F0");
		flowNames.add("F1");
		
		System.out.println(reliabilityVisualization.getTotalNumberOfNodes(flowNames, workLoad));
		
		int actualNodes = reliabilityVisualization.getTotalNumberOfNodes(flowNames, workLoad);
		
		assertEquals(6, actualNodes);
		
	}
	
		
	
	
	//just test creating a ra file and see what an output likes
	// same structure as WorkLoadTest.java
	//
	
	/*
	 * General test of running reliaibility analays
	 * k-Fault model and -e and -m
	 * What tings are for ReliabilityVisualization - does it have a title
	 * does it remove
	 * 
	 */
	
}
