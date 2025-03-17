#[derive(Debug, PartialEq, Eq)]
pub enum Error {
    NotEnoughPinsLeft,
    GameComplete,
}

#[derive(Debug)]
pub struct BowlingGame {
    rolls: Vec<u16>,
    current_frame: usize,
    pins_left_in_frame: u16,
    is_first_throw_in_frame: bool,
    fill_balls: u8,
}

impl BowlingGame {
    pub fn new() -> Self {
        BowlingGame {
            rolls: Vec::new(),
            current_frame: 1,
            pins_left_in_frame: 10,
            is_first_throw_in_frame: true,
            fill_balls: 0,
        }
    }

    pub fn roll(&mut self, pins: u16) -> Result<(), Error> {
        // Check if game is complete
        if self.is_game_complete() {
            return Err(Error::GameComplete);
        }

        // Validate pins
        if pins > self.pins_left_in_frame {
            return Err(Error::NotEnoughPinsLeft);
        }

        // Record the roll
        self.rolls.push(pins);

        // Handle fill balls (bonus rolls after 10th frame)
        if self.current_frame > 10 {
            self.fill_balls -= 1;
            
            // For the second fill ball after a strike in 10th frame
            if self.fill_balls == 0 && !self.is_first_throw_in_frame {
                // We've used all fill balls, game is complete
                return Ok(());
            }
            
            // If this is the first fill ball, and it's not a strike, 
            // we need to update pins_left_in_frame for the next roll
            if self.is_first_throw_in_frame {
                self.is_first_throw_in_frame = false;
                self.pins_left_in_frame -= pins;
                
                // If it was a strike, reset pins for next roll
                if pins == 10 {
                    self.pins_left_in_frame = 10;
                }
            } else {
                // Second fill ball, reset for next frame (though game will be complete)
                self.pins_left_in_frame = 10;
                self.is_first_throw_in_frame = true;
            }
            
            return Ok(());
        }

        // Handle regular frames (1-10)
        if self.is_first_throw_in_frame {
            if pins == 10 {
                // Strike
                if self.current_frame == 10 {
                    // 10th frame strike gets 2 fill balls
                    self.fill_balls = 2;
                    self.current_frame += 1;
                    self.is_first_throw_in_frame = true;
                    // Pins remain at 10 for next roll
                } else {
                    // Move to next frame
                    self.current_frame += 1;
                    // Pins remain at 10 for next roll
                }
            } else {
                // Not a strike, prepare for second roll
                self.pins_left_in_frame -= pins;
                self.is_first_throw_in_frame = false;
            }
        } else {
            // Second roll in frame
            let is_spare = self.pins_left_in_frame == pins;
            
            if self.current_frame == 10 && is_spare {
                // 10th frame spare gets 1 fill ball
                self.fill_balls = 1;
                self.current_frame += 1;
                self.is_first_throw_in_frame = true;
                self.pins_left_in_frame = 10;
            } else {
                // Move to next frame
                self.current_frame += 1;
                self.is_first_throw_in_frame = true;
                self.pins_left_in_frame = 10;
            }
        }

        Ok(())
    }

    pub fn score(&self) -> Option<u16> {
        // Game is not complete if not all frames are rolled or fill balls remain
        if !self.is_game_complete() {
            return None;
        }

        let mut score = 0;
        let mut frame_index = 0;
        
        // Score each of the 10 frames
        for _ in 0..10 {
            if frame_index >= self.rolls.len() {
                return None;
            }
            
            if self.is_strike(frame_index) {
                // Strike: 10 + next two rolls
                if frame_index + 2 >= self.rolls.len() {
                    return None; // Not enough rolls to score a strike
                }
                
                score += 10 + self.rolls[frame_index + 1] + self.rolls[frame_index + 2];
                frame_index += 1;
            } else if self.is_spare(frame_index) {
                // Spare: 10 + next roll
                if frame_index + 2 >= self.rolls.len() {
                    return None; // Not enough rolls to score a spare
                }
                
                score += 10 + self.rolls[frame_index + 2];
                frame_index += 2;
            } else {
                // Open frame: sum of two rolls
                if frame_index + 1 >= self.rolls.len() {
                    return None; // Not enough rolls to score an open frame
                }
                
                score += self.rolls[frame_index] + self.rolls[frame_index + 1];
                frame_index += 2;
            }
        }
        
        Some(score)
    }
    
    // Helper methods
    fn is_strike(&self, roll_index: usize) -> bool {
        roll_index < self.rolls.len() && self.rolls[roll_index] == 10
    }
    
    fn is_spare(&self, roll_index: usize) -> bool {
        roll_index + 1 < self.rolls.len() && 
        self.rolls[roll_index] + self.rolls[roll_index + 1] == 10
    }
    
    fn is_game_complete(&self) -> bool {
        // Game is complete when we've finished 10 frames and all fill balls
        self.current_frame > 10 && self.fill_balls == 0
    }
}
