record Pos(int x, int y) {}
record Grid(char[][] data, int width, int height) {
  char at(Pos p) {
    return data[p.y][p.x];
  }
  boolean in(Pos p) { return p.x >= 0 && p.x < width && p.y >= 0 && p.y < height; }
}

Map<Character, List<Pos>> antennaPositionsByType(Grid grid) {
  var map = new HashMap<Character, List<Pos>>();
  for(var j = 0; j < grid.height; j++) {
    for(var i = 0; i < grid.width; i++) {
      var pos = new Pos(i, j);
      var letter = grid.at(pos);
      if (letter != '.') {
        map.computeIfAbsent(letter, _ -> new ArrayList<>()).add(pos);
      }
    }
  }
  return map;
}

Set<Pos> antinodes(Map<Character, List<Pos>> antennas, Grid grid) {
  var antinodes = new HashSet<Pos>();
  for(var positions : antennas.values()) {
    for (var j = 0; j < positions.size(); j++) {
      var a = positions.get(j);
      for (var i = j + 1; i < positions.size(); i++) {
        var b = positions.get(i);
        var dx = b.x - a.x;
        var dy = b.y - a.y;  // should be always positive by construction
        var antinode1 = new Pos(a.x - dx, a.y - dy);
        if (grid.in(antinode1)) {
          antinodes.add(antinode1);
        }
        var antinode2 = new Pos(b.x + dx, b.y + dy);
        if (grid.in(antinode2)) {
          antinodes.add(antinode2);
        }
      }
    }
  }
  return antinodes;
}

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

  var data = input.lines().map(String::toCharArray).toArray(char[][]::new);
  var grid = new Grid(data, data[0].length, data.length);
  println(antinodes(antennaPositionsByType(grid), grid).size());
}
