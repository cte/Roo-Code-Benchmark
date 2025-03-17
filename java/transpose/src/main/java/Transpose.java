public class Transpose {
    public String transpose(String toTranspose) {
        // Handle empty string case
        if (toTranspose.isEmpty()) {
            return "";
        }

        // Split the input into rows
        String[] rows = toTranspose.split("\n");
        
        // Find the maximum row length
        int maxRowLength = 0;
        for (String row : rows) {
            maxRowLength = Math.max(maxRowLength, row.length());
        }
        
        // Build the transposed output
        StringBuilder result = new StringBuilder();
        
        for (int col = 0; col < maxRowLength; col++) {
            for (int row = 0; row < rows.length; row++) {
                // If this column exists in the current row, add the character
                // Otherwise, add a space if there are non-space characters to the right
                if (col < rows[row].length()) {
                    result.append(rows[row].charAt(col));
                } else {
                    // Check if we need to add a space (if there are characters to the right in rows below)
                    boolean needSpace = false;
                    for (int checkRow = row + 1; checkRow < rows.length; checkRow++) {
                        if (col < rows[checkRow].length()) {
                            needSpace = true;
                            break;
                        }
                    }
                    if (needSpace) {
                        result.append(' ');
                    }
                }
            }
            
            // Add a newline if this is not the last column
            if (col < maxRowLength - 1) {
                result.append('\n');
            }
        }
        
        return result.toString();
    }
}
