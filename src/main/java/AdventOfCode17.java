enum Opcode {
  adv, bxl, bst, jnz, bxc, out, bdv, cdv
}

static final Opcode[] OPCODES = Opcode.values();

static class Interpreter {
  int ra, rb, rc;
  ArrayList<Integer> out = new ArrayList<>();

  Interpreter(int ra, int rb, int rc) {
    this.ra = ra;
    this.rb = rb;
    this.rc = rc;
  }

  int combo(int v) {
    return switch (v) {
      case 0, 1, 2, 3 -> v;
      case 4 -> ra;
      case 5 -> rb;
      case 6 -> rc;
      default -> throw new AssertionError();
    };
  }

  void run(int[] program) {
    for(var ip = 0; ip < program.length;) {
      switch (OPCODES[program[ip++]]) {
        case adv -> ra = ra / (1 << combo(program[ip++]));
        case bxl -> rb = rb ^ program[ip++];
        case bst -> rb = combo(program[ip++]) & 7;
        case jnz -> ip = ra == 0 ? ip + 1 : program[ip];
        case bxc -> { rb ^= rc; ip++; }
        case out -> out.add(combo(program[ip++]) & 7);
        case bdv -> rb = ra / (1 << combo(program[ip++]));
        case cdv -> rc = ra / (1 << combo(program[ip++]));
      }
    }
  }
}



void main() {
  var input = """
      Register A: 729
      Register B: 0
      Register C: 0
      
      Program: 0,1,5,4,3,0
      """;

  var pattern = Pattern.compile("\\d+");
  var values = input.lines().flatMapToInt(l -> pattern.matcher(l).results().mapToInt(r -> Integer.parseInt(r.group()))).toArray();
  var program = Arrays.stream(values).skip(3).toArray();
  var interpreter = new Interpreter(values[0], values[1], values[2]);
  interpreter.run(program);
  println(interpreter.out.stream().map(String::valueOf).collect(Collectors.joining(",")));
}