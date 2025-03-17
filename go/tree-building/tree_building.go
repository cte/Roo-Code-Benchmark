package tree

import (
	"errors"
	"sort"
)

type Record struct {
	ID     int
	Parent int
}

type Node struct {
	ID       int
	Children []*Node
}

func Build(records []Record) (*Node, error) {
	if len(records) == 0 {
		return nil, nil
	}

	// Sort records by ID for easier validation and building
	sort.Slice(records, func(i, j int) bool {
		return records[i].ID < records[j].ID
	})

	// Validate records
	if err := validateRecords(records); err != nil {
		return nil, err
	}

	// Create nodes map
	nodes := make(map[int]*Node, len(records))
	for _, record := range records {
		nodes[record.ID] = &Node{ID: record.ID}
	}

	// Build the tree by connecting nodes
	for _, record := range records {
		// Skip the root node (ID 0, Parent 0)
		if record.ID == 0 {
			continue
		}

		parent := nodes[record.Parent]
		parent.Children = append(parent.Children, nodes[record.ID])
	}

	// Sort children by ID for each node
	for _, node := range nodes {
		if len(node.Children) > 0 {
			sort.Slice(node.Children, func(i, j int) bool {
				return node.Children[i].ID < node.Children[j].ID
			})
		}
	}

	return nodes[0], nil
}

func validateRecords(records []Record) error {
	// Check if records are continuous and valid
	if records[0].ID != 0 {
		return errors.New("no root node")
	}

	if records[0].Parent != 0 {
		return errors.New("root node has parent")
	}

	// Check for duplicate IDs, non-continuous IDs, and parent-child relationships
	for i, record := range records {
		// Check if IDs are continuous
		if record.ID != i {
			return errors.New("non-continuous node IDs")
		}

		// Check parent-child relationship
		if record.ID != 0 && record.Parent >= record.ID {
			return errors.New("parent ID must be less than child ID")
		}

		// Check for cycles
		if record.ID == record.Parent && record.ID != 0 {
			return errors.New("cycle detected")
		}
	}

	return nil
}
