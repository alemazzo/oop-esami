package a01b.e1;

import java.util.Optional;

public class ExamResultImpl implements ExamResult {

	private boolean laude;
	private Integer evaluation;
	private final Kind kind;

	public ExamResultImpl(final Kind kind, final int evaluation, final boolean laude) {
		if(kind == Kind.SUCCEEDED && (evaluation > 30 || evaluation < 18)) {
			throw new IllegalArgumentException();
		}
		this.kind = kind;
		this.evaluation = evaluation;
		this.laude = laude;
	}
	
	public ExamResultImpl(final Kind kind, final int evaluation) {
		this(kind, evaluation, false);
	}
	

	public ExamResultImpl(final Kind kind) {
		this.kind = kind;
	}
	

	@Override
	public Kind getKind() {
		return this.kind;
	}

	@Override
	public Optional<Integer> getEvaluation() {
		return Optional.ofNullable(this.evaluation);
	}

	@Override
	public boolean cumLaude() {
		return this.laude;
	}

	@Override
	public String toString() {
		return this.kind.equals(Kind.RETIRED) ? "RETIRED"
				: this.kind == Kind.FAILED ? "FAILED" : "SUCCEEDED(" + this.evaluation + (this.laude ? "L" : "") + ")";
	}

}
