def testInput = """\$ cd /
\$ ls
dir a
14848514 b.txt
8504156 c.dat
dir d
\$ cd a
\$ ls
dir e
29116 f
2557 g
62596 h.lst
\$ cd e
\$ ls
584 i
\$ cd ..
\$ cd ..
\$ cd d
\$ ls
4060174 j
8033020 d.log
5626152 d.ext
7214296 k
"""

sizes = [:].withDefault { 0 }
contains = [:].withDefault { [] }


def processEntry(String parent, String line) {
    def segments = line.split(' ')
    if(segments[0] != 'dir') {
        def path = (parent + (parent != '/' ? '/' : '')) + segments[1]
        sizes[path] = segments[0] as long 
    }
    contains[parent] << segments[1]
}

def loadTree(List<String> terminalInput) {
    def currentPath = ''
    for (int i = 0; i < terminalInput.size(); i++) {
        def currentCommand = terminalInput[i]
        println currentPath
        if(currentCommand.startsWith('$ cd ..')) {
            currentPath = currentPath.substring(0, currentPath.lastIndexOf('/')+(currentPath.count('/')==1 ? 1 : 0))
        } else if (currentCommand.startsWith('$ cd ')) {
            if(currentPath != '' && currentPath != '/') currentPath += '/'
            currentPath = currentPath + (currentCommand - '$ cd ')
        } else if (currentCommand == '$ ls' ) {
            println "Listing"
            while(i+1 < terminalInput.size() && !terminalInput[i+1].startsWith('\$')) {
                i++
                processEntry(currentPath, terminalInput[i])
            }
        } else {
            throw new RuntimeException('Error!' + currentCommand);
        }
    }
}

loadTree(testInput.split('\n') as List)

def getSumOfDirectoriesOfTotalSizeAtMost100000(List<String> input) {
    sizes.clear()
    contains.clear()
    
    def calculateTotalSize 
    calculateTotalSize = { path ->
        sizes.containsKey(path) ? 
        sizes[path] 
        : contains[path].sum { calculateTotalSize(path + (path != '/' ? '/' : '') + it) }
    }.memoize()
    loadTree(input)
    contains.keySet().collect{
        calculateTotalSize(it)
    }.findAll { it <= 100000 }.sum()
}

assert 19 == getSumOfDirectoriesOfTotalSizeAtMost100000(testInput.split('\n') as List)
println getSumOfDirectoriesOfTotalSizeAtMost100000(new File('input/day7.txt').readLines())