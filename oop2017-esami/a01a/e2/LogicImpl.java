package a01a.e2;

import java.util.ArrayList;
import java.util.List;

public class LogicImpl implements Logic {

	private int size;
	private List<Integer> values = new ArrayList<>();
	
	public LogicImpl(int size) {
		this.size = size;
		for(int i = 0; i < size; i++) {
			this.values.add(0);
		}
	}

	@Override
	public int hit(int index) {
		if(this.isDisabled(index)) {
			return this.values.get(index);
		}
		this.values.set(index, this.values.get(index) + 1);
		return this.values.get(index);
	}
	
	

	@Override
	public void print() {
		StringBuilder sb = new StringBuilder("<<");
		for(int i = 0; i < values.size(); i++) {
			sb.append(this.values.get(i));
			if(i != values.size() - 1) {
				sb.append("|");
			}
		}
		sb.append(">>");
		System.out.println(sb.toString());
	}

	@Override
	public boolean isDisabled(int indexOf) {
		return this.values.get(indexOf) == this.size;
	}
	
	@Override
	public boolean isGameOver() {
		return this.values.stream().distinct().count() == 1;
	}

}
