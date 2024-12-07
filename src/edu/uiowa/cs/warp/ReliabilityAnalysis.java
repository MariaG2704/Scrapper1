package edu.uiowa.cs.warp;
import java.util.Set;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;

import edu.uiowa.cs.warp.WarpDSL.InstructionParameters;

import java.util.Collection;

/**

* ReliabilityAnalysis analyzes the end-to-end reliability of messages transmitted in flows for the
 * WARP system.
 * <p>
 * 
 * Let M represent the Minimum Packet Reception Rate on an edge in a flow. The end-to-end
 * reliability for each flow, flow:src->sink, is computed iteratively as follows:<br>
 * (1)The flow:src node has an initial probability of 1.0 when it is released. All other initial
 * probabilities are 0.0. (That is, the reset of the nodes in the flow have an initial probability
 * value of 0.0.) <br>
 * (2) each src->sink pair probability is computed as NewSinkNodeState = (1-M)*PrevSnkNodeState +
 * M*PrevSrcNodeState <br>
 * This value represents the probability that the message as been received by the node SinkNode.
 * Thus, the NewSinkNodeState probability will increase each time a push or pull is executed with
 * SinkNode as a listener.
 * <p>
 * 
 * The last probability state value for any node is the reliability of the message reaching that
 * node, and the end-to-end reliability of a flow is the value of the last Flow:SinkNode
 * probability.
 * <p>
 * 
 * CS2820 Fall 2024 Project: Implement this class to compute the probabilities that comprise the
 * ReliablityMatrix, which is the core of the reliability visualization that is requested in Warp.
 * <p>
 * 
 * To do this, you will need to retrieve the program source, parse the instructions for each node,
 * in each time slot, to extract the src and snk nodes in the instruction and then apply the message
 * success probability equation defined in the project assignment.
 * <p>
 * 
 * I recommend using the getInstructionParameters method of the WarpDSL class to extract the src and
 * snk nodes from the instruction string in a program schedule time slot.
 * 
 * @author sgoddard
 * @version 1.8 Fall 2024
 * @versio  1.8.1 Fall 2024 msgauna- added new method getReliablities that returns a Reliability Table
 *  *
 */
public class ReliabilityAnalysis {
	
	/**
	 * The DEFAULT_TX_NUM variable specifies the default transmission number <br>
	 */   
	private static final Integer DEFAULT_TX_NUM = 0;
	/**
	 * The DEFAULT_M variable specifies the default minimum packet reception rate <br>
	 */
	private static final Double DEFAULT_M = 0.9;
	/**
	 * The DEFAULT_E2E variable specifies the default end-to-end reliability target <br>
	 */
	private static final Double DEFAULT_E2E = 0.99;
	
	/**
	 * The model variable specifies which model needs to be used based on how the 
	 * ReliabilityAnalysis is constructed <br>
	 */
	private Boolean model;
	/**
	 * The e2e variable specifies the end-to-end reliability target <br>
	 */
	private Double e2e;
	/**
	 * The minPacketReceptionRate variable specifies the minimum packet reception rate <br>
	 */
	private Double minPacketReceptionRate;
	/**
	 * The numFaults variable specifies the number of faults  <br>
	 */
	private Integer numFaults;
	/**
	 * The workLoad variable specifies WorkLoad invoked on reliabilityAnalysis  <br>
	 */
	private WorkLoad workLoad;
	/**
	 * The program variable specifies the priority schedule   <br>
	 */
	private Program program;
	
	private ArrayList<String> headerRow;
	

	/**
	 * Constructor for predictive fault model.
	 * 
 	 * @param e2e End-to-end reliability target.
 	 * @param minPacketReceptionRate Minimum packet reception rate
	 */
	public ReliabilityAnalysis(Double e2e, Double minPacketReceptionRate) {
		this.model= false;
		this.e2e = e2e;
		this.minPacketReceptionRate = minPacketReceptionRate;
		this.numFaults = DEFAULT_TX_NUM;
	}

	/**
	 * Constructor for fixed fault model
  	 * 
  	 * @param numFaults Number of faults to use in fixed model
	 */
	public ReliabilityAnalysis(Integer numFaults) {
		this.model = true;
		this.e2e = DEFAULT_E2E;
		this.minPacketReceptionRate = DEFAULT_M;
		this.numFaults = numFaults;
	}

	/**
	 * Constructor for creating a ReliabilityAnalysis program 
  	 * 
  	 * @param program defines the priority schedule you are using for the reliabilityAnalysis 
	 */
	public ReliabilityAnalysis(Program program) {
		this.program = program;
		this.workLoad = program.toWorkLoad();
		this.numFaults = workLoad.getNumFaults();
		if( numFaults > 0) {
			this.model = true;
		}else {
			this.model = false;
		}
			
		this.minPacketReceptionRate = workLoad.getMinPacketReceptionRate();
		this.e2e = workLoad.getE2e();
		buildReliabilityTable();
		// need program.getScheduler to make reliability table 
		//read pass into warp get instrustions paraameters which parses it get the flow, source, and the sink, know if its a pull or push function, 
		//tells us what index that needs to be change and then update it for the row your doing 
		
		
	}

