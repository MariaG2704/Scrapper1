package edu.uiowa.cs.warp;

import edu.uiowa.cs.utilities.Utilities;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
// import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;


/* Added JavaDocs as per HW2 instruction
 * Fixed a handful of indentation and grammar problems
 * Fixed incorrect usage of multi-line comments
 * Comments will be above line with asterisk, unneeded code will be double-slash
 * Couple formatted prints contained missing object for %s or newline before period, fixed.
 */

/**
 * Build the nodes and flows for the workload described in the workload description file, whose name
 * is passed into the Constructor via the parameter inputFileName. Good default values for the
 * constructors are m = 0.9, e2e = 0.99, and numFaults = 1 when the second constructor is used.

 * @author sgoddard
 * @author ccolin - modified as per HW2 instructions
 * @version 1.4
 *
 */
public class WorkLoad extends WorkLoadDescription implements ReliabilityParameters {

  private static final Integer DEFAULT_PRIORITY = 0;
  private static final Integer DEFAULT_INDEX = 0;
  private static final Integer DEFAULT_TX_NUM = 0;
  private static final String FLOW_WARNING =
      "\n\tWarning! Bad situation: " + "Flow %s doesn't exist but trying to ";

  private Integer numFaults = 0;
  private Double minPacketReceptionRate = 0.0;
  private Double e2e = 0.0;
  private Boolean intForNodeNames = false;
  private Boolean intForFlowNames = false;
  /* Map of all flow nodes in WARP graph (<name, Flow>) */
  private FlowMap flows;
  // private Integer nFlows = 0;
  /* Map of all graph nodes in the WARP graph (<name, Node>) */
  private NodeMap nodes;
  /* Name of the WARP graph defining the workload */
  private String name;
  /* Array to hold the names of the flows to preserve their order */
  private ArrayList<String> flowNamesInOriginalOrder = new ArrayList<>();
  private ArrayList<String> flowNamesInPriorityOrder = new ArrayList<>();
  // private FileManager fm;

  /**
   * Creates WorkLoad with 0 faults tolerated, specified packet reception rate and E2E reliability, 
   * and reads input file to build node and flow data.

   * @param m              minimum packet reception rate
   * @param e2e            E2E reliability target
   * @param inputFileName  name of input file
   */
  WorkLoad(Double m, Double e2e, String inputFileName) {
    super(inputFileName);
    setDefaultParameters();
    /* User file manager passed to this object */
    minPacketReceptionRate = m;
    /* Use populates this flows object as the input file is read */
    this.e2e = e2e;
    /*
     * Read input file, build the AST of graph and the listener will build the node and flow data
     * objects
     */
    WorkLoadListener.buildNodesAndFlows(this);
  }
  /**
   * Creates WorkLoad with specified fault toleration, packet reception rate and E2E reliability, 
   * and reads input file to build node and flow data.

   * @param numFaults      number of faults tolerated
   * @param m              minimum packet reception rate
   * @param e2e            E2E reliability target
   * @param inputFileName  name of input file
   */
  WorkLoad(Integer numFaults, Double m, Double e2e, String inputFileName) {
    super(inputFileName);
    setDefaultParameters();
    this.numFaults = numFaults;
    /* Use file manager passed to this object */
    minPacketReceptionRate = m;
    /* Use populate this flows object as the input file is read */
    this.e2e = e2e;
    /*
     * Read input file, build the AST of graph and the listener will build the node and flow data
     * objects
     */
    WorkLoadListener.buildNodesAndFlows(this);
  }

  private void setDefaultParameters() {
    /* Default is node and flow names are all alpha names */
    intForNodeNames = true;
    intForFlowNames = true;
    /* map of all flow nodes in the WARP graph (<name, Flow>) */
    flows = new FlowMap();
    /* map of all graph nodes in the WARP graph (<name, Node>) */
    nodes = new NodeMap();
    /* array to hold names of flows to preserve their order */
    flowNamesInOriginalOrder = new ArrayList<>();
    flowNamesInPriorityOrder = new ArrayList<>();
    numFaults = DEFAULT_TX_NUM;
  }

