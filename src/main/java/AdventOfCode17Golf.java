void main() {
  var input = """
      Register A: 729
      Register B: 0
      Register C: 0
      
      Program: 0,1,5,4,3,0
      """;

  var pattern = Pattern.compile("\\d+");
  var program = input.lines().flatMapToInt(l -> pattern.matcher(l).results().mapToInt(r -> Integer.parseInt(r.group()))).toArray();
  var rs = new int[] { 0, 1, 2, 3, program[0], program[1], program[2] };
  int A = 4, B = 5, C = 6;
  final int adv = 0, bxl = 1, bst = 2, jnz = 3, bxc = 4, out = 5, bdv = 6, cdv = 7;
  println(Stream.of(program).mapMultiToInt((p, c) -> {
    for(var ip = 3; ip < p.length;) {
      switch (p[ip++]) {
        case adv -> rs[A] = rs[A] / (1 << rs[p[ip++]]);
        case bxl -> rs[B] = rs[B] ^ p[ip++];
        case bst -> rs[B] = rs[p[ip++]] & 7;
        case jnz -> ip = rs[A] == 0 ? ip + 1 : 3 + p[ip];
        case bxc -> { rs[B] ^= rs[C]; ip++; }
        case out -> c.accept(rs[p[ip++]] & 7);
        case bdv -> rs[B] = rs[A] / (1 << rs[p[ip++]]);
        case cdv -> rs[C] = rs[A] / (1 << rs[p[ip++]]);
      }
    }
  }).mapToObj(String::valueOf).collect(Collectors.joining(",")));
}