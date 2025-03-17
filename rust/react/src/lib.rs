use std::cell::RefCell;
use std::rc::Rc;

/// `InputCellId` is a unique identifier for an input cell.
#[derive(Clone, Copy, Debug, PartialEq, Eq)]
pub struct InputCellId(usize);
/// `ComputeCellId` is a unique identifier for a compute cell.
/// Values of type `InputCellId` and `ComputeCellId` should not be mutually assignable,
/// demonstrated by the following tests:
///
/// ```compile_fail
/// let mut r = react::Reactor::new();
/// let input: react::ComputeCellId = r.create_input(111);
/// ```
///
/// ```compile_fail
/// let mut r = react::Reactor::new();
/// let input = r.create_input(111);
/// let compute: react::InputCellId = r.create_compute(&[react::CellId::Input(input)], |_| 222).unwrap();
/// ```
#[derive(Clone, Copy, Debug, PartialEq, Eq)]
pub struct ComputeCellId(usize);
#[derive(Clone, Copy, Debug, PartialEq, Eq)]
pub struct CallbackId(usize);

#[derive(Clone, Copy, Debug, PartialEq, Eq)]
pub enum CellId {
    Input(InputCellId),
    Compute(ComputeCellId),
}

#[derive(Debug, PartialEq, Eq)]
pub enum RemoveCallbackError {
    NonexistentCell,
    NonexistentCallback,
}

struct InputCell<T> {
    value: T,
}

struct ComputeCell<T> {
    dependencies: Vec<CellId>,
    compute_func: Box<dyn Fn(&[T]) -> T>,
    last_value: T,
    callbacks: Vec<Option<(usize, Rc<RefCell<dyn FnMut(T)>>)>>,
}

pub struct Reactor<T> {
    input_cells: Vec<InputCell<T>>,
    compute_cells: Vec<ComputeCell<T>>,
    next_callback_id: usize,
}

// You are guaranteed that Reactor will only be tested against types that are Copy + PartialEq.
impl<T: Copy + PartialEq> Reactor<T> {
    pub fn new() -> Self {
        Reactor {
            input_cells: Vec::new(),
            compute_cells: Vec::new(),
            next_callback_id: 0,
        }
    }

    // Creates an input cell with the specified initial value, returning its ID.
    pub fn create_input(&mut self, initial: T) -> InputCellId {
        let id = InputCellId(self.input_cells.len());
        self.input_cells.push(InputCell { value: initial });
        id
    }

