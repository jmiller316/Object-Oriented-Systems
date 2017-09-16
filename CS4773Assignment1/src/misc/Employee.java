package misc;

import java.util.Comparator;

public class Employee {

	private String firstName;
	private String lastName;
	private int age;
	private String paymentType;
	private double pay;
	
	public Employee() {
		
	}
	
	/**
	 * 
	 * @author Zachary Stowers
	 *
	 */
	public static class LastNameComparator implements Comparator<Employee> {

		@Override
		public int compare(Employee employee1, Employee employee2) {
			return String.valueOf(employee1.getLastName()).compareTo(String.valueOf(employee2.getLastName()));
			
		}
		
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public double getPay() {
		return pay;
	}

	public void setPay(double pay) {
		this.pay = pay;
	}
	
	
	
	
	

}
