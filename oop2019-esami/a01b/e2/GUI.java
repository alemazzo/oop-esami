package a01b.e2;

import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;
import java.awt.*;

public class GUI extends JFrame {
    
    private int size;
    private int mineNumber;
    
    private Logic logic;
    private Map<JButton, Pair<Integer, Integer>> buttons;
    
    public GUI(int size, int mineNumber) {
    	this.size = size;
    	this.mineNumber = mineNumber;
    	this.logic = new LogicImpl(this.size, this.mineNumber);
    	this.buttons = new HashMap<>();
    	
    	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(500, 500);

        JPanel panel = new JPanel(new GridLayout(this.size, this.size));
        this.getContentPane().add(BorderLayout.CENTER,panel);
        
        ActionListener al = (e)->{
            final JButton bt = (JButton)e.getSource();
            bt.setEnabled(false);
            final var coordinates = this.buttons.get(bt);
            
            if(this.logic.hit(coordinates.getX(), coordinates.getY())) {
            	System.out.println("LOST");
            	System.exit(0);
            }
            
            if(this.logic.hasWin()) {
            	System.out.println("WIN");
            	System.exit(0);
            }
            
            bt.setText("" + this.logic.getNearMinesCount(coordinates));
        };
        
        for(int x = 0; x < this.size; x++) {
            for(int y = 0; y < this.size; y++) {
            	final JButton bt = new JButton("");
            	bt.addActionListener(al);
                panel.add(bt);
                this.buttons.put(bt, new Pair<Integer, Integer>(x, y));
            }
        }

        
        this.setVisible(true);
    }
        
}
