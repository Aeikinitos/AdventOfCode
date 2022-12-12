use std::fs::read_to_string;

use pathfinding::astar;

#[derive(Clone, Debug, Eq, Hash, Ord, PartialEq, PartialOrd)]
struct Pos(i32, i32, Vec<Vec<i32>>);

impl Pos {
    fn distance(&self, other: &Pos) -> usize {
        ((self.0 - other.0).abs() + (self.1 - other.1).abs()) as usize
    }

    fn neighbours(&self) -> Vec<(Pos, usize)> {
        let Pos(x, y, map) = self;
        let mut valid_moves = vec![];
        vec![(0,1), (0,-1), (1,0), (-1,0)].iter().for_each(|(dx,dy)| {
            if in_bounds(x+dx, y+dy, map.len() as i32, map[0].len() as i32) && map[(x+dx) as usize][(y+dy) as usize] <= map[*x as usize][*y as usize]+1 {
                valid_moves.push(Pos(x+dx, y+dy, map.clone()));
            }
        });
        valid_moves.into_iter().map(|p|(p,1)).collect()
    }
}

fn in_bounds(x: i32, y:i32, map_len_x: i32, map_len_y: i32) -> bool {
    x>=0 && y>=0 && x < map_len_x && y < map_len_y
}

fn main() {
    let contents = read_to_string("inputs/advent2022/day12")
        .expect("Should have been able to read the file");

    let map = parse_map(&contents);
    let goal_coords = get_goal(&contents);
    let goal = Pos(goal_coords.0,goal_coords.1,vec![]);


    let mut min_path = map.len() * map[0].len();
    for x in 0..map.len() {
        for y in 0..map[x].len() {
            if map[x][y] == 97 {
                println!("testing {x},{y}");
                let result = astar(&Pos(x as i32, y as i32, map.clone()), |p| p.neighbours(), |p| p.distance(&goal) / 2,
                                   |p| goal_found(p, &goal));

                match result {
                    Some(path) => {if min_path > path.1 {min_path = path.1}},
                    None => {}
                }
            }

        }
    }
    println!("Part 2: {min_path}");
}
fn goal_found(p: &Pos, goal: &Pos) -> bool {
    p.0 == goal.0 && p.1 == goal.1
}

fn parse_map(input: &String) -> Vec<Vec<i32>> {
    let mut map = vec![];
    for (_row_index, line) in input.lines().enumerate() {
        map.push(line.chars().map(|char| {
                if char == 'S' {
                    97
                } else if char == 'E' {
                    122
                } else {
                    char as i32
            }
        }).collect())
    }

    map
}

fn get_goal(input: &String) -> (i32,i32) {
    for (row_index, line) in input.lines().enumerate() {
        for (column_index, column) in line.chars().enumerate() {
            if column == 'E' {
                return (row_index as i32, column_index as i32);
            }
        }
    }
    (0,0)
}
