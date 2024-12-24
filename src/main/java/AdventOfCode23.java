record Edge(String n1, String n2) {
  @Override
  public boolean equals(Object obj) {
    return obj instanceof Edge edge && Set.of(n1, n2).equals(Set.of(edge.n1, edge.n2));
  }

  @Override
  public int hashCode() {
    return n1.hashCode() ^ n2.hashCode();
  }
}

record Triplet(String n1, String n2, String n3) {
  @Override
  public boolean equals(Object obj) {
    return obj instanceof Triplet triplet && Set.of(n1, n2, n3).equals(Set.of(triplet.n1, triplet.n2, triplet.n3));
  }

  @Override
  public int hashCode() {
    return n1.hashCode() ^ n2.hashCode() ^ n3.hashCode();
  }
}

Set<Edge> edges(String input) {
  return input.lines()
      .map(l -> {
        var array = l.split("-");
        return new Edge(array[0], array[1]);
      })
      .collect(Collectors.toSet());
}

Set<String> nodes(Set<Edge> edges) {
  return edges.stream()
      .flatMap(edge -> Stream.of(edge.n1, edge.n2))
      .collect(Collectors.toSet());
}

Set<Triplet> triplets(Set<String> nodes, Set<Edge> edges) {
  return nodes.stream()
      .flatMap(node -> edges.stream()
          .filter(edge -> (edges.contains(new Edge(node, edge.n1)) && edges.contains(new Edge(node, edge.n2))))
          .map(edge -> new Triplet(node, edge.n1, edge.n2)))
      .collect(Collectors.toSet());
}

long countTriplet(Set<Triplet> triplets, Predicate<String> filter) {
  return triplets.stream()
      .filter(triplet -> filter.test(triplet.n1) ||  filter.test(triplet.n2) || filter.test(triplet.n3))
      .count();
}

void main() {
  var input = """
      kh-tc
      qp-kh
      de-cg
      ka-co
      yn-aq
      qp-ub
      cg-tb
      vc-aq
      tb-ka
      wh-tc
      yn-cg
      kh-ub
      ta-co
      de-co
      tc-td
      tb-wq
      wh-td
      ta-ka
      td-qp
      aq-cg
      wq-ub
      ub-vc
      de-ta
      wq-aq
      wq-vc
      wh-yn
      ka-de
      kh-ta
      co-tc
      wh-qp
      tb-vc
      td-yn
      """;

  var edges = edges(input);
  var nodes = nodes(edges);
  var triplet = triplets(nodes, edges);
  println(countTriplet(triplet, node -> node.contains("t")));
}
