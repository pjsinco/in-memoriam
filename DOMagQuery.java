import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Locale;

public class DOMagQuery
{
	// for logging on to mysql.osteopathic.org
	private static final String DATABASE_URL = 
			"jdbc:mysql://mysql.osteopathic.org:3306/domag";
	private static final String USER = "domag_user";
	private static final String SITE_URL = "www.do-online.org";
	private static final String PASSWORD = "1q2w3e4r5t";

	// tables names
	private static final String TABLE_SCHOOLS = "fullschoolnames";
	private static final String TABLE_INMEM = "inmemoriam";
	private static final String TABLE_POSTS = "wp_posts";

	private Connection connection; // managed connection
	private Statement statement; // query statement
	private ResultSet resultSet; // manages result

	/**
	 *	
	 */
	public DOMagQuery()
	{

	}

	/**
	 *	This method executes a query to the database.
	 *	Example query: 
	 *	myQuery.connect("fullcollegenames", "29325", 
	 *	SELECT fullname from fullcollegenames where code='29325'
	 *	SELECT [column] from [table] where [field] = [value]
	 *
	 *	@param		column		The column to search
	 *	@param		field		The table to use
	 *	@param		field		The column to get the value from
	 *	@param		field		The value to match
	 *	
	 *	@return					The query results
	 */
	private String connect(String column, String table, String field, 
		String value)
	{
		String s = "";
		String query = "SELECT " + column + " from " + table + 
				" WHERE " + field + "='" + value + "'";

		System.out.println(query); // debug

		try {
			// establish connection to database
			connection = DriverManager.getConnection(
				DATABASE_URL, USER, PASSWORD);

			// create Statement for querying database
			statement = connection.createStatement();
			
			// query database
			resultSet = statement.executeQuery(query);

			// process query results
			ResultSetMetaData metaData = resultSet.getMetaData();

			while (resultSet.next()) {
				s = resultSet.getString(column); // might be getString(1)
			}

		} catch (SQLException sqle) {
			System.out.println("Error interacting with database");
			sqle.printStackTrace();
		} finally {
			try {
				resultSet.close();
				statement.close();
				connection.close();
			} catch (SQLException sqle) {
				System.out.println("Error closing the database connection");
				sqle.printStackTrace();
			}
		}

		return s;
	}

	/**
	 *	Returns the abbreviation of the queried school.
	 *
	 *	@param		code		The college's iMIS code
	 *	@return					The college's abbreviation
	 */
	public String getSchoolAbbrev(String code)
	{
		String abbrev = this.connect("abbrev", TABLE_SCHOOLS, "code", code);
		return abbrev;
	}

	/**
	 *	Returns the fullname of the queried school.
	 *
	 *	@param		code		The college's iMIS code
	 *	@return					The college's full name	
	 */
	public String getSchoolFullName(String code)
	{
		String fullName = this.connect("fullname", TABLE_SCHOOLS, "code", code);
		return fullName;
	}

	public String getGradYear(String id)
	{
		String gradYear = this.connect("YEAR(graddate)", TABLE_INMEM, "id", id);
		return gradYear;
	}

	/**
	 *	Returns the correctly formatted date-of-death to display. Accounts for
	 *	whether the year should be included in the returned String.
	 *
	 *	See here for MySQL date functions:
	 *	http://dev.mysql.com/doc/refman/5.5/en/date-and-time-functions.html
	 *	
	 *	@param		id			The deceased's iMIS ID
	 *	@return					The date of death, correctly formatted
	 */
	public String getDeceasedDate(String id)
	{
		// get the current year
		Calendar cal = Calendar.getInstance();
		int thisYear = cal.get(Calendar.YEAR); 

		// 0 is left intentionally empty
		// 1 = "Jan.", 2 = "Feb.", etc.
		String[] monthsAP = {
			"", "Jan.", "Feb.", "March", "April", "May",
			"June", "July", "Aug.", "Sept.", "Oct.",
			"Nov.", "Dec."
		};

		String month = this.connect("MONTH(deceaseddate)", TABLE_INMEM, "id", id);
		String day = this.connect("DAYOFMONTH(deceaseddate)", TABLE_INMEM, "id", id);
		String year = this.connect("YEAR(deceaseddate)", TABLE_INMEM, "id", id);

		// return the appropriately formatted date
		// If the year is before this year, include it in the string
		String date = monthsAP[Integer.parseInt(month)] + " " + day + 
				(year.equals(String.valueOf(thisYear)) ? ("") : (", " + year));
				
		return date;
	}
	

}
