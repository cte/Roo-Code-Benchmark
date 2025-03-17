class InputCell:
    def __init__(self, initial_value):
        self._value = initial_value
        self._dependents = []

    @property
    def value(self):
        return self._value

    @value.setter
    def value(self, new_value):
        if self._value == new_value:
            return
        self._value = new_value
        
        # Create a set to track cells that have been updated in this propagation
        updated = set()
        
        # Create a list to track cells that need to be updated
        to_update = list(self._dependents)
        
        # Process cells in topological order
        while to_update:
            cell = to_update.pop(0)
            
            # Skip if already processed
            if cell in updated:
                continue
                
            # Check if all dependencies have been processed
            dependencies_processed = True
            for input_cell in cell._inputs:
                if input_cell not in updated and input_cell not in [self]:
                    dependencies_processed = False
                    break
                    
            if not dependencies_processed:
                # Put back at the end of the queue
                to_update.append(cell)
                continue
                
            # Update the cell
            old_value = cell._current_value
            cell._compute_value()
            updated.add(cell)
            
            # Add dependents to the update queue
            for dependent in cell._dependents:
                if dependent not in updated and dependent not in to_update:
                    to_update.append(dependent)
                    
            # Call callbacks if value changed
            if cell._current_value != old_value:
                for callback in list(cell._callbacks):
                    callback(cell._current_value)

    def add_dependent(self, dependent):
        if dependent not in self._dependents:
            self._dependents.append(dependent)


class ComputeCell:
    def __init__(self, inputs, compute_function):
        self._inputs = inputs
        self._compute_function = compute_function
        self._callbacks = []
        self._dependents = []
        self._current_value = None
        
        # Register as a dependent of each input
        for input_cell in inputs:
            input_cell.add_dependent(self)
        
        # Calculate initial value
        self._compute_value()

    @property
    def value(self):
        return self._current_value

    def _compute_value(self):
        """Calculate the current value based on inputs"""
        input_values = [input_cell.value for input_cell in self._inputs]
        self._current_value = self._compute_function(input_values)
        return self._current_value

    def update(self):
        """Update this cell (used when directly called)"""
        old_value = self._current_value
        self._compute_value()
        
        # Call callbacks if value changed
        if self._current_value != old_value:
            for callback in list(self._callbacks):
                callback(self._current_value)
                
        # Propagate changes to dependents
        for dependent in list(self._dependents):
            dependent.update()

    def add_dependent(self, dependent):
        if dependent not in self._dependents:
            self._dependents.append(dependent)

    def add_callback(self, callback):
        if callback not in self._callbacks:
            self._callbacks.append(callback)

    def remove_callback(self, callback):
        if callback in self._callbacks:
            self._callbacks.remove(callback)