// DeadRun.java

/**
 *	This class prints a correctly formatted obituary for each 
 *	physician or student who died between two dates. The format
 *	follows format in The DO's "In Memoriam" section.
 *
 *	@author		Patrick Sinco
 *	@version	0.9
 */

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
				+ stopDate + "' order by lastname asc");

		for (int i = 0; i < NUM_ARRAYS; i++) {
			// as long as the ID is set
			if (dead[i][14] != null) {
				System.out.println(inMemorialize(dead[i]));
			}
		}
	}

	/**
	 *	Formats an obituary for "In Memoriam."
	 *
	 *	@param		dead			An array containing every value returned from
	 *									the database for a deceased person.
	 *	@return						The correctly formatted obituary.
	 */
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
//		String status = dead[7];
		String dob = dead[8];
//		String gender = dead[9];
//		String classLevel = dead[10];
		String collegeCode = dead[11];
//		String collegeName = dead[12];
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
				", " + designation + ", " + getAge(dob, deceasedDate) + " (" + schoolAbbrev + 
				" " + gradDate + "), of " + getHometown(city, postalState) + 
				" died " + getDeceasedDate(deceasedDate) + ".";
	}

	/**
	 *	Calculates age.
	 *
	 *	@param		dob				The date of birth
	 *	@param		deceasedDate	The date of death
	 *	@return						The correct age
	 *
	 *	Algorithm source: 
	 *	http://stackoverflow.com/questions/1116123/how-do-i-calculate-someones-age-in-java
	 *
	 *	Does this algorithm hold up?
	 *
	 *	See here:
	 *	http://www.javaworld.com/javaworld/jw-03-2001/jw-0330-time.html
	 */
	public static String getAge(String dob, String deceasedDate)
	{
		int birthMonth = Integer.parseInt(parseDate(dob)[1]);
		int deathMonth = Integer.parseInt(parseDate(deceasedDate)[1]);
		int birthDay = Integer.parseInt(parseDate(dob)[2]);
		int deathDay = Integer.parseInt(parseDate(deceasedDate)[2]);
		int birthYear = Integer.parseInt(parseDate(dob)[0]);
		int deathYear = Integer.parseInt(parseDate(deceasedDate)[0]);

		int age = deathYear - birthYear;
		if (birthMonth > deathMonth) {
			age--;
		} else if (birthMonth == deathMonth) {
			if (birthDay > deathDay) {
				age--;
			}
		}
		
		return String.valueOf(age);
	}

	/**
	 *	Helper method parses the yyyy-MM-dd format used in the database.
	 *
	 *	@param		dateString		The date String to parse
	 *	@return						The parsed date as an array:
	 *									index 0 -- year
	 *									index 1 -- month
	 *									index 2 -- day
	 */
	public static String[] parseDate(String dateString)
	{
		String month = null;
		String day = null;
		String year = null;
		
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

		String[] parsed = {year, month, day};
		return parsed;
	}

	/**
	 *	Returns the correctly formatted date of death. Accounts for whether
	 *	the year should be included, as well as for AP month abbreviations.
	 *
	 *	@param		dateString			The date to format
	 *	@return							The date as it should appear in the obit
	 */
	public static String getDeceasedDate(String dateString)
	{
		String month = parseDate(dateString)[1];
		String day = parseDate(dateString)[2];
		String year = parseDate(dateString)[0];
		String[] monthsAP = {
			"", "Jan.", "Feb.", "March", "April", "May",
			"June", "July", "Aug.", "Sept.", "Oct.",
			"Nov.", "Dec."
		};

		// get the current year
		Calendar cal = Calendar.getInstance();
		int thisYear = cal.get(Calendar.YEAR);

		String dateFormatted = monthsAP[Integer.parseInt(month)] + " " + day + 
				(year.equals(String.valueOf(thisYear)) ? ("") : (", " + year));

		return dateFormatted;
	}

	/**
	 *	
	 */
	public static String getHometown(String city, String state)
	{
		Dateline dateline = new Dateline(city);
		StateRef stateRef = new StateRef(state);
		String place = String.format("%s, %s,", city, stateRef);

		if (dateline.canStandAlone()) {
			return city;
		} else {
			return place;
		}
	}

	public static String getGradYear(String date)
	{
		Scanner input = new Scanner(date);
		input.useDelimiter("-");
		String year = input.next();

		return year;
	}

}
