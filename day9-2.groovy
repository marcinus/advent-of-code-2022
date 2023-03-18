def testInput = """R 4
U 4
L 3
D 1
R 4
D 1
L 5
R 2
"""

def secondTestInput = """R 5
U 8
L 8
D 3
R 17
D 10
L 25
U 20
"""

def closestNeighbour(head, oldTail) {
    def closest = []
    def minDist = 1000
    (-1..1).each { y ->
        (-1..1).each { x-> 
            def dist = distance2(head, [oldTail[0]+y, oldTail[1]+x])
            if(dist < minDist) {
                minDist = dist
                closest = [oldTail[0]+y, oldTail[1]+x]
            }
        }
    }
    closest
}


def show(segments) {
    (-20..20).each { y -> 
        (-20..20).each { x ->
            def item = segments.findIndexOf { it[0]==y && it[1] == x } 
            print ( item != -1 ? item : '.')
        }
        println()
    }
}


def distance2(A, B) {
    Math.abs(A[0]-B[0]) + Math.abs(A[1]-B[1])
}

def distance(A, B) {
    Math.max(Math.abs(A[0]-B[0]), Math.abs(A[1]-B[1]))
}

def moveOnce(segments, dir, visited) {
    def newSegments = []
    switch(dir) {
        case 'L':
            newSegments.add([segments[0][0], segments[0][1]-1])
            break
        case 'R':
            newSegments.add([segments[0][0], segments[0][1]+1])
            break
        case 'U':
            newSegments.add([segments[0][0]+1, segments[0][1]])
            break
        case 'D':
            newSegments.add([segments[0][0]-1, segments[0][1]])
            break
        default:
            throw new RuntimeException("Invalid")
    }
    
    (1..<(segments.size())).each {
        if(distance(newSegments[it-1], segments[it]) > 1) {
            def newSegment = closestNeighbour(newSegments[it-1], segments[it])
            newSegments.add(newSegment)
        } else {
            newSegments.add(segments[it])
        }
    }
    def lastSegment = newSegments.last()
    visited.add(new Tuple2(lastSegment[0], lastSegment[1]))
    println "${newSegments}"
    newSegments
}

def move(segments, dir, len, visited) {
    len.times {
        segments = moveOnce(segments, dir, visited)
    }
    //show(segments)
    segments
}

def numberOfVisitedPositions(List<String> lines) {
    def segments = (1..10).collect { [0, 0] }
    def visited = [new Tuple2(0, 0)] as Set
    lines.each {
        def dirAndLen = it.split(' ')
        def dir = dirAndLen[0]
        def len = dirAndLen[1] as int
        segments = move(segments, dir, len, visited)
    }
    visited.size()
}

assert 1 == numberOfVisitedPositions(testInput.split('\n') as List)
assert 36 == numberOfVisitedPositions(secondTestInput.split('\n') as List)
println numberOfVisitedPositions(new File('input/day9.txt').readLines())