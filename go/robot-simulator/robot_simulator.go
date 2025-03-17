package robot

import "fmt"

// See defs.go for other definitions

// Step 1
// Define N, E, S, W here.
const (
	N Dir = iota // North
	E      // East
	S      // South
	W      // West
)

func Right() {
	Step1Robot.Dir = (Step1Robot.Dir + 1) % 4
}

func Left() {
	Step1Robot.Dir = (Step1Robot.Dir + 3) % 4
}

func Advance() {
	switch Step1Robot.Dir {
	case N:
		Step1Robot.Y++
	case E:
		Step1Robot.X++
	case S:
		Step1Robot.Y--
	case W:
		Step1Robot.X--
	}
}

func (d Dir) String() string {
	switch d {
	case N:
		return "North"
	case E:
		return "East"
	case S:
		return "South"
	case W:
		return "West"
	default:
		return "Invalid direction"
	}
}

// Step 2
// Define Action type here.
type Action struct {
	Command Command
}

func StartRobot(command chan Command, action chan Action) {
	for cmd := range command {
		action <- Action{Command: cmd}
	}
	close(action)
}

func Room(extent Rect, robot Step2Robot, action chan Action, report chan Step2Robot) {
	for act := range action {
		switch act.Command {
		case 'R':
			robot.Dir = (robot.Dir + 1) % 4
		case 'L':
			robot.Dir = (robot.Dir + 3) % 4
		case 'A':
			newPos := robot.Pos
			switch robot.Dir {
			case N:
				newPos.Northing++
			case E:
				newPos.Easting++
			case S:
				newPos.Northing--
			case W:
				newPos.Easting--
			}
			
			// Check if the new position is within the room boundaries
			if newPos.Easting >= extent.Min.Easting &&
			   newPos.Easting <= extent.Max.Easting &&
			   newPos.Northing >= extent.Min.Northing &&
			   newPos.Northing <= extent.Max.Northing {
				robot.Pos = newPos
			}
		}
	}
	report <- robot
}

// Step 3
// Define Action3 type here.
type Action3 struct {
	Name    string
	Command byte
}

func StartRobot3(name, script string, action chan Action3, log chan string) {
	for _, cmd := range script {
		if cmd != 'R' && cmd != 'L' && cmd != 'A' {
			log <- "Invalid command: " + string(cmd)
			continue
		}
		action <- Action3{Name: name, Command: byte(cmd)}
	}
}

func Room3(extent Rect, robots []Step3Robot, action chan Action3, rep chan []Step3Robot, log chan string) {
	// Check for initial errors
	if len(robots) == 0 {
		log <- "No robots provided"
		rep <- robots
		return
	}
	
	// Check for robots with no name
	for _, robot := range robots {
		if robot.Name == "" {
			log <- "Robot with no name"
			rep <- robots
			return
		}
	}
	
	// Check for robots with the same name
	names := make(map[string]bool)
	for _, robot := range robots {
		if names[robot.Name] {
			log <- "Duplicate robot name: " + robot.Name
			rep <- robots
			return
		}
		names[robot.Name] = true
	}
	
	// Check for robots at the same position
	positions := make(map[Pos]bool)
	for _, robot := range robots {
		if positions[robot.Pos] {
			log <- "Duplicate robot position: " + robot.Pos.String()
			rep <- robots
			return
		}
		positions[robot.Pos] = true
	}
	
	// Check for robots outside the room
	for _, robot := range robots {
		if robot.Pos.Easting < extent.Min.Easting ||
		   robot.Pos.Easting > extent.Max.Easting ||
		   robot.Pos.Northing < extent.Min.Northing ||
		   robot.Pos.Northing > extent.Max.Northing {
			log <- "Robot outside room: " + robot.Name
			rep <- robots
			return
		}
	}
	
	// Process actions
	robotMap := make(map[string]*Step3Robot)
	for i := range robots {
		robotMap[robots[i].Name] = &robots[i]
	}
	
	collisionCount := 0
	
	for act := range action {
		robot, exists := robotMap[act.Name]
		if !exists {
			log <- "Robot not found: " + act.Name
			continue
		}
		
		switch act.Command {
		case 'R':
			robot.Dir = (robot.Dir + 1) % 4
		case 'L':
			robot.Dir = (robot.Dir + 3) % 4
		case 'A':
			newPos := robot.Pos
			switch robot.Dir {
			case N:
				newPos.Northing++
			case E:
				newPos.Easting++
			case S:
				newPos.Northing--
			case W:
				newPos.Easting--
			}
			
			// Check if the new position is within the room boundaries
			if newPos.Easting < extent.Min.Easting ||
			   newPos.Easting > extent.Max.Easting ||
			   newPos.Northing < extent.Min.Northing ||
			   newPos.Northing > extent.Max.Northing {
				log <- "Wall collision: " + robot.Name
				continue
			}
			
			// Check for collision with other robots
			collision := false
			for _, otherRobot := range robots {
				if otherRobot.Name != robot.Name && otherRobot.Pos == newPos {
					log <- "Robot collision: " + robot.Name + " and " + otherRobot.Name
					collision = true
					collisionCount++
					break
				}
			}
			
			if !collision {
				robot.Pos = newPos
			}
		}
	}
	
	rep <- robots
}

// Helper method to convert Pos to string for error messages
func (p Pos) String() string {
	return fmt.Sprintf("(%d,%d)", p.Easting, p.Northing)
}
