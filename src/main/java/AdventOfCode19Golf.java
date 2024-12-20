void main() {
  var input = """
      r, wr, b, g, bwu, rb, gb, br
      
      brwrr
      bggr
      gbbr
      rrbgbr
      ubwu
      bwurrg
      brgr
      bbrgwb
      """;

  class Node {
    final HashMap<Integer, Node> map = new HashMap<>();
    boolean end;
  }
  var root = new Node();
  Arrays.stream(input.substring(0, input.indexOf('\n')).split(", "))
    .forEach(w -> w.chars().boxed().gather(Gatherers.fold(() -> root,
          (n, c) -> n.map.computeIfAbsent(c, _ -> new Node()))).findFirst().orElseThrow().end = true);
  println(input.lines().skip(2).filter(w ->
      w.chars().boxed().<Set<Node>>gather(Gatherers.fold(() -> new HashSet<>(Set.of(root)),
        (ms, c) -> ms.stream().flatMap(m -> Stream.ofNullable(m.map.get(c)))
          .flatMap(n -> n != null && n.end ? Stream.of(n, root) : Stream.ofNullable(n)).collect(Collectors.toSet())))
        .findFirst().orElseThrow().stream().anyMatch(n -> n.end)).count());
}
