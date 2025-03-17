//
// This is only a SKELETON file for the 'Grade School' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class GradeSchool {
  constructor() {
    this._roster = {};
  }

  roster() {
    // Return a deep copy of the roster to prevent modification outside
    return JSON.parse(JSON.stringify(this._roster));
  }

  add(name, grade) {
    // Check if the student is already in another grade
    this._removeStudentFromAllGrades(name);

    // Add the student to the specified grade
    if (!this._roster[grade]) {
      this._roster[grade] = [];
    }
    
    // Add the student and sort the array alphabetically
    this._roster[grade].push(name);
    this._roster[grade].sort();
  }

  grade(gradeNumber) {
    // Return a copy of the array of students in the specified grade
    return this._roster[gradeNumber] ? [...this._roster[gradeNumber]] : [];
  }

  // Helper method to remove a student from all grades
  _removeStudentFromAllGrades(name) {
    for (const grade in this._roster) {
      const index = this._roster[grade].indexOf(name);
      if (index !== -1) {
        this._roster[grade].splice(index, 1);
      }
    }
  }
}
