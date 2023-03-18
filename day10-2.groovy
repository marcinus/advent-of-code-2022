def testInput = """addx 15
addx -11
addx 6
addx -3
addx 5
addx -1
addx -8
addx 13
addx 4
noop
addx -1
addx 5
addx -1
addx 5
addx -1
addx 5
addx -1
addx 5
addx -1
addx -35
addx 1
addx 24
addx -19
addx 1
addx 16
addx -11
noop
noop
addx 21
addx -15
noop
noop
addx -3
addx 9
addx 1
addx -3
addx 8
addx 1
addx 5
noop
noop
noop
noop
noop
addx -36
noop
addx 1
addx 7
noop
noop
noop
addx 2
addx 6
noop
noop
noop
noop
noop
addx 1
noop
noop
addx 7
addx 1
noop
addx -13
addx 13
addx 7
noop
addx 1
addx -33
noop
noop
noop
addx 2
noop
noop
noop
addx 8
noop
addx -1
addx 2
addx 1
noop
addx 17
addx -9
addx 1
addx 1
addx -3
addx 11
noop
noop
addx 1
noop
addx 1
noop
noop
addx -13
addx -19
addx 1
addx 3
addx 26
addx -30
addx 12
addx -1
addx 3
addx 1
noop
noop
noop
addx -9
addx 18
addx 1
addx 2
noop
noop
addx 9
noop
noop
noop
addx -1
addx 2
addx -37
addx 1
addx 3
noop
addx 15
addx -21
addx 22
addx -6
addx 1
noop
addx 2
addx 1
noop
addx -10
noop
noop
addx 20
addx 1
addx 2
addx 2
addx -6
addx -11
noop
noop
noop
"""

def draw(cycleNumber, x) {
    if (Math.abs((cycleNumber % 40) - x - 1) <= 1) {
        print '#'
    } else {
        print '.'
    }
    if(cycleNumber % 40 == 0) println()
}

def isRelevant(cycleNumber) {
    (cycleNumber - 20) % 40 == 0 && cycleNumber <= 220
}

def drawSignal(List<String> lines) {
    def x = 1
    def cycleNumber = 1
    lines.each {
        draw(cycleNumber, x)
        //println "${cycleNumber}, $x"
        if(it == 'noop') {
            cycleNumber++
        }
        else {
            def amount = (it - 'addx ') as int
            draw(cycleNumber+1, x)
            //println "${cycleNumber+1}, $x"
            x += amount
            cycleNumber += 2
        }   
    }
}

drawSignal(testInput.split('\n') as List)
println()
drawSignal(new File('input/day10.txt').readLines())