/*
 * (c) Copyright 2016 Brite:Bill Ltd.
 *
 * 7 Grand Canal Street Lower, Dublin 2, Ireland
 * info@britebill.com
 * +353 1 661 9426
 */
package advent2020.day25;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
public class ComboBreaker {


    public static long findSecretLoopSize(long subjectNumber, long publicKey){
        long value = 1;
        long counter = 0;
        while(value != publicKey){

            value *= subjectNumber;
            value %= 20201227;

            counter++;
        }

        return counter;
    }

    public static long subjectNumber(long subjectNumber, long secretLoopSize){
        long value = 1;
        for (int i = 0; i < secretLoopSize; i++) {
            value *= subjectNumber;
            value %= 20201227;
        }
        return value;
    }
}
