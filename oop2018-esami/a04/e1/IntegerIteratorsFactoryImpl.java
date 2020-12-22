package a04.e1;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.IntSupplier;
import java.util.stream.IntStream;

public class IntegerIteratorsFactoryImpl implements IntegerIteratorsFactory {

	@Override
	public SimpleIterator<Integer> empty() {
		return new SimpleIterator<Integer>() {
			@Override
			public Optional<Integer> next() {
				return Optional.empty();
			}
		};
	}

	@Override
	public SimpleIterator<Integer> fromList(List<Integer> list) {
		return new SimpleIterator<Integer>() {

			private final Iterator<Integer> iterator = list.iterator();

			@Override
			public Optional<Integer> next() {
				return this.iterator.hasNext() ?
					Optional.of(this.iterator.next()) : Optional.empty();
			}
		};
	}

	@Override
	public SimpleIterator<Integer> random(int size) {
		return new SimpleIterator<Integer>() {

			private final Random random = new Random();
			private final Iterator<Integer> iterator = IntStream.generate(() -> random.nextInt(size)).limit(size).iterator();

			@Override
			public Optional<Integer> next() {
				return this.iterator.hasNext() ?
						Optional.of(this.iterator.next()) : Optional.empty();
			}
		};
	}

    /**
     * @return an infinite iterator of elements 1,2,2,3,3,3,4,4,4,4,5,5,...
     */
	@Override
	public SimpleIterator<Integer> quadratic() {
		return new SimpleIterator<Integer>() {
			
			private Integer actual = 1;
			private Iterator<Integer> iterator = IntStream.generate(() -> actual).limit(actual).iterator();

			@Override
			public Optional<Integer> next() {
				if (this.iterator.hasNext()) {
					return Optional.of(this.iterator.next());
				}else {
					this.actual++;
					this.iterator = IntStream.generate(() -> actual).limit(actual).iterator();
					return this.next();
				}
			}
		};
	}


    
    /**
     * @return an infinite iterator of elements 0,0,1,0,1,2,0,1,2,3,0,1,2,3,4,...
     */
	@Override
	public SimpleIterator<Integer> recurring() {
		return new SimpleIterator<Integer>() {
			
			private Integer actual = 1;
			private Iterator<Integer> iterator = IntStream.range(0, actual).iterator();

			@Override
			public Optional<Integer> next() {
				if (this.iterator.hasNext()) {
					return Optional.of(this.iterator.next());
				}else {
					this.actual++;
					this.iterator = IntStream.range(0, actual).iterator();
					return this.next();
				}

			}
		};
	}

}
