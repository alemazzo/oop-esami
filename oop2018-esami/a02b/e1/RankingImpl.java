package a02b.e1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RankingImpl implements Ranking {

	private List<Tournament> tournaments = new ArrayList<>();

	@Override
	public void loadTournament(Tournament tournament) {
		if(this.tournaments.contains(tournament)) {
			throw new IllegalStateException();
		}
		if(this.tournaments.size() >= 1 && isOlder(tournament, this.tournaments.get(this.tournaments.size() - 1))) {
			throw new IllegalStateException();
		}
		tournaments.add(tournament);
	}
	
	private boolean isOlder(Tournament t1, Tournament t2) {
		if (t1.getYear() < t2.getYear()) return true;
		if (t1.getYear() == t2.getYear() && t1.getWeek() < t2.getWeek()) return true;
		return false;
	}

	@Override
	public int getCurrentWeek() {
		if (this.tournaments.size() == 0) {
			throw new IllegalStateException();
		}
		return this.tournaments.get(this.tournaments.size() - 1).getWeek();
	}

	@Override
	public int getCurrentYear() {
		if (this.tournaments.size() == 0) {
			throw new IllegalStateException();
		}
		return this.tournaments.get(this.tournaments.size() - 1).getYear();
	}

	private Stream<Tournament> getLastYearTournaments(){
		return this.tournaments.stream()
				.filter(x -> getCurrentYear() - x.getYear() < 1 || (getCurrentYear() - x.getYear() == 1 && getCurrentWeek() < x.getWeek()));
				
	}
	@Override
	public Integer pointsFromPlayer(String player) {
		Optional<Integer> res = getLastYearTournaments()
				.filter(x -> x.getPlayers().contains(player))
				.map(x -> x.getResult(player).get())
				.reduce((x, y) -> x + y);
		return res.isEmpty() ? 0 : res.get();

	}

	@Override
	public List<String> ranking() {			
			
		Map<String, Integer> rankingMap = new HashMap<>();
		
		this.tournaments.stream()
			.map(x -> x.getPlayers())
			.forEach(x -> {
				x.forEach(player -> {
					rankingMap.put(player,  pointsFromPlayer(player));
				});
			});
		return rankingMap.entrySet().stream()
				.sorted((x, y) -> y.getValue() - x.getValue())
				.map(x -> x.getKey())
				.collect(Collectors.toList());
	}

	@Override
	public Map<String, String> winnersFromTournamentInLastYear() {
		return getLastYearTournaments()
				.collect(Collectors.toMap(x-> x.getName(), x-> x.winner()));
	}

	@Override
	public Map<String, Integer> pointsAtEachTournamentFromPlayer(String player) {
		return this.tournaments.stream()
				.filter(x -> x.getPlayers().contains(player))
				.collect(Collectors.toMap(x-> x.getName(), x-> x.getResult(player).get()));
	}


	@Override
	public List<Pair<String, Integer>> pointsAtEachTournamentFromPlayerSorted(String player) {
		return this.tournaments
				.stream()
				.filter(x -> x.getPlayers().contains(player))
				.sorted((x, y) -> ( (x.getYear() * 54) + x.getWeek() ) - ( (y.getYear() * 54) + y.getWeek() ) )
				.map(x -> new Pair<>(x.getName(), x.getResult(player).get()))
				.collect(Collectors.toList());
	}

}






