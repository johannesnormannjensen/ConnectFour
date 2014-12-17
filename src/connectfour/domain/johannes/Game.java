package connectfour.domain.johannes;

public class Game
{
	public static void newGame()
	{
		Client.sendIt("NEW GAME");
	}

	public static void endGame()
	{
		Client.sendIt("END GAME");
	}
}
