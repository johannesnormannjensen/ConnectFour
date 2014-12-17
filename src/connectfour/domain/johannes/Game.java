package connectfour.domain.johannes;


public class Game
{
	public static void newGame()
	{
		Network.sendMsg(MESSAGE.NEWGAME, "");
	}

	public static void endGame()
	{
		Network.sendMsg(MESSAGE.ENDGAME, "");
	}
}
