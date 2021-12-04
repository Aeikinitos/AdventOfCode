package advent2021.day4;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GiantSquid {


    protected long process(List<String> input, boolean winGoal){
        String lotteryNumbersSequence = input.get(0);
        List<Integer> lotteryNumbers = parseLotteryNumbers(lotteryNumbersSequence);
        List<List<Integer>> boards = parseBoards(input.subList(1, input.size()));
        return process(boards, lotteryNumbers, winGoal);
    }

    protected long process(List<List<Integer>> boards, List<Integer> lotteryNumbers, boolean winGoal){
        int boardsRemaining = boards.size()/10;
        for (Integer luckyNumber : lotteryNumbers) {
            for (int lineIndex = 0; lineIndex < boards.size(); lineIndex++) {
                List<Integer> line = boards.get(lineIndex);
                if(line.remove(luckyNumber) && line.size()==0){
                    // bingo
                    if(winGoal){
                        int boardIndex = lineIndex/10;
                        return calculateScore( boards.subList(boardIndex*10, boardIndex*10+5), luckyNumber);
                    } else {
                        int boardIndex = lineIndex/10;
                        if(boardsRemaining == 1){
                            return calculateScore( boards.subList(boardIndex*10, boardIndex*10+5), luckyNumber);
                        }
                        boards.subList(boardIndex*10, boardIndex*10+10).stream().forEach(integers -> integers.clear());
                        boardsRemaining--;
                    }

                }
            }
        }
        return -1;
    }

    private long calculateScore(List<List<Integer>> board, int luckyNumber){
        return board.stream().flatMap(List::stream).reduce(Integer::sum).get() * luckyNumber;
    }

    protected List<Integer> parseLotteryNumbers(String lotteryNumbersSequence){
        return Arrays.stream(lotteryNumbersSequence.split(",")).map(Integer::valueOf).collect(Collectors.toList());
    }

    protected List<List<Integer>> parseBoards(List<String> lines){
        List<List<Integer>> rows = lines.stream()
                                        .filter(s -> !s.equals(""))
                                        .map(s -> Arrays.stream(s.split(" "))
                                                        .filter(s1 -> !s1.equals("")) // avoid double space
                                                        .map(Integer::valueOf)
                                                        .collect(Collectors.toList()))
                                        .collect(Collectors.toList());

        List<List<Integer>> boards = new ArrayList<>();
        // create columns and add them sequentially after the rows
        // eventually each board will oocupy 10 entris in the board list, 5 for rows and 5 for columns
        for (int currentBoard = 0; currentBoard < rows.size(); currentBoard += 5) {
            boards.addAll(rows.subList(currentBoard, currentBoard+5));
            for (int columnIndex = 0; columnIndex < 5; columnIndex++) {
                List<Integer> column = new ArrayList<>();
                column.add(rows.get(currentBoard  ).get(columnIndex));
                column.add(rows.get(currentBoard+1).get(columnIndex));
                column.add(rows.get(currentBoard+2).get(columnIndex));
                column.add(rows.get(currentBoard+3).get(columnIndex));
                column.add(rows.get(currentBoard+4).get(columnIndex));
                boards.add(column);
            }
        }

        return boards;
    }

}
