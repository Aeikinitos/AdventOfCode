use std::fs::read_to_string;
use std::time::Instant;


fn main() {
    let start = Instant::now();
    let contents = read_to_string("../../../../inputs/advent2022/day25")
        .expect("Should have been able to read the file");

    println!("Part 1 {} : time elapsed {:?}", encode_snafu(decode_snafu(contents)) , start.elapsed());
}

fn decode_snafu(contents: String) -> i64 {
    contents.lines()
        .map(|line| {
            line.chars()
                .rev()
                .enumerate()
                .map(|(idx, char)| parse_char(idx as u32, char))
                .sum::<i64>()
    }).sum::<i64>()
}

fn parse_char(idx: u32, fivenary: char) -> i64 {
    match fivenary {
        '2' => 2 * 5_i64.pow(idx),
        '1' => 1 * 5_i64.pow(idx),
        '0' => 0,
        '-' => -1 * 5_i64.pow(idx),
        '=' => -2 * 5_i64.pow(idx),
        _ => panic!("not a correct character")
    }
}

fn encode_snafu(input: i64) -> String {
    let quotient = (input +2) /5;
    let prefix = if quotient == 0 {
        String::new()
    } else {
        encode_snafu(quotient)
    };

    prefix +
        &*match (input + 2) % 5 {
            0 => "=".to_string(),
            1 => "-".to_string(),
            2 => "0".to_string(),
            3 => "1".to_string(),
            4 => "2".to_string(),
            _ => { panic!("%5 gave a 6ith option?") }
        }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_to_snafu()  {
        // Decimal          SNAFU
        //         1              1
        //         2              2
        //         3             1=
        //         4             1-
        //         5             10
        //         6             11
        //         7             12
        //         8             2=
        //         9             2-
        //        10             20
        //        15            1=0
        //        20            1-0
        //      2022         1=11-2
        //     12345        1-0---0
        // 314159265  1121-1110-1=0
        assert_eq!(encode_snafu(1), "1");
        assert_eq!(encode_snafu(2), "2");
        assert_eq!(encode_snafu(3), "1=");
        assert_eq!(encode_snafu(4), "1-");
        assert_eq!(encode_snafu(5), "10");
        assert_eq!(encode_snafu(6), "11");
        assert_eq!(encode_snafu(7), "12");
        assert_eq!(encode_snafu(8), "2=");
        assert_eq!(encode_snafu(9), "2-");
        assert_eq!(encode_snafu(10), "20");
        assert_eq!(encode_snafu(15), "1=0");
        assert_eq!(encode_snafu(20), "1-0");
        assert_eq!(encode_snafu(2022), "1=11-2");
        assert_eq!(encode_snafu(12345), "1-0---0");
        assert_eq!(encode_snafu(314159265), "1121-1110-1=0");
    }

    #[test]
    fn test_from_snafu() {
        //  SNAFU  Decimal
        // 1=-0-2     1747
        //  12111      906
        //   2=0=      198
        //     21       11
        //   2=01      201
        //    111       31
        //  20012     1257
        //    112       32
        //  1=-1=      353
        //   1-12      107
        //     12        7
        //     1=        3
        //    122       37
        assert_eq!(decode_snafu("1=-0-2".to_string()), 1747);
        assert_eq!(decode_snafu("12111".to_string()), 906);
        assert_eq!(decode_snafu("2=0=".to_string()), 198);
        assert_eq!(decode_snafu("21".to_string()), 11);
        assert_eq!(decode_snafu("2=01".to_string()), 201);
        assert_eq!(decode_snafu("111".to_string()), 31);
        assert_eq!(decode_snafu("20012".to_string()), 1257);
        assert_eq!(decode_snafu("112".to_string()), 32);
        assert_eq!(decode_snafu("1=-1=".to_string()), 353);
        assert_eq!(decode_snafu("1-12".to_string()), 107);
        assert_eq!(decode_snafu("12".to_string()), 7);
        assert_eq!(decode_snafu("1=".to_string()), 3);
        assert_eq!(decode_snafu("122".to_string()), 37);
    }
}