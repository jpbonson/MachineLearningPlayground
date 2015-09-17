package com.anji.backgammon;

import java.util.ArrayList;

public class BoardState 
{
    private Position board_positions[];
    private Position white_bar;
    private Position black_bar;
    private Position white_beared;
    private Position black_beared;
    private final int quantPositions = 24;
    private ArrayList<DiceAtPosition> usedDiceValues = new ArrayList<DiceAtPosition>();
    
    public BoardState()
    {
        board_positions = new Position[quantPositions];
    }
    
    public BoardState(BoardState previousBoardState, DiceAtPosition useDiceValue, Color currentPlayer)
    {
        // (create a BoardState based on the previous one, add a DiceAtPosition, and update state 
        // of the BoardState based on the DiceAtPosition)
        // add DiceAtPosition to usedDiceValues
        // don't use the same dice more than one time, unless it's a double dice (restriction at the Board class)
        
        // copy previous board
        white_bar = new Position(previousBoardState.getWhiteBar().getColor(),
                previousBoardState.getWhiteBar().getQuantityCheckers());
        black_bar = new Position(previousBoardState.getBlackBar().getColor(),
                previousBoardState.getBlackBar().getQuantityCheckers());
        white_beared = new Position(previousBoardState.getWhiteBeared().getColor(),
                previousBoardState.getWhiteBeared().getQuantityCheckers());
        black_beared = new Position(previousBoardState.getBlackBeared().getColor(),
                previousBoardState.getBlackBeared().getQuantityCheckers());
        usedDiceValues.addAll(previousBoardState.getUsedDiceValues());
        
        Position[] previousPos = previousBoardState.getBoardPositions();
        board_positions = new Position[quantPositions];
        for(int i = 0; i < previousPos.length; i++)
            board_positions[i] = new Position(previousPos[i].getColor(),previousPos[i].getQuantityCheckers());
    
        // update actual board based on the new useDiceValue
        // it's assumed that is a valid move
        usedDiceValues.add(useDiceValue);
        updateBoard(useDiceValue, currentPlayer);
    }
    
    public void updateBoard(DiceAtPosition useDiceValue, Color currentPlayer)
    {
        Color oponentPlayer;
        if(currentPlayer == Color.WHITE)
        {
            int nextPos;
            oponentPlayer = Color.BLACK;
            if(useDiceValue.getPosition() == Board.WHITE_BAR_POS)
            {
                white_bar.removeChecker(currentPlayer);
                nextPos = useDiceValue.getDiceValue() -1;
                
            }
            else // regular position
            {
                board_positions[useDiceValue.getPosition()].removeChecker(currentPlayer);
                nextPos = useDiceValue.getPosition() + useDiceValue.getDiceValue();
            }
            
            if(nextPos >= quantPositions)// bear off
            {
                white_beared.addChecker(currentPlayer);
            }
            else // regular move
            {
                if(board_positions[nextPos].getColor() ==  oponentPlayer) // hit?
                {
                    board_positions[nextPos].removeChecker(oponentPlayer);
                    black_bar.addChecker(oponentPlayer);
                }
                board_positions[nextPos].addChecker(currentPlayer);
            }
        }
        else
        {
            int nextPos;
            oponentPlayer = Color.WHITE;
            if(useDiceValue.getPosition() == Board.BLACK_BAR_POS)
            {
                black_bar.removeChecker(currentPlayer);
                nextPos = 24 - useDiceValue.getDiceValue();
                
            }
            else // regular position
            {
                board_positions[useDiceValue.getPosition()].removeChecker(currentPlayer);
                nextPos = useDiceValue.getPosition() - useDiceValue.getDiceValue();
            }
            
            if(nextPos < 0)// bear off
            {
                black_beared.addChecker(currentPlayer);
            }
            else // regular move
            {
                if(board_positions[nextPos].getColor() ==  oponentPlayer) // hit?
                {
                    board_positions[nextPos].removeChecker(oponentPlayer);
                    white_bar.addChecker(oponentPlayer);
                }
                board_positions[nextPos].addChecker(currentPlayer);
            }
        }
    }
    
    public boolean isEndGame()
    {
        if(getWhiteBeared().getQuantityCheckers() == 15)
            return true;
        if(getBlackBeared().getQuantityCheckers() == 15)
            return true;
        return false;
    }
    
    public ArrayList<DiceAtPosition> getUsedDiceValues()
    {
        return usedDiceValues;
    }
    
