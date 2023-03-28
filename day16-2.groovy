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

DEPTH=26

def currentPathValue(currentPath, rates) {
    def sum = 0
    currentPath.eachWithIndex { it, index ->
        if(it == '+') {
            sum += rates[currentPath[index-1]] * (DEPTH-index)
        }
    }
    sum
}

def wiseDfs(interestingPoints, rates, distances, results) {
    wiseDfs(interestingPoints, rates, distances, ['AA'], 0, results, [] as Set)
}

def wiseDfs(interestingPoints, rates, distances, currentPath, bestSoFar, results, traversedInterestingPoints) {
    def stepsLeft = DEPTH+1 - currentPath.size()
    def currentNode = (currentPath.last() == '+') ? currentPath[currentPath.size()-2] : currentPath.last()
    def currentResult = currentPathValue(currentPath, rates)
    def clone = traversedInterestingPoints.collect() as Set
    results[clone] = Math.max(results[clone], currentResult)
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
            traversedInterestingPoints.add(i)
            bestSoFar = Math.max(bestSoFar, wiseDfs(newInterestingPoints, rates, distances, currentPath, bestSoFar, results, traversedInterestingPoints))            
            newNodes.times { currentPath.removeLast() }
            traversedInterestingPoints.remove(i)
        }
    }
    
    return bestSoFar
}

class TreeNode {
    TreeNode no
    TreeNode yes
    def value
    
    def traverse(decisions, Closure reduce) {
        if (value != null) {
            return value
        }
        def decision = decisions.pop()
        if(decision == 'yes' && yes != null) return yes.traverse(decisions, reduce)
        if(decision == 'no' && no != null) return no.traverse(decisions, reduce)
        if(decision == 'both') {
            return reduce(yes?.traverse(decisions.collect(), reduce) ?: 0, no?.traverse(decisions.collect(), reduce) ?: 0)
        }
        return 0
    }
    
    def push(decisions, value) {
        if(decisions.isEmpty()) {
            this.value = value
            return
        }
        def decision = decisions.pop()
        if (decision == 'yes') {
            if(yes == null) {
                yes = new TreeNode()
            }
            yes.push(decisions, value)
        }
        if (decision == 'no') {
            if(no == null) {
                no = new TreeNode()
            }
            no.push(decisions, value)
        }
    }
}

class Tree {
    TreeNode root
    
    Tree() {
        root = new TreeNode()
    }
    
    def push(decisions, value) {
        root.push(decisions, value)
    }
    
    def traverse(decisions, reduce) {
        root.traverse(decisions, reduce)
    }
}

def toTree(interestingPoints, results) {
    def tree = new Tree()
    results.each { result, value ->
        def path = interestingPoints.collect { i ->
            if (result.contains(i)) return 'yes'
            else return 'no'
        }
        tree.push(path, value)
    }
    
    results.take(10000).sort{ -it.value }.collect { key, value ->
        def decisions = interestingPoints.collect { i ->
            if (key.contains(i)) return 'no'
            else return 'both'
        }
        def elephantValue = tree.traverse(decisions, {a, b -> Math.max(a, b)})
        value + elephantValue
    }.sort { -it }.take(10)
    
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
    
    def results = [:].withDefault { 0 }
    
    def interestingValves = rates.findAll { it.value != 0 }.keySet()
    wiseDfs(interestingValves, rates, distances, results)
    
    println "First part done, ${results.size()}"
    
    toTree(interestingValves, results)
    //results.sort { -it.value }.each { println it.value }
}
println "Start"
println process(testData)
println process(new File('input/day16.txt').text)
println "TEST"