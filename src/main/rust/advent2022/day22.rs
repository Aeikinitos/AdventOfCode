use std::fs::read_to_string;
use std::ops::Range;
use itertools::Itertools;

struct Player {
    position: (usize, usize),
    facing: Direction
}
impl Player {
    fn can_move(&self, map: &Map) -> bool {
        let (x,y) = map.get_next_pos(self.position, self.facing);
        map.rows[y][x] != Tile::Wall
    }

    fn move_forward(&mut self, map: &Map) {
        self.position = map.get_next_pos(self.position, self.facing);
    }

    fn can_move_cube(&self, cube: &Cube) -> bool {
        let (x,y, _) = cube.get_next_pos(self.position, self.facing);
        cube.rows[y][x] != Tile::Wall
    }

    fn move_forward_cube(&mut self, cube: &Cube) {
        let (x,y,dir) = cube.get_next_pos(self.position, self.facing);
        self.position = (x,y);
        self.facing = dir;
    }

    fn turn(&mut self, rotation: Rotation ) {
        self.facing = self.facing.rotate(rotation);
    }

    fn get_score(&self) -> usize {
        (self.position.1+1) * 1000 + (self.position.0+1) * 4 + self.facing.get_score()
    }
}

#[derive(Copy, Clone, Debug)]
enum Direction {
    Left,
    Right,
    Down,
    Up
}
impl Direction {
    fn rotate(&self, rotation: Rotation) -> Direction {
        match self {
            Direction::Left => {
                match rotation {
                    Rotation::Left => {Direction::Down}
                    Rotation::Right => {Direction::Up}
                }
            }
            Direction::Right => {
                match rotation {
                    Rotation::Left => {Direction::Up}
                    Rotation::Right => {Direction::Down}
                }
            }
            Direction::Down => {
                match rotation {
                    Rotation::Left => {Direction::Right}
                    Rotation::Right => {Direction::Left}
                }
            }
            Direction::Up => {
                match rotation {
                    Rotation::Left => {Direction::Left}
                    Rotation::Right => {Direction::Right}
                }
            }
        }
    }

    fn get_score(&self) -> usize {
        match self {
            Direction::Left => {2}
            Direction::Right => {0}
            Direction::Down => {1}
            Direction::Up => {3}
        }
    }
}

#[derive(Debug)]
enum Rotation {
    Left,
    Right
}

impl Rotation {
    fn new(char: &str) -> Self {
        if char == "R" {
            Rotation::Right
        } else {
            Rotation::Left
        }
    }
}
type Row = Vec<Tile>;

#[derive(Debug)]
struct Map {
    rows: Vec<Row>,
    rows_start: Vec<usize>,
    rows_end: Vec<usize>,
    columns_start: Vec<usize>,
    columns_end: Vec<usize>
}
impl Map {
    fn find_start_pos(&self) -> usize{
        let (index, _) = self.rows[0].iter().enumerate().find(|(_index, tile)| **tile == Tile::Corridor).unwrap();
        index
    }

    fn find_start_of_row(&self, row_index: usize) -> usize{
        let (index, _) = self.rows[row_index].iter().enumerate().find(|(_index, tile)| **tile != Tile::Empty).unwrap();
        index
    }

    fn find_end_of_row(&self, row_index: usize) -> usize{
        let (index, _) = self.rows[row_index].iter().enumerate().rev().find(|(_index, tile)| **tile != Tile::Empty).unwrap();
        index
    }

    fn find_start_of_column(&self, column_index: usize) -> usize{
        let (index, _) = self.rows.iter().enumerate().find(|(_index, row)| if row.len() > column_index {row[column_index] != Tile::Empty} else {false}).unwrap();
        index
    }

    fn find_end_of_column(&self, column_index: usize) -> usize{
        let (index, _) = self.rows.iter().enumerate().rev().find(|(_index, row)| if row.len() > column_index {row[column_index] != Tile::Empty} else {false}).unwrap();
        index
    }

