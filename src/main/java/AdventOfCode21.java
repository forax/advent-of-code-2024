record Pos(int x, int y) {}
record Pad(Map<Character, Pos> data) {}
record Pair(char c1, char c2) {}

void permutations(String prefix, char c1, int r1, char c2, int r2, List<String> list) {
  if (r1 == 0 && r2 == 0)  {
    list.add(prefix + 'A');
    return;
  }
  if (r1 != 0) {
    permutations(prefix + c1, c1, r1 - 1, c2, r2, list);
  }
  if (r2 != 0) {
    permutations(prefix + c2, c1, r1, c2, r2 - 1, list);
  }
}

List<String> permutations(char c1, int r1, char c2, int r2) {
  var list = new ArrayList<String>();
  permutations("", c1, r1, c2, r2, list);
  return list;
}

char dir(int dv, char c1, char c2) {
  return switch (Integer.signum(dv)) {
    case 1 -> c1;
    case 0 -> 'X';
    case -1 -> c2;
    default -> throw new AssertionError();
  };
}

List<String> moves(Pos start, Pos end) {
  var dx = start.x - end.x;
  var dy = start.y - end.y;
  var cx = dir(dx, '<', '>');
  var cy = dir(dy, '^', 'v');
  return permutations(cx, Math.abs(dx), cy, Math.abs(dy));
}

boolean valid(Pos start, Pos poison, String move) {
  var pos = start;
  for(var i = 0; i < move.length() - 1; i++) {
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

void sequencesFromPairs(Pad pad, String prefix, int index, List<Pair> pairs, List<String> list) {
  if (index == pairs.size()) {
    list.add(prefix);
    return;
  }
  var pair = pairs.get(index);
  var start = pad.data.get(pair.c1);
  var end = pad.data.get(pair.c2);
  var moves = moves(start, end);
  var poison = pad.data.get(' ');
  var validMoves = moves.stream().filter(m -> valid(start, poison, m)).toList();
  for(var move : validMoves) {
    sequencesFromPairs(pad,prefix + move, index + 1, pairs, list);
  }
}

List<String> sequencesFromPairs(Pad pad, List<Pair> pairs) {
  var list = new ArrayList<String>();
  sequencesFromPairs(pad, "", 0, pairs, list);
  return list;
}

List<String> sequences(Pad pad, String word) {
  var w = "A" + word;
  var pairs = IntStream.range(0, w.length()).mapToObj(w::charAt)
      .gather(Gatherers.windowSliding(2))
      .map(l -> new Pair(l.getFirst(), l.getLast()))
      .toList();
  return sequencesFromPairs(pad, pairs);
}

List<String> sequences(Pad pad, List<String> words) {
  return words.stream().flatMap(w -> sequences(pad, w).stream()).toList();
}

String shortestSequence(List<String> sequences) {
  return sequences.stream().min(Comparator.comparingInt(String::length)).orElseThrow();
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
  var keyPad = new Pad(IntStream.range(0, 4).boxed().flatMap(j -> IntStream.range(0, 3).mapToObj(i -> new Pos(i, j)))
      .collect(Collectors.toMap(p -> keys.get(p.y).charAt(p.x), p -> p)));
  var dirs = List.of(" ^A", "<v>");
  var dirPad = new Pad(IntStream.range(0, 2).boxed().flatMap(j -> IntStream.range(0, 3).mapToObj(i -> new Pos(i, j)))
      .collect(Collectors.toMap(p -> dirs.get(p.y).charAt(p.x), p -> p)));

  println(input.lines()
      .mapToInt(w -> shortestSequence(sequences(dirPad, sequences(dirPad, sequences(keyPad, w)))).length() *
          Integer.parseInt(w.substring(0, w.length() - 1)))
      .sum());
}
