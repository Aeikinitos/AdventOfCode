package advent2020.day20;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
class JigsawPieceTest {

    @Test
    void rotated() {
        List<JigsawPiece> jigsawPieces =
                        JurassicJigsaw.parseInput(Arrays.asList("Tile 1234:\n12345\n67890\nabcde\nfghij\nklmno"));

        JigsawPiece piece = jigsawPieces.get(0);

        JigsawPiece rotated = piece.rotated();
        System.out.println(piece);
        System.out.println(rotated);
        Assertions.assertEquals("kfa61", rotated.top);
        Assertions.assertEquals("oje05", rotated.bottom);
        Assertions.assertEquals("klmno", rotated.left);
        Assertions.assertEquals("12345", rotated.right);
    }

    @Test
    void flippedHorizontlaly() {
        List<JigsawPiece> jigsawPieces =
                        JurassicJigsaw.parseInput(Arrays.asList("Tile 1234:\n12345\n67890\nabcde\nfghij\nklmno"));

        JigsawPiece piece = jigsawPieces.get(0);

        JigsawPiece flippedHorizontlaly = piece.flippedHorizontlaly();
        System.out.println(piece);
        System.out.println(flippedHorizontlaly);
        Assertions.assertEquals("54321", flippedHorizontlaly.top);
        Assertions.assertEquals("onmlk", flippedHorizontlaly.bottom);
        Assertions.assertEquals("50ejo", flippedHorizontlaly.left);
        Assertions.assertEquals("16afk", flippedHorizontlaly.right);
    }

    @Test
    void flippedVertically() {
        List<JigsawPiece> jigsawPieces =
                        JurassicJigsaw.parseInput(Arrays.asList("Tile 1234:\n12345\n67890\nabcde\nfghij\nklmno"));

        JigsawPiece piece = jigsawPieces.get(0);

        JigsawPiece flippedVertically = piece.flippedVertically();
        System.out.println(piece);
        System.out.println(flippedVertically);
        Assertions.assertEquals("klmno", flippedVertically.top);
        Assertions.assertEquals("12345", flippedVertically.bottom);
        Assertions.assertEquals("kfa61", flippedVertically.left);
        Assertions.assertEquals("oje05", flippedVertically.right);
    }
}