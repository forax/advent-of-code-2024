import static java.util.stream.IntStream.range;

void main() {
  var input = """
      ............
      ........0...
      .....0......
      .......0....
      ....0.......
      ......A.....
      ............
      ............
      ........A...
      .........A..
      ............
      ............
      """;

  record Pos(int x, int y) {}
  record Grid(char[][] data, int width, int height) {
    char at(Pos p) {
      return data[p.y][p.x];
    }
    boolean in(Pos p) { return p.x >= 0 && p.x < width && p.y >= 0 && p.y < height; }
  }
  var data = input.lines().map(String::toCharArray).toArray(char[][]::new);
  var grid = new Grid(data, data[0].length, data.length);
  println(range(0, grid.height).boxed().flatMap(j -> range(0, grid.width).mapToObj(i -> new Pos(i, j)))
      .filter(p -> grid.at(p) != '.')
      .collect(Collectors.groupingBy(grid::at))
      .values().stream()
      .flatMap(ps -> range(0, ps.size()).boxed().flatMap(j -> range(j + 1, ps.size()).boxed().flatMap(i -> {
        var a = ps.get(j);
        var b = ps.get(i);
        var dx = b.x - a.x;
        var dy = b.y - a.y;
        return Stream.of(new Pos(a.x - dx, a.y - dy), new Pos(b.x + dx, b.y + dy));
      })))
      .filter(grid::in)
      .distinct()
      .count());
}
