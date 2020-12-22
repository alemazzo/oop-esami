package a03b.e1;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.sun.jdi.Value;

public class ConferenceReviewingImpl implements ConferenceReviewing {

	private final Map<Integer, Set<Map<Question, Integer>>> articles = new HashMap<>();
	
	static private final Function<Map<Question, Integer>, Double> NORMAL_FINAL_SCORE = 
			(x -> x.get(Question.FINAL).doubleValue()); 
	
	static private final Function<Map<Question, Integer>, Double> WEIGHTED_FINAL_SCORE = 
			(x -> x.get(Question.CONFIDENCE).doubleValue() * x.get(Question.FINAL).doubleValue() / 10.0); 
	

	
	
	@Override
	public void loadReview(int article, Map<Question, Integer> scores) {
		if (!this.articles.containsKey(article)) {
			this.articles.put(article, new HashSet<>());
		}
		this.articles.get(article).add(scores);

	}

	@Override
	public void loadReview(int article, int relevance, int significance, int confidence, int fin) {
		final Map<Question, Integer> valuesMap = new HashMap<>();
		
		valuesMap.put(Question.RELEVANCE, relevance);
		valuesMap.put(Question.SIGNIFICANCE, significance);
		valuesMap.put(Question.CONFIDENCE, confidence);
		valuesMap.put(Question.FINAL, fin);
		this.loadReview(article, valuesMap);
		
	}
	
	

	@Override
	public List<Integer> orderedScores(int article, Question question) {
		return this.articles.get(article).stream()
				.map(x -> x.get(question))
				.sorted(Integer::compare)
				.collect(Collectors.toList());
	}
	
	


	private double calculateScore(int article, Function<Map<Question, Integer>, Double> func) {
		return this.articles.get(article).stream()
				.mapToDouble(x -> func.apply(x))
				.average().getAsDouble();
	}
	
	
	@Override
	public double averageFinalScore(int article) {
		return this.calculateScore(article, NORMAL_FINAL_SCORE);
	}


	@Override
	public Set<Integer> acceptedArticles() {
		return this.articles.entrySet().stream()
				.filter(x -> averageFinalScore(x.getKey()) >= 5.0)
				.filter(x-> orderedScores(x.getKey(), Question.RELEVANCE).stream().max(Integer::compare).get() >= 8.0 )
				.map(Map.Entry::getKey)
				.collect(Collectors.toSet());
	}

	@Override
	public List<Pair<Integer, Double>> sortedAcceptedArticles() {
		return this.acceptedArticles().stream()
				.sorted()
				.map(x -> new Pair<>(x, this.averageFinalScore(x)))
				.collect(Collectors.toList());
	}

	
	@Override
	public Map<Integer, Double> averageWeightedFinalScoreMap() {
		return this.articles.keySet().stream()
				.map(x -> new Pair<>(x, calculateScore(x, WEIGHTED_FINAL_SCORE)))
				.collect(Collectors.toMap(Pair::getX, Pair::getY));
			
	}

}
