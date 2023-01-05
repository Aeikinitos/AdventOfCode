use std::collections::VecDeque;
use std::fs::read_to_string;
use itertools::Itertools;

const ROUNDS: i32 = 10000;

enum Operation {
    ADD(i64),
    MULTIPLY(i64),
    POW2
}
impl Operation {
    fn operate(&self, old_value: &i64, greatest_common_factor: i64) -> i64 {
        match self {
            Operation::ADD(value) => { (old_value + value) % greatest_common_factor}
            Operation::MULTIPLY(value) => { (old_value * value) % greatest_common_factor}
            Operation::POW2 => {old_value.pow(2) % greatest_common_factor}
        }
    }
}

struct Monkey {
    items: VecDeque<i64>,
    operation: Operation,
    test_divider: i64,
    test_true_target: usize,
    test_false_target: usize
}
impl Monkey {
    fn decide_monkey_target(&self, value: &i64) -> usize {

        if  value % &self.test_divider == 0 {
            self.test_true_target
        } else {
            self.test_false_target
        }
    }
}

fn main() {
    let contents = read_to_string("inputs/day11")
        .expect("Should have been able to read the file");

    let mut monkeys = parse_data(contents);
    let mut monkey_inspections = vec![0;monkeys.len()];

    let greatest_common_factor = monkeys.iter().fold(1, |product, monkey| product * monkey.test_divider);

    for _round in 0..ROUNDS {
        for i in 0..monkeys.len(){
            while let Some(item) = monkeys[i].items.pop_front() {
                monkey_inspections[i] += 1;
                // operate and relief

                // let new_value = ((monkeys[i].operation.operate(&item) / 3 ) as f32).floor();
                let new_value = monkeys[i].operation.operate(&item, greatest_common_factor);
                let target_monkey = monkeys[i].decide_monkey_target(&new_value);
                // test and throw
                monkeys[target_monkey].items.push_back(new_value);

                // println!("{round}: Monkey:{i} inspects {item} and decides to throw {new_value} at {} ", target_monkey);
            }
        }

    }

    println!("Part 1&2 {}", monkey_inspections.iter().sorted().rev().take(2).product::<i64>());

}

fn parse_data(input: String) -> Vec<Monkey> {
    let mut monkeys = vec![];
    let chunks = input
        .lines()
        .chunks(7);
    for monkey_raw in chunks.into_iter().map(|chunk| chunk.collect::<Vec<_>>()) {
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

fn parse_monkey_items(line: &str) -> VecDeque<i64>{
    line
        .split(&[' ',','][..])
        .filter(is_numeric)
        .map(|item| item.parse::<i64>().expect("starting item should have been a number"))
        .collect::<VecDeque<_>>()
}

fn is_numeric(input: &&str) -> bool {
    input.parse::<i64>().is_ok()
}

fn parse_operation(line: &str) -> Operation {
    let splits = line.split(' ').collect::<Vec<_>>();
    let value = match splits[7] {
        "old" => {return Operation::POW2},
        value => {value.parse().expect("operation right hand should have been a number")},
    };

    match splits[6] {
        "*" => {Operation::MULTIPLY(value)},
        "+" => {Operation::ADD(value)},
        _ => panic!("invalid operation")
    }
}

fn parse_test(line: &str) -> i64 {
    line.split(' ').collect::<Vec<_>>()[5].parse::<i64>().expect("test value should have been a number")
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