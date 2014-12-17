package connectfour.domain.johannes;

import connectfour.gui.johannes.MainMenu;

public class Main1
{
	public static void main(String[] args)
	{
		MainMenu m = new MainMenu();
		Client c = new Client(1337);
		Server s = new Server(1338);

	}

}