    public Position[] getBoardPositions()
    {
        return board_positions;
    }
    
    public Position getWhiteBar()
    {
        return white_bar;
    }
    
    public Position getBlackBar()
    {
        return black_bar;
    }
    
    public Position getWhiteBeared()
    {
        return white_beared;
    }
    
    public Position getBlackBeared()
    {
        return black_beared;
    }
    
    public boolean addCheckerToBar(Color color)
    {
        if(color == Color.WHITE)
            return white_bar.addChecker(color);
        else
            return black_bar.addChecker(color);
    }
    
    private void initializeEmptyBoard()
    {
        for (int i=0; i<quantPositions; i++) { 
            board_positions[i] = new Position();
        }
        white_bar = new Position(Color.WHITE);
        black_bar = new Position(Color.BLACK);
        white_beared = new Position(Color.WHITE);
        black_beared = new Position(Color.BLACK);
    }
    
    public void initializeRegularBoard()
    {
        initializeEmptyBoard();
        board_positions[0].setColorsetQuantityCheckers(Color.WHITE, 2);
        board_positions[5].setColorsetQuantityCheckers(Color.BLACK, 5);
        board_positions[7].setColorsetQuantityCheckers(Color.BLACK, 3);
        board_positions[11].setColorsetQuantityCheckers(Color.WHITE, 5);
        board_positions[12].setColorsetQuantityCheckers(Color.BLACK, 5);
        board_positions[16].setColorsetQuantityCheckers(Color.WHITE, 3);
        board_positions[18].setColorsetQuantityCheckers(Color.WHITE, 5);
        board_positions[23].setColorsetQuantityCheckers(Color.BLACK, 2);
    }
    
    public void initializeRacingBoard()
    {
        initializeEmptyBoard();
        int pos;
        for(int i = 0; i < 15; i++)
        {
            pos = RobustRandomNumber.random(18, 6);
            board_positions[pos].addChecker(Color.WHITE);
        }
        for(int i = 0; i < 15; i++)
        {
            pos = RobustRandomNumber.random(0, 6);
            board_positions[pos].addChecker(Color.BLACK);
        }
    }
    
    public String toString_TreeDebugVersion()
    {
        String str = "";
        
        str+="---------------------------------------------------------\n";
        str+="DEBUG VERSION\n";
        for(int i = 0; i < usedDiceValues.size(); i++)
            str+="used dice ["+i+"]: "+usedDiceValues.get(i) +"\n";
        str+="\n";
        str+="\n";
        str+=toString();
        str+="\n";
        
        return str;
    }
    
    @Override
    public String toString()
    {
        String str = "";
        
        str+="white bar: "+white_bar.getQuantityCheckers()+" | "+"white beared: "+white_beared.getQuantityCheckers()+"\n";
        str+="black bar: "+black_bar.getQuantityCheckers()+" | "+"black beared: "+black_beared.getQuantityCheckers()+"\n";
        str+="\n";
        str+="IS RACING? "+checkForRacingSituation()+"\n\n";
        
        for(int i = 0; i < board_positions.length/4; i++)
            str+=String.format("[%-2d] : %-18s| [%-2d] : %-18s\n", 6-i+5, board_positions[6-i+5], i+12, board_positions[i+12]);
        str+="\n";
        for(int i = board_positions.length/4; i < board_positions.length/2; i++)
            str+=String.format("[%-2d] : %-18s| [%-2d] : %-18s\n", 6-(i-5), board_positions[6-(i-5)], i+12, board_positions[i+12]);
       
        return str;
    }
    
