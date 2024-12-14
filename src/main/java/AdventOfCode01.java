static final Pattern SPACES = Pattern.compile(" +");

record Pair(int left, int right) { }

Pair parse(String line) {
  var tokens = SPACES.split(line);
  var left = Integer.parseInt(tokens[0]);
  var right = Integer.parseInt(tokens[1]);
  return new Pair(left, right);
}

int distance(int left, int right) {
  return Math.abs(left - right);
}

int sumOfDistances(int[] lefts, int[] rights) {
  return IntStream.range(0, lefts.length)
      .map(i -> distance(lefts[i], rights[i]))
      .sum();
}

void main() {
  var input = """
      3   4
      4   3
      2   5
      1   3
      3   9
      3   3
      """;

  var pairs = input.lines().map(this::parse).toList();
  var lefts = pairs.stream().mapToInt(Pair::left).sorted().toArray();
  var rights = pairs.stream().mapToInt(Pair::right).sorted().toArray();
  println(sumOfDistances(lefts, rights));
}