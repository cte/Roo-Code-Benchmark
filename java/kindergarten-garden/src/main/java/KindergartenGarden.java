import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class KindergartenGarden {
    private static final List<String> STUDENTS = Arrays.asList(
        "Alice", "Bob", "Charlie", "David",
        "Eve", "Fred", "Ginny", "Harriet",
        "Ileana", "Joseph", "Kincaid", "Larry"
    );
    
    private String[] rows;

    KindergartenGarden(String garden) {
        this.rows = garden.split("\n");
    }

    List<Plant> getPlantsOfStudent(String student) {
        int studentIndex = STUDENTS.indexOf(student);
        int startPosition = studentIndex * 2;
        
        List<Plant> plants = new ArrayList<>();
        
        // Add plants from the first row
        plants.add(Plant.getPlant(rows[0].charAt(startPosition)));
        plants.add(Plant.getPlant(rows[0].charAt(startPosition + 1)));
        
        // Add plants from the second row
        plants.add(Plant.getPlant(rows[1].charAt(startPosition)));
        plants.add(Plant.getPlant(rows[1].charAt(startPosition + 1)));
        
        return plants;
    }
}
