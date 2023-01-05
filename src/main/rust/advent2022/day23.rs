use std::collections::{HashMap, HashSet};
use std::fmt::{Display, Formatter};
use std::fs::read_to_string;
use std::hash::{Hash, Hasher};
use std::time::Instant;
use num_traits::abs;

#[derive(Debug, Clone, Eq, PartialEq)]
enum Direction {
    N,
    S,
    W,
    E
}
impl Direction {
    fn get_order() -> Vec<Direction> {
        vec![Direction::N, Direction::S, Direction::W, Direction::E]
    }
    fn get_deltas(&self) -> Vec<(i32, i32)> {
        match self {
            Direction::E => {vec![(-1,1), (0,1), (1,1)]}
            Direction::W => {vec![(-1,-1), (0,-1), (1,-1)]}
            Direction::N => {vec![(-1,-1), (-1,0), (-1,1)]}
            Direction::S => {vec![(1,-1), (1,0), (1,1)]}
        }
    }
    fn get_delta(&self) -> (i32, i32) {
        match self {
            Direction::E => {(0,1)}
            Direction::W => {(0,-1)}
            Direction::N => {(-1,0)}
            Direction::S => {(1,0)}
        }
    }
    fn get_all_deltas() -> Vec<(i32,i32)> {
        vec![(-1,-1), (-1,0),  (-1,1),
             ( 0,-1),          ( 0,1),
             ( 1,-1), ( 1,0),  ( 1,1),
        ]
    }
    fn get_next(&self) -> Direction {
        match self {
            Direction::N => {Direction::S}
            Direction::S => {Direction::W}
            Direction::W => {Direction::E}
            Direction::E => {Direction::N}
        }
    }
}

#[derive(Debug)]
struct Orchestrator {
    elves: HashSet<Elf>,
    direction: Direction
}
impl Orchestrator {
    fn calculate_proposal(&mut self) -> HashSet<Elf> {
        let mut result = HashMap::new();
        for elf in &self.elves {
            let new_elf = self.propose_position_for(elf);
            result.entry(new_elf.position)
                .and_modify(|entry: &mut Vec<Elf>| entry.push(new_elf))
                .or_insert(vec![new_elf]);
        }
        self.direction = self.direction.get_next();
        result.iter()
            .filter(|(_,elves)| elves.len() <= 1)
            .map(|(_, elves)| elves[0])
            .collect::<HashSet<_>>()
    }

    fn propose_position_for(&self, elf: &Elf) -> Elf {
        if self.is_alone(elf) {
            return *elf;
        }
        return match self.get_direction_order().iter().find(|direction| {self.is_empty_direction(*elf, direction)}) {
            None => {*elf}
            Some(direction) => {elf.move_elf(direction.get_delta())}
        }
    }

    fn is_empty_position(&self, point: (i32, i32)) -> bool {
        !self.elves.iter().any(|elf| elf.position == point)
    }

    fn is_empty_direction(&self, elf: Elf, direction: &&Direction) -> bool {
        self.is_empty_position(elf.at(direction.get_deltas()[0]))
            && self.is_empty_position(elf.at(direction.get_deltas()[1]))
            && self.is_empty_position(elf.at(direction.get_deltas()[2]))
    }

    fn is_alone(&self, elf: &Elf) -> bool {
        Direction::get_all_deltas().iter().all(|direction| self.is_empty_position(elf.at(*direction)))

    }

    fn get_direction_order(&self) -> Vec<Direction> {
        Direction::get_order().into_iter()
            .cycle()
            .skip_while(|dir| *dir != self.direction)
            .take(4)
            .collect::<Vec<_>>()
    }
}

impl Display for Orchestrator {
    fn fmt(&self, f: &mut Formatter<'_>) -> std::fmt::Result {
        for i in 0..15 {
            for j in 0..15 {
                if self.elves.iter().any(|elf| elf.position == (i-5,j-5)) {
                    write!(f, "#").expect("cannot write this");
                } else {
                    write!(f, ".").expect("cannot write this");
                }
            }
            writeln!(f).expect("cannot write this");
        }
        Ok(())
    }
}

#[derive(Copy, Clone, Debug)]
struct Elf {
    id: usize,
    position: (i32, i32),
}

impl PartialEq<Self> for Elf {
    fn eq(&self, other: &Self) -> bool {
        self.id == other.id
    }
}

impl Hash for Elf {
    fn hash<H: Hasher>(&self, state: &mut H) {
        self.id.hash(state);
    }
}

impl Eq for Elf {}

impl Elf {
    fn new(id: usize, (x,y): (i32, i32)) -> Self {
        Self {id,position: (x, y)}
    }
    fn at(&self, direction: (i32, i32)) -> (i32, i32) {
        (self.position.0 + direction.0, self.position.1 + direction.1)
    }
    fn move_elf(&self, direction: (i32, i32)) -> Elf {
        Elf::new(self.id, self.at(direction))
    }
    fn same_position(&self, other: &Elf) -> bool{
        self.position == other.position
    }
}

fn main() {
    let start = Instant::now();
    let contents = read_to_string("inputs/day23")
        .expect("Should have been able to read the file");

    let mut orchestrator = Orchestrator {
        elves: get_elves(contents),
        direction: Direction::N,
    };

    for _ in 0..10 {
        let new_elves = orchestrator.calculate_proposal();
        // remove moved elves and add their new location
        orchestrator.elves.retain(|elf| !new_elves.contains(elf));
        orchestrator.elves.extend(new_elves);

    }

    println!("Part 1 {}", calculate_empty_ground(&mut orchestrator));

    println!("Time elapsed in part 1 is: {:?}", start.elapsed());

    let mut round = 10;
    loop {
        round += 1;
        println!("round : {}", round);
        let new_elves = orchestrator.calculate_proposal();
        if orchestrator.elves.len() == new_elves.len()
            && orchestrator.elves.len() == orchestrator.elves.iter().filter(|elf| elf.same_position(new_elves.get(elf).unwrap())).count(){
            break;
        }
        orchestrator.elves.retain(|elf| !new_elves.contains(elf));
        orchestrator.elves.extend(new_elves);
    }

    println!("Part 2 {}", round);

}

fn calculate_empty_ground(orchestrator: &mut Orchestrator) -> i32 {
    let max_x = orchestrator.elves.iter().map(|elf| elf.position.0).max().unwrap();
    let min_x = orchestrator.elves.iter().map(|elf| elf.position.0).min().unwrap();
    let max_y = orchestrator.elves.iter().map(|elf| elf.position.1).max().unwrap();
    let min_y = orchestrator.elves.iter().map(|elf| elf.position.1).min().unwrap();
    abs(max_x+1 - min_x) * abs(max_y+1 - min_y) - orchestrator.elves.len() as i32
}

fn get_elves(contents: String) -> HashSet<Elf> {
    contents.lines()
        .enumerate()
        .flat_map(|(i, line)|
            line.chars()
                .enumerate()
                .filter(|(_i, position)| position == &'#')
                .map(|(j, _elf)| (i*100 + j, (i, j)))
                .collect::<Vec<_>>())
        .map(|(id, position)| Elf::new(id, (position.0 as i32, position.1 as i32)))
        .collect::<HashSet<_>>()
}
