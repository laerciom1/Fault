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
		//adjacencyMatrix.printMatrix();
		CommunicationMatrix communicationMatrix = mfr.readCommunicationMatrix("communications/1rand9.txt");
		//communicationMatrix.printMatrix(0);
		Grid grid = new Grid(adjacencyMatrix, communicationMatrix);
		/*
		grid.printAll();
		
		int[][] GridXYPadraoHappyPath = grid.XYPadrao(communicationMatrix);
		routersUsage = statistic.routersUsage(adjacencyMatrix, GridXYPadraoHappyPath);
		routersUsagePercent = statistic.routersUsagePercent(adjacencyMatrix, GridXYPadraoHappyPath, routersUsage);
		mfw.printAll("resultados/GridXYPadraoHappyPath.txt", "XY Padrao Nao Adaptativo", adjacencyMatrix.getLines()*adjacencyMatrix.getColumns(),
				routersUsage, routersUsagePercent, GridXYPadraoHappyPath);
		System.out.print("XYPadraoHappyPath OK");
		
		int[][] GridYXPadraoHappyPath = grid.YXPadrao(communicationMatrix);
		routersUsage = statistic.routersUsage(adjacencyMatrix, GridYXPadraoHappyPath);
		routersUsagePercent = statistic.routersUsagePercent(adjacencyMatrix, GridYXPadraoHappyPath, routersUsage);
		mfw.printAll("resultados/GridYXPadraoHappyPath.txt", "YX Padrao Nao Adaptativo", adjacencyMatrix.getLines()*adjacencyMatrix.getColumns(),
				routersUsage, routersUsagePercent, GridYXPadraoHappyPath);
		System.out.print(" | YXPadraoHappyPath OK");
		
		int[][] GridXYTorusHappyPath = grid.XYTorus(communicationMatrix);
		routersUsage = statistic.routersUsage(adjacencyMatrix, GridXYTorusHappyPath);
		routersUsagePercent = statistic.routersUsagePercent(adjacencyMatrix, GridXYTorusHappyPath, routersUsage);
		mfw.printAll("resultados/GridXYTorusHappyPath.txt", "XY Torus Nao Adaptativo", adjacencyMatrix.getLines()*adjacencyMatrix.getColumns(),
				routersUsage, routersUsagePercent, GridXYTorusHappyPath);
		System.out.print(" | XYTorusHappyPath OK");
		
		int[][] GridYXTorusHappyPath = grid.YXTorus(communicationMatrix);
		routersUsage = statistic.routersUsage(adjacencyMatrix, GridYXTorusHappyPath);
		routersUsagePercent = statistic.routersUsagePercent(adjacencyMatrix, GridYXTorusHappyPath, routersUsage);
		mfw.printAll("resultados/GridYXTorusHappyPath.txt", "YX Torus Nao Adaptativo", adjacencyMatrix.getLines()*adjacencyMatrix.getColumns(),
				routersUsage, routersUsagePercent, GridYXTorusHappyPath);
		System.out.print(" | YXTorusHappyPath OK\n");
		*/
		grid.injectFaults(20);
		grid.printAll();
		/*
		int[][] GridXYPadraoInjectedFaults = grid.XYPadrao(communicationMatrix);
		routersUsage = statistic.routersUsage(adjacencyMatrix, GridXYPadraoInjectedFaults);
		routersUsagePercent = statistic.routersUsagePercent(adjacencyMatrix, GridXYPadraoInjectedFaults, routersUsage);
		mfw.printAll("resultados/GridXYPadraoInjectedFaults.txt", "XY Padrao Nao Adaptativo", adjacencyMatrix.getLines()*adjacencyMatrix.getColumns(),
				routersUsage, routersUsagePercent, GridXYPadraoInjectedFaults);
		System.out.print("XYPadraoInjectedFaults OK");
		
		int[][] GridYXPadraoInjectedFaults = grid.YXPadrao(communicationMatrix);
		routersUsage = statistic.routersUsage(adjacencyMatrix, GridYXPadraoInjectedFaults);
		routersUsagePercent = statistic.routersUsagePercent(adjacencyMatrix, GridYXPadraoInjectedFaults, routersUsage);
		mfw.printAll("resultados/GridYXPadraoInjectedFaults.txt", "YX Padrao Nao Adaptativo", adjacencyMatrix.getLines()*adjacencyMatrix.getColumns(),
				routersUsage, routersUsagePercent, GridYXPadraoInjectedFaults);
		System.out.print(" | YXPadraoInjectedFaults OK");
		
		int[][] GridXYTorusInjectedFaults = grid.XYTorus(communicationMatrix);
		routersUsage = statistic.routersUsage(adjacencyMatrix, GridXYTorusInjectedFaults);
		routersUsagePercent = statistic.routersUsagePercent(adjacencyMatrix, GridXYTorusInjectedFaults, routersUsage);
		mfw.printAll("resultados/GridXYTorusInjectedFaults.txt", "XY Torus Nao Adaptativo", adjacencyMatrix.getLines()*adjacencyMatrix.getColumns(),
				routersUsage, routersUsagePercent, GridXYTorusInjectedFaults);
		System.out.print(" | XYTorusInjectedFaults OK");
		
		int[][] GridYXTorusInjectedFaults = grid.YXTorus(communicationMatrix);
		routersUsage = statistic.routersUsage(adjacencyMatrix, GridYXTorusInjectedFaults);
		routersUsagePercent = statistic.routersUsagePercent(adjacencyMatrix, GridYXTorusInjectedFaults, routersUsage);
		mfw.printAll("resultados/GridYXTorusInjectedFaults.txt", "YX Torus Nao Adaptativo", adjacencyMatrix.getLines()*adjacencyMatrix.getColumns(),
				routersUsage, routersUsagePercent, GridYXTorusInjectedFaults);
		System.out.print(" | YXTorusInjectedFaults OK");
		*/
		int[][] GridXXYYPadraoInjectedFaults = grid.XXYYPadrao(communicationMatrix);
		routersUsage = statistic.routersUsage(adjacencyMatrix, GridXXYYPadraoInjectedFaults);
		routersUsagePercent = statistic.routersUsagePercent(adjacencyMatrix, GridXXYYPadraoInjectedFaults, routersUsage);
		mfw.printAll("resultados/GridXXYYPadraoInjectedFaults.txt", "XXYY Padrao Nao Adaptativo", adjacencyMatrix.getLines()*adjacencyMatrix.getColumns(),
				routersUsage, routersUsagePercent, GridXXYYPadraoInjectedFaults);
		System.out.print(" | XXYYPadraoInjectedFaults OK");
	}
}
