use std::collections::HashMap;
use std::fs::read_to_string;
use itertools::Itertools;

const FILE_THRESHOLD: usize = 100000;
const DISK_SPACE: usize = 70000000;
const NEEDED_SPACE: usize = 30000000;


struct File {
    size: usize,
    name: String,
    is_dir: bool,
    children: Vec<File>
}

impl File{
    pub fn new_dir(name: String) -> Self {
        Self {
            size: 0,
            name,
            is_dir: true,
            children: vec![],
        }
    }

    pub fn new_file(name: String, size: usize) -> Self {
        Self {
            size,
            name,
            is_dir: false,
            children: vec![],
        }
    }

    fn add_file(&mut self, file: File) {
        self.children.push(file);
    }

    fn add_files(&mut self, files: Vec<File>) {
        self.children.extend(files);
    }

    fn get_size(&self) -> usize {
        if self.is_dir {
            self.children.iter().map(|file| file.get_size()).sum()
        }  else {
            self.size
        }
    }

    fn get_dirs(&self) -> Vec<&File> {
        let mut dirs = vec![];
        self.children.iter().filter(is_dir).for_each(|file| {
            dirs.push(file);
            dirs.extend(file.get_dirs())
        });
        dirs
    }
}

fn is_dir(file: &&File) -> bool {
    file.is_dir
}

fn main() {
    let contents = read_to_string("inputs/advent2022/day07")
        .expect("Should have been able to read the file");
    let mut lines = contents.lines().collect::<Vec<_>>();
    lines.reverse();
    let mut root = File::new_dir("root".to_string());
    root.add_files(process_dir(&mut lines));

    println!("Part 1 {}", root.get_dirs().iter().filter(|dir| dir.get_size() <= FILE_THRESHOLD).map(|file| file.get_size()).sum::<usize>());
    let needed_remaining = NEEDED_SPACE -(DISK_SPACE-root.get_size());
    println!("Part 2 {}", root.get_dirs().iter().filter(|dir| dir.get_size() >= needed_remaining).map(|file| file.get_size()).sorted().collect::<Vec<_>>()[0]);

}

fn process_dir(lines: &mut Vec<&str>) -> Vec<File> {
    let mut listing = vec![];
    while let Some(line) = lines.pop() {
        match line.split(' ').collect::<Vec<_>>()[..] {
            ["$", "cd", "/"] => {}, // already handled
            ["$", "cd", ".."] => break,
            ["$", "cd", changed_dir_name] => {
                let mut changed_dir = File::new_dir(changed_dir_name.to_string());
                changed_dir.add_files(process_dir(lines));
                listing.push(changed_dir);
            },
            ["$", "ls"] => {},
            ["dir", _dirname] => {}, // unless cd'd and then ls'd, dont register the dir.
            [size, filename] => listing.push(File::new_file(filename.to_string(), size.parse::<usize>().expect("not a size"))),
            [..] => panic!("eshei klatsa")
        }
    }

    listing
}

