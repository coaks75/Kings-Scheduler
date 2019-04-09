

public class Course {

	/** A field for the course subject. */
	private String subject;
	
	/** A field for the course level. */
	private int level;
	
	/** A field for the course number. */
	private int number;
	
	/** A field for the couse section. */
	private char section;
	/**
	 * The constructor for the couse class
	 * 
	 * @param subjectValue The courses subject
	 * @param levelValue The course level
	 * @param numberValue The course number
	 * @param sectionValue The course section
	 */
	public Course(String subjectValue, int levelValue, int numberValue, char sectionValue) {
		subject = subjectValue;
		level = levelValue;
		number = numberValue;
		section = sectionValue;
	}
	
	/**
	 * The getter for the course subject
	 * 
	 * @return The course subject
	 */
	public String getSubject() {
		return subject;
	}
	
	/** The getter for the course level
	 * 
	 * @return The course level
	 */
	public int getLevel() {
		return level;
	}
	
	/**
	 * The getter for the course number
	 * 
	 * @return The course number
	 */
	public int getNumber() {
		return number;
	}
	
	/**
	 * The getter for the course section
	 * 
	 * @return The course section
	 */
	public char getSection() {
		return section;
	}
	
}
