use std::cmp::{max, min};
use std::collections::HashSet;
use std::fs::read_to_string;
use itertools::Itertools;

const DROP_SAND:(i32, i32) = (500,0);

fn main() {
    let contents = read_to_string("inputs/day14")
        .expect("Should have been able to read the file");

    let mut blocked_coords = get_rock_points(contents);

    let lowest_rock_formation = get_lowest_rock_formation(&mut blocked_coords);

    let sand_drops = drop_sand_until_stable(&mut blocked_coords, lowest_rock_formation+1, None);
    println!("Part 1: {:?}", sand_drops);

    let sand_drops = sand_drops + drop_sand_until_stable(&mut blocked_coords, lowest_rock_formation+1, Some(lowest_rock_formation +2));
    println!("Part 2: {:?}", sand_drops);
}

fn get_lowest_rock_formation(blocked_coords: &mut HashSet<(i32, i32)>) -> i32 {
    *blocked_coords.iter().map(|(_, y)| y).max().expect("Should have a lowest point")
}

fn drop_sand_until_stable(blocked_coords: &mut HashSet<(i32, i32)>, void: i32, floor_y: Option<i32>) -> i32 {
    let mut sand_drops = 0;
    loop {
        let mut sand = DROP_SAND;

        while let Some(new_sand_point) = can_move(sand, &blocked_coords, floor_y) {
            sand = new_sand_point;
            if into_the_void(void, floor_y, &mut sand) {
                return sand_drops;
            }
        }
        sand_drops += 1;
        blocked_coords.insert(sand);

        if sand == DROP_SAND {
            return sand_drops;
        }
    }
}

fn into_the_void(void: i32, floor_y: Option<i32>, sand: &mut (i32, i32)) -> bool {
    floor_y.is_none() && sand.1 == void
}

fn get_rock_points(contents: String) -> HashSet<(i32, i32)> {
    let mut blocked_coords = HashSet::new();
    contents.lines().for_each(|line| {
        let coords = line
            .split("->")
            .map(coord_str_to_tuple())
            .collect::<Vec<_>>();

        for i in 0..coords.len() - 1 {
            blocked_coords.extend(fill_line(coords[i], coords[i + 1]));
        }
    });
    blocked_coords
}

fn coord_str_to_tuple() -> fn(&str) -> (i32, i32) {
    |coord|
        coord.trim()
            .split(',')
            .map(|x| x.parse::<i32>().expect("coord should have been a number"))
            .collect_tuple::<(i32, i32)>().expect("Coords should have been in pairs")
}

fn can_move(sand: (i32, i32), blocked_coords: &HashSet<(i32, i32)>, floor_y: Option<i32>) -> Option<(i32,i32)> {
    if !blocked_coords.contains(&(sand.0, sand.1 +1)) && is_not_on_floor(sand, floor_y) {
        return Some((sand.0, sand.1 +1));
    } else if !blocked_coords.contains(&(sand.0-1, sand.1 +1)) && is_not_on_floor(sand, floor_y) {
        return Some((sand.0-1, sand.1 +1));
    } else if !blocked_coords.contains(&(sand.0+1, sand.1 +1)) && is_not_on_floor(sand, floor_y) {
        return Some((sand.0+1, sand.1 +1));
    }
    None
}

fn is_not_on_floor(sand: (i32, i32), floor_y: Option<i32>) -> bool {
    floor_y.is_none() || sand.1 + 1 != floor_y.unwrap()
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
