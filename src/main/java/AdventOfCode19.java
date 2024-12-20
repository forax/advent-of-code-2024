static class Node {
  final HashMap<Character, Node> map = new HashMap<>();
  boolean end;

  Node append(char c) {
    return map.computeIfAbsent(c, _ -> new Node());
  }

  Node next(char c) {
    return map.get(c);
  }
}

void insert(Node root, String word) {
  var node = root;
  for(var c : word.toCharArray()) {
    node = node.append(c);
  }
  node.end = true;
}

boolean match(Node root, String word) {
  var matches = new HashSet<>(Set.of(root));
  for(var c : word.toCharArray()) {
    var newMatches = new HashSet<Node>();
    for(var match : matches) {
      var next = match.next(c);
      if (next != null) {
        newMatches.add(next);
        if (next.end) {
          newMatches.add(root);
        }
      }
    }
    matches = newMatches;
  }
  return matches.stream().anyMatch(n -> n.end);
}

void main() {
  var input = """
      r, wr, b, g, bwu, rb, gb, br
      
      brwrr
      bggr
      gbbr
      rrbgbr
      ubwu
      bwurrg
      brgr
      bbrgwb
      """;

  var root = new Node();
  Arrays.stream(input.substring(0, input.indexOf('\n')).split(", ")).forEach(w -> insert(root, w));
  println(input.lines().skip(2).filter(w -> match(root, w)).count());
}
