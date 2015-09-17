package com.anji.backgammon;

import com.anji.tournament.Game;
import com.anji.tournament.GameConfiguration;
import com.anji.tournament.GameResults;
import com.anji.tournament.PlayerResults;
import com.anji.tournament.ScoringWeights;
import com.anji.util.Configurable;
import org.apache.log4j.Logger;

public class BackgammonGame implements Game, Configurable
{
    private static Logger logger = Logger.getLogger( BackgammonGame.class );
    private GameConfiguration gameConfig = GameConfiguration.DEFAULT;
    private Board board;
    public static Color currentPlayer;
    private boolean showMatch = false;
    private final static String SHOW_KEY = "backgammon.game.showMatch";
    private final static String SHOW_DEFAULT = "false";
    private final static String SHOW_VERBOSE = "true";
    private String show = SHOW_DEFAULT;
    private Board.BoardConfig boardConfig = Board.BoardConfig.RACING;
    private final static String GAMECONFIG_KEY = "backgammon.game.gameConfig";
    private final static String GAMECONFIG_DEFAULT = "default";
    private final static String GAMECONFIG_RACING = "racing";
    private final static String GAMECONFIG_RACING_QUAD2 = "racingQ2";
    private String gameconfig = GAMECONFIG_DEFAULT;
    
    public BackgammonGame()
    {
	super();
    }
    
    @Override
    public GameResults play( PlayerResults contestantResults, PlayerResults opponentResults )
    {
        if(show.equals(SHOW_DEFAULT))
            showMatch = false;
        else
            showMatch = true;
        
        if(gameconfig.equals(GAMECONFIG_DEFAULT))
            boardConfig = Board.BoardConfig.REGULAR;
        else if(gameconfig.equals(GAMECONFIG_RACING))
            boardConfig = Board.BoardConfig.RACING;
        else
            boardConfig = Board.BoardConfig.RACING_QUAD2;
        
        GameResults results = new GameResults();

	// players
	BackgammonPlayer player1 = (BackgammonPlayer) contestantResults.getPlayer();
	BackgammonPlayer player2 = (BackgammonPlayer) opponentResults.getPlayer();
	if ( gameConfig.doResetPlayers() ) {
		player1.reset();
		player2.reset();
	}
        
        board = new Board(this);
        board.initializeBoard(boardConfig);
        
        if(showMatch)
        {
            logger.info("START GAME\n");
            logger.info("Player 1 "+player1.getPlayerId()+" as WHITE\n");
            logger.info("Player 2 "+player2.getPlayerId()+" as BLACK\n");
        }
        
        if(RobustRandomNumber.random(0, 2) == 0)
        {
            currentPlayer = Color.WHITE;
        }
        else
        {
            currentPlayer = Color.BLACK;
        }
        
        if(showMatch)
        {
            logger.info(currentPlayer+" STARTS!\n");
            logger.info(board+"\n");
        }

        BoardState move;

        while(!board.isEndGame())
        {
            board.nextTurn();
            
            if(currentPlayer == Color.WHITE)
            {
                move = player1.move(board.getAllValidMoves());
                board.doMove(move);
                currentPlayer = Color.BLACK;
            }
            else
            {
                move = player2.move(board.getAllValidMoves());
                board.doMove(move);
                currentPlayer = Color.WHITE;
            }

            if(showMatch)
            {
                logger.info(board+"\n");
            }
        }
        if(showMatch)
            logger.info("END GAME\n");
        
        if(currentPlayer == Color.BLACK)
        {
            results.incrementPlayer1Wins( 1 );
            if(showMatch)
                logger.info("WHITE WINS!\n");
        }
        else
        {
            results.incrementPlayer1Losses( 1 );
            if(showMatch)
                logger.info("BLACK WINS!\n");
        }
        
        contestantResults.getResults().increment( results.getPlayer1Stats() );
	opponentResults.getResults().increment( results.getPlayer2Stats() );
	return results;
    }
    
    public Color getCurrentPlayer()
    {
        return currentPlayer;
    }
    
    @Override
    public Class requiredPlayerClass()
    {
	return BackgammonPlayer.class;
    }

    @Override
    public int getMaxScore( ScoringWeights aWeights )
    {
        return aWeights.getWinValue();
    }

    @Override
    public int getMinScore( ScoringWeights aWeights )
    {
        return aWeights.getLossValue();
    }

    @Override
    public void init( com.anji.util.Properties props ) throws Exception {
        gameConfig = (GameConfiguration) props.singletonObjectProperty( GameConfiguration.class );
        show = props.getProperty( SHOW_KEY, SHOW_DEFAULT );
        gameconfig = props.getProperty( GAMECONFIG_KEY, GAMECONFIG_DEFAULT );
    }
}
