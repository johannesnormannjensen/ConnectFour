package connectfour.domain.johannes;

public class Game
{
	public static void newGame()
	{
		//TODO do something here
		Network.sendIt("NEW GAME");
	}

	public static void endGame()
	{
		//TODO do something here
		Network.sendIt("END GAME");
	}
}