  /**
   * Get the number of tolerated faults.

   * @return the numFaults
   */
  public Integer getNumFaults() {
    return numFaults;
  }

  /**
   * Get the minimum packet reception rate.

   * @return the minPacketReceptionRate
   */
  public Double getMinPacketReceptionRate() {
    return minPacketReceptionRate;
  }

  /**
   * Get the E2E reliability target.

   * @return the e2e
   */
  public Double getE2e() {
    return e2e;
  }

  /**
   * Get whether node names are represented by integers.

   * @return the intForNodeNames
   */
  public Boolean getIntForNodeNames() {
    return intForNodeNames;
  }

  /**
   * Get whether flow names are represented by integers.

   * @return the intForFlowNames
   */
  public Boolean getIntForFlowNames() {
    return intForFlowNames;
  }

  /**
   * Get all flows.

   * @return the flows
   */
  public FlowMap getFlows() {
    return flows;
  }

  /**
   * Get all nodes.

   * @return the nodes
   */
  public NodeMap getNodes() {
    return nodes;
  }

  /**
   * Get the name of the WARP graph.

   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Get all the flows in their original order.

   * @return the flowNamesInOriginalOrder
   */
  public ArrayList<String> getFlowNamesInOriginalOrder() {
    return flowNamesInOriginalOrder;
  }

  /**
   * Get all the flows in order of priorities.

   * @return the flowNamesInPriorityOrder
   */
  public ArrayList<String> getFlowNamesInPriorityOrder() {
    return flowNamesInPriorityOrder;
  }

  /**
   * Sets the minimum packet reception rate of the WorkLoad.

   * @param minPacketReceptionRate the minPacketReceptionRate to set
   */
  public void setMinPacketReceptionRate(Double minPacketReceptionRate) {
    this.minPacketReceptionRate = minPacketReceptionRate;
  }

  /**
   * Finds what the largest phase is of all flows.

   * @return the maximum phase of all flows
   */
  public Integer getMaxPhase() {
    var queue = new SchedulableObjectQueue<Flow>(new MaxPhaseComparator<Flow>(), flows.values());
    return queue.poll().getPhase();
  }

  /**
   * Gets the minimum period of all flows.

   * @return the minimum period of all flows
   */
  public Integer getMinPeriod() {
    var queue = new SchedulableObjectQueue<Flow>(new PeriodComparator<Flow>(), flows.values());
    return queue.poll().getPeriod();
  }

  /**
   * Sets the E2E reliability target.

   * @param e2e the e2e to set
   */
  public void setE2e(Double e2e) {
    this.e2e = e2e;
  }

  /**
   * Sets whether node names are represented by integers or not.

   * @param intForNodeNames the intForNodeNames to set
   */
  public void setIntForNodeNames(Boolean intForNodeNames) {
    this.intForNodeNames = intForNodeNames;
  }

  /**
   * Sets whether flow names are represented by integers or not.

   * @param intForFlowNames the intForFlowNames to set
   */
  public void setIntForFlowNames(Boolean intForFlowNames) {
    this.intForFlowNames = intForFlowNames;
  }

  /**
   * Sets the flows to the object.

   * @param flows the flows to set
   */
  public void setFlows(FlowMap flows) {
    this.flows = flows;
  }

  /**
   * Sets the nodes to the object.

   * @param nodes the nodes to set
   */
  public void setNodes(NodeMap nodes) {
    this.nodes = nodes;
  }

  /**
   * Sets the name of the WorkLoad.

   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Sets the flows' original order.

   * @param flowNamesInOriginalOrder the flowNamesInOriginalOrder to set
   */
  public void setFlowNamesInOriginalOrder(ArrayList<String> flowNamesInOriginalOrder) {
    this.flowNamesInOriginalOrder = flowNamesInOriginalOrder;
  }

