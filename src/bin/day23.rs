use std::collections::{VecDeque, HashMap};
use std::time::Instant;

fn main() {
    const RADIX: u32 = 10;
    let sample = "389125467";
    let input = sample;
    // let input = "394618527";

    let mut cups: VecDeque<u32> = input.chars().map(|c| c.to_digit(RADIX).unwrap()).collect();
    let mut cupsIndex :HashMap<u32, _> = HashMap::new();
    
    cups.iter().enumerate().for_each( |(i, &cup)| {cupsIndex.insert(cup, i as usize);} );
    

    for i in 10..1000001 {
        cups.push_back(i);
        cupsIndex.insert(i,i as usize);
    }

    for iteration in 0..10000000 {
        let start = Instant::now();

        // take [0] as current cup
        // take out next 3
        // select destination by starting from current cup number and do -1 (care for negative) until the number exists in cups
        // add the "next3" after the destination
        // repeat
        let current_cup = cups.pop_front().unwrap();
        let drained = cups.drain(..3).collect::<VecDeque<_>>();

        let mut destination_cup = current_cup - 1;
        while drained.contains(&destination_cup) && destination_cup > 0 {
            destination_cup -= 1;
        }
        // either destination is Zero which means get the max number 
        if destination_cup == 0 {
            // check the 4 biggest numbers just in case all 3 are in the drained cups
            for max in 999997..1000001 {
                if !drained.contains(&max) {destination_cup = max;}
            }
        }
        // or destination is available
        let index_of_destination_cup = cupsIndex.get(&destination_cup).unwrap();
        // match cups.iter().position(|&x| x == destination_cup) {
        //     Some(index) => index_of_destination_cup = index,
        //     None => {},
        // }

        // let (index_of_destination_cup, _element) = cups
        //     .iter()
        //     .enumerate()
        //     .find(|(_i, cup)| **cup == destination_cup)
        //     .unwrap();

        // adding back items
        // // check if index_of_one changed
        // match drained.iter().position(|&x| x == 1) {
        //     Some(index) => index_of_one = index_of_destination_cup + 1 + index, // 2 -> [], 1 -> [1, 2], 0 -> [0, 1, 2]
        //     None => {},// do noting
        // }
        // if current_cup == 1 {
        //     index_of_one = cups.len();
        // }
        drained
            .into_iter()
            .rev()
            .for_each(|element| {
                cups.insert(index_of_destination_cup + 1, element);
                cupsIndex.insert(element, index_of_destination_cup + 1);
            });
        cups.push_back(current_cup);
    
        let elapsed = start.elapsed();
        if iteration % 100 == 0 {println!("{:?}: iteration - {}",elapsed,iteration);}
        
    }

    // let (index_of_one, _element) = cups
    //     .iter()
    //     .enumerate()
    //     .find(|(_i, cup)| **cup == 1)
    //     .unwrap();
    // let mut previous_to_one = cups.drain(..index_of_one).collect::<VecDeque<_>>();

    // part 2
    let index_of_one = cupsIndex.get(&1).unwrap();
    println!("1 - {}, 2 - {}, 3 - {}", cups.get(index_of_one+0).unwrap(), cups.get(index_of_one+1).unwrap(), cups.get(index_of_one+2).unwrap())

    // part 1
    // cups.append(&mut previous_to_one);
    // cups.pop_front(); // discard 1
    // let concated = cups.iter().map(u32::to_string).collect::<String>();    
    // println!("{:?}", concated);
}
