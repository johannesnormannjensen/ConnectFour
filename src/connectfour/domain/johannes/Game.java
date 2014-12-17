package connectfour.domain.johannes;

public class Game
{
	public static void newGame()
	{
		Network.sendIt("NEW GAME");
	}

	public static void endGame()
	{
		Network.sendIt("END GAME");
	}
}
