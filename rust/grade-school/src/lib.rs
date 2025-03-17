use std::collections::{HashMap, HashSet};

pub struct School {
    roster: HashMap<u32, HashSet<String>>,
    // Track all students to prevent adding to multiple grades
    all_students: HashSet<String>,
}

impl School {
    pub fn new() -> School {
        School {
            roster: HashMap::new(),
            all_students: HashSet::new(),
        }
    }

    pub fn add(&mut self, grade: u32, student: &str) {
        // Only add the student if they're not already in any grade
        if !self.all_students.contains(student) {
            self.roster
                .entry(grade)
                .or_insert_with(HashSet::new)
                .insert(student.to_string());
            
            self.all_students.insert(student.to_string());
        }
    }

    pub fn grades(&self) -> Vec<u32> {
        let mut grades: Vec<u32> = self.roster.keys().cloned().collect();
        grades.sort();
        grades
    }

    // If `grade` returned a reference, `School` would be forced to keep a `Vec<String>`
    // internally to lend out. By returning an owned vector of owned `String`s instead,
    // the internal structure can be completely arbitrary. The tradeoff is that some data
    // must be copied each time `grade` is called.
    pub fn grade(&self, grade: u32) -> Vec<String> {
        match self.roster.get(&grade) {
            Some(students) => {
                let mut student_list: Vec<String> = students.iter().cloned().collect();
                student_list.sort();
                student_list
            }
            None => Vec::new(),
        }
    }
}
