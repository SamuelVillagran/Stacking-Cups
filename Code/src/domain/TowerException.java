package domain;


/**
 * Write a description of class TowerException here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TowerException extends Exception {
    private String message;
    public static final String DONT_EXISTS_CUP = "Don't exists a cup where put a new type of cup";
	public static final String NOT_ALLOWED_TYPE = "Type of Cup or Lid don´t allowed";
	public static final String DONT_EXISTS_LASTCUP = "This action can't do it beacause don't have set a cup at the tower";
	public static final String CANT_CREATE_TOWER = "Can't create tower because invalidates the invariant";
	public static final String CANT_PUSH_CUP = "Can't push the cup because invalidates the invatiant";
	public static final String IS_OUT_SCREEN = "This action makes out of screen";
	public static final String INTERIOR_CUP_EXISTS = "This action can't be realized because there is a interior cup already";
	public static final String INTERIOR_CUP_DONT_EXISTS = "This action can't be realized because there isn't a interior cup";
	public static final String NO_ITEMS = "This actions can't be realized because there aren't items at the tower";
	public static final String DONT_EXISTS_LID = "Don't exists lid";
	public static final String DOESNT_HAVE_ASSOCIATED_CUP = "Don't exists their associated cup";
    
    public TowerException(String message) {
        super(message);
    }
}