  /**
   * Sets the flows' priority order.

   * @param flowNamesInPriorityOrder the flowNamesInPriorityOrder to set
   */
  public void setFlowNamesInPriorityOrder(ArrayList<String> flowNamesInPriorityOrder) {
    this.flowNamesInPriorityOrder = flowNamesInPriorityOrder;
  }

  /**
   * Sets a specific node to a specific channel.

   * @param name the node whose channel is to be set
   * @param channel the channel to set
   */
  public void setNodeChannel(String name, Integer channel) {
    /* Get the node object */
    var node = nodes.get(name);
    node.setChannel(channel);
    /* Update nodes map with updated object */
    nodes.put(name, node);
  }

  /**
   * Gets the channel of a node.

   * @return the node channel
   */
  public Integer getNodeChannel(String name) {
    /* Get node object */
    var node = nodes.get(name);
    return node.getChannel();
  }
  /**
   * Add a new flow node to the Flow dictionary.

   * @param flowName Name of the flow.
   */

  public void addFlow(String flowName) {
    /*
     * add a new flow node to the Flows dictionary. Only name, priority, and index are changed from
     * default values priority is set to the number of flows already added (index), 0 for first flow
     * This will create a default priority equal to the order listed in the input graph file. We
     * also set index to the same value so we can preserve that order as a secondary sort key. The
     * initalPriority field is probably not needed, but it might be useful in the future?? If the
     * optional flow parameters (priority, period, ...) is set, then this default priority will be
     * over written
     */
    if (flows.containsKey(flowName)) {
      System.out.printf("\n\tWarning! A flow with name %s already exists. "
          + "It has been replaced with a new flow.\n", flowName);
    }
    var index = flows.size();
    var flowNode = new Flow(flowName, index, index);
    flows.put(flowName, flowNode);
    if (!Utilities.isInteger(flowName) && intForFlowNames) {
      /* Set false because name not a number, above makes sure we only set it once */
      intForFlowNames = false;
    }
    flowNamesInOriginalOrder.add(flowName);
  }

  public Boolean isIntForNodeNames() {
    /* Return true if all node names are integers */
    return intForNodeNames;
  }

  public Boolean isIntForFlowNames() {
    /* Return true if all flow names are integers */
    return intForFlowNames;
  }

  /**
   * Add a new node to the flow.

   * @param flowName Name of the flow.
   * @param nodeName Name of the new node.
   */
  public void addNodeToFlow(String flowName, String nodeName) {
    if (!Utilities.isInteger(nodeName) && intForNodeNames) {
      /* set false because name not is a number; && above makes sure we only set it once */
      intForNodeNames = false;
    }
    /* Create node and add it to nodes if map doesn't have this node already */
    if (!nodes.containsKey(nodeName)) {
      /* If the node already exists, just need to add to the flow */
      /* nodeIndex will be the order added */
      var index = nodes.size();
      /* Create a new graph node */
      var graphNode = new Node(nodeName, DEFAULT_PRIORITY, index);
      /* Add it to the map of nodes */
      nodes.put(nodeName, graphNode);
    }
    /*
     * Node is now created and in the nodes map Next we need to get the current flow and add this
     * node to that flow by appending it to the node array for that flow
     */
    var flowNode = getFlow(flowName);
    var graphNode = new Node(nodeName, flowNode.nodes.size(), DEFAULT_INDEX);
    /* the priority is the node's index in the flow, which is the current array size */
    flowNode.addNode(graphNode);
    flowNode.linkTxAndTotalCost.add(DEFAULT_TX_NUM);
  }

