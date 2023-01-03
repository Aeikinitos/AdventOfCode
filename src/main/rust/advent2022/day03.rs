use std::fs::read_to_string;
use itertools::Itertools;


fn main() {
    let contents = read_to_string("../../../../inputs/advent2022/day03")
        .expect("Should have been able to read the file");

    let mut total = 0;
    contents.split('\n').for_each(|rucksack| {
        let (left, right) = rucksack.split_at(rucksack.len()/2);
        total += left.chars()
            .into_iter()
            .filter(|left_char| right.contains(*left_char))
            .unique()
            .map(|left_char|get_priority(left_char))
            .sum::<i32>();
    });

    println!("Part 1: {total}" );


    // part 2
    total = 0;
    let contents_vec: Vec<_> = contents.split('\n').collect();
    contents_vec.chunks(3).for_each(|group|{
         for i in 65u8..=122 {
             if group.into_iter()
                 .filter(|rucksack| rucksack.contains(i as char))
                 .count() == 3 {
                 total += get_priority(i as char);
             }
         }
    });

    println!("Part2: {total}" );
}

fn get_priority(badge: char) -> i32 {
    if badge.is_lowercase() {
        badge as i32 - 96
    } else {
        badge as i32 - 38
    }
}
