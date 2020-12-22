package a04b.e2;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class GUI extends JFrame {

	private int size;
	private Logic logic;
    private Map<JButton, Pair<Integer, Integer>> buttons = new HashMap<>();
	
    public GUI(int size) {
    	this.size = size;
    	this.logic = new LogicImpl(this.size);
    	
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(500, 500);

        JPanel panel = new JPanel(new GridLayout(this.size,this.size));
        this.getContentPane().add(BorderLayout.CENTER,panel);
        
        JButton jb = new JButton(">");
        jb.addActionListener(e -> {
        	this.logic.tick();
        	if(this.logic.isGameOver()) {
        		System.exit(0);
        	}
        	this.refresh();
        });
        this.getContentPane().add(BorderLayout.SOUTH, jb);
        
        for(int i = 0; i < this.size; i++) {
        	for(int j = 0; j < this.size; j++) {
        		final JButton bt = new JButton("a");
                panel.add(bt);
                this.buttons.put(bt, new Pair<>(i, j));
        	}
        }
        
        this.refresh();
        this.setVisible(true);
    }

	private void refresh() {
		this.buttons.entrySet().forEach(e -> {
			if(this.logic.isFill(e.getValue())) {
				e.getKey().setText("X");
			}else {
				e.getKey().setText("");
			}			
		});
	}


}
