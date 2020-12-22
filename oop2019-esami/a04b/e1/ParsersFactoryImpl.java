package a04b.e1;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class ParsersFactoryImpl implements ParsersFactory {

	private class BaseParser<R> implements Parser<R>{

		private R result;
		private boolean canBeEmpty;
		private boolean end = false;
		private List<String> tokens = new ArrayList<>();
		private BiFunction<BaseParser<R>, String, Boolean> func;
		
		public BaseParser(R start, boolean canBeEmpty, BiFunction<BaseParser<R>, String, Boolean> getNext) {
			this.result = start;
			this.canBeEmpty = canBeEmpty;
			this.func = getNext;
		}
		@Override
		public boolean getNext(String token) {
			return this.end ? false : this.func.apply(this, token);
		}

		@Override
		public boolean getAllInList(List<String> tokens) {
			return this.end ? false : tokens.stream().map(this::getNext).allMatch(x -> x);
		}

		@Override
		public R completeAndCreateResult() {
			if (this.end || (!this.canBeEmpty && this.result == null)) {
				throw new IllegalStateException();
			}
			this.end = true;
			return this.result;
		}
		
	}
	@Override
	public Parser<Integer> createSequenceParserToCount(String t) {
		
		return new BaseParser<Integer>(0, true, (parser, token) -> {
			if (!token.equals(t)) {
				parser.end = true;
				return false;
			}
			if(parser.result == null) {
				parser.result = 0;
			}
			parser.result = (int)parser.result + 1;
			return true;
		});
	}

	@Override
	public Parser<String> createNonEmptySequenceParserToString(String t) {
		return new BaseParser<String>(null, false, (parser, token) -> {
			if(token.length() > 1) {
				return parser.getAllInList(List.of(token.split("")));
			}
			if (!token.equals(t)) {
				parser.end = true;
				return false;
			}
			parser.result = parser.result == null ? "" : parser.result;
			parser.result += token;
			return true;
		});
	}

	@Override
	public Parser<Integer> createExpressionParserToResult() {
		return new BaseParser<Integer>(0, true, (parser, token) -> {
			String lastToken = parser.tokens.size() == 0 ? "+" : parser.tokens.get(parser.tokens.size() - 1);
			
			if (token.equals("0") || token.equals("1")) {
				if (lastToken != "+" && lastToken != "-") {
					parser.end = true;
					return false;
				} else {
					parser.result = (int)parser.result + (int)(lastToken.equals("+") ? Integer.parseInt(token) : -Integer.parseInt(token));
					parser.tokens.add(token);
					return true;
				}
			} else {
				if (lastToken != "1" && lastToken != "0") {
					parser.end = true;
					return false;
				} else {
					parser.tokens.add(token);
					return true;
				}
			}
		});
	}
}
