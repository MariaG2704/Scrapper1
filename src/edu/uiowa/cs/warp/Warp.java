/**
 * WARP: On-the-fly Program Synthesis for Agile, Real-time, and Reliable Wireless Networks. This
 * system generates node communication programs WARP uses programs to specify a network’s behavior
 * and includes a synthesis procedure to automatically generate such programs from a high-level
 * specification of the system’s workload and topology. WARP has three unique features: <br>
 * (1) WARP uses a domain-specific language to specify stateful programs that include conditional
 * statements to control when a flow’s packets are transmitted. The execution paths of programs
 * depend on the pattern of packet losses observed at run-time, thereby enabling WARP to readily
 * adapt to packet losses due to short-term variations in link quality. <br>
 * (2) Our synthesis technique uses heuristics to improve network performance by considering
 * multiple packet loss patterns and associated execution paths when determining the transmissions
 * performed by nodes. Furthermore, the generated programs ensure that the likelihood of a flow
 * delivering its packets by its deadline exceeds a user-specified threshold. <br>
 * (3) WARP can adapt to workload and topology changes without explicitly reconstructing a network’s
 * program based on the observation that nodes can independently synthesize the same program when
 * they share the same workload and topology information. Simulations show that WARP improves
 * network throughput for data collection, dissemination, and mixed workloads on two realistic
 * topologies. Testbed experiments show that WARP reduces the time to add new flows by 5 times over
 * a state-of-the-art centralized control plane and guarantees the real-time and reliability of all
 * flows.
 */

package edu.uiowa.cs.warp;

import argparser.ArgParser;
import argparser.BooleanHolder;
import argparser.DoubleHolder;
import argparser.IntHolder;
import argparser.StringHolder;
import edu.uiowa.cs.warp.SystemAttributes.ScheduleChoices;
import edu.uiowa.cs.warp.Visualization.SystemChoices;
import edu.uiowa.cs.warp.Visualization.WorkLoadChoices;

/**

* Warp class that is implemented to manage the behavior of a network <br>
 * There is no constructor so no Warp objects need to be instantiated <br>
 * All variables represent default conditions for Warp object creation <br>
 * All methods either implement or qualify the behavior of a warp <br>
 * 
 * Revision History: <br>
 * 1.8 Fall 2024 <br>
 * 1.8.1 - Maria Gauna - added Java Doc comments 09/21/24 <br>
 * 
 * @author sgoddard <br>
 * @version 1.8 Fall 2024 <br>
 *  *
 */
 
 //added comment to test pushing - Nancy
public class Warp {
  /** 
   * The NUM_CHANNELS variables specifies the default number of wireless channels available for scheduling and is 
   * and has an option for the command line <br>
   */
  private static final Integer NUM_CHANNELS = 16; 
  /** 
   * The MIN_LQ variable specifies the default minimum Link Quality in system and
   * has an option for command line option <br>
   */
  private static final Double MIN_LQ = 0.9; 
  /** 
   * E2E variable specifies a default end-to-end reliability for all flows and 
   * has an option for the command line <br>
   */
  private static final Double E2E = 0.99; 
  /** 
   * The variable DEFEAULT_OUTPUT_SUB_DIRECTORY specifies a default output sub directory <br>
   */
  private static final String DEFAULT_OUTPUT_SUB_DIRECTORY = "OutputFiles/";
  /** 
   * The variable DEPEAULT_SCHEDULER specifies a default scheduler <br>
   */
  private static final ScheduleChoices DEFAULT_SCHEDULER = ScheduleChoices.PRIORITY;
  /** 
   * The variable DEFAULT_FAULTS_TOLERATED specifies a default number of faults to be tolerated per transmission 
   * and has a option for the command line  <br>
   */
  private static final Integer DEFAULT_FAULTS_TOLERATED = 0;
  /** 
   * The variable nChannels is the number of wireless channels available for scheduling <br>
   */
  private static Integer nChannels;
  /** 
   * The variable numFaults is the number of faults tolerated per edge <br>
   */
  private static Integer numFaults; 
  /** 
   * The variable minLQ is the global variable for minimum Link Quality in system, 
   * later we can add local minLQ for each link <br>
   */
  private static Double minLQ; 
  /** 
   * The variable e2e is a global variable for minimum Link Quality in system, 
   * later we can add local minLQ for each link <br>
   */
  private static Double e2e; 
  /** 
   * The variable outputSubDirectory is the default output sub-directory (from working directory) where output files will be placed 
   * (e.g., gv, wf, ra) <br>
   */
  private static String outputSubDirectory; 
  /** 
   * The variable guiRequested tell is the GUI Visualization has been selected <br>
   */
  private static Boolean guiRequested;
  /** 
   * The variable gvRequsted shows a GraphVis file requested flag <br>
   */
  private static Boolean gvRequested; 
  /** 
   * The variable wfRequested shows s WARP file requested flag <br> 
   */
  private static Boolean wfRequested; 
  /** 
   * The variable raRequsted shows a Reliability Analysis file requested flag <br>
   */
  private static Boolean raRequested; 
  /** 
   * The variable laRequsted shows a Latency Analysis file requested flag <br>
   */
  private static Boolean laRequested; 
  /** 
   * The variable caRequsted shows a Channel Analysis file requested flag <br>
   */
  private static Boolean caRequested; 
  /** 
   * The variable simRequsted shows a Simulation file requested flag <br>
   */
  private static Boolean simRequested; 
  /** 
   * The variable allRequsted shows an all out files requested flag <br>
   */
  private static Boolean allRequested; 
  /** 
   * The variable latencyRequsted shows a latency report requested flag <br>
   */
  private static Boolean latencyRequested; 
  /** 
   * The variable schedulerRequsted shows a scheduler request flag <br>
   */
  private static Boolean schedulerRequested = false;
  /** 
   * The variable verboseMode shows a verbose mode flag (mainly for running in IDE) <br>
   */
  private static Boolean verboseMode; 
  /** 
   * The variable inputFile is a inputFile from which the graph workload is read <br>
   */
  private static String inputFile; 
  /** 
   * The object schedulerSeclected show what Scheduler is requested <br>
   */
  private static ScheduleChoices schedulerSelected; 
  
