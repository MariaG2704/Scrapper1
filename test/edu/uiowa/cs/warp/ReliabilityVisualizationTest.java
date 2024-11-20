package edu.uiowa.cs.warp;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import edu.uiowa.cs.warp.SystemAttributes.ScheduleChoices;

class ReliabilityVisualizationTest {
	

//	private static final String String = null;
	private GuiVisualization gVis;
	
	
	/*
	 public GuiVisualization displayVisualization() {
		return new GuiVisualization(createTitle(), createColumnHeader(), createVisualizationData());
	}
	 */
	

	 private WorkLoad workLoad;


	private Integer nChannels = 16;

	private ScheduleChoices schedulerSelected = SystemAttributes.ScheduleChoices.RM;
	private WarpInterface warp;
	 
	
	@BeforeEach
	void setUp() throws Exception {
	    //Initialize workload with default values given in WorkLoad.java comment
		workLoad = new WorkLoad(0.9, 0.99, "StressTest.txt");
	    //Captures System.out prints to check if printed warnings are correctly output
	    //Better way would be to return the warning string from the methods themselves in Workload.java
	    
		warp = SystemFactory.create(workLoad, nChannels, schedulerSelected);
	
	 }
	
	@Test
	void testDisplayVisualization() {
		
		// gVis = new GuiVisualization(String s, String[] a, String[][] b);
		
		
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
		
		
		System.out.println(reliabilityVisualization.createHeader());
		
		Description desc = new Description();
		desc.add("Reliability Analysis for graph StressTest\n");
		desc.add("Scheduler Name: RateMonotonic\n");
		desc.add("M: 0.9\n");
		desc.add("E2E: 0.99\n");
		desc.add("nChannels: 16\n");
	
		
		assertEquals(desc, reliabilityVisualization.createHeader());
		
		
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
