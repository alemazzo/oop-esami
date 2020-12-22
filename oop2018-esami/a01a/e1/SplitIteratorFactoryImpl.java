package a01a.e1;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class SplitIteratorFactoryImpl implements SplitIteratorFactory {

	@Override
	public SplitIterator<Integer> fromRange(int start, int stop) {
		List<Integer> elements = new ArrayList<>();
		while (start <= stop) {
			elements.add(start);
			start++;
		}		
		return new SplitIteratorImpl<>(SplitIteratorImpl.SPLITTABLE, elements);
	}

	@Override
	public SplitIterator<Integer> fromRangeNoSplit(int start, int stop) {
		List<Integer> elements = new ArrayList<>();
		while (start <= stop) {
			elements.add(start);
			start++;
		}		
		return new SplitIteratorImpl<>(SplitIteratorImpl.UNSPLITTABLE, elements);
	}

	@Override
	public <X> SplitIterator<X> fromList(List<X> list) {
		return new SplitIteratorImpl<>(SplitIteratorImpl.SPLITTABLE, list);
	}

	@Override
	public <X> SplitIterator<X> fromListNoSplit(List<X> list) {
		return new SplitIteratorImpl<>(SplitIteratorImpl.UNSPLITTABLE, list);
	}

	@Override
	public <X> SplitIterator<X> excludeFirst(SplitIterator<X> si) {
		List<X> res = new ArrayList<>(); 
		si.next();
		Optional<X> elem;
		while((elem = si.next()) != Optional.empty()){
			res.add(elem.get());
		}
		
		return new SplitIteratorImpl<>(res);
	}

	@Override
	public <X, Y> SplitIterator<Y> map(SplitIterator<X> si, Function<X, Y> mapper) {
		List<Y> elements = new ArrayList<>();
		Optional<X> elemX = si.next();
		while(elemX != Optional.empty()) {
			elements.add(mapper.apply(elemX.get()));
			elemX = si.next();
		}
		return new SplitIteratorImpl<>(SplitIteratorImpl.SPLITTABLE, elements);
	}

}
