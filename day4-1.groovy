def testData = """2-4,6-8
2-3,4-5
5-7,7-9
2-8,3-7
6-6,4-6
2-6,4-8
"""

def doesFirstContainSecond(int startA, int endA, int startB, int endB) {
    startA <= startB && endA >= endB
}

def doesOneContainTheOther(int startA, int endA, int startB, int endB) {
    doesFirstContainSecond(startA, endA, startB, endB) || doesFirstContainSecond(startB, endB, startA, endA)
}

def doesOneContainTheOther(String a, String b) {
    def aParts = a.split('-')
    def bParts = b.split('-')
    assert 2 == aParts.size()
    assert 2 == bParts.size()
    def startA = aParts[0] as int
    def endA = aParts[1] as int
    def startB = bParts[0] as int
    def endB = bParts[1] as int
    doesOneContainTheOther(startA, endA, startB, endB)
}

def countFullContains(List<String> lines) {
    lines.count {
        def items = it.split(',')
        assert items.size() == 2
        doesOneContainTheOther(items[0], items[1])
    }
}

assert 2 == countFullContains(testData.split('\n') as List )
println countOverLaps(new File('input/day4.txt').readLines())