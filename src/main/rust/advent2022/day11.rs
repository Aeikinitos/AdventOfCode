use std::collections::VecDeque;
use std::fs::read_to_string;
use itertools::Itertools;
use num_bigint::{BigInt, ToBigInt};
use num_traits::{Zero, One};


const ROUNDS: i32 = 10000;

// #[derive(Copy, Clone)]
enum Operation {
    ADD(BigInt),
    MULTIPLY(BigInt),
    POW_2
}
impl Operation {
    fn operate(&self, old_value: &BigInt) -> BigInt {
        match self {
            Operation::ADD(value) => {old_value + value}
            Operation::MULTIPLY(value) => {old_value * value}
            Operation::POW_2 => {old_value.pow(2)}
        }
    }
}

struct Monkey {
    items: VecDeque<BigInt>,
    operation: Operation,
    test_divider: BigInt,
    test_true_target: usize,
    test_false_target: usize
}
impl Monkey {
    fn decide_monkey_target(&self, value: &BigInt) -> usize {

        if  value % &self.test_divider == Zero::zero() {
            self.test_true_target
        } else {
            self.test_false_target
        }
    }
}

macro_rules! impl_to_bigint {
    ($T:ty, $from_ty:path) => {
        impl ToBigInt for $T {
            #[inline]
            fn to_bigint(&self) -> Option<BigInt> {
                $from_ty(*self)
            }
        }
    };
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

                // println!("{round}: Monkey:{i} inspects {item} and decides to throw {new_value} at {} ", target_monkey);
            }
        }
        if round % 100 == 0 {
            println!("{round}: {:?}", monkey_inspections.iter().collect::<Vec<_>>());
        }

    }

    for i in 0..monkeys.len() {
        println!("{} - {:?}", monkey_inspections[i], monkeys[i].items);
    };

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

fn parse_monkey_items(line: &str) -> VecDeque<BigInt>{
    line
        .split(&[' ',','][..])
        .filter(is_numeric)
        .map(|item| item.parse::<BigInt>().expect("starting item should have been a number"))
        .collect::<VecDeque<_>>()
}

fn is_numeric(input: &&str) -> bool {
    input.parse::<BigInt>().is_ok()
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

fn parse_test(line: &str) -> BigInt {
    line.split(' ').collect::<Vec<_>>()[5].parse::<BigInt>().expect("test value should have been a number")
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