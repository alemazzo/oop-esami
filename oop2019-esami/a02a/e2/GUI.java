package a02a.e2;

import javax.swing.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.awt.*;
import java.awt.event.ActionListener;

public class GUI extends JFrame {

	private static final long serialVersionUID = -6218820567019985015L;
	private static String FILL_SYMBOL = "X";
	private static String EMPTY_SYMBOL = "";

	private List<Pair<JButton, Pair<Integer, Integer>>> buttons;
	private Logic controller;
	private int size;

	public GUI(int size) {
		this.size = size;
		this.controller = new LogicImpl(size);

		this.setSize(500, 500);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		JPanel panel = new JPanel(new GridLayout(this.size, this.size));
		this.getContentPane().add(BorderLayout.CENTER, panel);
		
		JButton next = new JButton(">");
		next.addActionListener(e -> {
			this.controller.next();
			this.refresh();
		});
		this.getContentPane().add(BorderLayout.SOUTH, next);

		ActionListener al = (e) -> {
			final JButton bt = (JButton) e.getSource();
			if(bt.getText().equals(FILL_SYMBOL)) {
				System.exit(0);
			}
		};

		this.buttons = new ArrayList<>();
		for (int i = 0; i < this.size; i++) {
			for (int j = 0; j < this.size; j++) {
				final JButton jb = new JButton();
				jb.addActionListener(al);
				panel.add(jb);
				buttons.add(new Pair<>(jb, new Pair<>(i, j)));
			}
		}
		
		this.refresh();
		this.setVisible(true);
	}

	private Pair<Integer, Integer> getButtonPosition(JButton button){
		return this.buttons.stream()
				.filter(x -> x.equals(button))
				.map(x -> x.getY())
				.collect(Collectors.toList()).get(0);
	}
	
	private String getTextFromStatus(Logic.Status status) {
		return status == Logic.Status.EMPTY ? EMPTY_SYMBOL : FILL_SYMBOL;
	}
	private void refresh() {
		
		this.buttons.forEach(x -> {
			JButton b = x.getX();
			b.setText(this.getTextFromStatus(this.controller.getStatus(x.getY())));
		});

	}

}
