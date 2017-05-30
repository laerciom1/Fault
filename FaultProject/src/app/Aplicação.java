package app;
import java.io.FileNotFoundException;
import java.io.IOException;

import IO.*;
import domain.*;

public class Aplica��o {
	public static void main(String args[]) throws FileNotFoundException, IOException{
		MyFileReader myFileReader = new MyFileReader();
		MyFileWriter myFileWriter = new MyFileWriter();
		Statistic statistic = new Statistic();
		int[] routersUsage;
		double[] routersUsagePercent;
		AdjacencyMatrix adjacencyMatrix = myFileReader.readAdjacencyMatrix("gridTopology/9x9.txt");
		CommunicationMatrix communicationMatrix = myFileReader.readCommunicationMatrix("communications/1to1.txt");
		Grid grid = new Grid(adjacencyMatrix);
		
		{
			grid.allocateAppByRouter(1, 72);
			grid.allocateAppByRouter(0, 0);
			int[] routers = {5, 6, 10, 18, 20, 23, 26, 29, 35, 36, 38, 39, 43, 46, 48, 49, 51, 61, 63, 64, 67, 68, 69, 70};
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
		myFileWriter.printAll("resultados/Grid Parametrizavel Padrao.txt", "Parametrizavel Padr�o", grid, routersUsage, routersUsagePercent, tempo, GridParametrizavel);
		System.out.print("Parametrizavel Padr�o OK\n");
		
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
		mfw.printAll("resultados/Grid Dijkstra Padrao.txt", "Dijkstra Padr�o", grid, routersUsage, routersUsagePercent, tempo, GridDijkstra);
		System.out.print("Dijkstra Padr�o OK\n");
		
		tempo = System.currentTimeMillis();
		int[][] GridXXYYPadrao = grid.XXYYPadrao(communicationMatrix);
		tempo = System.currentTimeMillis()-tempo;
		routersUsage = statistic.routersUsage(adjacencyMatrix, GridXXYYPadrao);
		routersUsagePercent = statistic.routersUsagePercent(adjacencyMatrix, GridXXYYPadrao, routersUsage);
		mfw.printAll("resultados/Grid XXYY Padr�o.txt", "XXYY Padr�o n�o adaptativo", grid, routersUsage, routersUsagePercent, tempo, GridXXYYPadrao);
		System.out.print("XXYY Padr�o OK\n");
		
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
		mfw.printAll("resultados/Grid XY Padr�o.txt", "XY Padr�o", grid, routersUsage, routersUsagePercent, tempo, GridXYPadrao);
		System.out.print("XY Padr�o OK\n");
		
		tempo = System.currentTimeMillis();
		int[][] GridYXPadrao = grid.YXPadrao(communicationMatrix);
		tempo = System.currentTimeMillis()-tempo;
		routersUsage = statistic.routersUsage(adjacencyMatrix, GridYXPadrao);
		routersUsagePercent = statistic.routersUsagePercent(adjacencyMatrix, GridYXPadrao, routersUsage);
		mfw.printAll("resultados/Grid YX Padr�o.txt", "YX Padr�o", grid, routersUsage, routersUsagePercent, tempo, GridYXPadrao);
		System.out.print("YX Padr�o OK\n");
		*/
	}
}
