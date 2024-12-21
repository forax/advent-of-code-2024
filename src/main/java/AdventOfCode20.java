record Pos(int x, int y) {}
record Grid(char[][] data, int width, int height) {
  char at(Pos p) {
    return data[p.y][p.x];
  }
}

static final int[][] DIRS = new int[][] { {0, -1}, {1, 0}, {0, 1}, {-1, 0} };

Pos findStart(Grid grid) {
  return IntStream.range(0, grid.height).boxed()
      .flatMap(j -> IntStream.range(0, grid.width).mapToObj(i -> new Pos(i, j)))
      .filter(pos -> grid.at(pos) == 'S')
      .findFirst().orElseThrow();
}

Map<Pos, Integer> computeTrack(Pos start, Grid grid) {
  var track = new LinkedHashMap<Pos, Integer>();
  var dist = 0;
  loop: for(var pos = start;;) {
    track.put(pos, dist++);
    if (grid.at(pos) == 'E') {
      break;
    }
    for(var dir : DIRS) {
      var newPos = new Pos(pos.x + dir[0], pos.y + dir[1]);
      if (track.containsKey(newPos)) {
        continue;
      }
      if (grid.at(newPos) != '#') {
        pos = newPos;
        continue loop;
      }
    }
    throw new AssertionError();
  }
  return track;
}

List<Integer> findCheats(Map<Pos, Integer> track) {
  var cheats = new ArrayList<Integer>();
  for(var trackEntry : track.entrySet()) {
    var pos = trackEntry.getKey();
    var d = trackEntry.getValue();
    for(var dir : DIRS) {
      var cheatPos = new Pos(pos.x + dir[0] * 2, pos.y + dir[1] * 2);
      var cheatDist = track.get(cheatPos);
      int distance;
      if (cheatDist != null && (distance = cheatDist - d - 2) > 0) {
        cheats.add(distance);
      }
    }
  }
  return cheats;
}

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
  var grid = new Grid(data, data[0].length, data.length);
  var start = findStart(grid);
  var track = computeTrack(start, grid);
  var cheats = findCheats(track);
  println(cheats.stream().filter(d -> d <= 100).count());
}
