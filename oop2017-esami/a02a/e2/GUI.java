package a02a.e2;

import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GUI extends JFrame {

	private static final long serialVersionUID = 2163114900098955781L;
	
	private Logic logic;
	private Map<JButton, Pair<Integer, Integer>> buttons = new HashMap<>();
	
    public GUI(int size) {
    	this.logic = new LogicImpl(size);
    	
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(500, 500);
        
        JPanel panel = new JPanel(new GridLayout(size,size));
        this.getContentPane().add(BorderLayout.CENTER,panel);
        
        
        ActionListener al = (e)->{
            final JButton bt = (JButton)e.getSource();
            this.logic.hit(this.buttons.get(bt));
            bt.setText(this.logic.isPresent(this.buttons.get(bt)) ? "*" : "");
            if(this.logic.isGameOver()) {
            	System.exit(0);
            }
        };
        
        for(int i = 0; i < size; i++) {
        	for(int j = 0; j < size; j++) {
        		final JButton jb = new JButton("");
                jb.addActionListener(al);
                panel.add(jb);
                this.buttons.put(jb, new Pair<>(i, j));
        	}
        }

        this.setVisible(true);
    }
    
}
