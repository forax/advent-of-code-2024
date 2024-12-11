record Dir(int dx, int dy) {}
record Pos(int x, int y) {
  Pos next(Dir dir) {
    return new Pos(x + dir.dx, y + dir.dy);
  }
}
record Grid(int[][] data, int width, int height) {
  int at(Pos p) {
    return data[p.y][p.x];
  }
  boolean in(Pos p) { return p.x >= 0 && p.x < width && p.y >= 0 && p.y < height; }
}

List<Pos> trailheads(Grid grid) {
  var list = new ArrayList<Pos>();
  for(var j = 0; j < grid.height; j++) {
    for(var i = 0; i < grid.width; i++) {
      var pos = new Pos(i, j);
      if (grid.at(pos) == 0) {
        list.add(pos);
      }
    }
  }
  return list;
}

static final List<Dir> DIRS = List.of(new Dir(0, -1), new Dir(1, 0), new Dir(0, 1), new Dir(-1, 0));

void walk(Grid grid, Pos pos, int value, Set<Pos> ends) {
  if (value == 10) {
    ends.add(pos);
    return;
  }
  for(var dir : DIRS) {
    var newPos = pos.next(dir);
    if (!grid.in(newPos) || grid.at(newPos) != value) {
      continue;
    }
    walk(grid, newPos, value + 1, ends);
  }
}

int score(Grid grid, List<Pos> trailheads) {
  var sum = 0;
  for(var trailhead: trailheads) {
    var ends = new HashSet<Pos>();
    walk(grid, trailhead, 1, ends);
    sum += ends.size();
  }
  return sum;
}

void main() {
  var input = """
      89010123
      78121874
      87430965
      96549874
      45678903
      32019012
      01329801
      10456732
      """;

  var data = input.lines().map(l -> l.chars().map(c -> c -'0').toArray()).toArray(int[][]::new);
  var grid = new Grid(data, data[0].length, data.length);
  var trailheads = trailheads(grid);
  println(score(grid, trailheads));
}