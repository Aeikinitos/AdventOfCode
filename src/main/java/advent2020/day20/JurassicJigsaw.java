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

    private static JigsawPiece parseJigsawPiece(List<String> rawInput){
        Matcher matcher = TILE_PATTERN.matcher(rawInput.get(0));
        matcher.find();
        Long id = Long.valueOf(matcher.group("id"));

        StringBuilder left = new StringBuilder();
        StringBuilder right = new StringBuilder();
        for (int i = 1; i < rawInput.size(); i++) {
            left.append(rawInput.get(i).charAt(0));
            right.append(rawInput.get(i).charAt(rawInput.get(i).length()-1));
        }
        return new JigsawPiece(id, rawInput.get(1), rawInput.get(rawInput.size()-1), left.toString(), right.toString(), rawInput.subList(1, rawInput.size()));
    }

    public static List<JigsawPiece> findCategorizedPieces(List<JigsawPiece>  pieces, CategorizePiece condition){
        List<JigsawPiece> categorizedPieces = new ArrayList<>();
        for (JigsawPiece piece : pieces) {
            findCategorizedPiece(pieces, piece, condition).ifPresent(categorizedPieces::add);
            findCategorizedPiece(pieces, piece.rotated(), condition).ifPresent(categorizedPieces::add);
            findCategorizedPiece(pieces, piece.flippedHorizontlaly(), condition).ifPresent(categorizedPieces::add);
            findCategorizedPiece(pieces, piece.flippedVertically(), condition).ifPresent(categorizedPieces::add);
        }

        return categorizedPieces;
    }

    private static Optional<JigsawPiece> findCategorizedPiece(List<JigsawPiece> pieces, JigsawPiece piece, CategorizePiece condition) {
        boolean isTopMatched = false;
        boolean isBottomMatched = false;
        boolean isLeftMatched = false;
        boolean isRightMatched = false;
        for (JigsawPiece otherPiece : pieces) {
            if(piece.equals(otherPiece)) continue; // skip self

            isTopMatched    = isTopMatched || pieceMatching(piece, JigsawPiece::getTop, otherPiece, JigsawPiece::getBottom).isPresent();
            isBottomMatched = isBottomMatched || pieceMatching(piece, JigsawPiece::getBottom, otherPiece, JigsawPiece::getTop).isPresent();
            isLeftMatched   = isLeftMatched || pieceMatching(piece, JigsawPiece::getLeft, otherPiece, JigsawPiece::getRight).isPresent();
            isRightMatched  = isRightMatched || pieceMatching(piece, JigsawPiece::getRight, otherPiece, JigsawPiece::getLeft).isPresent();
        }

        if( condition.test(isTopMatched,isBottomMatched, isLeftMatched, isRightMatched) ) {
            // top piece
            return Optional.of(piece);
        }
        return Optional.empty();
    }

    private static Optional<JigsawPiece> pieceMatching(JigsawPiece target, Direction targetSide, JigsawPiece piece, Direction pieceSide){
        for (int i = 0; i < 4; i++) {
            if(pieceSide.getSide(piece).equals(targetSide.getSide(target))){
                return Optional.of(piece);
            }
            if(pieceSide.getSide(piece.flippedHorizontlaly()).equals(targetSide.getSide(target))){
                return Optional.of(piece.flippedHorizontlaly());
            }
            if(pieceSide.getSide(piece.flippedVertically()).equals(targetSide.getSide(target))){
                return Optional.of(piece.flippedVertically());
            }

            // rotate 90 degress 3 times
            piece = piece.rotated();

        }

        return Optional.empty();
    }

    public static List<List<JigsawPiece>> completeImage(List<JigsawPiece> corners, List<JigsawPiece> sides, List<JigsawPiece> centers, List<JigsawPiece> pieces) {
        List<List<JigsawPiece>>
//        grid = completeImage(corners, sides, centers, pieces, JigsawPiece::getRight, JigsawPiece::getTop, JigsawPiece::getLeft, JigsawPiece::getBottom);
//        if(!grid.isEmpty()) return grid;
//        grid = completeImage(corners, sides, centers, pieces, JigsawPiece::getRight, JigsawPiece::getBottom, JigsawPiece::getLeft, JigsawPiece::getTop);
//        if(!grid.isEmpty()) return grid;
//        grid = completeImage(corners, sides, centers, pieces, JigsawPiece::getTop, JigsawPiece::getRight, JigsawPiece::getBottom, JigsawPiece::getLeft);
//        if(!grid.isEmpty()) return grid;
        grid = completeImage(corners, sides, centers, pieces, JigsawPiece::getTop, JigsawPiece::getLeft, JigsawPiece::getBottom, JigsawPiece::getRight);
        if(!grid.isEmpty()) return grid;
//        grid = completeImage(corners, sides, centers, pieces, JigsawPiece::getLeft, JigsawPiece::getTop, JigsawPiece::getRight, JigsawPiece::getBottom);
        if(!grid.isEmpty()) return grid;
//        grid = completeImage(corners, sides, centers, pieces, JigsawPiece::getLeft, JigsawPiece::getBottom, JigsawPiece::getRight, JigsawPiece::getTop);
        if(!grid.isEmpty()) return grid;
        grid = completeImage(corners, sides, centers, pieces, JigsawPiece::getBottom, JigsawPiece::getRight, JigsawPiece::getTop, JigsawPiece::getLeft);
        if(!grid.isEmpty()) return grid;
        grid = completeImage(corners, sides, centers, pieces, JigsawPiece::getBottom, JigsawPiece::getLeft, JigsawPiece::getTop, JigsawPiece::getRight);
        if(!grid.isEmpty()) return grid;
        return new ArrayList<>();
    }

    public static List<List<JigsawPiece>> completeImage(List<JigsawPiece> corners, List<JigsawPiece> sides, List<JigsawPiece> centers, List<JigsawPiece> pieces
                    , Direction moveHorizontal, Direction moveVertical, Direction moveReverseHorizontal, Direction moveReverseVerical){
        JigsawPiece startingCorner = corners.get(0).flippedVertically(); // check all positions
        for (int rotation = 0; rotation < 4; rotation++) {

            List<List<JigsawPiece>> grid = new ArrayList<>();

            List<JigsawPiece> line = new ArrayList<>();
            // move horizontally
            line.add(startingCorner);
            line.addAll(moveTowardsAndCollect(sides, startingCorner, moveVertical, moveReverseVerical));
            line.addAll(moveTowardsAndCollect(corners, line.get(line.size()-1), moveVertical, moveReverseVerical));

            if(line.size() != Math.sqrt(pieces.size())){
                continue;
//                return new ArrayList<>();
            }

            // move vertically for
            // left side
            JigsawPiece topRowPiece = line.get(0);
            List<JigsawPiece> vertical = new ArrayList<>();
            vertical.add(topRowPiece);
            vertical.addAll(moveTowardsAndCollect(sides, topRowPiece, moveHorizontal, moveReverseHorizontal));
            vertical.addAll(moveTowardsAndCollect(corners, vertical.get(vertical.size() - 1), moveHorizontal, moveReverseHorizontal));
            grid.add(vertical);

            if(vertical.size() != line.size()){
                return new ArrayList<>();
            }
            // center
            for (int i = 1; i < line.size() - 1; i++) {
                topRowPiece = line.get(i);
                vertical = new ArrayList<JigsawPiece>();
                vertical.add(topRowPiece);
                vertical.addAll(moveTowardsAndCollect(centers, topRowPiece, moveHorizontal, moveReverseHorizontal));
                vertical.addAll(moveTowardsAndCollect(sides, vertical.get(vertical.size() - 1), moveHorizontal, moveReverseHorizontal));
                grid.add(vertical);
            }

            // right side
            topRowPiece = line.get(line.size() - 1);
            vertical = new ArrayList<>();
            vertical.add(topRowPiece);
            vertical.addAll(moveTowardsAndCollect(sides, topRowPiece, moveHorizontal, moveReverseHorizontal));
            vertical.addAll(moveTowardsAndCollect(corners, vertical.get(vertical.size() - 1), moveHorizontal, moveReverseHorizontal));
            grid.add(vertical);

            double length = Math.sqrt(pieces.size());
            if(grid.size() == length){
                if (grid.stream().map(List::size).filter(integer -> integer != length).count() == 0){
                    return grid;
                }
            }
            startingCorner = startingCorner.rotated();
        }
        return new ArrayList<>();

    }

    private static List<JigsawPiece> moveTowardsAndCollect(List<JigsawPiece> sides, JigsawPiece startingPiece, Direction pieceSide, Direction nextSide) {
        List<JigsawPiece> line = new ArrayList<>();
        Optional<JigsawPiece> nextPiece = findNextPiece(startingPiece, sides, pieceSide, nextSide);

        while(nextPiece.isPresent()){
            line.add(nextPiece.get());
//            System.out.println(nextPiece.get());
            nextPiece = findNextPiece(nextPiece.get(), sides, pieceSide, nextSide);
        }
        return line;
    }

    private static Optional<JigsawPiece> findNextPiece(JigsawPiece piece, List<JigsawPiece> sides, Direction pieceSide, Direction nextSide){

        for (JigsawPiece sidePiece : sides) {
            if(piece.equals(sidePiece)) continue; // skip self
            Optional<JigsawPiece> next =
                            pieceMatching(piece, pieceSide, sidePiece, nextSide);
            if(next.isPresent()){
//                System.out.println("Match "+piece.getId()+" with " + next.get().getId());
                return next;
            }
        }
        return Optional.empty();

    }
}

