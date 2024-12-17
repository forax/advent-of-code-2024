enum Dir {
  NORTH(0, -1), EAST(1, 0), SOUTH(0, 1), WEST(-1, 0);
  final int x, y;

  Dir(int x, int y) {
    this.x = x;
    this.y = y;
  }

  Dir next(int i) {
    return DIRS[(ordinal() + i) % DIRS.length];
  }
}

static final Dir[] DIRS = Dir.values();
static final int[] POINTS = new int[] { 1, 1_001, 2_002, 1_001 };

record Pos(int x, int y) {
  Pos next(Dir dir) {
    return new Pos(x + dir.x, y + dir.y);
  }
}

record Grid(char[][] data, int width, int height) {
  char at(Pos p) {
    return data[p.y][p.x];
  }

  boolean in(Pos p) {
    return p.x >= 0 && p.x < width && p.y >= 0 && p.y < height;
  }
}

record Pair(Pos pos, Dir dir) {}

int walk(Pos pos, Dir dir, Pos end, Grid grid, Map<Pos, Integer> map, int points, List<Pair> path) {
  path.add(new Pair(pos, dir));
  if (pos.equals(end)) {
    return points;
  }
  var p = map.getOrDefault(pos, Integer.MAX_VALUE);
  if (p <= points) {
    return Integer.MAX_VALUE;
  }
  map.put(pos, points);
  var value = Integer.MAX_VALUE;
  for(var i = 0; i < DIRS.length; i++) {
    var d = dir.next(i);
    var nextPos = pos.next(d);
    if (grid.at(nextPos) == '#') {
      continue;
    }
    var v = walk(nextPos,  d, end, grid, map, points + POINTS[i], new ArrayList<>(path));
    value = Math.min(value, v);
  }
  return value;
}

String debug(Grid grid, List<Pair> path) {
  var map = path.stream().collect(Collectors.toMap(Pair::pos, Pair::dir));
  var builder = new StringBuilder();
  for(var j = 0; j < grid.height; j++) {
    for (var i = 0; i < grid.width; i++) {
      var pos = new Pos(i, j);
      var dir = map.get(pos);
      var letter = switch (dir) {
        case null -> grid.at(pos);
        case EAST -> '>';
        case WEST -> '<';
        case NORTH -> '^';
        case SOUTH -> 'v';
      };
      builder.append(letter);
    }
    builder.append('\n');
  }
  return builder.toString();
}

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

  var data = input.lines().map(String::toCharArray).toArray(char[][]::new);
  var grid = new Grid(data, data[0].length, data.length);
  Pos start = null, end = null;
  for(var j = 0; j < grid.height; j++) {
    for(var i = 0; i < grid.width; i++) {
      var pos = new Pos(i, j);
      switch (grid.at(pos)) {
        case 'S' -> start = pos;
        case 'E' -> end = pos;
      }
    }
  }
  var points = walk(start, Dir.EAST, end, grid, new HashMap<>(), 0, new ArrayList<>());
  println(points);
}
