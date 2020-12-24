package advent2020.day24;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:sotirakis.lazarou@britebill.com">Sotirakis Lazarou</a>
 */
class LobbyLayoutTest {


    @Test
    void createHexCoord() {

        //e, se, sw, w, nw, and ne
        Assertions.assertEquals(new HexCoordinate(1, 1, 0), new LobbyLayout().createOrGetHexCoord("e"));
        Assertions.assertEquals(new HexCoordinate(1, 0, -1), new LobbyLayout().createOrGetHexCoord("se"));
        Assertions.assertEquals(new HexCoordinate(0, -1, -1), new LobbyLayout().createOrGetHexCoord("sw"));
        Assertions.assertEquals(new HexCoordinate(-1, -1, 0), new LobbyLayout().createOrGetHexCoord("w"));
        Assertions.assertEquals(new HexCoordinate(-1, 0, 1), new LobbyLayout().createOrGetHexCoord("nw"));
        Assertions.assertEquals(new HexCoordinate(0, 1, 1), new LobbyLayout().createOrGetHexCoord("ne"));

        Assertions.assertEquals(new HexCoordinate(1, 0, -1), new LobbyLayout().createOrGetHexCoord("esew"));
    }

    @Test
    void layTiles() {
        List<String> input =
                        Arrays.asList("sesenwnenenewseeswwswswwnenewsewsw",
                                        "neeenesenwnwwswnenewnwwsewnenwseswesw",
                                        "seswneswswsenwwnwse",
                                        "nwnwneseeswswnenewneswwnewseswneseene",
                                        "swweswneswnenwsewnwneneseenw",
                                        "eesenwseswswnenwswnwnwsewwnwsene",
                                        "sewnenenenesenwsewnenwwwse",
                                        "wenwwweseeeweswwwnwwe",
                                        "wsweesenenewnwwnwsenewsenwwsesesenwne",
                                        "neeswseenwwswnwswswnw",
                                        "nenwswwsewswnenenewsenwsenwnesesenew",
                                        "enewnwewneswsewnwswenweswnenwsenwsw",
                                        "sweneswneswneneenwnewenewwneswswnese",
                                        "swwesenesewenwneswnwwneseswwne",
                                        "enesenwswwswneneswsenwnewswseenwsese",
                                        "wnwnesenesenenwwnenwsewesewsesesew",
                                        "nenewswnwewswnenesenwnesewesw",
                                        "eneswnwswnwsenenwnwnwwseeswneewsenese",
                                        "neswnwewnwnwseenwseesewsenwsweewe",
                                        "wseweeenwnesenwwwswnew");
        LobbyLayout lobbyLayout = new LobbyLayout();
        int actual = lobbyLayout.layTiles(input).size();
        Assertions.assertEquals(10, actual);
    }

    @Test
    void part1() {
        List<String> input = readLines("src/test/resources/advent2020/day24/input");
        LobbyLayout lobbyLayout = new LobbyLayout();
        int actual = lobbyLayout.layTiles(input).size();
        System.out.println("Part1 :"+actual);
    }

