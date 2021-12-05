package advent2021.day5;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.*;

public class HydrothermalVenture {

    protected long process(List<String> input){

        List<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>> lines =
                        input.stream()
                             .map(s -> s.split(" -> "))
                             .map(this::convertToPairs)
                             .collect(Collectors.toList());
        return countOverlaps(lines);
    }



    protected long countOverlaps(List<Pair<Pair<Integer, Integer>,Pair<Integer, Integer>>> lines) {
        int[][] map = new int[1000][1000];

        for (Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> line : lines) {
            if (line.getLeft().getLeft().equals(line.getRight().getLeft())) {
                // x1 == x2
                int x = line.getLeft().getLeft();
                int y1 = line.getLeft().getRight();
                int y2 = line.getRight().getRight();
                int lineStart = min(y1, y2);
                int lineEnd = max(y1, y2);
                for (int i = lineStart; i <= lineEnd; i++) {
                    map[x][i] += 1;
                }
            } else if (line.getLeft().getRight().equals(line.getRight().getRight())) {
                // y1 == y2
                int y = line.getLeft().getRight();
                int x1 = line.getLeft().getLeft();
                int x2 = line.getRight().getLeft();
                int lineStart = min(x1, x2);
                int lineEnd = max(x1, x2);
                for (int i = lineStart; i <= lineEnd; i++) {
                    map[i][y] += 1;
                }
            } else {
                int x1 = line.getLeft().getLeft();
                int x2 = line.getRight().getLeft();
                int y1 = line.getLeft().getRight();
                int y2 = line.getRight().getRight();

                int dirX = (x1-x2)/abs(x1-x2);
                int dirY = (y1-y2)/abs(y1-y2);
                for (int i = 0; i <= abs(x1-x2); i++) {
                    map[x1 - i * dirX][y1 - i * dirY] += 1;
                }
            }
        }
        int count = 0;
        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 1000; j++) {
                if (map[i][j] > 1) {
                    count++;
                }
            }
        }
        return count;
    }

    private Pair<Pair<Integer, Integer>,Pair<Integer, Integer>> convertToPairs(String[] line){
        // "0,9", "5,9"
        String[] leftPoint = line[0].split(",");
        String[] rightPoint = line[1].split(",");
        return new ImmutablePair<>(
                        new ImmutablePair<>(Integer.valueOf(leftPoint[0]), Integer.valueOf(leftPoint[1])),
                        new ImmutablePair<>(Integer.valueOf(rightPoint[0]), Integer.valueOf(rightPoint[1]))
        );

    }
}
