use std::fs::read_to_string;
use std::time::Instant;
use regex::Regex;
use derive_new::new;
use nom::bytes::complete::tag;
use nom::character::complete::line_ending;
use nom::IResult;
use nom::multi::separated_list1;
use nom::character::complete::i32;
use nom::sequence::{delimited, tuple};

#[derive(Clone, Eq, PartialEq, Hash)]
struct Factory {
    robots: Robots,
    resources: Resources,
    blueprint: Blueprint,
    cycle: i32
}

#[derive(Copy, Clone, Eq, PartialEq, Hash)]
struct Robots {
    ore: i32,
    clay: i32,
    obsidian: i32,
    geode: i32
}

#[derive(Debug, Copy, Clone, Eq, PartialEq, Hash)]
struct Resources {
    ore: i32,
    clay: i32,
    obsidian: i32,
    geode: i32
}

#[derive(Debug, Copy, Clone, Hash, PartialEq, Eq)]
enum Robot {
    ORE,
    CLAY,
    OBSIDIAN,
    GEODE
}

#[derive(new, Copy, Clone, Eq, PartialEq, Hash)]
struct Demand {
    ore: i32,
    clay: i32,
    obsidian: i32
}

// costs
#[derive(Clone, Eq, PartialEq, Hash)]
struct Blueprint {
    id: i32,
    ore: Demand, // ore
    clay: Demand, // ore
    obsidian: Demand, // ore, clay
    geode: Demand // ore, obsidian
}

impl Blueprint {
    fn get_demand_for(&self, robot: Robot) -> Demand {
        match robot {
            Robot::ORE => self.ore,
            Robot::CLAY => self.clay,
            Robot::OBSIDIAN => self.obsidian,
            Robot::GEODE => self.geode
        }
    }
}

impl Factory {
    fn new(blueprint: Blueprint) -> Self {
        Self {
            robots: Robots {
                ore: 1,
                clay: 0,
                obsidian: 0,
                geode: 0,
            },
            resources: Resources {
                ore: 0,
                clay: 0,
                obsidian: 0,
                geode: 0,
            },
            blueprint,
            cycle: 0}
    }

    fn next_states(&self) -> Vec<Factory> {
        vec![Robot::ORE, Robot::CLAY, Robot::OBSIDIAN, Robot::GEODE]
            .iter()
            .map(|robot| self.get_next_state_for(*robot))
            .flatten()
            .collect::<Vec<_>>()
    }

    fn pay_resources(&mut self, target_robot: &Demand) {
        self.resources.ore -= target_robot.ore;
        self.resources.clay -= target_robot.clay;
        self.resources.obsidian -= target_robot.obsidian;
    }

    fn generate_resources(&mut self) {
        self.resources.ore += self.robots.ore;
        self.resources.clay += self.robots.clay;
        self.resources.obsidian += self.robots.obsidian;
        self.resources.geode += self.robots.geode;
    }

    fn get_next_state_for(&self, target_robot: Robot) -> Option<Factory> {
        // if this robot needs resources that cannot be made with this setup or should not be making more of this robot
        if !self.can_robot_be_ever_made(target_robot) || !self.robot_below_max(target_robot) {
            return None;
        }
        let mut new_state = self.clone();
        loop {
            new_state.cycle += 1;
            // loop until you can make this robot
            if !new_state.can_robot_be_made_and_below_max(target_robot) {
                new_state.generate_resources();
                continue;
            }
            new_state.pay_resources(&self.blueprint.get_demand_for(target_robot));
            new_state.generate_resources();
            new_state.create_robot(target_robot);
            return Some(new_state.clone());
        }
    }

    fn can_robot_be_ever_made(&self, target_robot: Robot) -> bool {
        match target_robot {
            Robot::ORE => true,
            Robot::CLAY => true,
            Robot::OBSIDIAN => self.robots.clay > 0,
            Robot::GEODE =>  self.robots.obsidian > 0,
        }
    }

    fn robot_below_max(&self, target_robot: Robot) -> bool {
        return match target_robot {
            Robot::ORE => self.robots.ore < self.blueprint.clay.ore.max(self.blueprint.obsidian.ore).max(self.blueprint.geode.ore),
            Robot::CLAY => self.robots.clay < self.blueprint.obsidian.clay,
            Robot::OBSIDIAN => self.robots.obsidian <self. blueprint.geode.obsidian,
            Robot::GEODE =>  true,
        }
    }

    fn can_robot_be_made_and_below_max(&self, target_robot: Robot) -> bool {
        return self.blueprint.can_make(target_robot, &self.resources)
            && self.robot_below_max(target_robot)
    }

    fn create_robot(&mut self, robot: Robot) {
        match robot {
            Robot::ORE => self.robots.ore += 1,
            Robot::CLAY => self.robots.clay += 1,
            Robot::OBSIDIAN => self.robots.obsidian += 1,
            Robot::GEODE => self.robots.geode += 1,
        }
    }

    fn heuristic(&self, cycles_to_go: i32) -> i32 {
        // how many geodes can I make?
        // the result needs to always be higher than the actual since we want maximize
        // so make any assumptions that will make the calculation easier
        let mut clay_robot = self.robots.clay;
        let mut clay = self.resources.clay;
        let mut obs_robot = self.robots.obsidian;
        let mut obsidian = self.resources.obsidian;
        let mut geode_robot = self.robots.geode;
        let mut geodes = self.resources.geode;
        for _cycle in 0..cycles_to_go {
            if self.blueprint.geode.obsidian <= obsidian {
                // simulate creating geodes
                obsidian += obs_robot;
                geodes += geode_robot;
                obsidian -= self.blueprint.geode.obsidian;
                geode_robot += 1;
                clay += clay_robot;
            } else {
                obsidian += obs_robot;
                geodes += geode_robot;
                if self.blueprint.obsidian.clay <= clay {
                    obs_robot += 1;
                } else {
                    clay_robot += 1;
                }
                clay += clay_robot;
            }
        }
        geodes
    }
}

