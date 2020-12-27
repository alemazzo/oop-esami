package a01a.e1;

import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class InfiniteSequenceOpsImpl implements InfiniteSequenceOps {

	private <X> InfiniteSequence<X> fromStream(Stream<X> stream) {
		return new InfiniteSequence<X>() {
			private Iterator<X> iterator = stream.iterator();

			@Override
			public X nextElement() {
				return iterator.next();
			}
		};
	}

	@Override
	public <X> InfiniteSequence<X> ofValue(X x) {
		return this.fromStream(Stream.generate(() -> x));
	}

	@Override
	public <X> InfiniteSequence<X> ofValues(List<X> l) {
		return this.fromStream(Stream.iterate(0, i -> (i + 1) % l.size()).map(i -> l.get(i)));
	}

	@Override
	public InfiniteSequence<Double> averageOnInterval(InfiniteSequence<Double> iseq, int intervalSize) {
		return this.fromStream(Stream.iterate(0, i -> i + 1).map(i -> Stream.iterate(0, j -> j + 1)
				.limit(intervalSize).map(j -> iseq.nextElement()).reduce((a, b) -> a + b).get() / intervalSize));

	}

	@Override
	public <X> InfiniteSequence<X> oneEachInterval(InfiniteSequence<X> iseq, int intervalSize) {
		return this.fromStream(Stream.iterate(0, i -> i + 1)
				.map(i -> Stream.generate(() -> iseq.nextElement()).limit(intervalSize).reduce((a, b) -> b).get()));
	}

	@Override
	public <X> InfiniteSequence<Boolean> equalsTwoByTwo(InfiniteSequence<X> iseq) {
		return this.fromStream(Stream.generate(() -> iseq.nextElement().equals(iseq.nextElement())));
	}

	@Override
	public <X, Y extends X> InfiniteSequence<Boolean> equalsOnEachElement(InfiniteSequence<X> isx,
			InfiniteSequence<Y> isy) {
		return this.fromStream(Stream.generate(() -> isx.nextElement().equals(isy.nextElement())));
	}

	@Override
	public <X> Iterator<X> toIterator(InfiniteSequence<X> iseq) {
		return Stream.generate(() -> iseq.nextElement()).iterator();
	}
}
