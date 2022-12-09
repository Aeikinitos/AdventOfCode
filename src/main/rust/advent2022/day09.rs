use std::collections::HashSet;
use std::fs::read_to_string;

macro_rules! loopn {
  ($n:expr, $body:block) => {
      for _ in 0..$n {
          $body
      }
  }
}

struct Position {
    x: i32,
    y: i32
}
fn main() {
    let contents = read_to_string("inputs/advent2022/day09")
        .expect("Should have been able to read the file");

    let mut tail_positions = HashSet::new();
    // let mut tail_positions_vec = Vec::new();
    let mut head = Position{x:0,y:0};
    let mut tail = Position{x:0,y:0};

    tail_positions.insert((tail.x, tail.y));

    let mut lines = contents.lines().rev().collect::<Vec<_>>();
    while let Some(line) = lines.pop() {
        match line.split(' ').collect::<Vec<_>>()[..] {
            [dir, count_str] => {
                let count = count_str.parse().expect("count was not a digit");
                loopn!(count, {
                    move_head(&mut head, dir);
                    check_and_move_tail(&mut tail, &head);
                    tail_positions.insert((tail.x,tail.y));
                    // tail_positions_vec.push((tail.x,tail.y));
                })
            },
            // ["L", count] => {println!("L")},
            // ["U", count] => {println!("U")},
            // ["D", count] => {println!("D")},
            [..] => panic!("klatsa")
        }
    }



    println!("Part 1: {:?}", tail_positions.len());
}

fn move_head(head: &mut Position, dir: &str) {
    match dir {
        "R" => {head.x = head.x +1},
        "L" => {head.x = head.x -1},
        "U" => {head.y = head.y +1},
        "D" => {head.y = head.y -1},
        _ => panic!("klatsa")
    }
}


fn check_and_move_tail(tail: &mut Position, head: &Position) {
    if(is_tail_close_to_head(tail, head)) {
        return;
    }
    match ((tail.x, tail.y),(head.x, head.y)) {
        ((tail_x, tail_y), (head_x, head_y)) if tail_x == head_x => {tail.y = tail.y + get_tail_row_move_direction(tail, head)}, // same row
        ((tail_x, tail_y), (head_x, head_y)) if tail_y == head_y => {tail.x = tail.x + get_tail_column_move_direction(tail, head)}, // same column
        ((tail_x, tail_y), (head_x, head_y)) => {
            let row_move = get_tail_row_move_direction(tail, head);
            let column_move = get_tail_column_move_direction(tail, head);
            tail.x = tail.x + column_move;
            tail.y = tail.y + row_move;
        }, // diagonally

    }
}

fn is_tail_close_to_head(tail: &Position, head: &Position) -> bool {
    (((tail.x-head.x).pow(2) + (tail.y-head.y).pow(2)) as f32).sqrt() <= 1.42 //sqrt of 2
}

fn get_tail_row_move_direction(tail: &Position, head: &Position) -> i32 {
    (head.y-tail.y) / (head.y-tail.y).abs()
}

fn get_tail_column_move_direction(tail: &Position, head: &Position) -> i32 {
    (head.x-tail.x) / (head.x-tail.x).abs()
}