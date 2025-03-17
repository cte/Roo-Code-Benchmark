export const recite = (start, end = start) => {
  const ordinals = [
    'first',
    'second',
    'third',
    'fourth',
    'fifth',
    'sixth',
    'seventh',
    'eighth',
    'ninth',
    'tenth',
    'eleventh',
    'twelfth'
  ];

  const gifts = [
    'a Partridge in a Pear Tree',
    'two Turtle Doves',
    'three French Hens',
    'four Calling Birds',
    'five Gold Rings',
    'six Geese-a-Laying',
    'seven Swans-a-Swimming',
    'eight Maids-a-Milking',
    'nine Ladies Dancing',
    'ten Lords-a-Leaping',
    'eleven Pipers Piping',
    'twelve Drummers Drumming'
  ];

  const generateVerse = (day) => {
    const dayIndex = day - 1;
    let verse = `On the ${ordinals[dayIndex]} day of Christmas my true love gave to me: `;
    
    if (day === 1) {
      verse += `${gifts[0]}.`;
    } else {
      for (let i = dayIndex; i > 0; i--) {
        verse += `${gifts[i]}, `;
      }
      verse += `and ${gifts[0]}.`;
    }
    
    return verse + '\n';
  };

  let result = '';
  for (let day = start; day <= end; day++) {
    result += generateVerse(day);
    if (day < end) {
      result += '\n';
    }
  }
  
  return result;
};
