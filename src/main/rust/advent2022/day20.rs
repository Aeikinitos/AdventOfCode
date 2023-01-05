use std::fs::read_to_string;
use derive_new::new;
use num_traits::{abs, ToPrimitive};

#[derive(new, Copy, Clone, Debug)]
struct Element {
    id: i32,
    value: i64,
    position: i32 // position in decrypted list
}


fn main() {
    let contents = read_to_string("inputs/day20")
        .expect("Should have been able to read the file");

    let mut order = vec![];
    let mut encrypted = vec![];
    let mut decrypted = vec![];
    let mut encrypted_key = vec![];
    let mut decrypted_key = vec![];

    generate_structs(contents, &mut encrypted, &mut order, &mut decrypted, &mut encrypted_key, &mut decrypted_key);


    for index in order.clone() {
        swap(encrypted[index], &mut encrypted, &mut decrypted);
    }

    println!("Part 1 {}", get_sum(&mut decrypted));

    for _ in 0..10 {
        for index in order.clone() {
            swap(encrypted_key[index], &mut encrypted_key, &mut decrypted_key);
        }
    }

    println!("Part 2 {}", get_sum(&mut decrypted_key));

}

fn generate_structs(contents: String, encrypted: &mut Vec<Element>, order: &mut Vec<usize>, decrypted: &mut Vec<Element>, encrypted_key: &mut Vec<Element>, decrypted_key: &mut Vec<Element>) {
    contents.lines().map(|line| line.parse::<i64>().unwrap()).enumerate()
        .map(|(i, value)| (i, value))
        .for_each(|(i, value)| {
            encrypted.push(Element::new(i as i32, value as i64, i as i32));
            encrypted_key.push(Element::new(i as i32, value * 811589153 as i64, i as i32));
            order.push(i as usize);
            decrypted.push(Element::new(i as i32, value as i64, i as i32));
            decrypted_key.push(Element::new(i as i32, value * 811589153 as i64, i as i32));
        });
}

fn get_sum(decrypted: &mut Vec<Element>) -> i64 {
    let (offset, _) = decrypted.iter().enumerate().find(|(_, element)| element.value == 0).unwrap();
    let max_len = decrypted.len();
    decrypted[(1000 + offset) % max_len].value + decrypted[(2000 + offset) % max_len].value + decrypted[(3000 + offset) % max_len].value
}

fn swap(element: Element, encrypted: &mut Vec<Element>, decrypted: &mut Vec<Element>) {
    let swap_repetitions = element.value;
    let position = element.position as i64;

    for dx in 0..abs(swap_repetitions % (decrypted.len()-1) as i64) {
        let pos_1 = get_or_wrap(position +  dx    * swap_repetitions.signum(), decrypted.len() as i64).to_usize().unwrap();
        let pos_2 = get_or_wrap(position + (dx+1) * swap_repetitions.signum(), decrypted.len() as i64).to_usize().unwrap();

        // swap values
        let temp = decrypted[pos_1];
        decrypted[pos_1] = decrypted[pos_2];
        decrypted[pos_2] = temp;

        // swap positions
        encrypted[decrypted[pos_1].id as usize].position = pos_1 as i32;
        encrypted[decrypted[pos_2].id as usize].position = pos_2 as i32;
    }
}

fn get_or_wrap(value: i64, max_len: i64) -> i64 {
    (value % max_len + max_len) % max_len
}
