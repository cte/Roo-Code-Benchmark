use anyhow::Error;
use std::fs;
use std::path::Path;

/// While using `&[&str]` to handle flags is convenient for exercise purposes,
/// and resembles the output of [`std::env::args`], in real-world projects it is
/// both more convenient and more idiomatic to contain runtime configuration in
/// a dedicated struct. Therefore, we suggest that you do so in this exercise.
///
/// [`std::env::args`]: https://doc.rust-lang.org/std/env/fn.args.html
#[derive(Debug)]
pub struct Flags {
    line_numbers: bool,    // -n flag
    file_names_only: bool, // -l flag
    case_insensitive: bool, // -i flag
    invert_match: bool,    // -v flag
    match_entire_line: bool, // -x flag
}

impl Flags {
    pub fn new(flags: &[&str]) -> Self {
        let mut line_numbers = false;
        let mut file_names_only = false;
        let mut case_insensitive = false;
        let mut invert_match = false;
        let mut match_entire_line = false;

        for flag in flags {
            match *flag {
                "-n" => line_numbers = true,
                "-l" => file_names_only = true,
                "-i" => case_insensitive = true,
                "-v" => invert_match = true,
                "-x" => match_entire_line = true,
                _ => (), // Ignore unknown flags
            }
        }

        Flags {
            line_numbers,
            file_names_only,
            case_insensitive,
            invert_match,
            match_entire_line,
        }
    }
}

pub fn grep(pattern: &str, flags: &Flags, files: &[&str]) -> Result<Vec<String>, Error> {
    let mut results = Vec::new();
    let multiple_files = files.len() > 1;

    for &file_path in files {
        // Check if file exists
        if !Path::new(file_path).exists() {
            return Err(Error::msg(format!("File not found: {}", file_path)));
        }

        let content = fs::read_to_string(file_path)?;
        let lines: Vec<&str> = content.lines().collect();
        let mut file_has_match = false;

        for (line_idx, line) in lines.iter().enumerate() {
            let line_number = line_idx + 1; // Line numbers start at 1
            let is_match = match (flags.case_insensitive, flags.match_entire_line) {
                (true, true) => line.to_lowercase() == pattern.to_lowercase(),
                (true, false) => line.to_lowercase().contains(&pattern.to_lowercase()),
                (false, true) => *line == pattern,
                (false, false) => line.contains(pattern),
            };

            let matches = if flags.invert_match { !is_match } else { is_match };

            if matches {
                file_has_match = true;

                // If we only want file names, we don't need to process further lines
                if flags.file_names_only {
                    break;
                }

                let mut result_line = String::new();

                // Add file name prefix for multiple files
                if multiple_files {
                    result_line.push_str(file_path);
                    result_line.push(':');
                }

                // Add line number if requested
                if flags.line_numbers {
                    result_line.push_str(&line_number.to_string());
                    result_line.push(':');
                }

                // Add the actual line content
                result_line.push_str(line);
                results.push(result_line);
            }
        }

        // If we only want file names and there was a match, add the file name
        if flags.file_names_only && file_has_match {
            results.push(file_path.to_string());
        }
    }

    Ok(results)
}
