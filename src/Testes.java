import java.io.FileNotFoundException;
import java.io.IOException;

import IO.*;
import domain.*;

public class Testes {
	public static void main(String args[]) throws FileNotFoundException, IOException{
		MyFileReader mfr = new MyFileReader();
		AdjacencyMatrix adjacencyMatrix = mfr.readAdjacencyMatrix("gridTopology/6x6.txt");
		CommunicationMatrix communicationMatrix = mfr.readCommunicationMatrix("communications/50tasks.txt");
		Grid grid = new Grid(adjacencyMatrix);
		
		int[][][] resultado = new int[10000][][];
		int soma = 0;
		int cmm = 0;
		double media;
		
		for(int i = 0; i < resultado.length; i++){
			grid.deallocateApps();
			grid.injectFaultsByPercentage(80);
			grid.allocateAppsRand(communicationMatrix.getTasks());
			resultado[i] = grid.Dijkstra(communicationMatrix);
		}
		
		for(int i = 0; i < resultado.length; i++){
			for(int j = 0; j < resultado[i].length; j++){
				if(resultado[i][j][resultado[i][j].length-1] != -1){
					soma += resultado[i][j].length-3;
				}
				else{
					soma += resultado[i][j].length-3;
				}
			}
		}
		
		for(int i = 0; i < resultado.length; i++){
			cmm += resultado[i].length;
		}
		
		media = (double) soma/cmm;
		System.out.println("SALTOS: " + soma + "; CMMS: " + cmm + "; MEDIA: " + media);
	}
}
