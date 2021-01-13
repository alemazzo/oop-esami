package a02b.e1;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ExamsFactoryImpl implements ExamsFactory {

	@Override
	public CourseExam<SimpleExamActivities> simpleExam() {
		return new CourseExam<ExamsFactory.SimpleExamActivities>() {

			private List<SimpleExamActivities> activities = List.of(SimpleExamActivities.values());

			private int activityIndex = 0;

			@Override
			public Set<SimpleExamActivities> getPendingActivities() {
				return this.examOver() ? Set.of() : Set.of(this.activities.get(this.activityIndex));
			}

			@Override
			public boolean examOver() {
				return this.activityIndex == this.activities.size();
			}

			@Override
			public void completed(SimpleExamActivities a) {
				if (this.activities.get(this.activityIndex).equals(a)) {
					this.activityIndex++;
				}
			}

			@Override
			public Set<SimpleExamActivities> activities() {
				return new HashSet<>(List.of(SimpleExamActivities.WRITTEN, SimpleExamActivities.ORAL,
						SimpleExamActivities.REGISTER));
			}
		};
	}

	@Override
	public CourseExam<OOPExamActivities> simpleOopExam() {

		return new CourseExam<ExamsFactory.OOPExamActivities>() {

			private List<OOPExamActivities> labActivities = List.of(OOPExamActivities.LAB_REGISTER,
					OOPExamActivities.LAB_EXAM);
			private List<OOPExamActivities> projectActivities = List.of(OOPExamActivities.PROJ_PROPOSE,
					OOPExamActivities.PROJ_SUBMIT, OOPExamActivities.PROJ_EXAM);

			private boolean completedEvaluation = false;

			private int labActivityIndex = 0;
			private int projActivityIndex = 0;

			@Override
			public Set<OOPExamActivities> getPendingActivities() {
				Set<OOPExamActivities> pending = new HashSet<>();
				if(this.labActivityIndex < this.labActivities.size()) {
					pending.add(this.labActivities.get(this.labActivityIndex));
				}
				if(this.projActivityIndex < this.projectActivities.size()) {
					pending.add(this.projectActivities.get(this.projActivityIndex));
				}
				if(!this.examOver() && this.labActivityIndex == this.labActivities.size() && this.projActivityIndex == this.projectActivities.size()) {
					pending.add(OOPExamActivities.FINAL_EVALUATION);
				}
				return pending;
			}

			@Override
			public boolean examOver() {
				return this.completedEvaluation;
			}

			@Override
			public void completed(OOPExamActivities a) {
				if (this.labActivityIndex < this.labActivities.size() &&  this.labActivities.get(this.labActivityIndex).equals(a)) {
					this.labActivityIndex++;
				} else if (this.projActivityIndex < this.projectActivities.size() && this.projectActivities.get(this.projActivityIndex).equals(a)) {
					this.projActivityIndex++;
				} else {
					if(a.equals(OOPExamActivities.FINAL_EVALUATION))
					this.completedEvaluation = true;
				}
			}

			@Override
			public Set<OOPExamActivities> activities() {
				return Set.of(OOPExamActivities.LAB_REGISTER, OOPExamActivities.LAB_EXAM,
						OOPExamActivities.PROJ_PROPOSE, OOPExamActivities.PROJ_SUBMIT, OOPExamActivities.PROJ_EXAM,
						OOPExamActivities.FINAL_EVALUATION);
			}
		};
	}

	@Override
	public CourseExam<OOPExamActivities> fullOopExam() {
		// TODO Auto-generated method stub
		return null;
	}

}
