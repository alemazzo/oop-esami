package a01b.e2;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

public class LogicImpl implements Logic {

	private Random rnd = new Random();
	private int size; 
	private int mineNumber;
	private Set<Pair<Integer, Integer>> mines;
	private int safeHit;
	
	public LogicImpl(int size, int mineNumber) {
		this.size = size;
		this.mineNumber = mineNumber;
		this.mines = new HashSet<>();
		
		IntStream.iterate(0, i -> i + 1).limit(this.mineNumber).forEach(x -> {
			Pair<Integer, Integer> coordinates;
			do {
				coordinates = new Pair<>(this.rnd.nextInt(this.size), this.rnd.nextInt(this.size));
			} while (this.mines.contains(coordinates));
			System.out.println("X = " + coordinates.getX() + " Y = " + coordinates.getY());
			this.mines.add(coordinates);
		});
		
	}
	@Override
	public boolean hit(int x, int y) {
		if(this.mines.contains(new Pair<>(x, y))) {
			return true;
		}else {
			this.safeHit++;
			return false;
		}
	}
	
	@Override
	public boolean hasWin() {
		return this.safeHit == (this.size * this.size) - this.mineNumber;
	}
	
	private boolean areNear(Pair<Integer, Integer> point1, Pair<Integer, Integer> point2) {
		return Math.abs(point1.getX() - point2.getX()) <= 1 &&  Math.abs(point1.getY() - point2.getY()) <= 1;
	}
	
	@Override
	public int getNearMinesCount(Pair<Integer, Integer> value) {
		return (int)this.mines.stream()
				.filter(x -> this.areNear(x, value))
				.count();
	}

}
