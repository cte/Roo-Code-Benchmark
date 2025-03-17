#!/usr/bin/env node

// The above line is a shebang. On Unix-like operating systems, or environments,
// this will allow the script to be run by node, and thus turn this JavaScript
// file into an executable. In other words, to execute this file, you may run
// the following from your terminal:
//
// ./grep.js args
//
// If you don't have a Unix-like operating system or environment, for example
// Windows without WSL, you can use the following inside a window terminal,
// such as cmd.exe:
//
// node grep.js args
//
// Read more about shebangs here: https://en.wikipedia.org/wiki/Shebang_(Unix)

const fs = require('fs');
const path = require('path');

/**
 * Reads the given file and returns lines.
 *
 * This function works regardless of POSIX (LF) or windows (CRLF) encoding.
 *
 * @param {string} file path to file
 * @returns {string[]} the lines
 */
function readLines(file) {
  const data = fs.readFileSync(path.resolve(file), { encoding: 'utf-8' });
  return data.split(/\r?\n/);
}

const VALID_OPTIONS = [
  'n', // add line numbers
  'l', // print file names where pattern is found
  'i', // ignore case
  'v', // reverse files results
  'x', // match entire line
];

const ARGS = process.argv;

// Parse command line arguments
const args = ARGS.slice(2); // Remove 'node' and script path
const flags = [];
const files = [];
let pattern = '';

// Extract flags and pattern from arguments
for (let i = 0; i < args.length; i++) {
  const arg = args[i];
  if (arg.startsWith('-') && arg.length > 1) {
    // This is a flag
    const options = arg.slice(1).split('');
    for (const option of options) {
      if (VALID_OPTIONS.includes(option)) {
        flags.push(option);
      }
    }
  } else if (pattern === '') {
    // This is the pattern
    pattern = arg;
  } else {
    // This is a file
    files.push(arg);
  }
}

// Create options object for easier access
const options = {
  lineNumbers: flags.includes('n'),
  fileNamesOnly: flags.includes('l'),
  caseInsensitive: flags.includes('i'),
  invertMatch: flags.includes('v'),
  exactMatch: flags.includes('x'),
};

// Process each file
const results = [];

for (const file of files) {
  const lines = readLines(file);
  const matchingLines = [];
  
  for (let i = 0; i < lines.length; i++) {
    const line = lines[i];
    let isMatch = false;
    
    if (options.exactMatch) {
      // Match entire line
      if (options.caseInsensitive) {
        isMatch = line.toLowerCase() === pattern.toLowerCase();
      } else {
        isMatch = line === pattern;
      }
    } else {
      // Match substring
      if (options.caseInsensitive) {
        isMatch = line.toLowerCase().includes(pattern.toLowerCase());
      } else {
        isMatch = line.includes(pattern);
      }
    }
    
    // Apply invert match if needed
    if (options.invertMatch) {
      isMatch = !isMatch;
    }
    
    if (isMatch) {
      matchingLines.push({
        lineNumber: i + 1, // Line numbers are 1-based
        content: line
      });
    }
  }
  
  if (matchingLines.length > 0) {
    if (options.fileNamesOnly) {
      // Only output file names
      results.push(file);
    } else {
      // Output matching lines
      for (const match of matchingLines) {
        let output = '';
        
        // Add file name for multiple files
        if (files.length > 1) {
          output += `${file}:`;
        }
        
        // Add line number if requested
        if (options.lineNumbers) {
          output += `${match.lineNumber}:`;
        }
        
        // Add line content
        output += match.content;
        
        results.push(output);
      }
    }
  }
}

// Output results
if (results.length > 0) {
  console.log(results.join('\n'));
}
