class BowlingGame {
    private static final int FRAMES_PER_GAME = 10;
    private static final int MAX_PINS = 10;
    private static final int MAX_ROLLS = 21; // Maximum possible rolls in a game (10 frames * 2 + 1 bonus roll for 10th frame)
    
    private int[] rolls = new int[MAX_ROLLS];
    private int currentRoll = 0;

    void roll(int pins) {
        validateRoll(pins);
        rolls[currentRoll++] = pins;
    }

    int score() {
        validateGameComplete();
        
        int score = 0;
        int rollIndex = 0;
        
        for (int frame = 0; frame < FRAMES_PER_GAME; frame++) {
            if (isStrike(rollIndex)) {
                score += MAX_PINS + strikeBonus(rollIndex);
                rollIndex++;
            } else if (isSpare(rollIndex)) {
                score += MAX_PINS + spareBonus(rollIndex);
                rollIndex += 2;
            } else {
                score += sumOfPinsInFrame(rollIndex);
                rollIndex += 2;
            }
        }
        
        return score;
    }
    
    private boolean isStrike(int rollIndex) {
        return rolls[rollIndex] == MAX_PINS;
    }
    
    private boolean isSpare(int rollIndex) {
        return rolls[rollIndex] + rolls[rollIndex + 1] == MAX_PINS;
    }
    
    private int strikeBonus(int rollIndex) {
        return rolls[rollIndex + 1] + rolls[rollIndex + 2];
    }
    
    private int spareBonus(int rollIndex) {
        return rolls[rollIndex + 2];
    }
    
    private int sumOfPinsInFrame(int rollIndex) {
        return rolls[rollIndex] + rolls[rollIndex + 1];
    }
    
    private void validateRoll(int pins) {
        if (pins < 0) {
            throw new IllegalStateException("Negative roll is invalid");
        }
        
        if (pins > MAX_PINS) {
            throw new IllegalStateException("Pin count exceeds pins on the lane");
        }
        
        if (isGameOver()) {
            throw new IllegalStateException("Cannot roll after game is over");
        }
        
        // For regular frames, check if this is the second roll and would exceed 10 pins total
        if (!isFirstRollInFrame() && !isLastFrame() && !isStrikeInPreviousRoll() && rolls[currentRoll - 1] + pins > MAX_PINS) {
            throw new IllegalStateException("Pin count exceeds pins on the lane");
        }
        
        // Special validation for the last frame
        if (isLastFrame()) {
            validateLastFrameRoll(pins);
        }
    }
    
    private boolean isStrikeInPreviousRoll() {
        return currentRoll > 0 && rolls[currentRoll - 1] == MAX_PINS;
    }
    
    private boolean isFirstRollInFrame() {
        // For regular frames, we need to account for strikes which take only one roll
        if (!isLastFrame()) {
            int frameCount = 0;
            int rollCount = 0;
            
            while (rollCount < currentRoll) {
                if (rolls[rollCount] == MAX_PINS) {
                    // Strike takes one roll
                    frameCount++;
                    rollCount++;
                } else {
                    // Non-strike takes two rolls
                    frameCount++;
                    rollCount += 2;
                }
            }
            
            // If we've completed exactly frameCount frames with the current rolls,
            // then the next roll is the first in a new frame
            return rollCount == currentRoll;
        }
        
        // For the last frame, it's the first roll if currentRoll is at the start of the last frame
        int lastFrameStart = getLastFrameStartIndex();
        return currentRoll == lastFrameStart;
    }
    
    private int getLastFrameStartIndex() {
        // Calculate the start index of the 10th frame
        int rollIndex = 0;
        for (int frame = 0; frame < FRAMES_PER_GAME - 1; frame++) {
            if (isStrike(rollIndex)) {
                rollIndex++;
            } else {
                rollIndex += 2;
            }
        }
        return rollIndex;
    }
    
    private boolean isLastFrame() {
        int lastFrameStart = getLastFrameStartIndex();
        return currentRoll >= lastFrameStart && currentRoll < lastFrameStart + 3;
    }
    
