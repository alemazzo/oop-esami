package a04b.e2;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class oldGUI extends JFrame {
    
	private int size;
	private oldLogic logic;
	private Map<JButton, Pair<Integer, Integer>> buttons = new HashMap<>();
	
    public oldGUI(int size) {
    	this.size = size;
    	this.logic = new oldLogicImpl(this.size);
    	
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(500, 500);
        
        JPanel panel = new JPanel(new GridLayout(this.size, this.size));
        this.getContentPane().add(BorderLayout.CENTER,panel);
        
        JButton south = new JButton(">");
        south.addActionListener(e -> {
        	this.logic.next();
        	this.refresh();
        	if(this.logic.isGameOver()) {
        		System.exit(0);
        	}
        });
        
        this.getContentPane().add(BorderLayout.SOUTH, south);
        
        for (int i = 0; i < this.size; i++){
        	for(int j = 0; j < this.size; j++) {
        		final JButton jb = new JButton("");
                panel.add(jb);
                this.buttons.put(jb, new Pair<>(i, j));
        	}
        }
        this.refresh();
        this.setVisible(true);
    }

	private void refresh() {
		this.buttons.entrySet().forEach(e -> {
			e.getKey().setText(this.logic.isFill(e.getValue()) ? "X" : "");
		});
		
	}

}
