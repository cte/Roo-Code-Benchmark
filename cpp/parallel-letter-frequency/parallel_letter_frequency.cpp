#include "parallel_letter_frequency.h"
#include <algorithm>
#include <cctype>
#include <thread>
#include <mutex>
#include <vector>

namespace parallel_letter_frequency {

// Helper function to count letter frequencies in a subset of texts
void count_frequencies(const std::vector<std::string_view>& texts, 
                      size_t start, size_t end, 
                      std::unordered_map<char, int>& local_freqs) {
    for (size_t i = start; i < end; ++i) {
        for (char c : texts[i]) {
            // Convert to lowercase
            if (std::isalpha(c)) {
                local_freqs[std::tolower(c)]++;
            }
        }
    }
}

std::unordered_map<char, int> frequency(const std::vector<std::string_view>& texts) {
    std::unordered_map<char, int> result;
    
    // If no texts, return empty map
    if (texts.empty()) {
        return result;
    }
    
    // Determine number of threads to use
    // Use hardware concurrency or a reasonable default
    unsigned int num_threads = std::thread::hardware_concurrency();
    if (num_threads == 0) {
        num_threads = 4; // Default to 4 threads if hardware_concurrency is not available
    }
    
    // Limit threads to the number of texts
    num_threads = std::min(num_threads, static_cast<unsigned int>(texts.size()));
    
    // Create thread pool and local frequency maps
    std::vector<std::thread> threads;
    std::vector<std::unordered_map<char, int>> thread_results(num_threads);
    
    // Calculate chunk size for each thread
    size_t chunk_size = texts.size() / num_threads;
    size_t remainder = texts.size() % num_threads;
    
    // Launch threads
    size_t start = 0;
    for (unsigned int i = 0; i < num_threads; ++i) {
        size_t end = start + chunk_size + (i < remainder ? 1 : 0);
        threads.emplace_back(count_frequencies, std::ref(texts), start, end, std::ref(thread_results[i]));
        start = end;
    }
    
    // Wait for all threads to complete
    for (auto& thread : threads) {
        thread.join();
    }
    
    // Merge results from all threads
    for (const auto& local_freqs : thread_results) {
        for (const auto& [letter, count] : local_freqs) {
            result[letter] += count;
        }
    }
    
    return result;
}

}
