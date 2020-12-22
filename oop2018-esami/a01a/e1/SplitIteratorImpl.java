package a01a.e1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class SplitIteratorImpl<X> implements SplitIterator<X> {

	public final static boolean SPLITTABLE = true;
	public final static boolean UNSPLITTABLE = false;
	private static final boolean DEFAULT = SPLITTABLE;
	
	private final boolean splittable;
	private Iterator<X> iterator;

	public SplitIteratorImpl(final boolean splittable, List<X> elements) {
		this.splittable = splittable;
		this.iterator = elements.iterator();
	}

	public SplitIteratorImpl(List<X> elements) {
		this(DEFAULT, elements);
	}

	@Override
	public Optional<X> next() {
		return this.iterator.hasNext() ? Optional.of(this.iterator.next()) : Optional.empty();
	}
	

	@Override
	public SplitIterator<X> split() {
		if (!this.splittable) {
			throw new java.lang.UnsupportedOperationException();
		}

		List<X> elements = new ArrayList<>();
		this.iterator.forEachRemaining(elements::add);
		
		List<X> first = elements.subList(0, (elements.size() / 2));
		elements = elements.subList((elements.size() / 2), elements.size());
		
		this.iterator = elements.iterator();
		return new SplitIteratorImpl<X>(first);
	}


}