    fn get_next_pos(&self, (x,y): (usize, usize), direction: Direction) -> (usize, usize) {
        return match direction {
            Direction::Left => {if self.rows_start[y] == x {(self.rows_end[y],y)} else {(x-1,y)}},
            Direction::Right => {if self.rows_end[y] == x {(self.rows_start[y],y)} else {(x+1,y)}},
            Direction::Down => {if self.columns_end[x] == y {(x,self.columns_start[x])} else {(x,y+1)}},
            Direction::Up => {if self.columns_start[x] == y {(x,self.columns_end[x])} else {(x,y-1)}}
        }
    }
}

struct Cube {
    rows: Vec<Row>,
    a: ((usize, Range<usize>), (Range<usize>, usize)),
    b: ((usize, Range<usize>), (Range<usize>, usize)),
    c: ((usize, Range<usize>), (Range<usize>, usize)),
    d: ((usize, Range<usize>), (usize, Range<usize>)),
    e: ((usize, Range<usize>), (Range<usize>, usize)),
    f: ((usize, Range<usize>), (usize, Range<usize>)),
    g: ((Range<usize>, usize), (Range<usize>, usize)),

}
impl Cube {
    fn get_next_pos(&self, (x,y): (usize, usize), direction: Direction) -> (usize, usize, Direction) {
        return match direction {
            Direction::Left => {
                match (x,y) {
                    (x,y) if self.a.0.0 == x && self.a.0.1.contains(&y) => (self.a.1.0.start+y-self.a.0.1.start,self.a.1.1, Direction::Down),
                    (x,y) if self.c.0.0 == x && self.c.0.1.contains(&y) => (self.c.1.0.start+y-self.c.0.1.start,self.c.1.1, Direction::Down),
                    (x,y) if self.f.0.0 == x && self.f.0.1.contains(&y) => (self.f.1.0, self.f.1.1.end-1-(y-self.f.0.1.start), Direction::Right),
                    (x,y) if self.f.1.0 == x && self.f.1.1.contains(&y) => (self.f.0.0, self.f.0.1.end-1-(y-self.f.1.1.start), Direction::Right),
                    _ => (x-1, y, Direction::Left)
                }
            },
            Direction::Right => {
                match (x,y) {
                    (x,y) if self.d.0.0 == x+1 && self.d.0.1.contains(&y) => (self.d.1.0-1, self.d.1.1.end-1-(y-self.d.0.1.start), Direction::Left),
                    (x,y) if self.e.0.0 == x+1 && self.e.0.1.contains(&y) => (self.e.1.0.start+y-self.e.0.1.start,self.e.1.1, Direction::Up),
                    (x,y) if self.d.1.0 == x+1 && self.d.1.1.contains(&y) => (self.d.0.0-1, self.d.0.1.end-1-(y-self.d.1.1.start), Direction::Left),
                    (x,y) if self.b.0.0 == x+1 && self.b.0.1.contains(&y) => (self.b.1.0.start+y-self.b.0.1.start,self.b.1.1, Direction::Up),
                    _ => (x+1, y, Direction::Right)
                }
            },
            Direction::Down => {
                match (x,y) {
                    (x,y) if self.e.1.1 == y && self.e.1.0.contains(&x) => (self.e.0.0-1, self.e.0.1.start+x-self.e.1.0.start, Direction::Left),
                    (x,y) if self.b.1.1 == y && self.b.1.0.contains(&x) => (self.b.0.0-1, self.b.0.1.start+x-self.b.1.0.start, Direction::Left),
                    (x,y) if self.g.1.1 == y && self.g.1.0.contains(&x) => (self.g.0.0.start+x-self.g.1.0.start, self.g.0.1, Direction::Down),
                    _ => (x, y+1, Direction::Down)
                }
            },
            Direction::Up => {
                match (x,y) {
                    (x,y) if self.g.0.1 == y && self.g.0.0.contains(&x) => (self.g.1.0.start+x-self.g.0.0.start, self.g.1.1, Direction::Up),
                    (x,y) if self.c.1.1 == y && self.c.1.0.contains(&x) => (self.c.0.0, self.c.0.1.start+x-self.c.1.0.start, Direction::Right),
                    (x,y) if self.a.1.1 == y && self.a.1.0.contains(&x) => (self.a.0.0, self.a.0.1.start+x-self.a.1.0.start, Direction::Right),
                    _ => (x, y-1, Direction::Up)
                }
            }
        }
    }
}

