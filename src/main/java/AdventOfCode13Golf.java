void main() {
  var input = """
      Button A: X+94, Y+34
      Button B: X+22, Y+67
      Prize: X=8400, Y=5400
      
      Button A: X+26, Y+66
      Button B: X+67, Y+21
      Prize: X=12748, Y=12176
      
      Button A: X+17, Y+86
      Button B: X+84, Y+37
      Prize: X=7870, Y=6450
      
      Button A: X+69, Y+23
      Button B: X+27, Y+71
      Prize: X=18641, Y=10279
      """;

  var pattern = Pattern.compile("(\\d+)");
  println(input.lines()
      .filter(Predicate.not(String::isEmpty))
      .map(l -> pattern.matcher(l).results().map(MatchResult::group).mapToInt(Integer::parseInt).toArray())
      .gather(Gatherers.windowFixed(3))
      .flatMap(l -> new Object() {
          Stream<int[]> solve(int a1, int b1, int c1, int a2, int b2, int c2) {
            var det = a1 * b2 - a2 * b1;
            if (det == 0) {
              return null;
            }
            var xNum = b2 * c1 - b1 * c2;
            var yNum = a1 * c2 - a2 * c1;
            int ix, iy;
            if (xNum % det == 0 && yNum % det == 0 && (ix = xNum / det) >= 0 && (iy = yNum / det) >=0) {
              return Stream.of(new int[] { ix, iy });
            }
            return null;
          }
        }.solve(l.get(0)[0], l.get(1)[0], l.get(2)[0], l.get(0)[1], l.get(1)[1], l.get(2)[1]))
      .mapToInt(p -> p[0] * 3 + p[1])
      .sum());
}