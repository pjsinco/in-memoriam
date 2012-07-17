import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class DeadRun
{
	// number of arrays DOMagQuery.connect() makes by default;
	public static final int NUM_ARRAYS = 50;

	public static void main(String[] args)
	{
		Scanner input = new Scanner(System.in);

		System.out.print("Enter the start date (yyyy-mm-dd): ");
		String startDate = input.next();
		System.out.print("Enter the stop date (yyyy-mm-dd): ");
		String stopDate = input.next();

		DOMagQuery query = new DOMagQuery();
		String dead[][] = query.connect("select * from inmemoriam where " +
				"lastupdated between '" + startDate +  "' and '" 
				+ stopDate + "'");

		System.out.println(Arrays.toString(dead[0]));
		System.out.println(Arrays.toString(dead[1]));

		for (int i = 0; i < NUM_ARRAYS; i++) {
			// as long as the ID is set
			if (dead[i][14] != null) {
				System.out.println(inMemorialize(dead[i]));
			}
		}
	}

	public static String inMemorialize(String[] dead)
	{
		DOMagQuery query = new DOMagQuery();

		// directly from the database
		String firstName = dead[0];
		String middleName = dead[1];
		String lastName = dead[2];
		String designation = dead[3];
		String city = dead[4];
		String postalState = dead[5];
		String deceasedDate = dead[6];
		String status = dead[7];
		String dob = dead[8];
		String gender = dead[9];
		String classLevel = dead[10];
		String collegeCode = dead[11];
		String collegeName = dead[12];
		String gradDate = getGradYear(dead[13]);
		String id = dead[14];
		String lastUpdated = dead[15];
		
		// building these from methods
		String currYear = null;
		String age = null;
		String schoolAbbrev = query.getSchoolAbbrev(collegeCode);
		String dateOfDeath = getDeceasedDate(deceasedDate);
		String state = null;

		return firstName + " " + middleName + " " + lastName + 
				", " + designation + ", [age] (" + schoolAbbrev + 
				" " + gradDate + "), of " + getHometown(city, postalState) + 
				" died " + getDeceasedDate(deceasedDate); // for now
	}

	public static String getHometown(String city, String state)
	{
		Dateline dateline = new Dateline(city);
		StateRef stateRef = new StateRef(state);

		return ""; // for now
	}

	public static String getGradYear(String date)
	{
		Scanner input = new Scanner(date);
		input.useDelimiter("-");
		String year = input.next();

		return year;
	}

	public static String getDeceasedDate(String dateString)
	{
		String month = null;
		String day = null;
		String year = null;
		String[] monthsAP = {
			"", "Jan.", "Feb.", "March", "April", "May",
			"June", "July", "Aug.", "Sept.", "Oct.",
			"Nov.", "Dec."
		};
		// get the current year
		Calendar cal = Calendar.getInstance();
		int thisYear = cal.get(Calendar.YEAR);

		try {
			Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
			month = new SimpleDateFormat("M").format(date);
			day = new SimpleDateFormat("d").format(date);
			year = new SimpleDateFormat("yyyy").format(date);
		} catch (NullPointerException npe) {
			System.out.println("Error constructing SimpleDateFormat object");
			npe.printStackTrace();
		} catch (IllegalArgumentException iae) {
			System.out.println("Error constructing SimpleDateFormat object");
			iae.printStackTrace();
		} catch (ParseException pe) {
			System.out.println("Error constructing SimpleDateFormat object");
			pe.printStackTrace();
		}

		String dateFormatted = monthsAP[Integer.parseInt(month)] + " " + day + 
				(year.equals(String.valueOf(thisYear)) ? ("") : (", " + year));

		return dateFormatted;
	}
}
