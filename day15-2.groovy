def testData = """Sensor at x=2, y=18: closest beacon is at x=-2, y=15
Sensor at x=9, y=16: closest beacon is at x=10, y=16
Sensor at x=13, y=2: closest beacon is at x=15, y=3
Sensor at x=12, y=14: closest beacon is at x=10, y=16
Sensor at x=10, y=20: closest beacon is at x=10, y=16
Sensor at x=14, y=17: closest beacon is at x=10, y=16
Sensor at x=8, y=7: closest beacon is at x=2, y=10
Sensor at x=2, y=0: closest beacon is at x=2, y=10
Sensor at x=0, y=11: closest beacon is at x=2, y=10
Sensor at x=20, y=14: closest beacon is at x=25, y=17
Sensor at x=17, y=20: closest beacon is at x=21, y=22
Sensor at x=16, y=7: closest beacon is at x=15, y=3
Sensor at x=14, y=3: closest beacon is at x=15, y=3
Sensor at x=20, y=1: closest beacon is at x=15, y=3
"""

regex = ~/Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)/

def manhattanDistance(A, B) {
    return Math.abs(A[0]-B[0]) + Math.abs(A[1]-B[1])
}

def markLine(sensor, beacon, testedY) {
    def d = manhattanDistance(sensor, beacon)
    def dDiff = d - Math.abs(testedY - sensor[1])
    if(dDiff >= 0) {
        [sensor[0]-dDiff, sensor[0]+dDiff]
    }
}

def getBeaconPosition(sensorBeacons, int testedY) {
    
    def intervals = sensorBeacons.findResults { sensorBeacon ->
        markLine(sensorBeacon[0], sensorBeacon[1], testedY)
    }.sort { it[1] }.sort { it[0] }
    def marker = Math.max(intervals[0][1], 0)
    intervals.drop(1).find {
        if(it[0] > marker+1) return true
        if(it[1] > marker) marker = Math.max(marker, it[1])
        return false
    }
    
    marker+1
}

def parseSensorsAndBeacons(List<String> lines) {
    lines.collect { line ->
        def positions = (line =~ regex)[0]
        [[positions[1] as int, positions[2] as int], [positions[3] as int, positions[4] as int]]
    }
}

def findBeacon(List<String> lines, int xMax, int yMax) {
    def sensorBeacons = parseSensorsAndBeacons(lines)
    
    def yp = (0..yMax).find { y ->
        def x = getBeaconPosition(sensorBeacons, y)
        x <= xMax
    }
    
    def xp = getBeaconPosition(sensorBeacons, yp)
    
    4000000*(xp as long)+yp as long
}


assert 56000011 == findBeacon(testData.split('\n') as List, 20, 20)
println findBeacon(new File('input/day15.txt').readLines(), 4000000, 4000000)