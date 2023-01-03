use std::fs::read_to_string;

const ROCK_DROP: i64 = 2755;
// const ROCK_DROP: i64 = 2022;
const SPAWN_POINT: Point = Point {x:3, y:4};

struct TetrisGrid {
    spawn_point: Point,
    blocked_left: Vec<Face>,
    blocked_right: Vec<Face>,
    blocked_top: Vec<Face>,
}
impl TetrisGrid {
    fn collides_moving_left(&self, rock: &Rock) -> bool {
        let left_side_points = rock.get_left_side_faces_at_location();
        for face in &self.blocked_left {
            if face.vec_collides(&left_side_points) {
                return true
            }
        }
        false
    }
    fn collides_moving_right(&self, rock: &Rock) -> bool {
        let right_side_points = rock.get_right_side_faces_at_location();
        for face in &self.blocked_right {
            if face.vec_collides(&right_side_points) {
                return true
            }
        }
        false
    }
    fn collides_moving_down(&self, rock: &Rock) -> bool {
        let bottom_side_points = rock.get_bottom_side_faces_at_location();
        for face in &self.blocked_top {
            if face.vec_collides(&bottom_side_points) {
                return true
            }
        }
        false
    }
    fn update_faces(&mut self, resting_rock: &Rock) {
        // self.blocked_left = resting_rock.get_right_side_faces_at_location();
        self.blocked_left.extend(&resting_rock.get_right_side_faces_at_location());
        self.blocked_right.extend(&resting_rock.get_left_side_faces_at_location());
        self.blocked_top.extend(&resting_rock.get_top_side_faces_at_location());
    }
    fn update_spawn_point(&mut self, resting_rock: &Rock) {
        if self.spawn_point.y < resting_rock.height +resting_rock.location.y+SPAWN_POINT.y {
            self.spawn_point = Point::new(self.spawn_point.x, resting_rock.height +resting_rock.location.y+SPAWN_POINT.y);
        }
    }
}

#[derive(Copy, Clone, Debug)]
struct Face {
    start: Point,
    end: Point
}
impl Face {
    fn new(x:i64, y:i64, j:i64, k: i64) -> Self {
        Self {start: Point::new(x,y), end: Point::new(j,k)}
    }
    fn point_collides(&self, face: &Face) -> bool {
        if between(&self.start, &face.start, &face.end)
        || between(&self.end, &face.start, &face.end)
        || between( &face.start, &self.start, &self.end)
        || between( &face.end, &self.start, &self.end) {
            return true
        }
        false
    }
    fn vec_collides(&self, faces: &Vec<Face>) -> bool {
        for face in faces {
            if self.point_collides(face) {
                return true;
            }
        }
        false
    }
    fn move_at_location(&self, location: &Point) -> Self {
        Face::new(self.start.x+location.x, self.start.y+location.y, self.end.x+location.x, self.end.y+location.y)
    }
}

#[derive(Copy, Clone, Debug)]
struct Point {
    x: i64,
    y: i64
}
impl Point {
    fn new(x:i64, y:i64) -> Self {
        Self { x,y }
    }
}

#[derive(Clone)]
struct Rock {
    left_side: Vec<Face>,
    bottom_side: Vec<Face>,
    right_side: Vec<Face>,
    top_side: Vec<Face>,
    location: Point,
    height: i64
}

impl Rock {
    fn get_left_side_faces_at_location(&self) -> Vec<Face> {
        self.left_side.iter().map(|face| face.move_at_location(&self.location)).collect::<Vec<Face>>()
    }
    fn get_bottom_side_faces_at_location(&self) -> Vec<Face> {
        self.bottom_side.iter().map(|face| face.move_at_location(&self.location)).collect::<Vec<Face>>()
    }
    fn get_right_side_faces_at_location(&self) -> Vec<Face> {
        self.right_side.iter().map(|face| face.move_at_location(&self.location)).collect::<Vec<Face>>()
    }
    fn get_top_side_faces_at_location(&self) -> Vec<Face> {
        self.top_side.iter().map(|face| face.move_at_location(&self.location)).collect::<Vec<Face>>()
    }
}

#[derive(Copy, Clone)]
enum Rocks {
    Horizontal,
    Cross,
    El,
    Vertical,
    Block
}
impl Rocks {
    fn next(&self) -> Rocks {
        use Rocks::*;
        match *self {
            Horizontal => Cross,
            Cross => El,
            El => Vertical,
            Vertical => Block,
            Block => Horizontal
        }
    }

    fn get_left_side(&self) -> Vec<Face> {
        use Rocks::*;
        match *self {
            Horizontal => vec![Face::new(0,0,0,0)],
            Cross => vec![Face::new(1,0,1,2),
                          Face::new(0,1,0,1)],
            El => vec![Face::new(2,1,2,2),
                       Face::new(0,0,0,0)],
            Vertical => vec![Face::new(0,0,0,3)],
            Block => vec![Face::new(0,0,0,1)]
        }
    }
    fn get_bottom_side(&self) -> Vec<Face> {
        use Rocks::*;
        match *self {
            Horizontal => vec![Face::new(0,0,3,0)],
            Cross => vec![Face::new(0,1, 2, 1),
                          Face::new(1,0,1,0)],
            El => vec![Face::new(0,0,2,0)],
            Vertical => vec![Face::new(0,0, 0,0)],
            Block => vec![Face::new(0,0,1,0)]
        }
    }
    fn get_right_side(&self) -> Vec<Face> {
        use Rocks::*;
        match *self {
            Horizontal => vec![Face::new(3,0,3,0)],
            Cross => vec![Face::new(1,0,1,2),
                          Face::new(2,1,2,1)],
            El => vec![Face::new(2,0,2,2)],
            Vertical => vec![Face::new(0,0,0,3)],
            Block => vec![Face::new(1,0,1,1)]
        }
    }
    fn get_top_side(&self) -> Vec<Face> {
        use Rocks::*;
        match *self {
            Horizontal => vec![Face::new(0,0,3,0)],
            Cross => vec![Face::new(0,1, 2, 1),
                          Face::new(1,2,1,2)],
            El => vec![Face::new(0,0,2,0),
                       Face::new(2,2,2,2)],
            Vertical => vec![Face::new(0,3, 0,3)],
            Block => vec![Face::new(0,1,1,1)]
        }
    }
    fn get_top_point(&self) -> i64 {
        use Rocks::*;
        match *self {
            Horizontal => 0,
            Cross => 2,
            El => 2,
            Vertical => 3,
            Block => 1
        }
    }
}

