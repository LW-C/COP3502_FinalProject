package ufl.cs1.controllers;

import game.controllers.DefenderController;
import game.models.*;

import java.util.ArrayList;
import java.util.List;

import static game.models.Game.Direction.*;

public final class StudentController implements DefenderController
{
	int j = 0;
	public void init(Game game) { }

	public void shutdown(Game game) { }

	public int[] update(Game game,long timeDue)
	{
		int[] actions = new int[Game.NUM_DEFENDER];
		List<Defender> enemies = game.getDefenders();

		Maze maze = game.getCurMaze();

		actions[0] = goParallel(game, 0, maze);
		actions[1] = chaseOrFlee(game, 1);
		actions[2] = blockPowerPills(game, maze, 2);
		actions[3] = chaseOrFlee(game, 3);

		return actions;
	}

	//if the defender is not vulnerable, it moves towards the attacker, if it is vulnerable, it moves away from it
	int chaseOrFlee(Game game, int defenderNum){
		int nextDir = game.getDefender(defenderNum).getNextDir(game.getAttacker().getLocation(), !game.getDefender(defenderNum).isVulnerable());
		return nextDir;
	}


	//this method has the defender circle the first available powerpill in the list, unless the attacker is closer to the
	//powerpill than the defender is, in which case it will flee
	int blockPowerPills(Game game, Maze maze, int defenderNum){
		int nextDir = EMPTY;

		List <Node> targetPillList = new ArrayList<Node>();

		for (int i = 0; i < targetPillList.size(); i++){
			if (game.checkPowerPill(targetPillList.get(i))){
				//if the power pill is between the defender and the attacker, the defender goes towards the attacker
				if (game.getAttacker().getLocation().getPathDistance(targetPillList.get(i)) <= game.getAttacker().getLocation().getPathDistance(game.getDefender(defenderNum).getLocation())) {
					nextDir = game.getDefender(defenderNum).getNextDir(game.getAttacker().getLocation(), true);
					return nextDir;
				}
				//if the attacker is not at the power pill, the defender will approach it
				else if (game.getDefender(defenderNum).getLocation() != targetPillList.get(i)){
					nextDir = game.getDefender(defenderNum).getNextDir(targetPillList.get(i), !game.getDefender(defenderNum).isVulnerable());
					return nextDir;
				}
				//if the attacker is at the power pill, it will continue on
				else{
					nextDir = EMPTY;
					return nextDir;
				}
			}
		}
		return nextDir;
	}

    int goParallel(Game game, int defenderNum, Maze maze){
        int nextDir = EMPTY;

        //this part uses the next method down to find the nearest powerpill
        Node nearestPowerPill = findNearestPowerPill(game, maze);

        //if the defender is vulnerable, the next direction is the one it needs to go to flee the attacker
        if (game.getDefender(defenderNum).isVulnerable()){
            nextDir = game.getDefender(defenderNum).getNextDir(game.getAttacker().getLocation(), !game.getDefender(defenderNum).isVulnerable());
        }
        //if the distance between the defender and the attacker is less than the distance between the attacker and
        //and the nearest powerpill AND the attacker is not at a junction, the defender will move towards the atttacker
        else if ((game.getDefender(defenderNum).getLocation().getPathDistance(game.getAttacker().getLocation()) < game.getAttacker().getLocation().getPathDistance(nearestPowerPill)) && !game.getAttacker().getLocation().isJunction()){
            nextDir = game.getDefender(defenderNum).getNextDir(game.getAttacker().getLocation(), !game.getDefender(defenderNum).isVulnerable());
        }
        //the defender will move in the same direction as the attacker (either chasing it or going parallel)
        else {
            nextDir = game.getAttacker().getDirection();
        }
        return nextDir;
    }

    //Finds the powerpill closest to the attacker
    Node findNearestPowerPill (Game game, Maze maze){
        List<Node> powerPillList = maze.getPowerPillNodes();
        Node nearest = powerPillList.get(0);

        //finds the powerpill that is closest to the attacker
        for (int i = 1; i < powerPillList.size(); i++){
            if (game.getAttacker().getLocation().getPathDistance(powerPillList.get(i)) < game.getAttacker().getLocation().getPathDistance(nearest)){
                nearest = powerPillList.get(i);
            }
        }

        return nearest;
    }

    //Not using this method
  /*int focusPacman(Game game) {

     int nextDir = 0;
     int defender2 = 0;
     if (game.getDefender(defender2).isVulnerable()){
        nextDir = game.getDefender(defender2).getNextDir(game.getAttacker().getLocation(), false);

     }
     else {
        nextDir = game.getDefender(defender2).getNextDir(game.getAttacker().getLocation(), true);
     }

     return nextDir;
  }*/

	//Not using this one
  /*int scatter(Game game, long time, Defender defender, int defenderNum){
     int nextDir = EMPTY;
     List<Integer> possibleDirs = defender.getPossibleDirs();

     if (time < 100){
        nextDir = possibleDirs.get(Game.rng.nextInt(possibleDirs.size()));
     }

     else if (((int)time/100)%2 == 0){ //fix this condition??
        if (defender.getLocation().getPathDistance(game.getAttacker().getLocation()) < 10){
           nextDir = game.getDefender(defenderNum).getNextDir(game.getAttacker().getLocation(), !defender.isVulnerable());
        }
        else{
           nextDir = possibleDirs.get(Game.rng.nextInt(possibleDirs.size()));
        }
     }

     return nextDir;
  }*/


	//Not using this one either
  /*int focusPacman(Game game, Maze maze, int defenderNum) {

     int nextDir = 0;
     List<Node> locatePowerPills = maze.getPowerPillNodes();
     for (int i = 0; i < locatePowerPills.size(); i++) {
        if (game.getDefender(defenderNum).isVulnerable()) {
           nextDir = game.getDefender(defenderNum).getNextDir(game.getAttacker().getLocation(), false);

        } else {
           nextDir = game.getDefender(defenderNum).getNextDir(game.getAttacker().getLocation(), true);
        }
        if (game.checkPowerPill(locatePowerPills.get(i)) && (game.getAttacker().getLocation().getPathDistance(locatePowerPills.get(0)) == 10)) {
           nextDir = game.getDefender(defenderNum).getNextDir(game.getAttacker().getLocation(), false);
        }
     }
     return nextDir;
  }*/
}