#[derive(Eq, PartialEq, Copy, Clone, Debug)]
enum Tile {
    Corridor,
    Wall,
    Empty
}


fn main() {
    let contents = read_to_string("../../../../inputs/advent2022/day22")
        .expect("Should have been able to read the file");

    let (map_lines, instructions) = contents.split("\r\n\r\n").collect_tuple::<(&str, &str)>().unwrap();
    let movements = instructions.split(&['R', 'L'][..]).map(|number| number.parse::<usize>().unwrap()).collect::<Vec<_>>();
    let rotations = instructions.split(|x: char| x.is_numeric()).filter(|v| !v.is_empty()).collect::<Vec<_>>();

    {
        let mut map = Map {
            rows: map_lines.split("\r\n").map(to_row).collect::<Vec<_>>(),
            rows_start: vec![],
            rows_end: vec![],
            columns_start: vec![],
            columns_end: vec![],
        };
        map.rows_start = map.rows.iter().enumerate().map(|(i, _row)| map.find_start_of_row(i)).collect::<Vec<_>>();
        map.rows_end = map.rows.iter().enumerate().map(|(i, _row)| map.find_end_of_row(i)).collect::<Vec<_>>();
        let max_column = map.rows.iter().map(|row| row.len()).max().unwrap();
        for i in 0..max_column {
            map.columns_start.push(map.find_start_of_column(i));
            map.columns_end.push(map.find_end_of_column(i));
        }

        let mut player = Player {
            position: (map.find_start_pos(), 0),
            facing: Direction::Right,
        };

        for i in 0..rotations.len() {
            for _ in 0..movements[i] {
                if player.can_move(&map) {
                    player.move_forward(&map);
                }
            }
            player.turn(Rotation::new(rotations[i]));
        }
        for _ in 0..movements[rotations.len()] {
            if player.can_move(&map) {
                player.move_forward(&map);
            }
        }

        println!("Part 1 {:?}", player.get_score());
    }

    let cube = Cube {
        rows: map_lines.split("\r\n").map(to_row).collect::<Vec<_>>(),
        a: ((50, Range { start:50, end: 100}), (Range { start:0, end: 50}, 100)),
        b: ((50, Range { start:150, end: 200}), (Range { start:50, end: 100}, 149)),
        c: ((0, Range { start:150, end: 200}), (Range { start:50, end: 100}, 0)),
        d: ((150, Range { start:0, end: 50}), (100, Range { start:100, end: 150})),
        e: ((100, Range { start:50, end: 100}), (Range { start:100, end: 150}, 49)),
        f: ((50, Range { start:0, end: 50}), (0, Range { start:100, end: 150})),
        g: ((Range { start:100, end: 150}, 0), (Range { start:0, end: 50}, 199)),

    };

    let mut player = Player {
        position: (50, 0),
        facing: Direction::Right,
    };
    for i in 0..rotations.len(){
        for _ in 0..movements[i]{
            if player.can_move_cube(&cube) {
                player.move_forward_cube(&cube);

            }
        }
        player.turn(Rotation::new(rotations[i]));
    }
    for _ in 0..movements[rotations.len()]{
        if player.can_move_cube(&cube) {
            player.move_forward_cube(&cube);
        }
    }

    println!("Part 2 {:?}", player.get_score());
}

fn to_row(line: &str) -> Row {
    line.chars().map(|char| {
        match char {
            '.' => Tile::Corridor,
            '#' => Tile::Wall,
            _ => Tile::Empty
        }
    }).collect()
}

