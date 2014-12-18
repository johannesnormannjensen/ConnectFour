package connectfour.domain.johannes;

import javax.swing.JButton;

import connectfour.gui.johannes.GameFrame;


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
