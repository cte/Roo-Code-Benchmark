import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public class React {

    public static class Cell<T> {
        protected T value;
        protected Set<ComputeCell<?>> dependents = new HashSet<>();
        
        public T getValue() {
            return value;
        }
        
        protected void addDependent(ComputeCell<?> cell) {
            dependents.add(cell);
        }
    }

    public static class InputCell<T> extends Cell<T> {
        public InputCell(T initialValue) {
            this.value = initialValue;
        }
        
        public void setValue(T newValue) {
            if ((value == null && newValue != null) || 
                (value != null && !value.equals(newValue))) {
                this.value = newValue;
                
                // Collect all cells that need to be updated
                Set<ComputeCell<?>> allDependents = new HashSet<>();
                Set<ComputeCell<?>> visited = new HashSet<>();
                
                // First pass: collect all dependents and update their values
                for (ComputeCell<?> dependent : dependents) {
                    collectAllDependents(dependent, allDependents, visited);
                }
                
                // Second pass: update all dependents in topological order
                List<ComputeCell<?>> orderedDependents = topologicalSort(allDependents);
                
                // Store old values for comparison
                Map<ComputeCell<?>, Object> oldValues = new HashMap<>();
                for (ComputeCell<?> cell : orderedDependents) {
                    oldValues.put(cell, cell.getValue());
                }
                
                // Update all cells
                for (ComputeCell<?> cell : orderedDependents) {
                    cell.recompute();
                }
                
                // Notify callbacks for cells whose values changed
                for (ComputeCell<?> cell : orderedDependents) {
                    Object oldValue = oldValues.get(cell);
                    Object currentValue = cell.getValue();
                    
                    boolean valueChanged = (oldValue == null && currentValue != null) ||
                                          (oldValue != null && !oldValue.equals(currentValue));
                    
                    if (valueChanged) {
                        ((ComputeCell<T>)cell).notifyCallbacks();
                    }
                }
            }
        }
        
        private void collectAllDependents(ComputeCell<?> cell, Set<ComputeCell<?>> allDependents, Set<ComputeCell<?>> visited) {
            if (visited.contains(cell)) {
                return;
            }
            
            visited.add(cell);
            allDependents.add(cell);
            
            for (ComputeCell<?> dependent : cell.dependents) {
                collectAllDependents(dependent, allDependents, visited);
            }
        }
        
        private List<ComputeCell<?>> topologicalSort(Set<ComputeCell<?>> cells) {
            List<ComputeCell<?>> result = new ArrayList<>();
            Set<ComputeCell<?>> visited = new HashSet<>();
            Set<ComputeCell<?>> temp = new HashSet<>();
            
            for (ComputeCell<?> cell : cells) {
                if (!visited.contains(cell)) {
                    topologicalSortUtil(cell, visited, temp, result);
                }
            }
            
            return result;
        }
        
        private void topologicalSortUtil(ComputeCell<?> cell, Set<ComputeCell<?>> visited, 
                                        Set<ComputeCell<?>> temp, List<ComputeCell<?>> result) {
            if (temp.contains(cell)) {
                // Cycle detected, but we'll ignore it for this exercise
                return;
            }
            
            if (!visited.contains(cell)) {
                temp.add(cell);
                
                for (ComputeCell<?> dependent : cell.dependents) {
                    if (dependent instanceof ComputeCell) {
                        topologicalSortUtil(dependent, visited, temp, result);
                    }
                }
                
                visited.add(cell);
                temp.remove(cell);
                result.add(0, cell); // Add at the beginning for correct order
            }
        }
    }

    public static class ComputeCell<T> extends Cell<T> {
        private final Function<List<T>, T> computeFunction;
        private final List<Cell<T>> dependencies;
        private final Set<Consumer<T>> callbacks = new HashSet<>();
        private T lastNotifiedValue;
        
        public ComputeCell(Function<List<T>, T> function, List<Cell<T>> cells) {
            this.computeFunction = function;
            this.dependencies = new ArrayList<>(cells);
            
            // Register this cell as dependent on its dependencies
            for (Cell<T> cell : cells) {
                cell.addDependent(this);
            }
            
            // Compute initial value
            recompute();
            this.lastNotifiedValue = this.value;
        }
        
        public void addCallback(Consumer<T> callback) {
            callbacks.add(callback);
        }
        
        public void removeCallback(Consumer<T> callback) {
            callbacks.remove(callback);
        }
        
        void recompute() {
            List<T> values = new ArrayList<>();
            for (Cell<T> cell : dependencies) {
                values.add(cell.getValue());
            }
            this.value = computeFunction.apply(values);
        }
        
        void notifyCallbacks() {
            if (lastNotifiedValue == null || !lastNotifiedValue.equals(value)) {
                for (Consumer<T> callback : callbacks) {
                    callback.accept(value);
                }
                lastNotifiedValue = value;
            }
        }
    }

    public static <T> InputCell<T> inputCell(T initialValue) {
        return new InputCell<>(initialValue);
    }

    public static <T> ComputeCell<T> computeCell(Function<List<T>, T> function, List<Cell<T>> cells) {
        return new ComputeCell<>(function, cells);
    }
}
