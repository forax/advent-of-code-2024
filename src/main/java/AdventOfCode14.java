record Grid(int width, int height) {}
record Quadrant(int qx, int qy) {}

class Robot {
  int x, y;
  final int vx, vy;

  Robot(int x, int y, int vx, int vy) {
    this.x = x; this.y = y; this.vx = vx; this.vy = vy;
  }

  @Override
  public String toString() {
    return "p=" + x + "," + y + " v=" + vx + "," + vy;
  }
}

static final Pattern PATTERN = Pattern.compile("p=([-]?\\d+),([-]?\\d+) v=([-]?\\d+),([-]?\\d+)");

Robot parseRobot(String line) {
  var matcher = PATTERN.matcher(line);
  matcher.matches();
  return new Robot(Integer.parseInt(matcher.group(1)),
      Integer.parseInt(matcher.group(2)),
      Integer.parseInt(matcher.group(3)),
      Integer.parseInt(matcher.group(4)));
}

List<Robot> parseRobots(String input) {
  return input.lines().map(this::parseRobot).toList();
}

int wrap(int value, int size) {
  return (value % size + size) % size;
}

List<Robot> advanceRobots(Grid grid, int steps, List<Robot> robots) {
  for(var i = 0; i < steps; i++) {
    for(var robot : robots) {
      robot.x = wrap(robot.x + robot.vx, grid.width);
      robot.y = wrap(robot.y + robot.vy, grid.height);
    }
  }
  return robots;
}

Quadrant quadrant(Grid grid, Robot robot) {
  var xMiddle = grid.width / 2;
  var yMiddle = grid.height / 2;
  return new Quadrant(Integer.signum(robot.x - xMiddle), Integer.signum(robot.y - yMiddle));
}

Map<Quadrant, Long> groupByQuadrantAndCount(Grid grid, List<Robot> robots) {
  return robots.stream()
      .map(r -> quadrant(grid, r))
      .filter(q -> q.qx != 0 && q.qy != 0)
      .collect(Collectors.groupingBy(q -> q, Collectors.counting()));
}

long safetyFactor(Map<Quadrant, Long> quadrantMap) {
  return quadrantMap.values().stream().mapToLong(v -> v).reduce(1L, (a, b) -> a * b);
}

void main() {
  var input = """
      p=0,4 v=3,-3
      p=6,3 v=-1,-3
      p=10,3 v=-1,2
      p=2,0 v=2,-1
      p=0,0 v=1,3
      p=3,0 v=-2,-2
      p=7,6 v=-1,-3
      p=3,0 v=-1,-2
      p=9,3 v=2,3
      p=7,3 v=-1,2
      p=2,4 v=2,-3
      p=9,5 v=-3,-3
      """;

  var grid = new Grid(11, 7);
  println(safetyFactor(groupByQuadrantAndCount(grid, advanceRobots(grid, 100, parseRobots(input)))));
}