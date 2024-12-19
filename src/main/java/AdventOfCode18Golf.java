void main() {
  var input = """
      5,4
      4,2
      4,5
      3,0
      2,1
      6,3
      2,4
      1,5
      0,6
      3,3
      2,6
      5,1
      1,2
      5,5
      2,5
      6,5
      1,4
      0,4
      6,4
      1,1
      6,1
      1,0
      0,5
      1,6
      2,0
      """;
  var width = 7;
  var height = 7;
  var bytes = 12;

  var pattern = Pattern.compile("(\\d+)");
  var walls = input.lines()
      .map(l -> pattern.matcher(l).results().map(r -> Integer.parseInt(r.group())).toList())
      .limit(bytes).collect(Collectors.toSet());
  var dirs = new int[][] { {0, -1}, {1, 0}, {0, 1}, {-1, 0} };
  var stateMap = new HashMap<List<Integer>, Integer>();
  record Work(List<Integer> p, int dist) { }
  var queue = new ArrayDeque<Work>() {{ add(new Work(List.of(0, 0), 0)); }};
  Stream.generate(queue::poll).takeWhile(Objects::nonNull)
      .filter(w -> !walls.contains(w.p)
          && w.p.get(0) >= 0 && w.p.get(0) < width && w.p.get(1) >= 0 && w.p.get(1) < height
          && w.dist != stateMap.getOrDefault(w.p, -1) && stateMap.merge(w.p, w.dist, Math::min) == w.dist)
      .forEach(w -> { for(var dir : dirs) {
          queue.offer(new Work(List.of(w.p.get(0) + dir[0], w.p.get(1) + dir[1]), w.dist + 1));
      }});
  println(stateMap.get(List.of(width - 1, height - 1)));
}
