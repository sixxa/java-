import java.text.DecimalFormat;
import java.util.Scanner;

public class GPACalculator {

	
	public static void main(String[] args) {
		// Total points / total credits(classes)
		// points for a class = grade value * credits(classes)
		// A = 4, B = 3....
		
		
	    Scanner scanner = new Scanner(System.in);
	    
	    Integer totalPoints = 0;
	    Integer totalCredits = 0;
	    
	    boolean addClasses = false;
	    
	    do {
		    
		    Integer credits = 0;
		    boolean validCredits = true;
		    do {
		    	validCredits = true;
		    	
			    System.out.println("Enter number of credits<it should be  number>:");
			    String creditsString = scanner.nextLine();  
			    
			    try {
			    	credits = Integer.parseInt(creditsString);
			    }
			    catch (Exception e){
				    System.out.println("Please enter a valid integer");
				    validCredits = false;
			    }
		    }
		    while (validCredits == false);
		    
		    
		    
		    boolean validGrade = true;
	
		    Integer gradeValue = 0;
		    String grade = "";
		    do {
		    	validGrade = true;
		    	
			    System.out.println("Enter grade<A,B,C,D,E,F>:");
			    grade = scanner.nextLine();  
			    
			    if (grade.equalsIgnoreCase("A")) {
			    	gradeValue = 4;
			    } else if (grade.equalsIgnoreCase("B")) {
			    	gradeValue = 3;
			    } else if (grade.equalsIgnoreCase("C")) {
			    	gradeValue = 2;
			    } else if (grade.equalsIgnoreCase("D")) {
			    	gradeValue = 1;
				} else if (grade.equalsIgnoreCase("E")) {
                    gradeValue = 0;
			    } else if (grade.equalsIgnoreCase("F")) {
			    	gradeValue = 0;
			    } else {
				    System.out.println("non valid grade ");
				    validGrade = false;
			    }
		    }
		    while (validGrade == false);
		    
		    
		    Integer points = gradeValue * credits;
		    
		    totalPoints += points;
		    totalCredits += credits;
		    
		    System.out.println("Would you like to enter another class? (Y/N)");
		    String addClassesString = scanner.nextLine();  
		    
		    addClasses = addClassesString.equalsIgnoreCase("Y");
		    
	    }
	    while (addClasses == true);
	    
	    
	    DecimalFormat round = new DecimalFormat("0.00");

	    Double gpa = Double.valueOf(totalPoints) / Double.valueOf(totalCredits);
	    
	    
	    System.out.println("Credits: " + totalCredits);  
	    System.out.println("Points: " + totalPoints);  
	    System.out.println("GPA: " + round.format(gpa));  
	    
	    
	    scanner.close();
	}

}

