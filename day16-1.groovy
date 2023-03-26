def testData = """Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
Valve BB has flow rate=13; tunnels lead to valves CC, AA
Valve CC has flow rate=2; tunnels lead to valves DD, BB
Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE
Valve EE has flow rate=3; tunnels lead to valves FF, DD
Valve FF has flow rate=0; tunnels lead to valves EE, GG
Valve GG has flow rate=0; tunnels lead to valves FF, HH
Valve HH has flow rate=22; tunnel leads to valve GG
Valve II has flow rate=0; tunnels lead to valves AA, JJ
Valve JJ has flow rate=21; tunnel leads to valve II
"""


regex = ~/Valve (\S{2}) has flow rate=(\d+); tunnels? leads? to valves? ([\S, ]+)/

DEPTH=30

def currentPathValue(currentPath, rates) {
    def sum = 0
    currentPath.eachWithIndex { it, index ->
        if(it == '+') {
            sum += rates[currentPath[index-1]] * (DEPTH-index)
        }
    }
    sum
}

def wiseDfs(interestingPoints, rates, distances, currentPath, bestSoFar) {
    def stepsLeft = DEPTH+1 - currentPath.size()
    def currentNode = (currentPath.last() == '+') ? currentPath[currentPath.size()-2] : currentPath.last()
    def currentResult = currentPathValue(currentPath, rates)
    if(currentResult > bestSoFar) {
        bestSoFar = currentResult
    }
    if(stepsLeft == 0) {
        return bestSoFar
    }

    def bestResult = 0
    interestingPoints.each { i ->
        def newInterestingPoints = new HashSet(interestingPoints)
        newInterestingPoints.remove(i)
        def next = distances[currentNode][i]
        if(next != null && next[1] < stepsLeft) { // strict comparison because we need one more operation for opening the valve
            next = next[0]
            def newNodes = 2
            while(next != i) {
                newNodes++
                currentPath << next
                next = distances[next][i][0]
            }
            currentPath << i
            currentPath << '+'
            bestSoFar = Math.max(bestSoFar, wiseDfs(newInterestingPoints, rates, distances, currentPath, bestSoFar))            
            newNodes.times { currentPath.removeLast() }
        }
    }
    
    return bestSoFar
}

def process(String input) {
    def rates = [:]
    def connections = [:]
    def distances = [:].withDefault { [:] }
    
    (input =~ regex).each {
        def name = it[1]
        def connected = it[3].split(', ') as Set
        rates[name] = it[2] as long
        distances[name][name] = [name, 0]
        connections[name] = connected
        connected.each {
            distances[name][it] = [it, 1]
        }
    }
    
    distances.keySet().each { k->
        distances.keySet().each { i->
            distances.keySet().each { j->
                if(distances[i].containsKey(k) && distances[k].containsKey(j)) {
                    def alternateDistance = distances[i][k][1] + distances[k][j][1]
                    if(!distances[i].containsKey(j) || alternateDistance < distances[i][j][1]) {
                        distances[i][j] = [distances[i][k][0], alternateDistance]
                    }
                }
            }
        }
    }
    
    def interestingValves = rates.findAll { it.value != 0 }.keySet()
    wiseDfs(interestingValves, rates, distances, ['AA'], 0)
}

println process(testData)
println process(new File('input/day16.txt').text)