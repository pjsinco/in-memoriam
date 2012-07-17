import java.util.ArrayList;

public class Dateline
{
	private String city;

	// These domestic cities can stand alone in datelines
	private String[] standAlone = {
		"Atlanta",
		"Milwaukee",
		"Baltimore",
		"Minneapolis",
		"Boston",
		"New Orleans",
		"Chicago",
		"New York",
		"Cincinnati",
		"Oklahoma City",
		"Cleveland",
		"Philadelphia",
		"Dallas",
		"Phoenix",
		"Denver",
		"Pittsburgh",
		"Detroit",
		"St. Louis",
		"Honolulu",
		"Salt Lake City",
		"Houston",
		"San Antonio",
		"Indianapolis",
		"San Diego",
		"Las Vegas",
		"San Francisco",
		"Los Angeles",
		"Seattle",
		"Miami",
		"Washington"
	};

	/**
	 *	Constructs a Dateline object.
	 *
	 *	@param		city		The dateline's city
	 */
	public Dateline(String city) {
		this.city = city;
	}

	/**
	 *	Checks whether a city can stand on its own without a state,
	 *	according to AP style.
	 *
	 *	@param		city		The city to check
	 *	@return					Whether the city can stand on its own
	 */
	public boolean canStandAlone()
	{
		for (String standAloneCity : standAlone) {
			if (standAloneCity.equals(city)) {
				return true;
			}
		}

		return false;
	}

	public String toString()
	{
		return this.city;
	}
}
