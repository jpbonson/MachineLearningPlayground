package com.anji.backgammon;

public class DiceRoller
{
    private Dice dice1 = new Dice();
    private Dice dice2 = new Dice();
    private int values[];
    
    public DiceRoller()
    {
        
    }
    
    public int[] getDiceRolls()
    {
        return values;
    }
    
    public void rollDices()
    {
        int roll1 = dice1.roll();
        int roll2 = dice2.roll();
        
        if(roll1 != roll2)
        {
            values = new int[2];
            values[0] = roll1;
            values[1] = roll2;
        }
        else
        {
            values = new int[4];
            for(int i = 0; i < 4; i++)
            {
                values[i] = roll1;
            }
        }
    }
}

