use std::fs::read_to_string;
use enum_map::{enum_map, Enum};
use std::str::FromStr;
use strum_macros::EnumString;
use itertools;
use itertools::Itertools;

#[derive(Debug, PartialEq, Enum, EnumString, Copy, Clone)]
enum Shapes {
    // A for Rock, B for Paper, and C for Scissors.
    // X for Rock, Y for Paper, and Z for Scissors.
    // 1 for Rock, 2 for Paper, and 3 for Scissors
    A,
    B,
    C,
    X,
    Y,
    Z
}



fn main() {
    let contents = read_to_string("inputs/day02")
        .expect("Should have been able to read the file");

    let shape_points = enum_map! {
        Shapes::A => 1,
        Shapes::B => 2,
        Shapes::C => 3,
        Shapes::X => 1,
        Shapes::Y => 2,
        Shapes::Z => 3
    };

    let mut total_points = 0;

    contents.split('\n').for_each(|item| {
        let (player1, player2) = get_tuple(item);
        // decide if its win/lose/draw and get points
        total_points += get_round_points(shape_points[player1], shape_points[player2]);
        // get points based on shape
        total_points += shape_points[player2];
    });

    println!("Part 1: {total_points}" );

    total_points = 0;
    contents.split('\n').for_each(|item| {
        let (player1, outcome) = get_tuple(item);
        // decide the shape to match the outcome
        let choosen_shape = get_shape_for_outcome(player1, outcome);

        total_points += get_round_points(shape_points[player1], shape_points[choosen_shape]);

        // get points based on shape
        total_points += shape_points[choosen_shape];
    });

    println!("Part 2: {total_points}");
}

fn get_tuple(item: &str) -> (Shapes, Shapes) {
    item.split(' ').map(|string| string.trim()).filter_map(|literal| Shapes::from_str(literal).ok()).collect_tuple().unwrap()
}
// 0 if you lost, 3 if the round was a draw, and 6 if you won
// A < B < C < A
// 1 < 2 < 3 < 1
fn get_round_points(p1: i32, p2: i32) -> i32 {
    if p1 == p2 {
        3
    } else if (p1-p2).rem_euclid(3) == 1{
        // p1 won
        0
    } else {
        6
    }
}

fn get_shape_for_outcome(p1: Shapes, outcome: Shapes) -> Shapes {
    if outcome == Shapes::Y {
        return p1;
    } else if outcome == Shapes::X {
        // lose
        match p1 {
            Shapes::A => Shapes::C,
            Shapes::B => Shapes::A,
            Shapes::C => Shapes::B,
            _ => panic!("Invalid player choice")
        }
    } else {
        // win
        match p1 {
            Shapes::A => Shapes::B,
            Shapes::B => Shapes::C,
            Shapes::C => Shapes::A,
            _ => panic!("Invalid player choice")
        }
    }
}

