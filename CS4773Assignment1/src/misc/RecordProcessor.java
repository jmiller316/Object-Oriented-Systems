package misc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecordProcessor {
	private static int sumOfAllAges = 0;
	private static int employeesWithCommissions = 0;
	private static int employeesWithSalary = 0;
	private static int employeesWithHourlyWage = 0;
	
	private static double sumOfAllCommissions = 0.0;
	private static double sumOfAllSalary = 0.0;
	private static double sumOfAllHourlyWage = 0.0;
	private static double averageCommission = 0.0;
	private static double averageSalary = 0.0;
	private static double averageHourlyWage = 0.0;
	private static double averageAge = 0.0;

	private static ArrayList<Employee> allEmployees = new ArrayList<Employee>();	
	private static final int NUMBER_OF_COLUMNS = 5;
	
	
	/**
	 * Takes the file and checks to see if the file is empty if not it stores the information and 
	 * formated function to correctly format
	 * @param inputFile
	 * @return a formatted string based on the input file 
	 */
	public static String processFile(String inputFile) {	
		StringBuffer output = new StringBuffer();
		ArrayList<String> inputFileLines = new ArrayList<String>();  
		int numberOfLinesInFile = 0;
		getAllLinesInFile(inputFile, inputFileLines);	
		numberOfLinesInFile = inputFileLines.size();
		
		if(numberOfLinesInFile == 0) {
			return null;
		}	
		if(!getDataFromFile(numberOfLinesInFile, inputFileLines))
			return null;
		formatEmployeeData(numberOfLinesInFile, output);	
		return output.toString();	
	}
	
	/**
	 * stores each line of the file
	 * @param inputFile
	 * @param inputFileLines
	 */
	public static void getAllLinesInFile(String inputFile, ArrayList<String> inputFileLines) {
		BufferedReader readFile = null;
		String currentLine;	
		try {
			readFile = new BufferedReader(new FileReader(inputFile));
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}	
		try { 
			while ((currentLine = readFile.readLine()) != null) {
				if(!currentLine.isEmpty())
					inputFileLines.add(currentLine);
			} 
		} catch (IOException e) {
				e.printStackTrace();
		}		
	}

	/**
	 * gets the data from each line
	 * @param numberOfLinesInFile
	 * @param inputFileLines
	 */
	public static boolean getDataFromFile(int numberOfLinesInFile, ArrayList<String> inputFileLines) {		
		String currentLine = null;
		String [] wordsInLine = null;
		
		for(int i = 0; i < numberOfLinesInFile; i++) {
			currentLine = inputFileLines.get(i);
			wordsInLine = currentLine.split(",");
			if(wordsInLine.length != NUMBER_OF_COLUMNS) {
				throw (new RuntimeException("Wrong number of columns"));
			}
				
			if(!setIndividualEmployeeData(wordsInLine, i))
				return false;
		}	
		return true;
	}
	
	/**
	 * Used to set all the information of each file to the information type
	 * @param wordsInLine
	 * @param lineNumber
	 */
	public static boolean setIndividualEmployeeData(String [] wordsInLine, int lineNumber) {	
		Employee employee = new Employee();
		employee.setFirstName(wordsInLine[0]);
		employee.setLastName(wordsInLine[1]);
		employee.setPaymentType(wordsInLine[3]);
		try {
			employee.setAge(Integer.parseInt(wordsInLine[2]));
			employee.setPay(Double.parseDouble(wordsInLine[4]));
		} catch(Exception e) {
			return false;
		}
		allEmployees.add(employee);
		addToEmployeeCalculations(employee);
		return true;
	}
	
	/**
	 * Adds the correct employee payment type to each employee
	 * @param employee
	 */
	public static void addToEmployeeCalculations(Employee employee) {	
		sumOfAllAges += employee.getAge();
		switch(employee.getPaymentType()) {
			case "Commission":
				sumOfAllCommissions += employee.getPay();
				employeesWithCommissions++;
				break;
			case "Salary":
				sumOfAllSalary += employee.getPay();
				employeesWithSalary++;
				break;
			case "Hourly":
				sumOfAllHourlyWage += employee.getPay();
				employeesWithHourlyWage++;
				break;
			default: 
				throw (new RuntimeException("Error, not a valid payment type"));
		}
	}
	
	/**
	 * Sorts employees by proper format
	 * @param numberOfLinesInFile
	 * @param output
	 */
	public static void formatEmployeeData(int numberOfLinesInFile, StringBuffer output) {
		List<Employee> sortedEmployees = new ArrayList<Employee>(allEmployees);
		Collections.sort(sortedEmployees, new Employee.LastNameComparator());
		output.append(String.format("# of people imported: %d\n", numberOfLinesInFile));
		output.append(String.format("\n%-30s %s  %-12s %12s\n", "Person Name", "Age", "Emp. Type", "Pay" ));
		output.append(String.format("------------------------------ ---  ------------ ------------\n"));
	
		for(int i = 0; i < numberOfLinesInFile; i++) {
			Employee currentEmployee = sortedEmployees.get(i);
			output.append(String.format("%-30s %-3d  %-12s $%12.2f\n", currentEmployee.getFirstName() + " " + currentEmployee.getLastName()
				,currentEmployee.getAge(), currentEmployee.getPaymentType(), currentEmployee.getPay()));
		}	
		calculateAverages(numberOfLinesInFile, output);
		compareNames(numberOfLinesInFile, output);
	}
	
	/**
	 * Calculates the average of all employees information
	 * @param numberOfLinesInFile
	 * @param output
	 */
	public static void calculateAverages(int numberOfLinesInFile, StringBuffer output) {	
		averageAge = (float) sumOfAllAges / numberOfLinesInFile;
		averageCommission = sumOfAllCommissions / employeesWithCommissions;
		averageHourlyWage = sumOfAllHourlyWage / employeesWithHourlyWage;
		averageSalary = sumOfAllSalary / employeesWithSalary;
		output.append(String.format("\nAverage age:         %12.1f\n", averageAge));
		output.append(String.format("Average commission:  $%12.2f\n", averageCommission));
		output.append(String.format("Average hourly wage: $%12.2f\n", averageHourlyWage));
		output.append(String.format("Average salary:      $%12.2f\n" , averageSalary));	
	}
	
	/**
	 *Sorts names and counts the number of times each name is used
	 * @param numberOfLinesInFile
	 * @param output
	 */
	public static void compareNames(int numberOfLinesInFile, StringBuffer output) {
		List<String> firstNames = new ArrayList<String>();
		List<String> lastNames = new ArrayList<String>();
		int numberOfSameFirstNames = 0;
		int numberOfSameLastNames = 0;
		for(int i = 0; i < numberOfLinesInFile; i++) {
			firstNames.add(allEmployees.get(i).getFirstName());
			lastNames.add(allEmployees.get(i).getLastName());
		}
		Set<String> uniqueFirstNames = new HashSet<String>(firstNames);
		Set<String> uniqueLastNames = new HashSet<String>(lastNames);
		output.append(String.format("\nFirst names with more than one person sharing it:\n"));
		
		for(String key: uniqueFirstNames) {
			numberOfSameFirstNames = Collections.frequency(firstNames, key);
			if(numberOfSameFirstNames > 1)
				output.append(String.format("%s, # people with this name: %d\n", key, numberOfSameFirstNames));
		}
		output.append(String.format("\nLast names with more than one person sharing it:"));
		
		for(String key: uniqueLastNames) {
			numberOfSameLastNames = Collections.frequency(lastNames, key);
			if(numberOfSameLastNames > 1)
				output.append(String.format("\n%s, # people with this name: %d\n", key, numberOfSameLastNames));
		}
	}
}