enum Dir {
  NORTH(0, -1), EAST(1, 0), SOUTH(0, 1), WEST(-1, 0);
  static final Dir[] DIRS = values();
  final int x, y;

  Dir(int x, int y) {
    this.x = x;
    this.y = y;
  }

  Dir next() {
    return DIRS[(ordinal() + 1) % DIRS.length];
  }
}

record Pos(int x, int y) {
  Pos next(Dir dir) {
    return new Pos(x + dir.x, y + dir.y);
  }
}

record Grid(char[][] data, int width, int height) {
  char at(Pos p) {
    return data[p.y][p.x];
  }

  boolean out(Pos p) {
    return p.x < 0 || p.x >= width || p.y < 0 || p.y >= height;
  }
}

Pos start(Grid grid) {
  for (var j = 0; j < grid.height; j++) {
    for (var i = 0; i < grid.width; i++) {
      var pos = new Pos(i, j);
      if (grid.at(pos) == '^') {
        return pos;
      }
    }
  }
  throw new IllegalArgumentException("no initial start");
}

int walk(Grid grid, Pos start, Dir dir) {
  var marks = new HashSet<Pos>();
  var pos = start;
  for (;;) {
    marks.add(pos);
    var newPos = pos.next(dir);
    if (grid.out(newPos)) {
      return marks.size();
    }
    if (grid.at(newPos) == '#') {
      dir = dir.next();
      continue;
    }
    pos = newPos;
  }
}

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
  var data = input.lines().map(String::toCharArray).toArray(char[][]::new);
  var grid = new Grid(data, data[0].length, data.length);
  var start = start(grid);
  println(walk(grid, start, Dir.NORTH));
}
