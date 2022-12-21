use std::fs::read_to_string;
use derive_new::new;
use regex::Regex;

const PLAYER: &str = "humn";

#[derive(new, Debug)]
enum Monkey {
    Number(String, i64),
    Math(String, Box<Monkey>, Operation, Box<Monkey>),
}

impl Monkey {
    fn calculate(&self) -> i64 {
        return match self {
            Monkey::Number(_, num) => *num,
            Monkey::Math(_, left, operation, right) => {
                match operation {
                    Operation::Add => left.calculate() + right.calculate(),
                    Operation::Multi => left.calculate() * right.calculate(),
                    Operation::Sub => left.calculate() - right.calculate(),
                    Operation::Div => left.calculate() / right.calculate()
                }
            }
        }
    }
    fn has_monkey(&self, target: &str) -> bool {
        return match self {
            Monkey::Number(name, _) => name == target,
            Monkey::Math(_, left, _, right) => {
                left.has_monkey(target) || right.has_monkey(target)
            }
        }
    }
    fn calculate_backwards(&self, amount: i64) -> i64 {
        return match self {
            Monkey::Number(_, _) => {
                amount
            },
            Monkey::Math(_, left, operation, right) => {
                if left.has_monkey(PLAYER) {
                    match operation {
                        Operation::Add => left.calculate_backwards(amount - right.calculate()),
                        Operation::Multi => left.calculate_backwards(amount / right.calculate()),
                        Operation::Sub => left.calculate_backwards(amount + right.calculate()),
                        Operation::Div => left.calculate_backwards(amount * right.calculate())
                    }
                } else {
                    match operation {
                        Operation::Add => right.calculate_backwards(amount - left.calculate()),
                        Operation::Multi => right.calculate_backwards(amount / left.calculate()),
                        Operation::Sub => right.calculate_backwards(left.calculate() - amount),
                        Operation::Div => right.calculate_backwards(left.calculate() / amount)
                    }
                }
            }
        }
    }
}

#[derive(new, Debug)]
enum Operation {
    Add,
    Multi,
    Sub,
    Div
}

fn main() {
    let contents = read_to_string("inputs/advent2022/day21")
        .expect("Should have been able to read the file");


    let root: Monkey = find_monkey(&contents, "root");
    println!("Part 1 {}", root.calculate());

    let part2 = match root {
        Monkey::Number(_, _) => {panic!("not a valid root")}
        Monkey::Math(_,left, _, right) => {

            if left.has_monkey(PLAYER) {
                left.calculate_backwards(right.calculate())
            } else {
                right.calculate_backwards(left.calculate())
            }
        }
    };
    println!("Part 2 {}", part2);
}

fn find_monkey(contents: &String, name: &str) -> Monkey {
    contents.lines()
        .find(|line| line.starts_with(name))
        .map(|line| from_line(line, contents))
        .expect("Monkey was not found").1
}

fn from_line(line: &str, contents: &String) -> (String, Monkey) {

    let input_pattern = Regex::new("((?P<name_math>[a-z]{4}): (?P<left>[a-z]{4}) (?P<operation>[\\+\\-\\*/]) (?P<right>[a-z]{4}))|((?P<name_number>[a-z]{4}): (?P<number>\\d+))").unwrap();
    let matcher = input_pattern.captures(line).unwrap();
    return match matcher.name("name_math") {
        None => {
            let name = matcher.name("name_number").map_or("", |m| m.as_str());
            let number = matcher.name("number").map_or(0, |m| m.as_str().parse::<i64>().unwrap());
            (name.to_string(), Monkey::new_number(name.to_string(), number))
        }
        Some(name) => {
            let left = matcher.name("left").map_or("", |m| m.as_str());
            let operation = match matcher.name("operation").map_or("", |m| m.as_str()) {
                "+" => Operation::Add,
                "-" => Operation::Sub,
                "/" => Operation::Div,
                "*" => Operation::Multi,
                _ => panic!("expected an operand")
            };
            let right = matcher.name("right").map_or("", |m| m.as_str());

            (name.as_str().to_string(),
             Monkey::new_math(
                 name.as_str().to_string(),
                 Box::new(find_monkey(contents, left)),
                 operation,
                 Box::new(find_monkey(contents, right))))
        }
    }
}