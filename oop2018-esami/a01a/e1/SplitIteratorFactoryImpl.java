package a01a.e1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SplitIteratorFactoryImpl implements SplitIteratorFactory {

	private class BasicSplitIterator<X> implements SplitIterator<X> {

		private Iterator<X> iterator;
		private boolean splittable;

		public BasicSplitIterator(Iterator<X> it, boolean splittable) {
			this.iterator = it;
			this.splittable = splittable;
		}

		@Override
		public Optional<X> next() {
			if (this.iterator.hasNext()) {
				return Optional.of(this.iterator.next());
			}
			return Optional.empty();
		}

		@Override
		public SplitIterator<X> split() {
			if (this.splittable) {
				List<X> remaining = new ArrayList<>();
				this.iterator.forEachRemaining(x -> {
					remaining.add(x);
				});

				this.iterator = remaining.subList(remaining.size() / 2, remaining.size()).iterator();
				return fromList(remaining.subList(0, remaining.size() / 2));
			} else {
				throw new UnsupportedOperationException();
			}

		}

	}

	@Override
	public SplitIterator<Integer> fromRange(int start, int stop) {
		return new BasicSplitIterator<>(IntStream.range(start, stop + 1).iterator(), true);
	}

	@Override
	public SplitIterator<Integer> fromRangeNoSplit(int start, int stop) {
		return new BasicSplitIterator<>(IntStream.range(start, stop + 1).iterator(), false);
	}

	@Override
	public <X> SplitIterator<X> fromList(List<X> list) {
		return new BasicSplitIterator<>(list.iterator(), true);
	}

	@Override
	public <X> SplitIterator<X> fromListNoSplit(List<X> list) {
		return new BasicSplitIterator<>(list.iterator(), false);
	}

	@Override
	public <X> SplitIterator<X> excludeFirst(SplitIterator<X> si) {
		
		 return new BasicSplitIterator<X>(Stream.generate(() -> si.next())
				.skip(1)
				.takeWhile(Optional::isPresent)
				.map(Optional::get)
				.iterator(), true);
		
	}

	@Override
	public <X, Y> SplitIterator<Y> map(SplitIterator<X> si, Function<X, Y> mapper) {
		return new SplitIterator<Y>() {

			@Override
			public Optional<Y> next() {
				Optional<X> elem = si.next();
				if(elem.isEmpty()) {
					return Optional.empty();
				}
				return Optional.of(mapper.apply(elem.get()));
			}

			@Override
			public SplitIterator<Y> split() {
				SplitIterator<X> splitted = si.split();
				return new BasicSplitIterator<Y>(
						Stream.generate(() -> splitted.next())
						.takeWhile(Optional::isPresent)
						.map(Optional::get)
						.map(mapper::apply)
						.iterator(), true);
				
			}
		};
	}

}
