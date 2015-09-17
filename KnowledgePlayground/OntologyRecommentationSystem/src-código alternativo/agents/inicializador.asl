!create_and_use.

+!create_and_use : true
  <- !setupTools;
  	+cont(0);
  	println("artefatos criados").

+results1(R): true
<- .my_name(Me);
	println(Me,": got results1 ", R);
	-cont(V);
	+cont(V+1);
	//.nth(0,R,X);
	//println(" OPA: ", X);
	!rankResults.
	
+results2(R): true
<- .my_name(Me);
	println(Me,": got results2 ", R);
	-cont(V);
	+cont(V+1);
	//.nth(0,R,X);
	//println(" OPA: ", X);
	!rankResults.
	
+results3(R): true
<- .my_name(Me);
	println(Me,": got results3 ", R);
	-cont(V);
	+cont(V+1);
	//.nth(0,R,X);
	//println(" OPA: ", X);
	!rankResults.
	
+!rankResults: cont(3) & results1(R1) & results2(R2) & results3(R3)
<- println("RANK!!!!!!!!!!!!");
	.intersection(R1,R2,X);
	.intersection(R2,R3,Y);
	.intersection(R1,R3,Z);
	.union(X,Y,Parcial);
	.union(Parcial,Z,Results);
	.length(Results,L);
	!showResults(Results, L).
	
+!rankResults: true.

+!showResults(Results, L): L > 0
<- .nth(0,Results,A);
	println("1: ", A);
	.nth(1,Results,B);
	println("2: ", B);
	.nth(2,Results,C);
	println("3: ", C);
	.nth(3,Results,D);
	println("4: ", D);
	.nth(4,Results,E);
	println("5: ", E);
	put2(Results).

+!showResults(Results, L): true
<- println("Nenhum resultado encontrado.").
	
// create the tools
+!setupTools: true 
  <- makeArtifact("entrada","artifacts.ArtefatoEntrada",[],A1);
  	 makeArtifact("saida","artifacts.ArtefatoSaida",[],A2);
	 
	 makeArtifact("atuador_por_individuos","artifacts.AbordagemPorIndividuo",[],A3);
	 makeArtifact("atuador_por_classes","artifacts.AbordagemPorClasses",[],A4);
	 makeArtifact("atuador_por_categorias","artifacts.AbordagemPorCategoria",[],A5).

  
