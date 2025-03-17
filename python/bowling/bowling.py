class BowlingGame:
    def __init__(self):
        self.rolls = []
        self.current_frame = 1
        self.current_roll_in_frame = 1
        self.is_bonus_roll = False
        self.bonus_rolls_needed = 0

    def roll(self, pins):
        # Validate pins
        if pins < 0:
            raise ValueError("Negative roll is invalid")
        if pins > 10:
            raise ValueError("Pin count exceeds pins on the lane")

        # Check if game is already over
        if self.current_frame > 10 and self.bonus_rolls_needed == 0:
            raise ValueError("Cannot roll after game is complete")

        # Handle regular frames (1-9)
        if self.current_frame < 10:
            # First roll in frame
            if self.current_roll_in_frame == 1:
                self.rolls.append(pins)
                if pins == 10:  # Strike
                    self.current_frame += 1
                else:
                    self.current_roll_in_frame = 2
            # Second roll in frame
            else:
                # Validate that the sum of rolls in the frame doesn't exceed 10
                if pins + self.rolls[-1] > 10:
                    raise ValueError("Pin count exceeds pins on the lane")
                self.rolls.append(pins)
                self.current_frame += 1
                self.current_roll_in_frame = 1
        # Handle 10th frame
        elif self.current_frame == 10:
            # First roll in 10th frame
            if self.current_roll_in_frame == 1:
                self.rolls.append(pins)
                if pins == 10:  # Strike in 10th frame
                    self.bonus_rolls_needed = 2
                self.current_roll_in_frame = 2
            # Second roll in 10th frame
            elif self.current_roll_in_frame == 2:
                # If first roll was a strike, this is a bonus roll
                if self.rolls[-1] == 10:
                    self.rolls.append(pins)
                    self.bonus_rolls_needed -= 1
                    if pins == 10 and self.bonus_rolls_needed > 0:
                        self.current_roll_in_frame = 3
                    elif self.bonus_rolls_needed == 0:
                        self.current_frame += 1
                    else:
                        self.current_roll_in_frame = 3
                else:
                    # Validate that the sum of rolls in the frame doesn't exceed 10
                    if pins + self.rolls[-1] > 10:
                        raise ValueError("Pin count exceeds pins on the lane")
                    self.rolls.append(pins)
                    # Check if we got a spare
                    if self.rolls[-1] + self.rolls[-2] == 10:
                        self.bonus_rolls_needed = 1
                        self.current_roll_in_frame = 3
                    else:
                        self.current_frame += 1
            # Third roll in 10th frame (bonus roll)
            elif self.current_roll_in_frame == 3:
                # If previous roll was a strike, no validation needed
                if self.rolls[-1] == 10:
                    self.rolls.append(pins)
                # If previous two rolls were a spare, no validation needed
                elif self.rolls[-1] + self.rolls[-2] == 10:
                    self.rolls.append(pins)
                # If first roll was strike and second wasn't, validate second bonus roll
                elif self.rolls[-2] == 10 and self.rolls[-1] < 10:
                    if pins > 10:
                        raise ValueError("Pin count exceeds pins on the lane")
                    if pins + self.rolls[-1] > 10:
                        raise ValueError("Pin count exceeds pins on the lane")
                    self.rolls.append(pins)
                self.bonus_rolls_needed -= 1
                if self.bonus_rolls_needed == 0:
                    self.current_frame += 1
        # Handle bonus rolls
        else:
            if self.bonus_rolls_needed > 0:
                self.rolls.append(pins)
                self.bonus_rolls_needed -= 1
            else:
                raise ValueError("Cannot roll after game is complete")

    def score(self):
        # Check if game is complete
        if len(self.rolls) == 0:
            raise ValueError("Score cannot be taken on an unstarted game")
        
        if self.current_frame <= 10 or self.bonus_rolls_needed > 0:
            raise ValueError("Score cannot be taken until the end of the game")

        total_score = 0
        roll_index = 0
        
        # Score each of the 10 frames
        for frame in range(1, 11):
            # Strike
            if self.rolls[roll_index] == 10:
                # Strike bonus: the value of the next two rolls
                total_score += 10 + self.rolls[roll_index + 1] + self.rolls[roll_index + 2]
                roll_index += 1
            # Spare
            elif self.rolls[roll_index] + self.rolls[roll_index + 1] == 10:
                # Spare bonus: the value of the next roll
                total_score += 10 + self.rolls[roll_index + 2]
                roll_index += 2
            # Open frame
            else:
                total_score += self.rolls[roll_index] + self.rolls[roll_index + 1]
                roll_index += 2
                
        return total_score
