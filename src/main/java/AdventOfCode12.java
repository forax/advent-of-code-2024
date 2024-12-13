record Dir(int dx, int dy) {}
record Pos(int x, int y) {
  Pos next(Dir dir) {
    return new Pos(x + dir.dx, y + dir.dy);
  }
}
record Grid(char[][] data, int width, int height) {
  char at(Pos p) {
    return data[p.y][p.x];
  }
  boolean in(Pos p) { return p.x >= 0 && p.x < width && p.y >= 0 && p.y < height; }
}

static class Region {
  int perimeter;
  int area;

  @Override
  public String toString() {
    return "Region{perimeter: " + perimeter + ", " + "area: " + area + "}";
  }
}

static final List<Dir> DIRS = List.of(new Dir(0, -1), new Dir(1, 0), new Dir(0, 1), new Dir(-1, 0));

void findRegion(Grid grid, Pos pos, char letter, Region region, Set<Pos> visited) {
  region.area++;
  for(var dir : DIRS) {
    var p = pos.next(dir);
    if (!grid.in(p) || grid.at(p) != letter) {
      region.perimeter++;
      continue;
    }
    if (!visited.add(p)) {
      continue;
    }
    findRegion(grid, p, letter, region, visited);
  }
}

void main() {
  var input = """
      RRRRIICCFF
      RRRRIICCCF
      VVRRRCCFFF
      VVRCCCJFFF
      VVVVCJJCFE
      VVIVCCJJEE
      VVIIICJJEE
      MIIIIIJJEE
      MIIISIJEEE
      MMMISSJEEE
      """;

  var data = input.lines().map(String::toCharArray).toArray(char[][]::new);
  var grid = new Grid(data, data[0].length, data.length);

  var regions = new ArrayList<Region>();
  var visited = new HashSet<Pos>();
  for(var j = 0; j < grid.height; j++) {
    for(var i = 0; i < grid.width; i++) {
      var pos = new Pos(i, j);
      if (!visited.add(pos)) {
        continue;
      }
      var letter = grid.at(pos);
      var region = new Region();
      findRegion(grid, pos, letter, region, visited);
      regions.add(region);
    }
  }
  println(regions.stream().mapToInt(r -> r.perimeter * r.area).sum());
}