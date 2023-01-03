use std::fs::read_to_string;
use itertools::Itertools;
use regex::Regex;
use substring::Substring;

const STACKS_NUM: usize = 9;

fn main() {
    let contents = read_to_string("../../../../inputs/advent2022/day05")
        .expect("Should have been able to read the file");

    let (stack_input, move_instructions): (&str, &str) = contents.split("\n\n").collect_tuple().unwrap();

    let mut part1_stacks: Vec<Vec<_>> = create_stacks(stack_input);
    let mut part2_stacks = part1_stacks.clone();

    move_instructions.lines().for_each(|line| {
        let (volume, from, to) = read_instruction(line);

        // Part 1
        move_cratemover9000(&mut part1_stacks, volume, from, to);

        // Part 2
        move_cratemover9001(&mut part2_stacks, volume, from, to);

    });

    let part1_ans = get_answer(part1_stacks);
    println!("Part 1 {part1_ans}");
    assert_eq!(part1_ans, "CNSZFDVLJ");

    let part2_ans = get_answer(part2_stacks);
    println!("Part 2 {part2_ans}");
    assert_eq!(part2_ans, "QNDWLMGNS");
}

fn read_instruction(line: &str) -> (usize, usize, usize) {
    let pattern = Regex::new("move (?P<volume>\\d+) from (?P<from>\\d+) to (?P<to>\\d+)").unwrap();
    let instruction = pattern.captures(line).unwrap();
    let volume = instruction.name("volume").map_or(0, |m| m.as_str().parse().unwrap());
    let from = instruction.name("from").map_or(0, |m| m.as_str().parse().unwrap());
    let to = instruction.name("to").map_or(0, |m| m.as_str().parse().unwrap());
    (volume, from, to)
}

fn create_stacks(stack_input: &str) -> Vec<Vec<&str>>{
    let mut stacks: Vec<Vec<_>> = (0..STACKS_NUM).map(|_| Vec::<&str>::new()).collect();
    stack_input.lines().rev().skip(1).for_each(|line| {
        (0..STACKS_NUM).for_each(|i| {
            let crate_box: &str = line.substring(i * 4 + 1, i * 4 + 2);
            if !crate_box.trim().is_empty() {
                stacks.get_mut(i).unwrap().push(crate_box);
            }
        })
    });

    stacks
}

fn move_cratemover9001(part2_stacks: &mut Vec<Vec<&str>>, volume: usize, from: usize, to: usize) {
    let remaining_stack_size = part2_stacks.get_mut(from - 1).unwrap().len() - volume;
    let mut moving_crates = part2_stacks.get_mut(from - 1).unwrap().drain(remaining_stack_size..).collect();
    part2_stacks.get_mut(to - 1).unwrap().append(&mut moving_crates);
}

fn move_cratemover9000(part1_stacks: &mut Vec<Vec<&str>>, volume: usize, from: usize, to: usize) {
    (0..volume).for_each(|_| {
        let popped = part1_stacks.get_mut(from - 1).unwrap().pop().unwrap();
        part1_stacks.get_mut(to - 1).unwrap().push(popped);
    });
}

fn get_answer(mut part1_stacks: Vec<Vec<&str>>) -> String {
    (0..STACKS_NUM)
        .map(|i| part1_stacks.get_mut(i).unwrap().pop().unwrap())
        .fold(String::new(), |acc, x| format!("{acc}{x}"))
}

