package connectfour.gui.johannes;

import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;





//import connectfour.domain.johannes.Client;
import connectfour.domain.johannes.Game;
import connectfour.domain.johannes.MESSAGE;
import connectfour.domain.johannes.Network;

public class GameFrame
{
	private static GameFrame instance;
	private JFrame frame;
	private int[][] pos = new int[7][6];
	private JButton[] btns = new JButton[42];
	
	public JButton[] getBtns()
	{
		return btns;
	}

	private Color myColor, opponentColor;
	public static String myString;
	public String opponentString;

	private GameFrame()
	{
		instantiate();
	}

	public static GameFrame Instance()
	{
		if (instance == null)
			instance = new GameFrame();
		return instance;
	}

	public void instantiate()
	{
		frame = new JFrame();
		frame.setBounds(100, 100, 520, 380);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

//		JButton btnGiveUp = new JButton("I give up!");
//		btnGiveUp.setBounds(380, 135, 89, 40);
//		btnGiveUp.addActionListener(new ActionListener()
//		{
//
//			@Override
//			public void actionPerformed(ActionEvent e)
//			{
//				Game.endGame();
//
//			}
//		});
//		frame.getContentPane().add(btnGiveUp);

		int a = 0;
		for (int i = 0; i < 7; i++)
		{
			for (int j = 0; j < 6; j++)
			{
				pos[i][j] = a;
				JButton newButton = createButton(10 + i * 50, 11 + j * 51, 45, 45, a);
				btns[a] = newButton;
				frame.getContentPane().add(newButton);
				a++;
			}
		}

		frame.setVisible(true);
	}

	JButton createButton(int x, int y, int boundx, int boundy, int index)
	{
		final int ind = index;
		JButton btnNewButton = new JButton("");
		btnNewButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				buttonClick(ind);
			}
		});
		btnNewButton.setBounds(x, y, boundx, boundy);
		return btnNewButton;
	}

	public void move(int ind, boolean myMove)
	{
		if(myMove)
		{
			int u = findCol(ind); 
			btns[u].setText(myString);
			btns[u].setBackground(myColor);
		}
		else 
		{
			int u = findCol(ind); 
			btns[u].setText(opponentString);
			btns[u].setBackground(opponentColor);
		}
	}
	
	private int findCol(int num)
	{
		int a = Math.round((float)num/6f - 0.5f);
//		int a = 1;
//		if (num > 6)
//			a = 7;
//		if (num > 12)
//			a = 13;
//		if (num > 18)
//			a = 19;
//		if (num > 24)
//			a = 25;
//		if (num > 30)
//			a = 31;
//		if (num > 36)
//			a = 37;
		for (int i = a*6 + 5; i >= a; i--)
		{
			if (btns[i].getText().equals(""))
			{
				return i;
			}
		}
		return a;
	}

	public void buttonClick(int ind)
	{
		int buttonIndex = findCol(ind); //finding the correct button to check for winning conditions
		System.out.println("Checking win");
		
		int winLine = CheckWin(btns, buttonIndex); 
		if(winLine >= 4)
		{
			System.out.println("I WIN ERMAGERD!!");
			Network.sendMsg(MESSAGE.WINGAME, Integer.toString(ind));
			move(ind, true);
		}
		else 
		{
			Network.sendMsg(MESSAGE.MOVE, Integer.toString(ind));
			move(ind, true);
			System.out.println("placed at " + ind);
			System.out.println("I have a line at " + winLine + " at best");
		}
	}

	public void initialize(String myName, Color c, String opponentName, Color c2) 
	{
		myColor = c;
		myString = myName;
		opponentString = opponentName;
		opponentColor = c2;
	}
	
	public static int CheckWin(JButton[] buttonArray, int buttonIndex)
	{
		int hits = 0, hitsTemp = 0; 
		int i = buttonIndex-6;
		
//		HORIZONTAL
		//left
		hitsTemp = 1; // we know that the thing we just placed has the right string
		while(i >= 0)
		{
			if(buttonArray[i].getText().equals(myString))
				hitsTemp++;
			else 
				break;
			i -= 6;
		}
		
		//right
		i = buttonIndex+6;
		while(i < 42)
		{
			if(buttonArray[i].getText().equals(myString))
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
			
			if(buttonArray[i].getText().equals(myString))
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
			if(buttonArray[i].getText().equals(myString))
				hitsTemp++;
			else 
				break;
			i -= (6 + 1); //previous column and one piece up
		}
		
		//diagonal 1 down-right
		i = buttonIndex + 6 + 1;
		while(i%6 != 0 && i < 42)
		{
			if(buttonArray[i].getText().equals(myString))
				hitsTemp++;
			else 
				break;
			i += (6 + 1); //next column and one piece down
		}
		
		//TODO: the other diag dir
		if(hitsTemp > hits)
			hits = hitsTemp;
		
		System.out.println("found a line of " + hits + " hits");
		return hits;
	}
}
