package com.anji.backgammon;

public class DiceAtPosition
{
    private final int pos;
    private final int dice_value;
    
    public DiceAtPosition(int pos, int dice_value)
    {
        this.pos = pos;
        this.dice_value = dice_value;
    }
    
    public int getPosition()
    {
        return pos;
    }
    
    public int getDiceValue()
    {
        return dice_value;
    }
    
    @Override
    public String toString()
    {
        return "p: "+pos+" | d: "+dice_value;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        DiceAtPosition d = (DiceAtPosition)obj;
        if(d.getPosition() == pos && d.getDiceValue() == dice_value)
            return true;
        return false;
    }
}

