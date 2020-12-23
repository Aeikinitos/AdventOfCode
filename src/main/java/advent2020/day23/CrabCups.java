package advent2020.day23;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
public class CrabCups {

    Map<Integer, HashLinkedListNode> cupIndices = new HashMap<>();
    HashLinkedListNode currentCup;

    public CrabCups(List<Integer> input) {
        // setup initial data
        currentCup = new HashLinkedListNode(input.get(0));
        cupIndices.put(3, currentCup);
        currentCup.setNext(currentCup); // create a circle
        for (Integer initial : input.subList(1, input.size())) {
            HashLinkedListNode newCup = new HashLinkedListNode(initial, currentCup.getNext());
            cupIndices.put(initial, newCup);
            currentCup.setNext(newCup);
            currentCup = newCup;
        }

        // setup generic data
        for (int i = input.size()+1; i <= 1000000; i++) {
            HashLinkedListNode newCup = new HashLinkedListNode(i, currentCup.getNext());
            cupIndices.put(i, newCup);
            currentCup.setNext(newCup);
            currentCup = newCup;
        }

        currentCup = cupIndices.get(input.get(0)); // get first cup
    }

    public Long playCups() {

        for (int i = 0; i < 10000000; i++) {
            // get first cup - no modification
            HashLinkedListNode currentCup = getCurrentCup();
            // find destination cup excluding the next 3 - no mod
            HashLinkedListNode destination = getDestination(calculateDestination());
            // place the next 3 of first cup after the destination cup - firstCup.next = n3.next; n3.next = destination.next; modify destination.next -> next1;
            placeAfter(destination, currentCup.getNext(), currentCup.getNext().getNext().getNext());
            // move the first cap point to the next - current cup = firstCup.next?
            takeAStep();
        }

        HashLinkedListNode one = cupIndices.get(1);
        return Long.valueOf(one.getNext().getValue()) * one.getNext(2).getValue();

    }

    private HashLinkedListNode getCurrentCup() {
        return currentCup;
    }

    private Integer calculateDestination() {
        Integer destinationCup = getCurrentCup().getValue() - 1;
        while (destinationCup > 0 && isCupInNextThreeOfCurrentCup(destinationCup)) {
            destinationCup -= 1;
        }
        // either destination is Zero which means get the max number
        if (destinationCup == 0) {
            // check the 4 biggest numbers just in case all 3 are in the drained cups
            for (int max = 1000000; max >= 999997; max--) {
                if (!isCupInNextThreeOfCurrentCup(max)) {
                    destinationCup = max;
                    break;
                }
            }
        }
        return destinationCup;
    }

    private boolean isCupInNextThreeOfCurrentCup(Integer destinationCup) {
        return getCurrentCup().getNext().getValue().equals(destinationCup)
                        || getCurrentCup().getNext(2).getValue().equals(destinationCup)
                        || getCurrentCup().getNext(3).getValue().equals(destinationCup);
    }

    private HashLinkedListNode getDestination(Integer value) {
        return cupIndices.get(value);
    }

    private void placeAfter(HashLinkedListNode destination, HashLinkedListNode one, HashLinkedListNode three) {
        getCurrentCup().setNext(three.getNext());
        three.setNext(destination.getNext());
        destination.setNext(one);
    }

    private void takeAStep() {
        currentCup = getCurrentCup().getNext();
    }
}


@Data
@RequiredArgsConstructor
@AllArgsConstructor
class HashLinkedListNode {
    @NonNull Integer value;
    HashLinkedListNode next;

    public HashLinkedListNode getNext(int i) {
        HashLinkedListNode result = this;
        for (int j = 0; j < i; j++) {
            result = result.getNext();
        }
        return result;
    }
}