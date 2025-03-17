export const meetup = (year, month, descriptor, weekday) => {
  // Map weekday names to their numeric values (0 = Sunday, 1 = Monday, etc.)
  const weekdayMap = {
    'Sunday': 0,
    'Monday': 1,
    'Tuesday': 2,
    'Wednesday': 3,
    'Thursday': 4,
    'Friday': 5,
    'Saturday': 6
  };

  // Get the numeric value for the specified weekday
  const targetWeekday = weekdayMap[weekday];

  // Handle different descriptors
  switch (descriptor) {
    case 'teenth': {
      // The 'teenth' days are from 13 to 19
      // Start from the 13th and find the first occurrence of the target weekday
      for (let day = 13; day <= 19; day++) {
        const date = new Date(year, month - 1, day);
        if (date.getDay() === targetWeekday) {
          return date;
        }
      }
      break;
    }
    
    case 'first':
    case 'second':
    case 'third':
    case 'fourth': {
      // Map descriptor to the number of occurrences to find
      const occurrenceMap = {
        'first': 1,
        'second': 2,
        'third': 3,
        'fourth': 4
      };
      
      const targetOccurrence = occurrenceMap[descriptor];
      let currentOccurrence = 0;
      
      // Start from the first day of the month and count occurrences
      for (let day = 1; day <= 31; day++) {
        const date = new Date(year, month - 1, day);
        
        // If we've gone to the next month, break
        if (date.getMonth() !== month - 1) {
          break;
        }
        
        // If this day is the target weekday, increment the occurrence counter
        if (date.getDay() === targetWeekday) {
          currentOccurrence++;
          
          // If we've found the target occurrence, return the date
          if (currentOccurrence === targetOccurrence) {
            return date;
          }
        }
      }
      break;
    }
    
    case 'last': {
      // Start from the last possible day of the month and work backwards
      // Get the first day of the next month and subtract 1 to get the last day of the current month
      const lastDay = new Date(year, month, 0).getDate();
      
      // Start from the last day and work backwards
      for (let day = lastDay; day >= 1; day--) {
        const date = new Date(year, month - 1, day);
        if (date.getDay() === targetWeekday) {
          return date;
        }
      }
      break;
    }
  }
  
  // If we get here, something went wrong
  throw new Error('Could not find the specified meetup date');
};