  /**
   * Get the priority of a specific node in a flow.

   * @param flowName Name of the flow that contains node
   * @param nodeName Name of the node in the flow
   * @return Priority of the given node in the flow
   */
  public Integer getFlowPriority(String flowName, String nodeName) {
    var priority = 0;
    var flow = getFlow(flowName);
    Iterator<Node> nodes = flow.nodes.iterator();
    while (nodes.hasNext()) {
      var node = nodes.next();
      if (node.getName() == nodeName) {
        /* Found source node, set its index */
        priority = node.getPriority();
        break;
      }
    }
    return priority;
  }
  /**
   * Return priority of a flow across all flows.

   * @param flowName Name of the flow
   * @return Priority of the flow
   */
  public Integer getFlowPriority(String flowName) {
    var flowNode = getFlow(flowName);
    return flowNode.getPriority();
  }

  public void setFlowPriority(String flowName, Integer priority) {
    var flowNode = getFlow(flowName);
    flowNode.setPriority(priority);
  }

  public void setFlowPeriod(String flowName, Integer period) {
    var flowNode = getFlow(flowName);
    flowNode.setPeriod(period);
  }

  public void setFlowDeadline(String flowName, Integer deadline) {
    var flowNode = getFlow(flowName);
    flowNode.setDeadline(deadline);
  }

  public void setFlowPhase(String flowName, Integer phase) {
    var flowNode = getFlow(flowName);
    flowNode.setPhase(phase);
  }

  public Integer getFlowIndex(String flowName) {
    var flowNode = getFlow(flowName);
    return flowNode.index;
  }

  /**
   * Retrieves the period of a given flow.

   * @param flowName Name of flow
   * @return Period of flow
   */
  public Integer getFlowPeriod(String flowName) {
    var flowNode = getFlow(flowName);
    return flowNode.getPeriod();
  }

  /**
   * Get the deadline of a given flow.

   * @param flowName Name of the flow
   * @return Deadline of the flow
   */
  public Integer getFlowDeadline(String flowName) {
    var flowNode = getFlow(flowName);
    return flowNode.getDeadline();
  }

  /**
   * Get the phase of a given flow.

   * @param flowName Name of the flow
   * @return Phase of the flow
   */
  public Integer getFlowPhase(String flowName) {
    var flowNode = getFlow(flowName);
    return flowNode.getPhase();
  }

  /**
   * Get the number of transmissions per link for a given flow.

   * @param flowName Name of the flow
   * @return numTxPerLink of the flow
   */
  public Integer getFlowTxAttemptsPerLink(String flowName) {
    var flowNode = getFlow(flowName);
    return flowNode.numTxPerLink;
  }

  /**
   * Sorts flows first by priority and then by index, and updates the `flowNamesInPriorityOrder`
   * list with the names of the flows in the sorted order.
   */
  public void setFlowsInPriorityOrder() {
    /* create a list of Flow objects from the FlowMap using the stream interface. */
    List<Flow> unsortedFlows = flows.values().stream().collect(Collectors.toList());
    /* Now sort by a secondary key, which is index in this case */
    List<Flow> sortedByIndex = unsortedFlows.stream().sorted(Comparator.comparing(Flow::getIndex))
        .collect(Collectors.toList());
    /* Now sort by primary key, which is priority in this case */
    List<Flow> sortedFlows = sortedByIndex.stream().sorted(Comparator.comparing(Flow::getPriority))
        .collect(Collectors.toList());
    /* Finally, create a new flowNamesInPriorityOrder that contains the flow names in the requested
     * order */
    flowNamesInPriorityOrder = new ArrayList<>();
    sortedFlows.forEach((node) -> flowNamesInPriorityOrder.add(node.getName()));
  }

  /**
   * Sorts flows first by deadline and then by priority, and updates the `flowNamesInPriorityOrder`
   * list with the names of the flows in the sorted order.
   */
  public void setFlowsInDMorder() {
    /* create a list of Flow objects from the FlowMap using the stream interface. */
    List<Flow> unsortedFlows = flows.values().stream().collect(Collectors.toList());
    /* Now sort by a secondary key, which is priority in this case */
    List<Flow> sortedByPriority = unsortedFlows.stream()
        .sorted(Comparator.comparing(Flow::getPriority)).collect(Collectors.toList());
    /* Now sort by primary key, which is deadline in this case */
    List<Flow> sortedFlows = sortedByPriority.stream()
        .sorted(Comparator.comparing(Flow::getDeadline)).collect(Collectors.toList());
    /* Finally, create a new flowNamesInPriorityOrder that contains the flow names in the requested
     * order */
    flowNamesInPriorityOrder = new ArrayList<>();
    sortedFlows.forEach((node) -> flowNamesInPriorityOrder.add(node.getName()));
  }

