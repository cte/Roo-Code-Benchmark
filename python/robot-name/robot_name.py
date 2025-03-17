import random
import string

class Robot:
    used_names = set()

    def __init__(self):
        self._name = None
        self.reset()

    @property
    def name(self):
        return self._name

    def reset(self):
        # Store the current name to avoid generating the same one again
        old_name = self._name
        
        # Clear the current name from the used names set
        if self._name in Robot.used_names:
            Robot.used_names.remove(self._name)
        
        # Generate a new unique name that's different from the old one
        while True:
            # Generate two random uppercase letters
            letters = ''.join(random.choice(string.ascii_uppercase) for _ in range(2))
            
            # Generate three random digits
            digits = ''.join(str(random.randint(0, 9)) for _ in range(3))
            
            # Combine to form the name
            new_name = letters + digits
            
            # Check if the name is unique and different from the old name
            if new_name not in Robot.used_names and new_name != old_name:
                Robot.used_names.add(new_name)
                self._name = new_name
                break
