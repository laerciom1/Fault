package domain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import dijkstra.*;

public class Grid {
	private NodeGrid[][] grid;
	private Random random;
	private int lines, columns;
	private int routers, faults;
	
	public Grid(AdjacencyMatrix am){
		this.random = new Random();
		this.columns = am.getColumns();
		this.lines = am.getLines();
		this.routers = lines*columns;
		grid = new NodeGrid[lines][columns];
		
		for(int i = 0; i < lines; i++){			// Setando os atributos dos roteadores
			for(int j = 0; j < columns; j++){
				grid[i][j] = new NodeGrid((i*columns)+j+1, true, 1.0);
			}
		}
	}
	
	public int getNumberOfRouters(){
		return routers;
	}

	public int getNumberOfFaults(){
		return faults;
	}
	
	public void allocateAppByRouter(int app, int router){
		grid[(router/columns)][(router%columns)].addApp(app);
	}
	
	public void allocateAppByPos(int app, int i, int j){
		grid[i][j].addApp(app);
	}
	
	public void allocateAppsSeq(int apps){
		int remaining = apps; // N aplicacoes
		deallocateApps();
		
		int i=0,j=0;
		while(remaining > 0){
			if(grid[i][j].isWorking()){
				grid[i][j].addApp(--remaining);
				j++;
			}
			else{
				j++;
			}
			
			if(j == columns){
				j=0;
				i++;
				if(i == lines){
					i=0;
				}
			}
		}
	}
	
	public void allocateAppsRand(int apps){
		int remaining = apps; // N aplicacoes
		deallocateApps();
	
		boolean allocated;
		int full_level = columns*lines-faults;
		int requisito = 1;
		int randomRouter;
		while(remaining > 0){
			allocated = false;
			while(!allocated){									// Enquando nao alocar
				randomRouter = random.nextInt(columns*lines);	// gera um router aleatorio
				if(grid[(randomRouter/columns)][(randomRouter%columns)].getApps().size() < requisito &&
						grid[(randomRouter/columns)][(randomRouter%columns)].isWorking()){
					grid[(randomRouter/columns)][(randomRouter%columns)].addApp(--remaining);
					full_level--;
					if(full_level == 0){
						full_level = columns*lines-faults;
						requisito++;
					}
					allocated = true;						// sinaliza que alocou.
				}
			}
		}
	}
	
	public void deallocateApps(){
		for(int i = 0; i < lines; i++){
			for(int j = 0; j < columns; j++){
				grid[i][j].clear();
			}
		}
	}
	
	public void injectFaultByNode(int router){
		if(grid[(router/columns)][(router%columns)].isWorking()){
			grid[(router/columns)][(router%columns)].setWorking(false);
			faults++;
		}
	}
	
	public void injectFaultListByNode(int[] routers){
		for(int i = 0; i < routers.length; i++){
			if(grid[(routers[i]/columns)][(routers[i]%columns)].isWorking()){
				grid[(routers[i]/columns)][(routers[i]%columns)].setWorking(false);
				faults++;
			}
		}
	}
	
	public void injectFaultByPos(int i, int j){
		grid[i][j].setWorking(false);
		faults++;
	}
	
