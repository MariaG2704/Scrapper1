package edu.uiowa.cs.warp;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getFlowDeadline(java.lang.String)}.
	 */
	@Test
	void testGetFlowDeadline() {
		fail("Not yet implemented");
	}

	/**
	 * Test getFlowTxAttemptsPerLink with a default flow
	 * Default number of transmissions per link is the default number of 
	 * faults tolerated (0) plus 1. 
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getFlowTxAttemptsPerLink(java.lang.String)}.
	 */
	@Test
	void testGetFlowTxAttemptsPerLinkDefault() {
		/* default flow should return value of 1 */
		workLoad.addFlow("Flow1");
		assertEquals(1, workLoad.getFlowTxAttemptsPerLink("Flow1"));
	}
	
	/**
	 * Test getFlowTxAttempsPerLinkNonExisting with a non-existent flow
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getFlowTxAttemptsPerLink(java.lang.String)}.
	 */
	@Test
	void testGetFlowTxAttemptsPerLinkNonExisting() {
		assertEquals(1, workLoad.getFlowTxAttemptsPerLink("Flow1"));
	}

	/**
	 * Test for 
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#setFlowsInRMorder()}.
	 */
	@Test
	void testSetFlowsInRMorder() {
		workLoad.addFlow("Flow1");
		workLoad.setFlowPeriod("Flow1", 1);
		workLoad.setFlowPriority("Flow1", 1);
		workLoad.addFlow("Flow2");
		workLoad.setFlowPeriod("Flow2", 2);
		workLoad.setFlowPriority("Flow2", 2);
		workLoad.addFlow("Flow3");
		workLoad.setFlowPeriod("Flow3", 3);
		workLoad.setFlowPriority("Flow2", 3);
		workLoad.setFlowsInRMorder();
		fail(workLoad.getFlowNamesInPriorityOrder().toString());
	}

	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getNodeNamesOrderedAlphabetically()}.
	 */
	@Test
	void testGetNodeNamesOrderedAlphabetically() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getFlowNames()}.
	 */
	@Test
	void testGetFlowNames() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getNodeIndex(java.lang.String)}.
	 */
	@Test
	void testGetNodeIndex() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getNodesInFlow(java.lang.String)}.
	 */
	@Test
	void testGetNodesInFlow() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getHyperPeriod()}.
	 */
	@Test
	void testGetHyperPeriod() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getTotalTxAttemptsInFlow(java.lang.String)}.
	 */
	@Test
	void testGetTotalTxAttemptsInFlow() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#getNumTxAttemptsPerLink(java.lang.String)}.
	 */
	@Test
	void testGetNumTxAttemptsPerLink() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link edu.uiowa.cs.warp.WorkLoad#maxFlowLength()}.
	 */
	@Test
	void testMaxFlowLength() {
		fail("Not yet implemented");
	}

}
