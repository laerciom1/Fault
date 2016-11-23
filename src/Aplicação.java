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
		AdjacencyMatrix adjacencyMatrix = mfr.readAdjacencyMatrix("gridTopology/8x8.txt");
		CommunicationMatrix communicationMatrix = mfr.readCommunicationMatrix("communications/50tasks.txt");
		Grid grid = new Grid(adjacencyMatrix);
		
//		{
//			grid.injectFaultListByNode(new int[]{1,3,4,9,16,18,20,32,35,42,43,44,47,52,53,54,57,58,59});
//			grid.allocateAppByRouter(1, 17);
//			grid.allocateAppByRouter(0, 63);
//		}
		
		{
			grid.injectFaultsByPercentage(30);
			grid.allocateAppsRand(communicationMatrix.getTasks());
		}
		
		//grid.allocateAppsSeq(communicationMatrix.getTasks());
		grid.paintGrid();
		
		long tempo;
		
		tempo = System.currentTimeMillis();
		int[][] GridParametrizavel = grid.ParametrizavelPadrao(communicationMatrix,4);		
		tempo = System.currentTimeMillis()-tempo;
		routersUsage = statistic.routersUsage(adjacencyMatrix, GridParametrizavel);
		routersUsagePercent = statistic.routersUsagePercent(adjacencyMatrix, GridParametrizavel, routersUsage);
		mfw.printAll("resultados/Grid Parametrizavel Padrao.txt", "Parametrizavel Padrão", grid, routersUsage, routersUsagePercent, tempo, GridParametrizavel);
		System.out.print("Parametrizavel Padrão OK\n");
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
