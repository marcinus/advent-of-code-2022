def currentCapacity = 0
def currentElf = 0
def queue = new PriorityQueue<Integer>(Comparator.reverseOrder());
new File('./input/day1.txt').eachLine {
    if(it.trim().isEmpty()) {
        queue.add(currentCapacity)
        currentElf++
        currentCapacity = 0
    } else {
        currentCapacity += Integer.parseInt(it)
    }
}

println ((1..3).collect { queue.poll() }.sum())

//println lines.size()