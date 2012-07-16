import java.util.Arrays;

/**
 *	This class converts a state's postal code into the state's
 *	AP abbreviation the state's full name.
 *
 *	@author		Patrick Sinco
 *	@version	1.0
 */
public class StateRef
{
	private String stateAp;
	private String statePostal;
	private String stateFull;
	private int index;
	private String[] full = {
		"Alabama", "Alaska", "Arizona", "Arkansas", "California",
		"Colorado", "Connecticut", "Delaware", "District of Columbia",
		"Florida", "Georgia", "Hawaii", "Idaho", "Illinois",
		"Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana",
		"Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota",
		"Mississippi", "Missouri", "Montana", "Nebraska", "Nevada",
		"New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina",
		"North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania",
		"Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas",
		"Utah", "Vermont", "Virginia", "Washington", "West Virginia",
		"Wisconsin", "Wyoming"
	};
	private	String[] postal = {
		"AL", "AK", "AZ", "AR", "CA",
		"CO", "CT", "DE", "DC",
		"FL", "GA", "HI", "ID", "IL",
		"IN", "IA", "KS", "KY", "LA",
		"ME", "MD", "MA", "MI", "MN",
		"MS", "MO", "MT", "NE", "NV",
		"NH", "NJ", "NM", "NY", "NC",
		"ND", "OH", "OK", "OR", "PA",
		"RI", "SC", "SD", "TN", "TX",
		"UT", "VT", "VA", "WA", "WV",
		"WI", "WY"
	};
	private String[] ap = {
		"Ala.", "Alaska", "Ariz.", "Ark.", "Calif.",
		"Colo.", "Conn.", "Del.", "D.C.",
		"Fla.", "Ga.", "Hawaii", "Idaho", "Ill.",
		"Ind.", "Iowa", "Kan.", "Ky.", "La.",
		"Maine", "Md.", "Mass.", "Mich.", "Minn.",
		"Miss.", "Mo.", "Mont.", "Neb.", "Nev.",
		"N.H.", "N.J.", "N.M.", "N.Y.", "N.C.",
		"N.D.", "Ohio", "Okla.", "Ore.", "Pa.",
		"R.I.", "S.C.", "S.D.", "Tenn.", "Texas",
		"Utah", "Vt.", "Va.", "Wash.", "W.Va.",
		"Wis.", "Wyo."
	};

	/**
	 *	Constructs a StateRef object based on postal abbreviation.
	 *
	 *	@param		postalAbbrev		The state's postal abbreviation
	 */
	public StateRef(String postalAbbrev)
	{
		this.statePostal = postalAbbrev;
		this.stateAp = this.getAp();
		this.stateFull = this.getFull();
	}

	/**
	 *	Finds the AP abbreviation of a state based on 
	 *	its postal abbreviation.
	 *
	 *	@return							The AP state abbreviation
	 */
	public String getAp() {
		int index = -1;

		for (int i = 0; i < this.postal.length; i++) {
			if (this.statePostal.equals(postal[i])) {
				index = i;
				return ap[i];
			}
		}	
		return "Error looking up state abbreviation";
	}
	
	/**
	 *	Finds the full name of a state based on its postal abbreviation.
	 *
	 *	@return							The full state name
	 */
	public String getFull() {
		int index = -1;

		for (int i = 0; i < this.full.length; i++) {
			if (this.statePostal.equals(postal[i])) {
				index = i;
				return full[i];	
			}
		}

		return "Error looking up state abbreviation";
	}	

	/**
	 *	Returns the AP state state abbreviation.
	 *
	 *	@return							The AP state abbreviation
	 */
	public String toString() {
		return this.stateAp;
	}

}
