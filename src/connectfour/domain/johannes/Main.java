package connectfour.domain.johannes;

import connectfour.gui.johannes.MainMenu;

public class Main {
	public static void main(String[] args)
	{
		MainMenu m = new MainMenu();
//		Client c = new Client(1337);
//		Server s = new Server(1338);
		Network n = new Network(1337);
	}
}
