package a05b.e1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class IteratorIteratorFactoryImpl implements IteratorIteratorFactory {

	@Override
	public <E> Iterator<Iterator<E>> constantWithSingleton(E e) {
		return Stream.generate(() -> List.of(e).iterator()).iterator();
	}

	@Override
	public <E> Iterator<Iterator<E>> increasingSizeWithSingleton(E e) {
		return Stream.iterate(new ArrayList<E>(List.of(e)), l -> {
			l.add(e);
			return l;
		}).map(List::iterator).iterator();
	}

	@Override
	public Iterator<Iterator<Integer>> squares() {
		return Stream.iterate(1, i -> i + 1).map(i -> Stream.iterate(0, j -> j + 1).limit(i).map(x -> x * x).iterator())
				.iterator();
	}

	@Override
	public Iterator<Iterator<Integer>> evens() {
		return Stream.iterate(1, i -> i + 1).map(i -> Stream.iterate(0, j -> j + 2).limit(i).iterator()).iterator();
	}

}
