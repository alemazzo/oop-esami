package a06b.e1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MiniAssemblyMachineImpl implements MiniAssemblyMachine {

	private enum Istruction {
		INC, DEC, JNZ, RET
	}

	private List<Pair<Integer, Pair<Istruction, Integer>>> operations = new ArrayList<>();

	@Override
	public void inc(int register) {
		this.operations.add(new Pair<>(register, new Pair<>(Istruction.INC, null)));
	}

	@Override
	public void dec(int register) {
		this.operations.add(new Pair<>(register, new Pair<>(Istruction.DEC, null)));
	}

	@Override
	public void jnz(int register, int target) {
		this.operations.add(new Pair<>(register, new Pair<>(Istruction.JNZ, target)));
	}

	@Override
	public void ret() {
		this.operations.add(new Pair<>(null, new Pair<>(Istruction.RET, null)));
	}

	@Override
	public List<Integer> execute(List<Integer> registers) {

		List<Integer> result = new ArrayList<>(registers);
		Iterator<Pair<Integer, Pair<Istruction, Integer>>> iterator = this.operations.iterator();

		while (iterator.hasNext()) {

			Pair<Integer, Pair<Istruction, Integer>> op = iterator.next();

			switch (op.getY().getX()) {

			case INC:
				result.set(op.getX(), result.get(op.getX()) + 1);
				break;

			case DEC:
				result.set(op.getX(), result.get(op.getX()) - 1);
				break;

			case JNZ:
				if (!result.get(op.getX()).equals(0)) {
					
					final int operationIndex = op.getY().getY();
					
					if (operationIndex >= this.operations.size()) {
						throw new IllegalStateException();
					}
					
					iterator = this.operations.iterator();
					for (int i = 0; i < operationIndex; i++) {
						iterator.next();
					}
				}
				break;
			case RET:
				return result;
			}
		}

		return null;
	}

}