    // Creates a compute cell with the specified dependencies and compute function.
    // The compute function is expected to take in its arguments in the same order as specified in
    // `dependencies`.
    // You do not need to reject compute functions that expect more arguments than there are
    // dependencies (how would you check for this, anyway?).
    //
    // If any dependency doesn't exist, returns an Err with that nonexistent dependency.
    // (If multiple dependencies do not exist, exactly which one is returned is not defined and
    // will not be tested)
    //
    // Notice that there is no way to *remove* a cell.
    // This means that you may assume, without checking, that if the dependencies exist at creation
    // time they will continue to exist as long as the Reactor exists.
    pub fn create_compute<F: Fn(&[T]) -> T + 'static>(
        &mut self,
        dependencies: &[CellId],
        compute_func: F,
    ) -> Result<ComputeCellId, CellId> {
        // Check if all dependencies exist
        for &dep in dependencies {
            match dep {
                CellId::Input(InputCellId(id)) => {
                    if id >= self.input_cells.len() {
                        return Err(dep);
                    }
                }
                CellId::Compute(ComputeCellId(id)) => {
                    if id >= self.compute_cells.len() {
                        return Err(dep);
                    }
                }
            }
        }

        // Compute the initial value
        let values = self.get_dependency_values(dependencies);
        let initial_value = compute_func(&values);

        // Create the compute cell
        let id = ComputeCellId(self.compute_cells.len());
        self.compute_cells.push(ComputeCell {
            dependencies: dependencies.to_vec(),
            compute_func: Box::new(compute_func),
            last_value: initial_value,
            callbacks: Vec::new(),
        });

        Ok(id)
    }

    // Helper function to get the values of dependencies
    fn get_dependency_values(&self, dependencies: &[CellId]) -> Vec<T> {
        dependencies
            .iter()
            .map(|&dep| self.value(dep).unwrap())
            .collect()
    }

    // Retrieves the current value of the cell, or None if the cell does not exist.
    //
    // You may wonder whether it is possible to implement `get(&self, id: CellId) -> Option<&Cell>`
    // and have a `value(&self)` method on `Cell`.
    //
    // It turns out this introduces a significant amount of extra complexity to this exercise.
    // We chose not to cover this here, since this exercise is probably enough work as-is.
    pub fn value(&self, id: CellId) -> Option<T> {
        match id {
            CellId::Input(InputCellId(id)) => self.input_cells.get(id).map(|cell| cell.value),
            CellId::Compute(ComputeCellId(id)) => {
                self.compute_cells.get(id).map(|cell| cell.last_value)
            }
        }
    }

    // Sets the value of the specified input cell.
    //
    // Returns false if the cell does not exist.
    //
    // Similarly, you may wonder about `get_mut(&mut self, id: CellId) -> Option<&mut Cell>`, with
    // a `set_value(&mut self, new_value: T)` method on `Cell`.
    //
    // As before, that turned out to add too much extra complexity.
    pub fn set_value(&mut self, id: InputCellId, new_value: T) -> bool {
        let InputCellId(input_id) = id;
        if input_id >= self.input_cells.len() {
            return false;
        }

        // Set the new value
        let old_value = self.input_cells[input_id].value;
        self.input_cells[input_id].value = new_value;

        // If the value hasn't changed, no need to propagate
        if old_value == new_value {
            return true;
        }

        // Propagate changes to compute cells
        self.propagate_changes();

        true
    }

    // Helper function to propagate changes to compute cells
    fn propagate_changes(&mut self) {
        // Keep track of which compute cells have changed
        let mut changed_cells = Vec::new();

        // For each compute cell, check if its value has changed
        for i in 0..self.compute_cells.len() {
            let values = self.get_dependency_values(&self.compute_cells[i].dependencies);
            let new_value = (self.compute_cells[i].compute_func)(&values);
            let old_value = self.compute_cells[i].last_value;

            if new_value != old_value {
                self.compute_cells[i].last_value = new_value;
                changed_cells.push((i, new_value));
            }
        }

        // Call callbacks for changed cells
        for (cell_id, new_value) in changed_cells {
            let cell = &mut self.compute_cells[cell_id];
            for callback in &mut cell.callbacks {
                if let Some((_, cb)) = callback {
                    let mut cb = cb.borrow_mut();
                    cb(new_value);
                }
            }
        }
    }

    // Adds a callback to the specified compute cell.
    //
    // Returns the ID of the just-added callback, or None if the cell doesn't exist.
    //
    // Callbacks on input cells will not be tested.
    //
    // The semantics of callbacks (as will be tested):
    // For a single set_value call, each compute cell's callbacks should each be called:
    // * Zero times if the compute cell's value did not change as a result of the set_value call.
    // * Exactly once if the compute cell's value changed as a result of the set_value call.
    //   The value passed to the callback should be the final value of the compute cell after the
    //   set_value call.
    pub fn add_callback<F: FnMut(T) + 'static>(
        &mut self,
        id: ComputeCellId,
        callback: F,
    ) -> Option<CallbackId> {
        let ComputeCellId(compute_id) = id;
        if compute_id >= self.compute_cells.len() {
            return None;
        }

        let callback_id = self.next_callback_id;
        self.next_callback_id += 1;

        self.compute_cells[compute_id]
            .callbacks
            .push(Some((callback_id, Rc::new(RefCell::new(callback)))));

        Some(CallbackId(callback_id))
    }

    // Removes the specified callback, using an ID returned from add_callback.
    //
    // Returns an Err if either the cell or callback does not exist.
    //
    // A removed callback should no longer be called.
    pub fn remove_callback(
        &mut self,
        cell: ComputeCellId,
        callback: CallbackId,
    ) -> Result<(), RemoveCallbackError> {
        let ComputeCellId(compute_id) = cell;
        if compute_id >= self.compute_cells.len() {
            return Err(RemoveCallbackError::NonexistentCell);
        }

        let CallbackId(callback_id) = callback;
        
        // Find the callback in the compute cell
        let compute_cell = &mut self.compute_cells[compute_id];
        
        for i in 0..compute_cell.callbacks.len() {
            if let Some((id, _)) = &compute_cell.callbacks[i] {
                if *id == callback_id {
                    compute_cell.callbacks[i] = None;
                    return Ok(());
                }
            }
        }
        
        Err(RemoveCallbackError::NonexistentCallback)
    }
}
