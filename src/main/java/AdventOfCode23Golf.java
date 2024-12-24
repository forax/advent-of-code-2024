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

  var pair = input.lines()
      .map(l -> Set.of(l.split("-")))
      .collect(Collectors.teeing(
          Collectors.flatMapping(Set::stream, Collectors.filtering(node -> node.contains("t"), Collectors.toSet())),
          Collectors.toSet(),
          Map::entry));
  println(pair.getKey().stream()
      .flatMap(node -> pair.getValue().stream()
          .filter(edge -> !edge.contains(node))
          .map(List::copyOf)
          .filter(edge -> edge.stream().allMatch(n -> pair.getValue().contains(Set.of(node, n))))
          .map(edge -> Set.of(node, edge.getFirst(), edge.getLast())))
      .distinct().count());
}
