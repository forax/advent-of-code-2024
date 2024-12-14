record Pair(int x, int y) {}
record Claw(int a1, int b1, int c1, int a2, int b2, int c2) {}

static final Pattern PATTERN = Pattern.compile("X[\\+|=](\\d+), Y[\\+|=](\\d+)");

Pair parsePair(String line) {
  var matcher = PATTERN.matcher(line);
  matcher.find();
  return new Pair(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
}

List<Claw> parseClaws(List<String> lines) {
  var claws = new ArrayList<Claw>();
  for(var i = 0; i < lines.size(); i += 3) {
    var buttonA = parsePair(lines.get(i));
    var buttonB = parsePair(lines.get(i + 1));
    var prize = parsePair(lines.get(i + 2));
    var claw = new Claw(buttonA.x, buttonB.x, prize.x, buttonA.y, buttonB.y, prize.y);
    claws.add(claw);
  }
  return claws;
}

// equation: a1*x + b1*y = c1
// equation: a2*x + b2*y = c2
Optional<Pair> solve(int a1, int b1, int c1, int a2, int b2, int c2) {
  var determinant = a1 * b2 - a2 * b1;
  if (determinant == 0) {
    return Optional.empty(); // no unique solution
  }
  var xNum = b2 * c1 - b1 * c2;
  var yNum = a1 * c2 - a2 * c1;
  if (xNum % determinant == 0 && yNum % determinant == 0) {
    var x = xNum / determinant;
    var y = yNum / determinant;
    if (x >= 0 && y >= 0) {
      return Optional.of(new Pair(x, y));
    }
  }
  return Optional.empty();
}

int sumTokens(List<Claw> claws) {
  var tokens = 0;
  for(var claw : claws) {
    var pairOpt = solve(claw.a1, claw.b1, claw.c1, claw.a2, claw.b2, claw.c2);
    if (pairOpt.isPresent()) {
      var pair = pairOpt.orElseThrow();
      tokens += pair.x * 3 + pair.y;
    }
  }
  return tokens;
}

void main() {
  var input = """
      Button A: X+94, Y+34
      Button B: X+22, Y+67
      Prize: X=8400, Y=5400
      
      Button A: X+26, Y+66
      Button B: X+67, Y+21
      Prize: X=12748, Y=12176
      
      Button A: X+17, Y+86
      Button B: X+84, Y+37
      Prize: X=7870, Y=6450
      
      Button A: X+69, Y+23
      Button B: X+27, Y+71
      Prize: X=18641, Y=10279
      """;

  var lines = input.lines().filter(Predicate.not(String::isEmpty)).toList();
  println(sumTokens(parseClaws(lines)));
}