package sublist

// Relation type is defined in relations.go file.

func Sublist(l1, l2 []int) Relation {
	// If both lists are empty, they are equal
	if len(l1) == 0 && len(l2) == 0 {
		return RelationEqual
	}

	// If l1 is empty, it's a sublist of l2
	if len(l1) == 0 {
		return RelationSublist
	}

	// If l2 is empty, l1 is a superlist of l2
	if len(l2) == 0 {
		return RelationSuperlist
	}

	// If both lists have the same length, check if they are equal
	if len(l1) == len(l2) {
		equal := true
		for i := range l1 {
			if l1[i] != l2[i] {
				equal = false
				break
			}
		}
		if equal {
			return RelationEqual
		}
	}

	// Check if l1 is a sublist of l2
	if len(l1) < len(l2) && isSubsequence(l1, l2) {
		return RelationSublist
	}

	// Check if l1 is a superlist of l2
	if len(l1) > len(l2) && isSubsequence(l2, l1) {
		return RelationSuperlist
	}

	// If none of the above, they are unequal
	return RelationUnequal
}

// isSubsequence checks if list1 is a subsequence of list2
func isSubsequence(list1, list2 []int) bool {
	if len(list1) > len(list2) {
		return false
	}

	for i := 0; i <= len(list2)-len(list1); i++ {
		match := true
		for j := 0; j < len(list1); j++ {
			if list1[j] != list2[i+j] {
				match = false
				break
			}
		}
		if match {
			return true
		}
	}
	return false
}
