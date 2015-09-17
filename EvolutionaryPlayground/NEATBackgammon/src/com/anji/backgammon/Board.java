package com.anji.backgammon;

import java.util.ArrayList;

public class Board {
    private boolean endGame = false;
    private BoardState boardState;
    private DiceRoller diceRoller = new DiceRoller();
    private int turn;
    private BackgammonGame game;
    public static final int WHITE_BAR_POS = 100;
    public static final int BLACK_BAR_POS = 200;
    public enum BoardConfig {REGULAR,RACING, RACING_QUAD2};
    
    public Board(BackgammonGame game) {
        boardState = new BoardState();
        turn = 0;
        this.game = game;
    }
    
    public void initializeBoard(BoardConfig config)
    {
        if(config == BoardConfig.REGULAR)
            boardState.initializeRegularBoard();
        if(config == BoardConfig.RACING)
            boardState.initializeRacingBoard();
        if(config == BoardConfig.RACING_QUAD2)
            boardState.initializeRacingBoard_quad2();
    }

    public boolean isEndGame()
    {
        return endGame;
    }
    
    public void nextTurn()
    {
        diceRoller.rollDices();
        turn++;
    }
    
    public int getTurn()
    {
        return turn;
    }
    
    public void doMove(BoardState move)
    {
        // if there is no valid moves, skip turn
        if(move == null)
            return;
        
        Color currentPlayer = game.getCurrentPlayer();
        // apply the usedDiceAtPositions to the actual board
        ArrayList<DiceAtPosition> usedDiceValues = move.getUsedDiceValues();
        for(DiceAtPosition dice: usedDiceValues)
            boardState.updateBoard(dice, currentPlayer);
        
        if(boardState.isEndGame())
            endGame = true;
    }
    
    public ArrayList<BoardState> getAllValidMoves()
    {
        int dice_values[] = diceRoller.getDiceRolls();
        Color currentPlayer = game.getCurrentPlayer();
        
        ArrayList<BoardState> nextBoardStates;
        ArrayList<BoardState> nextBoardStates2 = new ArrayList<BoardState>();
        ArrayList<BoardState> finalBoardStates = new ArrayList<BoardState>();
        
        BoardState currentBoardState = boardState;
        BoardState possibleWin;
        
        // create game tree for all sequences of dices
        if(dice_values.length == 2)
        {
            nextBoardStates = getAllValidMoves_Iteration(currentPlayer, currentBoardState, dice_values);
            if(checkIfMoveWins(nextBoardStates))
            {
                finalBoardStates.add(getWinMove(nextBoardStates));
                return finalBoardStates;
            }
            
            for(int i = 0; i < nextBoardStates.size(); i++)
                nextBoardStates2.addAll(getAllValidMoves_Iteration(currentPlayer, nextBoardStates.get(i), dice_values));
            if(checkIfMoveWins(nextBoardStates2))
            {
                finalBoardStates.add(getWinMove(nextBoardStates2));
                return finalBoardStates;
            }
            
            finalBoardStates = nextBoardStates2;
        }
        if(dice_values.length == 4)
        {
            nextBoardStates = getAllValidMoves_Iteration(currentPlayer, currentBoardState, dice_values);
            if(checkIfMoveWins(nextBoardStates))
            {
                finalBoardStates.add(getWinMove(nextBoardStates));
                return finalBoardStates;
            }
            
            for(int i = 0; i < nextBoardStates.size(); i++)
                nextBoardStates2.addAll(getAllValidMoves_Iteration(currentPlayer, nextBoardStates.get(i), dice_values));
            if(checkIfMoveWins(nextBoardStates2))
            {
                finalBoardStates.add(getWinMove(nextBoardStates2));
                return finalBoardStates;
            }
            
            nextBoardStates = new ArrayList<BoardState>();
            for(int i = 0; i < nextBoardStates2.size(); i++)
                nextBoardStates.addAll(getAllValidMoves_Iteration(currentPlayer, nextBoardStates2.get(i), dice_values));
            if(checkIfMoveWins(nextBoardStates))
            {
                finalBoardStates.add(getWinMove(nextBoardStates));
                return finalBoardStates;
            }
            
            nextBoardStates2 = new ArrayList<BoardState>();
            for(int i = 0; i < nextBoardStates.size(); i++)
                nextBoardStates2.addAll(getAllValidMoves_Iteration(currentPlayer, nextBoardStates.get(i), dice_values));
            if(checkIfMoveWins(nextBoardStates2))
            {
                finalBoardStates.add(getWinMove(nextBoardStates2));
                return finalBoardStates;
            }
            
            finalBoardStates = nextBoardStates2;
        }

        return finalBoardStates;
    }
    
    private boolean checkIfMoveWins(ArrayList<BoardState> boardStates)
    {
        for(BoardState board: boardStates)
        {
            if(board.isEndGame())
                return true;
        }
        return false;
    }
    
    private BoardState getWinMove(ArrayList<BoardState> boardStates)
    {
        for(BoardState board: boardStates)
        {
            if(board.isEndGame())
                return board;
        }
        return null;
    }
    
