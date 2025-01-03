record Equation(int test, int[] values) { }

boolean solve(Equation equation, List<IntBinaryOperator> ops) {
  return solve(equation, equation.values[0], 1, ops);
}

boolean solve(Equation equation, int partial, int index, List<IntBinaryOperator> ops) {
  if (index == equation.values.length) {
    return partial == equation.test;
  }
  var value = equation.values[index];
  for(var op : ops) {
    if (solve(equation, op.applyAsInt(partial, value), index + 1, ops)) {
      return true;
    }
  }
  return false;
}

Equation parse(String line) {
  var tokens = line.split("[: ]+");
  var test = Integer.parseInt(tokens[0]);
  var values = Arrays.stream(tokens, 1, tokens.length).mapToInt(Integer::parseInt).toArray();
  return new Equation(test, values);
}

int sumOfTestValues(List<Equation> equations, List<IntBinaryOperator> ops) {
  var sum = 0;
  for(var equation : equations) {
    if (solve(equation, ops)) {
      sum += equation.test();
    }
  }
  return sum;
}

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

  var ops = List.<IntBinaryOperator>of(Math::addExact, Math::multiplyExact);
  var equations = input.lines().map(this::parse).toList();
  println(sumOfTestValues(equations, ops));
}
