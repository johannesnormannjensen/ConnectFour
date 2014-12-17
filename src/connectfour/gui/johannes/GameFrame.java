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
	private JButton[] btns = new JButton[43];
	
	private Color myColor, opponentColor;
	private String myString, opponentString;

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

		int a = 1;
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
			btns[findCol(ind)].setText(myString);
			btns[findCol(ind)].setForeground(myColor);
		}
		else 
		{
			btns[findCol(ind)].setText(opponentString);
			btns[findCol(ind)].setForeground(opponentColor);
		}
	}
	
	private int findCol(int num)
	{
		int a = 1;
		if (num > 6)
			a = 7;
		if (num > 12)
			a = 13;
		if (num > 18)
			a = 19;
		if (num > 24)
			a = 25;
		if (num > 30)
			a = 31;
		if (num > 36)
			a = 37;
		for (int i = a + 5; i >= a; i--)
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
		//TODO do something here
		Network.sendMsg(MESSAGE.MOVE, Integer.toString(ind));
		move(ind, true);
		System.out.println(ind);
	}

	public void initialize(String myName, Color c, String opponentName, Color c2) 
	{
		myColor = c;
		myString = myName;
		opponentString = opponentName;
		opponentColor = c2;
	}
}
