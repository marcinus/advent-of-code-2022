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
    def id
    def divisors
    def items
    def operation
    def divisor
    def trueDir
    def falseDir
    def itemsInspected = BigInteger.ZERO
    
    def Monkey(id, items,operation,divisor,trueDir,falseDir) {
    this.id = id
    this.items= items.collect { it as long }
    this.operation=operation
    this.divisor=BigInteger.valueOf(divisor as long)
    this.trueDir=trueDir as int
    this.falseDir=falseDir as int
    }
    
    public void setDivisorsAndUpdateItems(divisors) {
        this.divisors = divisors
        items = items.collect { item ->
            divisors.collect { div ->
                item % div
            }
        }
    }
    
    public String toString() {
        items?.join('|') as String
    }
    
    def calculate() {
        def result = [:].withDefault { [] }
        items.collect {
            def worry = operate(it)
            if(worry[id as int] == 0) result[trueDir] << worry
            else result[falseDir] << worry
        }
        itemsInspected = itemsInspected.plus(BigInteger.valueOf(items.size()))
        items = []
        result
    }
    
    List<Long> add(operands, List<Long> worry) {
        def result = (1..worry.size()).collect { 0 as long }
        operands.collect { operand -> 
            operand == 'old' ? worry: ((1..(worry.size())).collect { operand as long })
        }.each { operand ->
            result.eachWithIndex { item, index ->
                result[index] = (item + operand[index]) % divisors[index]
            }
        }
        result
    }
    
    List<Long> multiply(operands, List<Long> worry) {
        def result = (1..worry.size()).collect { 1 as long }
        operands.collect { operand ->
            operand == 'old' ? worry: ((1..(worry.size())).collect { operand as long })
        }.each { operand ->
            result.eachWithIndex { item, index ->
                result[index] = (item * operand[index]) % divisors[index]
            }
        }
        result
    }
    
    List<Long> operate(worry) {
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
        new Monkey(id=it[1], items=it[2].split(', ') as List, operation=it[3], divisor=it[4], trueDir=it[5], falseDir=it[6])
    }
    
    def divisors = monkeys.collect {it.divisor}
    monkeys.each {
        it.setDivisorsAndUpdateItems(divisors)
    }
    
    10000.times {
        round(monkeys)
    }
    
    def processedItems = monkeys.collect {it.itemsInspected}.sort().reverse()
    println processedItems
    processedItems[0]*processedItems[1]
}
assert 2713310158 == monkeyBussiness(testInput)
println monkeyBussiness(new File('input/day11.txt').text)