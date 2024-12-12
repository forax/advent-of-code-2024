import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;

void main() {
  var input = """
     47|53
     97|13
     97|61
     97|47
     75|29
     61|13
     75|53
     29|13
     97|29
     53|29
     61|53
     97|53
     61|29
     47|13
     75|47
     97|75
     47|61
     75|61
     47|29
     75|13
     53|13
     
     75,47,61,53,29
     97,61,53,29,13
     75,29,13
     75,97,47,61,53
     61,13,29
     97,13,75,29,47
     """;

  var blankLineIndex = input.indexOf("\n\n");
  var ruleMap = input.substring(0, blankLineIndex).lines()
      .map(l -> l.split("\\|"))
      .collect(groupingBy(t -> parseInt(t[1]), mapping(t -> parseInt(t[0]), toSet())));
  println(input.substring( blankLineIndex + 2).lines()
      .map(l -> Arrays.stream(l.split(",")).map(Integer::parseInt).toList())
      .filter(update -> IntStream.range(0, update.size() - 1).noneMatch(i -> {
          var page = update.get(i);
          var rule = ruleMap.getOrDefault(page, Set.of());
          return IntStream.range(i + 1, update.size()).mapToObj(update::get).anyMatch(rule::contains);
      }))
      .mapToInt(update -> update.get(update.size() / 2))
      .sum());
}