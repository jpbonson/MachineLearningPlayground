package com.anji.backgammon;

import com.anji.integration.Activator;
import com.anji.integration.ActivatorTranscriber;
import com.anji.integration.TranscriberException;
import com.anji.tournament.Player;
import com.anji.tournament.PlayerTranscriber;
import com.anji.util.Configurable;
import com.anji.util.Properties;
import org.jgap.Chromosome;

public class BackgammonPlayerTranscriber implements PlayerTranscriber, Configurable
{
    private ActivatorTranscriber activatorTranscriber;
    
    /**
     * default constructor
     */
    public BackgammonPlayerTranscriber()
    {
	super();
    }
    
    public BackgammonPlayer newBackgammonPlayer( Chromosome genotype ) throws TranscriberException
    {
	Activator activator = activatorTranscriber.newActivator( genotype );
	return new BackgammonDefaultPlayer( activator );
    }
    
    @Override
    public Player newPlayer( Chromosome genotype ) throws TranscriberException
    {
        return newBackgammonPlayer(genotype);
    }
    
    @Override
    public Object transcribe( Chromosome genotype ) throws TranscriberException
    {
	return newBackgammonPlayer(genotype);
    }

    @Override
    public Class getPhenotypeClass()
    {
        return BackgammonPlayer.class;
    }

    @Override
    public void init( Properties props ) throws Exception
    {
        activatorTranscriber = (ActivatorTranscriber) props
			.singletonObjectProperty( ActivatorTranscriber.class );
    }
}
