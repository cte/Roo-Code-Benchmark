package bowling

import (
	"errors"
)

// Game represents a bowling game
type Game struct {
	rolls []int // All rolls in the game
}

// NewGame creates a new bowling game
func NewGame() *Game {
	return &Game{
		rolls: make([]int, 0, 21), // Maximum 21 rolls (10 frames + 2 bonus rolls)
	}
}

// Roll records a roll in the game
func (g *Game) Roll(pins int) error {
	// Validate pins
	if pins < 0 {
		return errors.New("Negative roll is invalid")
	}
	if pins > 10 {
		return errors.New("Pin count exceeds pins on the lane")
	}

	// Check if game is already over
	if g.isGameOver() {
		return errors.New("Cannot roll after game is over")
	}

	// Get current frame and roll within frame
	frameIndex, rollInFrame := g.getCurrentFrameAndRoll()

	// Handle validation for regular frames (0-8)
	if frameIndex < 9 {
		// If this is the second roll in a frame, check that the total doesn't exceed 10
		if rollInFrame == 1 && g.rolls[len(g.rolls)-1]+pins > 10 {
			return errors.New("Pin count exceeds pins on the lane")
		}
	} else if frameIndex == 9 {
		// Handle validation for the 10th frame
		startOfTenth := g.getStartOfFrame(9)
		rollsInTenth := len(g.rolls) - startOfTenth
		
		if rollsInTenth == 1 {
			// Second roll in 10th frame
			// If first roll was not a strike, ensure total doesn't exceed 10
			firstRoll := g.rolls[startOfTenth]
			if firstRoll < 10 && firstRoll+pins > 10 {
				return errors.New("Pin count exceeds pins on the lane")
			}
		} else if rollsInTenth == 2 {
			// Third roll in 10th frame (bonus roll)
			firstRoll := g.rolls[startOfTenth]
			secondRoll := g.rolls[startOfTenth+1]
			
			// If first roll was a strike and second roll was not, 
			// then the sum of second and third rolls cannot exceed 10
			if firstRoll == 10 && secondRoll < 10 && secondRoll+pins > 10 {
				return errors.New("Pin count exceeds pins on the lane")
			}
		}
	}

	// Record the roll
	g.rolls = append(g.rolls, pins)

	return nil
}

// Score calculates the total score of the game
func (g *Game) Score() (int, error) {
	if !g.isGameComplete() {
		return 0, errors.New("Score cannot be taken until the end of the game")
	}

	score := 0
	rollIndex := 0

	// Calculate score for each of the 10 frames
	for frame := 0; frame < 10; frame++ {
		if g.isStrike(rollIndex) {
			// Strike: 10 + the next two rolls
			if rollIndex+2 >= len(g.rolls) {
				return 0, errors.New("Score cannot be taken until the end of the game")
			}
			score += 10 + g.rolls[rollIndex+1] + g.rolls[rollIndex+2]
			rollIndex++
		} else if g.isSpare(rollIndex) {
			// Spare: 10 + the next roll
			if rollIndex+2 >= len(g.rolls) {
				return 0, errors.New("Score cannot be taken until the end of the game")
			}
			score += 10 + g.rolls[rollIndex+2]
			rollIndex += 2
		} else {
			// Open frame: sum of the two rolls
			if rollIndex+1 >= len(g.rolls) {
				return 0, errors.New("Score cannot be taken until the end of the game")
			}
			score += g.rolls[rollIndex] + g.rolls[rollIndex+1]
			rollIndex += 2
		}
	}

	return score, nil
}

// Helper methods

// isStrike checks if the roll at the given index is a strike
func (g *Game) isStrike(rollIndex int) bool {
	return g.rolls[rollIndex] == 10
}

// isSpare checks if the two rolls starting at the given index form a spare
func (g *Game) isSpare(rollIndex int) bool {
	return g.rolls[rollIndex]+g.rolls[rollIndex+1] == 10
}

// getCurrentFrameAndRoll determines the current frame and roll within that frame
func (g *Game) getCurrentFrameAndRoll() (int, int) {
	frameIndex := 0
	rollIndex := 0

	for i := 0; i < len(g.rolls); i++ {
		if frameIndex == 9 {
			// In the 10th frame
			return frameIndex, i - rollIndex
		}

		if g.rolls[i] == 10 {
			// Strike
			frameIndex++
			rollIndex = i + 1
		} else {
			// First roll of a frame
			if (i - rollIndex) % 2 == 0 {
				// Do nothing, stay in the same frame
			} else {
				// Second roll of a frame
				frameIndex++
				rollIndex = i + 1
			}
		}
	}

	return frameIndex, len(g.rolls) - rollIndex
}

