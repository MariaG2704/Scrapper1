package edu.uiowa.cs.warp;

import java.util.ArrayList;

/**
 * Represents a flow in a network with nodes, edges, and transmission parameters.
 * Supports priority-based scheduling and comparison.

 * @version 2.0
 * @author sgoddard
 * @author ccolin - modified as per HW2 instructions
 *
 */
// Fixed trivial Coding Style errors such as indentation, spacing, grammar
// Fixed existing semantic comment mistakes (suspected coming from copy-pasting similar methods)
public class Flow extends SchedulableObject implements Comparable<Flow> {

  private static final Integer UNDEFINED = -1;
  private static final Integer DEFAULT_FAULTS_TOLERATED = 0; 
  private static final Integer DEFAULT_INDEX = 0;
  private static final Integer DEFAULT_PERIOD = 100; 
  private static final Integer DEFAULT_DEADLINE = 100;
  private static final Integer DEFAULT_PHASE = 0;


  Integer initialPriority = UNDEFINED;
  Integer index;  // order in which the node was read from the Graph file
  Integer numTxPerLink; //  determined by fault model
  ArrayList<Node> nodes; // Flow source is 1st element and flow sink is last element in array
  /*
   *  nTx needed for each link to reach E2E reliability target. Indexed by source node of the link. 
   *  Last entry is total worst-case E2E Tx cost for schedulability analysis
   */
  ArrayList<Integer> linkTxAndTotalCost; 
  ArrayList<Edge> edges; //used in Partition and scheduling
  Node nodePredecessor;
  Edge edgePredecessor;
    
  /**
   * Constructor that sets name, priority, and index.

   * @param name        name of flow
   * @param priority    priority of flow
   * @param index       index of flow
   */
  Flow(String name, Integer priority, Integer index) {
    super(name, priority, DEFAULT_PERIOD, DEFAULT_DEADLINE, DEFAULT_PHASE);
    this.index = index;
    /*
     *  Default numTxPerLink is 1 transmission per link. Will be updated based
     *  on flow updated based on flow length and reliability parameters
     */
    this.numTxPerLink = DEFAULT_FAULTS_TOLERATED + 1; 
    this.nodes = new ArrayList<>();
    this.edges  = new ArrayList<>();
    this.linkTxAndTotalCost = new ArrayList<>();
    this.edges = new ArrayList<>();
    this.nodePredecessor = null;
    this.edgePredecessor = null;
  }
    
  /**
   * Default constructor that sets name, priority, and index to default values.
   */
  Flow() {
    super();
    this.index = DEFAULT_INDEX;
    /*
     *  Default numTxPerLink is 1 transmission per link. Will be updated based
     *  on flow updated based on flow length and reliability parameters
     */
    this.numTxPerLink = DEFAULT_FAULTS_TOLERATED + 1; 
    this.nodes = new ArrayList<>();
    this.linkTxAndTotalCost = new ArrayList<>();
    this.edges = new ArrayList<>();
    this.nodePredecessor = null;
    this.edgePredecessor = null;
  }

  /**
   * Get initial priority settings.

   * @return the initialPriority
   */
  public Integer getInitialPriority() {
    return initialPriority;
  }

  /**
   * Get current index.

   * @return the index
   */
  public Integer getIndex() {
    return index;
  }

  /**
   * Number of transmissions required per link in the flow.

   * @return the numTxPerLink
   */
  public Integer getNumTxPerLink() {
    return numTxPerLink;
  }

  /**
   * Get all nodes present.

   * @return the nodes
   */
  public ArrayList<Node> getNodes() {
    return nodes;
  }

  /**
   * Get all edges connecting nodes.

   * @return the edges
   */
  public ArrayList<Edge> getEdges() {
    return edges;
  }

  /**
   * Add an edge to the flow.

   * @param edge    Edge to be added.
   */
  public void addEdge(Edge edge) {
    /* set predecessor and add edge to flow */
    edge.setPredecessor(edgePredecessor);
    edges.add(edge);
    /* update predecessor for next edge added */
    edgePredecessor = edge;
  }

  /**
   * Add a node to the flow.

   * @param node    Node to be added.
   */
  public void addNode(Node node) {
    /* set predecessor and add node to flow */
    node.setPredecessor(nodePredecessor);
    nodes.add(node);
    /* update predecessor for next node added */
    nodePredecessor = node;
  }
  
  /**
   * Transmission needed to achieve E2E reliability and total transmission cost.

   * @return the linkTxAndTotalCost
   */
  
  public ArrayList<Integer> getLinkTxAndTotalCost() {
    return linkTxAndTotalCost;
  }

  /**
   * Sets priority of flow object.

   * @param initialPriority the initialPriority to set
   */
  public void setInitialPriority(Integer initialPriority) {
    this.initialPriority = initialPriority;
  }

  /**
   * Sets index of flow.

   * @param index the index to set
   */
  public void setIndex(Integer index) {
    this.index = index;
  }

  /**
   * Sets the number of transmissions per link.

   * @param numTxPerLink the numTxPerLink to set
   */
  public void setNumTxPerLink(Integer numTxPerLink) {
    this.numTxPerLink = numTxPerLink;
  }

  /**
   * Sets nodes to flow.

   * @param nodes the nodes to set
   */
  public void setNodes(ArrayList<Node> nodes) {
    this.nodes = nodes;
  }

  /**
   * Sets the transmission and total cost for E2E.

   * @param linkTxAndTotalCost the linkTxAndTotalCost to set
   */
  public void setLinkTxAndTotalCost(ArrayList<Integer> linkTxAndTotalCost) {
    this.linkTxAndTotalCost = linkTxAndTotalCost;
  }

  /**
   * Comparison override so that flow priority goes from 0 to infinity in highest priority.

   * @return the priority integer
   */
  @Override
  public int compareTo(Flow flow) {
    // ascending order (0 is highest priority)
    return flow.getPriority() > this.getPriority() ? -1 : 1;
  }

  /**
   * toString override so flow strings show names.

   * @return name of flow
   */
  @Override
  public String toString() {
    return getName();
  }
    
}
