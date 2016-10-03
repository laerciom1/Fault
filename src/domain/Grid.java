package domain;

import java.util.ArrayList;
import java.util.Random;

public class Grid {
	private NodeGrid[][] grid;
	private Random random;
	private int lines, columns, apps;
	
	public Grid(AdjacencyMatrix am, CommunicationMatrix cm){
		this.random = new Random();
		this.columns = am.getColumns();
		this.lines = am.getLines();
		grid = new NodeGrid[lines][columns];
		
		for(int i = 0; i < lines; i++){			// Setando os atributos dos roteadores
			for(int j = 0; j < columns; j++){
				grid[i][j] = new NodeGrid((i*columns)+j+1, -1, true, 1.0);
			}
		}
		
		this.apps = cm.getTasks();
		allocateApps(apps);
	}
	
	public void allocateApps(int apps){
		int remaining = apps; // N aplicacoes
		if(remaining > columns*lines){
			System.out.println("!!! ERRO !!!\nNao ha espaco para alocar " + remaining +
								" aplicacoes no grid!\nUse um grid com mais roteadores!\n\n");
		}
		else{	
			deallocateApps();
			boolean allocated;
			int randomRouter;
			while(remaining >= 0){
				allocated = false;
				while(!allocated){									// Enquando nao alocar
					randomRouter = random.nextInt(columns*lines);	// gera um router aleatorio
					if(grid[(randomRouter/columns)][(randomRouter%columns)].getApp() == -1){
						grid[(randomRouter/columns)][(randomRouter%columns)].setApp(--remaining);
						allocated = true;						// sinaliza que alocou.
					}
				}
			}
		}
	}
	
	public void deallocateApps(){
		for(int i = 0; i < lines; i++){
			for(int j = 0; j < columns; j++){
				grid[i][j].setApp(-1);
			}
		}
	}
	
	public void reset(){
		allocateApps(apps);
	}
	
	public void injectFaults(double percentage){
		if(percentage == 0){
			for(int i = 0; i < lines; i++){
				for(int j = 0; j < columns; j++){
					grid[i][j].setWorking(true);
				}
			}
		}
		else{
			int faults = (int) ((lines*columns*percentage)/100);
			System.out.print("\nFaults: " + faults + "\n");
			boolean injected;
			int randomRouter;
			while(faults > 0){
				injected = false;
				while(!injected){									// Enquando nao injetar a falha
					randomRouter = random.nextInt(columns*lines);	// pega um router aleatorio
					if(grid[(randomRouter/columns)][(randomRouter%columns)].getWorking() == true){
														// se o router estiver funcionando
						grid[(randomRouter/columns)][(randomRouter%columns)].setWorking(false);
														// injeta falha
						injected = true;				// sinaliza que injetou a falha.
						faults--;
					}
				}
			}	
		}
	}
	
	public void printApps(){
		for(int i = 0; i < lines; i++){
			for(int j = 0; j < columns; j++){
				System.out.print("[");
				if(grid[i][j].getApp() < 10 && grid[i][j].getApp() > -1){
					System.out.print("0");
				}
				System.out.print(grid[i][j].getApp() + "] ");
			}
			System.out.print("\n");
		}
	}
	
	public void printIds(){
		for(int i = 0; i < lines; i++){
			for(int j = 0; j < columns; j++){
				System.out.print("[");
				if(grid[i][j].getId() < 10){
					System.out.print("0");
				}
				System.out.print(grid[i][j].getId() + "] ");
			}
			System.out.print("\n");
		}
	}
	
	public void printIdsNApps(){
		for(int i = 0; i < lines; i++){
			for(int j = 0; j < columns; j++){
				System.out.print("[");
				if(grid[i][j].getId() < 10){
					System.out.print("0");
				}
				System.out.print(grid[i][j].getId() + "|");
				if(grid[i][j].getApp() < 10 && grid[i][j].getApp() > -1){
					System.out.print("0");
				}
				System.out.print(grid[i][j].getApp() + "] ");
			}
			System.out.print("\n");
		}
	}
	
	public void printAll(){
		for(int i = 0; i < lines; i++){
			for(int j = 0; j < columns; j++){
				System.out.print("[");
				if(grid[i][j].getId() < 10){
					System.out.print("0");
				}
				System.out.print(grid[i][j].getId() + "|");
				if(grid[i][j].getApp() < 10 && grid[i][j].getApp() > -1){
					System.out.print("0");
				}
				System.out.print(grid[i][j].getApp() + "] ");
			}
			System.out.print("\n");
			for(int k = 0; k < columns; k++){
				System.out.print("[");
				if(grid[i][k].getWorking()){
					System.out.print("11");
				}
				else{
					System.out.print("00");
				}
				System.out.print("|  ] ");
			}
			System.out.print("\n\n");
		}
	}
	
