!create_and_use.

+!create_and_use : true
  <- !setupTools;
  	println("artefatos criados").

// create the tools
+!setupTools: true 
  <- makeArtifact("entrada","artifacts.ArtefatoEntrada",[],A1);
  	 makeArtifact("saida","artifacts.ArtefatoSaida",[],A2);
	 
	 //makeArtifact("atuador_por_individuos","artifacts.AbordagemPorIndividuo",[],A3);
	 //makeArtifact("atuador_por_classes","artifacts.AbordagemPorClasses",[],A4);
	 //makeArtifact("atuador_por_categorias","artifacts.AbordagemPorCategoria",[],A5)
	 
	 makeArtifact("atuador_por_individuos1","artifacts.AbordagemPorIndividuo",[],A3);
	 makeArtifact("atuador_por_individuos2","artifacts.AbordagemPorIndividuo",[],A6);
	 makeArtifact("atuador_por_individuos3","artifacts.AbordagemPorIndividuo",[],A9);
	 makeArtifact("atuador_por_classes1","artifacts.AbordagemPorClasses",[],A4);
	 makeArtifact("atuador_por_classes2","artifacts.AbordagemPorClasses",[],A7);
	 makeArtifact("atuador_por_classes3","artifacts.AbordagemPorClasses",[],A10);
	 makeArtifact("atuador_por_categorias1","artifacts.AbordagemPorCategoria",[],A5);
	 makeArtifact("atuador_por_categorias2","artifacts.AbordagemPorCategoria",[],A8);
	 makeArtifact("atuador_por_categorias3","artifacts.AbordagemPorCategoria",[],A11).

  
