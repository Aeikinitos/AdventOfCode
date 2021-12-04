package advent2021.day3;


import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class BinaryDiagnostic {

    private BiPredicate<Integer, Integer> mostCommonBitCriteria = (ones, zeros) -> ones >= zeros;
    private BiPredicate<Integer, Integer> leastCommonBitCriteria = (ones, zeros) -> ones < zeros;

    protected long calculatePowerConsumption(List<String> binaryInput, int size){
        Binary sumProduct = new Binary(size);
        binaryInput.forEach(sumProduct::addBinary);

        int threshold = binaryInput.size()/2;
        StringBuilder gamma = new StringBuilder();
        StringBuilder epsilon = new StringBuilder();
        for (int i = 0; i < size; i++) {
            if(sumProduct.components[i] > threshold){
                gamma.append("1");
                epsilon.append("0");
            } else {
                gamma.append("0");
                epsilon.append("1");
            }
        }
        return Long.valueOf(gamma.toString(), 2) * Long.valueOf(epsilon.toString(), 2);
    }

    protected long getLifeSupportRating(List<String> binaryInput){
        return getRating(binaryInput,0, mostCommonBitCriteria) * getRating(binaryInput, 0, leastCommonBitCriteria);
    }

    protected long getRating(List<String> binaryInput, int index, BiPredicate bitCriteria){
        if(binaryInput.size() == 1){
            return Long.valueOf(binaryInput.get(0),2);
        }
        Map<Character, List<String>> partitionedMap =
                        binaryInput.stream().collect(Collectors.groupingBy(s -> s.charAt(index)));
        if(bitCriteria.test(partitionedMap.get('1').size(), partitionedMap.get('0').size())){
            return getRating(partitionedMap.get('1'), index+1, bitCriteria);
        } else {
            return getRating(partitionedMap.get('0'), index+1, bitCriteria);
        }
    }
}

class Binary {
    int binarySize;
    protected int[] components;

    public Binary(int size) {
        binarySize = size;
        components = new int[size];
    }

    public Binary(String binaryString) {
        for (int i = 0; i < binarySize; i++) {
            components[i] = Character.getNumericValue(binaryString.charAt(i));
        }
    }

    public void addBinary(String otherBinaryString){
        for (int i = 0; i < binarySize; i++) {
            components[i] += Character.getNumericValue(otherBinaryString.charAt(i));
        }
    }
}