	public void printCommunications(CommunicationMatrix cm){
		int[][] communications = cm.getMatrix();
		int tasks = cm.getTasks();
		int iC,iL, jC, jL;
		
		for(int i = 1; i < tasks; i++){ 		//tasks é o numero de tarefas
			iC = getColumn(i);						// Guarda posicao da aplicacao
			iL = getLine(i); 						// trabalhada atualmente
			for(int j = 0; j < tasks; j++){ 	// Busca aplicacoes que estao conectadas a aplicacao i;
				if(communications[i][j] != 0){ 	// Achou uma aplicacao conectada a i
					jC = getColumn(j); 				// Busca a posicao da aplicacao
					jL = getLine(j); 				// conectada a i que foi encontrada
				 	
					System.out.print(i + " -> " + j + " | " + ((iL*columns)+iC+1) + " -> " + ((jL*columns)+jC+1) + "\n");	
				}
			}
		}
	}

	public int getLine(int app){
		for(int i = 0; i < lines; i++){
			for(int j = 0; j < columns; j++){
				if(grid[i][j].getApp() == app){
					return i;
				}
			}
		}
		System.out.print("Nao encontrou a aplicacao " + app + "\n");
		return -1; 
	}

	public int getColumn(int app){
		for(int i = 0; i < lines; i++){
			for(int j = 0; j < columns; j++){
				if(grid[i][j].getApp() == app){
					return j;
				}
			}
		}
		System.out.print("Nao encontrou a aplicacao " + app + "\n");
		return -1; 
	}
	
	public int[][] XYPadrao(CommunicationMatrix cm){	
		int[][] communications = cm.getMatrix();
		int tasks = cm.getTasks();
		int iC,iL, jC, jL, auxC, auxL;
		ArrayList<int[]> headsNTails = new ArrayList<int[]>();
		ArrayList<int[]> paths = new ArrayList<int[]>();
		for(int i = 0; i < tasks; i++){ //nodes é o numero de nós do grafo
			iC = getColumn(i); // Coluna
			iL = getLine(i); // Linha
			for(int j = 0; j < tasks; j++){ // Busca aplicacoes que estao conectadas a aplicacao i;
				auxC = iC; // Seta novamente o aux pois ele sera
				auxL = iL; // alterado caso exista uma busca de caminho
				if(communications[i][j] != 0){ // Achou uma aplicacao conectada a i
					jC = getColumn(j); // Busca a posicao da aplicacao
					jL = getLine(j); // conectada a i que foi encontrada
					int[] headNTail = {i, j};// Vetor que vai guardar o caminho feito entre i e j;
												// Node inicial (i) 
												// Node final (j)	
												// "Caminho de i para j:"
												// Isso eh usado para saber a qual ligacao o caminho pertence
												// uma vez que os 2 primeiros elementos do vetor sempre serao
												// origem e destino.
					headsNTails.add(headNTail);
					
					ArrayList<Integer> actualPath = new ArrayList<Integer>();
					if(grid[auxL][auxC].getWorking()){
						actualPath.add((auxL*columns)+auxC+1);
					}
					else{
						actualPath.add((auxL*columns)+auxC+1);
						actualPath.add(-1);
					}
					
					if(!actualPath.contains(-1)){
						while(auxC != jC){ // Itera primeiramente nas colunas (largura/horizontal/eixo x);
		 					if(auxC < jC){
		 						auxC++;
		 						actualPath.add((auxL*columns)+auxC+1);
		 						if(!grid[auxL][auxC].getWorking()){
		 							actualPath.add(-1);
		 							break;
		 						}
							}
		 					
		 					if(auxC > jC){
		 						auxC--;
		 						actualPath.add((auxL*columns)+auxC+1);
		 						if(!grid[auxL][auxC].getWorking()){
		 							actualPath.add(-1);
		 							break;
		 						}
		 					}
						}
					}
	 				
	 				if(!actualPath.contains(-1)){
						while(auxL != jL){ // Iterando agora nas linhas (altura/vertical/eixo y)							
							if(auxL < jL){
								auxL++;
								actualPath.add((auxL*columns)+auxC+1);
								if(!grid[auxL][auxC].getWorking()){
									actualPath.add(-1);
									break;
								}
							}
							
							if(auxL > jL){
								auxL--;
								actualPath.add((auxL*columns)+auxC+1);
								if(!grid[auxL][auxC].getWorking()){
									actualPath.add(-1);
									break;
								}
							}
						}
	 				}
					int[] ap = new int[actualPath.size()];
					int indice = 0;
					for (Integer n : actualPath) {
						ap[indice++] = n;
					}
					paths.add(ap); // adiciona o caminho no vetor que guarda os resultados
				}
			}
		}
		
		ArrayList<int[]> resultado_aux = new ArrayList<int[]>();
		for(int i = 0; i<paths.size(); i++){
			ArrayList<Integer> aux_al = new ArrayList<Integer>();
			aux_al.add(headsNTails.get(i)[0]);
			aux_al.add(headsNTails.get(i)[1]);
			for(int j = 0; j<paths.get(i).length; j++){
				aux_al.add(paths.get(i)[j]);
			}
			
			int[] aux_v = new int[aux_al.size()];
			int indice = 0;
			for (Integer n : aux_al) {
				aux_v[indice++] = n;
			}
			resultado_aux.add(aux_v);
		}
		
		int[][] resultado =  new int[resultado_aux.size()][];
		for(int i = 0; i < resultado_aux.size(); i++){
			resultado[i] = resultado_aux.get(i);
		}
		
		return resultado;
	}
	