impl Blueprint {
    fn can_make(&self, robot: Robot, resources: &Resources) -> bool {
        match robot {
            Robot::ORE => resources.ore >= self.ore.ore,
            Robot::CLAY => resources.ore >= self.clay.ore,
            Robot::OBSIDIAN => resources.ore >= self.obsidian.ore && resources.clay >= self.obsidian.clay,
            Robot::GEODE => resources.ore >= self.geode.ore && resources.obsidian >= self.geode.obsidian
        }
    }

}
fn main() {
    let start = Instant::now();
    let contents = read_to_string("inputs/day19")
        .expect("Should have been able to read the file");

    let quality_level = parse(contents.as_str())
        .enumerate()
        .map(|(i, blueprint)| (i+1) * dfs(blueprint, 24) as usize)
        .sum::<usize>();

    println!("Part 1 {}", quality_level);

    let part2 = parse(contents.as_str())
        .take(3)
        .map(|blueprint| dfs(blueprint, 32) as usize)
        .product::<usize>();

    println!("Part 2 {}, elapsed {:?}", part2, start.elapsed());
}

fn dfs(blueprint: Blueprint, rounds: i32) -> i32 {

    let mut to_visit = vec![Factory::new(blueprint)];
    let mut actual = 0;

    while let Some(state) = to_visit.pop() {
        if state.cycle > rounds {
            continue;
        }
        actual = state.resources.geode.max(actual);

        let state_estimate = state.heuristic(rounds - state.cycle);
        if state_estimate <= actual {
            // if the state to check is in the best case scenario worse than the actual we have, then drop it
            continue;
        }
        to_visit.extend(state.next_states());
    }

    actual
}

#[allow(dead_code)] // this parser makes the run in 30-40ms while the nom parser counterpart takes 4-6ms
fn parse_blueprint_line(line: &str) -> Blueprint{
    // Blueprint 30: Each ore robot costs 2 ore. Each clay robot costs 4 ore. Each obsidian robot costs 3 ore and 20 clay. Each geode robot costs 2 ore and 17 obsidian.

    let pattern = Regex::new("Blueprint (?P<id>\\d+): Each ore robot costs (?P<ore>\\d+) ore. Each clay robot costs (?P<clay>\\d+) ore. Each obsidian robot costs (?P<obs_ore>\\d+) ore and (?P<obs_clay>\\d+) clay. Each geode robot costs (?P<geo_ore>\\d+) ore and (?P<geo_obs>\\d+) obsidian.").unwrap();
    let blueprint = pattern.captures(line).unwrap();

        let id = blueprint.name("id").map_or(0, |m| m.as_str().parse().unwrap());
        let ore = Demand::new(blueprint.name("ore").map_or(0, |m| m.as_str().parse().unwrap()), 0,0);
        let clay = Demand::new(blueprint.name("clay").map_or(0, |m| m.as_str().parse().unwrap()),0,0);
        let obsidian = Demand::new(blueprint.name("obs_ore").map_or(0, |m| m.as_str().parse().unwrap()), blueprint.name("obs_clay").map_or(0, |m| m.as_str().parse().unwrap()), 0);
        let geode = Demand::new(blueprint.name("geo_ore").map_or(0, |m| m.as_str().parse().unwrap()), 0, blueprint.name("geo_obs").map_or(0, |m| m.as_str().parse().unwrap()));

    Blueprint {
        id,
        ore,
        clay,
        obsidian,
        geode,
    }
}

// Blueprint 30: Each ore robot costs 2 ore. Each clay robot costs 4 ore. Each obsidian robot costs 3 ore and 20 clay. Each geode robot costs 2 ore and 17 obsidian.
fn parse(input: &str) -> impl Iterator<Item = Blueprint> {
    separated_list1(line_ending, blueprint_parser) (input).unwrap().1.into_iter()
}

fn blueprint_parser(input: &str) -> IResult<&str, Blueprint> {
    let (input, id) = delimited(tag("Blueprint "), i32, tag(":"))(input)?;
    let (input, ore_robot_ore_cost) = delimited(tag(" Each ore robot costs "), i32, tag(" ore."))(input)?;
    let (input, clay_robot_ore_cost) = delimited(tag(" Each clay robot costs "), i32, tag(" ore."))(input)?;
    let (input, (obsidian_robot_ore_cost,_,obsidian_robot_clay_cost)) = delimited(tag(" Each obsidian robot costs "), tuple((i32, tag(" ore and "), i32)), tag(" clay."))(input)?;
    let (input, (geode_robot_ore_cost,_,geode_robot_obsidian_cost)) = delimited(tag(" Each geode robot costs "), tuple((i32, tag(" ore and "), i32)), tag(" obsidian."))(input)?;

    Ok((input, Blueprint {
        id,
        ore: Demand::new(ore_robot_ore_cost,0,0),
        clay: Demand::new(clay_robot_ore_cost,0,0),
        obsidian: Demand::new(obsidian_robot_ore_cost,obsidian_robot_clay_cost,0),
        geode: Demand::new(geode_robot_ore_cost,0,geode_robot_obsidian_cost),
    }))
}