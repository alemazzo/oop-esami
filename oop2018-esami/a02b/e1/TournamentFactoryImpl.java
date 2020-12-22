package a02b.e1;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class TournamentFactoryImpl implements TournamentFactory {

	@Override
	public Tournament make(String name, int year, int week, final Set<String> players, Map<String, Integer> points) {

		return new Tournament() {

			private final String nameString = name;
			private final int yearInt = year;
			private final int weekInt = week;
			private Set<String> playersSet = players;
			private Map<String, Integer> pointsMap = points;

			@Override
			public String winner() {
				return this.pointsMap.entrySet().stream().max((x, y) -> x.getValue() - y.getValue())
						.map(x -> x.getKey()).get();
			}


			@Override
			public int getYear() {
				return this.yearInt;
			}

			@Override
			public int getWeek() {
				return this.weekInt;
			}

			@Override
			public Optional<Integer> getResult(String player) {
				Optional<Integer> res = Optional.ofNullable(this.pointsMap.get(player));
				if (res.isEmpty() && this.playersSet.contains(player)) {
					res = Optional.of(0);
				}

				return res;
			}

			@Override
			public Set<String> getPlayers() {
				return Set.copyOf(this.playersSet);
			}

			@Override
			public String getName() {
				return this.nameString;
			}

			@Override
			public boolean equals(Object t) {
				return this.nameString == ((Tournament) t).getName();
			}

		};
	}

}