	public int[][] YXPadrao(CommunicationMatrix cm){	
		int[][] communications = cm.getMatrix();
		int tasks = cm.getTasks();
		int iC,iL, jC, jL, auxC, auxL;
		ArrayList<int[]> headsNTails = new ArrayList<int[]>();
		ArrayList<int[]> paths = new ArrayList<int[]>();
		for(int i = 0; i < tasks; i++){ //nodes é o numero de nós do grafo
			iC = getColumn(i); // Coluna
			iL = getLine(i); // Linha
			for(int j = 0; j < tasks; j++){ // Busca aplicacoes que estao conectadas a aplicacao i;
				auxC = iC; // Seta novamente o aux pois ele sera
				auxL = iL; // alterado caso exista uma busca de caminho
				if(communications[i][j] != 0){ // Achou uma aplicacao conectada a i
					jC = getColumn(j); // Busca a posicao da aplicacao
					jL = getLine(j); // conectada a i que foi encontrada
					int[] headNTail = {i, j};// Vetor que vai guardar o caminho feito entre i e j;
												// Node inicial (i) 
												// Node final (j)	
												// "Caminho de i para j:"
												// Isso eh usado para saber a qual ligacao o caminho pertence
												// uma vez que os 2 primeiros elementos do vetor sempre serao
												// origem e destino.
					headsNTails.add(headNTail);
					
					ArrayList<Integer> actualPath = new ArrayList<Integer>();
					if(grid[auxL][auxC].getWorking()){
						actualPath.add((auxL*columns)+auxC+1);
					}
					else{
						actualPath.add((auxL*columns)+auxC+1);
						actualPath.add(-1);
					}
					
					if(!actualPath.contains(-1)){
						while(auxL != jL){ // Iterando agora nas linhas (altura/vertical/eixo y)							
							if(auxL < jL){
								auxL++;
								actualPath.add((auxL*columns)+auxC+1);
								if(!grid[auxL][auxC].getWorking()){
									actualPath.add(-1);
									break;
								}
							}
							
							if(auxL > jL){
								auxL--;
								actualPath.add((auxL*columns)+auxC+1);
								if(!grid[auxL][auxC].getWorking()){
									actualPath.add(-1);
									break;
								}
							}
						}
					}
	 				
	 				if(!actualPath.contains(-1)){
	 					while(auxC != jC){ // Itera primeiramente nas colunas (largura/horizontal/eixo x);
		 					if(auxC < jC){
		 						auxC++;
		 						actualPath.add((auxL*columns)+auxC+1);
		 						if(!grid[auxL][auxC].getWorking()){
		 							actualPath.add(-1);
		 							break;
		 						}
							}
		 					
		 					if(auxC > jC){
		 						auxC--;
		 						actualPath.add((auxL*columns)+auxC+1);
		 						if(!grid[auxL][auxC].getWorking()){
		 							actualPath.add(-1);
		 							break;
		 						}
		 					}
						}
	 				}
					int[] ap = new int[actualPath.size()];
					int indice = 0;
					for (Integer n : actualPath) {
						ap[indice++] = n;
					}
					paths.add(ap); // adiciona o caminho no vetor que guarda os resultados
				}
			}
		}
		
		ArrayList<int[]> resultado_aux = new ArrayList<int[]>();
		for(int i = 0; i<paths.size(); i++){
			ArrayList<Integer> aux_al = new ArrayList<Integer>();
			aux_al.add(headsNTails.get(i)[0]);
			aux_al.add(headsNTails.get(i)[1]);
			for(int j = 0; j<paths.get(i).length; j++){
				aux_al.add(paths.get(i)[j]);
			}
			
			int[] aux_v = new int[aux_al.size()];
			int indice = 0;
			for (Integer n : aux_al) {
				aux_v[indice++] = n;
			}
			resultado_aux.add(aux_v);
		}
		
		int[][] resultado =  new int[resultado_aux.size()][];
		for(int i = 0; i < resultado_aux.size(); i++){
			resultado[i] = resultado_aux.get(i);
		}
		
		return resultado;
	}
	
