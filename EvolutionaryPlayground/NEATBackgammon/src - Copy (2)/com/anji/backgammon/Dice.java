package com.anji.backgammon;

public class Dice
{
    public Dice()
    {
        
    }
    
    public int roll()
    {
        return RobustRandomNumber.random(1, 6);
    }
}
