package edu.uiowa.cs.warp;
import java.util.ArrayList;
import java.util.Arrays;


/**

* ReliabilityVisualization creates the visualizations for
 * the reliability analysis of the WARP program. <p>
 * 
 * CS2820 Fall 2024 Project: Implement this class to create
 * the visualizations that are requested in Warp. Your solution
 * should support both the file and Java Swing Window (gui) visualizations.
 *  *
 * I recommend using class ProgramVisualization as an example of how to implement
 * this class. Your solution will likely be similar to the code in ProgramVisualization.
 * 
 * @author sgoddard
 * @version 1.8 Fall 2024
 *  *
 */
public class ReliabilityVisualization extends VisualizationObject {

	/** Type of suffix of warp file to be created*/
	private static final String SOURCE_SUFFIX = ".ra"; 
	/** Name of object hardcoded as Reliability Analysis */
	private static final String OBJECT_NAME = "Reliability Analysis";
	/** warp object of type WarpInterface to be made from workLoad and into ReliabilityAnalysis */
	private WarpInterface warp; 
	/** ReliabilityAnalysis object that warp will be converted into */
	private ReliabilityAnalysis ra; 
	/** Program object to be made from the warp object*/
	private Program program;
	/**sourceTable to be givent ra attributes and reliabilities */
	private ReliabilityTable sourceTable;
	/** WorkLoad object for warp to be converted into*/
	private WorkLoad workLoad;
	/** Name of flows from a given workLoad, in order of each flow's priority number*/
	private ArrayList<String> flowNames; 
	
	/**
	 * Constructor for ReliabilityVisualization that sets the 
	 * workLoad, program, ra, sourceCode for the warp program. <br>
	 * 
	 * @param warp warp program to visualize <br>
	 */
	ReliabilityVisualization(WarpInterface warp) {
		super(new FileManager(), warp, SOURCE_SUFFIX);
   		this.workLoad = warp.toWorkload();
   		this.program = warp.toProgram();
   		this.ra = warp.toReliabilityAnalysis();
   		this.sourceTable = ra.getReliabilities();	
   		this.flowNames = workLoad.getFlowNamesInPriorityOrder(); 
   	}
	
	/**
	 * Function creates and returns a GuiVisualization object. <br>
	 */
	public GuiVisualization displayVisualization() {
		System.out.println("9");
		return new GuiVisualization(createTitle(), createColumnHeader(), createVisualizationData());
	}
	
	/**
	 * Function creates and returns a header description for the warp program that includes the title, 
	 * scheduler name, number of faults, minimum packet reception rate, E2E, 
	 * and number of channels the schedule has. <br>
	 * 
	 * @return header A Description object that contains the header info
	 */
	public Description createHeader() {
		
	   Description header = new Description();
	   
	   header.add(createTitle());
	   header.add(String.format("Scheduler Name: %s\n", program.getSchedulerName()));
	   /* The following parameters are output based on a special schedule or the fault model */
	   if (program.getNumFaults() > 0) { // only specify when deterministic fault model is assumed
		   header.add(String.format("numFaults: %d\n", program.getNumFaults()));
	   }
	   header.add(String.format("M: %s\n", String.valueOf(program.getMinPacketReceptionRate())));
	   header.add(String.format("E2E: %s\n", String.valueOf(program.getE2e())));
	   header.add(String.format("nChannels: %d\n", program.getNumChannels()));
	   
	   return header;
	}
	
	/**
	 * Function that gets the names of the nodes ordered alphabetically, 
	 * then stores and returns the names of the nodes in an array of Strings, 
	 * used for column headers of table when visualizing data. <br>
	 * 
	 * @return columnNames An array of strings that represent the column header
	 */
	public String[] createColumnHeader() {
		
		
		int totalNodes = sourceTable.getNumColumns();
		
		String[] columnNames = new String[totalNodes];
		
		int index = 0;
		for (String flowName : flowNames) {
			
			ArrayList<Node> nodes = workLoad.getFlows().get(flowName).getNodes();
			
			for (Node node : nodes) {
				columnNames[index] = flowName + ":" + node.getName();
				index++;
			}
		}
		return columnNames;
	}

	
	/**
	 * Creates a 2D array that represents the reliability analysis.
	 * Rows in table represent the time slots, 
	 * and columns represent the reliability values for each flow.
	 * 
	 * @return 2D array containing visualization data
	 */
	public String[][] createVisualizationData() {
		
		String[][] visualizationData= null;
		if (visualizationData == null) {
			int numRows = sourceTable.getNumRows();
			int numColumns = sourceTable.getNumColumns();
			visualizationData = new String[numRows][numColumns];
			for (int row = 0; row < numRows; row++) {
		        for (int column = 0; column < numColumns; column++) {
		        	visualizationData[row][column] = Double.toString(sourceTable.get(row, column));
		        }
			}
		}
		return visualizationData;
	}

	/**
	 * Creates and returns the title header for a Reliability Analysis graph.
	 * 
	 * @return String that containing the graph title
	 */
	public String createTitle() {
		
	   // TODO implement this operation
		return String.format("Reliability Analysis for graph %s\n",program.getName());
	}
	
/* File Visualization for workload defined in Example.txt follows. 
 * Your output in the file ExamplePriority-0.9M-0.99E2E.ra
 * should match this output, where \tab characters are used as column
 * delimiters.
Reliability Analysis for graph Example created with the following parameters:
Scheduler Name:	Priority
M:	0.9
E2E:	0.99
nChannels:	16
F0:A	F0:B	F0:C	F1:C	F1:B	F1:A
1.0		0.9		0.0		1.0		0.0		0.0
1.0		0.99	0.81	1.0		0.0		0.0
1.0		0.999	0.972	1.0		0.0		0.0
1.0		0.999	0.9963	1.0		0.0		0.0
1.0		0.999	0.9963	1.0		0.9		0.0
1.0		0.999	0.9963	1.0		0.99	0.81
1.0		0.999	0.9963	1.0		0.999	0.972
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
1.0		0.999	0.9963	1.0		0.999	0.9963
*/
}
