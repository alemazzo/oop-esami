package a02a.e2;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class LogicImpl implements Logic {
	private enum Directions {
		FRONT, BACK;
	}

	private class Element {

		private int size;
		private Pair<Integer, Integer> originalCoordinates;
		private Pair<Integer, Integer> coordinates;
		private Directions direction;
		private Pair<Integer, Integer> increment;

		public Element(Pair<Integer, Integer> coordinates, int size) {
			this.size = size;
			this.originalCoordinates = new Pair<Integer, Integer>(coordinates.getX(), coordinates.getY());
			this.coordinates = coordinates;
			this.setDirection();

		}
		
		private void setDirection() {
			int x = this.coordinates.getX() < this.size / 2 ? 1 : this.coordinates.getX() > this.size / 2 ? -1 : 0;
			int y = this.coordinates.getY() < this.size / 2 ? 1 : this.coordinates.getY() > this.size / 2 ? -1 : 0;
			this.increment = new Pair<Integer, Integer>(x, y);
		}

		public Pair<Integer, Integer> getCoordinates() {
			return this.coordinates;
		}

		void next() {
			this.direction = this.coordinates.equals(this.originalCoordinates) ? Directions.FRONT
					: this.coordinates.equals(new Pair<>(this.size / 2, this.size / 2)) ? Directions.BACK
							: this.direction;
			this.coordinates = this.direction.equals(Directions.FRONT)
					? new Pair<>(this.coordinates.getX() + this.increment.getX(),
							this.coordinates.getY() + this.increment.getY())
					: new Pair<>(this.coordinates.getX() - this.increment.getX(),
							this.coordinates.getY() - this.increment.getY());
		}
	}

	private List<Element> grid = new ArrayList<>();
	private final int size;

	public LogicImpl(int size) {
		this.size = size;
		IntStream.iterate(0, i -> i + this.size / 2).limit(3).forEach(x -> {
			IntStream.iterate(0, j -> j + this.size / 2).limit(3).forEach(y -> {
				if(!(x == this.size / 2 && y == this.size / 2)) {
					this.grid.add(new Element(new Pair<>(x, y), this.size));
				}
			});
		});
	}

	@Override
	public void next() {
		this.grid.forEach(x -> x.next());
	}

	@Override
	public Status getStatus(Pair<Integer, Integer> y) {
		return this.grid.stream().filter(x -> x.getCoordinates().equals(y)).count() > 0 ? Status.FILL : Status.EMPTY;
	}

}