	public int[][] XYTorus(CommunicationMatrix cm){	
		int[][] communications = cm.getMatrix();
		int tasks = cm.getTasks();
		int iC,iL, jC, jL, auxC, auxL;
		ArrayList<int[]> headsNTails = new ArrayList<int[]>();
		ArrayList<int[]> paths = new ArrayList<int[]>();
		for(int i = 0; i < tasks; i++){ //nodes é o numero de nós do grafo
			iC = getColumn(i); // Coluna
			iL = getLine(i); // Linha
			for(int j = 0; j < tasks; j++){ // Busca aplicacoes que estao conectadas a aplicacao i;
				auxC = iC; // Seta novamente o aux pois ele sera
				auxL = iL; // alterado caso exista uma busca de caminho
				if(communications[i][j] != 0){ // Achou uma aplicacao conectada a i
					jC = getColumn(j); // Busca a posicao da aplicacao
					jL = getLine(j); // conectada a i que foi encontrada
					int[] headNTail = {i, j};// Vetor que vai guardar o caminho feito entre i e j;
												// Node inicial (i) 
												// Node final (j)	
												// "Caminho de i para j:"
												// Isso eh usado para saber a qual ligacao o caminho pertence
												// uma vez que os 2 primeiros elementos do vetor sempre serao
												// origem e destino.
					headsNTails.add(headNTail);
					
					ArrayList<Integer> actualPath = new ArrayList<Integer>();
					if(grid[auxL][auxC].getWorking()){
						actualPath.add((auxL*columns)+auxC+1);
					}
					else{
						actualPath.add((auxL*columns)+auxC+1);
						actualPath.add(-1);
					}
					
					if(!actualPath.contains(-1)){
						int test = jC - iC;
						if(test < 0){test*=-1;}
						if(test <= columns/2){
							while(auxC != jC){ // Itera primeiramente nas colunas (largura/horizontal/eixo x);
			 					if(auxC < jC){
			 						auxC++;
			 						actualPath.add((auxL*columns)+auxC+1);
			 						if(!grid[auxL][auxC].getWorking()){
			 							actualPath.add(-1);
			 							break;
			 						}
								}
			 					
			 					if(auxC > jC){
			 						auxC--;
			 						actualPath.add((auxL*columns)+auxC+1);
			 						if(!grid[auxL][auxC].getWorking()){
			 							actualPath.add(-1);
			 							break;
			 						}
			 					}
							}
						}
						else{
							while(auxC != jC){ // Itera primeiramente nas colunas (largura/horizontal/eixo x);
			 					if(iC < jC){
			 						auxC--;
			 						if(auxC < 0){auxC=columns-1;}
			 						actualPath.add((auxL*columns)+auxC+1);
			 						if(!grid[auxL][auxC].getWorking()){
			 							actualPath.add(-1);
			 							break;
			 						}
								}
			 					
			 					if(iC > jC){
			 						auxC++;
			 						if(auxC == columns){auxC=0;}
			 						actualPath.add((auxL*columns)+auxC+1);
			 						if(!grid[auxL][auxC].getWorking()){
			 							actualPath.add(-1);
			 							break;
			 						}
			 					}
							}
						}
					}
	 				
	 				if(!actualPath.contains(-1)){
	 					int test = jL - iL;
						if(test < 0){test*=-1;}
						if(test <= lines/2){
							while(auxL != jL){ // Iterando agora nas linhas (altura/vertical/eixo y)							
								if(auxL < jL){
									auxL++;
									actualPath.add((auxL*columns)+auxC+1);
									if(!grid[auxL][auxC].getWorking()){
										actualPath.add(-1);
										break;
									}
								}
								
								if(auxL > jL){
									auxL--;
									actualPath.add((auxL*columns)+auxC+1);
									if(!grid[auxL][auxC].getWorking()){
										actualPath.add(-1);
										break;
									}
								}
							}
						}
						else{
							while(auxL != jL){ // Iterando agora nas linhas (altura/vertical/eixo y)							
								if(iL < jL){
									auxL--;
									if(auxL < 0){auxL = lines-1;}
									actualPath.add((auxL*columns)+auxC+1);
									if(!grid[auxL][auxC].getWorking()){
										actualPath.add(-1);
										break;
									}
								}
								
								if(auxL > jL){
									auxL++;
									if(auxL == lines){auxL = 0;}
									actualPath.add((auxL*columns)+auxC+1);
									if(!grid[auxL][auxC].getWorking()){
										actualPath.add(-1);
										break;
									}
								}
							}
						}
	 				}
					int[] ap = new int[actualPath.size()];
					int indice = 0;
					for (Integer n : actualPath) {
						ap[indice++] = n;
					}
					paths.add(ap); // adiciona o caminho no vetor que guarda os resultados
				}
			}
		}
		
		ArrayList<int[]> resultado_aux = new ArrayList<int[]>();
		for(int i = 0; i<paths.size(); i++){
			ArrayList<Integer> aux_al = new ArrayList<Integer>();
			aux_al.add(headsNTails.get(i)[0]);
			aux_al.add(headsNTails.get(i)[1]);
			for(int j = 0; j<paths.get(i).length; j++){
				aux_al.add(paths.get(i)[j]);
			}
			
			int[] aux_v = new int[aux_al.size()];
			int indice = 0;
			for (Integer n : aux_al) {
				aux_v[indice++] = n;
			}
			resultado_aux.add(aux_v);
		}
		
		int[][] resultado =  new int[resultado_aux.size()][];
		for(int i = 0; i < resultado_aux.size(); i++){
			resultado[i] = resultado_aux.get(i);
		}
		
		return resultado;
	}
	
