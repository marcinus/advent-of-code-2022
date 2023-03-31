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
        fields().collect { it[0] }.max()
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
    def fullTop = 0
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
        top = Math.max(top, pos[0]+currentFigure().height())
    }
    
    def fullRow() {
        (0..<7).every { tower[top][it] }
    }
    
    def clear() {
        def newTower = new boolean[HEIGHT_MAX][7]
        (0..<2).each { h->
            (0..<7).each { w ->
                newTower[h][w] = tower[top+h+1][w]
            }
        }
        tower = newTower
        top = currentFigure().height()-1
        // update top
    }
    
    def tetrisHeight(List<Character> jets) {
        pos = [3, 2]
        def figures = 0
        def i = 0
        while(figures < ROCKS_MAX) {
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
    
    def print(int height) {
        (0..<height).reverse().each { i->
            (0..<7).each { j->
                print (tower[i][j] ? '#' : '.')
            }
            println()
        }
    }
}

Tetris tetris = new Tetris()
tetris.tetrisHeight(testData as List)
println new Tetris().tetrisHeight(new File('input/day17.txt').text.trim() as List)