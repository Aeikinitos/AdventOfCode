package advent2020.day20;

import lombok.Data;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
public class JurassicJigsaw {

    private static final Pattern TILE_PATTERN = Pattern.compile("Tile (?<id>\\d*):");

    public static List<JigsawPiece> parseInput(List<String> rawInput){
        List<JigsawPiece> pieces = new ArrayList<>();
        for (String tile : rawInput) {
            pieces.add(JurassicJigsaw.parseJigsawPiece(Arrays.asList(tile.split("\\n"))));
        }

        return pieces;
    }

    public static JigsawPiece parseJigsawPiece(List<String> rawInput){
        Matcher matcher = TILE_PATTERN.matcher(rawInput.get(0));
        matcher.find();
        Long id = Long.valueOf(matcher.group("id"));

        StringBuilder left = new StringBuilder();
        StringBuilder right = new StringBuilder();
        for (int i = 1; i < rawInput.size(); i++) {
            left.append(rawInput.get(i).charAt(0));
            right.append(rawInput.get(i).charAt(rawInput.get(i).length()-1));
        }
        return new JigsawPiece(id, rawInput.get(1), rawInput.get(rawInput.size()-1), left.toString(), right.toString());
    }

    public static List<JigsawPiece> findCategorizedPieces(List<JigsawPiece>  pieces, CategorizePiece condition){
        List<JigsawPiece> cornerPieces = new ArrayList<>();
        for (JigsawPiece piece : pieces) {
            findCategorizedPiece(pieces, piece, condition).ifPresent(cornerPieces::add);
            findCategorizedPiece(pieces, piece.rotated(), condition).ifPresent(cornerPieces::add);
            findCategorizedPiece(pieces, piece.flipped(), condition).ifPresent(cornerPieces::add);
            findCategorizedPiece(pieces, piece.flippedReverse(), condition).ifPresent(cornerPieces::add);
        }

        return cornerPieces;
    }

    private static Optional<JigsawPiece> findCategorizedPiece(List<JigsawPiece> pieces, JigsawPiece piece, CategorizePiece condition) {
        boolean isTopMatched = false;
        boolean isBottomMatched = false;
        boolean isLeftMatched = false;
        boolean isRightMatched = false;
        for (JigsawPiece otherPiece : pieces) {
            if(piece.equals(otherPiece)) continue; // skip self

            isTopMatched    = isTopMatched || isMatching(piece, JigsawPiece::getTop, otherPiece, JigsawPiece::getBottom);
            isBottomMatched = isBottomMatched || isMatching(piece, JigsawPiece::getBottom, otherPiece, JigsawPiece::getTop);
            isLeftMatched   = isLeftMatched || isMatching(piece, JigsawPiece::getLeft, otherPiece, JigsawPiece::getRight);
            isRightMatched  = isRightMatched || isMatching(piece, JigsawPiece::getRight, otherPiece, JigsawPiece::getLeft);
        }

        if( condition.test(isTopMatched,isBottomMatched, isLeftMatched, isRightMatched) ) {
            // top piece
            return Optional.of(piece);
        }
        return Optional.empty();
    }

    private static boolean isMatching(JigsawPiece target, getSide targetSide, JigsawPiece piece, getSide pieceSide){
        for (int i = 0; i < 4; i++) {
            if(pieceSide.getSide(piece).equals(targetSide.getSide(target))
                            || pieceSide.getSide(piece.flipped()).equals(targetSide.getSide(target))
                            || pieceSide.getSide(piece.flippedReverse()).equals(targetSide.getSide(target))
            ){
                return true;
            }

            // rotate 90 degress 3 times
            piece = piece.rotated();

        }

        return false;
    }
}

@FunctionalInterface
interface getSide {
    String getSide(JigsawPiece piece);
}

@FunctionalInterface
interface CategorizePiece {
    boolean test(boolean isTopMatched, boolean isBottomMatched, boolean isLeftMatched, boolean isRightMatched);

    CategorizePiece CORNER = (isTopMatched, isBottomMatched, isLeftMatched, isRightMatched) -> (!isTopMatched && (!isLeftMatched || !isRightMatched))||(!isBottomMatched && (!isLeftMatched || !isRightMatched));
    CategorizePiece VERTICAL = (isTopMatched, isBottomMatched, isLeftMatched, isRightMatched) -> ( (isTopMatched && isBottomMatched) && (!isLeftMatched || !isRightMatched));
    CategorizePiece HORIZONTAL = (isTopMatched, isBottomMatched, isLeftMatched, isRightMatched) -> ( ( (!isTopMatched || !isBottomMatched) && (isLeftMatched && isRightMatched)));
    CategorizePiece CENTER = (isTopMatched, isBottomMatched, isLeftMatched, isRightMatched) -> (isTopMatched && isBottomMatched && isLeftMatched && isRightMatched);
}



@Data
class JigsawPiece {
    Long id;
    String top;
    String bottom;
    String left;
    String right;

    public JigsawPiece(Long id, String top, String bottom, String left, String right) {
        this.id = id;
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
    }

    public JigsawPiece rotated(){
        return new JigsawPiece(
                        id,
                        left,
                        right,
                        bottom,
                        top
                        );
    }

    public JigsawPiece flipped(){
        return new JigsawPiece(
                        id,
                        bottom,
                        top,
                        new StringBuilder(left).reverse().toString(),
                        new StringBuilder(right).reverse().toString());
    }

    public JigsawPiece flippedReverse(){
        return new JigsawPiece(
                        id,
                        new StringBuilder(top).reverse().toString(),
                        new StringBuilder(bottom).reverse().toString(),
                        right,
                        left);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        JigsawPiece that = (JigsawPiece) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}