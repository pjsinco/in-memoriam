public class DOMagQueryTest
{
	public static void main(String[] args)
	{
		DOMagQuery query = new DOMagQuery();
		System.out.println(query.getSchoolAbbrev("118447"));
		System.out.println(query.getSchoolFullName("118447"));
		System.out.println(query.getDeceasedDate("40203"));
		System.out.println(query.getDeceasedDate("1980"));
	}

}
