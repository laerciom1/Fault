import java.io.FileNotFoundException;
import java.io.IOException;

import IO.*;
import domain.*;

public class Aplicação {
	public static void main(String args[]) throws FileNotFoundException, IOException{
		MyFileReader mfr = new MyFileReader();
		MyFileWriter mfw = new MyFileWriter();
		Statistic statistic = new Statistic();
		int[] routersUsage;
		double[] routersUsagePercent;
		AdjacencyMatrix adjacencyMatrix = mfr.readAdjacencyMatrix("gridTopology/7x7.txt");
		CommunicationMatrix communicationMatrix = mfr.readCommunicationMatrix("communications/1to1.txt");
		Grid grid = new Grid(adjacencyMatrix);
		
		{
			grid.injectFaultListByNode(new int[]{1,8,19,47,12,22,29,36,37,38,39,17,24,40,33,26});
			grid.allocateAppByRouter(1, 0);
			grid.allocateAppByRouter(0, 0);
		}
		
//		{
//			grid.injectFaultsByPercentage(30);
//			grid.allocateAppsRand(communicationMatrix.getTasks());
//		}
		
		//grid.allocateAppsSeq(communicationMatrix.getTasks());
		grid.paintGrid();
		
		long tempo;
		
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
		/*
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
