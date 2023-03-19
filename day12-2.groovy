def testCase = """Sabqponm
abcryxxl
accszExk
acctuvwj
abdefghi
"""

def toTuple(position) {
    new Tuple2(position[0], position[1])
}

def h(map, position) {
    map[position[0]][position[1]]
}

def inMap(map, position) {
    position[0] >= 0 && position[1] >= 0 && position[0] < map.size() && position[1] < map[position[0]].size()
}

def reacheable(map, from, to) {
    inMap(map, from) && inMap(map, to) && ((h(map, from) as int) >= h(map, to) || h(map, from)+1 == h(map, to))
}

def nextPos(position, dy, dx) {
    return [position[0]+dy, position[1]+dx]
}

def bfs(endNode, map) {
    def inputNodes = [endNode] as Queue
    def visited = [:]
    visited[toTuple(endNode)] = 0
    while(!inputNodes.isEmpty()) {
        def node = inputNodes.pop()
        [[-1, 0], [0, 1], [0, -1], [1, 0]].each { move ->
            def dy = move[0]
            def dx = move[1]
                def newPos = nextPos(node, dy, dx)
                if(reacheable(map, newPos, node) && !visited.containsKey(toTuple(newPos))) {
                    visited[toTuple(newPos)] = visited[toTuple(node)]+1
                    inputNodes.add(newPos)
                }
        }
    }
    visited
}

def fewestStepsOnPath(List<String> lines) {
    def startPos = []
    def endPos
    def map = [:].withDefault { [] }
    lines.eachWithIndex { line, y ->
        line.eachWithIndex { c, x ->
            if(c == 'S' || c == 'a') {
                startPos << [y, x]
                map[y][x] = (int) 'a' 
            } else if(c == 'E') {
                endPos = [y, x]
                map[y][x] = (int)'z'
            } else {
                map[y][x] = (int)c
            }
        }
    }
    def visited = bfs(endPos, map)
    startPos.collect { visited[toTuple(it)] }.min()
}

assert 29 == fewestStepsOnPath(testCase.split('\n') as List)
println fewestStepsOnPath(new File('input/day12.txt').readLines())