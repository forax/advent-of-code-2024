void main() {
  var input = """
      #################
      #...#...#...#..E#
      #.#.#.#.#.#.#.#.#
      #.#.#.#...#...#.#
      #.#.#.#.###.#.#.#
      #...#.#.#.....#.#
      #.#.#.#.#.#####.#
      #.#...#.#.#.....#
      #.#.#####.#.###.#
      #.#.#.......#...#
      #.#.###.#####.###
      #.#.#...#.....#.#
      #.#.#.#####.###.#
      #.#.#.........#.#
      #.#.#.#########.#
      #S#.............#
      #################
      """;

  record Pos(int x, int y) { }
  record Pair(Pos pos, int dir) { }
  var data = input.lines().map(String::toCharArray).toArray(char[][]::new);
  var dirs = new int[][] { {0, -1}, {1, 0}, {0, 1}, {-1, 0} };
  var points = new int[] { 1, 1_001, 2_002, 1_001 };
  var map = new HashMap<Pair, Integer>();
  println(new Object() {
    int walk(Pos pos, int dir, int value) {
      if (data[pos.y][pos.x] == 'E') {
        return value;
      }
      var pair = new Pair(pos, dir);
      var p = map.getOrDefault(pair, Integer.MAX_VALUE);
      if (p <= value) {
        return Integer.MAX_VALUE;
      }
      map.put(pair, value);
      return IntStream.range(0, dirs.length).mapMulti((i, c) -> {
        var d = (dir + i) % dirs.length;
        var nextPos = new Pos(pos.x + dirs[d][0], pos.y + dirs[d][1]);
        if (data[nextPos.y][nextPos.x] != '#') {
          c.accept(walk(nextPos, d, value + points[i]));
        }
      }).reduce(Integer.MAX_VALUE, Math::min);
    }
  }.walk(IntStream.range(0, data.length).boxed().flatMap(j -> IntStream.range(0, data[0].length).mapToObj(i -> new Pos(i, j)))
      .filter(pos -> data[pos.y][pos.x] == 'S').findFirst().orElseThrow(), 1, 0));
}
