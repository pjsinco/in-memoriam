import java.util.Scanner;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;
import java.io.FileNotFoundException;

/**
 *	Classes used so far:
 *		DOMagQuery
 *		Dateline
 *		StateRef
 */
public class Dead
{
	// reflects order in inmemoriam database
	private String firstName;
	private	String middleName;
	private	String lastName;
	private String designation;
	private String city;
	private String postalState;
	private String deceasedDate;
	private String status;
	private String dob;
	private String gender;
	private String classLevel;
	private String collegeCode;
	private String collegeName;
	private String gradDate;
	private String id;
	private String lastUpdated;
	private String age;

	// variables we're building from db variables
	private String schoolAbbrev;
	private String gradYear;
	private String state;
	private DOMagQuery query;
	private Dateline dateline;
	private StateRef stateRef;
	private String currYear;

	public Dead(String fileName)
	{
		File f = new File(fileName);
		Scanner input = null;
		query = new DOMagQuery();

		try {
			input = new Scanner(f);
			input.useDelimiter(",");

			while (input.hasNext()) {
				this.firstName = input.next();
				this.middleName = input.next();
				this.lastName = input.next();
				this.designation = input.next();
				this.city = input.next();
				this.postalState = input.next();
				this.deceasedDate = input.next();
				this.status = input.next();
				this.dob = input.next();
				this.gender = input.next();
				this.classLevel = input.next();
				this.collegeCode = input.next();
				this.collegeName = input.next();
				this.gradDate = input.next();
				this.id = input.next();
				this.lastUpdated = input.next();
				this.age = input.next();
			}
		} catch (FileNotFoundException fnf) {
			System.out.println("An error occurred reading in dead file.");
			System.exit(1);
		}

		stateRef = new StateRef(this.postalState);

		this.schoolAbbrev = query.getSchoolAbbrev(collegeCode);
		this.gradYear = query.getGradYear(id);
		this.state = stateRef.getAp();
	}

	public String toString()
	{
		return this.firstName + " " + 
			this.middleName + " " + 
			this.lastName + ", " + 
			this.designation + " (" + 
			this.schoolAbbrev + " " +
			this.gradYear + "), " +
			this.age + ", of " +
			this.city + ", " +
			this.state + ", died " + 
			this.deceasedDate;
	}

	// ** INCOMPLETE **
	// helper method to parse date string YYYY-mm-dd
	public String[] getDate(String strDate)
	{
		// temp dummy array for returning in the meanwhile
		String[] a = new String[3];

		String[] months = {
			"", "Jan.", "Feb.", "March", "April", "May",
			"June", "July", "Aug.", "Sept.", "Oct.",
			"Nov.", "Dec."
		};

		

		try {

		} catch (NullPointerException npe) {
			System.out.println("Error formatting date");
			npe.printStackTrace();
		} catch (IllegalArgumentException iae) {
			System.out.println("Error formatting date");
			iae.printStackTrace();
		}
		
		return a; // for now

	}
}
