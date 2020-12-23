package a03a.e2;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class LogicImpl implements Logic {

	private enum Direction {
		RIGHT(new Pair<>(0, 1)), DOWN(new Pair<>(1, 0)), LEFT(new Pair<>(0, -1)), UP(new Pair<>(-1, 0));

		private Pair<Integer, Integer> increment;

		Direction(Pair<Integer, Integer> increment) {
			this.increment = increment;
		}

		public Pair<Integer, Integer> getIncrement() {
			return this.increment;
		}
	}

	
	private Iterator<Direction> directionIterator = Stream.iterate(0, i -> (i + 1) % 4)
			.map(i -> List.of(Direction.RIGHT, Direction.DOWN, Direction.LEFT, Direction.UP).get(i))
			.iterator();

	
	private int limit;
	private Pair<Integer, Integer> enemyPosition = new Pair<>(0, 0);
	private Direction enemyDirection;
	private Set<Pair<Integer, Integer>> playersPosition = new HashSet<>();

	public LogicImpl(int size) {
		this.limit = size - 1;
		this.enemyDirection = this.directionIterator.next();
	}

	private Pair<Integer, Integer> pairSum(Pair<Integer, Integer> p1, Pair<Integer, Integer> p2) {
		return new Pair<>(p1.getX() + p2.getX(), p1.getY() + p2.getY());
	}

	private boolean borderReached() {
		return this.enemyPosition.equals(new Pair<>(0, 0)) 
				|| this.enemyPosition.equals(new Pair<>(0, this.limit))
				|| this.enemyPosition.equals(new Pair<>(this.limit, 0)) 
				|| this.enemyPosition.equals(new Pair<>(this.limit, this.limit));
	}

	@Override
	public void next() {
		this.enemyPosition = this.pairSum(this.enemyPosition, this.enemyDirection.getIncrement());
		if(this.borderReached()) {
			this.enemyDirection = this.directionIterator.next();
		}
	}

	@Override
	public boolean isFinished() {
		return this.playersPosition.contains(this.enemyPosition);
	}

	@Override
	public Status getPositionStatus(Pair<Integer, Integer> value) {
		return this.enemyPosition.equals(value) ? Status.ENEMY
				: this.playersPosition.contains(value) ? Status.PLAYER : Status.EMPTY;
	}

	@Override
	public void addPoint(Pair<Integer, Integer> pair) {
		if (!this.playersPosition.contains(pair)) {
			playersPosition.add(pair);
		}
	}

}
