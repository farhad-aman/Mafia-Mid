package model.roles;

import model.logic.God;

/**
 * This class contains the role of mafia
 * 
 * @author Farhad Aman
 * @version 1.0
 */
public class Mafia extends Role
{
    

    public Mafia()
    {

    }

    @Override
    public String toString() 
    {
        return God.ANSI_YELLOW + "Mafia" + God.ANSI_RESET;
    }
}
