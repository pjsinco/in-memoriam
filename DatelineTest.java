public class DatelineTest
{
	public static void main(String[] args) {
		 Dateline city = new Dateline("Honolulu");
		 System.out.printf("City:\t%s\nStand alone?\t%s\n",
						 city, city.canStandAlone());
	}

}
