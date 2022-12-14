use std::cmp::{max, min};
use std::fs::read_to_string;
use itertools::Itertools;


fn main() {
    let contents = read_to_string("inputs/advent2022/day14")
        .expect("Should have been able to read the file");

    let mut blocked_coords:Vec<(i32,i32)> = vec![];
    contents.lines().for_each(|line|{
       let coords = line
           .split("->")
           .map(|coord|
               coord.trim()
                   .split(',')
                   .map(|x|x.parse::<i32>().expect("coord should have been a number"))
                   .collect_tuple::<(i32,i32)>().expect("Coords should have been in pairs"))
           .collect::<Vec<_>>();

        for i in 0..coords.len()-1 {
            blocked_coords.extend(fill_line(coords[i], coords[i+1]));
        }
        // println!("{:?}", coords);
    });

    let lowest = *blocked_coords.iter().map(|(_,y)| y).max().expect("Should have a lowest point");

    let (drop_sand_x, drop_sand_y) = (500,0);

    let mut sand_drops = 0;
    loop {
        let (mut sand_x, mut sand_y) = (drop_sand_x, drop_sand_y);
        while let Some((next_move_x, next_move_y)) = can_move((sand_x, sand_y), &blocked_coords) {
            sand_x = next_move_x;
            sand_y = next_move_y;
            // println!("next {sand_x},{sand_y}");
            if sand_y == lowest+1 {
                // reached void
                break;
            }
        }
        if sand_y == lowest+1 {
            // reached void
            break;
        }
        sand_drops += 1;
        blocked_coords.push((sand_x,sand_y));
    }


    println!("Part 1 {:?}", sand_drops);
}

fn can_move(sand: (i32, i32), blocked_coords: &Vec<(i32, i32)>) -> Option<(i32,i32)> {
    if !blocked_coords.contains(&(sand.0, sand.1 +1)) {
        return Some((sand.0, sand.1 +1));
    } else if !blocked_coords.contains(&(sand.0-1, sand.1 +1)) {
        return Some((sand.0-1, sand.1 +1));
    } else if !blocked_coords.contains(&(sand.0+1, sand.1 +1)) {
        return Some((sand.0+1, sand.1 +1));
    }
    None
}

fn fill_line((start_x, start_y): (i32, i32), (end_x,end_y): (i32, i32)) -> Vec<(i32, i32)> {
    let mut line = vec![];
    if start_x.eq(&end_x) {
        for i in min(start_y, end_y)..=max(start_y,end_y) {
            line.push((start_x, i));
        }
    } else {
        for i in min(start_x, end_x)..=max(start_x,end_x) {
            line.push((i, start_y));
        }
    }

    line
}
