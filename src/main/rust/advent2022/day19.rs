use std::collections::HashSet;
use std::fs::read_to_string;
use regex::Regex;
use derive_new::new;

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
        let mut next_states = vec![Robot::ORE, Robot::CLAY, Robot::OBSIDIAN, Robot::GEODE]
            .iter()
            .map(|robot| self.get_next_state_for(*robot))
            .filter(|next_state| next_state.is_some())
            .map(|next_state| next_state.unwrap())
            .collect::<Vec<_>>();

        let mut new_state = self.clone();
        new_state.generate_resources();
        new_state.cycle += 1;
        next_states.push(new_state);

        next_states
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
        if !self.can_robot_be_made_and_below_max(target_robot) {
            return None;
        }
        let mut new_state = self.clone();
        new_state.cycle += 1;
        new_state.pay_resources(&self.blueprint.get_demand_for(target_robot));
        new_state.generate_resources();
        new_state.create_robot(target_robot);
        Some(new_state)
    }

    fn can_robot_be_made_and_below_max(&self, target_robot: Robot) -> bool {
        return self.blueprint.can_make(target_robot, &self.resources)
            && match target_robot {
                Robot::ORE => self.robots.ore < self.blueprint.clay.ore.max(self.blueprint.obsidian.ore).max(self.blueprint.geode.ore),
                Robot::CLAY => self.robots.clay < self.blueprint.obsidian.clay,
                Robot::OBSIDIAN => self.robots.obsidian <self. blueprint.geode.obsidian,
                Robot::GEODE =>  true,
        }
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
            } else {
                obsidian += obs_robot;
                obs_robot += 1; // make obsidian as if free
                geodes += geode_robot;
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
    let contents = read_to_string("../../../../inputs/advent2022/day19")
        .expect("Should have been able to read the file");

    let quality_level = contents.lines()
        .map(|line| parse_blueprint_line(line))
        .enumerate()
        .map(|(i, blueprint)| (i+1) * dfs(blueprint, 24) as usize)
        .sum::<usize>();

    println!("Part 1 {}", quality_level);

    let part2 = contents.lines()
        .take(3)
        .map(|line| parse_blueprint_line(line))
        .map(|blueprint| dfs(blueprint, 32) as usize)
        .product::<usize>();

    println!("Part 2 {}", part2);
}

fn dfs(blueprint: Blueprint, rounds: i32) -> i32 {
    let factory = Factory::new(blueprint);

    let mut seen = HashSet::new();
    let mut to_visit = vec![factory];
    let mut actual = 0;

    while let Some(state) = to_visit.pop() {
        if state.resources.geode > actual {
            // if we have a better geode accumulation, then update the best actual state
            actual = state.resources.geode;
        }
        let state_estimate = state.heuristic(rounds - state.cycle);
        if state_estimate <= actual {
            // if the state to check is in the best case scenario worse than the actual we have, then drop it
            continue;
        }
        let mut next_states = state.next_states();
        next_states.retain(|state| !seen.contains(state) && state.cycle <= rounds);
        seen.extend(next_states.clone());
        to_visit.extend(next_states.clone());
    }

    actual
}

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