/**
 * %W% %E% Maria Gauna
 * 
 */
package edu.uiowa.cs.warp;

import java.io.File;

/**
 * Creates a VisualizationImplementation that implements Visualization <br>
 * Has two constructors for either a workload or warp input  <br>
 * Also has a default initalizer for a visualization for any object <br>
 * Has different methods for ways to display the contents of the visualization: file, display, string <br>
 * Has a method that creates a visualization based on a systems choice <br>
 * Has a method that creates a filename template <br>
 * 
 * @author sgoddard <br>
 * @version 1.5 <br>
 * @version 1.5.1 - Maria Gauna- added JavaDoc Comments 09/21/24 <br>
 */
public class VisualizationImplementation implements Visualization {
  /**
  * The variable visualization is a description of the visualization <br>
  */
  private Description visualization;
  /** 
   * The variable fileContent is a description of the file's content <br>
   */
  private Description fileContent;
  /**
   * The variable window that creates a GUI visualization window of the visualization <br>
   */
  private GuiVisualization window;
  /**
   * The variable fileName is the name of the output file where the visualization will be stored <br>
   */
  private String fileName;
  /**
   * the variable inputFileName is the name of the input file where the visualization will be stored <br>
   */
  private String inputFileName;
  /**
   * The variable fileNameTemplete is a template used for constructing the file's name <br>
   */
  private String fileNameTemplate;
  /**
   * The variable fm is the file manager that handles the file manager operations <br>
   */
  private FileManager fm = null;
  /**
   * The interface warp is responsible for storing the warp of the visualization <br>
   */
  private WarpInterface warp = null;
  /**
   * The object  workload is am workload responsible for holding the workload of the visualization <br>
   */
  private WorkLoad workLoad = null;
  /**
   * The object visualizationObject is responsible for holding what visualization is being implemented <br>
   */
  private VisualizationObject visualizationObject;
  
  /**
   * Constructor that creates a VisualizationImplementataion that: <br>
   * Creates a new file manager. <br>
   * Assigns the inputed warp to warp instance variable. <br>
   * Gets the fileName from the warp and assigns it the inputFileName instance variable. <br>
   * Creates a file name template based on the inputed output directory, 
   * and assigns it to the fileNameTemplete instance variable. <br>
   * Sets the visualization object equal to null and  
   * calls method createVisualization with an parameter choice sent to the constructor. <br> 
   * @param warp an interface that gives access to methods for managing warp operations <br>
   * @param outputDirectory a string that provides reference to the output directory <br>
   * @param choice what is sent to the createVizualization method <br>
   * @see VisualizationImplementation method createVisualization(SystemChoices) <br>
   */
  public VisualizationImplementation(WarpInterface warp, String outputDirectory,
      SystemChoices choice) {
    this.fm = new FileManager();
    this.warp = warp;
    inputFileName = warp.toWorkload().getInputFileName();
    this.fileNameTemplate = createFileNameTemplate(outputDirectory);
    visualizationObject = null;
    createVisualization(choice);
  }
  
  /**
   * Constructor that creates a VisualizationImplementation that: <br>
   * Creates a new file manager. <br>
   * Assigns the inputed workload to workload instance variable. <br>
   * Gets the fileName from the workload and assigns it the inputFileName instance variable. <br>
   * Creates a file name template based on the inputed output directory, 
   * and assigns it to the fileNameTemplete instance variable. <br>
   * Sets the visualization object equal to null and 
   * calls method createVisualization with an parameter choice sent to the constructor. <br>
   * @param workLoad an workload that gives access to methods for managing workload operations <br>
   * @param outputDirectory a string that provides reference to the output directory <br>
   * @param choice what is sent to the createVizualization method <br>
   * @see VisualizationImplementation method createVisualization(SystemChoices)  <br>
   */
  public VisualizationImplementation(WorkLoad workLoad, String outputDirectory,
      WorkLoadChoices choice) {
    this.fm = new FileManager();
    this.workLoad = workLoad;
    inputFileName = workLoad.getInputFileName();
    this.fileNameTemplate = createFileNameTemplate(outputDirectory);
    visualizationObject = null;
    createVisualization(choice);
  }
  
  /**
   * Overrides the implemented Visualization toDisplay method. <br>
   * Assigns the instance variable window equal to the display of the visualization object. <br>
   * If the window exist you make it visible. <br>
   * @see visualizationObject's method displayVisualization(); <br>
   */
  @Override
  public void toDisplay() {
    // System.out.println(displayContent.toString());
    window = visualizationObject.displayVisualization();
    if (window != null) {
      window.setVisible();
    }
  }
  
  /**
   * Overrides the implemented Visualization toFile method. <br>
   * Assigns the file manager to write the file name and content to a file. <br>
   */
  @Override
  public void toFile() {
    fm.writeFile(fileName, fileContent.toString());
  }
  
