class School:
    def __init__(self):
        self._students = {}  # Dictionary to store students by grade
        self._addition_results = []  # List to track addition results

    def add_student(self, name, grade):
        # Check if student already exists in any grade
        for grade_students in self._students.values():
            if name in grade_students:
                self._addition_results.append(False)
                return False
        
        # Add student to the specified grade
        if grade not in self._students:
            self._students[grade] = []
        
        self._students[grade].append(name)
        # Sort students in this grade alphabetically
        self._students[grade].sort()
        
        self._addition_results.append(True)
        return True

    def roster(self):
        result = []
        # Get all grades and sort them
        sorted_grades = sorted(self._students.keys())
        
        # Add students from each grade in order
        for grade in sorted_grades:
            result.extend(self._students[grade])
        
        return result

    def grade(self, grade_number):
        # Return an empty list if the grade doesn't exist
        if grade_number not in self._students:
            return []
        
        # Return a copy of the list to prevent modification
        return self._students[grade_number].copy()

    def added(self):
        return self._addition_results
