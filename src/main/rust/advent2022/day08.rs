use std::fs::read_to_string;

fn main() {
    let contents = read_to_string("inputs/day08")
        .expect("Should have been able to read the file");

    let grid_size = contents.lines().count();
    let mut grid = vec![vec![0;grid_size]; grid_size];

    for (row_i, row) in contents.lines().enumerate() {
        for (column_j, tree_height) in row.chars().map(|char| char.to_digit(10)).enumerate() {
            grid[row_i][column_j] = tree_height.expect("klatsa");
        }
    }

    let mut counter = 0;
    for row_i in 1..grid_size-1 {
        for column_j in 1..grid_size-1 {
            let mut visible = true;
            // look left
            for k in 0..column_j {
                if grid[row_i][k] >= grid[row_i][column_j] {
                    visible = false;
                    break;
                }
            }
            if visible {
                counter +=1;
                continue;
            }
            // look right
            visible = true;
            for k in column_j+1..grid_size {
                if grid[row_i][k] >= grid[row_i][column_j] {
                    visible = false;
                    break;
                }
            }
            if visible {
                counter +=1;
                continue;
            }
            // look down
            visible = true;
            for k in row_i+1..grid_size {
                if grid[k][column_j] >= grid[row_i][column_j] {
                    visible = false;
                    break;
                }
            }
            if visible {
                counter +=1;
                continue;
            }
            // look up
            visible = true;
            for k in 0..row_i {
                if grid[k][column_j] >= grid[row_i][column_j] {
                    visible = false;
                    break;
                }
            }
            if visible {
                counter +=1;
                continue;
            }
        }
    }
    println!("Part 1: {}", counter+(grid_size-1)*4);

    let mut max_score = 0;

    // dont get edge cause at least one of the scores are 0
    for row_i in 1..grid_size-1 {
        for column_j in 1..grid_size-1 {
            let mut distance_left = column_j;
            let mut distance_right = grid_size-1-column_j;
            let mut distance_down = grid_size-1-row_i;
            let mut distance_up = row_i;
            // look left
            for k in (0..column_j).rev() {
                if grid[row_i][k] >= grid[row_i][column_j] {
                    distance_left = column_j-k;
                    break;
                }
            }

            // look right
            for k in column_j+1..grid_size {
                if grid[row_i][k] >= grid[row_i][column_j] {
                    distance_right = k - column_j;
                    break;
                }
            }

            // look down
            for k in row_i+1..grid_size {
                if grid[k][column_j] >= grid[row_i][column_j] {
                    distance_down = k-row_i;
                    break;
                }
            }

            // look up
            for k in (0..row_i).rev() {
                if grid[k][column_j] >= grid[row_i][column_j] {
                    distance_up = row_i-k;
                    break;
                }
            }

            let score = distance_up*distance_left*distance_down*distance_right;
            if max_score < score {
                max_score = score;
            }
        }
    }

    println!("Part 2: {max_score}");
}

