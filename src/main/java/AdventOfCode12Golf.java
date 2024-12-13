import static java.util.stream.IntStream.range;

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

  record Pos(int x, int y) {
    Pos next(Pos dir) { return new Pos(x + dir.x, y + dir.y); }
  }
  record Grid(char[][] data, int width, int height) {
    char at(Pos p) { return data[p.y][p.x]; }
    boolean in(Pos p) { return p.x >= 0 && p.x < width && p.y >= 0 && p.y < height; }
  }
  var dirs = List.of(new Pos(0, -1), new Pos(1, 0), new Pos(0, 1), new Pos(-1, 0));
  class Region {
    int perimeter;
    int area;

    Region populate(Grid grid, Pos pos, char letter, Set<Pos> visited) {
      area++;
      for(var dir : dirs) {
        var p = pos.next(dir);
        if (!grid.in(p) || grid.at(p) != letter) {
          perimeter++;
          continue;
        }
        if (!visited.add(p)) {
          continue;
        }
        populate(grid, p, letter, visited);
      }
      return this;
    }
  }
  var data = input.lines().map(String::toCharArray).toArray(char[][]::new);
  var grid = new Grid(data, data[0].length, data.length);
  var visited = new HashSet<Pos>();
  println(range(0, grid.height).boxed().flatMap(j -> range(0, grid.width()).mapToObj(i -> new Pos(i, j)))
      .filter(visited::add)
      .map(p -> new Region().populate(grid, p, grid.at(p), visited))
      .mapToInt(r -> r.perimeter * r.area)
      .sum());
}