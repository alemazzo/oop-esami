package a06a.e2;

import javax.swing.*;


import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class GUI extends JFrame {
    
	private Logic logic;
	private Map<JButton, Pair<Integer, Integer>> buttons = new HashMap<>();
	

    public GUI(int size) {
    	this.logic = new LogicImpl(size);
    	
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(500, 500);
        int cols = size;
        
        JPanel panel = new JPanel(new GridLayout(cols,cols));
        this.getContentPane().add(BorderLayout.CENTER,panel);
        
        ActionListener al = (e)->{
            final JButton bt = (JButton)e.getSource();
            this.logic.hit(this.buttons.get(bt));
            if(this.logic.isGameOver()) {
            	System.exit(0);
            }
            this.refresh();
        };
        for (int i=0;i<cols;i++){
        	for(int j = 0; j < cols; j++) {
                final JButton jb = new JButton("");
                jb.addActionListener(al);
                panel.add(jb);
                this.buttons.put(jb, new Pair<>(i, j));        		
        	}
        }
        
        this.refresh();
        this.setVisible(true);
    }

        
    private void refresh() {
		this.buttons.entrySet().forEach(e -> {
			e.getKey().setText(this.logic.getValue(e.getValue()));
			
		});
		
	}

        
}
