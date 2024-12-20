record Match(String strip, int at) {
  boolean end() {
    return at == strip.length();
  }
  Match next(char letter) {
    if (strip.charAt(at) == letter) {
      return new Match(strip, at + 1);
    }
    return null;
  }

  @Override
  public String toString() {
    return strip + ":" + at;
  }
}

boolean match(List<String> stripes, String word) {
  var initMatches = stripes.stream().map(s -> new Match(s, 0)).collect(Collectors.toSet());
  var matches = initMatches;
  var end = false;
  for(var letter : word.toCharArray()) {
    end = false;
    var newMatches = new HashSet<Match>();
    for (var match : matches) {
      var next = match.next(letter);
      if (next == null) {
        continue;
      }
      if (next.end()) {
        newMatches.addAll(initMatches);
        end = true;
      } else {
        newMatches.add(next);
      }
    }
    matches = newMatches;
  }
  return end;
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

  var stripes = Arrays.stream(input.substring(0, input.indexOf('\n')).split(", ")).toList();
  //println(match(stripes, "bwr"));
  println(input.lines().skip(2).filter(w -> match(stripes, w)).count());
}