  /**

* Main: <br>
 *    * Calls the setWarpParamters method and calls the printWarpParameters when verboseMode is on. <br>
 *    * Create and visualizes a new workload with a inputFile string, which may be null. <br>
 *    * If the user has request all to be visualized, its iterates through all the workloads and visualizes them. <br>
 *    * If a schedule was chosen, it creates a warp system for it, 
 *    * then checks the performance requirements and visualizes the system's choices. <br>
 *    * Else it creates a system for each scheduler choice, 
 *    * checks the performance requirements of each system and for each system visualizes all the system choices <br>
 *    * If all were not requested, is visualizes warp workload, sources program, and any other requested items. <br>
 *    * It creates a warp interface with the specified workload, channels, and selected schedule <br>
 *    * Finally, it visualizing other aspects of the system based on what the user inputs <br>
 *    *  
 *    * @param args which is an input of an array of arguments from a command line <br>
 *    * @see WorkLoad's method workload(Integer,Double,Double,String) <br>
 *    * @see SystemFactory's create setWarpParameters(WorkLoad,Integer,ScheduleChoices) <br>
 *    * @see Warp's method setWarpParameters(String[] args) <br>
 *    * @see Warp's method printWarpParameters() <br>
 *    * @see Warp's method visualize(WorkLoad, WorkLoadChoices) <br>
 *    * @see Warp's method verifyPerformanceRequirements(Warp) <br>
 *    * 
   */
  public static void main(String[] args) {
    /** parse command-line options and set WARP system parameters */
    setWarpParameters(args);
   
    /** and print out the values if in verbose mode */
    if (verboseMode) {
      printWarpParameters();
    }
   
    /** Create and visualize the workload, inputFile string, which may be null. */
    WorkLoad workLoad = new WorkLoad(numFaults, minLQ, e2e, inputFile);
    
    if (allRequested) {
      for (WorkLoadChoices choice : WorkLoadChoices.values()) {
        visualize(workLoad, choice); // visualize all Program choices
      }
      // Create and visualize the Warp System
      if (schedulerRequested) {
        WarpInterface warp = SystemFactory.create(workLoad, nChannels, schedulerSelected);
        verifyPerformanceRequirements(warp);
        for (SystemChoices choice : SystemChoices.values()) {
          visualize(warp, choice); // visualize all System choices
        }
      } else { // create a system for all scheduler choices
        for (ScheduleChoices sch : ScheduleChoices.values()) {
          schedulerSelected = sch;
          WarpInterface warp = SystemFactory.create(workLoad, nChannels, schedulerSelected);
          verifyPerformanceRequirements(warp);
          for (SystemChoices choice : SystemChoices.values()) {
            visualize(warp, choice); // visualize all System choices
          }
        }
      }
    } else { // visualize warp workload, source program and other requested items
      visualize(workLoad, WorkLoadChoices.INPUT_GRAPH);
      if (wfRequested) {
        visualize(workLoad, WorkLoadChoices.COMUNICATION_GRAPH);
      }
      if (gvRequested) {
        visualize(workLoad, WorkLoadChoices.GRAPHVIZ);
      }
      WarpInterface warp = SystemFactory.create(workLoad, nChannels, schedulerSelected);
      verifyPerformanceRequirements(warp);
      visualize(warp, SystemChoices.SOURCE);
      if (caRequested) {
        visualize(warp, SystemChoices.CHANNEL);
      }
      if (laRequested) {
        visualize(warp, SystemChoices.LATENCY);
      }
      if (latencyRequested || laRequested) {
        visualize(warp, SystemChoices.LATENCY_REPORT);
      }
      if (raRequested) {
        visualize(warp, SystemChoices.RELIABILITIES);
      }
    }
   
     }
  /**

* Creates a visualize for workload based on specific choices sent to command line. <br>
 *    * And prints that visualization to the consule or to a GUI, 
 *    * depending on if either the verboseMode or the GUI is enabled <br>
 *    * Also places the visualization into a file <br>
 *    * @param workLoad used to create a visualization <br>
 *    * @param choice the type of visualization to make <br>
 *    * @see VisualizationFactory's createVisualization(WorkLoad,String,WorkLoadChoices) <br>
   */
  private static void visualize(WorkLoad workLoad, WorkLoadChoices choice) {
    var viz =
        VisualizationFactory.createVisualization(workLoad, outputSubDirectory, choice);
    if (viz != null) {
      if (verboseMode) {
        System.out.println(viz.toString());
      }
      viz.toFile();
      if (guiRequested) {
        viz.toDisplay();
      }
    }
     }
  /**

* Creates a visualization of a warp based on specific choices sent to command line. <br>
 *    * If that visualization exists, send to to a file,
 *    * and if GUI and scheduler equals true, then display the visualization. <br>
 *    * @param warp the warp used to create a visualization <br>
 *    * @param choice the type of visualization to make <br>
   */
  private static void visualize(WarpInterface warp, SystemChoices choice) {
    var viz = VisualizationFactory.createVisualization(warp, outputSubDirectory, choice);
    if (viz != null) {
      viz.toFile();
      if (guiRequested && schedulerRequested) {
        /* Only display window when a specific scheduler has been requested */
        viz.toDisplay();
      }
    }
     }
  /**

* Verifies each of the performance requirements for a warp <br>
 *    * @param warp what is sent to the methods <br>
 *    * @see Warp's method verifyDeadlines(Warp) <br>
 *    * @see Warp's verifyReliabilities(Warp) <br>
 *    * @see Warp's method  verifyNoChannelConflicts(Warp) <br> 
   */
  private static void verifyPerformanceRequirements(WarpInterface warp) {
    verifyDeadlines(warp);
    verifyReliabilities(warp);
    verifyNoChannelConflicts(warp);
     }
  
