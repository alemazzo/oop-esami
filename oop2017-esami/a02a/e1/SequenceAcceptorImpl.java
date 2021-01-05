package a02a.e1;

import java.util.Iterator;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SequenceAcceptorImpl implements SequenceAcceptor {

	Supplier<Iterator<Integer>> supplier;
	Iterator<Integer> sequenceIterator;

	
	@Override
	public void reset(Sequence sequence) {
		
		switch (sequence) {

		case POWER2:
			this.supplier = () -> IntStream.iterate(1, i -> i * 2).iterator();
			break;

		case FLIP:
			this.supplier = () -> IntStream.iterate(1, i -> i == 0 ? 1 : 0).iterator();
			break;

		case RAMBLE:
			this.supplier = () -> IntStream.iterate(0, i -> i + 1).map(i -> i % 2 == 0 ? 0 : (i / 2) + 1).iterator();
			break;

		case FIBONACCI:			
			this.supplier = () -> Stream.iterate(new Pair<>(1, 1), p -> new Pair<>(p.getY(), p.getX() + p.getY())).map(Pair::getX).iterator();
			break;

		}
		
		this.reset();

	}

	@Override
	public void reset() {
		if(this.supplier == null) {
			throw new IllegalStateException();
		}
		this.sequenceIterator = supplier.get();
	}

	@Override
	public void acceptElement(int i) {
		if (this.sequenceIterator == null || i != this.sequenceIterator.next()) {
			throw new IllegalStateException();
		}
	}

}
