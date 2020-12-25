package advent2020.day25;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
class ComboBreakerTest {

    @Test
    void findSecretLoopSizeCard() {
        long secretLoopSize = ComboBreaker.findSecretLoopSize(7, 5764801);
        Assertions.assertEquals(8, secretLoopSize);
    }

    @Test
    void findSecretLoopSizeDoor() {
        long secretLoopSize = ComboBreaker.findSecretLoopSize(7, 17807724);
        Assertions.assertEquals(11, secretLoopSize);
    }


    @Test
    void findEncryptionKey() {
        long secretLoopSizeOne = ComboBreaker.subjectNumber(5764801, 11);
        long secretLoopSizeTwo = ComboBreaker.subjectNumber(17807724, 8);
        Assertions.assertEquals(14897079, secretLoopSizeOne);
        Assertions.assertEquals(14897079, secretLoopSizeTwo);
    }


    @Test
    void part1() {

        long secretLoopSizeCard = ComboBreaker.findSecretLoopSize(7, 10441485);
        long encryptionKey = ComboBreaker.subjectNumber(1004920, secretLoopSizeCard);
        System.out.println("Part1: "+ encryptionKey);
    }

}