  /**
   * Sorts flows first by period and then by priority, and updates the `flowNamesInPriorityOrder`
   * list with the names of the flows in the sorted order.
   */
  public void setFlowsInRMorder() {
    /* create a list of Flow objects from the FlowMap using the stream interface. */
    List<Flow> unsortedFlows = flows.values().stream().collect(Collectors.toList());
    /* Now sort by a secondary key, which is priority in this case */
    List<Flow> sortedByPriority = unsortedFlows.stream()
        .sorted(Comparator.comparing(Flow::getPriority)).collect(Collectors.toList());
    /* Now sort by primary key, which is period in this case */
    List<Flow> sortedFlows = sortedByPriority.stream().sorted(Comparator.comparing(Flow::getPeriod))
        .collect(Collectors.toList());
    /* Finally, create a new flowNamesInPriorityOrder that contains the flow names in the requested
     * order */
    flowNamesInPriorityOrder = new ArrayList<>();
    sortedFlows.forEach((node) -> flowNamesInPriorityOrder.add(node.getName()));
  }

  public void setFlowsInRealTimeHARTorder() {
    /* use Priority order for RealTimeHART */
    setFlowsInPriorityOrder();
  }

  public void finalizeCurrentFlow(String flowName) {
    if (numFaults > 0) {
      finalizeFlowWithFixedFaultTolerance(flowName);
    } else {
      finalizeFlowWithE2eParameters(flowName);
    }
  }

  public Integer nextReleaseTime(String flowName, Integer currentTime) {
    var flow = getFlow(flowName);
    flow.setLastUpdateTime(currentTime);
    flow.setNextReleaseTime(currentTime);
    /* Next release TIme at or after currentTime */ 
    return flow.getReleaseTime();
  }

  public Integer nextAbsoluteDeadline(String flowName, Integer currentTime) {
    var flow = getFlow(flowName);
    flow.setLastUpdateTime(currentTime);
    flow.setNextReleaseTime(currentTime);
    /* Next deadline after currentTime */
    return flow.getReleaseTime() + flow.getDeadline();
  }

  private void finalizeFlowWithE2eParameters(String flowName) {
    var flowNode = flows.get(flowName);
    /* shorten the name :-) */
    var m = minPacketReceptionRate;
    if (flowNode != null) {
      var nodes = flowNode.nodes;
      int nHops = nodes.size();
      if (nHops < 1) {
        /*
         * number of hops in flow, but make sure it will be at least 1, else it isn't a flow! || was
         * -1 at end
         */
        nHops = 2;
      }
      /* set nTx to 1 by default (1 transmission per link required at minimum and when m == 1.0) */
      Double nTx = 1.0;
      if (m < 1.0) {
        /*
         * now compute nTXper link based on Ryan's formula: log(1 - e2e^(1/hops)) / log(1 - M) = #
         * txs per hop
         */
        nTx = Math.log((1.0 - Math.pow(e2e, (1.0 / (double) nHops)))) / Math.log(1.0 - m);
      }
      /* set numTxPerLink based on M, E2E, and flow length */
      flowNode.numTxPerLink = (int) Math.ceil(nTx);
      /* Now compute nTx per link to reach E2E requirement. */
      ArrayList<Integer> linkTxAndTotalCost =
          numTxAttemptsPerLinkAndTotalTxAttempts(flowNode, e2e, m, true);
      flowNode.linkTxAndTotalCost = linkTxAndTotalCost;
      /* Update flow node in Flows array */
      flows.put(flowName, flowNode);
      /* should never happen... */
    } else {
      System.out.printf("\n\tWarning! Bad situation: Flow %s doesn't exist but "
          + "trying to get its numTxPerLink property.\n", flowName);
    }
  }

