package advent2020.day20;

import org.junit.jupiter.api.Assertions;
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
        List<List<JigsawPiece>> image = JurassicJigsaw.completeImage(corners, sidePieces, centerPieces, jigsawPieces);
        if(!image.isEmpty()){
            System.out.println("Yhoo");
        }
        List<String> strings = JurassicJigsaw.joinTiles(image);
        System.out.println(strings);
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
            System.out.println("solution found");
//            for (List<JigsawPiece> lines : image) {
//                for (JigsawPiece piece : lines) {
//                    System.out.println(piece);
//                }
//            }
        }

        List<String> completedImage = JurassicJigsaw.joinTiles(image);
//        int monsterCount = RotateAndFlipToGetMonsterCount(completedImage); // commented to go a bit faster, uncomment in production
        int monsterCount = 36;
        long countHashes = completedImage.stream().flatMap(s -> s.chars().mapToObj(c -> (char) c))
                                   .filter(character -> '#' == character).count();

        System.out.println(countHashes-monsterCount*15);

    }

    private int RotateAndFlipToGetMonsterCount(List<String> completedImage) {
        JigsawPiece rotator = new JigsawPiece(1L,"","","","",null);
        for (int i = 0; i < 4; i++) {

            int monsterCount = JurassicJigsaw.findMonster(completedImage);
            if(monsterCount>0){
               return monsterCount;
            }
            List<String> flipVerticallyTile = rotator.flipVerticallyTile(completedImage);
            monsterCount = JurassicJigsaw.findMonster(flipVerticallyTile);
            if(monsterCount>0){
                return monsterCount;
            }

            List<String> flipHorizontallyTile = rotator.flipHorizontallyTile(completedImage);
            monsterCount = JurassicJigsaw.findMonster(flipHorizontallyTile);
            if(monsterCount>0){
                return monsterCount;
            }

            completedImage = rotator.rotateTile(completedImage);
        }
        return -1;

    }

    @Test
    void findMonster() {
        //   01234567890123456789
        //0                    #
        //1  #    ##    ##    ###
        //2   #  #  #  #  #  #
        List<String> monsterZero = Arrays.asList(
                        "                  # ",
                        "#    ##    ##    ###",
                        " #  #  #  #  #  #   ");
        Assertions.assertEquals(1,JurassicJigsaw.findMonster(monsterZero));

        List<String> monsterRowOne = Arrays.asList(
                        "                   # ",
                        " #    ##    ##    ###",
                        "  #  #  #  #  #  #   ");
        Assertions.assertEquals(1,JurassicJigsaw.findMonster(monsterRowOne));

        List<String> monsterColumnOne = Arrays.asList(
                        "  #   #            # ",
                        "                   # ",
                        " #    ##    ##    ###",
                        "  #  #  #  #  #  #   ");
        Assertions.assertEquals(1,JurassicJigsaw.findMonster(monsterColumnOne));

        List<String> monsterMeshed= Arrays.asList(
                        "  #   #            # ",
                        "                   # ",
                        " #    ## #  ##    ###",
                        "  #  #  #  ## #  #   ");
        Assertions.assertEquals(1,JurassicJigsaw.findMonster(monsterMeshed));

        List<String> noMonster= Arrays.asList(
                        "  #   #            # ",
                        "                   # ",
                        " #    ##    #     ###",
                        "  #  #  #   # #  #   ");
        Assertions.assertEquals(0,JurassicJigsaw.findMonster(noMonster));
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