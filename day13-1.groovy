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

def sumOfIndecisedOfPairsOfProperOrder(List<String> lines) {
    def sum = 0
    lines.collate(3).eachWithIndex { pair, index ->
        def line1 = Eval.me(pair[0])
        def line2 = Eval.me(pair[1])
        if(compare(line1, line2) == -1) sum += index+1
    }
    sum
}

assert 13 == sumOfIndecisedOfPairsOfProperOrder(testCase.split('\n') as List)
println sumOfIndecisedOfPairsOfProperOrder(new File('input/day13.txt').readLines())