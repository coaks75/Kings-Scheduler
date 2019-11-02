import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class Main {

	/**
	 * The main method
	 * 
	 * @param args The parameter
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		Scanner scanner = new Scanner(System.in);
		
		// Here we are setting the pattern of our dtf for the overall day and time
		String totalPattern = "dd MMM uuuu HH:mm:ss";
		DateTimeFormatter total = DateTimeFormatter.ofPattern(totalPattern);
		
		// Here we create two dtf's so that if we are sending an email it can be formatted better
		DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("dd MMM uuuu");  
		DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("HH:mm:ss:SSS");
		
		// Here we create the local date time variables we are going to use throughout the program
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime started;
		LocalDateTime ended;
		
		
		System.out.println("Before we start, make sure your web driver is in the current directory,");
		System.out.println("and it is named \"driver.exe\" if you're on Windows/Linux, and \"driver\" if you're on a Mac.");
		System.out.println("If you have this set up, press enter.");
		scanner.nextLine();
		
		
		// We are using the helper method to get all of our courses
		ArrayList<Course> courses = getCourses(scanner);
		System.out.println("What is your anticipated graduation date? Please enter in the format \"MM/YYYY\"");
		System.out.println("\tExample: 05/2020");
		String gradDate = scanner.nextLine();
		
		System.out.println("When is your registration time? Please enter in the format \"dd MMM uuuu HH:mm:ss\"");
		System.out.println("\tExample: 08 Apr 2019 18:30:00");
		String begin = scanner.nextLine();
		
		System.out.println("What is your webadvisor username?");
		String username = scanner.nextLine();
		System.out.println("What is your webadvisor password?");
		String password = scanner.nextLine();

		// We're calling this method to set up the webdriver for any supported OS and browser
		System.out.println("Lastly, lets set up the web browser");
		WebDriver browser = getWebBrowser(scanner);
		browser.get("https://coaks75.github.io/Test2");
		browser.manage().window().maximize();
		
		while (total.format(now.plusMinutes(5)).compareToIgnoreCase(begin) < 0) {
			now = LocalDateTime.now();
		}
		System.out.println("Opening the browser at " + total.format(now));

		browser.get("https://wa02.kings.edu:8443/webadvisorc/colleague?TOKENIDX=2078749147&type=M&constituency=WBST&pid=CORE-WBST");
		// We are creating this variable to know when each 'set' of commands is done,
		// 		when going to a new new page it is possible that when the program gets to
		//		the next line of code the webpage is not loaded up yet.
		
		boolean cmdDone = false;
		
		while (!cmdDone) {
			try {
				browser.findElement(By.id("acctLogin")).click();
				cmdDone = true;
			}
			catch (Exception e) {
				System.out.println("Navigating to login screen");
			}
		}
		cmdDone = false;
		
		while (!cmdDone) {
			try {
				WebElement user = browser.findElement(By.id("USER_NAME"));
				WebElement pass = browser.findElement(By.id("CURR_PWD"));
				user.sendKeys(Keys.CONTROL + "a");
				user.sendKeys(Keys.DELETE);
				user.sendKeys(username);
				pass.sendKeys(Keys.CONTROL + "a");
				pass.sendKeys(Keys.DELETE);
				pass.sendKeys(password + Keys.ENTER);
				cmdDone = true;
			}
			catch (Exception e) {
				System.out.println("Trying to log in");
			}
		}
		cmdDone = false;
		
		while (!cmdDone) {
			try {
				browser.findElement(By.linkText("Register for Sections")).click();
				cmdDone = true;
			}
			catch (Exception e) {
				System.out.println("Trying to click register");
			}
		}
		cmdDone = false;
		
		while (!cmdDone) {
			try {
				browser.findElement(By.linkText("Search and register for sections")).click();
				cmdDone = true;
			}
			catch (Exception e) {
				System.out.println("Trying to click search and register");
			}
		}
		cmdDone = false;
		
		while (!cmdDone) {
			try {
				Select term = new Select(browser.findElement(By.id("VAR1")));
				term.selectByIndex(1);
				inputClasses(courses, browser);
				cmdDone = true;
			}
			catch (Exception e) {
				System.out.println("Trying to select all classes");
			}
		}
		cmdDone = false;
		
		while(!cmdDone) {
			try {
				WebElement box;
				for (int i = 0; i < courses.size(); i++) {
					// We are using index here because the website starts off at 1, not 0
					int index = i + 1;
					box = browser.findElement(By.id("LIST_VAR1_" + index));
					box.click();
				}
				cmdDone = true;
			}
			catch(Exception e) {
				System.out.println("Trying to check off all classes");
			}
		}
		cmdDone = false;
		
		
		while (total.format(now).compareToIgnoreCase(begin) < 0) {
			now = LocalDateTime.now();
		}
		started = now;
		
		browser.findElement(By.name("SUBMIT2")).click();
		
		while(!cmdDone) {
			try {
				Select register = new Select(browser.findElement(By.id("VAR3")));
				register.selectByValue("RG");
				Select gradDateSelect = new Select(browser.findElement(By.id("VAR30")));
				gradDateSelect.selectByValue(gradDate);
				cmdDone = true;
				browser.findElement(By.name("SUBMIT2")).click();
			}
			catch(Exception e) {
				System.out.println("Trying to register");
			}
		}
		cmdDone = false;
		
		
	}
	
	/**
	 * This is a helper method to get the users web browser and set the driver
	 * 
	 * @param scanner The scanner we are using
	 */
	private static WebDriver getWebBrowser(Scanner scanner) {
		WebDriver browser = null;
		String usersBrowser = "";
		String operatingSystem = "";
		String ext = "";
		boolean browserFound = false;
		boolean OSFound = false;
		
		while (!OSFound) {
			System.out.println("What operating system do you use? The options are:");
			System.out.println("\tWindows, Linux, Mac");
			operatingSystem = scanner.nextLine();
			switch (operatingSystem.toLowerCase()) {
			case "windows":
			case "linux":
				OSFound = true;
				ext = ".exe";
				break;
			case "mac":
				OSFound = true;
				break;
			default :
				System.out.println("Sorry, \"" + operatingSystem + "\" is not a supported operating system, try again.");
				break;
			}
		}
		
		while (!browserFound) {
			System.out.println("What web browser do you use? The supported options are:");
			System.out.println("\tChrome, FireFox, Microsoft Edge");
			usersBrowser = scanner.nextLine();
			switch (usersBrowser.toLowerCase()) {
			case "chrome":
				System.setProperty("webdriver.chrome.driver", "driver" + ext);
				browser = new ChromeDriver();
				browserFound = true;
				break;
			case "firefox":
				System.setProperty("webdriver.gecko.driver", "driver" + ext);
				browser = new FirefoxDriver();
				browserFound = true;
				break;
			case "microsoft edge":
				System.setProperty("webdriver.edge.driver", "driver" + ext);
				browser = new EdgeDriver();
				browserFound = true;
				break;
			default :
				System.out.println("Sorry, \"" + usersBrowser + "\" is not a supprted browser, try again.");
				break;
			}
		}	
		return browser;
	}
	
	/**
	 * This is a helper method to get a list of courses from the user
	 * 
	 * @param scanner The scanner that we are using
	 * @return An arraylist of courses
	 */
	private static ArrayList<Course> getCourses(Scanner scanner) {
		ArrayList<Course> courses = new ArrayList<Course>();
		int numCourses = 0;
		boolean numCoursesFound = false;
		
		while (!numCoursesFound) {
			System.out.println("How many courses are you registering for?");
			numCourses = scanner.nextInt();
			scanner.nextLine();
			if (numCourses > 0) {
				numCoursesFound = true;
			}
			else {
				System.out.println("Sorry, you cant register for " + numCourses + " courses");
			}
		}
		
		for (int i = 0; i < numCourses; i++) {
			int courseNum = i + 1;
			boolean subjectFound = false;
			boolean levelFound = false;
			boolean numberFound = false;
			boolean sectionFound = false;
			
			String subject = "-";
			int level = 0;
			int number = 0;
			char section = '-';
			
			while (!subjectFound) {
				System.out.println("What is the subject of course " + courseNum + "?");
				System.out.println("\tPlease input as the shorthand version, examples: \"CS, CHEM, BIOL\"");
				subject = scanner.nextLine().toUpperCase();
				subjectFound = true;
			}
			
			while (!levelFound) {
				System.out.println("What is the level of course " + courseNum + "?");
				level = scanner.nextInt();
				scanner.nextLine();
				if (level >= 100 && level <= 800 && level % 100 < 10) {
					levelFound = true;
				}
				else {
					System.out.println("Sorry, you can't have a course level of \"" + level + "\", try again.");
				}
			}
			
			while (!numberFound) {
				System.out.println("What is the course number for course " + courseNum + "?");
				number = scanner.nextInt();
				scanner.nextLine();
				if (number >= level && number < (level + 100)) {
					numberFound = true;
				}
				else {
					System.out.println("Sorry, you can't input course number \"" + number + "\" with course level \"" + level + "\"");
				}
			}
			
			while (!sectionFound) {
				System.out.println("What is the section of course " + courseNum + "?");
				section = scanner.nextLine().toUpperCase().charAt(0);
				if (Character.isLetter(section)) {
					sectionFound = true;
				}
				else {
					System.out.println("Sorry, you can't have section \"" + section + "\", it must be a letter, try again.");
				}
			}
			Course newCourse = new Course(subject, level, number, section);
			courses.add(newCourse);
		}
		return courses;
	}
	
	/**
	 * A helper method to input a classe into the web browser
	 * 
	 * @param courses The list of courses
	 * @param browser The web browser
	 */
	private static void inputClasses(ArrayList<Course> courses, WebDriver browser) {
		Select subject;
		Select courseLevel;
		WebElement number;
		WebElement section;
		
		// Here we are looping through to find each element on the webpage we need for our courses
		// The website element id's are named such that it is 'LIST_VAR' then the row then '_' then the column
		// Because the elements are named like this, we can easily loop through
		for (int i = 0; i < courses.size(); i++) {
			Course current = courses.get(i);
			int index = i + 1;
			subject = new Select(browser.findElement(By.id("LIST_VAR1_" + index)));
			courseLevel = new Select(browser.findElement(By.id("LIST_VAR2_" + index)));
			number = browser.findElement(By.id("LIST_VAR3_" + index));
			section = browser.findElement(By.id("LIST_VAR4_" + index));
			
			
			subject.selectByValue(current.getSubject());
			courseLevel.selectByValue(Integer.toString(current.getLevel()));
			number.sendKeys(Integer.toString(current.getNumber()));
			section.sendKeys(Character.toString(current.getSection()));
		}
		
		// Right here we are clicking the submit button, since all the classes were inputted
		browser.findElement(By.name("SUBMIT2")).click();
	}
	
	
	
	
	
}