	public int[][] YXTorus(CommunicationMatrix cm){	
		int[][] communications = cm.getMatrix();
		int tasks = cm.getTasks();
		int iC,iL, jC, jL, auxC, auxL;
		ArrayList<int[]> headsNTails = new ArrayList<int[]>();
		ArrayList<int[]> paths = new ArrayList<int[]>();
		for(int i = 0; i < tasks; i++){ 		//nodes é o numero de nós do grafo
			iC = getColumn(i); 					// Coluna
			iL = getLine(i); 					// Linha
			for(int j = 0; j < tasks; j++){ 	// Busca aplicacoes que estao conectadas a aplicacao i;
				auxC = iC; 						// Seta novamente o aux pois ele sera
				auxL = iL; 						// alterado caso exista uma busca de caminho
				if(communications[i][j] != 0){ 	// Achou uma aplicacao conectada a i
					jC = getColumn(j); 			// Busca a posicao da aplicacao
					jL = getLine(j); 			// conectada a i que foi encontrada
					int[] headNTail = {i, j};	// Vetor que vai guardar o caminho feito entre i e j;
												// Node inicial (i) 
												// Node final (j)	
												// "Caminho de i para j:"
												// Isso eh usado para saber a qual ligacao o caminho pertence
												// uma vez que os 2 primeiros elementos do vetor sempre serao
												// origem e destino.
					headsNTails.add(headNTail);
					
					ArrayList<Integer> actualPath = new ArrayList<Integer>();
					if(grid[auxL][auxC].getWorking()){
						actualPath.add((auxL*columns)+auxC+1);
					}
					else{
						actualPath.add((auxL*columns)+auxC+1);
						actualPath.add(-1);
					}
					
					if(!actualPath.contains(-1)){
	 					int test = jL - iL;
						if(test < 0){test*=-1;}
						if(test <= lines/2){
							while(auxL != jL){ // Iterando nas linhas						
								if(auxL < jL){
									auxL++;
									actualPath.add((auxL*columns)+auxC+1);
									if(!grid[auxL][auxC].getWorking()){
										actualPath.add(-1);
										break;
									}
								}
								
								if(auxL > jL){
									auxL--;
									actualPath.add((auxL*columns)+auxC+1);
									if(!grid[auxL][auxC].getWorking()){
										actualPath.add(-1);
										break;
									}
								}
							}
						}
						else{
							while(auxL != jL){							
								if(iL < jL){
									auxL--;
									if(auxL < 0){auxL = lines-1;}
									actualPath.add((auxL*columns)+auxC+1);
									if(!grid[auxL][auxC].getWorking()){
										actualPath.add(-1);
										break;
									}
								}
								
								if(auxL > jL){
									auxL++;
									if(auxL == lines){auxL = 0;}
									actualPath.add((auxL*columns)+auxC+1);
									if(!grid[auxL][auxC].getWorking()){
										actualPath.add(-1);
										break;
									}
								}
							}
						}
	 				}
					
					if(!actualPath.contains(-1)){
						int test = jC - iC;
						if(test < 0){test*=-1;}
						if(test <= columns/2){
							while(auxC != jC){ // Iterando nas linhas
			 					if(auxC < jC){
			 						auxC++;
			 						actualPath.add((auxL*columns)+auxC+1);
			 						if(!grid[auxL][auxC].getWorking()){
			 							actualPath.add(-1);
			 							break;
			 						}
								}
			 					
			 					if(auxC > jC){
			 						auxC--;
			 						actualPath.add((auxL*columns)+auxC+1);
			 						if(!grid[auxL][auxC].getWorking()){
			 							actualPath.add(-1);
			 							break;
			 						}
			 					}
							}
						}
						else{
							while(auxC != jC){
			 					if(iC < jC){
			 						auxC--;
			 						if(auxC < 0){auxC=columns-1;}
			 						actualPath.add((auxL*columns)+auxC+1);
			 						if(!grid[auxL][auxC].getWorking()){
			 							actualPath.add(-1);
			 							break;
			 						}
								}
			 					
			 					if(iC > jC){
			 						auxC++;
			 						if(auxC == columns){auxC=0;}
			 						actualPath.add((auxL*columns)+auxC+1);
			 						if(!grid[auxL][auxC].getWorking()){
			 							actualPath.add(-1);
			 							break;
			 						}
			 					}
							}
						}
					}
					
					int[] ap = new int[actualPath.size()];
					int indice = 0;
					for (Integer n : actualPath) {
						ap[indice++] = n;
					}
					paths.add(ap); // adiciona o caminho no vetor que guarda os resultados
				}
			}
		}
		
		ArrayList<int[]> resultado_aux = new ArrayList<int[]>();
		for(int i = 0; i<paths.size(); i++){
			ArrayList<Integer> aux_al = new ArrayList<Integer>();
			aux_al.add(headsNTails.get(i)[0]);
			aux_al.add(headsNTails.get(i)[1]);
			for(int j = 0; j<paths.get(i).length; j++){
				aux_al.add(paths.get(i)[j]);
			}
			
			int[] aux_v = new int[aux_al.size()];
			int indice = 0;
			for (Integer n : aux_al) {
				aux_v[indice++] = n;
			}
			resultado_aux.add(aux_v);
		}
		
		int[][] resultado =  new int[resultado_aux.size()][];
		for(int i = 0; i < resultado_aux.size(); i++){
			resultado[i] = resultado_aux.get(i);
		}
		
		return resultado;
	}
	
