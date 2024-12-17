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
}

Pos findStart(Grid grid) {
  Pos start = null, end = null;
  for(var j = 0; j < grid.height; j++) {
    for(var i = 0; i < grid.width; i++) {
      var pos = new Pos(i, j);
      if (grid.at(pos) == 'S') {
       return pos;
      }
    }
  }
  throw new IllegalArgumentException("no start");
}

record Pair(Pos pos, Dir dir) {}

int walk(Pos pos, Dir dir, Grid grid, Map<Pair, Integer> map, int points) {
  if (grid.at(pos) == 'E') {
    return points;
  }
  var pair = new Pair(pos, dir);
  var previousPoints = map.getOrDefault(pair, Integer.MAX_VALUE);
  if (previousPoints <= points) {
    return Integer.MAX_VALUE;
  }
  map.put(pair, points);
  var value = Integer.MAX_VALUE;
  for(var i = 0; i < DIRS.length; i++) {
    var d = dir.next(i);
    var nextPos = pos.next(d);
    if (grid.at(nextPos) == '#') {
      continue;
    }
    var v = walk(nextPos,  d, grid, map, points + POINTS[i]);
    value = Math.min(value, v);
  }
  return value;
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
  var start = findStart(grid);
  println(walk(start, Dir.EAST, grid, new HashMap<>(), 0));
}
