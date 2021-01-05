package a02a.e1;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import a02a.e1.Tournament.Result;
import a02a.e1.Tournament.Type;

public class TournamentBuilderImpl implements TournamentBuilder {

	private boolean build = false;

	private String name;
	private Type type;
	private Map<String, Integer> ranking;
	private Map<String, Result> results = new HashMap<>();
	private Set<String> players = new HashSet<>();

	@Override
	public TournamentBuilder setType(Type type) {
		if (type == null) {
			throw new NullPointerException();
		}
		this.type = type;
		return this;
	}

	@Override
	public TournamentBuilder setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public TournamentBuilder setPriorRanking(Map<String, Integer> ranking) {
		this.ranking = ranking;
		this.ranking.keySet().forEach(x -> this.players.add(x));
		return this;
	}

	@Override
	public TournamentBuilder addResult(String player, Result result) {
		if (this.type == null || (result == Result.WINNER && this.results.containsValue(Result.WINNER))
				|| (this.results.containsKey(player))) {
			throw new IllegalStateException();
		}

		this.results.put(player, result);
		this.players.add(player);
		return this;
	}

	@Override
	public Tournament build() {
		if (build || type == null || name == null || ranking == null) {
			throw new IllegalStateException();
		}

		build = true;
		return new Tournament() {

			@Override
			public String winner() {
				return results.entrySet().stream().filter(x -> x.getValue() == Result.WINNER).map(x -> x.getKey())
						.findFirst().get();
			}

			private Double getMultiplierByResult(Result result) {
				return result == Result.WINNER ? 1
						: result == Result.FINALIST ? 0.5
								: result == Result.SEMIFINALIST ? 0.2 : result == Result.QUARTERFINALIST ? 0.1 : 0.0;
			}

			private Integer getPointsFromTournament(Type type) {
				return type == Type.MAJOR ? 2500 :
						  type == Type.ATP1000 ? 1000 : 
							type == Type.ATP500 ? 500 : 250;
			}

			private Double getFinalValue(String player) {
				return (ranking.containsKey(player) ? ranking.get(player) : 0) + this
						.getMultiplierByResult(results.containsKey(player) ? results.get(player) : Result.PARTICIPANT)
						* this.getPointsFromTournament(type);
			}

			@Override
			public Map<String, Integer> resultingRanking() {
				return players.stream().collect(Collectors.toMap(x -> x, x -> this.getFinalValue(x).intValue()));
			}

			@Override
			public List<String> rank() {
				return this.resultingRanking().entrySet().stream().sorted((x, y) -> y.getValue() - x.getValue())
						.map(x -> x.getKey()).collect(Collectors.toList());
			}

			@Override
			public Map<String, Integer> initialRanking() {
				return ranking;
			}

			@Override
			public Type getType() {
				return type;
			}

			@Override
			public Optional<Result> getResult(String player) {
				return results.containsKey(player) ? Optional.of(results.get(player)) : Optional.empty();
			}

			@Override
			public String getName() {
				return name;
			}
		};
	}

}