	public int[][] XXYYPadrao(CommunicationMatrix cm){	
		int[][] communications = cm.getMatrix();
		int tasks = cm.getTasks();
		int iC,iL, jC, jL, auxC, auxL;
		ArrayList<int[]> headsNTails = new ArrayList<int[]>();
		ArrayList<int[]> paths = new ArrayList<int[]>();
		for(int i = 0; i < tasks; i++){ 		//TASKS é o numero de nós do grafo
			iC = getColumn(i); 					// Coluna
			iL = getLine(i); 					// Linha
			for(int j = 0; j < tasks; j++){ 	// Busca aplicacoes que estao conectadas a aplicacao i;
				auxC = iC;						// Seta novamente o aux pois ele sera
				auxL = iL; 						// alterado caso exista uma busca de caminho
				if(communications[i][j] != 0){	// Achou uma aplicacao conectada a i
					jC = getColumn(j); 			// Busca a posicao da aplicacao
					jL = getLine(j);			// conectada a i que foi encontrada
					int[] headNTail = {i, j};	// Vetor que vai guardar o caminho feito entre i e j;
												// Node inicial (i) 
												// Node final (j)	
												// "Caminho de i para j:"
												// Isso eh usado para saber a qual ligacao o caminho pertence
												// uma vez que os 2 primeiros elementos do vetor sempre serao
												// origem e destino.
					headsNTails.add(headNTail);
					
					ArrayList<Integer> actualPath = new ArrayList<Integer>();
					if(grid[auxL][auxC].getWorking()){
						actualPath.add((auxL*columns)+auxC+1);
					}
					else{
						actualPath.add((auxL*columns)+auxC+1);
						actualPath.add(-1);
					}
					
					boolean hor = true; 	// TRUE - prioridade andar na horizontal
											// FALSE - prioridade andar na vertical
					
					if(!actualPath.contains(-1)){
						while((auxC != jC || auxL != jL) && !actualPath.contains(-1)){
							if(hor){
			 					if(auxC < jC){
			 						if(grid[auxL][auxC+1].getWorking()){
				 						auxC++;
				 						if(!actualPath.contains((auxL*columns)+auxC+1)){
				 							actualPath.add((auxL*columns)+auxC+1);
				 						}
				 						else{
				 							actualPath.add(-1);
				 							break;
				 						}
				 						
			 						}
				 					else if(!grid[auxL][auxC+1].getWorking()){
				 						boolean dUpOK = false;
				 						boolean dDownOK = false;
				 						int discovererUp = auxL;
				 						int discovererDown = auxL;
				 						
				 						while(!(dUpOK && dDownOK)){
				 							if(discovererUp > 0 && !dUpOK){
				 								if(grid[discovererUp-1][auxC].getWorking()){
				 									discovererUp--;
					 								if(grid[discovererUp][auxC].getWorking()){
					 									dUpOK = true;
					 								}
				 								}
				 								else{
				 									discovererUp = -1;
				 									dUpOK = true;
				 								}
				 							}
				 							else{
				 								discovererUp = -1;
				 								dUpOK = true;
				 							}
				 							
				 							if(discovererDown < lines-1 && !dDownOK){
				 								if(grid[discovererDown+1][auxC].getWorking()){
				 									discovererDown++;
					 								if(grid[discovererDown][auxC].getWorking()){
					 									dDownOK = true;
					 								}
				 								}
				 								else{
				 									discovererDown = -1;
				 									dDownOK = true;
				 								}
				 							}
				 							else{
				 								discovererDown = -1;
				 								dDownOK = true;
				 							}
				 						}
				 						
				 						if(discovererUp == -1 && discovererDown == -1){
				 							actualPath.add(-1);
				 							break;
				 						}
				 						else if(discovererUp != -1 && discovererDown != -1){
				 							int chosen = Math.abs(discovererUp - jL) <= Math.abs(discovererDown - jL) ? 
				 											discovererUp : discovererDown;
				 							
				 							while(auxL != chosen){
				 								if(auxL > chosen){
				 									auxL--;
				 								}
				 								else{auxL++;}
				 								if(!actualPath.contains((auxL*columns)+auxC+1)){
				 									actualPath.add((auxL*columns)+auxC+1);
				 								}
				 								else{
				 									actualPath.add(-1);
				 									break;
				 								}
				 							}
				 						}
				 						else{
				 							if(discovererUp != -1){
				 								while(auxL != discovererUp){
				 									auxL--;
				 									if(!actualPath.contains((auxL*columns)+auxC+1)){
					 									actualPath.add((auxL*columns)+auxC+1);
					 								}
					 								else{
					 									actualPath.add(-1);
					 									break;
					 								}
					 							}
				 							}
				 							else{
				 								while(auxL != discovererDown){
				 									auxL++;
				 									if(!actualPath.contains((auxL*columns)+auxC+1)){
					 									actualPath.add((auxL*columns)+auxC+1);
					 								}
					 								else{
					 									actualPath.add(-1);
					 									break;
					 								}
					 							}
				 							}
				 						}
				 					}
			 					}
			 					
			 					else if(auxC > jC){
			 						if(grid[auxL][auxC-1].getWorking()){
				 						auxC--;
				 						if(!actualPath.contains((auxL*columns)+auxC+1)){
		 									actualPath.add((auxL*columns)+auxC+1);
		 								}
		 								else{
		 									actualPath.add(-1);
		 									break;
		 								}
			 						}
				 					else if(!grid[auxL][auxC-1].getWorking()){
				 						boolean dUpOK = false;
				 						boolean dDownOK = false;
				 						int discovererUp = auxL;
				 						int discovererDown = auxL;
				 						
				 						while(!(dUpOK && dDownOK)){
				 							if(discovererUp > 0 && !dUpOK){
				 								if(grid[discovererUp-1][auxC].getWorking()){
				 									discovererUp--;
					 								if(grid[discovererUp][auxC].getWorking()){
					 									dUpOK = true;
					 								}
				 								}
				 								else{
				 									discovererUp = -1;
				 									dUpOK = true;
				 								}
				 							}
				 							else{
				 								discovererUp = -1;
				 								dUpOK = true;
				 							}
				 							
				 							if(discovererDown < lines-1 && !dDownOK){
				 								if(grid[discovererDown+1][auxC].getWorking()){
				 									discovererDown++;
					 								if(grid[discovererDown][auxC].getWorking()){
					 									dDownOK = true;
					 								}
				 								}
				 								else{
				 									discovererDown = -1;
				 									dDownOK = true;
				 								}
				 							}
				 							else{
				 								discovererDown = -1;
				 								dDownOK = true;
				 							}
				 						}
				 						
				 						if(discovererUp == -1 && discovererDown == -1){
				 							actualPath.add(-1);
				 							break;
				 						}
				 						else if(discovererUp != -1 && discovererDown != -1){
				 							int chosen = Math.abs(discovererUp - jL) <= Math.abs(discovererDown - jL) ? 
				 											discovererUp : discovererDown;
				 							
				 							while(auxL != chosen){
				 								if(auxL > chosen){
				 									auxL--;
				 								}
				 								else{auxL++;}
				 								if(!actualPath.contains((auxL*columns)+auxC+1)){
				 									actualPath.add((auxL*columns)+auxC+1);
				 								}
				 								else{
				 									actualPath.add(-1);
				 									break;
				 								}
				 							}
				 						}
				 						else{
				 							if(discovererUp != -1){
				 								while(auxL != discovererUp){
				 									auxL--;
				 									if(!actualPath.contains((auxL*columns)+auxC+1)){
					 									actualPath.add((auxL*columns)+auxC+1);
					 								}
					 								else{
					 									actualPath.add(-1);
					 									break;
					 								}
					 							}
				 							}
				 							else{
				 								while(auxL != discovererDown){
				 									auxL++;
				 									if(!actualPath.contains((auxL*columns)+auxC+1)){
					 									actualPath.add((auxL*columns)+auxC+1);
					 								}
					 								else{
					 									actualPath.add(-1);
					 									break;
					 								}
					 							}
				 							}
				 						}
				 					}
			 					}
			 					if(auxC == jC){hor = !hor;}
							}
		 					
							if(!hor){
			 					if(auxL < jL){
			 						if(grid[auxL+1][auxC].getWorking()){
				 						auxL++;
				 						if(!actualPath.contains((auxL*columns)+auxC+1)){
		 									actualPath.add((auxL*columns)+auxC+1);
		 								}
		 								else{
		 									actualPath.add(-1);
		 									break;
		 								}
			 						}
				 					else if(!grid[auxL+1][auxC].getWorking()){
				 						boolean dLeftOK = false;
				 						boolean dRightOK = false;
				 						int discovererLeft = auxC;
				 						int discovererRight = auxC;
				 						
				 						while(!(dLeftOK && dRightOK)){
				 							if(discovererLeft > 0 && !dLeftOK){
				 								if(grid[auxL][discovererLeft-1].getWorking()){
				 									discovererLeft--;
					 								if(grid[auxL][discovererLeft].getWorking()){
					 									dLeftOK = true;
					 								}
				 								}
				 								else{
				 									discovererLeft = -1;
				 									dLeftOK = true;
				 								}
				 							}
				 							else{
				 								discovererLeft = -1;
				 								dLeftOK = true;
				 							}
				 							
				 							if(discovererRight < columns-1 && !dRightOK){
				 								if(grid[auxL][discovererRight+1].getWorking()){
				 									discovererRight++;
					 								if(grid[auxL][discovererRight].getWorking()){
					 									dRightOK = true;
					 								}
				 								}
				 								else{
				 									discovererRight = -1;
				 									dRightOK = true;
				 								}
				 							}
				 							else{
				 								discovererRight = -1;
				 								dRightOK = true;
				 							}
				 						}
				 						
				 						if(discovererLeft == -1 && discovererRight == -1){
				 							actualPath.add(-1);
				 							break;
				 						}
				 						else if(discovererLeft != -1 && discovererRight != -1){
				 							int chosen = Math.abs(discovererLeft - jC) <= Math.abs(discovererRight - jC) ? 
				 											discovererLeft : discovererRight;
				 							
				 							while(auxC != chosen){
				 								if(auxC > chosen){
				 									auxC--;
				 								}
				 								else{auxC++;}
				 								if(!actualPath.contains((auxL*columns)+auxC+1)){
				 									actualPath.add((auxL*columns)+auxC+1);
				 								}
				 								else{
				 									actualPath.add(-1);
				 								}
				 							}
				 						}
				 						else{
				 							if(discovererLeft != -1){
				 								while(auxC != discovererLeft){
				 									auxC--;
				 									if(!actualPath.contains((auxL*columns)+auxC+1)){
					 									actualPath.add((auxL*columns)+auxC+1);
					 								}
					 								else{
					 									actualPath.add(-1);
					 									break;
					 								}
					 							}
				 							}
				 							else{
				 								while(auxC != discovererRight){
				 									auxC++;
				 									if(!actualPath.contains((auxL*columns)+auxC+1)){
					 									actualPath.add((auxL*columns)+auxC+1);
					 								}
					 								else{
					 									actualPath.add(-1);
					 									break;
					 								}
					 							}
				 							}
				 						}
				 					}
			 					}
			 					
			 					else if(auxL > jL){
			 						if(grid[auxL-1][auxC].getWorking()){
				 						auxL--;
				 						if(!actualPath.contains((auxL*columns)+auxC+1)){
		 									actualPath.add((auxL*columns)+auxC+1);
		 								}
		 								else{
		 									actualPath.add(-1);
		 									break;
		 								}
			 						}
				 					else if(!grid[auxL-1][auxC].getWorking()){
				 						boolean dLeftOK = false;
				 						boolean dRightOK = false;
				 						int discovererLeft = auxC;
				 						int discovererRight = auxC;
				 						
				 						while(!(dLeftOK && dRightOK)){
				 							if(discovererLeft > 0 && !dLeftOK){
				 								if(grid[auxL][discovererLeft-1].getWorking()){
				 									discovererLeft--;
					 								if(grid[auxL][discovererLeft].getWorking()){
					 									dLeftOK = true;
					 								}
				 								}
				 								else{
				 									discovererLeft = -1;
				 									dLeftOK = true;
				 								}
				 							}
				 							else{
				 								discovererLeft = -1;
				 								dLeftOK = true;
				 							}
				 							
				 							if(discovererRight < columns-1 && !dRightOK){
				 								if(grid[auxL][discovererRight+1].getWorking()){
				 									discovererRight++;
					 								if(grid[auxL][discovererRight].getWorking()){
					 									dRightOK = true;
					 								}
				 								}
				 								else{
				 									discovererRight = -1;
				 									dRightOK = true;
				 								}
				 							}
				 							else{
				 								discovererRight = -1;
				 								dRightOK = true;
				 							}
				 						}
				 						
				 						if(discovererLeft == -1 && discovererRight == -1){
				 							actualPath.add(-1);
				 							break;
				 						}
				 						else if(discovererLeft != -1 && discovererRight != -1){
				 							int chosen = Math.abs(discovererLeft - jC) <= Math.abs(discovererRight - jC) ? 
				 											discovererLeft : discovererRight;
				 							
				 							while(auxC != chosen){
				 								if(auxC > chosen){
				 									auxC--;
				 								}
				 								else{auxC++;}
				 								if(!actualPath.contains((auxL*columns)+auxC+1)){
				 									actualPath.add((auxL*columns)+auxC+1);
				 								}
				 								else{
				 									actualPath.add(-1);
				 									break;
				 								}
				 							}
				 						}
				 						else{
				 							if(discovererLeft != -1){
				 								while(auxC != discovererLeft){
				 									auxC--;
				 									if(!actualPath.contains((auxL*columns)+auxC+1)){
					 									actualPath.add((auxL*columns)+auxC+1);
					 								}
					 								else{
					 									actualPath.add(-1);
					 									break;
					 								}
					 							}
				 							}
				 							else{
				 								while(auxC != discovererRight){
				 									auxC++;
				 									if(!actualPath.contains((auxL*columns)+auxC+1)){
					 									actualPath.add((auxL*columns)+auxC+1);
					 								}
					 								else{
					 									actualPath.add(-1);
					 									break;
					 								}
					 							}
				 							}
				 						}
				 					}
			 					}
			 					if(auxL == jL){hor = !hor;}
							}
						}
					}
					
					int[] ap = new int[actualPath.size()];
					int indice = 0;
					for (Integer n : actualPath) {
						ap[indice++] = n;
					}
					paths.add(ap); // adiciona o caminho no vetor que guarda os resultados
				}
			}
		}
		
		ArrayList<int[]> resultado_aux = new ArrayList<int[]>();
		for(int i = 0; i<paths.size(); i++){
			ArrayList<Integer> aux_al = new ArrayList<Integer>();
			aux_al.add(headsNTails.get(i)[0]);
			aux_al.add(headsNTails.get(i)[1]);
			for(int j = 0; j<paths.get(i).length; j++){
				aux_al.add(paths.get(i)[j]);
			}
			int[] aux_v = new int[aux_al.size()];
			int indice = 0;
			for (Integer n : aux_al) {
				aux_v[indice++] = n;
			}
			resultado_aux.add(aux_v);
		}
		
		int[][] resultado =  new int[resultado_aux.size()][];
		for(int i = 0; i < resultado_aux.size(); i++){
			resultado[i] = resultado_aux.get(i);
		}
		
		return resultado;
	}
}
