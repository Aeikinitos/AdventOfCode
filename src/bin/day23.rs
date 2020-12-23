use std::collections::VecDeque;

fn main() {
    const RADIX: u32 = 10;
    // let sample = "389125467";
    // let input = sample;
    let input = "394618527";

    let mut cups: VecDeque<_> = input.chars().map(|c| c.to_digit(RADIX).unwrap()).collect();

    for _ in 0..100 {
        // take [0] as current cup
        // take out next 3
        // select destination by starting from current cup number and do -1 (care for negative) until the number exists in cups
        // add the "next3" after the destination
        // repeat
        let current_cup = cups.pop_front().unwrap();
        let drained = cups.drain(..3).collect::<VecDeque<_>>();

        let mut destination_cup = current_cup - 1;
        while destination_cup > 0 {
            if cups.contains(&destination_cup) {
                break;
            }
            destination_cup -= 1;
        }
        if destination_cup == 0 {
            destination_cup = *cups.iter().max().unwrap();
        }

        let (index_of_current_cup, _element) = cups
            .iter()
            .enumerate()
            .find(|(_i, cup)| **cup == destination_cup)
            .unwrap();

        drained
            .into_iter()
            .rev()
            .for_each(|element| cups.insert(index_of_current_cup + 1, element));
        cups.push_back(current_cup);
        // println!("{:?}", cups);
    }

    let (index_of_one, _element) = cups
        .iter()
        .enumerate()
        .find(|(_i, cup)| **cup == 1)
        .unwrap();
    let mut previous_to_one = cups.drain(..index_of_one).collect::<VecDeque<_>>();
    cups.append(&mut previous_to_one);
    cups.pop_front(); // discard 1

    let concated: String = cups.into_iter().fold("".to_string(), |mut i, j| {
        i.push_str(&j.to_string());
        i
    });
    println!("{:?}", concated);
}