	public void injectFaultsByPercentage(double percentage){
		int faults_aux = (int) ((lines*columns*percentage)/100);
		faults_aux -= faults;
		faults = faults_aux;
		boolean injected;
		int randomRouter;
		while(faults_aux > 0){
			injected = false;
			while(!injected){									// Enquando nao injetar a falha
				randomRouter = random.nextInt(columns*lines);	// pega um router aleatorio
				if(grid[(randomRouter/columns)][(randomRouter%columns)].isWorking() &&
						grid[(randomRouter/columns)][(randomRouter%columns)].getApps().size() == 0 ){
													// se o router estiver funcionando e não tiver tarefas alocadas
					grid[(randomRouter/columns)][(randomRouter%columns)].setWorking(false);
													// injeta falha
					injected = true;				// sinaliza que injetou a falha.
					faults_aux--;
				}
			}
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
	
	
	public void printAllocations(){
		for(int i = 0; i < columns*lines; i++){
			if(i < 9){
				System.out.print("Router [0" + (i+1) + "] (" + grid[(i/columns)][(i%columns)].getRealiability() + "), Apps: ");
			}
			else{
				System.out.print("Router [" + (i+1) + "] (" + grid[(i/columns)][(i%columns)].getRealiability() + "), Apps: ");
			}
			int aux = grid[(i/columns)][(i%columns)].getApps().size();
			for(int j = 0; j < aux; j++){
				if(j == aux-1){
					System.out.print(grid[(i/columns)][(i%columns)].getApps().get(j) + ".");
				}
				else{
					System.out.print(grid[(i/columns)][(i%columns)].getApps().get(j) + ", ");
				}
				
			}
			System.out.print("\n");
		}
	}
	
	public void printGrid(){
		for(int i = 0; i < lines; i++){
			System.out.print("+");
			for(int j = 0; j < columns; j++){
				System.out.print("-------+");
			}
			System.out.print("\n|");
			for(int j = 0; j < columns; j++){
				System.out.print("[");
				if(grid[i][j].getId() < 10){
					System.out.print("0");
				}
				System.out.print(grid[i][j].getId() + "|");
				if(grid[i][j].isWorking()){
					System.out.print("11]|");
				}
				else{
					System.out.print("00]|");
				}
			}
			System.out.print("\n");
		}
		System.out.print("+");
		for(int j = 0; j < columns; j++){
			System.out.print("-------+");
		}
		System.out.print("\n");
	}
	
	public void paintGrid(){
		JFrame main_frame = new JFrame("Fault");
		main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main_frame.setLayout(null);
		main_frame.setSize(8+10+(columns*64)+((columns-1)*5)+10, 30+10+(lines*64)+((lines-1)*5)+10);
		main_frame.setLocationRelativeTo(null);
		main_frame.setResizable(false);	
		JTextArea[][] routers = new JTextArea[lines][columns];
		for(int i = 0; i < lines; i++){
			for(int j = 0; j < columns; j++){
				routers[i][j] = new JTextArea();
				routers[i][j].setLineWrap(true);
				if(grid[i][j].isWorking()){
					routers[i][j].setBackground(new Color(153, 255,153));
				}
				else{
					routers[i][j].setBackground(new Color(255, 153,153));
				}
				routers[i][j].setBounds(10+(j*69),10+(i*69),64,64);
				routers[i][j].setBounds(10+(j*69),10+(i*69),64,64);
				String apps = new String();
				apps += "[" + (grid[i][j].getId()-1) + "] ";
				for(int a : grid[i][j].getApps()){
					apps += a+" ";
				}
				routers[i][j].setText(apps);
				routers[i][j].setVisible(true);
				routers[i][j].setVisible(true);
			}
		}
		
		
		
		for(int i = 0; i < lines; i++){
			for(int j = 0; j < columns; j++){
				main_frame.add(routers[i][j]);
			}
		}		
		main_frame.setVisible(true);
	}
	
	public void printCommunications(CommunicationMatrix cm){
		int[][] communications = cm.getMatrix();
		int tasks = cm.getTasks();
		int iC,iL, jC, jL;
		
		for(int i = 1; i < tasks; i++){ 		//tasks é o numero de tarefas
			int[] iPos = getPos(i);
			iL = iPos[0]; // Linha
			iC = iPos[1]; // Coluna
			for(int j = 0; j < tasks; j++){ 	// Busca aplicacoes que estao conectadas a aplicacao i;
				if(communications[i][j] != 0){ 	// Achou uma aplicacao conectada a i
					int[] jPos = getPos(j);
					jL = jPos[0]; 			// Busca a posicao da aplicacao
					jC = jPos[1];			// conectada a i que foi encontrada
				 	
					System.out.print(i + " -> " + j + " | " + ((iL*columns)+iC+1) + " -> " + ((jL*columns)+jC+1) + "\n");	
				}
			}
		}
	}

	public int[] getPos(int app){
		for(int i = 0; i < lines; i++){
			for(int j = 0; j < columns; j++){
				if(grid[i][j].getApps().contains(app)){
					int[] pos = new int[]{i, j};
					return pos;
				}
			}
		}
		System.out.print("Nao encontrou a aplicacao " + app + "\n");
		int[] pos = new int[]{-1, -1};
		return pos; 
	}
	/********************************************************************************************************************/
	public int[][] XYPadrao(CommunicationMatrix cm){	
		int[][] communications = cm.getMatrix();
		int tasks = cm.getTasks();
		int iC,iL, jC, jL, auxC, auxL;
		ArrayList<int[]> headsNTails = new ArrayList<int[]>();
		ArrayList<int[]> paths = new ArrayList<int[]>();
		for(int i = 0; i < tasks; i++){ //nodes é o numero de nós do grafo
			int[] iPos = getPos(i);
			iL = iPos[0]; // Coluna
			iC = iPos[1]; // Linha
			for(int j = 0; j < tasks; j++){ // Busca aplicacoes que estao conectadas a aplicacao i;
				auxC = iC; // Seta novamente o aux pois ele sera
				auxL = iL; // alterado caso exista uma busca de caminho
				if(communications[i][j] != 0){ // Achou uma aplicacao conectada a i
					int[] jPos = getPos(j);
					jL = jPos[0]; 			// Busca a posicao da aplicacao
					jC = jPos[1];			// conectada a i que foi encontrada
					int[] headNTail = {i, j};// Vetor que vai guardar o caminho feito entre i e j;
												// Node inicial (i) 
												// Node final (j)	
												// "Caminho de i para j:"
												// Isso eh usado para saber a qual ligacao o caminho pertence
												// uma vez que os 2 primeiros elementos do vetor sempre serao
												// origem e destino.
					headsNTails.add(headNTail);
					
					ArrayList<Integer> actualPath = new ArrayList<Integer>();
					if(grid[auxL][auxC].isWorking()){
						actualPath.add((auxL*columns)+auxC);
					}
					else{
						actualPath.add(-1);
					}
					
					if(!actualPath.contains(-1)){
						while(auxC != jC){ // Itera primeiramente nas colunas (largura/horizontal/eixo x);
		 					if(auxC < jC){
		 						auxC++;
		 						actualPath.add((auxL*columns)+auxC);
		 						if(!grid[auxL][auxC].isWorking()){
		 							actualPath.add(-1);
		 							break;
		 						}
							}
		 					
		 					if(auxC > jC){
		 						auxC--;
		 						actualPath.add((auxL*columns)+auxC);
		 						if(!grid[auxL][auxC].isWorking()){
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
								actualPath.add((auxL*columns)+auxC);
								if(!grid[auxL][auxC].isWorking()){
									actualPath.add(-1);
									break;
								}
							}
							
							if(auxL > jL){
								auxL--;
								actualPath.add((auxL*columns)+auxC);
								if(!grid[auxL][auxC].isWorking()){
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
		
		int[][] resultado = new int[paths.size()][];
		for(int i = 0; i<paths.size(); i++){
			int[] vetor_aux = new int[paths.get(i).length+2];
			vetor_aux[0] = headsNTails.get(i)[0];
			vetor_aux[1] = headsNTails.get(i)[1];
			for(int j = 0; j<paths.get(i).length; j++){
				vetor_aux[j+2] = paths.get(i)[j];
			}
			resultado[i] = vetor_aux;
		}		
		return resultado;
	}
	/********************************************************************************************************************/
	public int[][] YXPadrao(CommunicationMatrix cm){	
		int[][] communications = cm.getMatrix();
		int tasks = cm.getTasks();
		int iC,iL, jC, jL, auxC, auxL;
		ArrayList<int[]> headsNTails = new ArrayList<int[]>();
		ArrayList<int[]> paths = new ArrayList<int[]>();
		for(int i = 0; i < tasks; i++){ //nodes é o numero de nós do grafo
			int[] iPos = getPos(i);
			iL = iPos[0]; // Coluna
			iC = iPos[1]; // Linha
			for(int j = 0; j < tasks; j++){ // Busca aplicacoes que estao conectadas a aplicacao i;
				auxC = iC; // Seta novamente o aux pois ele sera
				auxL = iL; // alterado caso exista uma busca de caminho
				if(communications[i][j] != 0){ // Achou uma aplicacao conectada a i
					int[] jPos = getPos(j);
					jL = jPos[0]; 			// Busca a posicao da aplicacao
					jC = jPos[1];			// conectada a i que foi encontrada
					int[] headNTail = {i, j};// Vetor que vai guardar o caminho feito entre i e j;
												// Node inicial (i) 
												// Node final (j)	
												// "Caminho de i para j:"
												// Isso eh usado para saber a qual ligacao o caminho pertence
												// uma vez que os 2 primeiros elementos do vetor sempre serao
												// origem e destino.
					headsNTails.add(headNTail);
					
					ArrayList<Integer> actualPath = new ArrayList<Integer>();
					if(grid[auxL][auxC].isWorking()){
						actualPath.add((auxL*columns)+auxC);
					}
					else{
						actualPath.add((auxL*columns)+auxC);
						actualPath.add(-1);
					}
					
					if(!actualPath.contains(-1)){
						while(auxL != jL){ // Iterando agora nas linhas (altura/vertical/eixo y)							
							if(auxL < jL){
								auxL++;
								actualPath.add((auxL*columns)+auxC);
								if(!grid[auxL][auxC].isWorking()){
									actualPath.add(-1);
									break;
								}
							}
							
							if(auxL > jL){
								auxL--;
								actualPath.add((auxL*columns)+auxC);
								if(!grid[auxL][auxC].isWorking()){
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
		 						actualPath.add((auxL*columns)+auxC);
		 						if(!grid[auxL][auxC].isWorking()){
		 							actualPath.add(-1);
		 							break;
		 						}
							}
		 					
		 					if(auxC > jC){
		 						auxC--;
		 						actualPath.add((auxL*columns)+auxC);
		 						if(!grid[auxL][auxC].isWorking()){
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
		
		int[][] resultado = new int[paths.size()][];
		for(int i = 0; i<paths.size(); i++){
			int[] vetor_aux = new int[paths.get(i).length+2];
			vetor_aux[0] = headsNTails.get(i)[0];
			vetor_aux[1] = headsNTails.get(i)[1];
			for(int j = 0; j<paths.get(i).length; j++){
				vetor_aux[j+2] = paths.get(i)[j];
			}
			resultado[i] = vetor_aux;
		}		
		return resultado;
	}
	/********************************************************************************************************************/	
	public int[][] XYTorus(CommunicationMatrix cm){	
		int[][] communications = cm.getMatrix();
		int tasks = cm.getTasks();
		int iC,iL, jC, jL, auxC, auxL;
		ArrayList<int[]> headsNTails = new ArrayList<int[]>();
		ArrayList<int[]> paths = new ArrayList<int[]>();
		for(int i = 0; i < tasks; i++){ //nodes é o numero de nós do grafo
			int[] iPos = getPos(i);
			iL = iPos[0]; // Coluna
			iC = iPos[1]; // Linha
			for(int j = 0; j < tasks; j++){ // Busca aplicacoes que estao conectadas a aplicacao i;
				auxC = iC; // Seta novamente o aux pois ele sera
				auxL = iL; // alterado caso exista uma busca de caminho
				if(communications[i][j] != 0){ // Achou uma aplicacao conectada a i
					int[] jPos = getPos(j);
					jL = jPos[0]; 			// Busca a posicao da aplicacao
					jC = jPos[1];			// conectada a i que foi encontrada
					int[] headNTail = {i, j};// Vetor que vai guardar o caminho feito entre i e j;
												// Node inicial (i) 
												// Node final (j)	
												// "Caminho de i para j:"
												// Isso eh usado para saber a qual ligacao o caminho pertence
												// uma vez que os 2 primeiros elementos do vetor sempre serao
												// origem e destino.
					headsNTails.add(headNTail);
					
					ArrayList<Integer> actualPath = new ArrayList<Integer>();
					if(grid[auxL][auxC].isWorking()){
						actualPath.add((auxL*columns)+auxC);
					}
					else{
						actualPath.add(-1);
					}
					
					if(!actualPath.contains(-1)){
						int test = jC - iC;
						if(test < 0){test*=-1;}
						if(test <= columns/2){
							while(auxC != jC){ // Itera primeiramente nas colunas (largura/horizontal/eixo x);
			 					if(auxC < jC){
			 						auxC++;
			 						actualPath.add((auxL*columns)+auxC);
			 						if(!grid[auxL][auxC].isWorking()){
			 							actualPath.add(-1);
			 							break;
			 						}
								}
			 					
			 					if(auxC > jC){
			 						auxC--;
			 						actualPath.add((auxL*columns)+auxC);
			 						if(!grid[auxL][auxC].isWorking()){
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
			 						actualPath.add((auxL*columns)+auxC);
			 						if(!grid[auxL][auxC].isWorking()){
			 							actualPath.add(-1);
			 							break;
			 						}
								}
			 					
			 					if(iC > jC){
			 						auxC++;
			 						if(auxC == columns){auxC=0;}
			 						actualPath.add((auxL*columns)+auxC);
			 						if(!grid[auxL][auxC].isWorking()){
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
									actualPath.add((auxL*columns)+auxC);
									if(!grid[auxL][auxC].isWorking()){
										actualPath.add(-1);
										break;
									}
								}
								
								if(auxL > jL){
									auxL--;
									actualPath.add((auxL*columns)+auxC);
									if(!grid[auxL][auxC].isWorking()){
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
									actualPath.add((auxL*columns)+auxC);
									if(!grid[auxL][auxC].isWorking()){
										actualPath.add(-1);
										break;
									}
								}
								
								if(auxL > jL){
									auxL++;
									if(auxL == lines){auxL = 0;}
									actualPath.add((auxL*columns)+auxC);
									if(!grid[auxL][auxC].isWorking()){
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
		
		int[][] resultado = new int[paths.size()][];
		for(int i = 0; i<paths.size(); i++){
			int[] vetor_aux = new int[paths.get(i).length+2];
			vetor_aux[0] = headsNTails.get(i)[0];
			vetor_aux[1] = headsNTails.get(i)[1];
			for(int j = 0; j<paths.get(i).length; j++){
				vetor_aux[j+2] = paths.get(i)[j];
			}
			resultado[i] = vetor_aux;
		}		
		return resultado;
	}
	/********************************************************************************************************************/
	public int[][] YXTorus(CommunicationMatrix cm){	
		int[][] communications = cm.getMatrix();
		int tasks = cm.getTasks();
		int iC,iL, jC, jL, auxC, auxL;
		ArrayList<int[]> headsNTails = new ArrayList<int[]>();
		ArrayList<int[]> paths = new ArrayList<int[]>();
		for(int i = 0; i < tasks; i++){ 		//nodes é o numero de nós do grafo
			int[] iPos = getPos(i);
			iL = iPos[0]; // Coluna
			iC = iPos[1]; // Linha
			for(int j = 0; j < tasks; j++){ 	// Busca aplicacoes que estao conectadas a aplicacao i;
				auxC = iC; 						// Seta novamente o aux pois ele sera
				auxL = iL; 						// alterado caso exista uma busca de caminho
				if(communications[i][j] != 0){ 	// Achou uma aplicacao conectada a i
					int[] jPos = getPos(j);
					jL = jPos[0]; 			// Busca a posicao da aplicacao
					jC = jPos[1];			// conectada a i que foi encontrada
					int[] headNTail = {i, j};	// Vetor que vai guardar o caminho feito entre i e j;
												// Node inicial (i) 
												// Node final (j)	
												// "Caminho de i para j:"
												// Isso eh usado para saber a qual ligacao o caminho pertence
												// uma vez que os 2 primeiros elementos do vetor sempre serao
												// origem e destino.
					headsNTails.add(headNTail);
					
					ArrayList<Integer> actualPath = new ArrayList<Integer>();
					if(grid[auxL][auxC].isWorking()){
						actualPath.add((auxL*columns)+auxC);
					}
					else{
						actualPath.add(-1);
					}
					
					if(!actualPath.contains(-1)){
	 					int test = jL - iL;
						if(test < 0){test*=-1;}
						if(test <= lines/2){
							while(auxL != jL){ // Iterando nas linhas						
								if(auxL < jL){
									auxL++;
									actualPath.add((auxL*columns)+auxC);
									if(!grid[auxL][auxC].isWorking()){
										actualPath.add(-1);
										break;
									}
								}
								
								if(auxL > jL){
									auxL--;
									actualPath.add((auxL*columns)+auxC);
									if(!grid[auxL][auxC].isWorking()){
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
									actualPath.add((auxL*columns)+auxC);
									if(!grid[auxL][auxC].isWorking()){
										actualPath.add(-1);
										break;
									}
								}
								
								if(auxL > jL){
									auxL++;
									if(auxL == lines){auxL = 0;}
									actualPath.add((auxL*columns)+auxC);
									if(!grid[auxL][auxC].isWorking()){
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
			 						actualPath.add((auxL*columns)+auxC);
			 						if(!grid[auxL][auxC].isWorking()){
			 							actualPath.add(-1);
			 							break;
			 						}
								}
			 					
			 					if(auxC > jC){
			 						auxC--;
			 						actualPath.add((auxL*columns)+auxC);
			 						if(!grid[auxL][auxC].isWorking()){
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
			 						actualPath.add((auxL*columns)+auxC);
			 						if(!grid[auxL][auxC].isWorking()){
			 							actualPath.add(-1);
			 							break;
			 						}
								}
			 					
			 					if(iC > jC){
			 						auxC++;
			 						if(auxC == columns){auxC=0;}
			 						actualPath.add((auxL*columns)+auxC);
			 						if(!grid[auxL][auxC].isWorking()){
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
	/********************************************************************************************************************/
	public int[][] XXYYPadrao(CommunicationMatrix cm){	
		int[][] communications = cm.getMatrix();
		int tasks = cm.getTasks();
		int iC,iL, jC, jL, auxC, auxL;
		ArrayList<int[]> headsNTails = new ArrayList<int[]>();
		ArrayList<int[]> paths = new ArrayList<int[]>();
		for(int i = 0; i < tasks; i++){ 		//TASKS é o numero de nós do grafo
			int[] iPos = getPos(i);
			iL = iPos[0]; // Coluna
			iC = iPos[1]; // Linha
			for(int j = 0; j < tasks; j++){ 	// Busca aplicacoes que estao conectadas a aplicacao i;
				auxC = iC;						// Seta novamente o aux pois ele sera
				auxL = iL; 						// alterado caso exista uma busca de caminho
				if(communications[i][j] != 0){	// Achou uma aplicacao conectada a i
					int[] jPos = getPos(j);
					jL = jPos[0]; 			// Busca a posicao da aplicacao
					jC = jPos[1];			// conectada a i que foi encontrada
					int[] headNTail = {i, j};	// Vetor que vai guardar o caminho feito entre i e j;
												// Node inicial (i) 
												// Node final (j)	
												// "Caminho de i para j:"
												// Isso eh usado para saber a qual ligacao o caminho pertence
												// uma vez que os 2 primeiros elementos do vetor sempre serao
												// origem e destino.
					headsNTails.add(headNTail);
					
					ArrayList<Integer> actualPath = new ArrayList<Integer>(); // Array que vai guardar o caminho atual
					
					if(grid[auxL][auxC].isWorking()){ // Testando se o primeiro node está funcionando
						actualPath.add((auxL*columns)+auxC);
					}
					else{
						actualPath.add(-1);
					}
					
					boolean hor = true; 	// TRUE - prioridade andar na horizontal
											// FALSE - prioridade andar na vertical
					
					if(!actualPath.contains(-1)){
						while((auxC != jC || auxL != jL) && !actualPath.contains(-1)){	// Enquanto não chegar no destino
																						// e ainda está apto a andar
							if(hor){ // Prioridade no inicio é andar pela horizontal
			 					if(auxC < jC){ // Andando para direita
			 						if(grid[auxL][auxC+1].isWorking()){ // Se o node a direita está funcionando
				 						auxC++;
				 						if(!actualPath.contains((auxL*columns)+auxC)){ // se o node ainda não tiver sido adicionado
				 							actualPath.add((auxL*columns)+auxC); // adicione
				 						}
				 						else{ // se o node ja tiver sido adicionado (loop)
				 							actualPath.add(-1); // comunicação falhou
				 							break;
				 						}
				 						
			 						}
				 					else{	// Se o node a direita não estiver funcionando
				 						boolean dUpOK = false; 	// flags para saber se os discoverers
				 						boolean dDownOK = false;// ja chegaram a um resultado
				 						int discovererUp = auxL;	// dispara-se os discoverers a partir da linha atual 
				 						int discovererDown = auxL;	// no caso, pra cima e para baixo, significa subir ou descer nas linhas
				 						
				 						while(!dUpOK || !dDownOK){ // enquanto ambos os discoverers não chegarem a um resultado
				 							if(!dUpOK){ // se ainda não chegou a um resultado
				 								if(discovererUp > 0 && grid[discovererUp-1][auxC].isWorking()){ // se ainda pode andar pra cima e
				 																								// o node acima esta funcionando
				 									discovererUp--;	// sobe
					 								if(grid[discovererUp][auxC+1].isWorking()){ // se é um caminho possível
					 									dUpOK = true; // sinaliza que chegou a um resultado
					 								}
				 								}
				 								else{					// se o node acima não está funcionando ou não existe
				 									discovererUp = -1;	// sinaliza resultado negativo (não há caminhos possíveis por cima)
				 									dUpOK = true;		// sinaliza que chegou a um resultado
				 								}
				 							}
				 											 							
				 							if(!dDownOK){ // se ainda não chegou a um resultado
				 								if(discovererDown < lines-1 && grid[discovererDown+1][auxC].isWorking()){ 	// se ainda pode andar pra baixo e
				 																											// o node abaixo esta funcionando
				 									discovererDown++; // desce
					 								if(grid[discovererDown][auxC+1].isWorking()){ // se é um caminho possível
					 									dDownOK = true; // sinaliza que chegou a um resultado
					 								}
				 								}
				 								else{ 						// caso não possa mais descer
					 								discovererDown = -1;	// sinaliza resultado negativo (não há caminhos possíveis por baixo)
					 								dDownOK = true;			// sinaliza que chegou a um resultado
					 							}
				 							}
				 						}
				 						
				 						if(discovererUp == -1 && discovererDown == -1){ // se houve resultado negativo por cima e por baixo
				 							actualPath.add(-1);	// sinaliza que não há caminhos possíveis
				 							break;
				 						}
				 						else if(discovererUp != -1 && discovererDown != -1){ // se ambos acharam resultados positivos
				 							
				 							int chosen = Math.abs(discovererUp - jL) <= Math.abs(discovererDown - jL) ? discovererUp : discovererDown;
				 							// escolhe o resultado que esteja mais perto da linha destino
				 							while(auxL != chosen){ // adiciona todos os nodes do caminho escolhido
				 								if(auxL > chosen){
				 									auxL--;
				 								}
				 								else{auxL++;}
				 								if(!actualPath.contains((auxL*columns)+auxC)){ // testando se não está em um loop
				 									actualPath.add((auxL*columns)+auxC);
				 								}
				 								else{
				 									actualPath.add(-1);
				 									break;
				 								}
				 							}
				 						}
				 						else{ // se apenas um dos discoverers retornaram resultado positivo
				 							
				 							if(discovererUp != -1){ // resultado positivo acima
				 								while(auxL != discovererUp){ // adiciona todos os nodes do caminho escolhido
				 									auxL--;
				 									if(!actualPath.contains((auxL*columns)+auxC)){ // testando se não está em um loop
					 									actualPath.add((auxL*columns)+auxC);
					 								}
					 								else{
					 									actualPath.add(-1);
					 									break;
					 								}
					 							}
				 							}
				 							else{	// resultado positivo abaixo
				 								
				 								while(auxL != discovererDown){ // adiciona todos os nodes do caminho escolhido
				 									auxL++;
				 									if(!actualPath.contains((auxL*columns)+auxC)){ // testando se não está em um loop
					 									actualPath.add((auxL*columns)+auxC);
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
			 					
			 					else if(auxC > jC){ // andando para esquerda
			 						if(grid[auxL][auxC-1].isWorking()){ // se o node a esquerda estiver funcionando
				 						auxC--; // anda pra esquerda
				 						if(!actualPath.contains((auxL*columns)+auxC)){ // detecção de loops
		 									actualPath.add((auxL*columns)+auxC);
		 								}
		 								else{
		 									actualPath.add(-1);
		 									break;
		 								}
			 						}
				 					else{ // se o node a esqueda nao estiver funcionando
				 						boolean dUpOK = false; 	// flags para saber se os discoverers
				 						boolean dDownOK = false;// ja chegaram a um resultado
				 						int discovererUp = auxL;	// dispara-se os discoverers a partir da linha atual 
				 						int discovererDown = auxL;	// no caso, pra cima e para baixo, significa subir ou descer nas linhas
				 						
				 						while(!dUpOK || !dDownOK){ // enquanto não houver resultados de ambos
				 							if(!dUpOK){ // se ainda nao houver resultado do discovererUp
				 								if(discovererUp > 0 && grid[discovererUp-1][auxC].isWorking()){ // se ainda puder ir pra cima e
				 																								// o node a cima esta funcionando
				 									discovererUp--; // sobe
					 								if(grid[discovererUp][auxC-1].isWorking()){ // se houver um caminho possivel
					 									dUpOK = true; // sinaliza que chegou a um resultado
					 								}
				 								}
				 								else{ 					// se o node a cima nao estiver funcionando ou não existir
				 									discovererUp = -1; 	// sinaliza um resultado negativo
				 									dUpOK = true; 		// sinaliza que chegou a um resultado
				 								}
				 							}
				 							
				 							if(!dDownOK){ // se ainda nao achou um resultado
				 								if(discovererDown < lines-1 && grid[discovererDown+1][auxC].isWorking()){ 	// se ainda puder descer e
				 																											// o node abaixo está funcionando
				 									discovererDown++; // desce
					 								if(grid[discovererDown][auxC-1].isWorking()){ // se achou um caminho possivel
					 									dDownOK = true; // sinaliza que chegou a um resultado
					 								}
				 								}
				 								else{ // se o node abaixo nao estiver funcionando
				 									discovererDown = -1; // sinaliza resultado negativo
				 									dDownOK = true; // sinaliza que chegou a um resultado
				 								}
				 							}
				 						}
				 						
				 						if(discovererUp == -1 && discovererDown == -1){ // se ambos os resultados foram negativos
				 							actualPath.add(-1);
				 							break;
				 						}
				 						else if(discovererUp != -1 && discovererDown != -1){ // se ambos forem positivos
				 							int chosen = Math.abs(discovererUp - jL) <= Math.abs(discovererDown - jL) ? 
				 											discovererUp : discovererDown;
				 							// escolhe o mais próximo da linha destino
				 							while(auxL != chosen){ // adicionando os nodes do caminho escolhido
				 								if(auxL > chosen){
				 									auxL--;
				 								}
				 								else{auxL++;}
				 								if(!actualPath.contains((auxL*columns)+auxC)){ // verificação de loops
				 									actualPath.add((auxL*columns)+auxC);
				 								}
				 								else{
				 									actualPath.add(-1);
				 									break;
				 								}
				 							}
				 						}
				 						else{ // se houve apenas um resultado positivo
				 							if(discovererUp != -1){ // se foi pra cima
				 								while(auxL != discovererUp){ // adiciona os nodes do caminho encontrado
				 									auxL--;
				 									if(!actualPath.contains((auxL*columns)+auxC)){ // verificação de loops
					 									actualPath.add((auxL*columns)+auxC);
					 								}
					 								else{
					 									actualPath.add(-1);
					 									break;
					 								}
					 							}
				 							}
				 							else{ // se foi pra baixo
				 								while(auxL != discovererDown){ // adiciona os nodes do caminho encontrado
				 									auxL++;
				 									if(!actualPath.contains((auxL*columns)+auxC)){ // verificação de loops
					 									actualPath.add((auxL*columns)+auxC);
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
			 					if(auxC == jC){hor = !hor;} // se já alinhou com a coluna do destino, a prioridade agora é andar na vertical
							}
		 					
							if(!hor){ // se a prioridade é andar na vertical
			 					if(auxL < jL){ // andando para baixo
			 						if(grid[auxL+1][auxC].isWorking()){ // se o node abaixo esta funcionando
				 						auxL++; // desce
				 						if(!actualPath.contains((auxL*columns)+auxC)){ // verificando loops e adicionando o node ao caminho atual
		 									actualPath.add((auxL*columns)+auxC);
		 								}
		 								else{
		 									actualPath.add(-1);
		 									break;
		 								}
			 						}
				 					else{ // se o node nao estiver funcionando
				 						boolean dLeftOK = false; 	// flags para sinalizar se
				 						boolean dRightOK = false;	// ja chegou a um resultado
				 						int discovererLeft = auxC; 	// discoverers que irao procurar caminhos possiveis
				 						int discovererRight = auxC; // para a esquerda e para a direita
				 						
				 						while(!(dLeftOK && dRightOK)){ // enquanto ambos nao obtiverem um resultado
				 							if(!dLeftOK){ // se nao tiver encontrado um resultado ainda
				 								if(discovererLeft > 0 && grid[auxL][discovererLeft-1].isWorking()){ // se ainda puder ir pra esquerda e
				 																									// o node da esquerda estiver funcionando
				 									discovererLeft--; // vai pra esquerda
					 								if(grid[auxL+1][discovererLeft].isWorking()){
					 									dLeftOK = true; // sinaliza que encontrou um resultado
					 								}
				 								}
					 							else{ // se nao puder mais ir pra esquerda ou o node nao estiver funcionando
					 								discovererLeft = -1; // sinaliza resultado negativo
					 								dLeftOK = true; // sinaliza que chegou a um resultado
					 							}
				 							}
				 							
				 							if(!dRightOK){ // se ainda nao achou um resultado
				 								if(discovererRight < columns-1 && grid[auxL][discovererRight+1].isWorking()){// se ainda puder ir para a direita 
				 																										// e o node da direita estiver funcionando
				 									discovererRight++; // vai pra direita
					 								if(grid[auxL+1][discovererRight].isWorking()){ // se encontrou um caminho possível
					 									dRightOK = true; // sinaliza que tem um resultado
					 								}
				 								}
				 								else{ // se o node a direita nao estiver funcionando ou nao existir
				 									discovererRight = -1; // sinaliza resultado negativo
				 									dRightOK = true; // sinaliza que chegou a um resultado
				 								}
				 							}
				 						}
				 						
				 						if(discovererLeft == -1 && discovererRight == -1){ // se obteve resultado negativo em ambos os discoverers
				 							actualPath.add(-1); 
				 							break;
				 						}
				 						else if(discovererLeft != -1 && discovererRight != -1){ // se obteve resultado positivo em ambosd]
				 							int chosen = Math.abs(discovererLeft - jC) <= Math.abs(discovererRight - jC) ? 
				 											discovererLeft : discovererRight;
				 							// escolhe o que esta mais proximo a coluna destino
				 							while(auxC != chosen){ // adiciona os nodes do caminho encontrado
				 								if(auxC > chosen){
				 									auxC--;
				 								}
				 								else{auxC++;}
				 								if(!actualPath.contains((auxL*columns)+auxC)){ // verificando loops
				 									actualPath.add((auxL*columns)+auxC);
				 								}
				 								else{
				 									actualPath.add(-1);
				 								}
				 							}
				 						}
				 						else{ // se apenas um obteve resultado positivo
				 							if(discovererLeft != -1){ // se foi o da esquerda
				 								while(auxC != discovererLeft){ // adciona nodes do caminho escontrado
				 									auxC--;
				 									if(!actualPath.contains((auxL*columns)+auxC)){ // verificando loops
					 									actualPath.add((auxL*columns)+auxC);
					 								}
					 								else{
					 									actualPath.add(-1);
					 									break;
					 								}
					 							}
				 							}
				 							else{ // se foi o da direita
				 								while(auxC != discovererRight){ // adciona nodes do caminho escontrado
				 									auxC++;
				 									if(!actualPath.contains((auxL*columns)+auxC)){ // verificando loops
					 									actualPath.add((auxL*columns)+auxC);
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
			 					
			 					else if(auxL > jL){ // andando para a cima
			 						if(grid[auxL-1][auxC].isWorking()){ // se o node acima esta funcionando
				 						auxL--; // sobe
				 						if(!actualPath.contains((auxL*columns)+auxC)){ // verificando loops
		 									actualPath.add((auxL*columns)+auxC);
		 								}
		 								else{
		 									actualPath.add(-1);
		 									break;
		 								}
			 						}
				 					else if(!grid[auxL-1][auxC].isWorking()){ // se o node acima nao esta funcionando
				 						boolean dLeftOK = false; 	// flags para sinalizar se
				 						boolean dRightOK = false;	// ja chegou a um resultado
				 						int discovererLeft = auxC; 	// discoverers que irao procurar caminhos possiveis
				 						int discovererRight = auxC; // para a esquerda e para a direita
				 						
				 						while(!(dLeftOK && dRightOK)){ // enquando ambos nao chegarem a um resultado
				 							if(!dLeftOK){ // se ainda nao achou um resultado
				 								if(discovererLeft > 0 && grid[auxL][discovererLeft-1].isWorking()){ // se o node da esquerda existe
				 																									// e esta funcionando
				 									discovererLeft--; // vai pra esquerda
					 								if(grid[auxL-1][discovererLeft].isWorking()){ // se achou um caminho possivel
					 									dLeftOK = true; // sinaliza que chegou a um resultado
					 								}
				 								}
				 								else{ // se o node a esquerda nao esta funcionando ou nao existe
				 									discovererLeft = -1; // sinaliza resultado negativo
				 									dLeftOK = true; // sinaliza que chegou a um resultado
				 								}
				 							}
				 							
				 							if(!dRightOK){ // se ainda nao obteve um resultado
				 								if(discovererRight < columns-1 && grid[auxL][discovererRight+1].isWorking()){ 	// se o node a direita existe
				 																												// e esta funcionando
				 									discovererRight++; // vai para a direita
					 								if(grid[auxL-1][discovererRight].isWorking()){ // se encontrou um caminho possivel
					 									dRightOK = true;
					 								}
				 								}
				 								else{ // se o node a direita nao esta funcionando
				 									discovererRight = -1; // sinaliza resultado negativo
				 									dRightOK = true; // sinaliza que chegou a um resultado
				 								}
				 							}
				 						}
				 						
				 						if(discovererLeft == -1 && discovererRight == -1){ // se ambos chegaram a um resultado negativo
				 							actualPath.add(-1);
				 							break;
				 						}
				 						else if(discovererLeft != -1 && discovererRight != -1){ // se ambos chegaram a um resultado positivo
				 							int chosen = Math.abs(discovererLeft - jC) <= Math.abs(discovererRight - jC) ? 
				 											discovererLeft : discovererRight;
				 							// seleciona o resultado mais proximo da coluna do destino
				 							while(auxC != chosen){ // adicionando nodes ao caminho
				 								if(auxC > chosen){
				 									auxC--;
				 								}
				 								else{auxC++;}
				 								if(!actualPath.contains((auxL*columns)+auxC)){ // verificando loops
				 									actualPath.add((auxL*columns)+auxC);
				 								}
				 								else{
				 									actualPath.add(-1);
				 									break;
				 								}
				 							}
				 						}
				 						else{ // se apenas um obteve resultado positvo
				 							if(discovererLeft != -1){ //se foi a esquerda
				 								while(auxC != discovererLeft){ // adiciona nodes ao caminho
				 									auxC--;
				 									if(!actualPath.contains((auxL*columns)+auxC)){ // verificando loops
					 									actualPath.add((auxL*columns)+auxC);
					 								}
					 								else{
					 									actualPath.add(-1);
					 									break;
					 								}
					 							}
				 							}
				 							else{ // se foi a direita
				 								while(auxC != discovererRight){ // adicionando nodes ao caminho
				 									auxC++;
				 									if(!actualPath.contains((auxL*columns)+auxC)){ // verificando loops
					 									actualPath.add((auxL*columns)+auxC);
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
			 					if(auxL == jL){hor = !hor;} // se ja alinho a linha, a prioridade agora é andar na horizontal
							}
						}
					}
					
					int[] ap = new int[actualPath.size()]; // vetor que guardara o caminho encontrado
					int indice = 0; // variavel auxiliar
					for (Integer n : actualPath) { // populando o vetor resultado fazendo uma copia do arraylist
						ap[indice++] = n;
					}
					paths.add(ap); // adiciona o caminho no arraylist que guarda os resultados
				}
			}
		}
		
		int[][] resultado = new int[paths.size()][];
		for(int i = 0; i<paths.size(); i++){
			int[] vetor_aux = new int[paths.get(i).length+2];
			vetor_aux[0] = headsNTails.get(i)[0];
			vetor_aux[1] = headsNTails.get(i)[1];
			for(int j = 0; j<paths.get(i).length; j++){
				vetor_aux[j+2] = paths.get(i)[j];
			}
			resultado[i] = vetor_aux;
		}		
		return resultado;
	}
	/********************************************************************************************************************/
	public int[][] Dijkstra(CommunicationMatrix cm){	
		int[][] communications = cm.getMatrix();
		int tasks = cm.getTasks();
		int iC,iL, jC, jL;
		ArrayList<int[]> headsNTails = new ArrayList<int[]>();
		ArrayList<int[]> paths = new ArrayList<int[]>();
		
		List<Vertex> nodes = new ArrayList<Vertex>();
        List<Edge> edges = new ArrayList<Edge>();
        
        for (int i = 0; i < grid.length*grid[0].length; i++) {
                Vertex location = new Vertex("Node_" + i, "" + i);
                nodes.add(location);
        }
        
        int edge = 0;
        for(int i = 0; i < lines-1; i++){
        	for(int j = 0; j < columns-1; j++){
        		if(grid[i][j].isWorking()){
        			if(grid[i][j+1].isWorking()){
        				//System.out.println("addLane(Edge_" + edge + ", " + nodes.get((i*columns)+j) + ", " + nodes.get((i*columns)+j+1) + ", 1);");
        				Edge lane1 = new Edge("Edge_"+(edge++), nodes.get((i*columns)+j), nodes.get((i*columns)+j+1), 1);
        				//System.out.println("addLane(Edge_" + edge + ", " + nodes.get((i*columns)+j+1) + ", " + nodes.get((i*columns)+j) + ", 1);");
        				Edge lane2 = new Edge("Edge_"+(edge++), nodes.get((i*columns)+j+1), nodes.get((i*columns)+j), 1);
        				edges.add(lane1);
        				edges.add(lane2);
        			}
        			if(grid[i+1][j].isWorking()){
        				//System.out.println("addLane(Edge_" + edge + ", " + nodes.get((i*columns)+j) + ", " + nodes.get(((i+1)*columns)+j) + ", 1);");
        				Edge lane1 = new Edge("Edge_"+(edge++), nodes.get((i*columns)+j), nodes.get(((i+1)*columns)+j), 1);
        				//System.out.println("addLane(Edge_" + edge + ", " + nodes.get(((i+1)*columns)+j) + ", " + nodes.get((i*columns)+j) + ", 1);");
        				Edge lane2 = new Edge("Edge_"+(edge++), nodes.get(((i+1)*columns)+j), nodes.get((i*columns)+j),  1);
        				edges.add(lane1);
        				edges.add(lane2);
        			}
        		}
        	}
        }
        
        int jAux = grid[0].length-1;
        for(int i = 0; i < lines-1; i++){
        	if(grid[i][jAux].isWorking()){
        		if(grid[i+1][jAux].isWorking()){
        			//System.out.println("addLane(Edge_" + edge + ", " + nodes.get((i*columns)+jAux) + ", " + nodes.get(((i+1)*columns)+jAux) + ", 1);");
    				Edge lane1 = new Edge("Edge_"+(edge++), nodes.get((i*columns)+jAux), nodes.get(((i+1)*columns)+jAux), 1);
    				//System.out.println("addLane(Edge_" + edge + ", " + nodes.get(((i+1)*columns)+jAux) + ", " + nodes.get((i*columns)+jAux) + ", 1);");
    				Edge lane2 = new Edge("Edge_"+(edge++), nodes.get(((i+1)*columns)+jAux), nodes.get((i*columns)+jAux), 1);
    				edges.add(lane1);
    				edges.add(lane2);
    			}
        	}
        }
        
        int iAux = grid.length-1;
        for(int j = 0; j < columns-1; j++){
        	if(grid[iAux][j].isWorking()){
        		if(grid[iAux][j+1].isWorking()){
        			//System.out.println("addLane(Edge_" + edge + ", " + nodes.get((iAux*columns)+j) + ", " + nodes.get((iAux*columns)+j+1) + ", 1);");
        			Edge lane1 = new Edge("Edge_"+(edge++), nodes.get((iAux*columns)+j), nodes.get((iAux*columns)+j+1), 1);
        			//System.out.println("addLane(Edge_" + edge + ", " + nodes.get((iAux*columns)+j+1) + ", " + nodes.get((iAux*columns)+j) + ", 1);");
        			Edge lane2 = new Edge("Edge_"+(edge++), nodes.get((iAux*columns)+j+1), nodes.get((iAux*columns)+j), 1);
    				edges.add(lane1);
    				edges.add(lane2);
        		}
        	}
        }  
        
        Graph graph = new Graph(nodes, edges);
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
		
		for(int i = 0; i < tasks; i++){ 		//nodes é o numero de nós do grafo
			int[] iPos = getPos(i);
			iL = iPos[0]; // Coluna
			iC = iPos[1]; // Linha
			for(int j = 0; j < tasks; j++){ 	// Busca aplicacoes que estao conectadas a aplicacao i;
				if(communications[i][j] != 0){ 	// Achou uma aplicacao conectada a i
					int[] jPos = getPos(j);
					jL = jPos[0]; 			// Busca a posicao da aplicacao
					jC = jPos[1];			// conectada a i que foi encontrada
					int[] headNTail = {i, j};	// Vetor que vai guardar o caminho feito entre i e j;
												// Node inicial (i) 
												// Node final (j)	
												// "Caminho de i para j:"
												// Isso eh usado para saber a qual ligacao o caminho pertence
												// uma vez que os 2 primeiros elementos do vetor sempre serao
												// origem e destino.
					headsNTails.add(headNTail);
					int[] actualPath;
					if((iL*columns)+iC == (jL*columns)+jC){
						actualPath = new int[]{(jL*columns)+jC};
					}
					else{
						dijkstra.execute(nodes.get((iL*columns)+iC));
						LinkedList<Vertex> path = dijkstra.getPath(nodes.get((jL*columns)+jC));
						if(path != null){
							actualPath = new int[path.size()];
			                int indice = 0;
			                for (Vertex vertex : path) {
			                	actualPath[indice++] = Integer.parseInt(vertex.getName());
			                }
						}
						else{
							actualPath = new int[]{-1};
						}
					}
					paths.add(actualPath);
				}
			}
		}

		int[][] resultado = new int[paths.size()][];
		for(int i = 0; i<paths.size(); i++){
			int[] vetor_aux = new int[paths.get(i).length+2];
			vetor_aux[0] = headsNTails.get(i)[0];
			vetor_aux[1] = headsNTails.get(i)[1];
			for(int j = 0; j<paths.get(i).length; j++){
				vetor_aux[j+2] = paths.get(i)[j];
			}
			resultado[i] = vetor_aux;
		}		
		return resultado;
	}
}
