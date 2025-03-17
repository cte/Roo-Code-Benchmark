package react

// Define reactor, cell and canceler types here.
// These types will implement the Reactor, Cell and Canceler interfaces, respectively.

type reactor struct {
	nextCallbackID int
}

type inputCell struct {
	value int
	reactor *reactor
	dependents []*computeCell
}

type computeCell struct {
	value int
	reactor *reactor
	dependencies []Cell
	compute func([]int) int
	callbacks map[int]func(int)
	dependents []*computeCell
}

type canceler struct {
	cell *computeCell
	id int
}

func (c *canceler) Cancel() {
	delete(c.cell.callbacks, c.id)
}

func (c *inputCell) Value() int {
	return c.value
}

func (c *inputCell) SetValue(value int) {
	if c.value == value {
		return // No change, no need to propagate
	}
	
	c.value = value
	
	// Collect all compute cells that need to be updated
	visited := make(map[*computeCell]bool)
	toUpdate := make([]*computeCell, 0, len(c.dependents))
	
	for _, dep := range c.dependents {
		if !visited[dep] {
			visited[dep] = true
			toUpdate = append(toUpdate, dep)
		}
	}
	
	// Update all compute cells in topological order
	for len(toUpdate) > 0 {
		current := toUpdate[0]
		toUpdate = toUpdate[1:]
		
		oldValue := current.value
		
		// Get values from dependencies
		depValues := make([]int, len(current.dependencies))
		for i, dep := range current.dependencies {
			depValues[i] = dep.Value()
		}
		
		// Compute new value
		newValue := current.compute(depValues)
		
		// Only propagate changes if the value actually changed
		if oldValue != newValue {
			current.value = newValue
			
			// Add dependents to update queue
			for _, dep := range current.dependents {
				if !visited[dep] {
					visited[dep] = true
					toUpdate = append(toUpdate, dep)
				}
			}
			
			// Call callbacks
			for _, callback := range current.callbacks {
				callback(newValue)
			}
		}
	}
}

func (c *computeCell) Value() int {
	return c.value
}

func (c *computeCell) AddCallback(callback func(int)) Canceler {
	id := c.reactor.nextCallbackID
	c.reactor.nextCallbackID++
	
	c.callbacks[id] = callback
	
	return &canceler{
		cell: c,
		id: id,
	}
}

func New() Reactor {
	return &reactor{
		nextCallbackID: 0,
	}
}

func (r *reactor) CreateInput(initial int) InputCell {
	return &inputCell{
		value: initial,
		reactor: r,
		dependents: make([]*computeCell, 0),
	}
}

func (r *reactor) CreateCompute1(dep Cell, compute func(int) int) ComputeCell {
	// Create a compute cell with one dependency
	cell := &computeCell{
		reactor: r,
		dependencies: []Cell{dep},
		compute: func(values []int) int {
			return compute(values[0])
		},
		callbacks: make(map[int]func(int)),
		dependents: make([]*computeCell, 0),
	}
	
	// Initialize the value
	cell.value = compute(dep.Value())
	
	// Add this cell as a dependent of its dependency
	switch d := dep.(type) {
	case *inputCell:
		d.dependents = append(d.dependents, cell)
	case *computeCell:
		d.dependents = append(d.dependents, cell)
	}
	
	return cell
}

func (r *reactor) CreateCompute2(dep1, dep2 Cell, compute func(int, int) int) ComputeCell {
	// Create a compute cell with two dependencies
	cell := &computeCell{
		reactor: r,
		dependencies: []Cell{dep1, dep2},
		compute: func(values []int) int {
			return compute(values[0], values[1])
		},
		callbacks: make(map[int]func(int)),
		dependents: make([]*computeCell, 0),
	}
	
	// Initialize the value
	cell.value = compute(dep1.Value(), dep2.Value())
	
	// Add this cell as a dependent of its dependencies
	switch d := dep1.(type) {
	case *inputCell:
		d.dependents = append(d.dependents, cell)
	case *computeCell:
		d.dependents = append(d.dependents, cell)
	}
	
	switch d := dep2.(type) {
	case *inputCell:
		d.dependents = append(d.dependents, cell)
	case *computeCell:
		d.dependents = append(d.dependents, cell)
	}
	
	return cell
}
