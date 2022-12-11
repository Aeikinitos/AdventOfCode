use std::collections::VecDeque;
use std::fs::read_to_string;
use itertools::Itertools;
use num_bigint::{BigInt, ToBigInt};

const ROUNDS: i32 = 10000;

enum Operation {
    ADD(i32),
    MULTIPLY(i32),
    POW_2
}
impl Operation {
    fn operate(&self, old_value: &Divisible) -> Divisible {
        match self {
            Operation::ADD(value) => {
                Divisible {
                    by_23: (old_value.by_23 + value) % 23,
                    by_7: (old_value.by_7 + value) % 7,
                    by_19: (old_value.by_19 + value) % 19,
                    by_13: (old_value.by_13 + value) % 13,
                    by_3: (old_value.by_3 + value) % 3,
                    by_2: (old_value.by_2 + value) % 2,
                    by_11: (old_value.by_11 + value) % 11,
                    by_17: (old_value.by_17 + value) % 17,
                    by_5: (old_value.by_5 + value) % 5
                }
            }
            Operation::MULTIPLY(value) => {
                Divisible {
                    by_23: (old_value.by_23 * value) % 23,
                    by_7: (old_value.by_7 * value) % 7,
                    by_19: (old_value.by_19 * value) % 19,
                    by_13: (old_value.by_13 * value) % 13,
                    by_3: (old_value.by_3 * value) % 3,
                    by_2: (old_value.by_2 * value) % 2,
                    by_11: (old_value.by_11 * value) % 11,
                    by_17: (old_value.by_17 * value) % 17,
                    by_5: (old_value.by_5 * value) % 5
                }
            }
            Operation::POW_2 => {
                Divisible {
                    by_23: (old_value.by_23.pow(2)) % 23,
                    by_7: (old_value.by_7.pow(2)) % 7,
                    by_19: (old_value.by_19.pow(2)) % 19,
                    by_13: (old_value.by_13.pow(2)) % 13,
                    by_3: (old_value.by_3.pow(2)) % 3,
                    by_2: (old_value.by_2.pow(2)) % 2,
                    by_11: (old_value.by_11.pow(2)) % 11,
                    by_17: (old_value.by_17.pow(2)) % 17,
                    by_5: (old_value.by_5.pow(2)) % 5
                }
            }
        }
    }
}

struct Monkey {
    items: VecDeque<Divisible>,
    operation: Operation,
    test_divider: i32,
    test_true_target: usize,
    test_false_target: usize
}
impl Monkey {
    fn decide_monkey_target(&self, value: &Divisible) -> usize {
        if  value.test(self.test_divider) {
            self.test_true_target
        } else {
            self.test_false_target
        }
    }
}

#[derive(Debug, Clone)]
struct Divisible {
    by_23: i32,
    by_7: i32,
    by_19: i32,
    by_13: i32,
    by_3: i32,
    by_2: i32,
    by_11: i32,
    by_17: i32,
    by_5: i32
}
impl Divisible {
    fn from_num(num: i32) -> Divisible {
        Divisible {
            by_23: num % 23,
            by_7: num % 7,
            by_19: num % 19,
            by_13: num % 13,
            by_3: num % 3,
            by_2: num % 2,
            by_11: num % 11,
            by_17: num % 17,
            by_5: num % 5,
        }
    }

    fn test(&self, test: i32) -> bool {
        match test {
            23 => self.by_23 == 0,
            7 => self.by_7 == 0,
            19 => self.by_19 == 0,
            13 => self.by_13 == 0,
            3 => self.by_3 == 0,
            2 => self.by_2 == 0,
            11 => self.by_11 == 0,
            17 => self.by_17 == 0,
            5 => self.by_5 == 0,
            _ => panic!("klatsa")
        }
    }
}

fn main() {
    let contents = read_to_string("inputs/advent2022/day11")
        .expect("Should have been able to read the file");

    let mut monkeys = parse_data(contents);
    let mut monkey_inspections = vec![0;monkeys.len()];

    for round in 0..ROUNDS {
        for i in 0..monkeys.len(){
            while let Some(item) = monkeys[i].items.pop_front() {
                monkey_inspections[i] += 1;
                // operate and relief
                // let new_value = ((monkeys[i].operation.operate(&item) / 3 ) as f32).floor();
                let new_value = monkeys[i].operation.operate(&item);
                let target_monkey = monkeys[i].decide_monkey_target(&new_value);
                // test and throw
                monkeys[target_monkey].items.push_back(new_value);


            }
        }

    }


    println!("Part1 {}", monkey_inspections.iter().sorted().rev().take(2).product::<BigInt>());

}

fn parse_data(input: String) -> Vec<Monkey> {
    let mut monkeys = vec![];
    let chunks = input
        .lines()
        .chunks(7);
    for (i, monkey_raw) in chunks.into_iter().map(|chunk| chunk.collect::<Vec<_>>()).enumerate() {
        monkeys.push(Monkey {
            items: parse_monkey_items(monkey_raw[1]),
            operation: parse_operation(monkey_raw[2]),
            test_divider: parse_test(monkey_raw[3]),
            test_true_target: parse_test_true(monkey_raw[4]),
            test_false_target: parse_test_false(monkey_raw[5]),
        });
    }

    monkeys
}

fn parse_monkey_items(line: &str) -> VecDeque<Divisible>{
    line
        .split(&[' ',','][..])
        .filter(is_numeric)
        .map(|item| Divisible::from_num(item.parse::<i32>().expect("starting item should have been a number")))
        .collect::<VecDeque<_>>()
}

fn is_numeric(input: &&str) -> bool {
    input.parse::<i32>().is_ok()
}

fn parse_operation(line: &str) -> Operation {
    let splits = line.split(' ').collect::<Vec<_>>();
    let value = match splits[7] {
        "old" => {return Operation::POW_2},
        value => {value.parse().expect("operation right hand should have been a number")},
    };

    match splits[6] {
        "*" => {Operation::MULTIPLY(value)},
        "+" => {Operation::ADD(value)},
        _ => panic!("invalid operation")
    }
}

fn parse_test(line: &str) -> i32 {
    line.split(' ').collect::<Vec<_>>()[5].parse::<i32>().expect("test value should have been a number")
}

fn parse_test_true(line: &str) -> usize {
    line.chars().nth(29)
        .map(|test_value| test_value.to_digit(10).expect("test value should have been a digit"))
        .expect("test value should have been a number") as usize

}

fn parse_test_false(line: &str) -> usize {
    line.chars().nth(30)
        .map(|test_value| test_value.to_digit(10).expect("test value should have been a digit"))
        .expect("test value should have been a number") as usize
}