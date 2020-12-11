package advent2020.day11;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
@FunctionalInterface
public interface Decider {
     boolean able(char[][] seating, int seatRow, int seatColumn);
}
