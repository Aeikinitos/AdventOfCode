use std::collections::hash_map::DefaultHasher;
use std::collections::HashMap;
use std::fmt::{Display, Formatter};
use std::fs::read_to_string;
use std::hash::{Hash, Hasher};
use std::time::Instant;
use derive_new::new;
use regex::Regex;
use itertools::Itertools;

#[derive(Clone, Debug)]
struct Node {
    flow_rate: usize,
}

#[derive(new, Clone, Debug)]
struct State {
    opened_flow_rate: usize,
    position: Vec<String>,
    nodes: HashMap<String, Node>,
    time_remaining: Vec<usize>,
}


impl Display for State {
    fn fmt(&self, f: &mut Formatter<'_>) -> std::fmt::Result {
        write!(f, "[{:?} Flow:{:4} ||", self.position, self.opened_flow_rate).expect("cannot write this");
        for (name,node) in self.nodes.iter() {
            write!(f, "({}:{:2}), ", name, node.flow_rate).expect("cannot write this");
        }
        write!(f, "]").expect("cannot write this");
        Ok(())
    }
}


struct Printable<'a>(&'a Vec<State>);

impl Display for Printable<'_> {
    fn fmt(&self, f: &mut Formatter) -> std::fmt::Result {
        for v in self.0 {
            write!(f, "\t{}", v)?;
        }
        Ok(())
    }
}

impl State {

    fn hash(&self) -> u64 {
        let mut hasher = DefaultHasher::new();
        self.nodes.values().map(|node| node.flow_rate).enumerate().for_each(|value| value.hash(&mut hasher) );
        self.position.iter().sorted().for_each(|position| position.hash(&mut hasher));
        self.opened_flow_rate.hash(&mut hasher);
        hasher.finish()
    }

}

impl Hash for State {
    fn hash<H: Hasher>(&self, hasher: &mut H) {
        self.nodes.values().for_each(|value| value.flow_rate.hash( hasher) );
        self.position.hash(hasher);
        self.opened_flow_rate.hash(hasher);
    }
}

impl PartialEq<Self> for State {
    fn eq(&self, other: &Self) -> bool {
        self.hash() == other.hash()
    }
}

impl Eq for State {
}

fn main() {
    let start = Instant::now();
    let contents = read_to_string("inputs/advent2022/day16")
        .expect("Should have been able to read the file");

    let mut nodes: HashMap<String, Node> = HashMap::new();
    let mut edges: HashMap<String, HashMap<String,usize>> = HashMap::new();

    contents.lines().for_each(|line| {
        let (node_name, node, edge) = parse_line(line);
        nodes.insert(node_name.clone(), node);
        edges.insert(node_name, edge);
    });

    let non_zero_edges: HashMap<String, HashMap<String, usize>> = collapes_zero_edges(&nodes, edges);

    let mut max = 0usize;
    let mut hashes: HashMap<u64, Vec<usize>> = HashMap::new();
    expand_states_and_loop(&mut hashes, &non_zero_edges, State::new(0, vec!["AA".to_string()], nodes.clone(), vec![30]), &mut max);

    println!("Part 1: {}", max);
    println!("Time elapsed in part 1 is: {:?}", start.elapsed());

    max = 0;
    hashes.clear();
    expand_states_and_loop(&mut hashes, &non_zero_edges, State::new(0, vec!["AA".to_string(), "AA".to_string()], nodes, vec![26,26]), &mut max);

    println!("Part 2: {}", max);
    println!("Time elapsed in part 2 is: {:?}", start.elapsed());
}

fn collapes_zero_edges(nodes: &HashMap<String, Node>, edges: HashMap<String, HashMap<String, usize>>) -> HashMap<String, HashMap<String,usize>> {

    let non_zero_nodes = nodes.iter().filter(|(name, node)| node.flow_rate > 0 || name == &&"AA".to_string()).collect::<HashMap<_,_>>();
    let mut distances: HashMap<String, HashMap<String,usize>> = HashMap::new();
    for (node_a,_) in nodes.clone() {
        for (node_b,_) in nodes.clone() {
            if node_a == node_b {
                distances.entry(node_a.clone())
                    .and_modify(|edges| {edges.insert(node_b.clone(), 0usize);})
                    .or_insert(HashMap::from([(node_b.clone(), 0usize)]));
            }
            else {
                distances.entry(node_a.clone())
                    .and_modify(|edges| {edges.insert(node_b.clone(), usize::MAX);})
                    .or_insert(HashMap::from([(node_b.clone(), usize::MAX)]));
            }
        }
    }


    for edge in edges {
        distances.entry(edge.0)
            .and_modify(|edges| {edge.1.iter().for_each(|(edge,_distance)| {edges.insert(edge.clone(), 1usize);});});
    }

    for (k,_) in nodes {
        for (i,_) in nodes {
            for (j,_) in nodes {
                let distance_with_jump = distances.get(i).unwrap().get(k).unwrap().checked_add(distances.get(k).unwrap().get(j).unwrap().clone()).unwrap_or(usize::MAX);
                if distances.get(i).unwrap().get(j).unwrap() > &distance_with_jump {
                    distances.entry(i.clone()).and_modify(|edge_list| {edge_list.insert(j.clone(), distance_with_jump);});
                }
            }
        }
    }

    for (node,_) in nodes {
        if !non_zero_nodes.contains_key(node) {
            distances.remove(node);
            distances.values_mut().for_each(|edge| {edge.remove(node);});
        }
    }
    for (node, edges) in &mut distances {
        edges.remove(&*node);
    }
    distances
}