struct Spawner {
    next_rock: Rocks
}
impl Spawner {
    fn spawn(&mut self, at_location: Point) -> Rock {
        let spawned_rock = self.next_rock;
        self.next_rock = self.next_rock.next();
        Rock {
            left_side: spawned_rock.get_left_side(),
            bottom_side: spawned_rock.get_bottom_side(),
            right_side: spawned_rock.get_right_side(),
            top_side: spawned_rock.get_top_side(),
            location: at_location,
            height: spawned_rock.get_top_point()
        }
    }
}

#[derive(Copy, Clone)]
enum Direction {
    Left,
    Right
}

impl Direction {
    fn from(char: char) -> Self {
        if char == '<' {
            Direction::Left
        } else if char == '>' {
            Direction::Right
        } else {
            panic!("found a special {char}");
        }

    }
}
struct Mover {
    directions: Vec<Direction>,
    next_dir: usize
}
impl Mover {
    fn from_str(direction: &String) -> Self {
        Self {
            directions: direction.chars().map(|char| Direction::from(char)).collect::<Vec<_>>(),
            next_dir: 0,
        }
    }
    fn next(&mut self) -> Direction {
        if self.next_dir >= self.directions.len() {
            self.next_dir = 1;
        } else {
            self.next_dir += 1;
        }
        self.directions[self.next_dir-1]
    }
}

fn main() {
    let contents = read_to_string("../../../../inputs/advent2022/day17")
        .expect("Should have been able to read the file");

    // let contents = ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>".to_string();

    // max height = rocks * max_height_rock
    let max_height = ROCK_DROP * 4;

    let mut spawner = Spawner { next_rock: Rocks::Horizontal };
    let mut mover = Mover::from_str(&contents);
    let mut tetris = TetrisGrid {
        spawn_point: SPAWN_POINT,
        blocked_left: vec![Face { start: Point::new(0,0), end: Point::new(0, max_height) }],
        blocked_right: vec![Face { start: Point::new(8,0), end: Point::new(8, max_height) }],
        blocked_top: vec![Face { start: Point::new(0,0), end: Point::new(8, 0) }],
    };

    let mut drop = ROCK_DROP;
    let mut last = 0;
    while drop > 0 {
        drop -= 1;

        // spawn a rock
        let mut rock = spawner.spawn(tetris.spawn_point);
        if (ROCK_DROP-drop) % 5 == 0 {
            // Part 2:
            // get the height delta for every full rotation of the shapes
            // at the 326 rotation (326*5=1630 shapes) a pattern emerges that repeats every 349 rotations
            // each pattern consumes 349*5=1745 shapes and provides 2752 delta height
            // 1000000000000 % 1745 = 1125 remaining shapes
            // (1000000000000 - (1125+1630)) /1745 * 2752 = gained height
            // plus (1125+1630) shapes = 4363
            // total = 1577077363915
            println!("{}",tetris.spawn_point.y-SPAWN_POINT.y - last);
            last = tetris.spawn_point.y-SPAWN_POINT.y;

        }
        let mut resting = false;
        // while not resting
        while !resting {
            match mover.next() {
                Direction::Left => move_left(&mut tetris, &mut rock),
                Direction::Right => move_right(&mut tetris, &mut rock)
            }
            if !move_down(&mut tetris, &mut rock) {
                resting = true;
                // update faces
                tetris.update_faces(&rock);
                tetris.update_spawn_point(&rock);
            }
        }
    }
    println!("{}", tetris.spawn_point.y-SPAWN_POINT.y);

}

fn move_left(tetris: &mut TetrisGrid, mut rock: &mut Rock) {
    rock.location = Point::new(rock.clone().location.x - 1, rock.clone().location.y);
    if tetris.collides_moving_left(&rock) {
        rock.location = Point::new(rock.clone().location.x + 1, rock.clone().location.y);
    }
}

fn move_right(tetris: &mut TetrisGrid, mut rock: &mut Rock) {
    rock.location = Point::new(rock.clone().location.x + 1, rock.clone().location.y);
    if tetris.collides_moving_right(&rock) {
        rock.location = Point::new(rock.clone().location.x - 1, rock.clone().location.y);
    }
}
fn move_down(tetris: &mut TetrisGrid, mut rock: &mut Rock) -> bool{
    rock.location = Point::new(rock.clone().location.x, rock.clone().location.y - 1);
    return if tetris.collides_moving_down(&rock) {
        // dont move
        rock.location = Point::new(rock.clone().location.x, rock.clone().location.y + 1);
        false
    } else {
        true
    }
}

fn between(point: &Point, start:&Point, end:&Point) -> bool {
    (start.x <= point.x && point.x <= end.x && start.y == point.y && end.y == point.y)
        || (start.y <= point.y && point.y <= end.y && start.x == point.x && end.x == point.x)
}
