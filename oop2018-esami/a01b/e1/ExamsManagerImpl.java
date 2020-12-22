package a01b.e1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ExamsManagerImpl implements ExamsManager {

	private Map<String, Map<String, ExamResult>> exams = new HashMap<>();

	@Override
	public void createNewCall(String call) {
		if (this.exams.containsKey(call)) {
			throw new IllegalArgumentException();
		}
		this.exams.put(call, new HashMap<>());

	}

	@Override
	public void addStudentResult(String call, String student, ExamResult result) {
		if (this.exams.get(call).containsKey(student)) {
			throw new IllegalArgumentException();
		}
		this.exams.get(call).put(student, result);

	}

	@Override
	public Set<String> getAllStudentsFromCall(String call) {
		return Set.copyOf(this.exams.get(call).keySet());
	}

	@Override
	public Map<String, Integer> getEvaluationsMapFromCall(String call) {

		return this.exams.get(call).entrySet().stream().filter(x -> x.getValue().getKind() == ExamResult.Kind.SUCCEEDED)
				.map(x -> Map.entry(x.getKey(), x.getValue().getEvaluation().get()))
				.collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));

	}

	@Override
	public Map<String, String> getResultsMapFromStudent(String student) {
		
		return this.exams.entrySet().stream()
			.filter(x -> x.getValue().containsKey(student))
			.collect(Collectors.toMap(x->x.getKey(), x->x.getValue().get(student).toString()));

	}

	@Override
	public Optional<Integer> getBestResultFromStudent(String student) {
		List<Integer> resultsList = new ArrayList<>();

		this.exams.entrySet().forEach(x -> {
			x.getValue().entrySet().forEach(e -> {
				if (e.getKey().equals(student)) {
					if (e.getValue().getEvaluation().isPresent()) {
						resultsList.add(e.getValue().getEvaluation().get());
					}
				}
			});
		});

		return resultsList.size() == 0 ? Optional.empty() : Optional.of(Collections.max(resultsList));
	}

}
