void main() {
  var input = """
      p=0,4 v=3,-3
      p=6,3 v=-1,-3
      p=10,3 v=-1,2
      p=2,0 v=2,-1
      p=0,0 v=1,3
      p=3,0 v=-2,-2
      p=7,6 v=-1,-3
      p=3,0 v=-1,-2
      p=9,3 v=2,3
      p=7,3 v=-1,2
      p=2,4 v=2,-3
      p=9,5 v=-3,-3
      """;
  var width = 11;
  var height = 7;

  var pattern = Pattern.compile("(-?\\d+)");
  println(input.lines()
      .map(l -> pattern.matcher(l).results().mapToInt(r -> Integer.parseInt(r.group())).toArray())
      .map(robot -> {
        for(var i = 0; i < 100; i++) {
          robot[0] = ((robot[0] + robot[1]) % width + width) % width;
          robot[2] = ((robot[2] + robot[3]) % height + height) % height;
        }
        return List.of(Integer.signum(robot[0] - width / 2), Integer.signum(robot[2] - height / 2));
      })
      .filter(q -> q.getFirst() != 0 && q.getLast() != 0)
      .collect(Collectors.groupingBy(q -> q, Collectors.counting()))
      .values().stream().reduce(1L, (a, b) -> a * b));
}