	/**
	 * Compute number of Transmissions based on model type.
 	 * Given a fixed number of faults results in run of getFixedTxPerLinkAndTotalCost
	 * If not given a fixed number we use predictive model numTxAttemptsPerLinkAndTotalTxAttempts.
 	 * 
 	 * @param flow Flow to compute the number of transmissions.
 	 * @return ArrayList<Integer> containing number of transmissions per link and total cost.
	 */
	public ArrayList<Integer> numTxPerLinkAndTotalTxCost(Flow flow) {
		if (this.model) {
			/* Case 1: If there is fixed numFaults */
			return getFixedTxPerLinkAndTotalTxCost(flow);
		} else {
			/* Case 2: If there is not fixed numFaults */
			return numTxAttemptsPerLinkAndTotalTxAttempts(flow, this.e2e, 
					this.minPacketReceptionRate, false); }
	}
	
	/**
	 * Computes the number of transmissions needed per node and total cost for a given flow.
 	 * 
 	 * @param flow Flow whose node transmissions and total cost need to be calculated
 	 * @return ArrayList of number of transmissions needed for each node
	 */
	private ArrayList<Integer> getFixedTxPerLinkAndTotalTxCost(Flow flow) {
		/* Each node will have at most numFaults+1 transmissions, do not know
		 * which nodes will send message over an edge, giving cost to each node. */
	    int nNodesInFlow = flow.nodes.size();
	    int numEdgesInFlow = nNodesInFlow - 1;
	    
	    ArrayList<Integer> txArrayList = initializeTxArrayList(nNodesInFlow, numFaults + 1);
	    var maxFaultsInFlow = (nNodesInFlow - 1) * numFaults;
	    txArrayList.add(numEdgesInFlow + maxFaultsInFlow);
	    
	    return txArrayList;
	}
	
	/**
	 * Initializes the TxArrayList with defaultTx values
	 *   
	 * @param nNodesInFlow an int representing the number of nodes in the flow 
	 * @param defaultTx a int represents the default number of transmissions 
	 * @return an arrayList initialized with all default values
	 */
	private ArrayList<Integer> initializeTxArrayList(int nNodesInFlow, int defaultTx) {
		/* Compute the maximum # of TX, assuming at most numFaults occur on an edge per period, and
	     * each edge requires at least one successful TX. */
		ArrayList<Integer> txArrayList = new ArrayList<>();
		
		for (int i = 0; i < nNodesInFlow; i++) {
		      txArrayList.add(defaultTx);
		}
		return txArrayList;
	}
	
	/**
	 * Computes number of transmission attempts per link and total number to achieve end-to-end
	 * reliability for given flow.
	 * 
	 * @param flow Flow to analyze.
	 * @param e2e End-to-end reliability target
	 * @param M Minimum link reliability needed per successful link
	 * @param optimizationRequested Indicates if optimization is requested
	 * @return ArrayList<Integer> represents number of transmissions per link and their cost
	 */
	private ArrayList<Integer> numTxAttemptsPerLinkAndTotalTxAttempts(Flow flow, Double e2e, 
																	   Double M, 
																	   boolean optimizationRequested) {
		int nNodesInFlow = flow.nodes.size();
		/* ArrayList to track nPushes for each node in this flow (same as nTx per link) */
		ArrayList<Integer> nPushes = initalizePushArray(nNodesInFlow);
	    
		Double minLinkReliabilityNeeded = calculateMinLinkReliability(e2e, nNodesInFlow -1);
	            
		ReliabilityTable reliabilityWindow = initializeReliabilityWindow(nNodesInFlow);
		ArrayList<Double> currentReliabilityRow = initializeReliabilityRow(reliabilityWindow);
		//Double e2eReliabilityState = currentReliabilityRow[nNodesInFlow - 1];
		Double e2eReliabilityState = currentReliabilityRow.get(nNodesInFlow - 1);
	    
	  
		var timeSlot = 0;
		while (e2eReliabilityState < e2e) {
			currentReliabilityRow = computeReliabilityForNxtTmSlt(currentReliabilityRow,
	    															nNodesInFlow, M,
	    															minLinkReliabilityNeeded,
	    															nPushes);
			e2eReliabilityState = currentReliabilityRow.get(nNodesInFlow - 1);
			//e2eReliabilityState = currentReliabilityRow[nNodesInFlow - 1];
			updateReliabilityWindow(reliabilityWindow, currentReliabilityRow, timeSlot);
			timeSlot++;
		}

		nPushes.set(nNodesInFlow,reliabilityWindow.size());
		// return new ArrayList<>(Arrays.asList(nPushes));
		return nPushes;
	}
	/**
	 * Fills an ArrayList called nPushes with values of 0
	 *   
	 * @param nNodesInFlow
	 * @return nPushes which is an ArrayList of 0s 
	 */
	private ArrayList<Integer> initalizePushArray(int nNodesInFlow) {
		//Integer[] nPushes = new Integer[nNodesInFlow + 1];
		ArrayList<Integer> nPushes = new ArrayList<Integer>();
		for (int i = 0; i <= nNodesInFlow; i++) {
		      nPushes.add(0);
		}
		return nPushes;
	}
	
