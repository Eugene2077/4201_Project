package webd4201.shine;

/**
 * Class and object / DB connect, CRUD operation
 * @author Eugene Shin
 * @version 2.0
 * @since Feb 12 2023
 */

import java.text.DecimalFormat;

/**
 * Class and object / DB connect, CRUD operation
 * @author Eugene Shin
 * @version 2.0
 * @since Feb 12 2023
 */
public class Mark {
	/**
	 * @Constant MINIMUM_GPA: minimum grade of GPA
	 * @Constant MINIMUM_GPA: maximum grade of GPA
	 */
	public static final float MINIMUM_GPA = 0.00f;
	public static final float MAXIMUM_GPA = 5.00f;
	
	/*** 
	 * @Attributes courseCode : course code
	 * @Attributes courseName : name of the course
	 * @Attributes result     : course grade
	 * @Attributes gpaWeighting : GPA result
	 */
	private String courseCode;
	private String courseName;
	private int result;
	private float gpaWeighting;
	
	// getters and setters -------------------------------------------
	/**
	 * @return the courseCode
	 */
	public String getCourseCode() {
		return courseCode;
	}
	/**
	 * @param courseCode
	 * @throws InvalidUserDataException
	 */
	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}
	/**
	 * @return the courseName
	 */
	public String getCourseName() {
		return courseName;
	}
	/**
	 * 
	 * @param courseName
	 * @throws InvalidUserDataException
	 */
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	/**
	 * @return the result
	 */
	public int getResult() {
		return result;
	}
	/**
	 * 
	 * @param result
	 * @throws InvalidUserDataException
	 */
	public void setResult(int result) {
		this.result = result;
	}
	/**
	 * @return the gpaWeighting
	 */
	public float getGpaWeighting() {
		return gpaWeighting;
	}
	/**
	 * 
	 * @param gpaWeighting
	 * @throws InvalidUserDataException
	 */
	public void setGpaWeighting(float gpaWeighting) {
		this.gpaWeighting = gpaWeighting;
	}

	// constructor(default) -----------------------------------------------
	/**
	 * default constructor
	 */
	 public Mark() {
		 this("WEBD2201", "Web Development - Fundamentals", 71, 4.0f);
	 }
	
	// constructor(parameterized) -----------------------------------------
	/**
	 * @Overload
	 * @param courseCode
	 * @param courseName
	 * @param result
	 * @param gpaWeighting
	 */
	public Mark(String courseCode, String courseName, int result, float gpaWeighting) {
		setCourseCode(courseCode);
		setCourseName(courseName);
		setResult(result);
		setGpaWeighting(gpaWeighting);
	}
	
	
	// Methods  -------------------------------------------------------
	
	/* Override toString method for formatted output
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		DecimalFormat df = new DecimalFormat("0.0");
		String code = String.format("%-7s", getCourseCode());
		String name = String.format("%-35s", getCourseName());
		String result = String.format("%-5s", getResult());
		String weight = df.format(getGpaWeighting());
		return code + "\t" + name + "\t" + result + "\t" + weight;
	};
	
	
	
	
	
	
	
	
}
