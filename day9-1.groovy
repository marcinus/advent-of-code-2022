def testInput = """R 4
U 4
L 3
D 1
R 4
D 1
L 5
R 2
"""

def moveOnce(H, T, dir, visited) {
    switch(dir) {
        case 'L':
            if(H[1] < T[1]) {
                T[1]--
                T[0]=H[0]
                visited.add(new Tuple2(T[0], T[1]))
            }
            H[1]--
            break
        case 'R':
            if(H[1] > T[1]) {
                T[1]++
                T[0]=H[0]
                visited.add(new Tuple2(T[0], T[1]))
            }
            H[1]++
            break
        case 'U':
            if(H[0] > T[0]) {
                T[0]++
                T[1]=H[1]
                visited.add(new Tuple2(T[0], T[1]))
            }
            H[0]++
            break
        case 'D':
            if(H[0] < T[0]) {
                T[0]--
                T[1]=H[1]
                visited.add(new Tuple2(T[0], T[1]))
            }
            H[0]--
            break
        default:
            throw new RuntimeException("Invalid")
    }
}

def move(H, T, dir, len, visited) {
    len.times {
        moveOnce(H, T, dir, visited)
    }
}

def numberOfVisitedPositions(List<String> lines) {
    def H = [0, 0]
    def T = [0, 0]
    def visited = [new Tuple2(0, 0)] as Set
    lines.each {
        def dirAndLen = it.split(' ')
        def dir = dirAndLen[0]
        def len = dirAndLen[1] as int
        move(H, T, dir, len, visited)
    }
    println "$H $T $visited"
    visited.size()
}

assert 13 == numberOfVisitedPositions(testInput.split('\n') as List)
println numberOfVisitedPositions(new File('input/day9.txt').readLines())