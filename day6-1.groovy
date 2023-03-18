

def firstMarkerPosition(String line) {
    def firstMarker = 0
    def elements = [:].withDefault { 0 }
    (0..<3).each { elements[line[it]]++ }
    (3..<line.size()).find {
        elements[line[it]]++
        if(elements.values().every {
            it <= 1
        }) {
            return true
        } else {
            elements[line[it-3]]--
            return false
        }
    } + 1 
}

assert 7 == firstMarkerPosition('mjqjpqmgbljsphdztnvjfqwrcgsmlb')
assert 5 == firstMarkerPosition('bvwbjplbgvbhsrlpgdmjqwftvncz')
assert 6 == firstMarkerPosition('nppdvjthqldpwncqszvftbrmjlhg')
assert 10 == firstMarkerPosition('nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg')
assert 11 == firstMarkerPosition('zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw')
println firstMarkerPosition(new File('input/day6.txt').text.trim())