    @Test
    void flipTilesForDays() {
        List<String> input =
                        Arrays.asList("sesenwnenenewseeswwswswwnenewsewsw",
                                        "neeenesenwnwwswnenewnwwsewnenwseswesw",
                                        "seswneswswsenwwnwse",
                                        "nwnwneseeswswnenewneswwnewseswneseene",
                                        "swweswneswnenwsewnwneneseenw",
                                        "eesenwseswswnenwswnwnwsewwnwsene",
                                        "sewnenenenesenwsewnenwwwse",
                                        "wenwwweseeeweswwwnwwe",
                                        "wsweesenenewnwwnwsenewsenwwsesesenwne",
                                        "neeswseenwwswnwswswnw",
                                        "nenwswwsewswnenenewsenwsenwnesesenew",
                                        "enewnwewneswsewnwswenweswnenwsenwsw",
                                        "sweneswneswneneenwnewenewwneswswnese",
                                        "swwesenesewenwneswnwwneseswwne",
                                        "enesenwswwswneneswsenwnewswseenwsese",
                                        "wnwnesenesenenwwnenwsewesewsesesew",
                                        "nenewswnwewswnenesenwnesewesw",
                                        "eneswnwswnwsenenwnwnwwseeswneewsenese",
                                        "neswnwewnwnwseenwseesewsenwsweewe",
                                        "wseweeenwnesenwwwswnew");
        LobbyLayout lobbyLayout = new LobbyLayout();
        lobbyLayout.layTiles(input);
        Map<Integer, Integer> days = new HashMap<>();
        for(int day = 1 ; day < 101 ; day++){
            days.put(day, lobbyLayout.flipGridTiles().size());
        }
        Map<Integer, Integer> expectedDays = new HashMap<Integer, Integer>() {{
            put(1,15);
            put(2,12);
            put(3,25);
            put(4,14);
            put(5, 23);
            put(20, 132);
            put(70, 1106);
            put(100, 2208);
        }};

        expectedDays.keySet().stream().forEach(day -> Assertions.assertEquals(expectedDays.get(day), days.get(day)));

    }

    @Test
    void flipTilesOneBlack() {
        LobbyLayout lobbyLayout = new LobbyLayout();
        HexCoordinate coord = new HexCoordinate(1, 1, 1).switchColor();
        lobbyLayout.grid.put(coord, coord);
        Assertions.assertEquals(0, lobbyLayout.flipGridTiles().size());

    }

    @Test
    void flipTilesTwoBlackDontStay() {
        LobbyLayout lobbyLayout = new LobbyLayout();
        HexCoordinate coord = new HexCoordinate(1, 1, 0).switchColor();
        lobbyLayout.grid.put(coord, coord);
        coord = new HexCoordinate(-2, -2, 0).switchColor();
        lobbyLayout.grid.put(coord, coord);
        Assertions.assertEquals(0, lobbyLayout.flipGridTiles().size());

    }

    @Test
    void flipTilesTwoBlackDontStayWhiteFlips() {
        LobbyLayout lobbyLayout = new LobbyLayout();
        HexCoordinate coord = new HexCoordinate(1, 1, 0).switchColor();
        lobbyLayout.grid.put(coord, coord);
        coord = new HexCoordinate(-1, -1, 0).switchColor();
        lobbyLayout.grid.put(coord, coord);
        Assertions.assertEquals(1, lobbyLayout.flipGridTiles().size());

    }

    @Test
    void flipTilesCombo() {
        LobbyLayout lobbyLayout = new LobbyLayout();
        HexCoordinate coord = new HexCoordinate(1, 1, 0).switchColor();
        lobbyLayout.grid.put(coord, coord);
        coord = new HexCoordinate(-1, -1, 0).switchColor();
        lobbyLayout.grid.put(coord, coord);
        coord = new HexCoordinate(-1, 0, 1).switchColor();
        lobbyLayout.grid.put(coord, coord);

        Map<HexCoordinate, HexCoordinate> grid = lobbyLayout.flipGridTiles();
        Assertions.assertEquals(4, grid.size());

    }

    @Test
    void part2() {
        List<String> input = readLines("src/test/resources/advent2020/day24/input");
        LobbyLayout lobbyLayout = new LobbyLayout();
        lobbyLayout.layTiles(input);
        Map<Integer, Integer> days = new HashMap<>();
        for(int day = 1 ; day < 101 ; day++){
            days.put(day, lobbyLayout.flipGridTiles().size());
        }
        System.out.println("Part2: "+days.get(100));
    }

    /* *************************************** */

    protected List<String> readLines(String filename){
        List<String> lines = Collections.emptyList();
        try
        {
            File file = new File(filename);
            Scanner sc = new Scanner(file);
            sc.useDelimiter("\\Z");

            lines = Arrays.stream(sc.next().split("\\n")).collect(Collectors.toList());

        }
        catch (IOException e)
        {
            // do something
            e.printStackTrace();
        }
        return lines;
    }
}