	/**
	 * Calculates the minimum link reliability with e2e and n hops 
	 * 
	 * @param e2e the end to end reliability target 
	 * @param nHops the number of hops per link 
	 * @return returns the minimum link reliability
	 */
	private Double calculateMinLinkReliability(Double e2e, int nHops) {
		return Math.max(e2e, Math.pow(e2e, (1.0 / (double) nHops)));
	}
	
	/**
	 * Initializes a reliability window  with a single row containing reliability values 
	 * 
	 * @param nNodesInFlow the number of nodes in the flow 
	 * @return reliablityWindow a ReliabilityTable with one row of 0s except for the first value of 1.0  
	 */
	private ReliabilityTable initializeReliabilityWindow(int nNodesInFlow) {
		ReliabilityTable reliabilityWindow = new ReliabilityTable();
		ReliabilityRow initialRow = new ReliabilityRow(nNodesInFlow, 0.0);
		//Vector<Vector<Double>> reliabilityWindow = new Vector<>();
		//Vector<Double> initialRow = new Vector<>(Collections.nCopies(nNodesInFlow, 0.0));
		initialRow.set(0, 1.0);
		reliabilityWindow.add(initialRow);
		
		return reliabilityWindow;
	}
	
	/**
	 * Initializes a reliability row based on a reliability window 
	 * 
	 * @param reliabilityWindow a ReliabilityTable of ReliabilityRows, represents reliability values 
	 * @return a double ArrayList of the first row of the reliabilityWindow
	 */
	private ArrayList<Double> initializeReliabilityRow(ReliabilityTable reliabilityWindow) {
		//return reliabilityWindow.get(0).toArray(new Double[0]);
		return reliabilityWindow.get(0);
	}
	
	/**
	 * Calculates a new reliability state for the nodes of the next time slot, based on the 
	 * reliability values from the previous time slot
	 * 
	 * @param prevReliabilityRow an ArrayList of doubles 
	 * @param nNodesInFlow a int value that relates the number of the nodes in the flow 
	 * @param M the minimum packet reception rate 
	 * @param minLinkReliabilityNeeded a double that is the min reliability per link to hit E2E 
	 * @param nPushes a ArrayList of integers of pushes 
	 * @return currentReliabilityRow a ArrayList of doubles reps reliability states of each node at
	 */
	private ArrayList<Double> computeReliabilityForNxtTmSlt(ArrayList<Double> prevReliabilityRow, int nNodesInFlow,
													     Double M, Double minLinkReliabilityNeeded,
													     ArrayList<Integer> nPushes) {
		ArrayList<Double> currentReliabilityRow = new ArrayList<Double>(prevReliabilityRow);
		//Double[] currentReliabilityRow = Arrays.copyOf(prevReliabilityRow, prevReliabilityRow.length);
		for (int i = 0; i < nNodesInFlow - 1; i++) {
			Double prevSrcNodeState = prevReliabilityRow.get(i);
			Double prevSnkNodeState = prevReliabilityRow.get(i + 1);
			Double nextSnkState = calculateNextSinkState(M, prevSnkNodeState, prevSrcNodeState,
														 minLinkReliabilityNeeded);
			if (nextSnkState > prevSnkNodeState) {
				nPushes.set(i,nPushes.get(i)+1);
			}
			currentReliabilityRow.set(i + 1, nextSnkState);
		}
		return currentReliabilityRow;
	}
	
	/**
	 * Calculates the new reliability state for a sink node based on M,prevSnkNodeState,
	 * prevSrcNodeState, minLinkReliabilityNeeded
	 *  
	 * @param M the minimum packet reception rate
	 * @param prevSnkNodeState a double that represents the previous sink node state 
	 * @param prevSrcNodeState a double that represents the previous source node state 
	 * @param minLinkReliabilityNeeded a double that is the minimum reliability needed per link in a flow to hit E2E
	 * @return returns the next reliability state 
	 */
	private Double calculateNextSinkState(Double M, Double prevSnkNodeState,
										   Double prevSrcNodeState, 
										   Double minLinkReliabilityNeeded) {
		if (prevSnkNodeState < minLinkReliabilityNeeded && prevSrcNodeState > 0) {
			return (1.0 - M) * prevSnkNodeState + M * prevSrcNodeState;
		}
		return prevSnkNodeState;
	}
	