fn expand_states_and_loop(hashes: &mut HashMap<u64, Vec<usize>>, edges: &HashMap<String, HashMap<String, usize>>, state: State, max: &mut usize) {

    *max = state.opened_flow_rate.max(*max);

    for state in expand_states(edges, state) {
        let estimate = get_estimate(&state);
        if estimate <= *max {
            continue;
        }
        match hashes.get(&state.hash()) {
            None => {
                hashes.insert(state.hash(), state.time_remaining.clone());
                expand_states_and_loop(hashes, edges, state, max);
            }
            Some(time_remaining) if time_remaining <= &state.time_remaining.clone() => {
                hashes.insert(state.hash(), state.time_remaining.clone());
                expand_states_and_loop(hashes, edges, state, max);
            }
            _ => {} // already visited this state (same flow, same opened nodes, same position) and faster than this iteration
        }
    }
}

fn get_estimate(state: &State) -> usize {
    let two_min_repetitions = state.time_remaining.iter().min().unwrap()/2;
    state.nodes
        .iter()
        .map(|(_, node)| node.flow_rate)
        .filter(|flow| *flow != 0)
        .sorted()
        .rev()
        .zip((0..two_min_repetitions).rev())
        .fold(0, |acc, (flow, time)| acc + flow * (time*2-1))
        + state.opened_flow_rate
}

fn try_open(state: &State, position_idx: usize, next_node_name: &String, distance: &usize) -> Option<State> {
    let node = state.nodes.get(next_node_name);

    if node?.flow_rate > 0 {
        let mut nodes = state.nodes.clone();
        nodes.entry(next_node_name.to_string()).and_modify(|node| node.flow_rate = 0);
        let mut times_remaining = state.time_remaining.clone();
        times_remaining[position_idx] = times_remaining[position_idx].checked_sub(1 + distance)?;
        let mut position = state.position.clone();
        position[position_idx] = next_node_name.clone();
        Some(State {
            opened_flow_rate: state.opened_flow_rate + node?.flow_rate * times_remaining[position_idx],
            nodes,
            time_remaining: times_remaining,
            position,
        })
    } else {
        None
    }
}

fn move_to(state: &State, position_idx: usize, node_name: &String, distance: &usize) -> Option<State>  {
    let mut position = state.position.clone();
    position[position_idx] = node_name.clone();
    let mut times_remaining = state.time_remaining.clone();
    times_remaining[position_idx] = times_remaining[position_idx].checked_sub(*distance)?;
    Some(State {
        time_remaining: times_remaining,
        position,
        ..state.clone()
    })
}

// #[cached]
fn expand_states(edges: &HashMap<String, HashMap<String, usize>>, state: State) -> impl Iterator<Item = State>{
    let positions = state.position.clone();
    let mut states = vec![state];
    for (position_idx, position) in positions.iter().enumerate() {
        let mut new_states = vec![];
        while let Some(state) = states.pop() {
            edges
                .get(position.clone().as_str())
                .unwrap()
                .iter()
                .filter(|(next_node_name, _)| edges.get(*next_node_name).unwrap().len() != 1 || state.nodes.get(*next_node_name).unwrap().flow_rate > 0)
                .flat_map(|(next_node_name, distance)| [
                    try_open(&state, position_idx, next_node_name, distance),
                    move_to(&state, position_idx, next_node_name, distance)])
                .flatten()
                .sorted_by(|state1, state2| state2.opened_flow_rate.cmp(&state1.opened_flow_rate))
                .for_each(|new_state| new_states.push(new_state));
        }
        states = new_states;
    }
    states.into_iter()

}

fn parse_line(line: &str) -> (String, Node, HashMap<String,usize>){
    let pattern = Regex::new("Valve (?P<node_name>[A-Z]{2}) has flow rate=(?P<flowrate>\\d+); tunnels? leads? to valves? (?P<edges>[A-Z]+(, [A-Z]{2})*)").unwrap();
    let valve = pattern.captures(line).unwrap();

    let node_name = valve.name("node_name").map_or("".to_string(), |m| m.as_str().to_string());
    let flow_rate = valve.name("flowrate").map_or(0, |m| m.as_str().parse().unwrap());
    let edges = valve.name("edges").map_or(HashMap::new(), |m| m.as_str().split(&[' ', ','][..]).filter(|s| !s.is_empty()).map(|s| (s.to_string(),1)).collect::<HashMap<String,usize>>());

    (node_name.clone(), Node { flow_rate }, edges)
}