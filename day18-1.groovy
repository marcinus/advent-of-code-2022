def testData = """2,2,2
1,2,2
3,2,2
2,1,2
2,3,2
2,2,1
2,2,3
2,2,4
2,2,6
1,2,5
3,2,5
2,1,5
2,3,5"""


def countFreeSides(List<String> input) {
    def cubes = input.collect { it.split(',').collect { it as int } }
    cubes.sum {
        (cubes.contains([it[0]+1, it[1], it[2]]) ? 0 : 1) +
        (cubes.contains([it[0]-1, it[1], it[2]]) ? 0 : 1) +
        (cubes.contains([it[0], it[1]+1, it[2]]) ? 0 : 1) +
        (cubes.contains([it[0], it[1]-1, it[2]]) ? 0 : 1) +
        (cubes.contains([it[0], it[1], it[2]+1]) ? 0 : 1) +
        (cubes.contains([it[0], it[1], it[2]-1]) ? 0 : 1)
    }
}

println countFreeSides(testData.split('\n') as List)
println countFreeSides(new File('input/day18.txt').readLines())