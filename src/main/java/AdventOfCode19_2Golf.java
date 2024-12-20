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

  var stripes = Arrays.stream(input.substring(0, input.indexOf('\n')).split(", ")).toList();
  record Match(String strip, int at) {
    boolean end() { return at == strip.length(); }
    Stream<Match> next(int l) { return strip.charAt(at) == l ? Stream.of(new Match(strip, at + 1)) : null; }
  }
  var initMatches = stripes.stream().map(s -> new Match(s, 0)).collect(Collectors.toSet());
  println(input.lines().skip(2).filter(w -> w.chars().boxed()
      .gather(Gatherers.fold(() -> initMatches,
          (matches, c) -> matches.stream()
              .flatMap(m -> m.end() ? initMatches.stream(): Stream.of(m))
              .flatMap(m -> m.next(c))
              .collect(Collectors.toSet())))
      .findFirst().orElseThrow().stream().anyMatch(Match::end)).count());
}
