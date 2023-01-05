use std::collections::{HashSet};
use std::fs::read_to_string;
use itertools::Itertools;
use num_traits::abs;

#[derive(Eq, PartialEq, Hash)]
enum COORDS {
    X=1,Y=2,Z=3
}

#[derive(Debug, Eq, PartialEq, Copy, Clone, Hash)]
struct Point3D {
    x:i32,
    y:i32,
    z:i32
}
impl Point3D {
    fn new(x:i32,y:i32,z:i32) -> Self {
        Self {x,y,z}
    }
    fn from_str(text: &str) -> Self {
        let (x,y,z) = text.split(',').map(|element| element.parse::<i32>().unwrap()).collect_tuple().unwrap();
        Self{x,y,z}
    }
    fn has_common_face(&self, other: &Point3D) -> Option<COORDS> {
        match (self.x, self.y, self.z, other.x, other.y, other.z) {
            (x,y,z,j,k,l) if x==j && y==k && abs(z-l) == 1 => Some(COORDS::Z),
            (x,y,z,j,k,l) if x==j && z==l && abs(y-k) == 1 => Some(COORDS::Y),
            (x,y,z,j,k,l) if y==k && z==l && abs(x-j) == 1 => Some(COORDS::X),
            (_, _, _, _, _, _) => None
        }
    }
}

fn main() {
    let contents = read_to_string("inputs/day18")
        .expect("Should have been able to read the file");

    let droplets = contents.lines().map(|line| Point3D::from_str(line)).collect::<Vec<_>>();

    let mut common: i32 = 0;
    for point in &droplets {
        for other in &droplets {
            if point.has_common_face(other).is_some() {
                common += 1; // add 1 cause point.common(other) will be counted twice on other.common(point)
            }
        }
    }

    println!("Part 1 {}", droplets.len() as i32 *6-common);

    let mut water_filled: Vec<Point3D> = vec![];
    let mut air_pockets = HashSet::new();
    let bounds = find_bounds(&droplets);
    for point in &droplets {
        if is_air_pocket(&Point3D::new(point.x+1, point.y, point.z), &droplets, &mut water_filled, bounds) { air_pockets.insert(Point3D::new(point.x+1, point.y, point.z));}
        if is_air_pocket(&Point3D::new(point.x-1, point.y, point.z), &droplets, &mut water_filled, bounds) { air_pockets.insert(Point3D::new(point.x-1, point.y, point.z));}
        if is_air_pocket(&Point3D::new(point.x, point.y+1, point.z), &droplets, &mut water_filled, bounds) { air_pockets.insert(Point3D::new(point.x, point.y+1, point.z));}
        if is_air_pocket(&Point3D::new(point.x, point.y-1, point.z), &droplets, &mut water_filled, bounds) { air_pockets.insert(Point3D::new(point.x, point.y-1, point.z));}
        if is_air_pocket(&Point3D::new(point.x, point.y, point.z+1), &droplets, &mut water_filled, bounds) { air_pockets.insert(Point3D::new(point.x, point.y, point.z+1));}
        if is_air_pocket(&Point3D::new(point.x, point.y, point.z-1), &droplets, &mut water_filled, bounds) { air_pockets.insert(Point3D::new(point.x, point.y, point.z-1));}
    }

    let mut air_faces = 0;
    for air_pocket in &air_pockets {
        for droplet in &droplets {
            if air_pocket.has_common_face(droplet).is_some() {
                air_faces += 1;
            }
        }
    }
    println!("Part 2 {}", droplets.len() as i32 *6 -common -air_faces);

}

fn is_air_pocket(point: &Point3D, droplets: &Vec<Point3D>, water_filled: &mut Vec<Point3D>, bounds: (i32, i32, i32, i32, i32, i32)) -> bool {
    if droplets.contains(point) || !within_bounds(point, bounds) {
        return false;
    }
    let mut temp_water_filled: Vec<Point3D> = vec![];

    let mut to_visit: Vec<Point3D> = vec![point.clone()];

    while let Some(next_point) = to_visit.pop() {
        let neighbours = neighbours(&next_point);
        if neighbours.iter()
            .any(|neighbours| !within_bounds(neighbours, bounds)) {
            // this pocket can be accessed externally
            return false;
        }
        neighbours
            .iter()
            // .filter(|neighbour| within_bounds(neighbour, bounds))
            .filter(|neighbour| !droplets.contains(neighbour))
            .filter(|neighbour| !water_filled.contains(neighbour))
            .filter(|neighbour| !temp_water_filled.contains(neighbour))
            .for_each(|neighbour| to_visit.push(neighbour.clone()));

        temp_water_filled.push(next_point.clone());
    }

    water_filled.extend(temp_water_filled);
    // this pocket cannot be access externally, all its faces should be removed from count
    true
}

fn neighbours(point: &Point3D) -> Vec<Point3D> {
    vec![
        Point3D::new(point.x+1, point.y, point.z),
        Point3D::new(point.x-1, point.y, point.z),
        Point3D::new(point.x, point.y+1, point.z),
        Point3D::new(point.x, point.y-1, point.z),
        Point3D::new(point.x, point.y, point.z+1),
        Point3D::new(point.x, point.y, point.z-1)
    ]
}

fn within_bounds(neighbour: &Point3D, (min_bound_x, min_bound_y, min_bound_z, max_bound_x, max_bound_y, max_bound_z): (i32, i32, i32, i32, i32, i32)) -> bool {
    min_bound_x <= neighbour.x && neighbour.x <= max_bound_x
    && min_bound_y <= neighbour.y && neighbour.y <= max_bound_y
    && min_bound_z <= neighbour.z && neighbour.z <= max_bound_z
}

fn find_bounds(droplets: &Vec<Point3D>) -> (i32, i32, i32, i32, i32, i32){
    (droplets.iter().map(|point| point.x).min().unwrap(),
     droplets.iter().map(|point| point.y).min().unwrap(),
     droplets.iter().map(|point| point.z).min().unwrap(),
     droplets.iter().map(|point| point.x).max().unwrap(),
     droplets.iter().map(|point| point.y).max().unwrap(),
     droplets.iter().map(|point| point.z).max().unwrap())
}