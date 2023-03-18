def testInput = """30373
25512
65332
33549
35390
"""


def getNumberOfVisibleTrees(List<String> lines) {
    def visibility = [:].withDefault { [] as Set }
    def w = lines[0].size()
    def h = lines.size()
    lines.eachWithIndex { line, y ->
        def currentMax = -1
        line.eachWithIndex { it, x ->
            if(it > currentMax) {
                println line
                visibility[y] << x
                currentMax = it
            }
        }
        currentMax = -1
        line.reverse().eachWithIndex { it, x ->
            if(it > currentMax) {
                visibility[y] << w-x-1
                currentMax = it
            }
        }
    }
    (0..<w).each { x ->
        def currentMax = -1
        lines.eachWithIndex { line, y ->
            if(line[x] > currentMax) {
                visibility[y] << x
                currentMax = line[x]
            }    
        }
        currentMax = -1
        lines.reverse().eachWithIndex { line, y ->
            if(line[x] > currentMax) {
                visibility[h-y-1] << x
                currentMax = line[x]
            }    
        }
    }
    (0..<h).each { y -> 
        (0..<w).each { x ->
            print (visibility[y].contains(x) ? lines[y][x] : ' ')
        }
        println()
    }
    visibility.collect { it.value.size() }.sum()
}

assert 21 == getNumberOfVisibleTrees(testInput.split('\n') as List)
println getNumberOfVisibleTrees(new File('input/day8.txt').readLines())