// getStartOfFrame returns the roll index where the specified frame starts
func (g *Game) getStartOfFrame(frame int) int {
	if frame == 0 {
		return 0
	}

	frameIndex := 0
	rollIndex := 0

	for i := 0; i < len(g.rolls); i++ {
		if frameIndex == frame {
			return i
		}

		if g.rolls[i] == 10 {
			// Strike
			frameIndex++
		} else {
			// First roll of a frame
			if (i - rollIndex) % 2 == 0 {
				// Do nothing, stay in the same frame
			} else {
				// Second roll of a frame
				frameIndex++
				rollIndex = i + 1
			}
		}
	}

	return len(g.rolls)
}

// countFrames counts the number of complete frames in the game
func (g *Game) countFrames() int {
    if len(g.rolls) == 0 {
        return 0
    }

    frames := 0
    rollIndex := 0

    // Count frames 1-9
    for frames < 9 && rollIndex < len(g.rolls) {
        if g.rolls[rollIndex] == 10 {
            // Strike
            frames++
            rollIndex++
        } else if rollIndex + 1 < len(g.rolls) {
            // Normal frame with two rolls
            frames++
            rollIndex += 2
        } else {
            // Incomplete frame
            break
        }
    }

    // Handle 10th frame
    if frames == 9 && rollIndex < len(g.rolls) {
        // We're in the 10th frame
        if rollIndex + 1 < len(g.rolls) {
            // At least two rolls in the 10th frame
            if g.rolls[rollIndex] == 10 || g.rolls[rollIndex] + g.rolls[rollIndex+1] == 10 {
                // Strike or spare in the 10th frame
                if rollIndex + 2 < len(g.rolls) {
                    // All three rolls completed
                    frames++
                }
            } else {
                // Open frame in the 10th
                frames++
            }
        }
    }

    return frames;
}

// isGameOver checks if the game is over (no more rolls allowed)
func (g *Game) isGameOver() bool {
    // If we have 10 complete frames, the game is over
    if g.countFrames() == 10 {
        return true
    }

    // If we have 9 complete frames and are in the 10th
    if g.countFrames() == 9 {
        frameIndex, _ := g.getCurrentFrameAndRoll()
        if frameIndex == 9 {
            startOfTenth := g.getStartOfFrame(9)
            rollsInTenth := len(g.rolls) - startOfTenth

            // If first roll is a strike, need 3 rolls
            if rollsInTenth > 0 && g.rolls[startOfTenth] == 10 {
                return rollsInTenth >= 3
            }
            
            // If first two rolls form a spare, need 3 rolls
            if rollsInTenth > 1 && g.rolls[startOfTenth] + g.rolls[startOfTenth+1] == 10 {
                return rollsInTenth >= 3
            }
            
            // Otherwise, need 2 rolls
            return rollsInTenth >= 2
        }
    }

    return false
}

// isGameComplete checks if the game is complete (score can be calculated)
func (g *Game) isGameComplete() bool {
    // For scoring purposes, we need to check if we have enough rolls to calculate the score
    // This is different from isGameOver which checks if we can roll more

    // If we have fewer than 12 rolls, we need to check if we have 10 complete frames
    if len(g.rolls) < 12 {
        return g.countFrames() == 10
    }

    // If we have 12 or more rolls, we need to check if we have enough rolls to score all frames
    rollIndex := 0
    for frame := 0; frame < 10; frame++ {
        if rollIndex >= len(g.rolls) {
            return false
        }

        if g.isStrike(rollIndex) {
            // For a strike, we need two more rolls
            if rollIndex + 2 >= len(g.rolls) {
                return false
            }
            rollIndex++
        } else {
            // For a non-strike, we need one more roll
            if rollIndex + 1 >= len(g.rolls) {
                return false
            }
            
            // If it's a spare, we need one more roll after that
            if g.isSpare(rollIndex) && rollIndex + 2 >= len(g.rolls) {
                return false
            }
            
            rollIndex += 2
        }
    }

    return true
}
