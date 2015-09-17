package com.anji.backgammon;

import com.anji.util.Configurable;
import com.anji.util.Properties;
import java.util.ArrayList;

public class BackgammonRandomPlayer implements BackgammonPlayer, Configurable
{
    private static final String playerId = "Backgammon Random Player";
    
    @Override
    public String getPlayerId() {
        return playerId;
    }

    @Override
    public BoardState move( ArrayList<BoardState> moves ) {
        if(moves.isEmpty())
        {
            return null;
        }
        else
        {
            return moves.get((int)(Math.random() * moves.size()));
        }
    }

    @Override
    public void init( Properties props ) throws Exception {

    }

    @Override
    public void reset() {
        // no-op
    }

    @Override
    public String toString() {
        return playerId;
    }
}
