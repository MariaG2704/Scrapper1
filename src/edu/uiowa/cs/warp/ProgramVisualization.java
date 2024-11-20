package edu.uiowa.cs.warp;

/**

* Class takes in a WarpInterface object, and includes tools to 
 * visualize the data, such as a table/matrix representation of the schedule, 
 * header, footer, and title generator with relevant information about the program.
 * Also supports the use of a graphical user interface visualization 
 * with the GuiVisualization class. <br>
 * 
 * @version 1.5
 * @author sgoddard
 * @author yongycheng - Develop JavaDoc comments for the missing summary comment 
 * 						  and all methods in in Flow.java
 *  * 
 */
public class ProgramVisualization extends VisualizationObject {

  private static final String SOURCE_SUFFIX = ".dsl"; //file type of warp file
  private ProgramSchedule sourceCode; //source code of program
  private Program program; //the warp program
  private Boolean deadlinesMet; //whether deadlines were met
  
  /**

* Constructor for ProgramVisualization that sets program, sourceCode,
 *    * and deadlinesMet variables from a warp program. <br>
 *    * 
 *    * @param warp	warp program to visualize <br>
   */
  ProgramVisualization(WarpInterface warp) {
    super(new FileManager(), warp, SOURCE_SUFFIX);
    this.program = warp.toProgram();
    this.sourceCode = program.getSchedule();
    this.deadlinesMet = warp.deadlinesMet();
  		}

  /**
   * Function creates and returns a GuiVisualization object. <br>
   */
  @Override
  public GuiVisualization displayVisualization() {
    return new GuiVisualization(createTitle(), createColumnHeader(), createVisualizationData());
     }

  /**
   * Function creates and returns a header description for the warp program that includes the title, 
   * scheduler name, number of faults, minimum packet reception rate, E2E, 
   * and number of channels the schedule has. <br>
   */
  @Override
  protected Description createHeader() {
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

  /**
   * Function creates and returns a footer description for the warp program that indicates
   * whether flow deadlines were met. <br>
   */
  @Override
  protected Description createFooter() {
    Description footer = new Description();
    String deadlineMsg = null;
   
    if (deadlinesMet) {
      deadlineMsg = "All flows meet their deadlines\n";
    } else {
      deadlineMsg = "WARNING: NOT all flows meet their deadlines. See deadline analysis report.\n";
    }
    footer.add(String.format("// %s", deadlineMsg));
    return footer;
     }


  /**
   * Function that gets the names of the nodes ordered alphabetically, then stores and returns the 
   * names of the nodes in an array of Strings, used for column headers of table when visualizing data. <br>
   */
  @Override
  protected String[] createColumnHeader() {
    var orderedNodes = program.toWorkLoad().getNodeNamesOrderedAlphabetically();
    String[] columnNames = new String[orderedNodes.length + 1];
    columnNames[0] = "Time Slot"; // add the Time Slot column header first
    /* loop through the node names, adding each to the header */
    for (int i = 0; i < orderedNodes.length; i++) {
      columnNames[i + 1] = orderedNodes[i];
    }
    return columnNames;
     }

  /**
   * Function creates and returns a 2D array of Strings filled with the schedule data. <br>
   */
  @Override
  protected String[][] createVisualizationData() {
    if (visualizationData == null) {
      int numRows = sourceCode.getNumRows();
      int numColumns = sourceCode.getNumColumns();
      visualizationData = new String[numRows][numColumns + 1];
   
      for (int row = 0; row < numRows; row++) {
        visualizationData[row][0] = String.format("%s", row);
        for (int column = 0; column < numColumns; column++) {
          visualizationData[row][column + 1] = sourceCode.get(row, column);
        }
      }
    }
    return visualizationData;
     }

  /**

* Function creates and returns a String as the name of the warp program visualization. <br>
   */
  private String createTitle() {
    return String.format("WARP program for graph %s\n", program.getName());
     }
}
