record Pos(int x, int y) { }

record Dir(int dx, int dy) { }

record Grid(char[][] data, int width, int height) {
  char at(Pos p) {
    return data[p.y][p.x];
  }
  boolean in(Pos p) {
    return p.x >= 0 && p.x < width && p.y >= 0 && p.y < height;
  }
}

boolean match(Grid grid, Pos center, Dir direction, String word) {
  for(var i = 0; i < word.length(); i++) {
    var pos = new Pos(center.x + direction.dx * i, center.y + direction.dy * i);
    if (!grid.in(pos)) {
      return false;
    }
    var letter = grid.at(pos);
    if (letter != word.charAt(i)) {
      return false;
    }
  }
  return true;
}

int countWords(Grid grid, List<Dir> directions, String word) {
  var count = 0;
  for(var j = 0; j < grid.height; j++) {
    for(var i = 0; i < grid.width; i++) {
      var center = new Pos(i, j);
      for(var direction : directions) {
        if (match(grid, center, direction, word)) {
          count++;
        }
      }
    }
  }
  return count;
}

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

  var data = input.lines().map(String::toCharArray).toArray(char[][]::new);
  var grid = new Grid(data, data[0].length, data.length);
  var directions = List.of(new Dir(0, 1), new Dir(0, -1), new Dir(1, 0), new Dir(-1, 0),
      new Dir(1, 1), new Dir(1, -1), new Dir(-1, 1), new Dir(-1, -1));

  println(countWords(grid, directions, word));
}
