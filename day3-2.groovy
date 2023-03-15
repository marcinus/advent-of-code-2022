def getPriority(String itemType) {
    int type = (int) itemType;
    if(type >= 65 && type <= 90) return type-65+27;
    if(type >= 97 && type <= 122) return type-96;
    throw new Exception("Invalid itemType: $itemType $type");
}

def testData = """vJrwpWtwJgWrhcsFMMfFFhFp
jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
PmmdzqPrVvPwwTWBwg
wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
ttgJtRGJQctTZtZT
CrZsJsPPZsGzwwsLwLmpwMDw
"""


def calculateSumOfPrioritiesOfGroupsOfThree(List<String> lines) {
    lines.collate(3).sum { threeLines ->
        def line1 = threeLines[0] as Set
        def line2 = threeLines[1] as Set
        def line3 = threeLines[2] as Set
        def result = line1.intersect(line2).intersect(line3)
        assert result.size() == 1
        getPriority(result[0])
    }
}

assert 70 == calculateSumOfPrioritiesOfGroupsOfThree(testData.split('\n') as List)
println calculateSumOfPrioritiesOfGroupsOfThree(new File('input/day3.txt').readLines())