  /**
   * Overrides the implemented Visualization toString method. <br>
   * @return string representation of the visualization. <br>
   */
  @Override
  public String toString() {
    return visualization.toString();
  }
  
  /**
   * Creates a visualization based on a choice parameter. <br>
   * A switch statement evaluates the choice, options for choices include: <br>
   * source, reliability, simulator, latency, channel, latency report, and deadline report <br>
   * The default case creates a placeholder visualization.  <br>
   * @param choice, the type of system visualization to create <br>
   * @see VisualizationImplementation's createVisualization(WorkLoadChoices) <br>
   */
  private void createVisualization(SystemChoices choice) {
    switch (choice) { // select the requested visualization
      case SOURCE:
        createVisualization(new ProgramVisualization(warp));
        break;

      case RELIABILITIES:
        // TODO Implement Reliability Analysis Visualization
        createVisualization(new ReliabilityVisualization(warp));
        break;

      case SIMULATOR_INPUT:
        // TODO Implement Simulator Input Visualization
        createVisualization(new NotImplentedVisualization("SimInputNotImplemented"));
        break;

      case LATENCY:
        // TODO Implement Latency Analysis Visualization
        createVisualization(new LatencyVisualization(warp));
        break;

      case CHANNEL:
        // TODO Implement Channel Analysis Visualization
        createVisualization(new ChannelVisualization(warp));
        break;

      case LATENCY_REPORT:
        createVisualization(new ReportVisualization(fm, warp,
            new LatencyAnalysis(warp).latencyReport(), "Latency"));
        break;

      case DEADLINE_REPORT:
        createVisualization(
            new ReportVisualization(fm, warp, warp.toProgram().deadlineMisses(), "DeadlineMisses"));
        break;

      default:
        createVisualization(new NotImplentedVisualization("UnexpectedChoice"));
        break;
    }
  }
  /**
   * Creates a visualization based on a choice parameter. <br>
   * A switch statement evaluates the choice, options for choices include:  <br>
   * communication graph, graph visualization, or input graph. <br>
   * The default case creates a placeholder visualization.  <br>
   * @param choice, the type of workload visualization to make. <br>
   * @see VisualizationImplementation's createVisualization(WorkLoadChoices) <br>
   */
  private void createVisualization(WorkLoadChoices choice) {
    switch (choice) { // select the requested visualization
      case COMUNICATION_GRAPH:
        // createWarpVisualization();
        createVisualization(new CommunicationGraph(fm, workLoad));
        break;

      case GRAPHVIZ:
        createVisualization(new GraphViz(fm, workLoad.toString()));
        break;

      case INPUT_GRAPH:
        createVisualization(workLoad);
        break;

      default:
        createVisualization(new NotImplentedVisualization("UnexpectedChoice"));
        break;
    }
  }
  
  /**
   * Create a visualization based on an object that extends VisualizationObject that is sent as a parameter. <br>
   * Assigns the instance variable visualization equal to the object's visualization. <br>
   * Assigns the instance variable fileContent equal to the object's visualization file's content. <br>
   * Creates a file for object based on the fileNameTemplete and assigns it to the instance variable fileName. <br>
   * Sets the Visualization object equal to the obj parameter. <br>
   * @param <T>, a placeholder that can be filled by any object type sent as parameter. <br>
   * @param obj, the object that needs to be visualized. <br>
   */
  private <T extends VisualizationObject> void createVisualization(T obj) {
    visualization = obj.visualization();
    fileContent = obj.fileVisualization();
    /* display is file content printed to console */
    fileName = obj.createFile(fileNameTemplate); // in output directory
    visualizationObject = obj;
  }
  
  /**
   * Creates a template for the file name of an outputDirectory parameter. <br>
   * Uses a file manager to make a new directory for the filename based a base directory and output directory. <br>
   * Creates the fileNameTemplate using full output path and input filename. <br>
   * @param outputDirectory the directory where the file will be saved. <br>
   * @return the fileNameTemplete as a string <br>
   * @see FileManager's method getBaseDirectory() <br>
   * @see FileManager's method createDirectory(String,String) <br> 
   */
  private String createFileNameTemplate(String outputDirectory) {
    String fileNameTemplate;
    var workingDirectory = fm.getBaseDirectory();
    var newDirectory = fm.createDirectory(workingDirectory, outputDirectory);
    // Now create the fileNameTemplate using full output path and input filename
    if (inputFileName.contains("/")) {
      var index = inputFileName.lastIndexOf("/") + 1;
      fileNameTemplate = newDirectory + File.separator + inputFileName.substring(index);
    } else {
      fileNameTemplate = newDirectory + File.separator + inputFileName;
    }
    return fileNameTemplate;
  }

}
