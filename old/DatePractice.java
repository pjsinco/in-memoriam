import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DatePractice
{
	public static void main(String[] args)
	{
		Calendar cal = Calendar.getInstance(); // get the current date and time

		GregorianCalendar gCal = new GregorianCalendar();
		
		// just to see everything in the Calendar object
		System.out.println("cal: " + cal); 
	
		// get the year -- the 1 corresponds to the YEAR field in
		// the Calendar class
		System.out.println("cal.get(1): " + cal.get(1));

		// same thing -- but this way makes more sense
		System.out.println("cal.get(Calendar.YEAR): " + cal.get(Calendar.YEAR));

		System.out.println("cal.get(Calendar.MONTH): " + cal.get(Calendar.MONTH));

		// dummy call for now
		System.out.println("getDate(): " + getDate("2012-06-12"));		
		

	}

	public static String[] getDate(String strDate)
	{
		// temp dummy array for returning in the meanwhile
		String[] a = new String[3];

		String[] months = {
			"", "Jan.", "Feb.", "March", "April", "May",
			"June", "July", "Aug.", "Sept.", "Oct.",
			"Nov.", "Dec."
		};

		SimpleDateFormat formatter = null;
		Date date = null;

		try {
			formatter = new SimpleDateFormat("yyyy-MM-dd");
		} catch (NullPointerException npe) {
			System.out.println("Error formatting date");
			npe.printStackTrace();
		} catch (IllegalArgumentException iae) {
			System.out.println("Error formatting date");
			iae.printStackTrace();
		}
		
		try {
			date = formatter.parse(strDate);
		} catch (ParseException pe) {
			System.out.println("Error parsing string date");
			pe.printStackTrace();
		}

		System.out.println("Date object: " + date);
		
		return a; // for now

	}

}
