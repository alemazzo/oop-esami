package a04b.e2;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class LogicImpl implements Logic {

	private int size;
	private Set<Pair<Integer, Integer>> positions = new HashSet<>();
	
	
	public LogicImpl(int size) {
		this.size = size;
		
		do {
			this.positions.add(this.getRandomCoordinates());			
		} while (this.positions.size() < 3);
	}
	
	private Pair<Integer, Integer> getRandomCoordinates(){
		Random rnd = new Random();
		return new Pair<>(rnd.nextInt(this.size), rnd.nextInt(this.size));
	}

	@Override
	public void tick() {
		
	}

	@Override
	public boolean isGameOver() {
		return this.positions.size() == this.size * this.size;
	}

	@Override
	public boolean isFill(Pair<Integer, Integer> value) {
		return this.positions.contains(value);
	}

}
