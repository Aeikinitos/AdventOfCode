use std::fs::read_to_string;
use std::ops::Add;
use std::time::Instant;
use derive_new::new;
use pathfinding::astar;

//  01234567 -> y
//0 #.######
//1 #>>.<^<#
//2 #.<..<<#
//3 #>v.><>#
//4 #<^v^^>#
//5 ######.#
//x
//|
//V
const MAX_ALLOWED_X: i32 = 25; // real
const MAX_ALLOWED_Y: i32 = 120; // real
static INPUT: &str = "inputs/day24"; // test
// const MAX_ALLOWED_X: i32 = 4; // test
// const MAX_ALLOWED_Y: i32 = 6; // test
// static INPUT: &str = "inputs/day24_test"; // test

#[derive(new, Debug, Copy, Clone, Eq, PartialEq, Hash)]
struct Point(i32, i32);

impl Add for Point {
    type Output = Self;
    fn add(self, other: Self) -> Point {
        Point(
            wrapping_add(self.0, other.0, MAX_ALLOWED_X),
            wrapping_add(self.1, other.1, MAX_ALLOWED_Y)
        )
    }
}

fn wrapping_add(x: i32, y:i32, max_allowed: i32) -> i32 {
    if 1 <= x+y && x+y <= max_allowed {
        x+y
    } else if y < 0 { // going UP or left
        max_allowed
    } else {
        1
    }
}

#[derive(new, Debug, Copy, Clone, Eq, PartialEq, Hash)]
struct Blizzard {
    point: Point,
    direction: Direction
}

impl Blizzard {
    fn calculate_next_pos(&self) -> Self {
        // point + direction.get_delta() with wrapping
        Self {
            point: self.point + self.direction.get_delta(),
            direction: self.direction
        }
    }

    fn is_on(&self, point: &Point) -> bool {
        self.point == *point
    }
}

#[derive(Debug, Copy, Clone, Eq, PartialEq, Hash)]
enum Direction {
    Up,
    Down,
    Left,
    Right
}

#[derive(new, Eq, PartialEq, Hash, Clone)]
struct Player {
    position: Point,
    blizzards: Vec<Blizzard>
}

impl Player {
    fn get_neighbouring_points(&self) -> Vec<Point> {

        let mut points = vec![self.position.clone()];

        if self.position == Point(0,1) {
            points.push(Point(1,1));
        } else if self.position == Point(MAX_ALLOWED_X, MAX_ALLOWED_Y) {
            points.push(Point(MAX_ALLOWED_X+1,MAX_ALLOWED_Y));
        } else if self.position == Point(MAX_ALLOWED_X+1, MAX_ALLOWED_Y) {
            points.push(Point(MAX_ALLOWED_X,MAX_ALLOWED_Y));
        } else if self.position == Point(1, 1) {
            points.push(Point(0, 1));
        }

        (self.position.1 > 1)               .then(||points.push(self.position + Direction::Left.get_delta()));
        (self.position.1 < MAX_ALLOWED_Y)   .then(||points.push(self.position + Direction::Right.get_delta()));
        (self.position.0 > 1)               .then(||points.push(self.position + Direction::Up.get_delta()));
        (self.position.0 < MAX_ALLOWED_X)   .then(||points.push(self.position + Direction::Down.get_delta()));

        points
    }

    fn next_states(&self) -> Vec<(Player, usize)> {
        let next_blizzards = self.blizzards.iter().map(|blizzard| blizzard.calculate_next_pos()).collect::<Vec<_>>();

        self.get_neighbouring_points()
            .into_iter()
            .filter(|point| is_a_calm_point(&next_blizzards, point))
            .map(|point| (Self::new(point, next_blizzards.clone()),1))
            .collect::<Vec<_>>()

    }

    fn distance(&self, other: &Point) -> usize {
        ((self.position.0 - other.0).abs() + (self.position.1 - other.1).abs()) as usize
    }
}

fn is_a_calm_point(next_blizzards: &Vec<Blizzard>, point: &Point) -> bool {
    !next_blizzards.iter().any(|blizzard| blizzard.is_on(point))
}

impl Direction {
    fn get_delta(&self) -> Point {
        match self {
            Direction::Up => {Point::new(-1,0)}
            Direction::Down => {Point::new(1,0)}
            Direction::Left => {Point::new(0,-1)}
            Direction::Right => {Point::new(0,1)}
        }
    }

    fn from(direction: char) -> Self {
        match direction {
            '^' => Direction::Up,
            'v' => Direction::Down,
            '<' => Direction::Left,
            '>' => Direction::Right,
            _ => panic!("incorrect direction")
        }
    }
}

fn main() {
    let start = Instant::now();
    let contents = read_to_string(INPUT)
        .expect("Should have been able to read the file");

    let blizzards = parse(contents);

    let starting = Point::new(0,1);
    let goal = Point::new(MAX_ALLOWED_X+1,MAX_ALLOWED_Y);
    let player = Player::new(starting.clone(), blizzards);

    let path = find_path_from_to(&player, &goal);
    println!("Part 1 {}: elapsed {:?}", path.1, start.elapsed());

    let path_back = find_path_from_to(&path.0, &starting);
    println!("Mid trip elapsed {:?}", start.elapsed());

    let final_path = find_path_from_to(&path_back.0, &goal);
    println!("Part 2 {}: elapsed {:?}", path.1+path_back.1+final_path.1, start.elapsed());
}

fn parse(contents: String) -> Vec<Blizzard> {
    contents.lines()
        .enumerate()
        .flat_map(|(line_idx, line)| {
            line.chars()
                .enumerate()
                .filter(|(_, char)| ['>', 'v', '^', '<'].contains(char))
                .map(move |(char_idx, blizzard_dir)| Blizzard::new(Point(line_idx as i32, char_idx as i32), Direction::from(blizzard_dir)))
    }).collect::<Vec<_>>()
}

fn find_path_from_to(player: &Player, goal: &Point) -> (Player, usize) {
    match astar(player, |p| p.next_states(), |p| p.distance(goal),|p| p.position == *goal) {
        Some(path) => {
            (path.0[path.1].clone(), path.1)
        }
        _ => {panic!("No way home spiderman")}
    }
}