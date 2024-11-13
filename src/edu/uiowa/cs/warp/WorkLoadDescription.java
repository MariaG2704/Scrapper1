package edu.uiowa.cs.warp;


import java.util.*;
import java.io.File;
import java.util.Scanner;


/**

* Reads the input file, whose name is passed as input parameter to the constructor, and builds a
 * Description object based on the contents. Each line of the file is an entry (string) in the
 * Description object.
 * 
 * @author sgoddard
 * @version 1.8 Fall 2024
 * Modified by Maria Gauna on 9/16/24
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
  
  /* Gets the name of a file without the .txt on the end
   * @param WorkLoadDescription which is an object that points to file of flows
   */
  public static String getName(WorkLoadDescription file) {
	  String name= file.getInputFileName();
   	  return name.substring(0,name.length()-4);
     }
  
  /* Adds all the flows from a text file into a ArrayList 
   * @param WorkLoadDescription which is an object that points to file of flows
   */
  public static ArrayList<String> getFlows(WorkLoadDescription fileOfFlows){
	  ArrayList<String> text = new ArrayList<String>();
   	  for(int i=1;i<fileOfFlows.description.size()-1;i++) {
   		  text.add(fileOfFlows.description.get(i));
   	  }
   	  return text;
   	  
     }
  /* Prints out each string of an ArrayList, preceded by a flow count 
   * @param arrayListOfFlows which is an arrayList 
   */
  public static void printArray(ArrayList<String> arrayListOfFlows) {
	  for(int i=0;i<arrayListOfFlows.size();i++) {
   		  System.out.print("Flow "+(i+1)+": " +arrayListOfFlows.get(i));
   	  }
     }
 
  private void initialize(String inputFile) {
    // Get the input graph file name and read its contents
    InputGraphFile gf = new InputGraphFile(fm);
    inputGraphString = gf.readGraphFile(inputFile);
    this.inputFileName = gf.getGraphFileName();
    description = new Description(inputGraphString);
     }
  
  
  /* main method that sorts and prints the flows of text file alphabetically 
   */
  public static void main (String[] args) {
	  WorkLoadDescription workLoadDescrip= new WorkLoadDescription("StressTest.txt"); 
   	  System.out.println(getName(workLoadDescrip));
   	  ArrayList<String> arrayListOfFlows = getFlows(workLoadDescrip);
   	  Collections.sort(arrayListOfFlows);
   	  printArray(arrayListOfFlows);
   	  
     }
	  
}