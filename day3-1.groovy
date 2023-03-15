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

def findRepeatingCharacterInBothParts(String line) {
    def counter = new HashSet<String>();
    (0..<(line.size()/2)).collect { it as int}.each {
        counter.add(line[it])
    }
    def repeatedItemIndex = (((line.size()/2))..<line.size()).collect { it as int }.find {
        counter.contains(line[it])
    }
    line[repeatedItemIndex]
}

def calculateSumOfPriorities(List<String> lines) {
    lines.sum { line ->
        def repeatedItemType = findRepeatingCharacterInBothParts(line)
        getPriority(repeatedItemType)
    }
}

println calculateSumOfPriorities(testData.split('\n') as List)
println calculateSumOfPriorities(new File('input/day3.txt').readLines())