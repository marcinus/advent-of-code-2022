def testData = """>>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>"""

def shapes = [
"""####""",
""".#.
###
.#.""",
"""..#
..#
###""",
"""#
#
#
#""",
"""##
##
"""
]

abstract class Figure {
    def toCoords(pos) {
        fields().collect { [pos[0]+it[0], pos[1]+it[1]] }
    }
        
    boolean pointConflicts(point, tower) {
        point[1] < 0 || point[1] > 6 || tower[point[0]][point[1]]
    }
    
    boolean conflictsAt(pos, tower) {
        toCoords(pos).any { pointConflicts(it, tower) }
    }
    
    int height() {
        fields().collect { it[0] }.max()+1
    }
    
    void putAt(pos, tower) {
        toCoords(pos).each { tower[it[0]][it[1]] = true }
    }
    
    abstract def fields()
}

class FigureA extends Figure {
    
    def fields = (0..3).collect { [0, it] }
    
    def fields() {
        fields
    }
}

class FigureB extends Figure {
    def fields = [[0, 1], [1, 0], [1, 1], [1, 2], [2, 1]]
    
    def fields() {
        fields
    }
}

class FigureC extends Figure {
    def fields = [[0, 0], [0, 1], [0, 2], [1, 2], [2, 2]]
    
    def fields() {
        fields
    }
}

class FigureD extends Figure {
    def fields = (0..3).collect { [it, 0] }
    
    def fields() {
        fields
    }
}

class FigureE extends Figure {
    def fields = [[0, 0], [1, 0], [0, 1], [1, 1]]
    
    def fields() {
        fields
    }
}


class Tetris {

    def FIGURES = [
        new FigureA(),
        new FigureB(),
        new FigureC(),
        new FigureD(),
        new FigureE(),
    ]
        
    def ROCKS_MAX=2022
    
    def HEIGHT_MAX=ROCKS_MAX*4

    def tower = new boolean[HEIGHT_MAX][7]
    def top = 0
    def fullTop = []
    def pos = [top+3, 2]
    def currentFigureCounter = 0
    
    def currentFigure() {
        FIGURES[currentFigureCounter]
    }
    
    def nextFigure() {
        currentFigureCounter = (currentFigureCounter + 1) % FIGURES.size()
    }
    
    def pushRight() {
       if(!currentFigure().conflictsAt([pos[0], pos[1]+1], tower)) {
           pos[1]++
       } 
    }
    
    def pushLeft() {
       if(!currentFigure().conflictsAt([pos[0], pos[1]-1], tower)) {
           pos[1]--
       } 
    }
    
    def pushDown() {
       def stopped = pos[0] == 0 || currentFigure().conflictsAt([pos[0]-1, pos[1]], tower)
       if(!stopped) {
           pos[0]--
       } else {
           currentFigure().putAt(pos, tower)
       }
       stopped 
    }
    
    def updateTop() {
        top = Math.max(top, pos[0]+currentFigure().height()-1)
    }
    
    def fullRow() {
        (0..<7).every { tower[top][it] }
    }
    
    def clear() {
        def newTower = new boolean[HEIGHT_MAX][7]
        def newTop = Math.max(pos[0]+currentFigure().height()-1, top)
        def base = pos[0]
        ((pos[0]+1)..newTop).each { h->
            (0..<7).each { w ->
                newTower[h-base][w] = tower[h][w]
            }
        }
        tower = newTower
        fullTop << pos[0]
        top = currentFigure().height()-1
        //top -= pos[0]
        // update top
    }
    
    def findCycle() {
        println "Reached cycle finding phase"
        new File('output.txt').write(print(HEIGHT_MAX))
        for(int i = 1; i < 600; i++) {
            def foundContradiction = false
            for(int start = HEIGHT_MAX-10; start >= 6000; start--) {
                if((0..<7).any { tower[start][it] != tower[start-i][it] }) {
                    foundContradiction = true
                    break
                }
            }
            if(!foundContradiction) {
                return i
            }
        }
    }
    
    def isArithmetic(container) {
        def first = container[0]
        def second = container[1]
        def difference = second - first
        def holds = true
        container.drop(2).inject(second) { prev, curr ->
            if(curr-prev != difference) holds = false
            curr
        }
        holds ? difference : -1
    }
    
    def simpleTetrisHeight(List<Character> jets, rocks) {
        pos = [3, 2]
        def figures = 0
        def i = 0
        def data = [:].withDefault { [] }
        while(figures < rocks) {
            if(jets[i] == '>') pushRight()
            else if(jets[i] == '<') pushLeft()
            else throw new RuntimeException("Wrong input!")
            def stopped = pushDown()   
            if(stopped) {
                figures++
                if(figures % 100 == 0) println figures
                updateTop()
                pos = [top+4, 2]
                nextFigure()
            }
            i = (i+1) % jets.size()
        }
        top+1
    }
    
    def tetrisHeight(List<Character> jets) {
        pos = [3, 2]
        def figures = 0
        def i = 0
        def data = [:].withDefault { [] }
        while(figures < ROCKS_MAX*5) {
            if(jets[i] == '>') pushRight()
            else if(jets[i] == '<') pushLeft()
            else throw new RuntimeException("Wrong input!")
            def stopped = pushDown()
            
            if(stopped) {
                figures++
                if(figures % 100 == 0) println figures
                if(fullRow()) {
                    clear()
                    def id = new Tuple3(i, figures % 5, top)
                    data[id] << [figures, fullTop.sum()]
                    println ". >> $top $i $figures"
                    //println "$top $fullTop"
                    //print(4)
                } else {
                    updateTop()   
                }
                if(top+4 >= HEIGHT_MAX) {
                    println findCycle()
                    return -1
                    // start searching for cycles phase
                 }
                pos = [top+4, 2]
                nextFigure()
            }
            i = (i+1) % jets.size()
        }
        //(fullTop.sum() ?: 0) + top + 1
        def result = data.findAll { k,v-> k[2] == 0 } .findResult { k, v ->
            def arithmetic = isArithmetic(v.collect {it[0]})
            def arithmeticHeight = isArithmetic(v.collect {it[1]})
            if(v) return [k[0], k[1], k[2], arithmetic, v.find { true }[0], v.find{true}[1], arithmeticHeight]
        }
        println result
        def xy= [(1000000000000 - result[4]) % result[3], (long)((long)(1000000000000 - result[4]) / (long)result[3])]
        println xy
        def height = new Tetris().simpleTetrisHeight(jets, result[4]+xy[0])
        def fullHeight = height + xy[1]*result[6]
        return fullHeight
    }
    
    def print(int height) {
        def builder = new StringBuilder()
        (0..<height).reverse().each { i->
            (0..<7).each { j->
                builder.append(tower[i][j] ? '#' : '.')            }
            builder.append('\n')
        }
        builder.toString()
    }
}

//Tetris tetris = new Tetris()
//println tetris.tetrisHeight(testData as List)
println (new Tetris().tetrisHeight(new File('input/day17.txt').text.trim() as List))