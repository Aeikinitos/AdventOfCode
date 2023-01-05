extern crate core;

use std::cmp::{Ordering};
use std::fmt;
use std::fs::read_to_string;
use itertools::Itertools;
use pest::iterators::Pair;

use pest::Parser;
use pest_derive::Parser;

#[derive(Parser)]
#[grammar = "src/main/rust/advent2022/day13.pest"]
pub struct Day13Parser;


#[derive(Debug, PartialEq, Eq)]
enum PacketData {
    LIST(Vec<PacketData>),
    INT(i32)
}

impl fmt::Display for PacketData {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        return match self {
            PacketData::LIST(list) => {
                if list.is_empty() { return Ok(()); }
                write!(f, "[{}", list.iter().next().unwrap()).expect("cannot write this");
                list.iter().skip(1).for_each(|packet| { write!(f, ",{}", *packet).expect("cannot write this"); });
                write!(f, "]").expect("how can you not write ]?");
                Ok(())
            }
            PacketData::INT(int) => write!(f, "{}", int)
        }
    }
}

impl PartialOrd for PacketData {
    fn partial_cmp(&self, other: &Self) -> Option<Ordering> {
        Some(self.cmp(other))
    }
}

impl Ord for PacketData {
    fn cmp(&self, other: &Self) -> Ordering {
        match compare(self, other) {
            ORDER::BROKEN => {Ordering::Greater}
            ORDER::CORRECT => {Ordering::Less}
            ORDER::CONTINUE => {Ordering::Equal}
        }

    }
}

#[derive(Eq, PartialEq)]
enum ORDER {
    BROKEN,
    CORRECT,
    CONTINUE
}

fn main() {
    let contents = read_to_string("inputs/day13")
        .expect("Should have been able to read the file");

    let file = Day13Parser::parse(Rule::file, &contents)
        .expect("unsuccessful parse") // unwrap the parse result
        .next().unwrap(); // get and unwrap the `file` rule; never fails

    let mut packets = vec![];
    let mut iter = file.into_inner();
    while let Some(line) = iter.next() {
        packets.push(parse_value(line));
    }

    let count = packets.iter()
        .chunks(2)
        .into_iter()
        .collect::<Vec<_>>()
        .into_iter()
        .map(|mut pair| compare(&pair.next().unwrap(), &pair.next().unwrap()))
        .enumerate()
        .filter(|(_i, result)| *result == ORDER::CORRECT)
        .fold(0, |acc, (index, _)| acc + index+1);

    println!("Part 1 {count}");

    packets.push(PacketData::LIST(vec![PacketData::INT(2)]));
    packets.push(PacketData::LIST(vec![PacketData::INT(6)]));
        packets.sort();
    // packets.iter().for_each(|packet| println!("{packet}"));
    let part2 = packets.iter()
        .enumerate()
        .filter(|(_i, packet)| **packet == PacketData::LIST(vec![PacketData::INT(2)]) || **packet == PacketData::LIST(vec![PacketData::INT(6)]))
        .map(|(i,_)| i+1)
        .product::<usize>();
    println!("Part 2 {:?}", part2);

}

fn parse_value(pairs: Pair<Rule>) -> PacketData {
    match pairs.as_rule() {
        Rule::packet => {
            let list_rules = pairs.into_inner().next().unwrap();
            match list_rules.as_rule() {
                Rule::list => PacketData::LIST(list_rules.into_inner().map(parse_value).collect::<Vec<PacketData>>()),
                _ => unreachable!(),
            }
        }
        Rule::value => parse_value(pairs.into_inner().next().unwrap()),
        Rule::list => PacketData::LIST(pairs.into_inner().map(parse_value).collect::<Vec<_>>()),
        Rule::emptylist => PacketData::LIST(vec![]),
        Rule::number => PacketData::INT(pairs.as_str().parse().unwrap()),
        _ => unreachable!()
    }
}

fn compare(left_packet: &PacketData, right_packet: &PacketData) -> ORDER {
    match (left_packet, right_packet)  {
        (PacketData::LIST(left_list), PacketData::LIST(right_list)) => {
            match left_list.cmp(right_list) {
                Ordering::Less => ORDER::CORRECT,
                Ordering::Equal => ORDER::CONTINUE,
                Ordering::Greater => ORDER::BROKEN
            }
        }
        (PacketData::INT(left_int), PacketData::INT(right_int)) => {
            match left_int.cmp(right_int) {
                Ordering::Less => ORDER::CORRECT,
                Ordering::Equal => ORDER::CONTINUE,
                Ordering::Greater => ORDER::BROKEN
            }
        }
        (PacketData::INT(left_int), list) => {
            compare(&PacketData::LIST(vec![PacketData::INT(*left_int)]), list)
        }
        (list, PacketData::INT(right_int)) => {
            compare(list, &PacketData::LIST(vec![PacketData::INT(*right_int)]))
        }
    }
}