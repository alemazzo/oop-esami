package a01b.e1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TraceFactoryImpl implements TraceFactory {

	private <X> Trace<X> fromList(List<Event<X>> events) {
		return new Trace<X>() {

			Iterator<Event<X>> iterator = events.iterator();

			@Override
			public Optional<Event<X>> nextEvent() {
				if (!this.iterator.hasNext()) {
					return Optional.empty();
				}
				return Optional.of(this.iterator.next());
			}

			@Override
			public Iterator<Event<X>> iterator() {
				return this.iterator;
			}

			@Override
			public void skipAfter(int time) {
				final Optional<Event<X>> firstEvent = events.stream().filter(x -> x.getTime() > time).findFirst();
				this.iterator = events.subList(firstEvent.isPresent() ? events.indexOf(firstEvent.get()) : events.size(), events.size()).iterator();
			}

			@Override
			public Trace<X> combineWith(Trace<X> trace) {
				List<Event<X>> newList = new ArrayList<>(events);
				Iterator<Event<X>> it2 = trace.iterator();
				while (it2.hasNext()) {
					newList.add(it2.next());			
				}
				newList.sort((x, y) -> x.getTime() - y.getTime());
				return fromList(newList);
			}

			@Override
			public Trace<X> dropValues(X value) {
				return fromList(events.stream().filter(x -> !x.getValue().equals(value)).collect(Collectors.toList()));
			}
		};
	}

	@Override
	public <X> Trace<X> fromSuppliers(Supplier<Integer> sdeltaTime, Supplier<X> svalue, int size) {
		return this.fromList(Stream.iterate(0, i -> i + sdeltaTime.get()).limit(size)
				.map(i -> new EventImpl<X>(i, svalue.get())).collect(Collectors.toList()));
	}

	@Override
	public <X> Trace<X> constant(Supplier<Integer> sdeltaTime, X value, int size) {
		return this.fromSuppliers(sdeltaTime, () -> value, size);
	}

	@Override
	public <X> Trace<X> discrete(Supplier<X> svalue, int size) {
		return this.fromSuppliers(() -> 1, svalue, size);
	}

}
