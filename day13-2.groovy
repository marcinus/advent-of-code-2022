def testCase = """[1,1,3,1,1]
[1,1,5,1,1]

[[1],[2,3,4]]
[[1],4]

[9]
[[8,7,6]]

[[4,4],4,4]
[[4,4],4,4,4]

[7,7,7,7]
[7,7,7]

[]
[3]

[[[]]]
[[]]

[1,[2,[3,[4,[5,6,7]]]],8,9]
[1,[2,[3,[4,[5,6,0]]]],8,9]
"""

def compare(A, B) {
    if(A instanceof Integer && B instanceof Integer) {
        return Math.signum(A - B)
    } else if(A instanceof List && B instanceof Integer) {
        return compare(A, [B])
    } else if(A instanceof Integer && B instanceof List) {
        return compare([A], B)
    } else if(A instanceof List && B instanceof List) {
        def minSize = Math.min(A.size(), B.size())
        for(int i = 0; i < minSize; i++) {
            def result = compare(A[i], B[i])
            if(result != 0) return result
        }
        if(A.size() > minSize) return 1
        if(B.size() > minSize) return -1
        return 0
    } else {
        throw new RuntimeException('Invalid!')
    }
}

def productOfIndeciesOfDividerPairs(List<String> lines) {
    def evaluatedLines = lines.findAll { it.size() > 0 }.collect { Eval.me(it) }
    evaluatedLines << [[2]]
    evaluatedLines << [[6]]
    def sortedLines = evaluatedLines.sort { a, b -> compare(a, b) }
    (sortedLines.findIndexOf { it instanceof List && it.size() == 1 && it[0] instanceof List && it[0].size() == 1 && it[0][0] == 2 } + 1) * 
    (sortedLines.findIndexOf { it instanceof List && it.size() == 1 && it[0] instanceof List && it[0].size() == 1 && it[0][0] == 6 } + 1)
}

assert 140 == productOfIndeciesOfDividerPairs(testCase.split('\n') as List)
println productOfIndeciesOfDividerPairs(new File('input/day13.txt').readLines())