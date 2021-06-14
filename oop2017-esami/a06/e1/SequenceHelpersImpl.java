package a06.e1;

import java.util.Iterator;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

public class SequenceHelpersImpl implements SequenceHelpers {

	private <X> Sequence<X> fromStream(Stream<X> stream){
		return new Sequence<X>() {
			private Iterator<X> iterator = stream.iterator();
			@Override
			public X nextElement() {
				return iterator.next();
			}
		};
	}
	
	@Override
	public <X> Sequence<X> of(X x) {
		return this.fromStream(Stream.generate(() -> x));
	}

	@Override
	public <X> Sequence<X> cyclic(List<X> l) {
		return this.fromStream(Stream.generate(() -> l).flatMap(List::stream));
	}

	@Override
	public Sequence<Integer> incrementing(int start, int increment) {
		return this.fromStream(Stream.iterate(start, i -> i + increment));
	}

	@Override
	public <X> Sequence<X> accumulating(Sequence<X> input, BinaryOperator<X> op) {
		return this.fromStream(Stream.iterate(input.nextElement(), e -> op.apply(e, input.nextElement())));
	}

	@Override
	public <X> Sequence<Pair<X, Integer>> zip(Sequence<X> input) {
		return this.fromStream(Stream.iterate(0, i -> i + 1).map(i -> new Pair<>(input.nextElement(), i)));
	}

}
