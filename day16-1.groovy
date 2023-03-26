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

def testData2 = """Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
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


Date start = new Date()
I = 0

import groovy.time.TimeCategory 
import groovy.time.TimeDuration

def wiseDfs(interestingPoints, rates, distances, currentPath, bestSoFar) {
    def stepsLeft = DEPTH+1 - currentPath.size()
    def currentNode = (currentPath.last() == '+') ? currentPath[currentPath.size()-2] : currentPath.last()
    
    println "Current node is $currentNode"
    
    if(stepsLeft == 0 || interestingPoints.size() == 0) {
        I++
        if(I % 100 == 0) {
            TimeDuration td = TimeCategory.minus(new Date(), start)
            def millis = td.minutes*60*1000 + td.seconds*1000 + td.millis
            println "Processed $I items in ${td} ${I/millis}"
        }
        def currentResult = currentPathValue(currentPath, rates)
        if(currentResult > bestSoFar) {
            return currentResult
        }
        return bestSoFar
    }

    def bestResult = 0
    interestingPoints.each { i ->
        def newInterestingPoints = new HashSet(interestingPoints)
        newInterestingPoints.remove(i)
        def next = distances[currentNode][i]
        if(next[1] < stepsLeft) { // strict comparison because we need one more operation for opening the valve
            next = next[0]
            def newNodes = 2
            while(next != i) {
                newNodes++
                currentPath << next
                println "$currentPath"
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
    def distances = [:].withDefault { [:].withDefault {[-1, 4000] } }
    
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
    
    distances.keySet().each { i->
        distances.keySet().each { j->
            distances.keySet().each { k->
                def alternateDistance = distances[i][k][1] + distances[k][j][1]
                if(alternateDistance < distances[i][j][1]) {
                    distances[i][j] = [distances[i][k][0], alternateDistance]
                }
            }
        }
    }
    
    def interestingValves = rates.findAll { it.value != 0 }.keySet()
    
    /*interestingValves.each { i -> println i
        interestingValves.each { j ->
                println "\t $j: ${distances[i][j]}"
        }
    }*/
    
    println "${interestingValves.size()}"
    I = 0
    start = new Date()
    wiseDfs(interestingValves, rates, distances, ['AA'], 0)
}

println process(testData)
println process(new File('input/day16.txt').text)