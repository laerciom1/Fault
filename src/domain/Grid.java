package domain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import java.awt.Color;
import java.io.IOException;

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
		deallocateApps();
		int remaining = apps; // N aplicacoes	
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
	
	public void resetFaults(){
		for(int i = 0; i < lines; i++){
			for(int j = 0; j < columns; j++){
				grid[i][j].setWorking(true);;
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
		resetFaults();
		int faults_aux = (int) ((lines*columns*percentage)/100);
		faults = faults_aux;
		boolean injected;
		int randomRouter;
		while(faults_aux > 0){
			injected = false;
			while(!injected){									// Enquando nao injetar a falha
				randomRouter = random.nextInt(columns*lines);	// pega um router aleatorio
				if(grid[(randomRouter/columns)][(randomRouter%columns)].isWorking() &&
						grid[(randomRouter/columns)][(randomRouter%columns)].getApps().size() == 0 ){
													// se o router estiver funcionando e n�o tiver tarefas alocadas
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
		
		for(int i = 1; i < tasks; i++){ 		//tasks � o numero de tarefas
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
		for(int i = 0; i < tasks; i++){ //nodes � o numero de n�s do grafo
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
		for(int i = 0; i < tasks; i++){ //nodes � o numero de n�s do grafo
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
		for(int i = 0; i < tasks; i++){ //nodes � o numero de n�s do grafo
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
		for(int i = 0; i < tasks; i++){ 		//nodes � o numero de n�s do grafo
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
		for(int i = 0; i < tasks; i++){ 		//TASKS � o numero de n�s do grafo
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
					
					if(grid[auxL][auxC].isWorking()){ // Testando se o primeiro node est� funcionando
						actualPath.add((auxL*columns)+auxC);
					}
					else{
						actualPath.add(-1);
					}
					
					boolean hor = true; 	// TRUE - prioridade andar na horizontal
											// FALSE - prioridade andar na vertical
					
					if(!actualPath.contains(-1)){
						while((auxC != jC || auxL != jL) && !actualPath.contains(-1)){	// Enquanto n�o chegar no destino
																						// e ainda est� apto a andar
							if(hor){ // Prioridade no inicio � andar pela horizontal
			 					if(auxC < jC){ // Andando para direita
			 						if(grid[auxL][auxC+1].isWorking()){ // Se o node a direita est� funcionando
				 						auxC++;
				 						if(!actualPath.contains((auxL*columns)+auxC)){ // se o node ainda n�o tiver sido adicionado
				 							actualPath.add((auxL*columns)+auxC); // adicione
				 						}
				 						else{ // se o node ja tiver sido adicionado (loop)
				 							actualPath.add(-1); // comunica��o falhou
				 							break;
				 						}
				 						
			 						}
				 					else{	// Se o node a direita n�o estiver funcionando
				 						boolean dUpOK = false; 	// flags para saber se os discoverers
				 						boolean dDownOK = false;// ja chegaram a um resultado
				 						int discovererUp = auxL;	// dispara-se os discoverers a partir da linha atual 
				 						int discovererDown = auxL;	// no caso, pra cima e para baixo, significa subir ou descer nas linhas
				 						
				 						while(!dUpOK || !dDownOK){ // enquanto ambos os discoverers n�o chegarem a um resultado
				 							if(!dUpOK){ // se ainda n�o chegou a um resultado
				 								if(discovererUp > 0 && grid[discovererUp-1][auxC].isWorking()){ // se ainda pode andar pra cima e
				 																								// o node acima esta funcionando
				 									discovererUp--;	// sobe
					 								if(grid[discovererUp][auxC+1].isWorking()){ // se � um caminho poss�vel
					 									dUpOK = true; // sinaliza que chegou a um resultado
					 								}
				 								}
				 								else{					// se o node acima n�o est� funcionando ou n�o existe
				 									discovererUp = -1;	// sinaliza resultado negativo (n�o h� caminhos poss�veis por cima)
				 									dUpOK = true;		// sinaliza que chegou a um resultado
				 								}
				 							}
				 											 							
				 							if(!dDownOK){ // se ainda n�o chegou a um resultado
				 								if(discovererDown < lines-1 && grid[discovererDown+1][auxC].isWorking()){ 	// se ainda pode andar pra baixo e
				 																											// o node abaixo esta funcionando
				 									discovererDown++; // desce
					 								if(grid[discovererDown][auxC+1].isWorking()){ // se � um caminho poss�vel
					 									dDownOK = true; // sinaliza que chegou a um resultado
					 								}
				 								}
				 								else{ 						// caso n�o possa mais descer
					 								discovererDown = -1;	// sinaliza resultado negativo (n�o h� caminhos poss�veis por baixo)
					 								dDownOK = true;			// sinaliza que chegou a um resultado
					 							}
				 							}
				 						}
				 						
				 						if(discovererUp == -1 && discovererDown == -1){ // se houve resultado negativo por cima e por baixo
				 							actualPath.add(-1);	// sinaliza que n�o h� caminhos poss�veis
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
				 								if(!actualPath.contains((auxL*columns)+auxC)){ // testando se n�o est� em um loop
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
				 									if(!actualPath.contains((auxL*columns)+auxC)){ // testando se n�o est� em um loop
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
				 									if(!actualPath.contains((auxL*columns)+auxC)){ // testando se n�o est� em um loop
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
				 						if(!actualPath.contains((auxL*columns)+auxC)){ // detec��o de loops
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
				 						
				 						while(!dUpOK || !dDownOK){ // enquanto n�o houver resultados de ambos
				 							if(!dUpOK){ // se ainda nao houver resultado do discovererUp
				 								if(discovererUp > 0 && grid[discovererUp-1][auxC].isWorking()){ // se ainda puder ir pra cima e
				 																								// o node a cima esta funcionando
				 									discovererUp--; // sobe
					 								if(grid[discovererUp][auxC-1].isWorking()){ // se houver um caminho possivel
					 									dUpOK = true; // sinaliza que chegou a um resultado
					 								}
				 								}
				 								else{ 					// se o node a cima nao estiver funcionando ou n�o existir
				 									discovererUp = -1; 	// sinaliza um resultado negativo
				 									dUpOK = true; 		// sinaliza que chegou a um resultado
				 								}
				 							}
				 							
				 							if(!dDownOK){ // se ainda nao achou um resultado
				 								if(discovererDown < lines-1 && grid[discovererDown+1][auxC].isWorking()){ 	// se ainda puder descer e
				 																											// o node abaixo est� funcionando
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
				 							// escolhe o mais pr�ximo da linha destino
				 							while(auxL != chosen){ // adicionando os nodes do caminho escolhido
				 								if(auxL > chosen){
				 									auxL--;
				 								}
				 								else{auxL++;}
				 								if(!actualPath.contains((auxL*columns)+auxC)){ // verifica��o de loops
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
				 									if(!actualPath.contains((auxL*columns)+auxC)){ // verifica��o de loops
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
				 									if(!actualPath.contains((auxL*columns)+auxC)){ // verifica��o de loops
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
			 					if(auxC == jC){hor = !hor;} // se j� alinhou com a coluna do destino, a prioridade agora � andar na vertical
							}
		 					
							if(!hor){ // se a prioridade � andar na vertical
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
					 								if(grid[auxL+1][discovererRight].isWorking()){ // se encontrou um caminho poss�vel
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
			 					if(auxL == jL){hor = !hor;} // se ja alinho a linha, a prioridade agora � andar na horizontal
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
		
		for(int i = 0; i < tasks; i++){ 		//nodes � o numero de n�s do grafo
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
	
	/**
	 * @throws IOException ******************************************************************************************************************/
	public int[][] ParametrizavelPadrao(CommunicationMatrix cm, int lookahead) throws IOException{	
		int[][] communications = cm.getMatrix();
		int tasks = cm.getTasks();
		int sourceC,sourceL, destinationC, destinationL, auxC, auxL;
		ArrayList<int[]> headsNTails = new ArrayList<int[]>();
		ArrayList<int[]> paths = new ArrayList<int[]>();
		for(int source = 0; source < tasks; source++){ 		//nodes � o numero de n�s do grafo
			int[] sourcePos = getPos(source);
			sourceL = sourcePos[0]; // Coluna
			sourceC = sourcePos[1]; // Linha
			for(int destination = 0; destination < tasks; destination++){ 	// Busca aplicacoes que estao conectadas a aplicacao i;
				auxC = sourceC; 						// Seta novamente o aux pois ele sera
				auxL = sourceL; 						// alterado caso exista uma busca de caminho
				if(communications[source][destination] != 0){ 	// Achou uma aplicacao conectada a i
					int[] destinationPos = getPos(destination);
					destinationL = destinationPos[0]; 			// Busca a posicao da aplicacao
					destinationC = destinationPos[1];			// conectada a i que foi encontrada
					int[] headNTail = {source, destination};	// Vetor que vai guardar o caminho feito entre i e j;
//					System.out.println("Source: " + source + " | Destination: " + destination);
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
					
					int north[] = new int[2]; 	// posicao mais proxima do destino que pode-se chegar indo por cima
					int south[] = new int[2]; 	// posicao mais proxima do destino que pode-se chegar indo por baixo
					int west[] = new int[2];	// posicao mais proxima do destino que pode-se chegar indo pela esquerda
					int east[] = new int[2];	// posicao mais proxima do destino que pode-se chegar indo pela direita
					
					if(!actualPath.contains(-1)){ // se o router source estiver funcionando...
	 					while(!actualPath.contains((destinationL*columns)+destinationC) && !actualPath.contains(-1)){ 	// Enquanto n�o se atingir o destino ou
	 																													// n�o for verificado que n�o ser� poss�vel
	 																													// alcan��-lo...
	 						// Para testes e debug
//	 						System.out.println("Current: " + auxL + ", " + auxC + "(" + ((auxL*columns)+auxC) + ")");
//	 						System.out.println("Destination: " + destinationL + ", " + destinationC + "(" + ((destinationL*columns)+destinationC) + ")");
	 						
	 						/*
	 						 * BUSCA PELO NORTE
	 						 */
	 						if(auxL-1 < 0){		// se est� no limite do grid
	 											// seta a decis�o para cima como -1, -1
	 											// o que faz o norte ser descartado
	 											// na hora de tomar a decis�o do next step
	 							north[0] = -1;
	 							north[1] = -1;
	 						}
	 						else{				// se n�o est� no limite do grid
	 							int up = 0;
	 							boolean limite = false;
	 							boolean looka = false;
	 							boolean falha = false;
	 							while(!limite && !looka && !falha){			// loop para saber at� onde � possivel seguir para cima
	 																		// onde a variavel "up" ao final ser� o numero de saltos
	 																		// que posso fazer diretamente para cima (icrementando apenas
	 																		// a linha)
	 								if(!grid[auxL-up][auxC].isWorking()){	// caso encontra um router com falha
	 									up--;								// o ultimo router avaliado eh o limite, entao a variavel eh
	 									falha = true;						// � decrementada
	 								}
	 								else if(up == lookahead){				// caso chegue ao limite do lookahead
	 									looka = true;
	 								}
	 								else if(auxL-up == 0){					// caso chegue ao limite da grid
	 									limite = true;
	 								}
	 								else{									// se n�o obteve nenhuma das condicoes anteriores como verdadeira
	 									up++;								// continua incrementando a vari�vel
	 								}
	 							}
								if(up > 0){						// caso seja possivel dar mais que 0 saltos diretamente pra cima
	 								if(auxC != destinationC){	// caso a coluna destino seja diferente da coluna atual
	 									int pontos[][] = new int[up][2];		// matriz que guardar� os pontos mais pr�ximos do destino
	 																			// que pode ser alcan�ado em cada linha acima do ponto atual
			 							for(int i = 0; i < pontos.length; i++){	// cada linha da matriz recebe o X e o Y do ponto mais pr�ximo encontrado
			 								int hops = lookahead-(i+1);			// numero de saltos que podem ser dados a partir da linha central 
			 								pontos[i][0] = auxL-(i+1);			// fixa a linha que est� sendo avaliada
			 								if(hops == 0){						// se o numero de saltos restantes � zero
			 									pontos[i][1] = auxC;			// a coluna resultado � a coluna atual
			 								}
			 								else{								// caso hops seja maior que zero
			 									// distancia do ponto central da linha para o destino
			 									double distance = Math.sqrt(Math.pow(auxC-destinationC, 2)+Math.pow((auxL-(i+1))-destinationL,2));
			 									pontos[i][1] = auxC; // coluna (eixo X) do ponto central da linha
			 									// a partir dele ser� investigados os pontos a direita e a esquerda (dependendo de onde est� o destino)
			 									// e caso a distancia do ponto investigado seja menor do que essa distancia, o valor da coluna (eixo X)
			 									// � marcado como o mais pr�ximo do destino
			 									for(int j = 1; j <= hops; j++){ // avaliando o ponto encontrado a cada hop, come�ando com 1 hop
				 									if(auxC > destinationC){ // se o destino est� a esquerda do ponto atual
				 										if(grid[auxL-1-i][auxC-j].isWorking()){ // se o router est� funcionando
				 											if(Math.sqrt(Math.pow((auxC-j)-destinationC, 2)+Math.pow((auxL-1-i)-destinationL,2)) < distance){
				 												distance = Math.sqrt(Math.pow((auxC-j)-destinationC, 2)+Math.pow((auxL-1-i)-destinationL,2));
				 												pontos[i][1] = auxC-j;
				 												// compara a distancia at� o destino que se mostrou a menor at� agora com a distancia do ponto
				 												// investigado at� o destino, e caso a nova distancia seja menor, atualiza as vari�veis
				 											}
				 										}
				 										else{ // se o router n�o est� funcionando, sai do la�o
				 											break;
				 										}
				 										
				 										if(j == hops || auxC-j == 0){ // se chegou ao numero m�ximo de hops ou encontrou o fim da grid, sai do la�o
				 											break;
				 										}
				 									}
				 									else{ // se o destino est� a direita do ponto atual
				 										if(grid[auxL-1-i][auxC+j].isWorking()){ // se o router est� funcionando
				 											if(Math.sqrt(Math.pow((auxC+j)-destinationC, 2)+Math.pow((auxL-1-i)-destinationL,2)) < distance){
				 												distance = Math.sqrt(Math.pow((auxC+j)-destinationC, 2)+Math.pow((auxL-1-i)-destinationL,2));
				 												pontos[i][1] = auxC+j;
				 												// compara a distancia at� o destino que se mostrou a menor at� agora com a distancia do ponto
				 												// investigado at� o destino, e caso a nova distancia seja menor, atualiza as vari�veis
				 											}
				 										}
				 										else{ // se o router n�o est� funcionando, sai do la�o
				 											break;
				 										}
				 										
				 										if(j == hops || auxC+j == columns-1){ // se chegou ao numero m�ximo de hops ou encontrou o fim da grid, sai do la�o
				 											break;
				 										}
				 									}
				 								}
			 								}
			 							}
			 							
			 							// a menor distancia entre o ponto possivel e o destino
			 							double min = Math.sqrt(Math.pow(pontos[0][1]-destinationC, 2)+Math.pow(pontos[0][0]-destinationL,2));
			 							// e a posi��o dele no vetor de pontos
 		 								int pos = 0; 
 		 								for(int i = 1; i < pontos.length; i++){	// la�o para encontrar, dentre cada ponto com distancia minima at� o destino
 		 																		// de cada linha investigada, o ponto com menor distancia at� o destino
 		 									if(Math.sqrt(Math.pow(pontos[i][1]-destinationC, 2)+Math.pow(pontos[i][0]-destinationL,2)) < min){
 		 										min = Math.sqrt(Math.pow(pontos[i][1]-destinationC, 2)+Math.pow(pontos[i][0]-destinationL,2));
 		 										pos = i;
 		 									}
 		 								}
 		 								
 		 								north[0] = pontos[pos][0]; // setando a decis�o para a busca pelo norte
 		 								north[1] = pontos[pos][1];
	 								}
	 								else{ 	// se a coluna atual � a mesma do destino, n�o ser�o feitos deslocamentos na horizontal,
	 										// logo a decis�o pelo norte ser� subir "up" roteadores, onde up foi definido no la�o anterior
	 									north[0] = auxL-up;
	 									north[1] = auxC;
	 								}
	 							}
								else{	// caso up == 0, significa que, mesmo que n�o se esteja no limite do grid, n�o � poss�vel "andar" para cima
										// (por causa de um router com falha logo acima, por exemplo). neste caso a decis�o para a busca do norte
										// � desconsiderada na hora de tomar a decis�o para o next step, por isso recebe -1, -1
	 								north[0] = -1;
	 								north[1] = -1;
	 							}
 							}
	 						
	 						/*
	 						 * O processo � bastante similar para as buscar pelo sul, leste e oeste, mudando apenas a dire��o que a busca ir� andar
	 						 * para descobrir os pontos mais pr�ximos do ponto destino, ent�o basta se atentar bem ao processo do norte (que est� devidamente
	 						 * comentado), e tentar fazer as "analogias" cab�veis para entender como � o processo das outras 3 buscas
	 						 */
	 						
	 						/*
	 						 * BUSCA PELO SUL
	 						 */
	 						if(auxL+1 > lines-1){
	 							south[0] = -1;
	 							south[1] = -1;
	 						}
	 						else{
	 							int down = 0;
	 							boolean limite = false;
	 							boolean looka = false;
	 							boolean falha = false;
	 							while(!limite && !looka && !falha){
	 								if(!grid[auxL+down][auxC].isWorking()){
	 									down--;
	 									falha = true;
	 								}
	 								else if(down == lookahead){
	 									looka = true;
	 								}
	 								else if(auxL+down == lines-1){
	 									limite = true;
	 								}
	 								else{
	 									down++;
	 								}
	 							}
								if(down > 0){
	 								if(auxC != destinationC){
	 									int pontos[][] = new int[down][2];
			 							for(int i = 0; i < pontos.length; i++){
			 								int hops = lookahead-(i+1);
			 								pontos[i][0] = auxL+(i+1);
			 								if(hops == 0){
			 									pontos[i][1] = auxC;
			 								}
			 								else{
			 									double distance = Math.sqrt(Math.pow(auxC-destinationC, 2)+Math.pow((auxL+(i+1))-destinationL,2));
			 									pontos[i][1] = auxC;
			 									for(int j = 1; j <= hops; j++){
			 										if(auxC > destinationC){
			 											if(grid[auxL+1+i][auxC-j].isWorking()){
				 											if(Math.sqrt(Math.pow((auxC-j)-destinationC, 2)+Math.pow((auxL+1+i)-destinationL,2)) < distance){
				 												distance = Math.sqrt(Math.pow((auxC-j)-destinationC, 2)+Math.pow((auxL+1+i)-destinationL,2));
				 												pontos[i][1] = auxC-j;
				 											}
				 										}
			 											else{
				 											break;
				 										}
			 											
				 										if(j == hops || auxC-j == 0){
				 											break;
				 										}
				 									}
				 									else{
				 										if(grid[auxL+1+i][auxC+j].isWorking()){
				 											if(Math.sqrt(Math.pow((auxC+j)-destinationC, 2)+Math.pow((auxL+1+i)-destinationL,2)) < distance){
				 												distance = Math.sqrt(Math.pow((auxC+j)-destinationC, 2)+Math.pow((auxL+1+i)-destinationL,2));
				 												pontos[i][1] = auxC+j;
				 											}
				 										}
				 										else{
				 											break;
				 										}
				 										
				 										if(j == hops || auxC+j == columns-1){
				 											break;
				 										}
				 									}
				 								}
			 								}
			 							}
			 							
			 							double min = Math.sqrt(Math.pow(pontos[0][1]-destinationC, 2)+Math.pow(pontos[0][0]-destinationL,2));
 		 								int pos = 0; 
 		 								for(int i = 1; i < pontos.length; i++){
 		 									if(Math.sqrt(Math.pow(pontos[i][1]-destinationC, 2)+Math.pow(pontos[i][0]-destinationL,2)) < min){
 		 										min = Math.sqrt(Math.pow(pontos[i][1]-destinationC, 2)+Math.pow(pontos[i][0]-destinationL,2));
 		 										pos = i;
 		 									}
 		 								}
 		 								
 		 								south[0] = pontos[pos][0];
 		 								south[1] = pontos[pos][1];
	 								}
	 								else{
	 									south[0] = auxL+down;
	 									south[1] = auxC;
	 								}
	 							}
								else{
	 								south[0] = -1;
	 								south[1] = -1;
	 							}
 							}
	 						
	 						/*
	 						 * BUSCA PELO OESTE
	 						 */
	 						if(auxC-1 < 0){
	 							west[0] = -1;
	 							west[1] = -1;
	 						}
	 						else{
	 							int left = 0;
	 							boolean limite = false;
	 							boolean looka = false;
	 							boolean falha = false;
	 							while(!limite && !looka && !falha){
	 								if(!grid[auxL][auxC-left].isWorking()){
	 									left--;
	 									falha = true;
	 								}
	 								else if(left == lookahead){
	 									looka = true;
	 								}
	 								else if(auxC-left == 0){
	 									limite = true;
	 								}
	 								else{
	 									left++;
	 								}
	 							}
								if(left > 0){
	 								if(auxL != destinationL){
	 									int pontos[][] = new int[left][2];
			 							for(int i = 0; i < pontos.length; i++){
			 								int hops = lookahead-(i+1);
			 								pontos[i][1] = auxC-(i+1);
			 								if(hops == 0){
			 									pontos[i][0] = auxL;
			 								}
			 								else{
			 									double distance = Math.sqrt(Math.pow((auxC-i-1)-destinationC, 2)+Math.pow(auxL-destinationL,2));
			 									pontos[i][0] = auxL;
			 									for(int j = 1; j <= hops; j++){
				 									if(auxL > destinationL){
				 										if(grid[auxL-j][auxC-1-i].isWorking()){
				 											if(Math.sqrt(Math.pow((auxC-1-i)-destinationC, 2)+Math.pow((auxL-j)-destinationL,2)) < distance){
				 												distance = Math.sqrt(Math.pow((auxC-1-i)-destinationC, 2)+Math.pow((auxL-j)-destinationL,2));
				 												pontos[i][0] = auxL-j;
				 											}
				 										}
				 										else{
				 											break;
				 										}
				 										
				 										if(j == hops || auxL-j == 0){
				 											break;
				 										}
				 									}
				 									else{
				 										if(grid[auxL+j][auxC-1-i].isWorking()){
				 											if(Math.sqrt(Math.pow((auxC-1-i)-destinationC, 2)+Math.pow((auxL+j)-destinationL,2)) < distance){
				 												distance = Math.sqrt(Math.pow((auxC-1-i)-destinationC, 2)+Math.pow((auxL+j)-destinationL,2));
				 												pontos[i][0] = auxL+j;
				 											}
				 										}
				 										else{
				 											break;
			 											}
				 										
				 										if(j == hops || auxL+j == lines-1){
				 											break;
				 										}
				 									}
				 								}
			 								}
			 							}
			 							
			 							double min = Math.sqrt(Math.pow(pontos[0][1]-destinationC, 2)+Math.pow(pontos[0][0]-destinationL,2));
 		 								int pos = 0; 
 		 								for(int i = 1; i < pontos.length; i++){
 		 									if(Math.sqrt(Math.pow(pontos[i][1]-destinationC, 2)+Math.pow(pontos[i][0]-destinationL,2)) < min){
 		 										min = Math.sqrt(Math.pow(pontos[i][1]-destinationC, 2)+Math.pow(pontos[i][0]-destinationL,2));
 		 										pos = i;
 		 									}
 		 								}
 		 								
 		 								west[0] = pontos[pos][0];
 		 								west[1] = pontos[pos][1];
	 								}
	 								else{
	 									west[0] = auxL;
	 									west[1] = auxC-left;
	 								}
	 							}
								else{
	 								west[0] = -1;
	 								west[1] = -1;
	 							}
 							}
	 						/*
	 						 * BUSCA PELO LESTE
	 						 */
	 						if(auxC+1 > columns-1){
	 							east[0] = -1;
	 							east[1] = -1;
	 						}
	 						else{
	 							int right = 0;
	 							boolean limite = false;
	 							boolean looka = false;
	 							boolean falha = false;
	 							while(!limite && !looka && !falha){
	 								if(!grid[auxL][auxC+right].isWorking()){
	 									right--;
	 									falha = true;
	 								}
	 								else if(right == lookahead){
	 									looka = true;
	 								} 
	 								else if(auxC+right == columns-1){
	 									limite = true;
	 								}
	 								else{
	 									right++;
	 								}
	 							}
								if(right > 0){
	 								if(auxL != destinationL){
	 									int pontos[][] = new int[right][2];
			 							for(int i = 0; i < pontos.length; i++){
			 								int hops = lookahead-(i+1);
			 								pontos[i][1] = auxC+(i+1);
			 								if(hops == 0){
			 									pontos[i][0] = auxL;
			 								}
			 								else{
			 									double distance = Math.sqrt(Math.pow(auxC+i+1-destinationC, 2)+Math.pow(auxL-destinationL,2));
			 									pontos[i][0] = auxL;
			 									for(int j = 1; j <= hops; j++){
				 									if(auxL > destinationL){
				 										if(grid[auxL-j][auxC+1+i].isWorking()){
				 											if(Math.sqrt(Math.pow((auxC+i+1)-destinationC, 2)+Math.pow((auxL-j)-destinationL,2)) < distance){
				 												distance = Math.sqrt(Math.pow((auxC+i+1)-destinationC, 2)+Math.pow((auxL-j)-destinationL,2));
				 												pontos[i][0] = auxL-j;
				 											}
				 										}
				 										else{
				 											break;
				 										}
				 										
				 										if(j == hops || auxL-j == 0){
				 											break;
				 										}
				 									}
				 									else{
				 										if(grid[auxL+j][auxC+1+i].isWorking()){
				 											if(Math.sqrt(Math.pow((auxC+i+1)-destinationC, 2)+Math.pow((auxL+j)-destinationL,2)) < distance){
				 												distance = Math.sqrt(Math.pow((auxC+i+1)-destinationC, 2)+Math.pow((auxL+j)-destinationL,2));
				 												pontos[i][0] = auxL+j;
				 											}
				 										}
				 										else{
				 											break;
				 										}
				 										
				 										if(j == hops || auxL+j == lines-1){
				 											break;
				 										}
				 									}
				 								}
			 								}
			 							}
			 							
			 							double min = Math.sqrt(Math.pow(pontos[0][1]-destinationC, 2)+Math.pow(pontos[0][0]-destinationL,2));
 		 								int pos = 0; 
 		 								for(int i = 1; i < pontos.length; i++){
 		 									if(Math.sqrt(Math.pow(pontos[i][1]-destinationC, 2)+Math.pow(pontos[i][0]-destinationL,2)) < min){
 		 										min = Math.sqrt(Math.pow(pontos[i][1]-destinationC, 2)+Math.pow(pontos[i][0]-destinationL,2));
 		 										pos = i;
 		 									}
 		 								}
 		 								
 		 								east[0] = pontos[pos][0];
 		 								east[1] = pontos[pos][1];
	 								}
	 								else{
	 									east[0] = auxL;
	 									east[1] = auxC+right;
	 								}
	 							}
								else{
	 								east[0] = -1;
	 								east[1] = -1;
	 							}
 							}
	 						
	 						int northORsouth[] = new int[2];
	 						int westOReast[] = new int[2];
	 						
	 						// Para testes e debug
//	 						System.out.println("North: " + north[0] + ", " + north[1] + "(" + ((north[0]*columns)+north[1]) + ")");
//	 						System.out.println("South: " + south[0] + ", " + south[1] + "(" + ((south[0]*columns)+south[1]) + ")");
//	 						System.out.println("West: " + west[0] + ", " + west[1] + "(" + ((west[0]*columns)+west[1]) + ")");
//	 						System.out.println("East: " + east[0] + ", " + east[1] + "(" + ((east[0]*columns)+east[1]) + ")");
//	 						System.out.println("North to dest: " + Math.sqrt(Math.pow(north[1]-destinationC, 2)+Math.pow(north[0]-destinationL,2)));
//	 						System.out.println("South to dest: " + Math.sqrt(Math.pow(south[1]-destinationC, 2)+Math.pow(south[0]-destinationL,2)));
//	 						System.out.println("West to dest: " + Math.sqrt(Math.pow(west[1]-destinationC, 2)+Math.pow(west[0]-destinationL,2)));
//	 						System.out.println("East to dest: " + Math.sqrt(Math.pow(east[1]-destinationC, 2)+Math.pow(east[0]-destinationL,2)));
	 						
	 						/*
	 						 * Aqui j� se tem as decis�es de todas as 4 buscas, e o que ser� feito � estud�-las para saber qual delas deve ser a pr�xima decis�o
	 						 */
	 						
	 						// Testando se o ponto almejado j� n�o est� incluso no caminho (testando loops)
	 						if((actualPath.contains((north[0]*columns)+north[1]) && actualPath.contains((south[0]*columns)+south[1])) || (north[0] == -1 && south[0] == -1)){
	 							// se tanto a decis�o do norte como a do sul s�o pontos que j� est�o no caminho
	 							// a decis�o norte ou sul � considerada inv�lida e recebe -1, -1
 								northORsouth[0] = -1;
	 							northORsouth[1] = -1;
 							}
 							else if((actualPath.contains((north[0]*columns)+north[1]) || north[0] == -1) && !actualPath.contains((south[0]*columns)+south[1])){
 								// se a decis�o do norte j� est� no caminho
	 							// a decis�o norte ou sul � imediatamente setada como a decis�o sul
 								northORsouth = south;
 							}
 							else if((actualPath.contains((south[0]*columns)+south[1]) || south[0] == -1) && !actualPath.contains((north[0]*columns)+north[1])){
 								// se a decis�o do sul j� est� no caminho
	 							// a decis�o norte ou sul � imediatamente setada como a decis�o norte
 								northORsouth = north;
 							}
	 						else{
	 							// se nenhuma das decis�es norte e sul est�o j� est�o no caminho
	 							// testa-se qual dos pontos escolhidos � o mais pr�ximo do ponto destino
 								northORsouth = 	Math.sqrt(Math.pow(north[1]-destinationC, 2)+Math.pow(north[0]-destinationL,2)) >
 												Math.sqrt(Math.pow(south[1]-destinationC, 2)+Math.pow(south[0]-destinationL,2)) ?
 												south : north;
 							}
	 						
	 						/*
	 						 * O processo � o mesmo para a decis�o leste ou oeste
	 						 */
	 						
	 						if((actualPath.contains((west[0]*columns)+west[1]) && actualPath.contains((east[0]*columns)+east[1])) || (west[0] == -1 && east[0] == -1)){
	 							westOReast[0] = -1;
	 							westOReast[1] = -1;
 							}
 							else if((actualPath.contains((west[0]*columns)+west[1]) || west[0] == -1) && !actualPath.contains((east[0]*columns)+east[1])){
 								westOReast = east;
 							}
 							else if((actualPath.contains((east[0]*columns)+east[1]) || east[0] == -1) && !actualPath.contains((west[0]*columns)+west[1])){
 								westOReast = west;
 							}
	 						else{
	 							westOReast = 	Math.sqrt(Math.pow(west[1]-destinationC, 2)+Math.pow(west[0]-destinationL,2)) >
 												Math.sqrt(Math.pow(east[1]-destinationC, 2)+Math.pow(east[0]-destinationL,2)) ?
 												east : west;
 							}
	 						
	 						// Tomada de decisao final
	 						
	 						int decision[] = new int[2]; // vetor que guardar� as coordenadas da decis�o
	 						
	 						if(northORsouth[0] == -1 && westOReast[0] == -1){ // se ambas as decis�es "norte/sul" e "leste/oeste" s�o inv�lidas
	 							decision[0] = -1; // a decis�o final tamb�m ser� inv�lida
	 							decision[1] = -1;
	 						}
	 						else if(northORsouth[0] == -1){ // se a decis�o do "norte/sul" � inv�lida
	 														// a decis�o final � imediatamente setada como a decis�o "leste/oeste"
	 							decision = westOReast;
	 						}
	 						else if(westOReast[0] == -1){	// se a decis�o do "leste/oeste" � inv�lida
															// a decis�o final � imediatamente setada como a decis�o "norte/sul"
	 							decision = northORsouth;
	 						}
	 						else{	// se nenhuma das decis�es "norte/sul" e "leste/oeste" s�o inv�lidas
	 								// testa-se qual dos pontos escolhidos � o mais pr�ximo do ponto destino
	 							decision = 	Math.sqrt(Math.pow(northORsouth[1]-destinationC, 2)+Math.pow(northORsouth[0]-destinationL,2)) >
											Math.sqrt(Math.pow(westOReast[1]-destinationC, 2)+Math.pow(westOReast[0]-destinationL,2)) ?
											westOReast : northORsouth;
	 						}
	 						
	 						// Para testes e debuga
//	 						System.out.println("Decision: " + decision[0] + ", " + decision[1] + "(" + ((decision[0]*columns)+decision[1]) + ")\n");
//	 						new java.util.Scanner(System.in).nextLine();
	 						
	 						if(decision[0] == -1){ // caso a decis�o final seja inv�lida, � imposs�vel atingir o destino
	 							actualPath.add(-1);
	 						}
	 						/*
	 						 * Para casa tipo de decis�o, existe uma mecanica para ir adicionando os routers na ordem em que foram investigados
	 						 * norte/sul: YX ; leste/oeste: XY > os else's abaixo descobrem como esses routers devem ser adicionados, os adicionam
	 						 */
	 						else if(decision == north){
 								while(auxL != north[0]){
 									auxL--;
 									actualPath.add((auxL*columns)+auxC);
 									if((auxL*columns)+auxC == (destinationL*columns)+destinationC){break;}
 								}
 								if(actualPath.contains((destinationL*columns)+destinationC)){break;}
 								while(auxC != north[1]){
 									auxC = auxC > north[1] ? auxC-1 : auxC+1;
 									actualPath.add((auxL*columns)+auxC);
 									if((auxL*columns)+auxC == (destinationL*columns)+destinationC){break;}
 								}
 								if(actualPath.contains((destinationL*columns)+destinationC)){break;}
 							}
 							else if(decision == south){
 								while(auxL != south[0]){
 									auxL++;
 									actualPath.add((auxL*columns)+auxC);
 									if((auxL*columns)+auxC == (destinationL*columns)+destinationC){break;}
 								}
 								if(actualPath.contains((destinationL*columns)+destinationC)){break;}
 								while(auxC != south[1]){
 									auxC = auxC > south[1] ? auxC-1 : auxC+1;
 									actualPath.add((auxL*columns)+auxC);
 									if((auxL*columns)+auxC == (destinationL*columns)+destinationC){break;}
 								}
 								if(actualPath.contains((destinationL*columns)+destinationC)){break;}
 							}
 							else if(decision == west){
 								while(auxC != west[1]){
 									auxC--;
 									actualPath.add((auxL*columns)+auxC);
 									if((auxL*columns)+auxC == (destinationL*columns)+destinationC){break;}
 								}
 								if(actualPath.contains((destinationL*columns)+destinationC)){break;}
 								while(auxL != west[0]){
 									auxL = auxL > west[0] ? auxL-1 : auxL+1;
 									actualPath.add((auxL*columns)+auxC);
 									if((auxL*columns)+auxC == (destinationL*columns)+destinationC){break;}
 								}
 								if(actualPath.contains((destinationL*columns)+destinationC)){break;}
 							}
 							else{
 								while(auxC != east[1]){
 									auxC++;
 									actualPath.add((auxL*columns)+auxC);
 									if((auxL*columns)+auxC == (destinationL*columns)+destinationC){break;}
 								}
 								if(actualPath.contains((destinationL*columns)+destinationC)){break;}
 								while(auxL != east[0]){
 									auxL = auxL > east[0] ? auxL-1 : auxL+1;
 									actualPath.add((auxL*columns)+auxC);
 									if((auxL*columns)+auxC == (destinationL*columns)+destinationC){break;}
 								}
 								if(actualPath.contains((destinationL*columns)+destinationC)){break;}
 							}
	 					}
	 				}
					
					int[] ap = new int[actualPath.size()];	// convertendo o arrayList
					int indice = 0;							// "actualPath" para um
					for (Integer n : actualPath) {			// vetor de inteiros que ser�
						ap[indice++] = n;					// adicionado ao arrayList
					}										// "paths"
					
					paths.add(ap); // adiciona o caminho no vetor que guarda os resultados
				}
			}
		}
		
		ArrayList<int[]> resultado_aux = new ArrayList<int[]>(); // arrayLista que linka sources e destinations com seus respectivos caminhos
		for(int i = 0; i<paths.size(); i++){	// loop que far� a linkagem entre sources e destinations as rotas, transformando-os em um unico vetor
												// onde os dois primeiros elementos ser�o, respectivamente, source e destination, e os demais elementos
												// ser�o os routers que compoem a rota encontrada
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
		
		int[][] resultado =  new int[resultado_aux.size()][]; 	// convertendo o arrayList de vetores de inteiros
		for(int i = 0; i < resultado_aux.size(); i++){			// para a matriz de inteiros "resultado"
			resultado[i] = resultado_aux.get(i);
		}
		
		return resultado; // retorna a matriz de inteiros resultado;
	}
}
