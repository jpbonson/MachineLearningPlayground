package com.anji.backgammon;

import com.anji.integration.Activator;
import java.util.ArrayList;

/**
 * Backgammon subject whose moves are determined by a INPUTS_SIZEx1 neural network.
 * @author Jessica Pauli de C. Bonson
 */
public class BackgammonDefaultPlayer implements BackgammonPlayer
{
    private Activator activator = null;
    
    public BackgammonDefaultPlayer(Activator anActivator)
    {
        this.activator = anActivator;
    }
    
    /**
    * @return String unique ID
    */
    @Override
    public String getPlayerId()
    {
        return activator.getName();
    }

    /**
     * reset player state; after a call to reset, the player should be in the same state as it was
     * when created; i.e., it has no memory of previous games played
     */
    @Override
    public void reset()
    {
        activator.reset();
    }
    
    @Override
    public BoardState move( ArrayList<BoardState> moves ) {
        if(moves.isEmpty())
        {
            return null;
        }
        else
        {
            double[] moveList = new double[ moves.size() ];
            double[] nnOutput;
            double[] gammonInput;
            int i = 0;
            for(BoardState board: moves)
            {
                gammonInput = board.codifyToNEAT();
                nnOutput = activator.next(gammonInput);
                moveList[i] = nnOutput[0];
                i++;
            }
            
            double max = -Double.MAX_VALUE;
            int myMove = -1;
            for ( int j = 0; j < moves.size(); j++ ) 
            {
                if ( max <= moveList[ j ] )
                {
                    myMove = j;
                    max = moveList[ j ];
                }
            }
            
            return moves.get(myMove);
        }
    }
    
    @Override
    public String toString()
    {
	return activator.getName();
    }
}
