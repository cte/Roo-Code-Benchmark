//
// This is only a SKELETON file for the 'Tournament' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const tournamentTally = (input) => {
  // Define the header for the table
  const header = 'Team                           | MP |  W |  D |  L |  P';
  
  // If input is empty, return just the header
  if (!input) {
    return header;
  }
  
  // Initialize an object to store team statistics
  const teams = {};
  
  // Parse the input and update team statistics
  input.split('\n').forEach(match => {
    const [team1, team2, result] = match.split(';');
    
    // Initialize teams if they don't exist
    if (!teams[team1]) {
      teams[team1] = { name: team1, mp: 0, w: 0, d: 0, l: 0, p: 0 };
    }
    if (!teams[team2]) {
      teams[team2] = { name: team2, mp: 0, w: 0, d: 0, l: 0, p: 0 };
    }
    
    // Update match played count
    teams[team1].mp += 1;
    teams[team2].mp += 1;
    
    // Update statistics based on the result
    if (result === 'win') {
      // Team 1 wins
      teams[team1].w += 1;
      teams[team1].p += 3;
      teams[team2].l += 1;
    } else if (result === 'loss') {
      // Team 2 wins
      teams[team1].l += 1;
      teams[team2].w += 1;
      teams[team2].p += 3;
    } else if (result === 'draw') {
      // Draw
      teams[team1].d += 1;
      teams[team1].p += 1;
      teams[team2].d += 1;
      teams[team2].p += 1;
    }
  });
  
  // Convert the teams object to an array for sorting
  const teamsArray = Object.values(teams);
  
  // Sort teams by points (descending) and then alphabetically
  teamsArray.sort((a, b) => {
    if (a.p !== b.p) {
      return b.p - a.p; // Sort by points descending
    }
    return a.name.localeCompare(b.name); // Sort alphabetically in case of tie
  });
  
  // Format the output
  const rows = teamsArray.map(team => {
    const name = team.name.padEnd(30);
    const mp = String(team.mp).padStart(2);
    const w = String(team.w).padStart(2);
    const d = String(team.d).padStart(2);
    const l = String(team.l).padStart(2);
    const p = String(team.p).padStart(2);
    
    return `${name} | ${mp} | ${w} | ${d} | ${l} | ${p}`;
  });
  
  // Combine header and rows
  return [header, ...rows].join('\n');
};
