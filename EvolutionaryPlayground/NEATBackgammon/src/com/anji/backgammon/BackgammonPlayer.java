package com.anji.backgammon;

import com.anji.tournament.Player;
import java.util.ArrayList;

public interface BackgammonPlayer extends Player // similar to com.anji.ttt.BoardPlayer
{
    /**
    * @param boardStates
    * @return next move
    */
    public BoardState move( ArrayList<BoardState> moves );
}
