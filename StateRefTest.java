public class StateRefTest
{
	public static void main(String[] args)
	{
		StateRef okla = new StateRef("OK");
		StateRef nd = new StateRef("ND");
		System.out.println(okla.getAp());
		System.out.println(nd.getFull());
	}

}
