export class Scale {
  constructor(tonic) {
    this.tonic = tonic.charAt(0).toUpperCase() + tonic.slice(1).toLowerCase();
    this.useFlats = ['F', 'Bb', 'Eb', 'Ab', 'Db', 'Gb', 'd', 'g', 'c', 'f', 'bb', 'eb'].includes(tonic);
    
    // Define the chromatic scales with sharps and flats
    this.sharpScale = ['C', 'C#', 'D', 'D#', 'E', 'F', 'F#', 'G', 'G#', 'A', 'A#', 'B'];
    this.flatScale = ['C', 'Db', 'D', 'Eb', 'E', 'F', 'Gb', 'G', 'Ab', 'A', 'Bb', 'B'];
    
    // Choose the appropriate scale based on the tonic
    this.scale = this.useFlats ? this.flatScale : this.sharpScale;
    
    // Find the index of the tonic in the scale
    this.tonicIndex = this.scale.indexOf(this.tonic);
    
    // If the tonic is not found directly (e.g., lowercase notes), try to find it case-insensitively
    if (this.tonicIndex === -1) {
      this.tonicIndex = this.scale.findIndex(
        note => note.toLowerCase() === this.tonic.toLowerCase()
      );
    }
  }

  chromatic() {
    // Return the chromatic scale starting from the tonic
    const result = [];
    for (let i = 0; i < 12; i++) {
      const index = (this.tonicIndex + i) % 12;
      result.push(this.scale[index]);
    }
    return result;
  }

  interval(intervals) {
    const result = [];
    let currentIndex = this.tonicIndex;
    
    // Add the tonic as the first note
    result.push(this.scale[currentIndex]);
    
    // Process each interval
    for (const interval of intervals) {
      switch (interval) {
        case 'm': // Minor second (half step)
          currentIndex = (currentIndex + 1) % 12;
          break;
        case 'M': // Major second (whole step)
          currentIndex = (currentIndex + 2) % 12;
          break;
        case 'A': // Augmented second (1.5 steps)
          currentIndex = (currentIndex + 3) % 12;
          break;
      }
      result.push(this.scale[currentIndex]);
    }
    
    return result;
  }
}
