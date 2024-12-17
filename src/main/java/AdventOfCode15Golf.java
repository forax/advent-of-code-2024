void main() {
  var input = """
      ##########
      #..O..O.O#
      #......O.#
      #.OO..O.O#
      #..O@..O.#
      #O#..O...#
      #O..O..O.#
      #.OO.O.OO#
      #....O...#
      ##########
      
      <vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
      vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
      ><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
      <<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
      ^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
      ^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
      >^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
      <><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
      ^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
      v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
      """;

  record Pos(int x, int y) {
    Pos next(Pos dir) {
      return new Pos(x + dir.x, y + dir.y);
    }
  }
  record Grid(char[][] data, int width, int height) {
    char at(Pos p) {
      return data[p.y][p.x];
    }
  }
  var index = input.indexOf("\n\n");
  var data = input.substring(0, index).lines().map(String::toCharArray).toArray(char[][]::new);
  var grid = new Grid(data, data[0].length, data.length);
  var state = new Object() { Pos robot; };
  var boxes = IntStream.range(0, grid.height).boxed().flatMap(j -> IntStream.range(0, grid.width).mapToObj(i -> new Pos(i, j)))
      .peek(p -> { if (grid.at(p) == '@') { state.robot = p; }})
      .filter(p -> grid.at(p) == 'O')
      .collect(Collectors.toSet());
  var dirMap = Map.of('^', new Pos(0, -1), '>', new Pos(1, 0), 'v', new Pos(0, 1), '<', new Pos(-1, 0));
  println(input.substring(index + 2).lines().flatMapToInt(String::chars)
      .mapToObj(m -> dirMap.get((char) m))
      .<Pos>gather(Gatherer.ofSequential(() -> state, (s, dir, d) -> {
        var nextRobot = state.robot.next(dir);
        var boxesToMove = Stream.iterate(nextRobot, boxes::contains, p -> p.next(dir)).toList();
        var toInsert = boxesToMove.isEmpty() ? nextRobot : boxesToMove.getLast().next(dir);
        if (grid.at(toInsert) != '#') {
          if (!boxesToMove.isEmpty()) {
            boxes.remove(nextRobot);
            boxes.add(toInsert);
          }
          state.robot = nextRobot;
        }
        return true;
      }, (_, d) -> boxes.forEach(d::push)))
      .mapToInt(b -> b.x + b.y * 100).sum());
}