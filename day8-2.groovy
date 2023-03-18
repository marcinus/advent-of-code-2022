def testInput = """30373
25512
65332
33549
35390
"""

def calculateScenicScore(List<String> lines, int y, int x) {
    def score = [], w = lines[0].size(), h = lines.size()
    def yp = y-1, i = 0
    while(yp >= 0 && lines[yp][x] < lines[y][x]) { i++
        yp--
    }
    if(yp >= 0) i++
    score << i
    yp = y+1
    i = 0
    while(yp < h && lines[yp][x] < lines[y][x]) {
        yp++
        i++
    }
    if(yp < h) i++
    score << i
    def xp = x-1
    i = 0 
    while(xp >= 0 && lines[y][xp] < lines[y][x]) {
        xp--
        i++
    }
    if(xp >= 0) i++
    score << i
    xp = x+1
    i = 0
    while(xp < w && lines[y][xp] < lines[y][x]) { xp++
        i++
    }
    if(xp < h) i++
    score << i
    println "$y $x $score"
    score[0]*score[1]*score[2]*score[3]
}


def getNumberOfVisibleTrees(List<String> lines) {
    int w = lines[0].size()
    int h = lines.size()
    (0..<h).collect { y ->
         (0..<w).collect { x ->  
            def s = calculateScenicScore(lines, y, x)   
            println "$y $x $s"
            s
        }.max()
    }.max()
}

assert 8 == getNumberOfVisibleTrees(testInput.split('\n') as List)
println getNumberOfVisibleTrees(new File('input/day8.txt').readLines())