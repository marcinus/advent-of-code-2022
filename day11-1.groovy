def testInput = """
Monkey 0:
  Starting items: 79, 98
  Operation: new = old * 19
  Test: divisible by 23
    If true: throw to monkey 2
    If false: throw to monkey 3

Monkey 1:
  Starting items: 54, 65, 75, 74
  Operation: new = old + 6
  Test: divisible by 19
    If true: throw to monkey 2
    If false: throw to monkey 0

Monkey 2:
  Starting items: 79, 60, 97
  Operation: new = old * old
  Test: divisible by 13
    If true: throw to monkey 1
    If false: throw to monkey 3

Monkey 3:
  Starting items: 74
  Operation: new = old + 3
  Test: divisible by 17
    If true: throw to monkey 0
    If false: throw to monkey 1
"""

class Monkey {
    def items
    def operation
    def divisor
    def trueDir
    def falseDir
    def itemsInspected = 0
    
    def Monkey(items,operation,divisor,trueDir,falseDir) {
    this.items= items
    this.operation=operation
    this.divisor=divisor as int
    this.trueDir=trueDir as int 
    this.falseDir=falseDir as int 
    }
    
    public String toString() {
        items?.join('|') as String
    }
    
    def calculate() {
        def result = [:].withDefault { [] }
        items.collect {
            def worry = (operate(it) / 3) as int
            if(worry % divisor == 0) result[trueDir] << worry
            else result[falseDir] << worry
        }
        itemsInspected += items.size()
        items = []
        result
    }
    
    int add(operands, worry) {
        operands.collect {
            it == 'old' ? worry as int : it as int
        }.sum()
    }
    
    int multiply(operands, worry) {
        def result = 1
        operands.collect {
            it == 'old' ? worry as int : it as int
        }.each {
            result *= it
        }
        result
    }
    
    int operate(worry) {
        if(operation.contains(' + ')) {
            add(operation.split(' \\+ '), worry)
        } else if(operation.contains(' * ')) {
            multiply(operation.split(' \\* '), worry)
        } else {
            throw new RuntimeException("Invalid")
        }
    }
}

def round(List<Monkey> monkeys) {
    monkeys.each {
        def result = it.calculate()
        result.each { target, worryList ->
            monkeys[target].items = monkeys[target].items + worryList
        }
    }
}

def monkeyBussiness(String input) {
def regex = ~"""(?m)Monkey (\\d)+:
  Starting items: (.*)\$
  Operation: new = (.*)\$
  Test: divisible by (\\d+)
    If true: throw to monkey (\\d+)
    If false: throw to monkey (\\d+)"""
    def result =input =~ regex
    
    def monkeys = result.collect {
        new Monkey(items=it[2].split(', ') as List, operation=it[3], divisor=it[4], trueDir=it[5], falseDir=it[6])
    }
   
    
    20.times {
        round(monkeys)
    }
    
    def processedItems = monkeys.collect {it.itemsInspected}.sort().reverse()
    processedItems[0]*processedItems[1]
}

assert 10605 == monkeyBussiness(testInput)
println monkeyBussines(new File('input/day11.txt').text)