package a01b.e1;

import java.util.*;
import java.util.function.Function;

public class GridFactoryImpl implements GridFactory {

	@Override
	public <E> Grid<E> create(int rows, int cols) {

		return new Grid<E>() {

			{
				elements = new HashSet<>();
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < cols; j++) {
						this.elements.add(new Cell<>(i, j, null));
					}
				}
			}

			private Set<Cell<E>> elements;
			private final Comparator<Cell<E>> cellsComparator = (x, y) -> y.getRow() < x.getRow() ? 1
					: y.getRow() > x.getRow() ? -1
							: y.getColumn() < x.getColumn() ? 1 : y.getColumn() > x.getColumn() ? -1 : 0;

			@Override
			public int getRows() {
				return rows;
			}

			@Override
			public int getColumns() {
				return cols;
			}

			@Override
			public E getValue(int row, int column) {
				return this.elements.stream().filter(x -> x.getRow() == row && x.getColumn() == column)
						.reduce((x, y) -> y).get().getValue();
			}

			private void modifyCells(Function<Cell<E>, Cell<E>> mapper) {
				Set<Cell<E>> newElements = new HashSet<>();
				this.elements.stream().forEach(x -> newElements.add(mapper.apply(x)));
				this.elements = newElements;
			}

			@Override
			public void setColumn(int column, E value) {
				this.modifyCells((x) -> x.getColumn() == column ? new Cell<>(x.getRow(), column, value) : x);
			}

			@Override
			public void setRow(int row, E value) {
				this.modifyCells((x) -> x.getRow() == row ? new Cell<>(row, x.getColumn(), value) : x);
			}

			@Override
			public void setBorder(E value) {
				this.modifyCells((x) -> x.getRow() == 0 || x.getRow() == this.getRows() - 1 || x.getColumn() == 0
						|| x.getColumn() == this.getColumns() - 1 ? new Cell<>(x.getRow(), x.getColumn(), value) : x);
			}

			@Override
			public void setDiagonal(E value) {
				this.modifyCells((x) -> x.getRow() == x.getColumn() ? new Cell<>(x.getRow(), x.getColumn(), value) : x);
			}

			@Override
			public Iterator<Cell<E>> iterator(boolean onlyNonNull) {
				return this.elements.stream().filter(x -> onlyNonNull ? x.getValue() != null : true)
						.sorted(this.cellsComparator).iterator();
			}
		};
	}

}
