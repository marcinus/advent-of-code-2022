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

def markLine(result, sensor, beacon, testedY) {
    def d = manhattanDistance(sensor, beacon)
    def dDiff = d - Math.abs(testedY - sensor[1])
    if(dDiff >= 0) {
        result << [sensor[0]-dDiff, sensor[0]+dDiff]
    }
    result
}

def getNumberOfPositionsWhereTheBeaconCannotBePresent(int testedY, List<String> lines) {
    def sensorBeacons = lines.collect { line ->
        def positions = (line =~ regex)[0]
        [[positions[1] as int, positions[2] as int] , [positions[3] as int, positions[4] as int]]
    }
    
    def set = sensorBeacons.inject([]) { result, sensorBeacon ->
        markLine(result, sensorBeacon[0], sensorBeacon[1], testedY)
    }.inject([] as Set) { set, interval ->
        (interval[0]..interval[1]).each { set.add(it) }
        set
    }
    
    sensorBeacons.collect { it[1] }.findAll { it[1] == testedY } .each {
        set.remove(it[0])
    }
    set.size()
}


assert 26 == getNumberOfPositionsWhereTheBeaconCannotBePresent(10, testData.split('\n') as List)
println getNumberOfPositionsWhereTheBeaconCannotBePresent(2000000, new File('input/day15.txt').readLines())