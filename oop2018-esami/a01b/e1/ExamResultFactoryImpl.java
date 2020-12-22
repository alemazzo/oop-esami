package a01b.e1;

import java.util.Optional;

import a01b.e1.ExamResult.Kind;

public class ExamResultFactoryImpl implements ExamResultFactory {

	@Override
	public ExamResult failed() {
		return new ExamResultImpl(Kind.FAILED);
	}

	@Override
	public ExamResult retired() {
		return new ExamResultImpl(Kind.RETIRED);
	}

	@Override
	public ExamResult succeededCumLaude() {
		return new ExamResultImpl(Kind.SUCCEEDED, 30, true);
	}

	@Override
	public ExamResult succeeded(int evaluation) {
		return new ExamResultImpl(Kind.SUCCEEDED, evaluation);
	
	}

}
