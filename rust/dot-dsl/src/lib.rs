pub mod graph {
    use std::collections::HashMap;

    #[derive(Clone, PartialEq, Debug)]
    pub struct Graph {
        pub nodes: Vec<graph_items::node::Node>,
        pub edges: Vec<graph_items::edge::Edge>,
        pub attrs: HashMap<String, String>,
    }

    impl Graph {
        pub fn new() -> Self {
            Graph {
                nodes: Vec::new(),
                edges: Vec::new(),
                attrs: HashMap::new(),
            }
        }

        pub fn with_nodes(self, nodes: &[graph_items::node::Node]) -> Self {
            let mut new_graph = self;
            new_graph.nodes = nodes.to_vec();
            new_graph
        }

        pub fn with_edges(self, edges: &[graph_items::edge::Edge]) -> Self {
            let mut new_graph = self;
            new_graph.edges = edges.to_vec();
            new_graph
        }

        pub fn with_attrs(self, attrs: &[(&str, &str)]) -> Self {
            let mut new_graph = self;
            for &(key, value) in attrs {
                new_graph.attrs.insert(key.to_string(), value.to_string());
            }
            new_graph
        }

        pub fn node(&self, name: &str) -> Option<&graph_items::node::Node> {
            self.nodes.iter().find(|node| node.name == name)
        }
    }

    pub mod graph_items {
        pub mod node {
            use std::collections::HashMap;

            #[derive(Clone, PartialEq, Debug)]
            pub struct Node {
                pub name: String,
                attrs: HashMap<String, String>,
            }

            impl Node {
                pub fn new(name: &str) -> Self {
                    Node {
                        name: name.to_string(),
                        attrs: HashMap::new(),
                    }
                }

                pub fn with_attrs(self, attrs: &[(&str, &str)]) -> Self {
                    let mut new_node = self;
                    for &(key, value) in attrs {
                        new_node.attrs.insert(key.to_string(), value.to_string());
                    }
                    new_node
                }

                pub fn attr(&self, key: &str) -> Option<&str> {
                    self.attrs.get(key).map(|s| s.as_str())
                }
            }
        }

        pub mod edge {
            use std::collections::HashMap;

            #[derive(Clone, PartialEq, Debug)]
            pub struct Edge {
                pub from: String,
                pub to: String,
                attrs: HashMap<String, String>,
            }

            impl Edge {
                pub fn new(from: &str, to: &str) -> Self {
                    Edge {
                        from: from.to_string(),
                        to: to.to_string(),
                        attrs: HashMap::new(),
                    }
                }

                pub fn with_attrs(self, attrs: &[(&str, &str)]) -> Self {
                    let mut new_edge = self;
                    for &(key, value) in attrs {
                        new_edge.attrs.insert(key.to_string(), value.to_string());
                    }
                    new_edge
                }

                pub fn attr(&self, key: &str) -> Option<&str> {
                    self.attrs.get(key).map(|s| s.as_str())
                }
            }
        }
    }
}
