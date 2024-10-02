# CS2820 Fall 2024 WARP Project Code

This codebase is used for the University of Iowa CS:2820 Introduction to Software Development course section 0AAA of F24. The original author and developer was Steve Goddard of the University of Iowa for the WARP sensor network research project and has undergone multiple iterations, transitioning from Swift to Java and finally into an object-oriented programming style. This specific fork serves as a testing ground for learning software development principles.

## Project Overview

WARP (Wireless Adaptive Real-time Programming) is a novel approach designed to meet the demands of future Industrial Internet of Things (IIoT) applications. Traditional software synthesis relies heavily on SMT solvers and resource-rich machines. WARP adapts these techniques for embedded devices and wireless networks, offering real-time, reliable performance despite network dynamics.

Key Features of WARP:
- **Data Plane**: Programs specify network operations more expressively than traditional schedules.
- **Software Synthesis**: Generates programs that meet real-time flow deadlines and reliability constraints.
- **Control Plane**: Manages workload and network topology changes efficiently.

## Interaction

WARP is interacted with via the command line interface (CLI). The primary command is `warp`, followed by various argparser options that can be supplied to modify behavior. Users can specify the network workload and topology, which consists of real-time flows between sensors, controllers, and actuators.

## Contributions

This project has been modified and extended by **ccolin**, **msgauna**, and **yongycheng** as part of Steve Goddard's Software Development course.
*The above details of WARP's architecture and features are based on the officially released paper by Ryan Brummet, Md Kowsar Hossain, Octav Chipara, Ted Herman, and Steve Goddard, presented at IPSN '21.*
---

## Homework Assignments

### HW1: Introduction to Debugging and Refactoring (Contributed by ccolin)
- **Objective**: Introduce debugging and refactoring techniques by working with a simplified Java program.
- **Tasks**:
  1. Created a `HW1` branch and developed a `main` method within `WorkLoadDescription.java`.
  2. Printed out the contents of a file in a specific, alphabetically sorted, and formatted manner.
  3. Committed, pushed, and merged the `HW1` branch with the `main` branch.

### HW2: JavaDoc Documentation (Contributions by ccolin, msgauna, yongycheng)
- **Objective**: Add comprehensive JavaDoc-compliant documentation for various methods and classes.
- **Contributions**:
  - **ccolin**:
    - Documented the summary comment and all methods for `Flow.java`.
    - Provided JavaDoc comments for 21 methods, 2 constructors, and the class comment for `WorkLoad.java`, including:
      - `addFlow()`
      - `addNodeToFlow()`
      - `getNodeIndex()`
      - `getFlowPriority()`
      - `getFlowDeadline()`
      - `getFixedTxPerLinkAndTotalTxCost()`
      - `getFlowPeriod()`
      - `getFlowPhase()`
      - `getFlowTxAttemptsPerLink()`
      - `setFlowsInPriorityOrder()`
      - `setFlowsInDMorder()`
      - `setFlowsInRMorder()`
      - `getNodesNamesOrderedAlphabetically()`
      - `getFlowNames()`
      - `getNodesInFlow()`
      - `getHyperPeriod()`
      - `numTxAttemptsPerLinkAndTotalTxAttempts()`
      - `getTotalTxAttemptsInFlow()`
      - `getNumTxAttemptsPerLink()`
      - `addEdge()`
      - `maxFlowLength()`
  - **msgauna**:
    - Documented `Warp.java` and `VisualizationImplementation.java`, including all summary comments, attributes, and methods.
  - **yongycheng**:
    - Documented `ProgramVisualization.java` and methods in `Program.java`, including:
      - `getSchedule()`
      - `getNodeMapIndex()`
      - `toWorkLoad()`

### HW3: JUnit 5 Testing (Contributions by ccolin, placeholders for msgauna and yongycheng)
- **Objective**: Develop, comment, and execute JUnit 5 test cases for `WorkLoad.java`.
- **Contributions**:
  - **ccolin**:
    - Created a `BeforeEach` and `AfterEach` method to ensure the workload object is reset for each test case.
    - Test cases for `addFlow()`:
      - `testAddValidFlow()`
      - `testAddFlowWithExistingNameNoSizeChange()`
      - `testAddFlowWithExistingNameCorrectWarning()`
    - Test cases for `addNodeToFlow()`:
      - `testAddNewNodeToFlowNodeNowExists()`
      - `testAddExistingNodeToFlowTwoExistsInFlow()`
      - `testAddExistingNodeToFlowOneExistsInNodeMap()`
    - Test cases for `getFlowPriority()`:
      - `testGetFlowPriorityOfNodeInFlow()`
      - `testGetFlowPriorityOfNonExistingNodeDefaultToZero()`
    - Overloaded variant:
      - `testGetFlowPriorityNewFlowHasLastPriority()`
      - `testGetFlowPriorityNonExistingFlow()`
    - Test cases for `setFlowPriority()`:
      - `testSetFlowPriorityOfExistingFlowToZero()`
      - `testSetFlowPriorityOfExistingFlowToNegative()`
      - `testSetFlowPriorityOfNonExistingFlow()`
    - Test cases for `setFlowDeadline()`:
      - `testSetFlowDeadlineOfExistingFlowToZero()`
      - `testSetFlowDeadlineOfExistingFlowToNegative()`
      - `testSetFlowDeadlineOfNonExistingFlow()`
- **msgauna & yongycheng**:
  - Contributions pending. Placeholder section added for their work.

---

## Project Setup

No special installation or setup is required. The project is designed to run directly in Java, and users can interact with the project via the CLI using the `warp` command.

---

## Acknowledgments

This project was originally developed by **Steve Goddard** for the WARP sensor network research project. It has since been modified by **ccolin**, **msgauna**, and **yongycheng** for educational purposes in the CS2820 Software Development course.
