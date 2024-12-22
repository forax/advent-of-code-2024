record Pos(int x, int y) { }

void permutations(String prefix, char c1, int r1, char c2, int r2, Consumer<String> c) {
  if (r1 == 0 && r2 == 0) {
    c.accept(prefix + 'A');
    return;
  }
  if (r1 != 0) {
    permutations(prefix + c1, c1, r1 - 1, c2, r2, c);
  }
  if (r2 != 0) {
    permutations(prefix + c2, c1, r1, c2, r2 - 1, c);
  }
}

boolean valid(Pos start, Pos poison, String move) {
  var pos = start;
  for (var i = 0; i < move.length() - 1; i++) {
    if (pos.equals(poison)) {
      return false;
    }
    pos = switch (move.charAt(i)) {
      case '<' -> new Pos(pos.x - 1, pos.y);
      case '^' -> new Pos(pos.x, pos.y - 1);
      case '>' -> new Pos(pos.x + 1, pos.y);
      case 'v' -> new Pos(pos.x, pos.y + 1);
      default -> throw new AssertionError();
    };
  }
  return true;
}

char dir(int dv, char c1, char c2) {
  return switch (Integer.signum(dv)) {
    case 1 -> c1;
    case 0 -> 'X';
    case -1 -> c2;
    default -> throw new AssertionError();
  };
}

void sequencesFromPairs(Map<Character, Pos> pad, String text, int i, List<List<Character>> pairs, Consumer<String> c) {
  if (i == pairs.size()) {
    c.accept(text);
    return;
  }
  var pair = pairs.get(i);
  var start = pad.get(pair.getFirst());
  var end = pad.get(pair.getLast());
  var dx = start.x - end.x;
  var dy = start.y - end.y;
  permutations("", dir(dx, '<', '>'), Math.abs(dx), dir(dy, '^', 'v'), Math.abs(dy), move -> {
    if (valid(start, pad.get(' '), move)) {
      sequencesFromPairs(pad, text + move, i + 1, pairs, c);
    }
  });
}

void sequences(Map<Character, Pos> pad, String word, Consumer<String> c) {
  var w = "A" + word;
  var pairs = IntStream.range(0, w.length()).mapToObj(w::charAt).gather(Gatherers.windowSliding(2)).toList();
  sequencesFromPairs(pad, "", 0, pairs, c);
}

void main() {
  var input = """
      029A
      980A
      179A
      456A
      379A
      """;

  var keys = List.of("789", "456", "123", " 0A");
  var keyPad = IntStream.range(0, 4).boxed().flatMap(j -> IntStream.range(0, 3).mapToObj(i -> new Pos(i, j)))
      .collect(Collectors.toMap(p -> keys.get(p.y).charAt(p.x), p -> p));
  var dirs = List.of(" ^A", "<v>");
  var dirPad = IntStream.range(0, 2).boxed().flatMap(j -> IntStream.range(0, 3).mapToObj(i -> new Pos(i, j)))
      .collect(Collectors.toMap(p -> dirs.get(p.y).charAt(p.x), p -> p));
  println(input.lines()
      .mapToInt(w -> Stream.of(w).<String>mapMulti((s, c) -> sequences(keyPad, s,
          s2 -> sequences(dirPad, s2,
              s3 -> sequences(dirPad, s3, c)))
      ).mapToInt(String::length).min().orElseThrow() * Integer.parseInt(w.substring(0, w.length() - 1)))
      .sum());
}
