
void main() {
  var input = """
      ....#.....
      .........#
      ..........
      ..#.......
      .......#..
      ..........
      .#..^.....
      ........#.
      #.........
      ......#...
      """;

  enum Dir {
    NORTH(0, -1), EAST(1, 0), SOUTH(0, 1), WEST(-1, 0);
    final int x, y;
    Dir(int x, int y) { this.x = x; this.y = y; }
    static final Dir[] DIRS = values();
    Dir next() { return DIRS[(ordinal() + 1) % DIRS.length]; }
  }
  record Pos(int x, int y) {
    Pos next(Dir dir) { return new Pos(x + dir.x, y + dir.y); }
  }
  record Grid(char[][] data, int width, int height) {
    char at(Pos p) { return data[p.y][p.x]; }
    boolean out(Pos p) { return p.x < 0 || p.x >= width || p.y < 0 || p.y >= height; }
  }
  var data = input.lines().map(String::toCharArray).toArray(char[][]::new);
  var grid = new Grid(data, data[0].length, data.length);
  var marks = new HashSet<Pos>();
  var start = IntStream.range(0, grid.height).boxed()
      .flatMap(j -> IntStream.range(0, grid.height).mapToObj(i -> new Pos(i, j)))
      .filter(p -> grid.at(p) == '^').findFirst().orElseThrow();
  var dir = Dir.NORTH;
  var pos = start;
  for(;;) {
    marks.add(pos);
    var newPos = pos.next(dir);
    if (grid.out(newPos)) {
      break;
    }
    if (grid.at(newPos) == '#') {
      dir = dir.next();
    } else {
      pos = newPos;
    }
  }
  println(marks.size());
}
