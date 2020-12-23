package a03a.e2;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;

public class GUI extends JFrame {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -1657555521661688348L;
	private int size;
	private Logic logic;
	private Map<JButton, Pair<Integer, Integer>> buttons = new HashMap<>();
	
    public GUI(int size) {
    	this.size = size;
    	this.logic = new LogicImpl(this.size);
    	
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(500, 500);

        JPanel panel = new JPanel(new GridLayout(this.size, this.size));
        this.getContentPane().add(BorderLayout.CENTER,panel);
        
        JButton south = new JButton(">");
        south.addActionListener(e -> {
        	this.logic.next();
        	if(this.logic.isFinished()) {
        		System.exit(0);
        	}
        	this.refresh();
        });
        
        this.getContentPane().add(BorderLayout.SOUTH, south);
        
        ActionListener al = (e)->{
            final JButton bt = (JButton)e.getSource();
            this.logic.addPoint(this.buttons.get(bt));
            this.refresh();
        };
        
        for(int i = 0; i < this.size; i++) {
        	for(int j = 0; j < this.size; j++) {
                final JButton jb = new JButton();
                jb.addActionListener(al);
                panel.add(jb);
                this.buttons.put(jb, new Pair<>(i, j));
        	}
        }

        this.refresh();
        this.setVisible(true);
    }

        
    private void refresh() {
    	this.buttons.entrySet().forEach(x -> {
    		Logic.Status status = this.logic.getPositionStatus(x.getValue());
    		x.getKey().setText(status.equals(Logic.Status.ENEMY) ? "X" : status.equals(Logic.Status.PLAYER) ? "O" : "");
    	});
	}
        
}
