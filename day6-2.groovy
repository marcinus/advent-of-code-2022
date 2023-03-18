def firstMarkerPosition(String line) {
    def firstMarker = 0
    def elements = [:].withDefault { 0 }
    (0..<13).each { elements[line[it]]++ }
    (13..<line.size()).find {
        elements[line[it]]++
        if(elements.values().every {
            it <= 1
        }) {
            return true
        } else {
            elements[line[it-14]]--
            return false
        }
    } + 1 
}

assert 19 == firstMarkerPosition('mjqjpqmgbljsphdztnvjfqwrcgsmlb')
assert 23 == firstMarkerPosition('bvwbjplbgvbhsrlpgdmjqwftvncz')
assert 23 == firstMarkerPosition('nppdvjthqldpwncqszvftbrmjlhg')
assert 29 == firstMarkerPosition('nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg')
assert 26 == firstMarkerPosition('zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw')
println firstMarkerPosition(new File('input/day6.txt').text.trim())