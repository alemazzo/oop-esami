package a03a.e1;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import a03a.e1.Evaluation.Question;
import a03a.e1.Evaluation.Result;

public class EvaluationBuilderImpl implements EvaluationBuilder {

	private boolean build = false;
	Map<String, Map<Integer, Map<Question, Result>>> courses = new HashMap<>();

	private void checkValidity(String course, int student) {
		if (!this.courses.containsKey(course)) {
			this.courses.put(course, new HashMap<>());
		}
		if (!this.courses.get(course).containsKey(student)) {
			this.courses.get(course).put(student, new HashMap<>());
		} else {
			throw new IllegalStateException();
		}
	}

	@Override
	public EvaluationBuilder addEvaluationByMap(String course, int student, Map<Question, Result> results) {
		this.checkValidity(course, student);
		if (results.size() < 3) {
			throw new IllegalArgumentException();
		}
		this.courses.get(course).put(student, results);
		return this;
	}

	@Override
	public EvaluationBuilder addEvaluationByResults(String course, int student, Result resOverall, Result resInterest,
			Result resClarity) {
		this.checkValidity(course, student);

		this.courses.get(course).get(student).put(Question.OVERALL, resOverall);
		this.courses.get(course).get(student).put(Question.INTEREST, resInterest);
		this.courses.get(course).get(student).put(Question.CLARITY, resClarity);
		return this;

	}

	@Override
	public Evaluation build() {
		if (build) {
			throw new IllegalStateException();
		}

		build = true;
		return new Evaluation() {

			@Override
			public Map<Question, Result> results(String course, int student) {
				return Collections.unmodifiableMap(courses.get(course).getOrDefault(student, new HashMap<>()));
			}
			

			@Override
			public Map<Result, Long> resultsCountForCourseAndQuestion(String course, Question questions) {	
				return courses.get(course).values().stream()
						.map(x -> x.get(questions))
						.collect(Collectors.groupingBy(x->x, Collectors.counting()));
			}

			
			@Override
			public Map<Result, Long> resultsCountForStudent(int student) {
				return courses.values().stream()
						.filter(x -> x.containsKey(student))
						.flatMap(x -> x.get(student).values().stream())
						.collect(Collectors.groupingBy(x->x, Collectors.counting()));
			}

			@Override
			public double coursePositiveResultsRatio(String course, Question question) {
				return (double) courses.get(course).values().stream()
						.map(x -> x.get(question))
						.filter(x -> x.equals(Result.FULLY_POSITIVE) || x.equals(Result.WEAKLY_POSITIVE))
						.count() / (double) courses.get(course).values().size();

			}
		};
	}

}
