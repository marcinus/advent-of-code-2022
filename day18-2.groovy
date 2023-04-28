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

MAX_SIZE = 20

def visitedNodes = [] as Set

class Area {
    def MAX_SIZE
    def cubes
    def counter = 0
    def posToComponent = [:]
    def freeComponents = [] as Set
    def enclosedComponents = [] as Set
    
    Area(cubes, MAX_SIZE) {
        this.cubes = cubes
        this.MAX_SIZE = cubes.collect { it.max() }.max() 
    }
    
    
    def getOrCreateComponent(pos) {
        if(!posToComponent.containsKey(pos)) {
            posToComponent[pos] = counter++
        }
        posToComponent[pos]
    }

    def outside(pos) {
        return [pos[0], pos[1], pos[2]].any { it <= 0 || it > MAX_SIZE }
    }
    
    def neighbours(pos) {
        [
            new Tuple3(pos[0]+1, pos[1], pos[2]),
            new Tuple3(pos[0]-1, pos[1], pos[2]),
            new Tuple3(pos[0], pos[1]+1, pos[2]),
            new Tuple3(pos[0], pos[1]-1, pos[2]),
            new Tuple3(pos[0], pos[1], pos[2]+1),
            new Tuple3(pos[0], pos[1], pos[2]-1)
        ].findAll { !cubes.contains(it) }
    }
    
    def isTracked(pos) {
        pos[0] == 2 && pos[1] ==2 && pos[2] ==5
    }
    
    def limitedBfs(pos) {
        def component = posToComponent[pos]
        def queue = [pos] as Queue
        while(!queue.isEmpty() && !outside(queue.peek())) {
            def nexta = neighbours(queue.pop())
            for(int i = 0; i < nexta.size(); i++) {
                def next = nexta[i]
                if(!posToComponent.containsKey(next)) {
                    posToComponent[next] = component
                    queue.add(next)
                } else if(posToComponent[next] != component && freeComponents.contains(posToComponent[next])) {
                    freeComponents.add(component)
                    return
                }
            }
        }
        if(!queue.isEmpty()) {
            freeComponents.add(component)
        } else {
            enclosedComponents.add(component)
        }
    }
    
    def isFree(pos) {
        if(cubes.contains(pos)) {
            return false
        }
        def component = getOrCreateComponent(pos)
        if(freeComponents.contains(component)) {
            return true
        }
        if(enclosedComponents.contains(component)) {
            return false
        }
        //println "BFS from $pos"
        limitedBfs(pos)
        return freeComponents.contains(component) ? true : (enclosedComponents.contains(component) ? false : new RuntimeException("Won't work"))
    }
}

def toTuple(position) {
    new Tuple3(position[0], position[1], position[2])
}

def countFreeSides(List<String> input) {
    def cubes = input.collect { it.split(',').collect { it as int } }.collect { toTuple(it) } 
    def area = new Area(cubes, 20)
    cubes.sum { pos ->
        area.neighbours(pos).sum {
            def result = area.isFree(it) ? 1 : 0
            //println "neighbour of $pos at $it is free ? $result"
            result
        } ?: 0
    } ?: 0
}

println countFreeSides(testData.split('\n') as List)
println countFreeSides(new File('input/day18.txt').readLines())