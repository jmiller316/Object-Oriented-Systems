package misc;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

public class RecordProcessor {
	private static String [] firstName;
	private static String [] lastName;
	private static int [] age;
	private static String [] paymentType;
	private static double [] pay;

	public static String processFile(String inputFile) {
		StringBuffer ouput = new StringBuffer();

		Scanner readFile = null;
		try {
			readFile = new Scanner(new File(inputFile));
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
			return null;
		}

		int numberOfLinesInFile = 0;
		while(readFile.hasNextLine()) {
			String readLine = readFile.nextLine();
			if(readLine.length() > 0)
				numberOfLinesInFile++;
		}

		firstName = new String[numberOfLinesInFile];
		lastName = new String[numberOfLinesInFile];
		age = new int[numberOfLinesInFile];
		paymentType = new String[numberOfLinesInFile];
		pay = new double[numberOfLinesInFile];

		readFile.close();
		try {
			readFile = new Scanner(new File(inputFile));
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
			return null;
		}

		numberOfLinesInFile = 0;
		while(readFile.hasNextLine()) {
			String readLine = readFile.nextLine();
			if(readLine.length() > 0) {

				String [] wordsInLine = readLine.split(",");

				int wordLocationInLine = 0;
				for(;wordLocationInLine < lastName.length; wordLocationInLine++) {
					if(lastName[wordLocationInLine] == null)
						break;

					if(lastName[wordLocationInLine].compareTo(wordsInLine[1]) > 0) {
						for(int i = numberOfLinesInFile; i > wordLocationInLine; i--) {
							firstName[i] = firstName[i - 1];
							lastName[i] = lastName[i - 1];
							age[i] = age[i - 1];
							paymentType[i] = paymentType[i - 1];
							pay[i] = pay[i - 1];
						}
						break;
					}
				}

				firstName[wordLocationInLine] = wordsInLine[0];
				lastName[wordLocationInLine] = wordsInLine[1];
				paymentType[wordLocationInLine] = wordsInLine[3];

				try {
					age[wordLocationInLine] = Integer.parseInt(wordsInLine[2]);
					pay[wordLocationInLine] = Double.parseDouble(wordsInLine[4]);
				} catch(Exception e) {
					System.err.println(e.getMessage());
					readFile.close();
					return null;
				}

				numberOfLinesInFile++;
			}
		}

		if(numberOfLinesInFile == 0) {
			System.err.println("No records found in data file");
			readFile.close();
			return null;
		}

		//print the rows
		ouput.append(String.format("# of people imported: %d\n", firstName.length));

		ouput.append(String.format("\n%-30s %s  %-12s %12s\n", "Person Name", "Age", "Emp. Type", "Pay"));
		for(int i = 0; i < 30; i++)
			ouput.append(String.format("-"));
		ouput.append(String.format(" ---  "));
		for(int i = 0; i < 12; i++)
			ouput.append(String.format("-"));
		ouput.append(String.format(" "));
		for(int i = 0; i < 12; i++)
			ouput.append(String.format("-"));
		ouput.append(String.format("\n"));

		for(int i = 0; i < firstName.length; i++) {
			ouput.append(String.format("%-30s %-3d  %-12s $%12.2f\n", firstName[i] + " " + lastName[i], age[i]
				, paymentType[i], pay[i]));
		}

		int sumOfAllAges = 0;
		float avegareAge = 0f;
		int wordLocationInLine = 0;
		double sumOfAllCommissions = 0;
		double averageComission = 0;
		int employeesWithHourlyWage = 0;
		double sumOfAllHourlyWage = 0;
		double averageHourlyWage = 0;
		int employeesWithSalary = 0;
		double sumOfAllSalary = 0;
		double averageSalary = 0;
		for(int i = 0; i < firstName.length; i++) {
			sumOfAllAges += age[i];
			if(paymentType[i].equals("Commission")) {
				sumOfAllCommissions += pay[i];
				wordLocationInLine++;
			} else if(paymentType[i].equals("Hourly")) {
				sumOfAllHourlyWage += pay[i];
				employeesWithHourlyWage++;
			} else if(paymentType[i].equals("Salary")) {
				sumOfAllSalary += pay[i];
				employeesWithSalary++;
			}
		}
		avegareAge = (float) sumOfAllAges / firstName.length;
		ouput.append(String.format("\nAverage age:         %12.1f\n", avegareAge));
		averageComission = sumOfAllCommissions / wordLocationInLine;
		ouput.append(String.format("Average commission:  $%12.2f\n", averageComission));
		averageHourlyWage = sumOfAllHourlyWage / employeesWithHourlyWage;
		ouput.append(String.format("Average hourly wage: $%12.2f\n", averageHourlyWage));
		averageSalary = sumOfAllSalary / employeesWithSalary;
		ouput.append(String.format("Average salary:      $%12.2f\n", averageSalary));

		HashMap<String, Integer> hashMapOfMultipleFirstNames = new HashMap<String, Integer>();
		int sameFirstName = 0;
		for(int i = 0; i < firstName.length; i++) {
			if(hashMapOfMultipleFirstNames.containsKey(firstName[i])) {
				hashMapOfMultipleFirstNames.put(firstName[i], hashMapOfMultipleFirstNames.get(firstName[i]) + 1);
				sameFirstName++;
			} else {
				hashMapOfMultipleFirstNames.put(firstName[i], 1);
			}
		}

		ouput.append(String.format("\nFirst names with more than one person sharing it:\n"));
		if(sameFirstName > 0) {
			Set<String> set = hashMapOfMultipleFirstNames.keySet();
			for(String multipleFirstName : set) {
				if(hashMapOfMultipleFirstNames.get(multipleFirstName) > 1) {
					ouput.append(String.format("%s, # people with this name: %d\n", multipleFirstName, hashMapOfMultipleFirstNames.get(multipleFirstName)));
				}
			}
		} else {
			ouput.append(String.format("All first names are unique"));
		}

		HashMap<String, Integer> hashMapOfMultipleLastNames = new HashMap<String, Integer>();
		int sameLastName = 0;
		for(int i = 0; i < lastName.length; i++) {
			if(hashMapOfMultipleLastNames.containsKey(lastName[i])) {
				hashMapOfMultipleLastNames.put(lastName[i], hashMapOfMultipleLastNames.get(lastName[i]) + 1);
				sameLastName++;
			} else {
				hashMapOfMultipleLastNames.put(lastName[i], 1);
			}
		}

		ouput.append(String.format("\nLast names with more than one person sharing it:\n"));
		if(sameLastName > 0) {
			Set<String> set = hashMapOfMultipleLastNames.keySet();
			for(String multipleLastName : set) {
				if(hashMapOfMultipleLastNames.get(multipleLastName) > 1) {
					ouput.append(String.format("%s, # people with this name: %d\n", multipleLastName, hashMapOfMultipleLastNames.get(multipleLastName)));
				}
			}
		} else {
			ouput.append(String.format("All last names are unique"));
		}

		//close the file
		readFile.close();

		return ouput.toString();
	}

}
