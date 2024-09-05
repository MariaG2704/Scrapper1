package edu.uiowa.cs.warp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
/**
 * Reads the input file, whose name is passed as input parameter to the constructor, and builds a
 * Description object based on the contents. Each line of the file is an entry (string) in the
 * Description object.

 * @author sgoddard
 * @version 1.8 Fall 2024
 */
public class WorkLoadDescription extends VisualizationObject {

  private static final String EMPTY = "";
  private static final String INPUT_FILE_SUFFIX = ".wld";

  private Description description;
  private String inputGraphString;
  private FileManager fm;
  private String inputFileName;

  WorkLoadDescription(String inputFileName) {
    super(new FileManager(), EMPTY, INPUT_FILE_SUFFIX); // VisualizationObject constructor
    this.fm = this.getFileManager();
    initialize(inputFileName);
  }

  @Override
  public Description visualization() {
    return description;
  }

  @Override
  public Description fileVisualization() {
    return description;
  }

  @Override
  public String toString() {
    return inputGraphString;
  }

  public String getInputFileName() {
    return inputFileName;
  }

  private void initialize(String inputFile) {
    // Get the input graph file name and read its contents
    InputGraphFile gf = new InputGraphFile(fm);
    inputGraphString = gf.readGraphFile(inputFile);
    this.inputFileName = gf.getGraphFileName();
    description = new Description(inputGraphString);
  }
  /**
   * Reads a graph txt file and outputs flows in alphabetical order.
   * Leverages existing file manager structure

   * @param args no intended input
   */
  public static void main(String[] args) {
    // Open and read file using FM and Scanner
    InputGraphFile graphFile = new InputGraphFile(new FileManager());
    String currObj = graphFile.readGraphFile("StressTest.txt");
    Scanner reader = new Scanner(currObj);
    ArrayList<String> flowArr = new ArrayList<String>();
    // Get filename from first line, remove {
    String data = reader.nextLine();
    System.out.println(data.substring(0, data.length() - 2));
    // Gather all flows into flowArr
    while (reader.hasNextLine()) {
      data = reader.nextLine();
      // End of flow data
      if (data.equals("}")) {
        continue;
      }
      flowArr.add(data);
    }
    reader.close();
    // Sort array alphabetically, only considering the names and not the data portion
    String[] sortedArray = flowArr.toArray(new String[0]);
    Arrays.sort(sortedArray, (s1, s2) -> {
      String prefix1 = s1.split(" ")[0];
      String prefix2 = s2.split(" ")[0];
      return prefix1.compareTo(prefix2);
    });
    // Print all flows in formatted way
    int count = 1;
    for (String flow : sortedArray) {
      System.out.print("Flow " + count + ": ");
      System.out.println(flow);
      count++;
    }
  }
}
