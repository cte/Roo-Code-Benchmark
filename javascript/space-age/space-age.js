//
// This is only a SKELETON file for the 'Space Age' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const age = (planet, seconds) => {
  // Earth orbital period in seconds (1 Earth year)
  const earthYearInSeconds = 31557600; // 365.25 days * 24 hours * 60 minutes * 60 seconds
  
  // Orbital periods of planets relative to Earth years
  const orbitalPeriods = {
    mercury: 0.2408467,
    venus: 0.61519726,
    earth: 1.0,
    mars: 1.8808158,
    jupiter: 11.862615,
    saturn: 29.447498,
    uranus: 84.016846,
    neptune: 164.79132
  };
  
  // Calculate Earth years from seconds
  const earthYears = seconds / earthYearInSeconds;
  
  // Convert Earth years to the specified planet's years
  const planetYears = earthYears / orbitalPeriods[planet];
  
  // Round to 2 decimal places
  return Number(planetYears.toFixed(2));
};
