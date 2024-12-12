void main() {
  var input = """
      190: 10 19
      3267: 81 40 27
      83: 17 5
      156: 15 6
      7290: 6 8 6 15
      161011: 16 10 13
      192: 17 8 14
      21037: 9 7 18 13
      292: 11 6 16 20
      """;

  record Equation(int test, int[] values) {
    boolean eval(int value, int index, List<IntBinaryOperator> ops) {
      if (index == values.length) { return value == test; }
      return ops.stream().anyMatch(op -> eval(op.applyAsInt(value, values[index]), index + 1, ops));
    }
  }
  println(input.lines().map(l -> {
    var scanner = new Scanner(l).useDelimiter("[:| ]+");
    return new Equation(scanner.nextInt(),
                        Stream.iterate(scanner, Scanner::hasNextInt, s -> s).mapToInt(Scanner::nextInt).toArray());
  }).filter(e -> e.eval(e.values[0], 1, List.of(Math::addExact, Math::multiplyExact))).mapToInt(Equation::test).sum());
}
