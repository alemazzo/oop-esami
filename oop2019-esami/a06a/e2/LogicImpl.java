package a06a.e2;

import java.util.HashMap;
import java.util.Map;


public class LogicImpl implements Logic {

	private Map<Pair<Integer, Integer>, Integer> field = new HashMap<>();
	private int size;
	private boolean gameOver = false;
	
	public LogicImpl(int size) {
		this.size = size;
	}

	@Override
	public void hit(Pair<Integer, Integer> pair) {
		this.gameOver = field.containsKey(pair);
		
		this.field.entrySet().stream()
			.filter(x -> !(x.getKey().getX().equals(pair.getX())))
			.filter(x -> !(x.getKey().getY().equals(pair.getY())))
			.forEach(e -> {
				this.field.replace(e.getKey(), e.getValue() + 1);
			});
		this.field.put(pair, 0);
		
	}

	@Override
	public boolean isGameOver() {
		return this.gameOver;
	}

	@Override
	public String getValue(Pair<Integer, Integer> value) {
		return !this.field.containsKey(value) ? "" : this.field.get(value).toString();
	}

}
