package edu.uiowa.cs.warp;
import java.util.ArrayList;


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
public class ReliabilityVisualization  extends VisualizationObject {
	
	private static final String SOURCE_SUFFIX = ".ra"; //file type of warp file
	private static final String OBJECT_NAME = "Reliability Analysis";
	private WarpInterface warp; //the warp program
	private ReliabilityAnalysis ra; //reliability analysis object
	private Program program;
	private ReliabilityTable sourceCode;
	private WorkLoad workLoad;
	
	ReliabilityVisualization(WarpInterface warp) {
		super(new FileManager(), warp, SOURCE_SUFFIX);
   		this.warp = warp;
   		this.workLoad = warp.toWorkload();
   		this.program = warp.toProgram();
   		this.ra = warp.toReliabilityAnalysis();
   		this.sourceCode = ra.getReliabilities();	
   	}
	
	// TODO Auto-generated class stub for unimplemented visualization
	public GuiVisualization displayVisualization() {
		return new GuiVisualization(createTitle(), createColumnHeader(), createVisualizationData());
	}
	
	public Description createHeader() {
	   // TODO implement this operation
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
	
	public String[] createColumnHeader() {
	   // TODO implement this operation
		ArrayList<String> flowNames = workLoad.getFlowNamesInPriorityOrder(); //need to access the flowNames in order from flow
		String[] columnNames = new String[flowNames.size() + 1];
		columnNames[0] = "Time Slot"; // first spot should be "Time Slot"
		
		//populating with flow names
		int index = 1;
		for (String flowName : flowNames) {
			columnNames[index] = flowName;
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
			int numRows = sourceCode.getNumRows();
			int numColumns = sourceCode.getNumColumns();
			visualizationData = new String[numRows][numColumns];
			
			for (int row = 0; row < numRows; row++) {
				visualizationData[row][0] = String.format("%s", row);
		        for (int column = 0; column < numColumns; column++) {
		          visualizationData[row][column + 1] = Double.toString(sourceCode.get(row, column));
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
		return String.format("Reliability Analysis for graph %s\n", warp.getName());
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
