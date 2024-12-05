import static java.util.stream.Collectors.joining;
import static java.util.stream.IntStream.range;

void main() {
  var input = """
      MMMSXXMASM
      MSAMXMSMSA
      AMXSXMAAMM
      MSAMASMSMX
      XMASAMXAMM
      XXAMMXXAMA
      SMSMSASXSS
      SAXAMASAAA
      MAMMMXMMMM
      MXMXAXMASX
      """;
  var word = "XMAS";

  record Pos(int x, int y) {
    Pos with(int dx, int dy) { return new Pos(x + dx, y + dy); }
  }
  record Grid(char[][] data, int width, int height) {
    String at(Pos p) { return "" + data[p.y][p.x]; }
    boolean in(Pos p) { return p.x >= 0 && p.x < width && p.y >= 0 && p.y < height; }
  }
  var data = input.lines().map(String::toCharArray).toArray(char[][]::new);
  var grid = new Grid(data, data[0].length, data.length);

  var dirs = List.of(new Pos(0, 1), new Pos(0, -1), new Pos(1, 0), new Pos(-1, 0),
      new Pos(1, 1), new Pos(1, -1), new Pos(-1, 1), new Pos(-1, -1));

  println(range(0, grid.height).boxed()
      .flatMap(j -> range(0, grid.width).mapToObj(i -> new Pos(i, j))
          .flatMap(c -> dirs.stream().map(d -> range(0, word.length())
              .mapToObj(k -> c.with(k * d.x, k * d.y)).filter(grid::in).map(grid::at).collect(joining()))))
      .filter(word::equals)
      .count());
}
