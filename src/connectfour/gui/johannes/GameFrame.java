package connectfour.gui.johannes;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

import connectfour.domain.johannes.Client;
import connectfour.domain.johannes.Game;

public class GameFrame
{
	private static GameFrame instance;
	private JFrame frame;
	private int[][] pos = new int[7][6];
	private JButton[] btns = new JButton[43];

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

		JButton btnGiveUp = new JButton("I give up!");
		btnGiveUp.setBounds(380, 135, 89, 40);
		btnGiveUp.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				Game.endGame();

			}
		});
		frame.getContentPane().add(btnGiveUp);

		int a = 1;
		for (int i = 0; i < 7; i++)
		{
			for (int j = 0; j < 6; j++)
			{
				pos[i][j] = a;
				JButton newButton = createButton(10 + i * 50, 11 + j * 51, 45,
						45, a);
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

	public void move(int ind)
	{
		int a = 1;
		if (ind > 6)
			a = 7;
		if (ind > 12)
			a = 13;
		if (ind > 18)
			a = 19;
		if (ind > 24)
			a = 25;
		if (ind > 30)
			a = 31;
		if (ind > 36)
			a = 37;
		for (int i = a + 5; i >= a; i--)
		{
			if (btns[i].getText().equals(""))
			{
				btns[i].setText("X");
				break;
			}
		}

	}

	public void buttonClick(int ind)
	{
		move(ind);
		System.out.println(ind);
	}
}
