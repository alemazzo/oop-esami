package a03b.e1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class PostOfficeFactoryImpl implements PostOfficeFactory {

	@Override
	public PostOffice createFlexible(int desksSize) {

		return new PostOffice() {

			private int ticket = 0;
			private Map<Integer, Operation> people = new TreeMap<>();
			private Map<Integer, Pair<Optional<Operation>, Optional<Operation>>> desks;

			{
				this.desks = new HashMap<>();
				for (int i = 0; i < desksSize; i++) {
					this.desks.put(i, new Pair<>(Optional.of(Operation.RECEIVE), Optional.empty()));
				}
			}

			@Override
			public List<Integer> peopleWaiting() {
				return this.people.keySet().stream().collect(Collectors.toList());
			}

			@Override
			public void goToDesk(int ticket, int desk) {
				this.desks.replace(desk, new Pair<>(this.desks.get(desk).getY(), Optional.of(this.people.get(ticket))));
				this.people.remove(ticket);
			}

			@Override
			public int getTicket(Operation operation) {
				this.people.put(ticket, operation);
				return this.ticket++;
			}

			@Override
			public void exitOnWait(int ticket) {
				this.people.remove(ticket);
			}

			@Override
			public Optional<Operation> deskState(int desk) {
				return this.desks.get(desk).getY();
			}

			@Override
			public void deskReleased(int desk) {
				this.desks.replace(desk, new Pair<>(Optional.empty(), Optional.empty()));
			}
		};
	}

	@Override
	public PostOffice createAlternating(int desksSize) {
		return new PostOffice() {

			private int ticket = 0;
			private Map<Integer, Optional<Operation>> people = new TreeMap<>();
			private Map<Integer, Pair<Optional<Operation>, Optional<Operation>>> desks;

			{
				this.desks = new HashMap<>();
				for (int i = 0; i < desksSize; i++) {
					this.desks.put(i, new Pair<>(Optional.empty(), Optional.empty()));
				}
			}

			@Override
			public List<Integer> peopleWaiting() {
				return this.people.keySet().stream().collect(Collectors.toList());
			}

			@Override
			public void goToDesk(int ticket, int desk) {
				if(this.desks.get(desk).getX().equals(this.people.get(ticket))) {
					throw new IllegalStateException();
				}
				this.desks.replace(desk, new Pair<>(this.desks.get(desk).getX(), this.people.get(ticket)));
				this.people.remove(ticket);
			}

			@Override
			public int getTicket(Operation operation) {
				this.people.put(ticket, Optional.of(operation));
				return this.ticket++;
			}

			@Override
			public void exitOnWait(int ticket) {
				this.people.remove(ticket);
			}

			@Override
			public Optional<Operation> deskState(int desk) {
				return this.desks.get(desk).getY();
			}

			@Override
			public void deskReleased(int desk) {
				this.desks.replace(desk, new Pair<>(this.desks.get(desk).getY(), Optional.empty()));
			}
		};
	}

}
