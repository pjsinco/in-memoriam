// DeadRun.java
// TODO
// Weed out obits that are too old, i.e. deceased date > 8 months ago
// <html> and <body> tags are opened but not closed -- Fix
// Weed out obits that have already been published -- search db?

/**
 *	This class prints a correctly formatted obituary for each 
 *	physician or student who died between two dates. The format
 *	follows format in The DO's "In Memoriam" section.
 *
 *	@author		Patrick Sinco
 *	@version	1.0
 *	@version	Last modified 2012-07-18
 */

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;
import utilities.database.DOMagQuery;

public class DeadRun
{
	// number of arrays DOMagQuery.connect() makes by default;
	public static final int NUM_ARRAYS = 50;

	public static void main(String[] args)
	{
		String startDate = null;
//		String stopDate = null;
		Scanner input = new Scanner(System.in);
		File f = new File("in-mem.html");
		PrintWriter output = null;
		
		try {
			startDate = args[0];
//			stopDate = args[1];
		} catch (ArrayIndexOutOfBoundsException aioob) {
			System.out.println("\nNo dates were included.");
			System.out.println("Usage: java DeadRun <start-date> (yyyy-mm-dd)\n");
			System.exit(1);
		}

		try {
			output = new PrintWriter(f);
		} catch (FileNotFoundException fnf) {
			System.out.println("\nError: Could not find the file to write to\n");
			System.exit(1);
		}

		// get rows from inmemoriam table between specified dates
		DOMagQuery query = new DOMagQuery();
		String dead[][] = query.getArrays("select * from inmemoriam where " +
				"lastupdated >= '" + startDate +  "' order by lastname asc");

		printTopMatter(output);

		// print all the formatted obits
		// CLUMSY -- not sure we need *all* these steps now that getArray() in DOMagQuery
		// uses an ArrayList and returns an array with no nulls
		for (int i = 0; i < NUM_ARRAYS; i++) {
			// ... as long as the id is set; id cannot be null in MySQL table
			if (dead[i][14] != null) {
				output.println(inMemorialize(dead[i]));
				output.println(lookup(dead[i]));
			}
		}

		// close PrintWriter
		output.close();

		if (f.exists()) {
			System.out.println("\nSuccess! File has been output to \"in-mem.html\".\n");
		} else {
			System.out.println("\nError: No file has been written.\n");
		}
	}

	/**
	 *	Prints boilerplate text at top of "In Memoriam" section.
	 */
	public static void printTopMatter(PrintWriter output)
	{
		// print reminder
		output.println("<html><body style=\"font-family: monospace;\">");
		output.println("<p><em>REMINDER: get_template_part: inmem-db</em></p>\n");

		// print top matter
		output.println("<p><em>The following list of recently deceased " + 
				"osteopathic physicians includes links to obituaries and " + 
				"online memorials if they&rsquo;re available. Readers " + 
				"can notify </em>The DO<em> of their deceased colleagues " + 
				"by sending an email to " + 
				"<a href=\"mailto:thedo@osteopathic.org?subject=In Memoriam\">" +
				"thedo@osteopathic.org.</a></em></p>");

		// print excerpt
		output.println("EXCERPT:");
		output.println("View the names of recently deceased osteopathic physicians.");
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
//		String status = dead[7]; // not needed
		String dob = dead[8];
//		String gender = dead[9]; // not needed
//		String classLevel = dead[10]; // not needed
		String collegeCode = dead[11];
//		String collegeName = dead[12]; // not needed
		String gradDate = getGradYear(dead[13]);
		String id = dead[14];
		String lastUpdated = dead[15];
		
		// building these from methods
		String schoolAbbrev = query.getSchoolAbbrev(collegeCode);
		String dateOfDeath = getDeceasedDate(deceasedDate);

		return "<p><strong class=\"mem\">" + firstName + " " + middleName + " " + 
				lastName + ", " + designation + ", </strong>" + 
				getAge(dob, deceasedDate) + " (" + schoolAbbrev + 
				" " + gradDate + "), of " + getHometown(city, postalState) + 
				" \n<a href=\"\" title=\"\" target=\"_blank\">\ndied</a> " + 
				getDeceasedDate(deceasedDate) + ". Visit " + 
				(designation.contains("DO") ? "Dr. " : "" ) + 
				lastName + "&rsquo;s \n<a href=\"\" title=\"\" " +
				"target=\"_blank\">\nonline guest book</a>.</p>";
	}
/**
	 *	Creates links to obit searches on Bing and Google for the deceased. Also
	 *	creates a link to The DO so I can check if the obit has already run.
	 *
	 *	@param			dead		An array containing every value returned from
	 *									the database for a deceased person
	 *	@return						Three links: two to search for obits on Bing
	 *									and Google, and one to search The DO
	 */
	public static String lookup(String[] dead)
	{
		String google =
				String.format("<a href=\"" +
				"https://www.google.com/search?q=%s+%s+osteopathic+obituary\"" +
				" target=\"_blank\"><small>Google</small></a>",
				dead[0], dead[2]);
		String bing = 
				String.format("<a href=\"" +
				"https://www.bing.com/search?q=%s+%s+osteopathic+obituary\"" +
				" target=\"_blank\"><small>Bing</small></a>",
				dead[0], dead[2]);
		String thedo = 
				String.format("<a href=\"" +
				"https://www.do-online.org/TheDO/?s=%s+%s\"" +
				" target=\"_blank\"><small><em>The DO</em></small></a>",
				dead[0], dead[2]);


		return  "<p>\n\t" + google + "\n\t" + bing + "\n\t" + thedo + "\n</p>\n";
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