@FunctionalInterface
interface Direction {
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

    List<String> tile;

    public JigsawPiece(Long id, String top, String bottom, String left, String right, List<String> tile) {
        this.id = id;
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
        this.tile = tile;
    }

    public JigsawPiece rotated(){
        return new JigsawPiece(
                        id,
                        left,
                        right,
                        bottom,
                        top,
                        rotateTile(tile));
    }

    public JigsawPiece flippedHorizontlaly(){
        return new JigsawPiece(
                        id,
                        bottom,
                        top,
                        new StringBuilder(left).reverse().toString(),
                        new StringBuilder(right).reverse().toString(),
                        flipHorizontallyTile(tile));
    }

    public JigsawPiece flippedVertically(){
        return new JigsawPiece(
                        id,
                        new StringBuilder(top).reverse().toString(),
                        new StringBuilder(bottom).reverse().toString(),
                        right,
                        left,
                        flipVerticallyTile(tile));
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

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        tile.stream().forEach(s -> sb.append(s).append("\n"));
        return sb.append("\n").toString();
    }

    private List<String> rotateTile(List<String> tile) {
        // each horizontal String in list becomes vertical
        List<String> rotated = new ArrayList<>();
        for (int i = 0; i < tile.size(); i++) {
            StringBuilder column = new StringBuilder();
            for (String row : tile) {
                column.append(row.charAt(i));
            }
            rotated.add(column.toString());
        }
        return rotated;
    }

    private List<String> flipHorizontallyTile(List<String> tile){
        List<String> flipped = new ArrayList<>();
        for (String row : tile) {
            flipped.add(new StringBuilder(row).reverse().toString());
        }

        return flipped;
    }

    private List<String> flipVerticallyTile(List<String> tile){
        Collections.reverse(tile);
        return tile;
    }
}