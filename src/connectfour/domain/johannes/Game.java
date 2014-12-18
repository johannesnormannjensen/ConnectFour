package connectfour.domain.johannes;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import connectfour.gui.johannes.GameFrame;


public class Game
{
	public static Color myColor, opponentColor;
	public static String myString;
	public static String opponentString;
	
	public static void newGame()
	{
		Network.sendMsg(MESSAGE.NEWGAME, "");
	}

	public static void endGame()
	{
		Network.sendMsg(MESSAGE.ENDGAME, "");
	}
	
	public static void initialize(String myName, Color c, String opponentName, Color c2) 
	{
		myColor = c;
		myString = myName;
		opponentString = opponentName;
		opponentColor = c2;
		
		GameFrame.Instance();
	}
	
	public static void move(int ind, boolean myMove)
	{
		int buttonIndex = GameFrame.Instance().findCol(ind);
		if(buttonIndex != -1)
		{
			int w = Game.CheckWin(GameFrame.Instance().getBtns(), buttonIndex, (myMove ? Game.myString : Game.opponentString));
			if(w >= 4)
			{
				if(myMove)
				{
					Network.sendMsg(MESSAGE.WINGAME, ind + "");
					JOptionPane.showMessageDialog(null, "YOU WON!");
				}
				else
				{
					Network.sendMsg(MESSAGE.ACKWINGAME, "");
					JOptionPane.showMessageDialog(null, "YOU LOST!");
				}
			}
			else if(myMove)
			{
				Network.sendMsg(MESSAGE.MOVE, ind + "");
				System.out.println("Making a move " + ind);
			}
			GameFrame.Instance().move(ind, myMove);
		}
	}
	
	public static int CheckWin(JButton[] buttonArray, int buttonIndex, String checkString)
	{
		int hits = 0, hitsTemp = 0; 
		int i = buttonIndex-6;
		
//		HORIZONTAL
		//left
		hitsTemp = 1; // we know that the thing we just placed has the right string
		while(i >= 0)
		{
			if(buttonArray[i].getText().equals(checkString))
				hitsTemp++;
			else 
				break;
			i -= 6;
		}
		
		//right
		i = buttonIndex+6;
		while(i < 42)
		{
			if(buttonArray[i].getText().equals(checkString))
				hitsTemp++;
			else 
				break;
			i += 6;
		}
		
		if(hitsTemp > hits)
			hits = hitsTemp;
		hitsTemp = 1;
		
//		VERTICAL
		//down
		i = buttonIndex+1;
		do {
			if(i%6 == 0) //if we're at the top of the column, we're done
				break;
			
			if(buttonArray[i].getText().equals(checkString))
				hitsTemp++;
			else 
				break;
			i++;
		} while(i < 42);
		
		if(hitsTemp > hits)
			hits = hitsTemp;
		hitsTemp = 1; 
		
//		DIAGONAL 1		
		//diagonal 1 up-left
		i = buttonIndex - 6 - 1;
		while(i%6 != 5 && i >= 0) 
		{
			if(buttonArray[i].getText().equals(checkString))
				hitsTemp++;
			else 
				break;
			i -= (6 + 1); //previous column and one piece up
		}
		
		//diagonal 1 down-right
		i = buttonIndex + 6 + 1;
		while(i%6 != 0 && i < 42)
		{
			if(buttonArray[i].getText().equals(checkString))
				hitsTemp++;
			else 
				break;
			i += (6 + 1); //next column and one piece down
		}
		
		if(hitsTemp > hits)
			hits = hitsTemp;
		hitsTemp = 1; 
		
//		DIAGONAL 2		
		//diagonal 1 up-right
		i = buttonIndex + 6 - 1;
		while(i%6 != 5 && i < 42) 
		{
			if(buttonArray[i].getText().equals(checkString))
				hitsTemp++;
			else 
				break;
			i += (6 - 1); //previous column and one piece up
		}
		
		//diagonal 1 down-left
		i = buttonIndex - 6 + 1;
		while(i%6 != 0 && i >= 0)
		{
			if(buttonArray[i].getText().equals(checkString))
				hitsTemp++;
			else 
				break;
			i -= (6 - 1); //next column and one piece down
		}

		if(hitsTemp > hits)
			hits = hitsTemp;
		
		System.out.println("found a line of " + hits + " hits");
		return hits;
	}
}
