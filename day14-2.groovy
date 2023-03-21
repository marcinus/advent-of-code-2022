def testData = """498,4 -> 498,6 -> 496,6
503,4 -> 502,4 -> 502,9 -> 494,9
"""

def print(map, minCoords, maxCoords) {
    (minCoords[1]..maxCoords[1]).each { y ->
        (minCoords[0]..maxCoords[0]).each { x ->
            print (map[x][y] == 1 ? '#' : ( map[x][y] == 0 ? '.' : 'o'))    
        }
        println()
    }
}

def min(A, B) {
    return (0..1).collect { i-> Math.min(A[i], B[i]) }
}

def max(A, B) {
    return (0..1).collect { i-> Math.max(A[i], B[i]) }
}

def exceeds(point, minCoords, maxCoords) {
    return (0..1).any { i -> point[i] < minCoords[i] } || (0..1).any { i -> point[i] > maxCoords[i] }
}

def isTaken(map, x, y) {
    return map[x][y] == -1 || map[x][y] == 1
}

def tryAdvance(map, point, floorLvl) {
    if(point[1]+1 == floorLvl) return point
    if(!isTaken(map, point[0], point[1]+1)) return [point[0], point[1]+1]
    if(!isTaken(map, point[0]-1, point[1]+1)) return [point[0]-1, point[1]+1]
    if(!isTaken(map, point[0]+1, point[1]+1)) return [point[0]+1, point[1]+1]
    return point 
}


def unitsOfSandThatComeToRest(List<String> lines) {
    def map = [:].withDefault { [:].withDefault { 0 } }
    def firstCoords = lines[0].split(' -> ')[0].split(',').collect { it as int }
    def minCoords = [500, 0]
    def maxCoords = [500, 0]
    lines.collect {
        it.split(' -> ').collect { it.split(',').collect { it as int } }.inject { prev, next ->
            minCoords = min(min(minCoords, prev), next)
            maxCoords = max(max(maxCoords, prev), next)
            //println "Moving from $prev to $next"
            (prev[0]..<(next[0]+1)).each { map[it][prev[1]] = 1 }
            (prev[1]..<(next[1]+1)).each { map[prev[0]][it] = 1 }
            (next[0]..<prev[0]+1).each { map[it][prev[1]] = 1 }
            (next[1]..<prev[1]+1).each { map[prev[0]][it] = 1 }
            next
        }
    }
    def point = [500, 0]
    while(true) {
        def nextPoint = tryAdvance(map, point, maxCoords[1]+2)
        if(nextPoint == point) { 
            map[point[0]][point[1]] = -1
            if(point == [500, 0]) break
            point = [500, 0]
        } else {
            point = nextPoint
        }
    }
    map.collect { k, v -> v.count { it.value == -1 } }.sum()
}


assert 93 == unitsOfSandThatComeToRest(testData.split('\n') as List)
println unitsOfSandThatComeToRest(new File('input/day14.txt').readLines())