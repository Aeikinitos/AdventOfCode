package advent2020.day20;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
class JurassicJigsawTest {

    @Test
    void parseInput() {
        List<String> input = readLines("src/test/resources/advent2020/day20/sample");

        List<JigsawPiece> jigsawPieces = JurassicJigsaw.parseInput(input);

        List<JigsawPiece> corners = JurassicJigsaw.findCategorizedPieces(jigsawPieces, CategorizePiece.CORNER).stream().distinct().collect(Collectors.toList());
        List<JigsawPiece> sidePieces = JurassicJigsaw.findCategorizedPieces(jigsawPieces, CategorizePiece.HORIZONTAL).stream().distinct().collect(Collectors.toList());
        List<JigsawPiece> centerPieces = JurassicJigsaw.findCategorizedPieces(jigsawPieces, CategorizePiece.CENTER).stream().distinct().collect(Collectors.toList());

//        JurassicJigsaw.completeImage(corners, sidePieces, centerPieces, jigsawPieces);
        List<List<JigsawPiece>> lists = JurassicJigsaw.completeImage(corners, sidePieces, centerPieces, jigsawPieces);
        if(!lists.isEmpty()){
            System.out.println("Yhoo");
        }
    }

    @Test
    void part1() {
        List<String> input = readLines("src/test/resources/advent2020/day20/input");

        List<JigsawPiece> jigsawPieces = JurassicJigsaw.parseInput(input);

        List<JigsawPiece> corners = JurassicJigsaw.findCategorizedPieces(jigsawPieces, CategorizePiece.CORNER).stream().distinct().collect(Collectors.toList());


        long answer = corners.stream().map(JigsawPiece::getId).reduce(1L, (product, id) -> product * id).longValue();
        System.out.println("Part1 :" + answer);
    }

    @Test
    void part2(){
        List<String> input = readLines("src/test/resources/advent2020/day20/input");

        List<JigsawPiece> jigsawPieces = JurassicJigsaw.parseInput(input);

        List<JigsawPiece> corners = JurassicJigsaw.findCategorizedPieces(jigsawPieces, CategorizePiece.CORNER).stream().distinct().collect(Collectors.toList());
        List<JigsawPiece> sidePieces = JurassicJigsaw.findCategorizedPieces(jigsawPieces, CategorizePiece.HORIZONTAL).stream().distinct().collect(Collectors.toList());
        List<JigsawPiece> centerPieces = JurassicJigsaw.findCategorizedPieces(jigsawPieces, CategorizePiece.CENTER).stream().distinct().collect(Collectors.toList());

        List<List<JigsawPiece>> image = JurassicJigsaw.completeImage(corners, sidePieces, centerPieces, jigsawPieces);
        if(!image.isEmpty()){
            for (List<JigsawPiece> lines : image) {
                for (JigsawPiece piece : lines) {
                    System.out.println(piece);
                }
            }
//            JurassicJigsaw.printInOrder(input, image);
            
        }
    }

    /* *************************************** */

    protected List<String> readLines(String filename){
        List<String> lines = Collections.emptyList();
        try
        {
            File file = new File(filename);
            Scanner sc = new Scanner(file);
            sc.useDelimiter("\\Z");

            lines = Arrays.stream(sc.next().split("\n\\n")).collect(Collectors.toList());

        }
        catch (IOException e)
        {
            // do something
            e.printStackTrace();
        }
        return lines;
    }
}