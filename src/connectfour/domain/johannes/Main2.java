package connectfour.domain.johannes;

import connectfour.gui.johannes.MainMenu;

public class Main2
{
	public static void main(String[] args)
	{
		MainMenu m = new MainMenu();
		Client c = new Client(1338);
		Server s = new Server(1337);
	}

}
