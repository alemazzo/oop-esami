package a01a.e1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class GraphFactoryImpl implements GraphFactory {

	public class BasicGraph<X> implements Graph<X> {

		private Set<Pair<X, X>> edges;

		public BasicGraph(Set<Pair<X, X>> edges) {
			this.edges = edges;
		}

		@Override
		public Set<X> getNodes() {
			return this.edges.stream().flatMap(x -> Stream.of(x.getX(), x.getY())).collect(Collectors.toSet());
		}

		@Override
		public boolean edgePresent(X start, X end) {
			return this.edges.contains(new Pair<X, X>(start, end));
		}

		@Override
		public int getEdgesCount() {
			return this.edges.size();
		}

		@Override
		public Stream<Pair<X, X>> getEdgesStream() {
			return this.edges.stream();
		}
	}

	@Override
	public <X> Graph<X> createDirectedChain(List<X> nodes) {
		Set<Pair<X, X>> edges = IntStream.range(0, nodes.size() - 1)
				.mapToObj(i -> new Pair<X, X>(nodes.get(i), nodes.get(i + 1))).collect(Collectors.toSet());
		return new BasicGraph<>(edges);
	}

	@Override
	public <X> Graph<X> createBidirectionalChain(List<X> nodes) {
		Set<Pair<X, X>> edges = IntStream.range(0, nodes.size() - 1)
				.mapToObj(i -> i)
				.flatMap(i -> Stream.of(new Pair<X, X>(nodes.get(i), nodes.get(i + 1)), new Pair<X, X>(nodes.get(i + 1), nodes.get(i))))
				.collect(Collectors.toSet());
		return new BasicGraph<>(edges);
	}

	@Override
	public <X> Graph<X> createDirectedCircle(List<X> nodes) {
		List<X> allNodes = new ArrayList<>(nodes);
		allNodes.add(nodes.get(0));
		return createDirectedChain(allNodes);
	}

	@Override
	public <X> Graph<X> createBidirectionalCircle(List<X> nodes) {
		List<X> allNodes = new ArrayList<>(nodes);
		allNodes.add(nodes.get(0));
		return createBidirectionalChain(allNodes);
	}

	@Override
	public <X> Graph<X> createDirectedStar(X center, Set<X> nodes) {
		Set<Pair<X, X>> edges = nodes.stream().map(node -> new Pair<X, X>(center, node)).collect(Collectors.toSet());
		return new BasicGraph<>(edges);
	}

	@Override
	public <X> Graph<X> createBidirectionalStar(X center, Set<X> nodes) {
		Set<Pair<X, X>> edges = nodes.stream()
				.flatMap(x -> Stream.of(new Pair<X, X>(center, x), new Pair<X, X>(x, center)))
				.collect(Collectors.toSet());
		return new BasicGraph<>(edges);
	}

	@Override
	public <X> Graph<X> createFull(Set<X> nodes) {
		Set<Pair<X, X>> edges = nodes.stream()
				.flatMap(x -> nodes.stream().filter(y -> y != x).map(y -> new Pair<X, X>(x, y)))
				.collect(Collectors.toSet());
		return new BasicGraph<>(edges);
	}

	@Override
	public <X> Graph<X> combine(Graph<X> g1, Graph<X> g2) {
		Set<Pair<X, X>> edges = g1.getEdgesStream().collect(Collectors.toSet());
		edges.addAll(g2.getEdgesStream().collect(Collectors.toSet()));
		return new BasicGraph<>(edges);
	}
}