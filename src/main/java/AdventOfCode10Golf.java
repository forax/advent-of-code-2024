import static java.util.stream.IntStream.range;

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

  record Pos(int x, int y) {
    Pos next(Pos d) { return new Pos(x + d.x, y + d.y); }
  }
  var dirs = List.of(new Pos(0, -1), new Pos(1, 0), new Pos(0, 1), new Pos(-1, 0));
  record Grid(int[][] data, int width, int height) {
    int at(Pos p) { return data[p.y][p.x]; }
    boolean in(Pos p) { return p.x >= 0 && p.x < width && p.y >= 0 && p.y < height; }
  }
  var data = input.lines().map(l -> l.chars().map(c -> c -'0').toArray()).toArray(int[][]::new);
  var grid = new Grid(data, data[0].length, data.length);
  println(range(0, grid.height).boxed()
      .flatMap(j -> range(0, grid.width).mapToObj(i -> new Pos(i, j)).filter(p -> grid.at(p) == 0))
      .mapToLong(h -> Stream.of(h).<Pos>mapMulti((p, c) -> new Object() {
        void walk(Grid grid, Pos pos, int value, Consumer<Pos> consumer) {
          if (value == 10) {
            consumer.accept(pos);
            return;
          }
          for(var dir : dirs) {
            var newPos = pos.next(dir);
            if (!grid.in(newPos) || grid.at(newPos) != value) {
              continue;
            }
            walk(grid, newPos, value + 1, consumer);
          }
        }
      }.walk(grid, p, 1, c)).distinct().count())
      .sum());
}