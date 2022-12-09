use std::collections::HashSet;
use std::fs::read_to_string;
use itertools::Itertools;

macro_rules! loopn {
  ($n:expr, $body:block) => {
      for _ in 0..$n {
          $body
      }
  }
}

#[derive(Copy, Clone)]
struct Position {
    x: i32,
    y: i32
}
fn main() {
    let contents = read_to_string("inputs/advent2022/day09")
        .expect("Should have been able to read the file");

    let mut part1_tail_positions = HashSet::new();
    let mut part2_tail_positions = HashSet::new();

    let mut head = Position{x:0,y:0};
    let mut tails = vec![Position{x:0,y:0}; 9];

    part1_tail_positions.insert((0, 0)); // insert initial position
    part2_tail_positions.insert((0, 0)); // insert initial position

    for line in contents.lines(){
        let (dir, count_str) = line.split(' ').collect_tuple().expect("klatsa");
        let count = count_str.parse().expect("count was not a digit");
        loopn!(count, {
                    move_head(&mut head, dir);
                    check_and_move_tails(&mut tails, &head);
                    part1_tail_positions.insert((tails[0].x,tails[0].y));
                    part2_tail_positions.insert((tails[8].x,tails[8].y));
                })
    }

    println!("Part 1: {:?}", part1_tail_positions.len());
    println!("Part 2: {:?}", part2_tail_positions.len());
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

fn check_and_move_tails(tails: &mut Vec<Position>, head: &Position) {
    check_and_move_tail(&mut tails[0], head);
    for i in 1..=8 {
        let new_head = &tails[i-1].clone();
        check_and_move_tail(&mut tails[i], new_head);
    }
}

fn check_and_move_tail(tail: &mut Position, head: &Position) {
    if is_tail_close_to_head(tail, head) {
        return;
    }
    match ((tail.x, tail.y),(head.x, head.y)) {
        ((tail_x, _tail_y), (head_x, _head_y)) if tail_x == head_x => { move_vertically(tail, head) }, // same row
        ((_tail_x, tail_y), (_head_x, head_y)) if tail_y == head_y => { move_horizontally(tail, head) }, // same column
        (_, _) => { move_diagonally(tail, head);}, // diagonally

    }
}


fn is_tail_close_to_head(tail: &Position, head: &Position) -> bool {
    (((tail.x-head.x).pow(2) + (tail.y-head.y).pow(2)) as f32).sqrt() <= 1.42 //sqrt of 2
}

fn move_vertically(tail: &mut Position, head: &Position) {
    tail.y = tail.y + get_tail_row_move_direction(tail, head)
}

fn move_horizontally(tail: &mut Position, head: &Position) {
    tail.x = tail.x + get_tail_column_move_direction(tail, head)
}

fn move_diagonally(tail: &mut Position, head: &Position) {
    let row_move = get_tail_row_move_direction(tail, head);
    let column_move = get_tail_column_move_direction(tail, head);
    tail.x = tail.x + column_move;
    tail.y = tail.y + row_move;
}

fn get_tail_row_move_direction(tail: &Position, head: &Position) -> i32 {
    (head.y-tail.y) / (head.y-tail.y).abs()
}

fn get_tail_column_move_direction(tail: &Position, head: &Position) -> i32 {
    (head.x-tail.x) / (head.x-tail.x).abs()
}