  private void finalizeFlowWithFixedFaultTolerance(String flowName) {
    var flowNode = flows.get(flowName);
    if (flowNode != null) {
      /* set numTxPerLink based on numFaults */
      flowNode.numTxPerLink = numFaults + 1;
      /* Now compute nTx per link to reach E2E requirement. */
      ArrayList<Integer> linkTxAndTotalCost = getFixedTxPerLinkAndTotalTxCost(flowNode);
      flowNode.linkTxAndTotalCost = linkTxAndTotalCost;
      /* Update flow node in Flows array */
      flows.put(flowName, flowNode);
      /* should never happen... */
    } else {
      System.out.printf("\n\tWarning! Bad situation: Flow %s doesn't exist but "
          + "trying to get its numTxPerLink property.\n", flowName);
    }
  }

  /**
   * Computes the number of transmissions needed per node and total cost for a given flow.

   * @param flow Flow whose node transmissions and total cost need to be calculated
   * @return Array of number of transmissions needed for each node
   */
  private ArrayList<Integer> getFixedTxPerLinkAndTotalTxCost(Flow flow) {
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
   * Computes the number of transmission attempts needed per link and the total number of
   * transmission attempts required to achieve the E2E reliability target for the given flow,
   * considering the specified reliability and optimization parameters.

   * @param flow Flow for which to compute transmission attempts and the cost for
   * @param e2e  E2E reliability target
   * @param M    Minimum link reliability needed to ensure successful transmission
   * @param optimizationRequested  Boolean for if transmission calculations should be optimized
   * @return The numTxAttemptsPerLinkAndTotalTxAttempts of the given flow
   */
  private ArrayList<Integer> numTxAttemptsPerLinkAndTotalTxAttempts(Flow flow, Double e2e, Double M,
      boolean optimizationRequested) {
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
   * Get the names of all nodes in flow, ordered alphabetically.
   * 

   * @return Array of all node names sorted
   */
  public String[] getNodeNamesOrderedAlphabetically() {
    var nodes = getNodes();
    /* Get names from the node map */
    Set<String> keys = nodes.keySet();
    String[] nodeNames = keys.toArray(new String[keys.size()]);
    /* Sort node names */
    Arrays.sort(nodeNames);
    /* However, if names are actually strings of integers, then the sort doesn't come out
     * the way we would like. So, handle that case */
    var nodeNamesAsInts = new Integer[nodeNames.length];
    /* Flag to see if all names are integers or not */
    var allIntNames = true;
    for (int i = 0; i < nodeNames.length; i++) {
      var nodeName = nodeNames[i];
      if (Utilities.isInteger(nodeName) && allIntNames) {
        /* nodeName is an alpha representation of an integer */
        nodeNamesAsInts[i] = Integer.parseInt(nodeName);
      } else {
        /* nodeName is an alpha name and not an integer, so set flag and terminate loop */
        allIntNames = false;
        /* Can stop loop if we know not all names are integers */
        break;
      }
    }
    if (allIntNames) {
      /* If all names are integers, then we need to sort them accordingly
       * Otherwise, we get names in what appears to not be in order because
       * below sorts in ascending order */
      Arrays.sort(nodeNamesAsInts);
      for (int i = 0; i < nodeNamesAsInts.length; i++) {
        /* Integer to string */
        nodeNames[i] = Integer.toString(nodeNamesAsInts[i]);
      }
    }
    return nodeNames;
  }

  /* private function to the flow node with specified name */
  private Flow getFlow(String flowName) {
    /* Get requested flow node */
    var flow = flows.get(flowName);
    /* Return empty node if not found */
    if (flow == null) {
      flow = new Flow();
      System.out.printf(FLOW_WARNING + "retrieve it.\n", flowName);
    }
    return flow;
  }
  /**
   * Get the names of all the flows in the graph file, ordered as they were read from
   * the graph file.

   * @return Array of all flow names ordered as read
   */
  public String[] getFlowNames() {
    return flowNamesInOriginalOrder.toArray(new String[0]);
    // could use new String[list.size()], but due to JVM optimizations new (new String[0] is better 
  }
  /**
   * Get the index of the node in the nodes dictionary.

   * @param nodeName Name of node.
   * @return Index of node
   */
  public Integer getNodeIndex(String nodeName) {
    var index = 0;
    /* Could throw exception if null but just return 0 for now */
    var node = nodes.get(nodeName);
    if (node != null) {
      index = node.getIndex();
    }
    return index;
  }

  /**
   * Get the names of all the nodes in a flow, ordered as they exist in the flow specification.

   * @param flowName Name of flow
   * @return All node names in the flow
   */
  public String[] getNodesInFlow(String flowName) {
    var flow = flows.get(flowName);
    String[] nodes;
    if (flow != null) {
      nodes = new String[flow.nodes.size()];
      for (int i = 0; i < nodes.length; i++) {
        /* Get node from array, get node's name and store it in array */
        var node = flow.nodes.get(i);
        nodes[i] = node.getName();
      }

    } else {
      /* return empty array */
      nodes = new String[0];
      System.out.printf("\n\t Warning! No Flow with name %s.\n", flowName);
    }
    return nodes;
  }

  /**
   * Calculates and returns the least common multiple (LCM) of the periods
   * of all flows to determine the hyperPeriod.

   * @return the hyperPeriod of the flow
   */
  public Integer getHyperPeriod() {
    /* hyperPeriod is LCM of all periods, initialize to 1 */
    var hyperPeriod = 1;
    for (String flowName : getFlowNames()) {
      // var dm = new Utlitities();
      /* Find LCM of hyperPeriod so far and the current period,
       * update hyperPeriod with new value */
      hyperPeriod = Utilities.lcm(hyperPeriod, getFlowPeriod(flowName));
    }
    return hyperPeriod;
  }

  /**
   * Get the total cost of all transmission attempts in flow.

   * @param flowName Name of the flow
   * @return Cost of all transmissions
   */
  public Integer getTotalTxAttemptsInFlow(String flowName) {
    var flow = getFlow(flowName);
    var linkTxAndTotalCost = flow.getLinkTxAndTotalCost();
    var totalCostIndex = linkTxAndTotalCost.size() - 1;
    var totalCost = linkTxAndTotalCost.get(totalCostIndex);
    return totalCost;
  }

  /**
   * Get the number of transmissions needed per link to meet E2E reliability target.

   * @param flowName Name of flow
   * @return Array of number of transmissions needed per link
   */
  public Integer[] getNumTxAttemptsPerLink(String flowName) {
    var flow = getFlow(flowName);
    var linkTxAndTotalCost = new ArrayList<Integer>(flow.getLinkTxAndTotalCost());
    var lastElement = linkTxAndTotalCost.size() - 1;
    /* Remove last element since that is the sum of the others */
    linkTxAndTotalCost.remove(lastElement);
    return linkTxAndTotalCost.toArray(new Integer[0]);
  }

  /**
   * Add an edge to a given node.

   * @param nodeName Name of the node for new edge
   * @param edge New edge to be added
   */
  public void addEdge(String nodeName, Edge edge) {
    /* Get node object */
    var node = nodes.get(nodeName);
    node.addEdge(edge);
  }

  /**
   * Get the length of the longest flow among all flows.

   * @return Longest flow integer
   */
  public Integer maxFlowLength() {
    Integer maxLength = 0;
    for (Flow flow : flows.values()) {
      maxLength = Math.max(maxLength, flow.nodes.size());
    }
    return maxLength;
  }
}
