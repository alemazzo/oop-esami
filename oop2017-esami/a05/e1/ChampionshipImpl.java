package a05.e1;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChampionshipImpl implements Championship {

	private List<String> names = new ArrayList<>();
	private Map<String, Integer> classification = new HashMap<>();
	private Iterator<List<String>> allMatches;
	private Set<Match> matches = new HashSet<>();

	@Override
	public void registerTeam(String name) {
		this.classification.put(name, 0);
		names.add(name);
	}

	@Override
	public void startChampionship() {
		this.allMatches = Combinations.combine(this.names).iterator();
	}

	@Override
	public void newDay() {
		this.matches = Stream.generate(() -> this.allMatches.next()).limit(this.names.size() / 2)
				.map(x -> new MatchImpl(x.get(0), x.get(1))).collect(Collectors.toSet());
	}

	@Override
	public Set<Match> pendingMatches() {
		return this.matches;
	}

	@Override
	public void matchPlay(Match match, int homeGoals, int awayGoals) {
		if(homeGoals > awayGoals) {
			this.classification.replace(match.getHomeTeam(), this.classification.get(match.getHomeTeam()) + 3);
		}else if(homeGoals < awayGoals) {
			this.classification.replace(match.getAwayTeam(), this.classification.get(match.getAwayTeam()) + 3);
		}else {
			this.classification.replace(match.getHomeTeam(), this.classification.get(match.getHomeTeam()) + 1);
			this.classification.replace(match.getAwayTeam(), this.classification.get(match.getAwayTeam()) + 1);
		}
		this.matches.remove(match);
	}

	@Override
	public Map<String, Integer> getClassification() {
		return Collections.unmodifiableMap(this.classification);
	}

	@Override
	public boolean championshipOver() {
		return !this.allMatches.hasNext() && this.matches.size() == 0; 
	}

}
