use std::fs::read_to_string;
use itertools::Itertools;

static MARKER_SIZE: usize = 4;
static PACKET_SIZE: usize = 14;

fn main() {
    let contents = read_to_string("inputs/advent2022/day06")
        .expect("Should have been able to read the file");

    let characters: Vec<char> = contents.chars().collect();

    println!("Part 1 - {}", find_marker(&characters, MARKER_SIZE));
    println!("Part 2 - {}", find_marker(&characters, PACKET_SIZE));

}

fn find_marker(characters: &Vec<char>, window_size: usize) -> usize {
    for i in 0..characters.len() - window_size {
        if are_different(&characters[i..i + window_size]) {
            return i + window_size;
        }
    }
    0
}

fn are_different(message: &[char]) -> bool {
    let unique_len = message
        .iter()
        .unique()
        .collect::<Vec<&char>>()
        .len();

    message.len() == unique_len
}