package assignment2;

import java.io.IOException;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

public class Assignment2 {
	final static int ROWS = 1000;
	final static int COLS = 8;
	//read file
	public static String[] readFile(String fileIn) {
		String[] data = new String[ROWS];
		Path file = Paths.get(fileIn);
		if (!file.toFile().exists()) {
			System.err.println(fileIn + "does not exist");
			return null;
		}
		try (BufferedReader reader = Files.newBufferedReader(file)) {
			for (int i = 0; i < ROWS; i++) {
				data[i] = reader.readLine();
			}
		}catch (IOException err) {
			System.err.println(err.getMessage());
		} catch (NumberFormatException err) {
			System.err.println(err.getMessage());			
		}
	return data;
	}
	//convert 1d array to 2d array
	public static String[][] to2D(String[] array1d){
		String[][] array2d = new String[ROWS][COLS];
			for(int i=0;i<ROWS;i++) {
				array2d[i] = array1d[i].split(",");
			}
			
		return array2d;
	}
//	convert 2d string array into double
	public static double[][] toDouble(String[][] string2d){
		double[][] array2d = new double[ROWS][COLS];
		for(int i=0;i<ROWS;i++) {
			for(int j=0;j<COLS;j++) {
				array2d[i][j] = Double.parseDouble(string2d[i][j]);//converts each element to double
			}
		}
		return array2d;
	}
	
	public static String formatData(double[][] array2d, int motor) {
		String result = "";
		int start=0;
		int finish;
		double current;
		double average=0;
		for(int i=0;i<ROWS;++i) {
			current = array2d[i][motor];//here i is time in seconds
			if (current>1) {
				if (start==0) {
					start = i;
				}
			average += current;//increment average
			} else if (start >0) {
				finish = i-1;
				average /= finish-start+1;
				average = Math.round(1000 * average) / 1000.0; //cut to 2 dec places
				result += start + ", " + finish + ", " + average;
				if (average>8) {
					result += "***current exceeded***";
				}
				result += "\n";
				start = 0;
				average = 0;//reset values
			}	//end if
		}//end for loop
		if(result=="") {
			result+="motor not used \n";
		}
	return result;
	}
	
	public static void saveReport(String report, int motorNum) {
		String title = "motor" + motorNum + ".csv";
		Path file = Paths.get(title);
		try (BufferedWriter writer = Files.newBufferedWriter(file)) {
			writer.write(report); 
		} catch (IOException err) {
			System.err.println(err.getMessage());
		}
	}
	
	public static void main(String[] args){
		String[] dataRows = readFile("logger.csv");
		String[][] String2d = to2D(dataRows);
		double[][] array2d = toDouble(String2d);
		String report;
		for(int motorNum=1;motorNum<8;motorNum++) {
			report = "Start (s), End (s), Current (A) \n";
			report+= formatData(array2d, motorNum);
			saveReport(report, motorNum);
		}
		System.out.println("Motor reports created");
	}
}