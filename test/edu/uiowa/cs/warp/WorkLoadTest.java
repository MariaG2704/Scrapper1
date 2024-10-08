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

/**
 * Set of JUnit 5 test cases for Workload.java that
 * tests a total of 17 methods of Workload.java for correctness.
 */
class WorkLoadTest {

  private WorkLoad workLoad;
  private final PrintStream originalOut = System.out;
  private ByteArrayOutputStream tempOutContent = new ByteArrayOutputStream();

  /**
   * Sets up the testing Workload object for each test case.

   * @throws java.lang.Exception in case of setup error
   */
  @BeforeEach
  void setUp() throws Exception {
    //Initialize workload with default values given in WorkLoad.java comment
    workLoad = new WorkLoad(0.9, 0.99, "StressTest.txt");
    //Captures System.out prints to check if printed warnings are correctly output
    //Better way would be to return the warning string from the methods themselves in Workload.java 
    tempOutContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(tempOutContent));
  }
  
  @AfterEach
  void tearDown() throws Exception {
    /*Resets output to correctly show on console after tests
     *If test itself wants to print, set setOut to originalOut at the start
     *of the test case itself
     */
    System.setOut(originalOut);
  }

  /**
   * Test adding valid flow to WorkLoad, check if new flow is in WorkLoad.
   * Test method for {@link edu.uiowa.cs.warp.WorkLoad#addFlow(java.lang.String)}.
   */
  @Test
  void testAddValidFlow() {
    workLoad.addFlow("Flow1");
    assertTrue(workLoad.getFlows().containsKey("Flow1"), "Flow not added to WorkLoad");
  }

  /**
   * Test adding a flow with an existing name to the WorkLoad.
   * Check that workLoad size (number of flows) hasn't changed.
   * Test method for {@link edu.uiowa.cs.warp.WorkLoad#addFlow(java.lang.String)}.
   */
  @Test
  void testAddFlowWithExistingNameNoSizeChange() {
    int flowSize = workLoad.getFlows().size();
    workLoad.addFlow("Flow1");
    flowSize++;
    workLoad.addFlow("Flow1");
    assertEquals(flowSize, workLoad.getFlows().size(),
                 "Existing flow incorrectly added to WorkLoad, increased size");
  }

  /**
   * Test adding a flow with an existing name to the WorkLoad.
   * Check that error has been correctly output to user.
   * Test method for {@link edu.uiowa.cs.warp.WorkLoad#addFlow(java.lang.String)}.
   */
  @Test
  void testAddFlowWithExistingNameCorrectWarning() {
    workLoad.addFlow("Flow1");
    workLoad.addFlow("Flow1");
    String expectedMessageString = "\n\tWarning! A flow with name Flow1 already exists. "
            + "It has been replaced with a new flow.\n";
    String actualMessageString = tempOutContent.toString();
    assertEquals(expectedMessageString, actualMessageString, "Warning message did not"
               + " appear or didn't appear correctly formatted");
  }

  /**
   * Test adding a node to a flow.
   * Check that node is in flow after adding.
   * Test for {@link edu.uiowa.cs.warp.WorkLoad#addNodeToFlow(java.lang.String, java.lang.String)}.
   */
  @Test
  void testAddNewNodeToFlowNodeNowExists() {
    // Set up the test flow
    workLoad.addFlow("Flow1");
    // Add a new node to the flow
    workLoad.addNodeToFlow("Flow1", "Node1");
    assertTrue(workLoad.getNodes().containsKey("Node1"), "Node1 should be added to Flow1");
    assertEquals(1, workLoad.getNodesInFlow("Flow1").length, "Node1 should be added to Flow1");
  }
  
  /**
   * Test adding an existing node to a flow.
   * Check that node hasn't been duplicated.
   * Test for {@link edu.uiowa.cs.warp.WorkLoad#addNodeToFlow(java.lang.String, java.lang.String)}.
   */
  @Test
  void testAddExistingNodeToFlowTwoExistsInFlow() {
    workLoad.addFlow("Flow1");
    workLoad.addNodeToFlow("Flow1", "Node1");
    workLoad.addNodeToFlow("Flow1", "Node1");
    assertEquals(2, workLoad.getNodesInFlow("Flow1").length, "Node1 should be in Flow1 twice");
  }
  
  /**
   * Test adding an existing node to a flow.
   * Check that node hasn't been duplicated.
   * Test for {@link edu.uiowa.cs.warp.WorkLoad#addNodeToFlow(java.lang.String, java.lang.String)}.
   */
  @Test
  void testAddExistingNodeToFlowOneExistsInNodeMap() {
    workLoad.addFlow("Flow1");
    workLoad.addNodeToFlow("Flow1", "Node1");
    int expectedNodeMapSize = workLoad.getNodes().size();
    workLoad.addNodeToFlow("Flow1", "Node1");
    assertEquals(expectedNodeMapSize, workLoad.getNodes().size(),
                 "Node1 shouldn't be in NodeMap twice");
  }

  /**
   * Test whether new node priorities get assigned correctly.
   * Check if Node_n in new flow has priority n-1.
   * Test {@link edu.uiowa.cs.warp.WorkLoad#getFlowPriority(java.lang.String, java.lang.String)}.
   */
  @Test
  void testGetFlowPriorityOfNodeInFlow() {
    workLoad.addFlow("Flow1");
    workLoad.addNodeToFlow("Flow1", "Node1");
    workLoad.addNodeToFlow("Flow1", "Node2");
    workLoad.addNodeToFlow("Flow1", "Node3");
    assertEquals(2, workLoad.getFlowPriority("Flow1", "Node3"), "Node3 should have priority 2");
  }
  
  /**
   * Test whether default return in case of non-existing node is 0.
   * Test {@link edu.uiowa.cs.warp.WorkLoad#getFlowPriority(java.lang.String, java.lang.String)}.
   */
  @Test
  void testGetFlowPriorityOfNonExistingNodeDefaultToZero() {
    workLoad.addFlow("Flow1");
    workLoad.addNodeToFlow("Flow1", "Node1");
    assertEquals(0, workLoad.getFlowPriority("Flow1", "Node2"),
                 "Node2 not in flow, should default to 0");
  }

  /**
   * Test whether default return in case of empty flow is 0.
   * Test {@link edu.uiowa.cs.warp.WorkLoad#getFlowPriority(java.lang.String, java.lang.String)}.
   */
  @Test
  void testGetFlowPriorityOfNodeInEmptyFlow() {
    workLoad.addFlow("Flow1");
    assertEquals(0, workLoad.getFlowPriority("Flow1", "Node1"),
                 "Node1 not in flow, should default to 0");
  }

  /**
   * Tests whether a new flow correctly gets assigned the last priority.
   * By induction this also tests that first flow of empty workload will have priority 0.
   * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getFlowPriority(java.lang.String)}.
   */
  @Test
  void testGetFlowPriorityNewFlowHasLastPriority() {
    //Given current flows size n, last priority flow should have priority n-1, meaning new Flow1
    //should have priority n
    int expectedPriority = workLoad.getFlows().size();
    workLoad.addFlow("Flow1");
    assertEquals(expectedPriority, workLoad.getFlowPriority("Flow1"),
                 "Flow1 should have last priority");
  }

  /**
   * Test that warning message appears when getting priority of non-existing flow
   * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getFlowPriority(java.lang.String)}.
   */
  @Test
  void testGetFlowPriorityNonExistingFlow() {
    workLoad.getFlowPriority("Flow1");
    String expectedMessageString = "\n\tWarning! Bad situation: Flow Flow1 doesn't exist but "
                                   + "trying to retrieve it.\n";
    String actualMessageString = tempOutContent.toString();
    assertEquals(expectedMessageString, actualMessageString, "Warning message didn't send or"
                                                             + "wasn't formatted correctly");
  }

  /**
   * Tests whether existing flow priority is correctly changed
   * Test {@link edu.uiowa.cs.warp.WorkLoad#setFlowPriority(java.lang.String, java.lang.Integer)}.
   */
  @Test
  void testSetFlowPriorityOfExistingFlowToZero() {
    workLoad.addFlow("Flow1");
    workLoad.setFlowPriority("Flow1", 0);
    assertEquals(0, workLoad.getFlowPriority("Flow1"));
  }
  
  /**
   * Tests whether existing flow priority is correctly changed
   * Negative priority is semantically wrong but current code-base doesn't check
   * Only checks for if integer
   * Test {@link edu.uiowa.cs.warp.WorkLoad#setFlowPriority(java.lang.String, java.lang.Integer)}.
   */
  @Test
  void testSetFlowPriorityOfExistingFlowToNegative() {
    workLoad.addFlow("Flow1");
    workLoad.setFlowPriority("Flow1", -1);
    assertEquals(-1, workLoad.getFlowPriority("Flow1"));
  }
  
  /**
   * Tests setting priority of non-existing flow
   * Semantically wrong, but is correctly handled by getFlow error
   * Technically this is a getFlow test, not SetFlowPriority test
   * Test {@link edu.uiowa.cs.warp.WorkLoad#setFlowPriority(java.lang.String, java.lang.Integer)}.
   */
  @Test
  void testSetFlowPriorityOfNonExistingFlow() {
    //Flow doesn't exist, technically getFlow creates new flow with default priority 0
    workLoad.setFlowPriority("Flow1", 0);
    assertEquals(0, workLoad.getFlowPriority("Flow1"));
  }

  /**
   * Tests whether existing flow deadline is correctly changed
   * Test {@link edu.uiowa.cs.warp.WorkLoad#setFlowDeadline(java.lang.String, java.lang.Integer)}.
   */
  @Test
  void testSetFlowDeadlineOfExistingFlowToZero() {
    workLoad.addFlow("Flow1");
    workLoad.setFlowDeadline("Flow1", 0);
    assertEquals(0, workLoad.getFlowDeadline("Flow1"));
  }
  
  /**
   * Tests whether existing flow deadline is correctly changed
   * Negative deadline is semantically wrong but current code-base doesn't check
   * Only checks for if integer
   * Test {@link edu.uiowa.cs.warp.WorkLoad#setFlowDeadline(java.lang.String, java.lang.Integer)}.
   */
  @Test
  void testSetFlowDeadlineOfExistingFlowToNegative() {
    workLoad.addFlow("Flow1");
    workLoad.setFlowDeadline("Flow1", -1);
    assertEquals(-1, workLoad.getFlowDeadline("Flow1"));
  }
  
  /**
   * Tests setting deadline of non-existing flow
   * Semantically wrong, but is correctly handled by getFlow error
   * Technically this is a getFlow test, not SetFlowDeadline test
   * Test {@link edu.uiowa.cs.warp.WorkLoad#setFlowDeadline(java.lang.String, java.lang.Integer)}.
   */
  @Test
  void testSetFlowDeadlineOfNonExistingFlow() {
    //Flow doesn't exist, technically getFlow creates new flow with default deadline 100
    workLoad.setFlowDeadline("Flow1", 0);
    assertEquals(100, workLoad.getFlowDeadline("Flow1"));
  }

  /**
   * Tests getFlowDeadline for all flows in StressTest.txt
   * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getFlowDeadline(java.lang.String)}.
   */
  @Test
  void testGetFlowDeadlineDefault() {
    int deadlineF1 = workLoad.getFlowDeadline("F1");
    assertEquals(20, deadlineF1, "get deadline for F1 failed");

    int deadlineF2 = workLoad.getFlowDeadline("F2");
    assertEquals(50, deadlineF2, "get deadline for F2 failed");

    int deadlineF3 = workLoad.getFlowDeadline("F3");
    assertEquals(50, deadlineF3, "get deadline for F3 failed");

    int deadlineF4 = workLoad.getFlowDeadline("F4");
    assertEquals(75, deadlineF4, "get deadline for F4 failed");

    int deadlineF5 = workLoad.getFlowDeadline("F5");
    assertEquals(75, deadlineF5, "get deadline for F5 failed");

    int deadlineF6 = workLoad.getFlowDeadline("F6");
    assertEquals(75, deadlineF6, "get deadline for F6 failed");

    int deadlineF7 = workLoad.getFlowDeadline("F7");
    assertEquals(100, deadlineF7, "get deadline for F7 failed");

    int deadlineF8 = workLoad.getFlowDeadline("F8");
    assertEquals(100, deadlineF8, "get deadline for F8 failed");

    int deadlineF9 = workLoad.getFlowDeadline("F9");
    assertEquals(100, deadlineF9, "get deadline for F9 failed");

    int deadlineF10 = workLoad.getFlowDeadline("F10");
    assertEquals(100, deadlineF10, "get deadline for F10 failed");

    int deadlineAF1 = workLoad.getFlowDeadline("AF1");
    assertEquals(20, deadlineAF1, "get deadline for AF1 failed");

    int deadlineAF2 = workLoad.getFlowDeadline("AF2");
    assertEquals(50, deadlineAF2, "get deadline for AF2 failed");

    int deadlineAF4 = workLoad.getFlowDeadline("AF4");
    assertEquals(75, deadlineAF4, "get deadline for AF4 failed");

    int deadlineAF5 = workLoad.getFlowDeadline("AF5");
    assertEquals(75, deadlineAF5, "get deadline for AF5 failed");

    int deadlineAF10 = workLoad.getFlowDeadline("AF10");
    assertEquals(100, deadlineAF10, "get deadline for AF10 failed");
  }

  /**
   * Tests getting a deadline of a flow that does not exist, 
   * should return default value of 100
   * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getFlowDeadline(java.lang.String)}.
   */
  @Test
  void testGetFlowDeadlineNonExistent() {
    int deadlineNonExistent = workLoad.getFlowDeadline("not a flow");
    assertEquals(100, deadlineNonExistent);
  }

  /**
   * Test getFlowTxAttemptsPerLink with the StressTest.txt
   * Should all have a value of 3
   * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getFlowTxAttemptsPerLink(java.lang.String)}.
   */
  @Test
  void testGetFlowTxAttemptsPerLinkDefault() {
    assertEquals(3, workLoad.getFlowTxAttemptsPerLink("F1"), "flow F1 fail");
    assertEquals(3, workLoad.getFlowTxAttemptsPerLink("F2"), "flow F2 fail");
    assertEquals(3, workLoad.getFlowTxAttemptsPerLink("F3"), "flow F3 fail");
    assertEquals(3, workLoad.getFlowTxAttemptsPerLink("F4"), "flow F4 fail");
    assertEquals(3, workLoad.getFlowTxAttemptsPerLink("F5"), "flow F5 fail");
    assertEquals(3, workLoad.getFlowTxAttemptsPerLink("F6"), "flow F6 fail");
    assertEquals(3, workLoad.getFlowTxAttemptsPerLink("F7"), "flow F7 fail");
    assertEquals(3, workLoad.getFlowTxAttemptsPerLink("F8"), "flow F8 fail");
    assertEquals(3, workLoad.getFlowTxAttemptsPerLink("F9"), "flow F9 fail");
    assertEquals(3, workLoad.getFlowTxAttemptsPerLink("F10"), "flow F10 fail");
    assertEquals(3, workLoad.getFlowTxAttemptsPerLink("AF1"), "flow AF1 fail");
    assertEquals(3, workLoad.getFlowTxAttemptsPerLink("AF2"), "flow AF2 fail");
    assertEquals(3, workLoad.getFlowTxAttemptsPerLink("AF4"), "flow AF4 fail");
    assertEquals(3, workLoad.getFlowTxAttemptsPerLink("AF5"), "flow AF5 fail");
    assertEquals(3, workLoad.getFlowTxAttemptsPerLink("AF10"), "flow AF10 fail");
  }

  /**
   * Test getFlowTxAttempsPerLinkNonExisting with a non-existent flow
   * Default number of transmissions per link is the default number of 
   * faults tolerated (0) plus 1. 
   * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getFlowTxAttemptsPerLink(java.lang.String)}.
   */
  @Test
  void testGetFlowTxAttemptsPerLinkNonExisting() {
    /* default flow should return value of 1 */
    workLoad = new WorkLoad(0.9, 0.99, "EmptyTest.txt");
    assertEquals(1, workLoad.getFlowTxAttemptsPerLink("Flow1"));
  }

  /**
   * Test for setFlowsInRMorder, tests the default workFlow object
   * Test method for {@link edu.uiowa.cs.warp.WorkLoad#setFlowsInRMorder()}.
   */
  @Test
  void testSetFlowsInRMorderDefault(){
    workLoad.setFlowsInRMorder();
    Iterator<String> iter = workLoad.getFlowNamesInPriorityOrder().iterator();
    assertWorkloadFlows(iter); //assert results
  }

  /**
   * Test for setFlowsInRMorder, tests sorting an empty WorkLoad.
   * Test method for {@link edu.uiowa.cs.warp.WorkLoad#setFlowsInRMorder()}.
   */
  @Test
  void testSetFlowsInRMorderEmpty(){
    workLoad = new WorkLoad(0.9, 0.99, "EmptyTest.txt");
    ArrayList<String> expected = new ArrayList<String>();
    workLoad.setFlowsInRMorder();
    ArrayList<String> sortedFlows = workLoad.getFlowNamesInPriorityOrder();
    assertTrue(expected.equals(sortedFlows), "expected empty ArrayList, result not empty.");
  }

  /**
   * Test for setFlowsInRMorder
   * Stress test that runs the function 500 times, with workLoads with random flow counts, 
   * and random periods and priorities for flows.
   * Test method for {@link edu.uiowa.cs.warp.WorkLoad#setFlowsInRMorder()}.
   */
  @RepeatedTest(500)
  void testSetFlowsInRMorderStressTest() {
    generateRandomWorkloadFlows(); //generates random workload flows
    workLoad.setFlowsInRMorder(); 
    Iterator<String> iter = workLoad.getFlowNamesInPriorityOrder().iterator();
    assertWorkloadFlows(iter); //assert resulting list
    workLoad = new WorkLoad(0.9, 0.99, "StressTest.txt"); //reset workLoad after each iteration
  }

  /**
   * Helper method for testSetFlowsInRMorder stress test, generates random workload flows.
   */
  private void generateRandomWorkloadFlows() {
    int count = 1;
    for (int length = 0; length < Math.random() * 100; length++) { //length varies 0-100 flows
      String flowName = "Flow" + Integer.toString(count);
      workLoad.addFlow(flowName);
      workLoad.setFlowPeriod(flowName, (int) Math.random() * 1000); //set period 0-1000 randomly
      workLoad.setFlowPriority(flowName, (int) Math.random() * 1000); //set priority 0-1000 randomly
      count++;
    }
  }

  /**
   * Helper method for all tests for setFlowsInRMorder, goes through and checks
   * order of all the flows and their periods and priorities.
   * 
   * @param iter - Iterator<String> for sorted flow names
   */
  private void assertWorkloadFlows(Iterator<String> iter) {
    int prevPeriod = 0;
    int prevPriority = 0;
    while (iter.hasNext()) { //iterate through and assert results
      String currentFlowName = iter.next();
      int currentPeriod = workLoad.getFlowPeriod(currentFlowName);
      int currentPriority = workLoad.getFlowPriority(currentFlowName);
      assertTrue(currentPeriod >= prevPeriod, "flows not sorted by period, " + "Flows: "
                 + Arrays.toString(workLoad.getFlowNames()));
      assertTrue(currentPriority >= prevPriority, "flows not sorted by priority, " + "Flows: "
                 + Arrays.toString(workLoad.getFlowNames()));
      prevPeriod = currentPeriod;
      prevPriority = currentPriority;
    }
  }

  /**
   * Test method for getNodeNamesOrderedAlphabetically, uses the default workload object
   * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getNodeNamesOrderedAlphabetically()}.
   */
  @Test
  void testGetNodeNamesOrderedAlphabeticallyDefault() {
    String[] sortedNodes = workLoad.getNodeNamesOrderedAlphabetically();
    assertWorkloadNodes(sortedNodes); //assert results
  }

  /**
   * Test method for getNodeNamesOrderedAlphabetically, tests
   * sorting a workload with no nodes.
   * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getNodeNamesOrderedAlphabetically()}.
   */
  @Test
  void testGetNodeNamesOrderedAlphabeticallyEmpty() {
    String[] expected = new String[0];
    workLoad = new WorkLoad(0.9, 0.99, "EmptyTest.txt");
    String[] sortedNodes = workLoad.getNodeNamesOrderedAlphabetically();
    assertArrayEquals(expected, sortedNodes, "expected empty array");
  }

  /**
   * Test method for getNodeNamesOrderedAlphabetically, randomly generates nodes
   * in random order and numbers and asserts the result.
   * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getNodeNamesOrderedAlphabetically()}.
   */
  @RepeatedTest(500)
  void testGetNodeNamesOrderedAlphabeticallyStressTest() {
    generateRandomNodes();
    String[] sortedNodes = workLoad.getNodeNamesOrderedAlphabetically();
    assertWorkloadNodes(sortedNodes);
    workLoad = new WorkLoad(0.9, 0.99, "StressTest.txt"); //reset workLoad after each iteration
  }

  /**
   * Helper method for getNodeNamesOrderedAlphabeticallyStressTest, 
   * generates random length nodes in flow with random names out of 
   * 26 alphabet letters.
   */
  private void generateRandomNodes() {
    String[] nodeNames = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
                                      "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
                                      "Y", "Z"};
    workLoad.addFlow("Flow1");
    for (int i = 0; i < Math.random() * 100; i++) {
      String nodeName = nodeNames[(int) (Math.random() * 25.99)]; //random alphabet as name
      workLoad.addNodeToFlow("Flow1",  nodeName);
    }
  }

  /**
   * Helper method for tests for getNodeNamesOrderedAlphabetically, 
   * loops through array and checks alphabetical order.

   * @param nodes - array of node names
   */
  private void assertWorkloadNodes(String[] nodes) {
    char previousLetter = 0; //null value for char
    for (String node : nodes) {
      char currentLetter = node.charAt(0); //get first char of nodeName
      assertTrue(currentLetter >= previousLetter, "nodes not sorted alphabetically, "
                 + "Displaying nodes: " + Arrays.toString(nodes));
      previousLetter = currentLetter; //set previousLetter to current
    }
  }

  /**
   * Tests the getFlowNames result for the default StressTest.txt
   * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getFlowNames()}.
   */
  @Test
  void testGetFlowNames1() {
    String[] expected = new String[] {"F1", "F5", "F2", "F4", "F3", "F6", "F7", "F8", 
                                      "F9", "F10", "AF1", "AF5", "AF2", "AF4", "AF10"};
    String[] actual = workLoad.getFlowNames();
    Assert.assertArrayEquals(expected, actual);
  }

  /**
   * Tests the getFlowNames result with StressTest4.txt.
   * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getFlowNames()}.
   */
  @Test
  void testGetFlowNames2() {
    workLoad = new WorkLoad(0.9, 0.99, "Test1.txt");
    String[] expected = new String[] {"F1A", "F1B"};
    String[] actual = workLoad.getFlowNames();
    Assert.assertArrayEquals(expected, actual);
  }

  /**
   * Tests getFlowNames of an empty workload. Should return
   * an empty array
   * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getFlowNames()}.
   */
  @Test
  void testGetFlowNamesEmpty() {
    workLoad = new WorkLoad(0.9, 0.99, "EmptyTest.txt");
    String[] expected = new String[0];
    String[] sortedNodes = workLoad.getFlowNames();
    Assert.assertArrayEquals(expected, sortedNodes);
  }

  /**
   * Tests getNodeIndex with StressTest.txt nodes
   * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getNodeIndex(java.lang.String)}.
   */
  @Test
  void testGetNodeIndexDefault() {
    int aNode = workLoad.getNodeIndex("A");
    int bNode = workLoad.getNodeIndex("B");
    int cNode = workLoad.getNodeIndex("C");
    int dNode = workLoad.getNodeIndex("D");
    int eNode = workLoad.getNodeIndex("E");
    int fNode = workLoad.getNodeIndex("F");
    int gNode = workLoad.getNodeIndex("G");
    int hNode = workLoad.getNodeIndex("H");
    int iNode = workLoad.getNodeIndex("I");
    int jNode = workLoad.getNodeIndex("J");
    int kNode = workLoad.getNodeIndex("K");
    int lNode = workLoad.getNodeIndex("L");
    int mNode = workLoad.getNodeIndex("M");
    int nNode = workLoad.getNodeIndex("N");
    assertEquals(0, bNode, "node B fail");
    assertEquals(1, cNode, "node C fail");
    assertEquals(2, dNode, "node D fail");
    assertEquals(3, aNode, "node A fail");
    assertEquals(4, eNode, "node E fail");
    assertEquals(5, fNode, "node F fail");
    assertEquals(6, gNode, "node G fail");
    assertEquals(7, hNode, "node H fail");
    assertEquals(8, iNode, "node I fail");
    assertEquals(9, jNode, "node J fail");
    assertEquals(10, kNode, "node K fail");
    assertEquals(11, lNode, "node L fail");
    assertEquals(15, mNode, "node M fail");
    assertEquals(12, nNode, "node N fail");
  }

  /**
   * Tests getNodeIndex on a flow that does not exist, 
   * should return the null value 0.
   * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getNodeIndex(java.lang.String)}.
   */
  @Test
  void testGetNodeIndexNonExistent() {
    int indexNonExistent = workLoad.getNodeIndex("not a node");
    assertEquals(0, indexNonExistent);
  }

  /**
   * Tests if the outputed flows are in order 
   * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getNodesInFlow(java.lang.String)}.
   */
  @Test
  void testGetNodeInFlowOrder() {
    String[] flows = workLoad.getNodesInFlow("F1");
    String order = flows[0] + " " + flows[1] + " " + flows[2];
    assertEquals("B C D", order);
  }

  /**
   * Tests if an empty string array is returned for a non valid flow name input 
   * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getNodesInFlow(java.lang.String)}.
   */
  @Test
  void testGetNodesInFlowDefault() {
    String[] flows = workLoad.getNodesInFlow("A2"); 
    assertEquals(0, flows.length);
  }

  /**
   * Tests the hyper period of a workload with multiple flows 
   * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getHyperPeriod()}.
   */
  @Test
  void testGetHyperPeriodMutipleFlows() {
    assertEquals(300, workLoad.getHyperPeriod());
  }

  /**
   * Tests the hyper period of a workload with one flows
   * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getHyperPeriod()}.
   */
  @Test
  void testGetHyperPeriodOneFlow() {
    WorkLoad workLoadOneFlow = new WorkLoad(0.9, 0.99, "LongChain.txt");
    assertEquals(150, workLoadOneFlow.getHyperPeriod());
  }

  /**
   * Tests if the defaulted 1 is printed out for a workLoad with no flowPeriods
   * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getHyperPeriod()}.
   */
  @Test 
  void testDefaultHyperPeriod() {
    WorkLoad workLoadNoFlow = new WorkLoad(0.9, 0.99, "Example3.txt");
    assertEquals(100, workLoadNoFlow.getHyperPeriod());
  }

  /**
   * Test if the number of transmission attempts if correctly outputted 
   * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getTotalTxAttemptsInFlow(java.lang.String)}.
   */
  @Test 
  void testValidFlowNameForGetTotalTxAttemptInFlow() {
    assertEquals(4, workLoad.getTotalTxAttemptsInFlow("F1"));
  }

  /**
   * Tests if the default is printed out if an un-valid flow name is used as a argument 
   * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getTotalTxAttemptsInFlow(java.lang.String)}.
   */
  @Test 
  void testUnValidFlowNameForGetTotalTxAttemptInFlow() {
    assertEquals(-1, workLoad.getTotalTxAttemptsInFlow("A12"));
  }

  /**
   * Tests if the outputted integer array is correct by testing one index
   * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getNumTxAttemptsPerLink(java.lang.String)}.
   */
  @Test
  void testGetNumTxAttemptsPerLink() {
    Integer[] output = workLoad.getNumTxAttemptsPerLink("F1");
    assertEquals(3, output[0]);
  }

  /**
   * Tests if the method getNumTxAttemptsPerLink correctly returns an
   * empty array of integers for a non valid flow name 
   * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getNumTxAttemptsPerLink(java.lang.String)}.
   */
  @Test
  void testGetNumTxAttemptsPerLinkNonValidFlowName() {
    Integer[] output = workLoad.getNumTxAttemptsPerLink("A12");
    assertTrue(output.length == 0);
  }

  /**
   * Tests if the largest flow length is correctly outputted for a workload
   * with multiple flows of the different lengths
   * Test method for {@link edu.uiowa.cs.warp.WorkLoad#maxFlowLength()}.
   */
  @Test
  void testMaxFlowLengthMutipleFlowsWithDifferentLengths() {
    WorkLoad workLoadWithFlowsOfDiffLengths = new WorkLoad(0.9, 0.99, "Example4.txt");
    Integer output = workLoadWithFlowsOfDiffLengths.maxFlowLength();
    assertEquals(4, output);
  }

  /**
   * Tests if the largest flow length is correctly outputted for a workload
   * with multiple flows of the same length 
   * Test method for {@link edu.uiowa.cs.warp.WorkLoad#maxFlowLength()}.
   */
  @Test
  void testMaxFlowLengthMutipleFlowsWithSameLengths() {
    Integer output = workLoad.maxFlowLength();
    assertEquals(8, output);
  }

  /**
   * Tests if the largest flow length is correctly outputted for a workload with one flows 
   * Test method for {@link edu.uiowa.cs.warp.WorkLoad#maxFlowLength()}.
   */
  @Test
  void testMaxFlowLengthOneFlow() {
    WorkLoad workLoadOneFlow = new WorkLoad(0.9, 0.99, "LongChain.txt");
    Integer output = workLoadOneFlow.maxFlowLength();
    assertEquals(26, output);
  }
}