  /**

* As long as the schedule selected is not RTHART, then this method: <br>
 *    * Verifies if all reliabilities have been met. <br>
 *    * Prints an error if they don't  
 *    * or prints a confirmation of reliability if they are and verboseMode is on. <br>
 *    * @param warp the warp you use to determine if reliabilities have been met
   */
  private static void verifyReliabilities(WarpInterface warp) {
    if (schedulerSelected != ScheduleChoices.RTHART) {
      /* RealTime HART doesn't adhere to reliability targets */
      if (!warp.reliabilitiesMet()) {
        System.err.printf(
            "\n\tERROR: Not all flows meet the end-to-end "
                + "reliability of %s under %s scheduling.\n",
            String.valueOf(e2e), schedulerSelected.toString());
      } else if (verboseMode) {
        System.out.printf(
            "\n\tAll flows meet the end-to-end reliability " + "of %s under %s scheduling.\n",
            String.valueOf(e2e), schedulerSelected.toString());
      }
    }
     }
  /**

* Checks if deadlines of a warp have been met. <br>
 *    * If they haven't been met, an error is printed,
 *    * and a visualization of the deadline report is prompted <br>
 *    * Else if verboseMode is on, a deadline confirmation is printed to console. <br>
 *    * @param warp an interface that gives access to methods for managing warp operations in the visualization. <br>
 *    * @see Warp's method visualize(WorkLoad, WorkLoadChoices) <br>
   */
  private static void verifyDeadlines(WarpInterface warp) {
    if (!warp.deadlinesMet()) {
      System.err.printf("\n\tERROR: Not all flows meet their deadlines under %s scheduling.\n",
          schedulerSelected.toString());
      visualize(warp, SystemChoices.DEADLINE_REPORT);
    } else if (verboseMode) {
      System.out.printf("\n\tAll flows meet their deadlines under %s scheduling.\n",
          schedulerSelected.toString());
    }
     }
  /**

* Checks if there is no channel conflicts for a warp. <br>
 *    * If there is a channel conflict then an error is printed, 
 *    * and if a visualization is not requested yet then prompt a visualization of the channel. <br>
 *    * Else if verbose mode is on, an no channel conflicts confirmation is printed to console. <br>
 *    * @param warp an interface that gives access to methods for managing warp operations in the visualization. <br>
   */
  private static void verifyNoChannelConflicts(WarpInterface warp) {
    if (warp.toChannelAnalysis().isChannelConflict()) {
      System.err
          .printf("\n\tERROR: Channel conficts exists. See Channel Visualization for details.\n");
      if (!caRequested) { // only need to create the visualization if not already requested
        visualize(warp, SystemChoices.CHANNEL);
      }
    } else if (verboseMode) {
      System.out.printf("\n\tNo channel conflicts detected.\n");
    }
     }
  /** 

* Creates a parser, that parses the command line arguments and adds certain parameters for the system depending on the input. <br>
 *    * The Holders store parsed variables from the command line arguments. <br>
 *    * The parser's addOption sets valid command line options, types, and what holder will hold them. <br>
 *    * Sets Warp system configuration options: <br>
 *    * If a global variable defined doesn't figure certain requirements,then set it equal to a default. <br>
 *    * Sets requested flag variables equal to holders <br>
 *    * If a scheduler value was defined, scheduler choices equal to that value. <br>
 *    * Else set the scheduler choices equal to a default schedule. <br>
 *    * 
 *    * @param args which is an input of an array of arguments from a command line <br>
   */
  private static void setWarpParameters(String[] args) { // move command line parsing into this
                                                         // function--need to set up globals?
   
    // create holder objects for storing results ...
    // BooleanHolder debug = new BooleanHolder();
    StringHolder schedulerSelected = new StringHolder();
    IntHolder channels = new IntHolder();
    IntHolder faults = new IntHolder();
    DoubleHolder m = new DoubleHolder();
    DoubleHolder end2end = new DoubleHolder();
    BooleanHolder gui = new BooleanHolder();
    BooleanHolder gv = new BooleanHolder();
    BooleanHolder wf = new BooleanHolder();
    BooleanHolder ra = new BooleanHolder();
    BooleanHolder la = new BooleanHolder();
    BooleanHolder ca = new BooleanHolder();
    BooleanHolder s = new BooleanHolder();
    BooleanHolder all = new BooleanHolder();
    BooleanHolder latency = new BooleanHolder();
    BooleanHolder verbose = new BooleanHolder();
    StringHolder input = new StringHolder();
    StringHolder output = new StringHolder();
   
    // create the parser and specify the allowed options ...
    ArgParser parser = new ArgParser("java -jar warp.jar");
    parser.addOption("-sch, --schedule %s {priority,rm,dm,rtHart,poset} #scheduler options",
        schedulerSelected);
    parser.addOption("-c, --channels %d {[1,16]} #number of wireless channels", channels);
    parser.addOption("-m %f {[0.5,1.0]} #minimum link quality in the system", m);
    parser.addOption(
        "-e, --e2e %f {[0.5,1.0]} #global end-to-end communcation reliability for all flows",
        end2end);
    parser.addOption("-f, --faults %d {[1,10]} #number of faults per edge in a flow (per period)",
        faults);
    parser.addOption("-gui %v #create a gui visualizations", gui);
    parser.addOption("-gv %v #create a graph visualization (.gv) file for GraphViz", gv);
    parser.addOption(
        "-wf  %v #create a WARP (.wf) file that shows the maximum number of transmissions on each segment of the flow needed to meet the end-to-end reliability",
        wf);
    parser.addOption(
        "-ra  %v #create a reliability analysis file (tab delimited .csv) for the warp program",
        ra);
    parser.addOption(
        "-la  %v #create a latency analysis file (tab delimited .csv) for the warp program", la);
    parser.addOption(
        "-ca  %v #create a channel analysis file (tab delimited .csv) for the warp program", ca);
    parser.addOption("-s  %v #create a simulator input file (.txt) for the warp program", s);
    parser.addOption("-a, --all  %v #create all output files (activates -gv, -wf, -ra, -s)", all);
    parser.addOption("-l, --latency  %v #generates end-to-end latency report file (.txt)", latency);
    parser.addOption("-i, --input %s #<InputFile> of graph flows (workload)", input);
    parser.addOption("-o, --output %s #<OutputDIRECTORY> where output files will be placed",
        output);
    parser.addOption(
        "-v, --verbose %v #Echo input file name and parsed contents. Then for each flow instance: show maximum E2E latency and min/max communication cost for that instance of the flow",
        verbose);
    // parser.addOption ("-d, -debug, --debug %v #Debug mode: base directory =
    // $HOME/Documents/WARP/", debug);
   
   
    // match the arguments ...
    parser.matchAllArgs(args);
   
    // Set WARP system configuration options
    if (channels.value > 0) {
      nChannels = channels.value; // set option specified
    } else {
      nChannels = NUM_CHANNELS; // set to default
    }
    if (faults.value > 0) { // global variable for # of Faults tolerated per edge
      numFaults = faults.value; // set option specified
    } else {
      numFaults = DEFAULT_FAULTS_TOLERATED; // set to default
    }
    if (m.value > 0.0) { // global variable for minimum Link Quality in system
      minLQ = m.value; // set option specified
    } else {
      minLQ = MIN_LQ; // set to default
    }
    if (end2end.value > 0.0) { // global variable for minimum Link Quality in system
      e2e = end2end.value; // set option specified
    } else {
      e2e = E2E; // set to default
    }
    if (output.value != null) { // default output subdirectory (from working directory)
      outputSubDirectory = output.value; // set option specified
    } else {
      outputSubDirectory = DEFAULT_OUTPUT_SUB_DIRECTORY; // set to default
    }
    /** GraphVis file requested flag */
    guiRequested = gui.value; 
    /** GraphVis file requested flag */
    gvRequested = gv.value; 
    /** WARP file requested flag */
    wfRequested = wf.value; 
    /** Reliability Analysis file requested flag */
    raRequested = ra.value; 
    /** Latency Analysis file requested flag */
    laRequested = la.value; 
    /** Latency Analysis file requested flag */
    caRequested = ca.value; 
    /** Simulation file requested flag */
    simRequested = s.value; 
    /** all out files requested flag */
    allRequested = all.value; 
    /** latency report requested flag */
    latencyRequested = latency.value; 
    /** verbose mode flag (mainly for running in IDE) */
    verboseMode = verbose.value; 
    // debugMode = debug.value; // debug mode flag (mainly for running in IDE)
    /** input file specified */
    inputFile = input.value; 
    
    if (schedulerSelected.value != null) { // can't switch on a null value so check then switch
      schedulerRequested = true;
      switch (schedulerSelected.value) {
        case "priority":
          Warp.schedulerSelected = ScheduleChoices.PRIORITY;
          break;
   
        case "rm":
          Warp.schedulerSelected = ScheduleChoices.RM;
          break;
   
        case "dm":
          Warp.schedulerSelected = ScheduleChoices.DM;
          break;
   
        case "rtHart":
          Warp.schedulerSelected = ScheduleChoices.RTHART;
          break;
   
        case "poset":
          Warp.schedulerSelected = ScheduleChoices.POSET_PRIORITY;
          break;
   
        default:
          Warp.schedulerSelected = ScheduleChoices.PRIORITY;
          break;
      }
    } else { // null value when no scheduler specified; so use default
      Warp.schedulerSelected = DEFAULT_SCHEDULER;
    }
     }
  /**

* Prints out each of the warp system configuration parameters on a separate lines <br>
   */
  private static void printWarpParameters() { 
    System.out.println("WARP system configuration values:");
    System.out.println("\tScheduler=" + schedulerSelected);
    System.out.println("\tnChanels=" + nChannels);
    System.out.println("\tnumFaults=" + numFaults);
    System.out.println("\tminLQ=" + minLQ);
    System.out.println("\tE2E=" + e2e);
    System.out.println("\tguiRequest flag=" + guiRequested);
    System.out.println("\tgvRequest flag=" + gvRequested);
    System.out.println("\twfRequest flag=" + wfRequested);
    System.out.println("\traRequest flag=" + raRequested);
    System.out.println("\tlaRequest flag=" + laRequested);
    System.out.println("\tcaRequest flag=" + caRequested);
    System.out.println("\tsimRequest flag=" + simRequested);
    System.out.println("\tallOutFilesRequest flag=" + allRequested);
    System.out.println("\tlatency flag=" + latencyRequested);
    if (inputFile != null) {
      System.out.println("\tinput file=" + inputFile);
    } else {
      System.out.println("\tNo input file specified; will be requested when needed.");
    }
    System.out.println("\toutputSubDirectory=" + outputSubDirectory);
    System.out.println("\tverbose flag=" + verboseMode);
    // System.out.println ("\tdebug flag=" + debugMode);
     }

}
