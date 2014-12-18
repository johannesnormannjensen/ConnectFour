package connectfour.gui.johannes;

import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;

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
			btns[u].setText(Game.myString);
			btns[u].setBackground(Game.myColor);
		}
		else 
		{
			int u = findCol(ind); 
			btns[u].setText(Game.opponentString);
			btns[u].setBackground(Game.opponentColor);
		}
	}
	
	public int findCol(int num)
	{
		int a = Math.round((float)num/6f - 0.5f);
		for (int i = a*6 + 5; i >= a*6; i--)
		{
			if (btns[i].getText().equals(""))
			{
				return i;
			}
		}
		return -1;
	}

	public void buttonClick(int ind)
	{
		Game.move(ind, true);
	}
}
