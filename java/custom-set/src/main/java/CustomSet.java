import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

class CustomSet<T> {
    private final HashSet<T> elements;

    CustomSet() {
        this.elements = new HashSet<>();
    }

    CustomSet(Collection<T> data) {
        this.elements = new HashSet<>();
        if (data != null) {
            this.elements.addAll(data);
        }
    }

    boolean isEmpty() {
        return elements.isEmpty();
    }

    boolean contains(T element) {
        return elements.contains(element);
    }

    boolean isDisjoint(CustomSet<T> other) {
        // Two sets are disjoint if they have no elements in common
        for (T element : other.elements) {
            if (this.contains(element)) {
                return false;
            }
        }
        return true;
    }

    boolean add(T element) {
        return elements.add(element);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CustomSet)) {
            return false;
        }
        
        @SuppressWarnings("unchecked")
        CustomSet<T> other = (CustomSet<T>) obj;
        
        // Two sets are equal if they contain the same elements
        // First check if sizes are different
        if (this.elements.size() != other.elements.size()) {
            return false;
        }
        
        // Then check if all elements in this set are in the other set
        for (T element : this.elements) {
            if (!other.contains(element)) {
                return false;
            }
        }
        
        return true;
    }

    CustomSet<T> getIntersection(CustomSet<T> other) {
        // Intersection contains elements that are in both sets
        CustomSet<T> intersection = new CustomSet<>();
        for (T element : this.elements) {
            if (other.contains(element)) {
                intersection.add(element);
            }
        }
        return intersection;
    }

    CustomSet<T> getUnion(CustomSet<T> other) {
        // Union contains all elements from both sets
        CustomSet<T> union = new CustomSet<>();
        union.elements.addAll(this.elements);
        union.elements.addAll(other.elements);
        return union;
    }

    CustomSet<T> getDifference(CustomSet<T> other) {
        // Difference contains elements in this set that are not in the other set
        CustomSet<T> difference = new CustomSet<>();
        for (T element : this.elements) {
            if (!other.contains(element)) {
                difference.add(element);
            }
        }
        return difference;
    }

    boolean isSubset(CustomSet<T> other) {
        // Check if other is a subset of this set
        // other is a subset of this if all elements in other are also in this
        for (T element : other.elements) {
            if (!this.contains(element)) {
                return false;
            }
        }
        return true;
    }
}