    private void validateLastFrameRoll(int pins) {
        int lastFrameStart = getLastFrameStartIndex();
        
        // First roll in last frame - no special validation needed
        if (currentRoll == lastFrameStart) {
            return;
        }
        
        // Second roll in last frame
        if (currentRoll == lastFrameStart + 1) {
            // If first roll was not a strike, then the sum cannot exceed 10
            if (rolls[lastFrameStart] != MAX_PINS && rolls[lastFrameStart] + pins > MAX_PINS) {
                throw new IllegalStateException("Pin count exceeds pins on the lane");
            }
            return;
        }
        
        // Third roll in last frame (bonus roll)
        if (currentRoll == lastFrameStart + 2) {
            // Case 1: First roll was a strike, second roll was not a strike
            // In this case, the second and third rolls together cannot exceed 10 pins
            if (rolls[lastFrameStart] == MAX_PINS &&
                rolls[lastFrameStart + 1] != MAX_PINS &&
                rolls[lastFrameStart + 1] + pins > MAX_PINS) {
                throw new IllegalStateException("Pin count exceeds pins on the lane");
            }
            
            // Case 2: First roll was not a strike but second roll was not a spare
            // In this case, no third roll is allowed
            if (rolls[lastFrameStart] != MAX_PINS &&
                (rolls[lastFrameStart] + rolls[lastFrameStart + 1] != MAX_PINS)) {
                throw new IllegalStateException("Cannot roll after game is over");
            }
            
            // Case 3: First roll was not a strike, but was a spare with second roll
            // In this case, the third roll is allowed but no further rolls
            if (rolls[lastFrameStart] != MAX_PINS &&
                rolls[lastFrameStart] + rolls[lastFrameStart + 1] == MAX_PINS) {
                // No additional validation needed for this case
                return;
            }
        }
    }
    
    private boolean isGameOver() {
        int lastFrameStart = getLastFrameStartIndex();
        
        // Game is not over if we haven't reached the last frame
        if (currentRoll < lastFrameStart) {
            return false;
        }
        
        // If we've already used all rolls
        if (currentRoll >= MAX_ROLLS) {
            return true;
        }
        
        // Last frame with a strike
        if (rolls[lastFrameStart] == MAX_PINS) {
            // If we've already rolled twice more after the strike
            if (currentRoll >= lastFrameStart + 3) {
                return true;
            }
            return false;
        }
        
        // Last frame with a spare
        if (currentRoll > lastFrameStart &&
            rolls[lastFrameStart] + rolls[lastFrameStart + 1] == MAX_PINS) {
            // If we've already rolled once more after the spare
            return currentRoll >= lastFrameStart + 3;
        }
        
        // Last frame with open frame
        return currentRoll >= lastFrameStart + 2;
    }
    
    private void validateGameComplete() {
        // Game hasn't started
        if (currentRoll == 0) {
            throw new IllegalStateException("Score cannot be taken until the end of the game");
        }
        
        int lastFrameStart = getLastFrameStartIndex();
        
        // Not enough rolls for a complete game
        if (currentRoll < lastFrameStart) {
            throw new IllegalStateException("Score cannot be taken until the end of the game");
        }
        
        // Check if we need bonus rolls
        if (currentRoll > lastFrameStart && rolls[lastFrameStart] == MAX_PINS) {
            // Strike in the last frame needs two bonus rolls
            if (currentRoll < lastFrameStart + 3) {
                throw new IllegalStateException("Score cannot be taken until the end of the game");
            }
        } else if (currentRoll > lastFrameStart + 1 &&
                  rolls[lastFrameStart] + rolls[lastFrameStart + 1] == MAX_PINS) {
            // Spare in the last frame needs one bonus roll
            if (currentRoll < lastFrameStart + 3) {
                throw new IllegalStateException("Score cannot be taken until the end of the game");
            }
        } else {
            // Open frame in the last frame doesn't need bonus rolls
            if (currentRoll < lastFrameStart + 2) {
                throw new IllegalStateException("Score cannot be taken until the end of the game");
            }
        }
    }
}