# Game status categories
# Change the values as you see fit
STATUS_WIN = 'win'
STATUS_LOSE = 'lose'
STATUS_ONGOING = 'ongoing'


class Hangman:
    def __init__(self, word):
        self.word = word
        self.remaining_guesses = 9
        self.status = STATUS_ONGOING
        self.guessed_letters = set()

    def guess(self, char):
        if self.status != STATUS_ONGOING:
            raise ValueError("The game has already ended.")
        
        if char in self.guessed_letters:
            self.remaining_guesses -= 1
        else:
            self.guessed_letters.add(char)
            if char not in self.word:
                self.remaining_guesses -= 1
        
        # Check if the game is lost
        if self.remaining_guesses < 0:
            self.status = STATUS_LOSE
        
        # Check if the game is won
        if all(letter in self.guessed_letters for letter in self.word):
            self.status = STATUS_WIN
            
    def get_masked_word(self):
        return ''.join(letter if letter in self.guessed_letters else '_' for letter in self.word)

    def get_status(self):
        return self.status