    public double[] codifyToNEAT()
    {
        /*double[] array = new double[4+quantPositions+quantPositions+1];
        
        array[0] = white_beared.getQuantityCheckers();
        array[1] = black_beared.getQuantityCheckers();
        array[2] = white_bar.getQuantityCheckers();
        array[3] = black_bar.getQuantityCheckers();
        // board positions with WHITE checkers
        for(int i = 0; i < board_positions.length; i++)
            if(board_positions[i].getColor() == Color.WHITE)
                array[i+4] = board_positions[i].getQuantityCheckers();
            else
                array[i+4] = 0.0;
        // board positions with BLACK checkers
        for(int i = 0; i < board_positions.length; i++)
            if(board_positions[i].getColor() == Color.WHITE)
                array[i+4+board_positions.length] = board_positions[i].getQuantityCheckers();
            else
                array[i+4+board_positions.length] = 0.0;
        array[4+quantPositions+quantPositions] = 1.0; // bias
        
        return array;*/
        
        if(BackgammonGame.currentPlayer == Color.WHITE) // So I'm white (opposite direction)
        {
            double[] array = new double[4+quantPositions+1];//29

            array[0] = -black_bar.getQuantityCheckers();
            
            // board positions
            for(int i = 0; i < board_positions.length; i++)
            {
                if(board_positions[i].getColor() == Color.WHITE)
                    array[24-i] = board_positions[i].getQuantityCheckers();
                else if(board_positions[i].getColor() == Color.BLACK)
                    array[24-i] = -board_positions[i].getQuantityCheckers();
                else
                    array[24-i] = 0.0;
            }
            
            array[1+quantPositions] = white_bar.getQuantityCheckers();
            array[2+quantPositions] = white_beared.getQuantityCheckers();
            array[3+quantPositions] = -black_beared.getQuantityCheckers();
            array[4+quantPositions] = 1.0; // bias

            return array;
        }
        else // I'm black (direction = ok)
        {
            double[] array = new double[4+quantPositions+1];//29

            array[0] = -white_bar.getQuantityCheckers();
            
            // board positions
            for(int i = 0; i < board_positions.length; i++)
            {
                if(board_positions[i].getColor() == Color.WHITE)
                    array[i+1] = -board_positions[i].getQuantityCheckers();
                else if(board_positions[i].getColor() == Color.BLACK)
                    array[i+1] = board_positions[i].getQuantityCheckers();
                else
                    array[i+1] = 0.0;
            }
            
            array[1+quantPositions] = black_bar.getQuantityCheckers();
            array[2+quantPositions] = black_beared.getQuantityCheckers();
            array[3+quantPositions] = -white_beared.getQuantityCheckers();
            array[4+quantPositions] = 1.0; // bias

            return array;
        }
    }
    
    public int[] codifyToNEAT_pubeval()
    {
        if(BackgammonGame.currentPlayer == Color.WHITE) // So I'm white (opposite direction)
        {
            int[] array = new int[4+quantPositions];//28

            array[0] = -black_bar.getQuantityCheckers();
            
            // board positions
            for(int i = 0; i < board_positions.length; i++)
            {
                if(board_positions[i].getColor() == Color.WHITE)
                    array[24-i] = board_positions[i].getQuantityCheckers();
                else if(board_positions[i].getColor() == Color.BLACK)
                    array[24-i] = -board_positions[i].getQuantityCheckers();
                else
                    array[24-i] = 0;
            }
            
            array[1+quantPositions] = white_bar.getQuantityCheckers();
            array[2+quantPositions] = white_beared.getQuantityCheckers();
            array[3+quantPositions] = -black_beared.getQuantityCheckers();

            return array;
        }
        else // I'm black (direction = ok)
        {
            int[] array = new int[4+quantPositions];//28

            array[0] = -white_bar.getQuantityCheckers();
            
            // board positions
            for(int i = 0; i < board_positions.length; i++)
            {
                if(board_positions[i].getColor() == Color.WHITE)
                    array[i+1] = -board_positions[i].getQuantityCheckers();
                else if(board_positions[i].getColor() == Color.BLACK)
                    array[i+1] = board_positions[i].getQuantityCheckers();
                else
                    array[i+1] = 0;
            }
            
            array[1+quantPositions] = black_bar.getQuantityCheckers();
            array[2+quantPositions] = black_beared.getQuantityCheckers();
            array[3+quantPositions] = -white_beared.getQuantityCheckers();

            return array;
        }
    }
    
    // race = check if the board situation is racing or contact (true = racing situation)
    public boolean checkForRacingSituation()
    {
        // achar checker mais longe de cada player
        if(white_bar.getQuantityCheckers() > 0 || black_bar.getQuantityCheckers() > 0)
            return false;
        
        int pos_away_white = -1;
        for(int i = 0; i < board_positions.length; i++)
            if(board_positions[i].getColor() == Color.WHITE)
            {
                pos_away_white = i;
                break;
            }
        
        if(pos_away_white == -1)
            return true;
        
        int pos_away_black = -1;
        for(int i = board_positions.length-1; i >= 0; i--)
            if(board_positions[i].getColor() == Color.BLACK)
            {
                pos_away_black = i;
                break;
            }
        
        if(pos_away_black == -1)
            return true;
        
        // se white_mais_longe_pos - black_mais_longe_pos > 0 == racing
        if(pos_away_white - pos_away_black > 0)
            return true;
        else
            return false;
    }
}
