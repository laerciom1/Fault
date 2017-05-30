import java.io.FileNotFoundException;
import java.io.IOException;

import IO.*;
import domain.*;

public class Testes {
	public static void main(String args[]) throws FileNotFoundException, IOException{
		Testes testes = new Testes();		
		String _4x4 = "gridTopology/4x4.txt";
		String _6x6 = "gridTopology/6x6.txt";
		String _8x8 = "gridTopology/8x8.txt";
		String _10x10 = "gridTopology/10x10.txt";
		
		int lookahead = 1;
		int execucoes = 10000;
		testes.teste(_4x4, lookahead, execucoes);
		testes.teste(_6x6, lookahead, execucoes);
		testes.teste(_8x8, lookahead, execucoes);
		testes.teste(_10x10, lookahead, execucoes);
		
	}
	
	public void teste(String topology, int lookahead, int execucoes) throws FileNotFoundException, IOException{
		MyFileReader mfr = new MyFileReader();
		CommunicationMatrix communicationMatrix = mfr.readCommunicationMatrix("communications/50tasks.txt");
		AdjacencyMatrix adjacencyMatrix = mfr.readAdjacencyMatrix(topology);
		Grid grid = new Grid(adjacencyMatrix);
		
		int[][][] resultado;
		int somaValidas;
		int somaInvalidas;
		int cmmValidas;
		int cmmInvalidas;
		double mediaValidas;
		double mediaInvalidas;
		
		/* 10% */
		
		resultado = new int[execucoes][][];
		somaValidas = 0;
		somaInvalidas = 0;
		cmmValidas = 0;
		cmmInvalidas = 0;
		
		for(int i = 0; i < resultado.length; i++){
			grid.deallocateApps();
			grid.injectFaultsByPercentage(10);
			grid.allocateAppsRand(communicationMatrix.getTasks());
			resultado[i] = grid.ParametrizavelPadrao(communicationMatrix, lookahead);
		}
		
		for(int i = 0; i < resultado.length; i++){
			for(int j = 0; j < resultado[i].length; j++){
				if(resultado[i][j][resultado[i][j].length-1] != -1){
					somaValidas += resultado[i][j].length-3;
					cmmValidas++;
				}
				else{
					somaInvalidas += resultado[i][j].length-4;
					cmmInvalidas++;
				}
			}
		}
		
		mediaValidas = (double) somaValidas/cmmValidas;
		mediaInvalidas = (double) somaInvalidas/cmmInvalidas;
		
		System.out.println(mediaValidas + "	" + mediaInvalidas + "	" + cmmValidas + "	" + cmmInvalidas);
		
		/* 20% */
		
		resultado = new int[execucoes][][];
		somaValidas = 0;
		somaInvalidas = 0;
		cmmValidas = 0;
		cmmInvalidas = 0;
		
		for(int i = 0; i < resultado.length; i++){
			grid.deallocateApps();
			grid.injectFaultsByPercentage(20);
			grid.allocateAppsRand(communicationMatrix.getTasks());
			resultado[i] = grid.ParametrizavelPadrao(communicationMatrix, lookahead);
		}
		
		for(int i = 0; i < resultado.length; i++){
			for(int j = 0; j < resultado[i].length; j++){
				if(resultado[i][j][resultado[i][j].length-1] != -1){
					somaValidas += resultado[i][j].length-3;
					cmmValidas++;
				}
				else{
					somaInvalidas += resultado[i][j].length-4;
					cmmInvalidas++;
				}
			}
		}
		
		mediaValidas = (double) somaValidas/cmmValidas;
		mediaInvalidas = (double) somaInvalidas/cmmInvalidas;
		
		System.out.println(mediaValidas + "	" + mediaInvalidas + "	" + cmmValidas + "	" + cmmInvalidas);
		
		/* 50% */
		
		resultado = new int[execucoes][][];
		somaValidas = 0;
		somaInvalidas = 0;
		cmmValidas = 0;
		cmmInvalidas = 0;
		
		for(int i = 0; i < resultado.length; i++){
			grid.deallocateApps();
			grid.injectFaultsByPercentage(50);
			grid.allocateAppsRand(communicationMatrix.getTasks());
			resultado[i] = grid.ParametrizavelPadrao(communicationMatrix, lookahead);
		}
		
		for(int i = 0; i < resultado.length; i++){
			for(int j = 0; j < resultado[i].length; j++){
				if(resultado[i][j][resultado[i][j].length-1] != -1){
					somaValidas += resultado[i][j].length-3;
					cmmValidas++;
				}
				else{
					somaInvalidas += resultado[i][j].length-4;
					cmmInvalidas++;
				}
			}
		}
		
		mediaValidas = (double) somaValidas/cmmValidas;
		mediaInvalidas = (double) somaInvalidas/cmmInvalidas;
		
		System.out.println(mediaValidas + "	" + mediaInvalidas + "	" + cmmValidas + "	" + cmmInvalidas);
		
		/* 80% */
		
		resultado = new int[execucoes][][];
		somaValidas = 0;
		somaInvalidas = 0;
		cmmValidas = 0;
		cmmInvalidas = 0;
		
		for(int i = 0; i < resultado.length; i++){
			grid.deallocateApps();
			grid.injectFaultsByPercentage(80);
			grid.allocateAppsRand(communicationMatrix.getTasks());
			resultado[i] = grid.ParametrizavelPadrao(communicationMatrix, lookahead);
		}
		
		for(int i = 0; i < resultado.length; i++){
			for(int j = 0; j < resultado[i].length; j++){
				if(resultado[i][j][resultado[i][j].length-1] != -1){
					somaValidas += resultado[i][j].length-3;
					cmmValidas++;
				}
				else{
					somaInvalidas += resultado[i][j].length-4;
					cmmInvalidas++;
				}
			}
		}
		
		mediaValidas = (double) somaValidas/cmmValidas;
		mediaInvalidas = (double) somaInvalidas/cmmInvalidas;
		
		System.out.println(mediaValidas + "	" + mediaInvalidas + "	" + cmmValidas + "	" + cmmInvalidas);
	}
}
