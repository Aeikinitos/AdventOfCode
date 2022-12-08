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
    children: HashMap<String, File>
}

impl File{
    pub fn new_dir(name: String) -> Self {
        Self {
            size: 0,
            name,
            is_dir: true,
            children: HashMap::new(),
        }
    }

    pub fn new_file(name: String, size: usize) -> Self {
        Self {
            size,
            name,
            is_dir: false,
            children: HashMap::new(),
        }
    }

    fn add_file(&mut self, file: File) {
        self.children.insert(file.name.clone(), file);
    }

    fn add_files(&mut self, files: Vec<File>) {
        files.into_iter().for_each(|file| self.add_file(file))
    }

    fn get_size(&self) -> usize {
        if self.is_dir {
            self.children.iter().map(|(_,file)| file.get_size()).sum()
        }  else {
            self.size
        }
    }

    fn get_dirs(&self) -> Vec<&File> {
        let mut dirs = vec![];
        self.children.iter().for_each(|(_,file)| if file.is_dir {dirs.push(file);dirs.extend(file.get_dirs())});
        dirs
    }
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
/*
- / (dir)
  - a (dir)
    - e (dir)
      - i (file, size=584)
    - f (file, size=29116)
    - g (file, size=2557)
    - h.lst (file, size=62596)
  - b.txt (file, size=14848514)
  - c.dat (file, size=8504156)
  - d (dir)
    - j (file, size=4060174)
    - d.log (file, size=8033020)
    - d.ext (file, size=5626152)
    - k (file, size=7214296)
 */
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

