package a04b.e2;

import java.util.*;

public class oldLogicImpl implements oldLogic {
	
	private int size;
	private Set<Pair<Integer, Integer>> filled = new HashSet<>();

	public oldLogicImpl(int size) {
		this.size = size;
		for(int i = 0; i < 3; i++) {
			Pair<Integer, Integer> coordinates;
			do {
				coordinates = this.getRandomCoordinates();
			}while(this.filled.contains(coordinates));
			this.filled.add(coordinates);
		}
	}
	
	private Pair<Integer, Integer> getRandomCoordinates(){
		final Random rnd = new Random();
		return new Pair<>(rnd.nextInt(this.size),rnd.nextInt(this.size)); 
	}

	private int getValueOrBound(int index) {
		return index < 0 ? 0 : index >= this.size ? this.size -1 : index;
	}
	private Set<Pair<Integer, Integer>> getNearCoordinates(Pair<Integer, Integer> base){
		Set<Pair<Integer, Integer>> result = new HashSet<>();
		for(int i = getValueOrBound(base.getX() - 1); i <=  getValueOrBound(base.getX() + 1); i++) {
			for(int j = getValueOrBound(base.getY() - 1); j <= getValueOrBound(base.getY() + 1); j++) {
				result.add(new Pair<>(i, j));
			}
		}
		return result;
	}
	@Override
	public void next() {
		Set<Pair<Integer, Integer>> newFilled = new HashSet<>();
		this.filled.stream().forEach(x -> newFilled.addAll(this.getNearCoordinates(x)));
		this.filled = newFilled;		
	}

	@Override
	public boolean isFill(Pair<Integer, Integer> pair) {
		return this.filled.contains(pair);
	}

	@Override
	public boolean isGameOver() {
		return this.filled.size() == this.size * this.size;
	}

}
