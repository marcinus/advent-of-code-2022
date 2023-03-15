def pointsForSelection(String x, String y) {
    switch(x) {
        case 'A':
            switch(y) {
                case 'X':
                    return 3
                case 'Y':
                    return 1
                case 'Z':
                    return 2
            }
        case 'B':
            switch(y) {
                case 'X':
                    return 1
                case 'Y':
                    return 2
                case 'Z':
                    return 3
            }
        case 'C':
            switch(y) {
                case 'X':
                    return 2
                case 'Y':
                    return 3
                case 'Z':
                    return 1
            }
        default:
            throw new IllegalStateException("Invalid char $x")
    
    }
}


def pointsForSelection(String x) {
    switch(x) {
        case 'X':
            return 0
        case 'Y':
            return 3
        case 'Z':
            return 6
        default:
            throw new IllegalStateException("Invalid char $x")
    
    }
}

def total = 0

new File('input/day2.txt').eachLine { line ->
    def data = line.trim().split(' ')
    def opponent = data[0]
    def yours = data[1]
    total += pointsForSelection(yours) + pointsForSelection(opponent, yours)
}

println total