use std::collections::HashMap;
use std::thread;
use std::sync::{Arc, Mutex};

pub fn frequency(input: &[&str], worker_count: usize) -> HashMap<char, usize> {
    // Handle empty input case
    if input.is_empty() {
        return HashMap::new();
    }

    // If only one worker is requested, process sequentially
    if worker_count == 1 {
        return process_chunk(input);
    }

    // Calculate chunk size for each worker
    let chunk_size = (input.len() + worker_count - 1) / worker_count;
    
    // Create a shared result map that all threads can update
    let result = Arc::new(Mutex::new(HashMap::new()));
    let mut handles = vec![];

    // Create threads to process chunks in parallel
    for chunk_index in 0..worker_count {
        let start = chunk_index * chunk_size;
        let end = std::cmp::min(start + chunk_size, input.len());
        
        // Skip if this chunk would be empty
        if start >= input.len() {
            continue;
        }

        // Clone the data for this chunk to avoid lifetime issues
        let chunk_data: Vec<String> = input[start..end].iter().map(|&s| s.to_string()).collect();
        
        // Clone the Arc for this thread
        let thread_result = Arc::clone(&result);
        
        // Spawn a thread to process this chunk
        let handle = thread::spawn(move || {
            // Process the chunk
            let local_freq = process_chunk_owned(&chunk_data);
            
            // Update the shared result
            let mut result_map = thread_result.lock().unwrap();
            for (c, count) in local_freq {
                *result_map.entry(c).or_insert(0) += count;
            }
        });
        
        handles.push(handle);
    }

    // Wait for all threads to complete
    for handle in handles {
        handle.join().unwrap();
    }

    // Return the combined result
    Arc::try_unwrap(result)
        .expect("There should be no more references to the result")
        .into_inner()
        .unwrap()
}

// Helper function to process a chunk of text and return a frequency map
fn process_chunk(texts: &[&str]) -> HashMap<char, usize> {
    let mut map = HashMap::new();

    for line in texts {
        for chr in line.chars().filter(|c| c.is_alphabetic()) {
            if let Some(c) = chr.to_lowercase().next() {
                *map.entry(c).or_insert(0) += 1;
            }
        }
    }

    map
}

// Helper function to process a chunk of owned strings
fn process_chunk_owned(texts: &[String]) -> HashMap<char, usize> {
    let mut map = HashMap::new();

    for line in texts {
        for chr in line.chars().filter(|c| c.is_alphabetic()) {
            if let Some(c) = chr.to_lowercase().next() {
                *map.entry(c).or_insert(0) += 1;
            }
        }
    }

    map
}
