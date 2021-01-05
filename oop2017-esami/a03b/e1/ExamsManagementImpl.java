package a03b.e1;

import java.util.*;
import java.util.stream.Collectors;

public class ExamsManagementImpl implements ExamsManagement {

	Map<Integer, String> students = new HashMap<>();
	Map<Integer, Map<Integer, Integer>> labEvaluations = new HashMap<>();
	Map<Integer, Pair<String, Integer>> projectEvaluations = new HashMap<>();

	@Override
	public void createStudent(int studentId, String name) {
		if (this.students.containsKey(studentId)) {
			throw new IllegalStateException();
		}
		this.students.put(studentId, name);
	}

	@Override
	public void registerLabEvaluation(int studentId, int evaluation, int exam) {
		if (!this.students.containsKey(studentId) || (this.labEvaluations.containsKey(studentId)
				&& this.labEvaluations.get(studentId).containsKey(exam))) {
			throw new IllegalStateException();
		}
		
		if (!this.labEvaluations.containsKey(studentId)) {
			this.labEvaluations.put(studentId, new HashMap<>());
		}

		this.labEvaluations.get(studentId).put(exam, evaluation);
	}

	@Override
	public void registerProjectEvaluation(int studentId, int evaluation, String project) {
		if (!this.students.containsKey(studentId) || this.projectEvaluations.containsKey(studentId)) {
			throw new IllegalStateException();
		}
		this.projectEvaluations.put(studentId, new Pair<>(project, evaluation));
	}

	@Override
	public Optional<Integer> finalEvaluation(int studentId) {
		if (this.labEvaluations.containsKey(studentId) && this.projectEvaluations.containsKey(studentId)) {
			final int lab = this.labEvaluations.get(studentId).values().stream().reduce((x, y) -> y).get();
			final int project = this.projectEvaluations.get(studentId).getY();
			return Optional.of((int) Math.round(Math.max(lab, project) * 0.6 + Math.min(lab, project) * 0.4));
		} else {
			return Optional.empty();
		}
	}

	@Override
	public Map<String, Integer> labExamStudentToEvaluation(int exam) {
		return this.labEvaluations.entrySet().stream().filter(x -> x.getValue().containsKey(exam))
				.map(x -> new Pair<>(this.students.get(x.getKey()), x.getValue().get(exam)))
				.collect(Collectors.toMap(x -> x.getX(), x -> x.getY()));
	}

	@Override
	public Map<String, Integer> allLabExamStudentToFinalEvaluation() {
		return this.students.entrySet().stream()
				.map(x -> new Pair<>(this.students.get(x.getKey()), this.finalEvaluation(x.getKey())))
				.filter(x -> x.getY().isPresent()).collect(Collectors.toMap(x -> x.getX(), x -> x.getY().get()));
	}

	@Override
	public Map<String, Integer> projectEvaluation(String project) {
		return this.projectEvaluations.entrySet().stream().filter(x -> x.getValue().getX().equals(project))
				.map(x -> new Pair<>(this.students.get(x.getKey()), x.getValue().getY()))
				.collect(Collectors.toMap(x -> x.getX(), x -> x.getY()));

	}

}
