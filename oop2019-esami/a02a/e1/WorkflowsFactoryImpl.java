package a02a.e1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class WorkflowsFactoryImpl implements WorkflowsFactory {

	private class BasicWorkflow<T> implements Workflow<T>{
		
		private List<Set<T>> tasks = new ArrayList<>();
		
		public BasicWorkflow(List<Set<T>> tasks) {
			this.tasks = tasks.stream()
					.map(x -> new HashSet<T>(x))
					.collect(Collectors.toList());
		}
		
		public List<Set<T>> getSequentialTasks(){
			return new ArrayList<>(this.tasks);
		}
		@Override
		public Set<T> getTasks() {
			return this.tasks.stream()
					.flatMap(x -> x.stream())
					.collect(Collectors.toSet());
		}

		@Override
		public Set<T> getNextTasksToDo() {
			return this.isCompleted() ? Set.of() : this.tasks.get(0);
		}

		@Override
		public void doTask(T t) {

			if(!this.getTasks().contains(t)) {
				throw new IllegalArgumentException();
			}
			if(!this.getNextTasksToDo().contains(t)) {
				throw new IllegalStateException();
			}
			
			this.tasks.get(0).remove(t);
			if(this.tasks.get(0).size() == 0) {
				this.tasks.remove(0);
			}
		}

		@Override
		public boolean isCompleted() {
			return this.tasks.size() == 0;
		}
	
	}
	
	@Override
	public <T> Workflow<T> singleTask(T task) {
		return new BasicWorkflow<>(List.of(Set.of(task)));
	}

	@Override
	public <T> Workflow<T> tasksSequence(List<T> tasks) {
		List<Set<T>> allTasks = tasks.stream()
				.map(x -> new HashSet<T>(Set.of(x)))
				.collect(Collectors.toList());
		return new BasicWorkflow<>(allTasks);
	}

	@Override
	public <T> Workflow<T> tasksJoin(Set<T> initialTasks, T finalTask) {
		return new BasicWorkflow<>(List.of(initialTasks, Set.of(finalTask)));
	}

	@Override
	public <T> Workflow<T> tasksFork(T initialTask, Set<T> finalTasks) {
		return new BasicWorkflow<>(List.of(Set.of(initialTask), finalTasks));
	}

	@Override
	public <T> Workflow<T> concat(Workflow<T> first, Workflow<T> second) {
		List<Set<T>> allTasks = ((BasicWorkflow<T>)first).getSequentialTasks();
		allTasks.addAll( ((BasicWorkflow<T>)second).getSequentialTasks());
		return new BasicWorkflow<>(allTasks);
	}

}
