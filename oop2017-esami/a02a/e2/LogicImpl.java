package a02a.e2;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class LogicImpl implements Logic {

	private List<Pair<Integer, Integer>> cells = new ArrayList<>();
	private int size;

	public LogicImpl(int size) {
		this.size = size;
	}

	@Override
	public void hit(Pair<Integer, Integer> pair) {
		if (this.isPresent(pair)) {
			this.cells.remove(pair);
		} else {
			this.cells.add(pair);
		}

	}

	@Override
	public boolean isPresent(Pair<Integer, Integer> pair) {
		return this.cells.contains(pair);
	}

	private boolean isRowFull() {
		return IntStream.iterate(0, i -> i + 1).limit(this.size)
				.anyMatch(i -> this.cells.stream().filter(x -> x.getX().equals(i)).count() == this.size);
	}

	private boolean isColumnFull() {
		return IntStream.iterate(0, i -> i + 1).limit(this.size)
				.anyMatch(i -> this.cells.stream().filter(x -> x.getY().equals(i)).count() == this.size);
	}

	@Override
	public boolean isGameOver() {
		return this.isRowFull() && this.isColumnFull();
	}

}
