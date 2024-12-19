record Pos(int x, int y) { }
record Dir(int dx, int dy) { }
record Grid(int width, int height) {
  boolean out(Pos pos) {
    return pos.x < 0 || pos.x >= width || pos.y < 0 || pos.y >= height;
  }
}
record Work(Pos pos, int dist) { }

Pos parsePos(String line) {
  var index = line.indexOf(',');
  var x = Integer.parseInt(line.substring(0, index));
  var y = Integer.parseInt(line.substring(index + 1));
  return new Pos(x, y);
}

Set<Pos> parseWalls(String input, int posCount) {
  return input.lines().map(this::parsePos).limit(posCount).collect(Collectors.toSet());
}

static final Dir[] DIRS = { new Dir(0, -1), new Dir(1, 0), new Dir(0, 1), new Dir(-1, 0) };

Map<Pos, Integer> computeStateMap(Set<Pos> walls, Grid grid, Pos end) {
  var stateMap = new HashMap<Pos, Integer>();
  var queue = new ArrayDeque<Work>();
  queue.add(new Work(new Pos(0, 0), 0));
  while (!queue.isEmpty()) {
    var work = queue.poll();
    var pos = work.pos;
    var state = stateMap.getOrDefault(pos, Integer.MAX_VALUE);
    if (work.dist >= state) {
      continue;
    }
    stateMap.put(pos, work.dist);
    if (pos.equals(end)) {
      continue;
    }
    for(var dir : DIRS) {
      var newPos = new Pos(pos.x + dir.dx, pos.y + dir.dy);
      if (grid.out(newPos)) {
        continue;
      }
      if (walls.contains(newPos)) {
        continue;
      }
      queue.offer(new Work(newPos, work.dist + 1));
    }
  }
  return stateMap;
}

void main() {
  var input = """
      5,4
      4,2
      4,5
      3,0
      2,1
      6,3
      2,4
      1,5
      0,6
      3,3
      2,6
      5,1
      1,2
      5,5
      2,5
      6,5
      1,4
      0,4
      6,4
      1,1
      6,1
      1,0
      0,5
      1,6
      2,0
      """;

  var grid = new Grid(7, 7);
  var end = new Pos(6, 6);
  var walls = parseWalls(input, 12);
  var stateMap = computeStateMap(walls, grid, end);
  println(stateMap.get(end));
}
