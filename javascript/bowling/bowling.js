export class Bowling {
  constructor() {
    this.rolls = [];
    this.currentFrame = 0;
    this.isFirstRollInFrame = true;
    this.gameFinished = false;
  }

  roll(pins) {
    // Validate the roll
    this.validateRoll(pins);
    
    // Add the roll to our rolls array
    this.rolls.push(pins);
    
    // Update frame information
    this.updateFrameInfo(pins);
  }

  validateRoll(pins) {
    // Check if game is already over
    if (this.gameFinished) {
      throw new Error('Cannot roll after game is over');
    }
    
    // Check if we've already completed a game with a spare in the 10th frame and a bonus roll
    if (this.rolls.length >= 21) {
      const isSpareInTenth = this.rolls[18] + this.rolls[19] === 10 && this.rolls[18] !== 10;
      if (isSpareInTenth) {
        this.gameFinished = true;
        throw new Error('Cannot roll after game is over');
      }
    }
    
    // Check for negative rolls
    if (pins < 0) {
      throw new Error('Negative roll is invalid');
    }
    
    // Check for rolls over 10
    if (pins > 10) {
      throw new Error('Pin count exceeds pins on the lane');
    }
    
    // Check if this roll would make the current frame exceed 10 pins
    // (unless it's a bonus roll after a strike in the 10th frame)
    if (!this.isFirstRollInFrame &&
        this.currentFrame < 10 &&
        this.rolls[this.rolls.length - 1] + pins > 10) {
      throw new Error('Pin count exceeds pins on the lane');
    }
    
    // Special case for the second bonus roll after a strike in the 10th frame
    if (this.currentFrame === 10 &&
        !this.isFirstRollInFrame &&
        this.rolls[this.rolls.length - 2] === 10 &&
        this.rolls[this.rolls.length - 1] !== 10 &&
        this.rolls[this.rolls.length - 1] + pins > 10) {
      throw new Error('Pin count exceeds pins on the lane');
    }
  }

  updateFrameInfo(pins) {
    // Handle normal frames (1-9)
    if (this.currentFrame < 10) {
      if (this.isFirstRollInFrame) {
        // If it's a strike, move to the next frame
        if (pins === 10) {
          this.currentFrame++;
        } else {
          this.isFirstRollInFrame = false;
        }
      } else {
        // After second roll in a frame, move to the next frame
        this.currentFrame++;
        this.isFirstRollInFrame = true;
      }
      
      // Check if we've completed 10 frames with no strikes or spares
      if (this.currentFrame === 10 && this.isFirstRollInFrame) {
        // Check if the last frame was not a strike or spare
        const lastFrameIndex = this.rolls.length - 2;
        if (lastFrameIndex >= 0 &&
            this.rolls[lastFrameIndex] + this.rolls[lastFrameIndex + 1] < 10) {
          this.gameFinished = true;
        }
      }
    }
    // Handle the 10th frame
    else if (this.currentFrame === 10) {
      if (this.isFirstRollInFrame) {
        // If it's a strike in the 10th frame
        if (pins === 10) {
          this.isFirstRollInFrame = false;
        } else {
          this.isFirstRollInFrame = false;
        }
      } else {
        // Check if we need bonus rolls
        const firstRollInLastFrame = this.rolls[this.rolls.length - 2];
        const secondRollInLastFrame = pins;
        
        // If it's a strike or spare, we get a bonus roll
        if (firstRollInLastFrame === 10 || firstRollInLastFrame + secondRollInLastFrame === 10) {
          this.currentFrame = 11; // Move to bonus roll
          this.isFirstRollInFrame = true;
        } else {
          this.gameFinished = true;
        }
      }
    }
    // Handle bonus rolls
    else if (this.currentFrame === 11) {
      // If we had a strike in the 10th frame, we get two bonus rolls
      const tenthFrameFirstRoll = this.rolls[this.rolls.length - 3];
      
      if (tenthFrameFirstRoll === 10) {
        if (this.isFirstRollInFrame) {
          this.isFirstRollInFrame = false;
        } else {
          this.gameFinished = true;
        }
      } else {
        // If we had a spare in the 10th frame, we only get one bonus roll
        this.gameFinished = true;
      }
    }
  }

  score() {
    // Check if the game is complete
    if (!this.isGameComplete()) {
      throw new Error('Score cannot be taken until the end of the game');
    }
    
    let score = 0;
    let rollIndex = 0;
    
    // Calculate score for each of the 10 frames
    for (let frame = 0; frame < 10; frame++) {
      // Strike
      if (this.rolls[rollIndex] === 10) {
        score += 10 + this.strikeBonus(rollIndex);
        rollIndex++;
      }
      // Spare
      else if (this.rolls[rollIndex] + this.rolls[rollIndex + 1] === 10) {
        score += 10 + this.spareBonus(rollIndex);
        rollIndex += 2;
      }
      // Open frame
      else {
        score += this.rolls[rollIndex] + this.rolls[rollIndex + 1];
        rollIndex += 2;
      }
    }
    
    return score;
  }

  strikeBonus(rollIndex) {
    return this.rolls[rollIndex + 1] + this.rolls[rollIndex + 2];
  }

  spareBonus(rollIndex) {
    return this.rolls[rollIndex + 2];
  }

  isGameComplete() {
    // If we haven't rolled any balls, the game isn't complete
    if (this.rolls.length === 0) {
      return false;
    }
    
    // If we've marked the game as finished, it's complete
    if (this.gameFinished) {
      return true;
    }
    
    // If we haven't completed 10 frames yet, the game isn't complete
    if (this.currentFrame < 10) {
      return false;
    }
    
    // For the 10th frame, we need to check if we need bonus rolls
    const rollsInTenthFrame = this.getRollsInTenthFrame();
    
    // If we have a strike in the 10th frame, we need two more rolls
    if (rollsInTenthFrame[0] === 10) {
      return this.rolls.length >= this.getRollIndexForTenthFrame() + 3;
    }
    
    // If we have a spare in the 10th frame, we need one more roll
    if (rollsInTenthFrame[0] + rollsInTenthFrame[1] === 10) {
      return this.rolls.length >= this.getRollIndexForTenthFrame() + 3;
    }
    
    // Otherwise, we just need the two rolls in the 10th frame
    return this.rolls.length >= this.getRollIndexForTenthFrame() + 2;
  }

  getRollIndexForTenthFrame() {
    let rollIndex = 0;
    
    // Count rolls for frames 1-9
    for (let frame = 0; frame < 9; frame++) {
      // Strike
      if (this.rolls[rollIndex] === 10) {
        rollIndex++;
      }
      // Non-strike (two rolls)
      else {
        rollIndex += 2;
      }
    }
    
    return rollIndex;
  }

  getRollsInTenthFrame() {
    const tenthFrameIndex = this.getRollIndexForTenthFrame();
    return [
      this.rolls[tenthFrameIndex] || 0,
      this.rolls[tenthFrameIndex + 1] || 0
    ];
  }
}
