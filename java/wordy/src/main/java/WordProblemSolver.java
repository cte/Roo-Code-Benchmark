import java.util.regex.Matcher;
import java.util.regex.Pattern;

class WordProblemSolver {
    private static final String ERROR_MESSAGE = "I'm sorry, I don't understand the question!";
    private static final Pattern QUESTION_PATTERN = Pattern.compile("What is (.+)\\?");
    private static final Pattern OPERATION_PATTERN = Pattern.compile("([-+]?\\d+)\\s+(plus|minus|multiplied by|divided by)\\s+([-+]?\\d+)(.*)");

    int solve(final String wordProblem) {
        // Extract the mathematical expression from the word problem
        Matcher questionMatcher = QUESTION_PATTERN.matcher(wordProblem);
        if (!questionMatcher.matches()) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }

        String expression = questionMatcher.group(1).trim();
        
        // Handle the case of just a number
        try {
            if (!expression.contains(" ")) {
                return Integer.parseInt(expression);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }

        // Process the expression from left to right
        return evaluateExpression(expression);
    }

    private int evaluateExpression(String expression) {
        Matcher operationMatcher = OPERATION_PATTERN.matcher(expression);
        
        if (!operationMatcher.matches()) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
        
        int leftOperand = Integer.parseInt(operationMatcher.group(1));
        String operator = operationMatcher.group(2);
        int rightOperand = Integer.parseInt(operationMatcher.group(3));
        String remainingExpression = operationMatcher.group(4).trim();
        
        int result = performOperation(leftOperand, operator, rightOperand);
        
        // If there are more operations, continue evaluating
        if (!remainingExpression.isEmpty()) {
            return evaluateExpression(result + " " + remainingExpression);
        }
        
        return result;
    }
    
    private int performOperation(int leftOperand, String operator, int rightOperand) {
        switch (operator) {
            case "plus":
                return leftOperand + rightOperand;
            case "minus":
                return leftOperand - rightOperand;
            case "multiplied by":
                return leftOperand * rightOperand;
            case "divided by":
                return leftOperand / rightOperand;
            default:
                throw new IllegalArgumentException(ERROR_MESSAGE);
        }
    }
}
