package connectfour.gui.johannes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;

import connectfour.domain.johannes.Game;
import connectfour.domain.johannes.Server;
import connectfour.domain.johannes.Client;

public class MainMenu
{

	private JFrame frame;
	private JTextField txtEx;

	public MainMenu()
	{
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblYourIp = new JLabel("Your IP: " + Server.getLocIp());
		lblYourIp.setBounds(10, 10, 150, 14);
		frame.getContentPane().add(lblYourIp);

		JButton btnConnect = new JButton("Connect");
		btnConnect.setBounds(161, 143, 89, 23);
		frame.getContentPane().add(btnConnect);
		btnConnect.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				Client.setSendIP(txtEx.getText());
				Game.newGame();
			}
		});

		JButton btnExit = new JButton("Exit");
		btnExit.setBounds(161, 179, 89, 23);
		frame.getContentPane().add(btnExit);
		btnExit.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				System.exit(0);

			}
		});

		txtEx = new JTextField();
		txtEx.setText("");
		txtEx.setBounds(127, 96, 156, 20);
		frame.getContentPane().add(txtEx);
		txtEx.setColumns(10);

		JLabel lblOpponentIp = new JLabel("        Opponent IP");
		lblOpponentIp.setBounds(127, 71, 156, 14);
		frame.getContentPane().add(lblOpponentIp);

		frame.setVisible(true);

	}
}
