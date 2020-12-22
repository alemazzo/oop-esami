package a01a.e2;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.*;



public class GUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9015427435725275347L;
	
	private ChessController controller = new ChessControllerImpl();
	private static final int GRID_SIZE = 5;
	
	private final List<JButton> buttons = new ArrayList<>();
	
	
	private Pair<Integer, Integer> horse;
	private Pair<Integer, Integer> pawn;
	
	private enum Cells{
		EMPTY(""),
		PAWN("*"),
		HORSE("K");
		
		private String symbol;
		private Cells(String symbol) {
			this.symbol = symbol;
		}
		
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return this.symbol;
		}		
		
	}

	
	
	
	private int getPositionFromCoordinate(Pair<Integer, Integer> p) {
		return (p.getX() * GRID_SIZE) + (p.getY() % GRID_SIZE);
	}


	public GUI() {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(300, 300);

		JPanel panel = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE));
		this.getContentPane().add(BorderLayout.CENTER, panel);

		ActionListener al = (e) -> {
			final JButton bt = (JButton) e.getSource();
			final int row = this.buttons.indexOf(bt) / GRID_SIZE;
			final int column = this.buttons.indexOf(bt) % GRID_SIZE;
			final Pair<Integer, Integer> buttonPosition = new Pair<>(row, column);
			
			if(this.controller.move(buttonPosition)) {
				if (bt.getText().equals(Cells.PAWN.toString())) {
					System.out.println("WIN");
				}
				
				this.buttons.get(getPositionFromCoordinate(this.horse)).setText("");
				bt.setText(Cells.HORSE.toString());
				this.horse = new Pair<>(row, column);
			}

			System.out.println(row + ":" + column);
		};

		for (int i = 0; i < (GRID_SIZE * GRID_SIZE); i++) {
			final JButton jb = new JButton(Cells.EMPTY.toString());
			jb.addActionListener(al);
			this.buttons.add(jb);
			panel.add(jb);
		}

		this.horse = new Pair<Integer, Integer>(new Random().nextInt(GRID_SIZE), new Random().nextInt(GRID_SIZE));
		this.pawn = new Pair<Integer, Integer>(new Random().nextInt(GRID_SIZE), new Random().nextInt(GRID_SIZE));

		this.buttons.get(getPositionFromCoordinate(this.horse)).setText("K");
		this.buttons.get(getPositionFromCoordinate(this.pawn)).setText("*");

		this.controller.loadField(GRID_SIZE, this.horse, this.pawn);
		
		this.setVisible(true);
	}

	public static void main(String[] args) {
		new GUI();
	}

}