    private ArrayList<BoardState> getAllValidMoves_Iteration(Color currentPlayer, BoardState currentBoardState, int dice_values[])
    {
        ArrayList<BoardState> nextBoardStates = new ArrayList<BoardState>();
        
        // which column apply the dice (for the current BoardState)
        ArrayList<Integer> validColumns = validColumns(currentPlayer, currentBoardState);
        // dice ordering
        // for each Integer in the ArrayList, create a DiceAtPosition for each valid dice in that position
        ArrayList<DiceAtPosition> diceAtPosition = new ArrayList<DiceAtPosition>();
        DiceAtPosition diceAtPositionTemp;
        for(int i = 0; i < validColumns.size(); i++)
            for(int j = 0; j < dice_values.length; j++)
                if(validDiceAtPosition(currentPlayer, validColumns.get(i), dice_values[j], currentBoardState))
                {
                    diceAtPositionTemp = new DiceAtPosition(validColumns.get(i), dice_values[j]);
                    if(!diceAtPosition.contains(diceAtPositionTemp))
                        diceAtPosition.add(diceAtPositionTemp);
                }
        // for each DiceAtPosition generate a specific BoardState
        for(int i = 0; i < diceAtPosition.size(); i++)
            if(checkIfDiffDice(dice_values.length, currentBoardState, diceAtPosition.get(i)))
                nextBoardStates.add(new BoardState(currentBoardState, diceAtPosition.get(i), currentPlayer));
        
        return nextBoardStates;
    }
    
    // dont use the same dice value more than one time (unless its a double dice)
    private boolean checkIfDiffDice(int quant_dices, BoardState currentBoardState, DiceAtPosition currentDiceAtPosition)
    {
        if(quant_dices == 4) // it's a double dice
            return true;
        
        for(int i = 0; i < currentBoardState.getUsedDiceValues().size(); i++)
            if(currentBoardState.getUsedDiceValues().get(i).getDiceValue() == currentDiceAtPosition.getDiceValue())
            {
                return false;
            }
        return true;
    }
    
    private boolean validDiceAtPosition(Color currentPlayer, Integer pos, int dice_value, BoardState currentBoardState)
    {
        Position[] board_pos = currentBoardState.getBoardPositions();
        
        if(currentPlayer == Color.WHITE)
        {
            int nextPos;
            if(pos == WHITE_BAR_POS){
                nextPos = dice_value -1;
            }    
            else{
                nextPos = pos + dice_value;
            }
            
            // bear-off?
            if(nextPos >= board_pos.length)
            {
                if(isValidToBearoff(currentPlayer, currentBoardState))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }

            Color nextPosColor = board_pos[nextPos].getColor();
            if(nextPosColor == Color.NEUTRAL || nextPosColor == Color.WHITE)
                return true;

            // hit?
            if(nextPosColor == Color.BLACK && board_pos[nextPos].getQuantityCheckers() == 1)
                return true;
        }
        else // black
        {
            int nextPos;
            if(pos == BLACK_BAR_POS){
                nextPos = 24 - dice_value;
            }    
            else{
                nextPos = pos - dice_value;
            }
            
            // bear-off?
            if(nextPos < 0)
            {
                if(isValidToBearoff(currentPlayer, currentBoardState))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }

            Color nextPosColor = board_pos[nextPos].getColor();
            if(nextPosColor == Color.NEUTRAL || nextPosColor == Color.BLACK)
                return true;

            // hit?
            if(nextPosColor == Color.WHITE && board_pos[nextPos].getQuantityCheckers() == 1)
                return true;
        }
        return false;
    }
    
    private boolean isValidToBearoff(Color currentPlayer, BoardState currentBoardState)
    {
        Position[] board_pos = currentBoardState.getBoardPositions();
        if(currentPlayer == Color.WHITE)
        {
            for(int i = 0; i <= 17; i++)
                if(board_pos[i].getColor() == Color.WHITE)
                {
                    return false;
                }
            return true;
        }
        else
        {
            for(int i = 6; i < board_pos.length; i++)
                if(board_pos[i].getColor() == Color.BLACK)
                {
                    return false;
                }
            return true;
        }
    }
    
    // add valid column positions
    private ArrayList<Integer> validColumns(Color currentPlayer, BoardState currentBoardState)
    {
        ArrayList<Integer> validColumns = new ArrayList<Integer>();
        
        // check if it's at bar
        if(currentPlayer == Color.WHITE && currentBoardState.getWhiteBar().getQuantityCheckers() > 0)
        {
            validColumns.add(WHITE_BAR_POS);
            return validColumns;
        }
        if(currentPlayer == Color.BLACK && currentBoardState.getBlackBar().getQuantityCheckers() > 0)
        {
            validColumns.add(BLACK_BAR_POS);
            return validColumns;
        }
        
        // if it isn't at bar, then get all columns with the player's color
        Position[] boardPositions = currentBoardState.getBoardPositions();
        for(int i = 0; i < boardPositions.length; i++)
        {
            if(boardPositions[i].getColor() == currentPlayer)
                validColumns.add(i);
        }
        return validColumns;
    }
    
    @Override
    public String toString()
    {
        String str = "";
        
        str+="---------------------------------------------------------\n";
        str+="Backgammon, turn: "+turn+"\n";
        int dice_values[] = diceRoller.getDiceRolls();
        if(dice_values != null)
            for(int i = 0; i < dice_values.length; i++)
                str+="dice ["+i+"]: "+dice_values[i]+"| ";
        str+="\n";
        str+="\n";
        str+=boardState;
        str+="\n";
        
        return str;
    }
}
