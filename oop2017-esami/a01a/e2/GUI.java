package a01a.e2;


import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;



public class GUI extends JFrame{

	private List<JButton> buttons = new ArrayList<>();
	private Logic logic;
	
	public GUI(int size){
		this.logic = new LogicImpl(size);
		
		this.setSize(500, 100);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.getContentPane().setLayout(new FlowLayout());
				
		ActionListener ac = e -> {
			final JButton jb = (JButton)e.getSource();
			final int index = this.buttons.indexOf(jb);
			final int res = this.logic.hit(index);
			jb.setText("" + res);
			jb.setEnabled(res == size ? false : true);
			if(this.logic.isGameOver()) {
				System.exit(1);
			}
		};
		
		for(int i = 0; i < size; i++) {
			JButton bt = new JButton("0");
			this.buttons.add(bt);
			bt.addActionListener(ac);		
			this.getContentPane().add(bt);
		}
		
		
		final JButton print = new JButton("Print");
		print.addActionListener(e -> {
			this.logic.print();
		});
		this.getContentPane().add(print);
		
		this.setVisible(true);
	}

}
