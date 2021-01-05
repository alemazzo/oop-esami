package a02b.e1;

import java.util.Set;

public class ExamsFactoryImpl implements ExamsFactory {

	@Override
	public CourseExam<SimpleExamActivities> simpleExam() {
		return new CourseExam<ExamsFactory.SimpleExamActivities>() {
			
			@Override
			public Set<SimpleExamActivities> getPendingActivities() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public boolean examOver() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void completed(SimpleExamActivities a) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public Set<SimpleExamActivities> activities() {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}

	@Override
	public CourseExam<OOPExamActivities> simpleOopExam() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CourseExam<OOPExamActivities> fullOopExam() {
		// TODO Auto-generated method stub
		return null;
	}

}
