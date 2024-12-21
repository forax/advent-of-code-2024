void main() {
  var input = """
      ###############
      #...#...#.....#
      #.#.#.#.#.###.#
      #S#...#.#.#...#
      #######.#.#.###
      #######.#.#...#
      #######.#.###.#
      ###..E#...#...#
      ###.#######.###
      #...###...#...#
      #.#####.#.###.#
      #.#...#.#.#...#
      #.#.#.#.#.#.###
      #...#...#...###
      ###############
      """;

  var data = input.lines().map(String::toCharArray).toArray(char[][]::new);
  var width = data[0].length;
  var height = data.length;
  record Pos(int x, int y) {}
  var start = IntStream.range(0, height).boxed().flatMap(j -> IntStream.range(0, width).mapToObj(i -> new Pos(i, j)))
      .filter(pos -> data[pos.y][pos.x] == 'S').findFirst().orElseThrow();
  var dirs = new int[][] { {0, -1}, {1, 0}, {0, 1}, {-1, 0} };
  var track = new LinkedHashMap<Pos, Integer>();
  for(var pos = start;; pos = switch (pos) { default -> {
    var _pos = pos;
    yield Arrays.stream(dirs).map(d -> new Pos(_pos.x + d[0], _pos.y + d[1]))
        .filter(Predicate.not(track::containsKey))
        .filter(p -> data[p.y][p.x] != '#')
        .findFirst().orElseThrow();
  }}) {
    track.put(pos, track.size());
    if (data[pos.y][pos.x] == 'E') {
      break;
    }
  }
  println(track.entrySet().stream()
      .flatMapToInt(e ->
        Arrays.stream(dirs).map(d -> track.get(new Pos(e.getKey().x + d[0] * 2, e.getKey().y + d[1] * 2)))
            .flatMapToInt(d -> d != null  && d - e.getValue() - 2 > 0 ? IntStream.of(d - e.getValue() - 2) : null))
      .filter(d -> d <= 100)
      .count());
}
