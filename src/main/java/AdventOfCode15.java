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
  void at(Pos p, char c) {
    data[p.y][p.x] = c;
  }
}

Pos initGridFindRobot(Grid grid, Set<Pos> boxes) {
  Pos robot = null;
  for(var j = 0; j < grid.height; j++) {
    for(var i = 0; i < grid.width; i++) {
      var pos = new Pos(i, j);
      switch (grid.at(pos)) {
        case '@' -> {
          robot = pos;
          grid.at(pos, '.');
        }
        case 'O' -> {
          boxes.add(pos);
          grid.at(pos, '.');
        }
      }
    }
  }
  return robot;
}

List<Pos> boxesToMove(Grid grid, Set<Pos> boxes, Pos robot, Dir dir) {
  var boxesToMove = new ArrayList<Pos>();
  for(var pos = robot;;) {
    pos = pos.next(dir);
    if (boxes.contains(pos)) {
      boxesToMove.add(pos);
      continue;
    }
    if (grid.at(pos) == '.') {
      return boxesToMove;
    }
    return null;
  }
}

void moveBoxes(Set<Pos> boxes, List<Pos> toMoves, Dir dir) {
  if (!toMoves.isEmpty()) {
    boxes.remove(toMoves.getFirst());
    boxes.add(toMoves.getLast().next(dir));
  }
}

static final Map<Character, Dir> DIRS =
    Map.of('^', new Dir(0, -1), '>', new Dir(1, 0), 'v', new Dir(0, 1), '<', new Dir(-1, 0));

void moveRobot(Grid grid, Set<Pos> boxes, Pos robotStart, String moves) {
  var robot = robotStart;
  for(var move : moves.toCharArray()) {
    var dir = DIRS.get(move);
    var boxesToMove = boxesToMove(grid, boxes, robot, dir);
    if (boxesToMove == null) {
      continue;
    }
    moveBoxes(boxes, boxesToMove, dir);
    robot = robot.next(dir);
  }
}

int sumOfCoordinates(Set<Pos> boxes) {
  return boxes.stream().mapToInt(b -> b.x + b.y * 100).sum();
}

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

  var index = input.indexOf("\n\n");
  var data = input.substring(0, index).lines().map(String::toCharArray).toArray(char[][]::new);
  var grid = new Grid(data, data[0].length, data.length);
  var moves = input.substring(index + 2).lines().collect(Collectors.joining());
  var boxes = new HashSet<Pos>();
  var robot = initGridFindRobot(grid, boxes);
  moveRobot(grid, boxes, robot, moves);
  println(sumOfCoordinates(boxes));
}