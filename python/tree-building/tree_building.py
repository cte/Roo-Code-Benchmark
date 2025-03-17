class Record:
    def __init__(self, record_id, parent_id):
        self.record_id = record_id
        self.parent_id = parent_id


class Node:
    def __init__(self, node_id):
        self.node_id = node_id
        self.children = []


def BuildTree(records):
    # Handle empty input
    if not records:
        return None
    
    # Sort records by ID for easier processing
    records.sort(key=lambda x: x.record_id)
    
    # Validate records
    if records[0].record_id != 0:
        raise ValueError("Record id is invalid or out of order.")
    
    if records[-1].record_id != len(records) - 1:
        raise ValueError("Record id is invalid or out of order.")
    
    # Create nodes for each record
    nodes = {}
    for record in records:
        # Validate record
        if record.record_id != record.parent_id and record.record_id < record.parent_id:
            raise ValueError("Node parent_id should be smaller than it's record_id.")
        
        if record.record_id == record.parent_id and record.record_id != 0:
            raise ValueError("Only root should have equal record and parent id.")
        
        # Create node
        nodes[record.record_id] = Node(record.record_id)
    
    # Build tree by connecting nodes
    for record in records:
        # Skip root node (it has no parent)
        if record.record_id == 0:
            continue
        
        # Connect node to its parent
        parent = nodes[record.parent_id]
        parent.children.append(nodes[record.record_id])
    
    return nodes[0] if nodes else None
