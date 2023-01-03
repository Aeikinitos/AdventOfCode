use std::collections::{HashMap, HashSet};
use std::fs::read_to_string;
use num_traits::abs;
use ranges::Ranges;
use regex::Regex;

const ROW:i32 = 2000000;
const MAX_RANGE:i32 = 4000000;

#[derive(Debug)]
struct Sensor {
    location: (i32, i32),
    beacon: (i32, i32),
    range: i32
}

impl Sensor {
    fn covered_range(&self, row: i32) -> Ranges<i32> {
        let count = self.range - self.vertical_distance_from(row) +1;
        Ranges::from(self.location.0-count+1..self.location.0+count-1)
    }

    fn covered_area(&self, row: i32) -> Vec<i32> {
        let count = self.range - self.vertical_distance_from(row) +1;
        let mut covered_area = vec![];
        for i in -count+1..count{
            covered_area.push(self.location.0 + i);
        }
        covered_area
    }

    fn vertical_distance_from(&self, row: i32) -> i32 {
        abs(self.location.1 - row)
    }

    fn within_range(&self, point: (i32,i32)) -> bool {
        manhattan((self.location.0, self.location.1), point) <= self.range
    }
}

struct Grid {
    sensors: Vec<Sensor>,
    rows: HashMap<i32, Ranges<i32>>
}
impl Grid {
    fn calculate_covered_ranges(&mut self) {
        for row in 0..MAX_RANGE {
            let range = self.sensors.iter()
                .filter(|sensor| sensor.vertical_distance_from(row) <= sensor.range)
                .map(|sensor| sensor.covered_range(row))
                .reduce(|accum, range| accum.union(range))
                .expect("There should be at least one range");
            self.rows.insert(row, range);
        }
    }

    fn calculate_covered_area_for(&self, row: i32) -> HashSet<i32> {
        let mut covered_area = self.sensors.iter()
            .filter(|sensor| sensor.vertical_distance_from(row) <= sensor.range)
            .flat_map(|sensor| sensor.covered_area(row))
            .collect::<HashSet<_>>();

        let beacons_x = self.sensors.iter()
            .filter(|sensor| sensor.beacon.1 == row)
            .map(|sensor| sensor.beacon.0)
            .collect::<Vec<_>>();

        covered_area.retain(|element| !beacons_x.contains(element));

        covered_area
    }

    fn is_covered(&self, x:i32, y:i32) -> bool {
        for sensor in &self.sensors {
            if sensor.within_range((x,y)) {
                return true;
            }
        }
        false
    }
}

fn main() {
    let contents = read_to_string("../../../../inputs/advent2022/day15")
        .expect("Should have been able to read the file");

    let mut grid = Grid {
        sensors: contents.lines().map(to_sensor).collect::<Vec<_>>(),
        rows: HashMap::new()
    };

    println!("Part 1 {}", grid.calculate_covered_area_for(ROW).len() as i32);
    grid.calculate_covered_ranges();

    let mut row = 0;
    for i in 0..MAX_RANGE {
        if !(Ranges::from(0..MAX_RANGE) - grid.rows.get(&i).unwrap().clone()).is_empty() {
            row = i;
            break;
        }
    }
    for column in 0..=MAX_RANGE {
        if !grid.is_covered(column, row) {
            println!("Part 2 {:?}", column as i64 * 4000000 as i64 + row as i64);
            return;
        }
    }
}

fn to_sensor(line: &str) -> Sensor {
    let input_pattern = Regex::new("Sensor at x=(?P<sensor_x>-?\\d+), y=(?P<sensor_y>-?\\d+): closest beacon is at x=(?P<beacon_x>-?\\d+), y=(?P<beacon_y>-?\\d+)").unwrap();
    let matcher = input_pattern.captures(line).unwrap();
    let sensor_x = matcher.name("sensor_x").map_or(0, |m| m.as_str().parse::<i32>().unwrap());
    let sensor_y = matcher.name("sensor_y").map_or(0, |m| m.as_str().parse::<i32>().unwrap());
    let beacon_x = matcher.name("beacon_x").map_or(0, |m| m.as_str().parse::<i32>().unwrap());
    let beacon_y = matcher.name("beacon_y").map_or(0, |m| m.as_str().parse::<i32>().unwrap());
    Sensor {
        location: (sensor_x, sensor_y),
        beacon: (beacon_x, beacon_y),
        range: manhattan((sensor_x, sensor_y), (beacon_x, beacon_y))
    }
}

fn manhattan((x,y): (i32,i32), (j,k): (i32,i32)) -> i32 {
    abs(x-j) + abs(y-k)
}
