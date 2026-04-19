package domain;


/**
 * Write a description of class TowerContest here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TowerContest {
    private Tower proof;
    
    public String solve(int n, int h) {
        Tower proof = new Tower(200, 200);
        String input = n + " " + h;
        
        return proof.icpcProblem(input);
    }
    
    public void simulate(int n, int h) {
        Tower proof = new Tower(200, 200);
        String input = n + " " + h;
        
        proof.icpcProblem(input);
    }
}