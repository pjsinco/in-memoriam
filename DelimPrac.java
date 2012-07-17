import java.util.Scanner;

public class DelimPrac
{
	public static void main(String[] args) {
		 String date = "2012-06-26";
		 Scanner input = new Scanner(date);
		 input.useDelimiter("-");

		 while (input.hasNext()) {
			System.out.println(input.next());
		 }

	}
}
