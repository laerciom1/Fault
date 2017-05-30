package app;
import java.io.FileNotFoundException;
import java.io.IOException;

import IO.*;
import domain.*;

public class Aplicação {
	public static void main(String args[]) throws FileNotFoundException, IOException{
		MyFileReader myFileReader = new MyFileReader();
		MyFileWriter myFileWriter = new MyFileWriter();
		Statistic statistic = new Statistic();
		int[] routersUsage;
		double[] routersUsagePercent;
		AdjacencyMatrix adjacencyMatrix = myFileReader.readAdjacencyMatrix("gridTopology/10x10.txt");
		CommunicationMatrix communicationMatrix = myFileReader.readCommunicationMatrix("communications/1to1.txt");
		Grid grid = new Grid(adjacencyMatrix);
		
		{
			grid.allocateAppByRouter(1, 7);
			grid.allocateAppByRouter(0, 64);
			int[] routers = {1, 2, 5, 6, 9, 12, 16, 17, 26, 28, 31, 33, 42, 43, 47, 48, 49, 51, 54, 65, 66, 67, 70, 76, 85, 87, 91, 95, 98, 99};
			grid.injectFaultListByNode(routers);

//			grid.injectFaultsByPercentage(30);
//			grid.allocateAppsRand(communicationMatrix.getTasks());
		}
		
		grid.paintGrid();
		
		long tempo;
		
		tempo = System.currentTimeMillis();
		int[][] GridParametrizavel = grid.ParametrizavelPadrao(communicationMatrix, 5);	
		//int[][] GridParametrizavel = grid.ParametrizavelPadrao(communicationMatrix, 1);
		tempo = System.currentTimeMillis()-tempo;
		routersUsage = statistic.routersUsage(adjacencyMatrix, GridParametrizavel);
		routersUsagePercent = statistic.routersUsagePercent(adjacencyMatrix, GridParametrizavel, routersUsage);
		myFileWriter.printAll("resultados/Grid Parametrizavel Padrao.txt", "Parametrizavel Padrão", grid, routersUsage, routersUsagePercent, tempo, GridParametrizavel);
		System.out.print("Parametrizavel Padrão OK\n");
		
		for(int i = 0; i < GridParametrizavel.length; i++){
			for(int j = 0; j < GridParametrizavel[i].length; j++){
				System.out.print(GridParametrizavel[i][j] + " ");
			}
			System.out.println(" ");
		}
		/*
		tempo = System.currentTimeMillis();
		int[][] GridDijkstra = grid.Dijkstra(communicationMatrix);
		tempo = System.currentTimeMillis()-tempo;
		routersUsage = statistic.routersUsage(adjacencyMatrix, GridDijkstra);
		routersUsagePercent = statistic.routersUsagePercent(adjacencyMatrix, GridDijkstra, routersUsage);
		mfw.printAll("resultados/Grid Dijkstra Padrao.txt", "Dijkstra Padrão", grid, routersUsage, routersUsagePercent, tempo, GridDijkstra);
		System.out.print("Dijkstra Padrão OK\n");
		
		tempo = System.currentTimeMillis();
		int[][] GridXXYYPadrao = grid.XXYYPadrao(communicationMatrix);
		tempo = System.currentTimeMillis()-tempo;
		routersUsage = statistic.routersUsage(adjacencyMatrix, GridXXYYPadrao);
		routersUsagePercent = statistic.routersUsagePercent(adjacencyMatrix, GridXXYYPadrao, routersUsage);
		mfw.printAll("resultados/Grid XXYY Padrão.txt", "XXYY Padrão não adaptativo", grid, routersUsage, routersUsagePercent, tempo, GridXXYYPadrao);
		System.out.print("XXYY Padrão OK\n");
		
		tempo = System.currentTimeMillis();
		int[][] GridXYTorus = grid.XYTorus(communicationMatrix);
		tempo = System.currentTimeMillis()-tempo;
		routersUsage = statistic.routersUsage(adjacencyMatrix, GridXYTorus);
		routersUsagePercent = statistic.routersUsagePercent(adjacencyMatrix, GridXYTorus, routersUsage);
		mfw.printAll("resultados/Grid XY Torus.txt", "XY Torus", grid, routersUsage, routersUsagePercent, tempo, GridXYTorus);
		System.out.print("XY Torus OK\n");
		
		tempo = System.currentTimeMillis();
		int[][] GridYXTorus = grid.YXTorus(communicationMatrix);
		tempo = System.currentTimeMillis()-tempo;
		routersUsage = statistic.routersUsage(adjacencyMatrix, GridYXTorus);
		routersUsagePercent = statistic.routersUsagePercent(adjacencyMatrix, GridYXTorus, routersUsage);
		mfw.printAll("resultados/Grid YX Torus.txt", "YX Torus", grid, routersUsage, routersUsagePercent, tempo, GridYXTorus);
		System.out.print("YX Torus OK\n");
		
		tempo = System.currentTimeMillis();
		int[][] GridXYPadrao = grid.XYPadrao(communicationMatrix);
		tempo = System.currentTimeMillis()-tempo;
		routersUsage = statistic.routersUsage(adjacencyMatrix, GridXYPadrao);
		routersUsagePercent = statistic.routersUsagePercent(adjacencyMatrix, GridXYPadrao, routersUsage);
		mfw.printAll("resultados/Grid XY Padrão.txt", "XY Padrão", grid, routersUsage, routersUsagePercent, tempo, GridXYPadrao);
		System.out.print("XY Padrão OK\n");
		
		tempo = System.currentTimeMillis();
		int[][] GridYXPadrao = grid.YXPadrao(communicationMatrix);
		tempo = System.currentTimeMillis()-tempo;
		routersUsage = statistic.routersUsage(adjacencyMatrix, GridYXPadrao);
		routersUsagePercent = statistic.routersUsagePercent(adjacencyMatrix, GridYXPadrao, routersUsage);
		mfw.printAll("resultados/Grid YX Padrão.txt", "YX Padrão", grid, routersUsage, routersUsagePercent, tempo, GridYXPadrao);
		System.out.print("YX Padrão OK\n");
		*/
	}
}
