def testData = """    [D]    
[N] [C]    
[Z] [M] [P]
 1   2   3 

move 1 from 2 to 1
move 3 from 1 to 3
move 2 from 2 to 1
move 1 from 1 to 2
"""

// Find first line that does has only numbers
// Count the numbers

def doOperation(stacks, number, from, to) {
    def itemsMoved = (1..number).collect{
        stacks[from-1].pop()
    }
    itemsMoved.reverse().each {
        stacks[to-1].push(it)
    }
}

def getInitialState(List<String> board) {
    def indexes = board.last().split(' ').findAll { it.trim() }.collect { it as int }
    def result = [].withDefault { new Stack<String>() }
    board.reverse().drop(1).each { line ->
        indexes.each { index ->
            def itemIndex = (index-1)*4+1
            if(line[itemIndex] != ' ') {
                result[index-1].push(line[itemIndex])
            }
        }
    }
    result
}

def processSteps(state, commands) {
    commands.each { command ->
        def regex = /move (\d+) from (\d+) to (\d+)/
        def result = (command =~ regex)
        def number = result[0][1] as int
        def from = result[0][2] as int
        def to = result[0][3] as int
        doOperation(state, number, from, to)
    }
}

def getResultingArray(List<String> input) {
    def firstMoveLine = input.findIndexOf { it.startsWith('move') }
    def state = getInitialState(input.subList(0, firstMoveLine-1))
    processSteps(state, input.subList(firstMoveLine, input.size()))
    state.collect { it.peek() }.join()
}

assert "MCD" == getResultingArray(testData.split('\n') as List)
println getResultingArray(new File('input/day5.txt').readLines())