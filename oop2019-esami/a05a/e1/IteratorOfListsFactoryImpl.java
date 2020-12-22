package a05a.e1;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IteratorOfListsFactoryImpl implements IteratorOfListsFactory {

	public <E> IteratorOfLists<E> fromStream(Supplier<Stream<List<E>>> stream) {

		return new IteratorOfLists<E>() {

			{
				reset();
			}

			Iterator<List<E>> iterator;

			@Override
			public List<E> nextList() {
				return this.iterator.next();
			}

			@Override
			public boolean hasOtherLists() {
				return this.iterator.hasNext();
			}

			@Override
			public void reset() {
				this.iterator = stream.get().iterator();
			}

		};
	}

	@Override
	public <E> IteratorOfLists<E> iterative(E e) {
		return fromStream(() -> Stream.iterate(0, i -> i + 1).map(i -> Collections.nCopies(i, e)));
	}

	@Override
	public <E> IteratorOfLists<E> iterativeOnList(List<E> list) {
		return fromStream(() -> Stream.iterate(0, i -> i + 1).map(i -> Stream.iterate(0, j -> (j + 1) % list.size())
				.limit(i).map(j -> list.get(j)).collect(Collectors.toList())));
	}

	@Override
	public <E> IteratorOfLists<E> iterativeWithPreamble(E e, List<E> preamble) {
		return fromStream(() -> Stream.iterate(0, i -> i + 1).map(i -> Stream
				.concat(preamble.stream(), Collections.nCopies(i, e).stream()).collect(Collectors.toList())));
	}
}
