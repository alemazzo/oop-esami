package a04a.e1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class SequencesProvidersFactoryImpl implements SequencesProvidersFactory {

	class BasicSequenceProvider<E> implements SequencesProvider<E> {

		private E element;
		private Iterator<List<E>> elements;

		public BasicSequenceProvider(E element) {
			this.element = element;
			this.reset();
		}

		@Override
		public List<E> nextSequence() {
			return this.hasOtherSequences() ? this.elements.next() : null;
		}

		@Override
		public boolean hasOtherSequences() {
			return this.elements.hasNext();
		}

		@Override
		public void reset() {
			this.elements = Stream.iterate((List<E>) new ArrayList<E>(), x -> {
				x.add(this.element);
				return x;
			}).iterator();
		}

	}

	class AlterningSequenceProvider<E> implements SequencesProvider<E> {

		private List<SequencesProvider<E>> providers;
		private int index = -1;

		public AlterningSequenceProvider(List<SequencesProvider<E>> providers) {
			this.providers = providers;
			this.reset();
		}

		@Override
		public List<E> nextSequence() {
			this.index = (this.index + 1) % this.providers.size();
			return this.hasOtherSequences() ? this.providers.get(this.index).nextSequence() : null;
		}

		@Override
		public boolean hasOtherSequences() {
			return this.providers.get(this.index).hasOtherSequences();
		}

		@Override
		public void reset() {
			this.index = -1;
			this.providers.forEach(p -> p.reset());
		}

	}

	class BoundedSequenceProvider<E> implements SequencesProvider<E>{

		private final SequencesProvider<E> provider;
		private final int bound;
		private Iterator<List<E>> iterator;
		
		
		public BoundedSequenceProvider(SequencesProvider<E> provider, int bound) {
			this.bound = bound;
			this.provider = provider;
			this.reset();
		}

		@Override
		public List<E> nextSequence() {
			return this.hasOtherSequences() ? this.iterator.next() : null;
		}

		@Override
		public boolean hasOtherSequences() {
			return this.iterator.hasNext();
		}

		@Override
		public void reset() {
			this.provider.reset();
			this.iterator = Stream.generate(() -> provider.nextSequence()).limit(bound).iterator();
		}
		
	}
	
	
	@Override
	public <E> SequencesProvider<E> iterative(E e) {
		return new BasicSequenceProvider<>(e);
	}

	@Override
	public <E> SequencesProvider<E> alternating(E e1, E e2) {
		return new AlterningSequenceProvider<>(List.of(iterative(e1), iterative(e2)));
	}

	@Override
	public <E> SequencesProvider<E> iterativeBounded(E e, int bound) {
		return new BoundedSequenceProvider<>(iterative(e), bound);
	}

	@Override
	public <E> SequencesProvider<E> alternatingBounded(E e1, E e2, int bound) {
		return new BoundedSequenceProvider<>(alternating(e1, e2), bound);
	}

}
