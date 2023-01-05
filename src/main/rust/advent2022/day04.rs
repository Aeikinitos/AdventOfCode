use std::fs::read_to_string;
use itertools::Itertools;


fn main() {
    let contents = read_to_string("inputs/day04")
        .expect("Should have been able to read the file");

    let overlapping_groups = process(&contents, overlap);
    println!("Part 1: {overlapping_groups}" );

    let overlapping_at_all_groups = process(&contents, overlap_at_all);
    println!("Part2: {overlapping_at_all_groups}" );
}

fn process(contents: &String, pred: fn(&(i32, i32, i32, i32)) -> bool) -> usize {
    contents.split('\n')
        .flat_map(|s| s.split(&['-', ','][..]))
        .map(|s| s.trim().parse::<i32>().unwrap())
        .tuples::<(i32, i32, i32, i32)>()
        .filter(pred)
        .count()
}

fn overlap((elf1_start, elf1_end, elf2_start, elf2_end): &(i32,i32,i32,i32)) -> bool {
    (elf1_start <= elf2_start && elf1_end >= elf2_end) || (elf2_start <= elf1_start && elf2_end >= elf1_end)
}

fn overlap_at_all((elf1_start, elf1_end, elf2_start, elf2_end): &(i32,i32,i32,i32)) -> bool {
    between(elf1_start, elf2_start, elf2_end) ||
        between(elf1_end, elf2_start, elf2_end) ||
        between(elf2_start, elf1_start, elf1_end) ||
        between(elf2_end, elf1_start, elf1_end)

}

fn between(value:&i32, start:&i32, end:&i32) -> bool {
    start <= value && value <= end
}