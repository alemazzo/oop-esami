package a06.e1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ParserFactoryImpl implements ParserFactory {

	private class MultipleParser implements Parser {

		private List<String> tokens;
		private int index = 0;

		public MultipleParser(final List<String> tokens) {
			this.tokens = tokens;
		}

		@Override
		public boolean acceptToken(String token) {
			if (this.inputCompleted()) {
				return false;
			} else {
				if (this.tokens.get(index).equals(token)) {
					this.index++;
					return true;
				} else {
					return false;
				}
			}
		}

		@Override
		public boolean inputCompleted() {
			return this.index == this.tokens.size();
		}

		@Override
		public void reset() {
			this.index = 0;
		}

	}

	private class AnyParser implements Parser {

		private Set<Parser> parsers = new HashSet<>();
		private final int limit;
		
		private int count;

		public AnyParser(final Set<String> tokens, final int limit) {
			
			this.limit = limit;
			tokens.forEach(elem -> {
				this.parsers.add(new MultipleParser(List.of(elem)));
			});
		}

		@Override
		public boolean acceptToken(String token) {
			if(this.inputCompleted()) {
				return false;
			}
			if(this.parsers.stream().anyMatch(x -> x.acceptToken(token))){
				this.count++;
				return true;
			}else {
				return false;
			}
		}

		@Override
		public boolean inputCompleted() {
			return this.count == this.limit;
		}

		@Override
		public void reset() {
			this.parsers.forEach(p -> p.reset());
			this.count = 0;
		}

	}

	@Override
	public Parser one(String token) {
		return new MultipleParser(List.of(token));
	}

	@Override
	public Parser many(String token, int elemCount) {
		List<String> tokens = Stream.generate(() -> token).limit(elemCount).collect(Collectors.toList());
		return new MultipleParser(tokens);
	}

	@Override
	public Parser oneOf(Set<String> set) {		
		return new AnyParser(set, 1);
	}
		

	@Override
	public Parser sequence(String token1, String token2) {
		return new MultipleParser(List.of(token1, token2));
	}

	@Override
	public Parser fullSequence(String begin, Set<String> elem, String separator, String end, int elemCount) {
		
		return new Parser() {

			private MultipleParser beginParser = new MultipleParser(List.of(begin));
			private MultipleParser endParser = new MultipleParser(List.of(end));
			private AnyParser middleParser = new AnyParser(elem, elemCount);
			
			private List<Parser> parsers = new ArrayList<Parser>(beginParser, middleParser, endParser);
			
			@Override
			public void reset() {
				beginParser = new MultipleParser(List.of(begin));
				middleParser = new AnyParser(elem, elemCount);
				endParser = new MultipleParser(List.of(end));
			}
			
			@Override
			public boolean inputCompleted() {
				return endParser.inputCompleted();
			}
			
			@Override
			public boolean acceptToken(String token) {
				
				if(this.inputCompleted()) {
					return false;
				}
				
				if(!this.beginParser.inputCompleted()) {
					return beginParser.acceptToken(token);
				}
				
				if(this.middleParser.inputCompleted() && this.endParser.acceptToken(token)) {
					return true;
				}
				
				if(this.middleParser.inputCompleted()) {
					if(token == separator) {
						return true;
					}else {
						return false;
					}
				}else {
					return this.middleParser.acceptToken(token);
				}
			}
		};

	}

}
