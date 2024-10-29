package edu.uiowa.cs.warp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;
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
 *
 */
public class ReliabilityAnalysis {
	private static final Integer DEFAULT_TX_NUM = 0;
	private static final Double DEFAULT_M = 0.9;
	private static final Double DEFAULT_E2E = 0.99;

	private Boolean mode;
	private Double e2e;
	private Double minPacketReceptionRate;
	private Integer numFaults;

	/**
	 * Constructor for predictive fault model.
	 * 
	 * @param e2e End-to-end reliability target.
	 * @param minPacketReceptionRate Minimum packet reception rate.
	 */
	public ReliabilityAnalysis(Double e2e, Double minPacketReceptionRate) {
		this.mode= false;
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
		this.mode = true;
		this.e2e = DEFAULT_E2E;
		this.minPacketReceptionRate = DEFAULT_M;
		this.numFaults = numFaults;
	}

	public ReliabilityAnalysis(Program program) {
		// TODO Auto-generated constructor stub
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
		if (this.mode) {
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
	 * @return Array of number of transmissions needed for each node
	 */
	private ArrayList<Integer> getFixedTxPerLinkAndTotalTxCost(Flow flow) {
//		var nodesInFlow = flow.nodes;
//		var nNodesInFlow = nodesInFlow.size();
//		ArrayList<Integer> txArrayList = new ArrayList<Integer>();
//		/*
//		 * Each node will have at most numFaults+1 transmissions. Because we don't know which nodes will
//		 * send the message over an edge, we give the cost to each node.
//		 */
//		txArrayList = (ArrayList<Integer>)addTo(txArrayList,numFaults+1,nNodesInFlow);
//		/*
//		 * now compute the maximum # of TX, assuming at most numFaults occur on an edge per period, and
//		 * each edge requires at least one successful TX.
//		 */
//		var numEdgesInFlow = nNodesInFlow - 1;
//		var maxFaultsInFlow = numEdgesInFlow * numFaults;
//		txArrayList.add(numEdgesInFlow + maxFaultsInFlow);
//		return txArrayList;
		
	    var nodesInFlow = flow.nodes;
	    var nNodesInFlow = nodesInFlow.size();
	    ArrayList<Integer> txArrayList = new ArrayList<Integer>();
	    /*
	     * Each node will have at most numFaults+1 transmissions. Because we don't know which nodes will
	     * send the message over an edge, we give the cost to each node.
	     */
	    for (int i = 0; i < nNodesInFlow; i++) {
	      txArrayList.add(numFaults + 1);
	    }
	    /*
	     * now compute the maximum # of TX, assuming at most numFaults occur on an edge per period, and
	     * each edge requires at least one successful TX.
	     */
	    var numEdgesInFlow = nNodesInFlow - 1;
	    var maxFaultsInFlow = numEdgesInFlow * numFaults;
	    txArrayList.add(numEdgesInFlow + maxFaultsInFlow);
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
	private ArrayList<Integer> numTxAttemptsPerLinkAndTotalTxAttempts(Flow flow, Double e2e, Double M, boolean optimizationRequested) {
//		ArrayList<Node> nodesInFlow = new ArrayList<Node>();
//		int nNodesInFlow;
//		
//		Integer[] nPushes = new Integer[0];
//		ArrayList<Double> currentReliabilityRow = new ArrayList<Double>();
//		ArrayList<Integer> nPushesArrayList = new ArrayList<Integer>();
//		Vector<Vector<Double>> reliabilityWindow = new Vector<Vector<Double>>();
//		Vector<Double> newReliabilityRow = new Vector<Double>();
//		Vector<Double> tmpVector = new Vector<Double>();
//		Double e2eReliabilityState;
//			
//		nodesInFlow = flow.nodes;
//			/* The last entry will contain the worst-case cost of transmitting E2E in isolation */
//		nNodesInFlow = nodesInFlow.size();
//			// var nPushes = Array(repeating: 0, count: nNodesInFlow+1);
//			/* Array to track nPushes for each node in this flow (same as nTx per link) */
//		nPushes = new Integer[nNodesInFlow + 1];
//			/* initialize to all 0 values */
//		Arrays.fill(nPushes, 0);
//				
//	
//			/* Now compute reliability of packet reaching each node in the given time slot
//			* Start with a 2-D reliability window that is a 2-D matrix of no size
//			* each row is a time slot, stating at time 0
//			* each column represents the reliability of the packet reaching that node at the
//			* current time slot (i.e., the row it is in)
//			* will add rows as we compute reliabilities until the final reliability is reached
//			* for all nodes. 
//			*/
//		newReliabilityRow = (Vector<Double>)addTo(newReliabilityRow,0.0,nNodesInFlow);
//				
//			/* Now add row to the reliability window, Time 0 */
//		reliabilityWindow.add(newReliabilityRow);
//		tmpVector = reliabilityWindow.get(0);
//		currentReliabilityRow = new ArrayList<>(tmpVector);
//				/* var currentReliabilityRow = (Double[]) reliabilityWindow.get(0).toArray();
//				 * Want reliabilityWindow[0][0] = 1.0 (i.e., P(packet@FlowSrc) = 1
//				 * but I din't want to mess with the newReliablityRow vector I use below
//				 * So, we initialize this first entry to 1.0, which is reliabilityWindow[0][0]
//				 * We will then update this row with computed values for each node and put it
//				 * back in the matrix
//				 * The analysis will end when the E2E reliability metric is met, initially
//				 * will be 0 with this statement. */
//		currentReliabilityRow.set(0, 1.0);
//				// initialize (i.e., P(packet@FlowSrc) = 1
//		e2eReliabilityState = currentReliabilityRow.get(nNodesInFlow - 1);
//		reliabilityWindow = NoIdeaWhatThisDoesYet(e2eReliabilityState,currentReliabilityRow,newReliabilityRow,nNodesInFlow,nPushes,reliabilityWindow,M, e2e);
//				/* Total worst-case cost to transmit E2E in isolation where specified reliability
//				 * target is the number of rows in the reliabilityWindow */
//		nPushes[nNodesInFlow] = reliabilityWindow.size();
//				/* Now convert the array to the ArrayList needed to return */
//		Collections.addAll(nPushesArrayList, nPushes);
//		return nPushesArrayList;
//	
		
	    var nodesInFlow = flow.nodes;
	    /* The last entry will contain the worst-case cost of transmitting E2E in isolation */
	    var nNodesInFlow = nodesInFlow.size();
	    // var nPushes = Array(repeating: 0, count: nNodesInFlow+1);
	    /* Array to track nPushes for each node in this flow (same as nTx per link) */
	    var nPushes = new Integer[nNodesInFlow + 1];
	    /* initialize to all 0 values */
	    Arrays.fill(nPushes, 0);
	    var nHops = nNodesInFlow - 1;
	    /* minLinkReliablityNeded is the minimum reliability needed per link in a flow to hit E2E
	     * reliability for the flow
	     * Use max to handle rounding error when E2E == 1.0 */
	    Double minLinkReliablityNeded = Math.max(e2e, Math.pow(e2e, (1.0 / (double) nHops)));
	    /* Now compute reliability of packet reaching each node in the given time slot
	     * Start with a 2-D reliability window that is a 2-D matrix of no size
	     * each row is a time slot, stating at time 0
	     * each column represents the reliability of the packet reaching that node at the
	     * current time slot (i.e., the row it is in)
	     * will add rows as we compute reliabilities until the final reliability is reached
	     * for all nodes. */
	    var reliabilityWindow = new Vector<Vector<Double>>();
	    var newReliabilityRow = new Vector<Double>();
	    for (int i = 0; i < nNodesInFlow; i++) {
	      /* Create the row initialized with 0.0 values */
	      newReliabilityRow.add(0.0);
	    }
	    /* Now add row to the reliability window, Time 0 */
	    reliabilityWindow.add(newReliabilityRow);
	    Vector<Double> tmpVector = reliabilityWindow.get(0);
	    var currentReliabilityRow = tmpVector.toArray(new Double[tmpVector.size()]);
	    /* var currentReliabilityRow = (Double[]) reliabilityWindow.get(0).toArray();
	     * Want reliabilityWindow[0][0] = 1.0 (i.e., P(packet@FlowSrc) = 1
	     * but I din't want to mess with the newReliablityRow vector I use below
	     * So, we initialize this first entry to 1.0, which is reliabilityWindow[0][0]
	     * We will then update this row with computed values for each node and put it
	     * back in the matrix
	     * The analysis will end when the E2E reliability metric is met, initially
	     * will be 0 with this statement. */
	    currentReliabilityRow[0] = 1.0; // initialize (i.e., P(packet@FlowSrc) = 1
	    Double e2eReliabilityState = currentReliabilityRow[nNodesInFlow - 1];
	    /*Start time at 0  */
	    var timeSlot = 0;
	    /* Change to while and increment timeSlot since we don't know how long
	     * the schedule window will last */
	    while (e2eReliabilityState < e2e) {
	      var prevReliabilityRow = currentReliabilityRow;
	      /* Would be reliabilityWindow[timeSlot] if working through a schedule */
	      currentReliabilityRow = newReliabilityRow.toArray(new Double[newReliabilityRow.size()]);
	      /* Now use each flow:source->sink to update reliability computations
	       * this is the update formula for the state probabilities
	       * nextState = (1 - M) * prevState + M*NextHighestFlowState
	       * use MinLQ for M in above equation
	       * NewSinkNodeState = (1-M)*PrevSnkNodeState + M*PrevSrcNodeState
	       * 
	       * Loop Through each node in the flow and update states for each link (sink->source pair) */
	      for (int nodeIndex = 0; nodeIndex < (nNodesInFlow - 1); nodeIndex++) {
	        var flowSrcNodeindex = nodeIndex;
	        var flowSnkNodeindex = nodeIndex + 1;
	        var prevSrcNodeState = prevReliabilityRow[flowSrcNodeindex];
	        var prevSnkNodeState = prevReliabilityRow[flowSnkNodeindex];
	        Double nextSnkState;
	        /* Do a push until PrevSnk state > E2E to ensure next node reaches target E2E
	         * BUT skip if no chance of success (source doesn't have packet) */
	        if ((prevSnkNodeState < minLinkReliablityNeded) && prevSrcNodeState > 0) {
	          /* Need to continue attempting Tx so update current state */
	          nextSnkState = ((1.0 - M) * prevSnkNodeState) + (M * prevSrcNodeState);
	          /* Increment number of pushes for this node to sink node */
	          nPushes[nodeIndex] += 1;
	        } else {
	          /* sink node has met its reliability, move to next node and record the reliability met */
	          nextSnkState = prevSnkNodeState;
	        }
	        /* Probabilities are non-decreasing so update if we were
	         * higher by carrying old value forward */
	        if (currentReliabilityRow[flowSrcNodeindex] < prevReliabilityRow[flowSrcNodeindex]) {
	          /* Carry previous state forward for source node which may get overwritten
	           * later by another instruction in this slot */
	          currentReliabilityRow[flowSrcNodeindex] = prevReliabilityRow[flowSrcNodeindex];
	        }
	        currentReliabilityRow[flowSnkNodeindex] = nextSnkState;
	      }

	      e2eReliabilityState = currentReliabilityRow[nNodesInFlow - 1];
	      Vector<Double> currentReliabilityVector = new Vector<Double>();
	      /* convert the row to a vector so we can add it to the reliability window */
	      Collections.addAll(currentReliabilityVector, currentReliabilityRow);
	      if (timeSlot < reliabilityWindow.size()) {
	        reliabilityWindow.set(timeSlot, (currentReliabilityVector));
	      } else {
	        reliabilityWindow.add(currentReliabilityVector);
	      }
	      /* Increase to next timeSlot */
	      timeSlot += 1;
	    }
	    var size = reliabilityWindow.size();
	    /* Total worst-case cost to transmit E2E in isolation where specified reliability
	     * target is the number of rows in the reliabilityWindow */
	    nPushes[nNodesInFlow] = size;
	    /* Now convert the array to the ArrayList needed to return */
	    ArrayList<Integer> nPushesArrayList = new ArrayList<Integer>();
	    Collections.addAll(nPushesArrayList, nPushes);
	    return nPushesArrayList;

	}

	/**
	 * Places a variable sent to the method into each index from 1 to size of the ArrayList name sent to the method 
	 * @param name The ArrayList being updated 
	 * @param variable The variable being inputed into each index 
	 * @param size The limit of how many indexed should be updated 
	 * @return The ArrayList now updated 
	 */
	private <T> Collection<T> addTo(Collection<T> collection , T variable, int size){
		for (int i = 0; i < size; i++) {
			collection.add(variable);
		}
		return collection;
	}
	/**
	 * 
	 * @param e2eReliabilityState
	 * @param currentReliabilityRow
	 * @param newReliabilityRow 
	 * @param nNodesInFlow
	 * @param nPushes
	 * @param reliabilityWindow
	 * @return
	 */
	private Vector<Vector<Double>> NoIdeaWhatThisDoesYet(Double e2eReliabilityState,ArrayList<Double> currentReliabilityRow,Vector<Double> newReliabilityRow,int nNodesInFlow,Integer[] nPushes,Vector<Vector<Double>> reliabilityWindow, double M, double e2e){
		//ArrayList<Double> prevReliabilityRow = new ArrayList<Double>();
		Vector<Double> currentReliabilityVector = new Vector<Double>();
		int timeSlot = 0;
		/*Start time at 0  */
		/* Change to while and increment timeSlot since we don't know how long
		 * the schedule window will last */
		while (e2eReliabilityState < e2e) {
			var prevReliabilityRow = currentReliabilityRow;
			/* Would be reliabilityWindow[timeSlot] if working through a schedule */
			currentReliabilityRow = new ArrayList<>(newReliabilityRow);
			//currentReliabilityRow = newReliabilityRow.toArray(new Double[newReliabilityRow.size()]);
			//currentReliabilityRow = newReliabilityRow.toArray(new Double[newReliabilityRow.size()]);
			currentReliabilityRow = updateStates(nNodesInFlow,currentReliabilityRow, prevReliabilityRow, nPushes, M, e2e);
			
			e2eReliabilityState = currentReliabilityRow.get(nNodesInFlow - 1);
			/* convert the row to a vector so we can add it to the reliability window */
			Collections.addAll(currentReliabilityVector, currentReliabilityRow.toArray(new Double[0]));
			if (timeSlot < reliabilityWindow.size()) {
				reliabilityWindow.set(timeSlot, (currentReliabilityVector));
			} else {
				reliabilityWindow.add(currentReliabilityVector);
			}
			/* Increase to next timeSlot */
			timeSlot += 1;
		} 
		return reliabilityWindow;
	}
	/**
	 * Now use each flow:source->sink to update reliability computations
	 * this is the update formula for the state probabilities
	 * nextState = (1 - M) * prevState + M*NextHighestFlowState
	 * use minPacketReceptionRate for M in above equation
	 * NewSinkNodeState = (1-M)*PrevSnkNodeState + M*PrevSrcNodeState
	 * 
	 * Loop Through each node in the flow and update states for each link (sink->source pair) 
	 * 
	 * @param NodesInFlow, 
	 * @param currentReliabilityRow, 
	 * @param prevReliabilityRow, 
	 * @param nPushes
	 */
	private ArrayList<Double> updateStates(int nNodesInFlow, ArrayList<Double> currentReliabilityRow,ArrayList<Double> prevReliabilityRow, Integer[] nPushes, double M, double e2e) {
		int flowSrcNodeindex;
		int flowSnkNodeindex;
		Double prevSrcNodeState;
		Double prevSnkNodeState;
		Double nextSnkState;
		int nHops = nNodesInFlow - 1;
		/* minLinkReliablityNeded is the minimum reliability needed per link in a flow to hit E2E
		 * reliability for the flow
		 * Use max to handle rounding error when E2E == 1.0 
		 */
		Double minLinkReliablityNeded = Math.max(e2e, Math.pow(e2e, (1.0 / (double) nHops)));

		for (int nodeIndex = 0; nodeIndex < (nNodesInFlow - 1); nodeIndex++) {
			flowSrcNodeindex = nodeIndex;
			flowSnkNodeindex = nodeIndex + 1;
			prevSrcNodeState = prevReliabilityRow.get(flowSrcNodeindex);
			prevSnkNodeState = prevReliabilityRow.get(flowSnkNodeindex);
			/* Do a push until PrevSnk state > E2E to ensure next node reaches target E2E
			 * BUT skip if no chance of success (source doesn't have packet) */
			if ((prevSnkNodeState < minLinkReliablityNeded) && prevSrcNodeState > 0) {
				/* Need to continue attempting Tx so update current state */
				nextSnkState = ((1.0 - M) * prevSnkNodeState) 
						+ (M * prevSrcNodeState);
				/* Increment number of pushes for this node to sink node */
				nPushes[nodeIndex] += 1;
			} else {
				/* sink node has met its reliability, move to next node and record 
				 * the reliability met */
				nextSnkState = prevSnkNodeState;
			}
			/* Probabilities are non-decreasing so update if we were
			 * higher by carrying old value forward */
			if (currentReliabilityRow.get(flowSrcNodeindex) < prevReliabilityRow.get(flowSnkNodeindex)){
				/* Carry previous state forward for source node which may get overwritten
				 * later by another instruction in this slot */
				currentReliabilityRow.set(flowSrcNodeindex, prevReliabilityRow.get(flowSrcNodeindex));
			}
			currentReliabilityRow.set(flowSnkNodeindex,nextSnkState);
		}
		return currentReliabilityRow;
	}
	 
	public Boolean verifyReliablities() {
		// TODO Auto-generated method stub
		return true;
	}

	// TODO Auto-generated class
	//TODO implement this operation
	public ReliabilityTable getReliabilities() {
		// TODO implement this operation
		throw new UnsupportedOperationException("not implemented");
	}

}