	/**
	 * Updates the reliability window with a new row of reliability data for a specific time slot 
	 * 
	 * @param reliabilityWindow a ReliabilityTable of ReliabiltyRows, represents the reliability values
	 * @param currentReliabilityRow a ArrayList of doubles representing the reliability states for each node in the flow at the next time slot 
	 * @param timeSlot the current time slot 
	 */
	private void updateReliabilityWindow(ReliabilityTable reliabilityWindow, 
											ArrayList<Double> currentReliabilityRow, int timeSlot) {
		//Vector<Double> newRow = new Vector<>(Arrays.asList(currentReliabilityRow));
		ReliabilityRow newRow = new ReliabilityRow(currentReliabilityRow.toArray(new Double[0]));
		if (timeSlot < reliabilityWindow.size()) {
			reliabilityWindow.set(timeSlot, newRow);
		} else {
			reliabilityWindow.add(newRow);
		}
	}
	 
	public Boolean verifyReliablities() {
		// TODO Auto-generated method stub
		return true;
	}
	
	/**
	 * Helper method to calculate the nodes in all flows of workLoad.
	 * 
	 * @param flowNames The flow names that are in workLoad
	 * @param workLoad The Warp programs workLoad object
	 * @return totalNodes The total number of nodes in each workLoad flow
	 */
	public int getTotalNumberOfNodes() {
//		ArrayList<String> flowNames = workLoad.getFlowNamesInPriorityOrder(); 
//
//		int totalNodes = 0;
//		for (String flowName : flowNames) {
//			totalNodes += workLoad.getFlows().get(flowName).getNodes().size();
//		}
//		return totalNodes;
		return headerRow.size();
	}
	protected ArrayList<String> createHeaderRow() {
		ArrayList<String> flowNames = workLoad.getFlowNamesInPriorityOrder();
		ArrayList<String> headerRow = new ArrayList<String>();
 		for(String flowName: flowNames) {
 			ArrayList<Node> flows = workLoad.getFlows().get(flowName).getNodes();
 			for(Node nodes:flows) {
 				headerRow.add(nodes.getName());
 			}
		
		}
		return headerRow;
	}
	
	protected HashMap<String, Integer> headerRowHashMap(ArrayList<String> headerRow){
		HashMap<String,Integer> indexes = new HashMap<String,Integer>();
		for(int i = 0 ;i<headerRow.size();i++) {
			String flowName = headerRow.get(i);
			indexes.put(flowName,i);
		}
		return indexes;
	}
	/**
	 * Supposed to create a table from the reliabilities but right now just creates a dummy table for implementing ReliabilityVisualization
	 * 
	 * @return ReliabilityTable a table made from all the reliabilities
	 */
	public ReliabilityTable getReliabilities() {
		return buildReliabilityTable();
	}

	public Double findSrc(String instruction, Double prevSrcNodeState) {
		// TODO implement this operation
		throw new UnsupportedOperationException("not implemented");
		//  <- Src
	}

	public Double findSnk(String instruction, Double prevSnkNodeState) {
		// TODO implement this operation
		throw new UnsupportedOperationException("not implemented");
		// ^ Sink
	}

	public Double findNewSnkNodeState(Double currentSnkNode, Double currentSrcNode, Double M) {
		// TODO implement this operation
		throw new UnsupportedOperationException("not implemented");
		//Equation 
	}
	
	protected ReliabilityTable buildReliabilityTable() {
		headerRow = createHeaderRow();
		Table<String,InstructionTimeSlot> scheduleTable = program.getSchedule();
		WarpDSL dsl = new WarpDSL();
		ArrayList<InstructionParameters> instructionsArray = new ArrayList<InstructionParameters>();
		ReliabilityTable reliabilties = new ReliabilityTable();
		InstructionParameters instructionObject;
		for(int row = 0; row< scheduleTable.getNumRows();row++) {
			String instruction = scheduleTable.get(row).toString();
			instructionsArray = dsl.getInstructionParameters(instruction);	
			for(int col =0; col < scheduleTable.getNumColumns(); col++) {
	
				instructionObject = instructionsArray.get(col);
				
				String flowName = instructionObject.getFlow();
				String snk = instructionObject.getSnk();
				if (!flowName.equals(instructionObject.unused())) {
						String columnName = flowName +":"+snk;
				}
			}
		}
	
		return reliabilties;
}

}
