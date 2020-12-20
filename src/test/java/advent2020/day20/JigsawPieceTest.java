package advent2020.day20;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
class JigsawPieceTest {

    @Test
    void rotated() {
        JigsawPiece piece = new JigsawPiece(1L, "","","","", Arrays.asList(
                        "...#.",
                        "...#.",
                        "...#.",
                        "...#.",
                        "...#."));
        JigsawPiece rotated = piece.rotated();
        System.out.println(piece);
        System.out.println(rotated);
    }
}