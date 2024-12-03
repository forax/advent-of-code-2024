void main() {
  var text = """
      7 6 4 2 1
      1 2 7 8 9
      9 7 6 2 1
      1 3 2 4 5
      8 6 4 4 1
      1 3 6 7 9
      """;

  println(text.lines().filter(l -> Arrays.stream(l.split(" ")).map(Integer::parseInt)
      .gather(Gatherers.windowSliding(2))
      .map(pair -> switch (pair.getFirst() - pair.getLast()) {
        case -3, -2, -1 -> "ascending";
        case 1, 2, 3 -> "decending";
        default -> "invalid";
      })
      .reduce((s, s2) -> s != s2 ? "invalid" : s)
      .map(v -> v != "invalid").orElse(true)).count());
}