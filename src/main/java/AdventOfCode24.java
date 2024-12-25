enum OpKind { AND, OR, XOR }

sealed interface Expr { }
record Const(int value) implements Expr { }
record Op(String left, OpKind kind, String right, String symbol) implements Expr { }

record Pair(String symbol, Expr expr) {}

static final Pattern PATTERN = Pattern.compile("(\\w+): (\\d+)|(\\w+) (AND|OR|XOR) (\\w+) -> (\\w+)");

Pair parseExprPair(String line) {
  var matcher = PATTERN.matcher(line);
  if (!matcher.matches()) {
    return null;
  }
  if (matcher.start(1) != -1) {
    var symbol = matcher.group(1);
    var value = Integer.parseInt(matcher.group(2));
    return new Pair(symbol, new Const(value));
  }
  var r1 = matcher.group(3);
  var kind = OpKind.valueOf(matcher.group(4));
  var r2 = matcher.group(5);
  var symbol = matcher.group(6);
  return new Pair(symbol, new Op(r1, kind, r2, symbol));
}

Map<String, Expr> createStates(String input) {
  return input.lines()
      .flatMap(line -> Stream.ofNullable(parseExprPair(line)))
      .collect(Collectors.toMap(Pair::symbol, Pair::expr));
}

int eval(Expr expr, Map<String, Expr> states) {
  return switch (expr) {
    case Const(var value) -> value;
    case Op(var left, var kind, var right, var symbol) -> {
      var v1 = eval(states.get(left), states);
      var v2 = eval(states.get(right), states);
      var result = switch (kind) {
        case AND -> v1 & v2;
        case OR -> v1 | v2;
        case XOR -> v1 ^ v2;
      };
      states.put(symbol, new Const(result));
      yield result;
    }
  };
}

int computeNumber(Map<String, Expr> states) {
  return states.entrySet().stream()
      .filter(e -> e.getKey().startsWith("z"))
      .map(e -> eval(e.getValue(), states) << Integer.parseInt(e.getKey().substring(1)))
      .reduce(0, (a, b) -> a | b);
}

void main() {
  var input = """
      x00: 1
      x01: 0
      x02: 1
      x03: 1
      x04: 0
      y00: 1
      y01: 1
      y02: 1
      y03: 1
      y04: 1
      
      ntg XOR fgs -> mjb
      y02 OR x01 -> tnw
      kwq OR kpj -> z05
      x00 OR x03 -> fst
      tgd XOR rvg -> z01
      vdt OR tnw -> bfw
      bfw AND frj -> z10
      ffh OR nrd -> bqk
      y00 AND y03 -> djm
      y03 OR y00 -> psh
      bqk OR frj -> z08
      tnw OR fst -> frj
      gnj AND tgd -> z11
      bfw XOR mjb -> z00
      x03 OR x00 -> vdt
      gnj AND wpb -> z02
      x04 AND y00 -> kjc
      djm OR pbm -> qhw
      nrd AND vdt -> hwm
      kjc AND fst -> rvg
      y04 OR y02 -> fgs
      y01 AND x02 -> pbm
      ntg OR kjc -> kwq
      psh XOR fgs -> tgd
      qhw XOR tgd -> z09
      pbm OR djm -> kpj
      x03 XOR y03 -> ffh
      x00 XOR y04 -> ntg
      bfw OR bqk -> z06
      nrd XOR fgs -> wpb
      frj XOR qhw -> z04
      bqk OR frj -> z07
      y03 OR x01 -> nrd
      hwm AND bqk -> z03
      tgd XOR rvg -> z12
      tnw OR pbm -> gnj
      """;

  println(computeNumber(createStates(input)));
}
