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
		grid.injectFaults(20);
		grid.allocateApps(communicationMatrix.getTasks());
		grid.printAll();
		
		int[][] GridXXYYPadraoInjectedFaults = grid.XXYYPadrao(communicationMatrix);
		routersUsage = statistic.routersUsage(adjacencyMatrix, GridXXYYPadraoInjectedFaults);
		routersUsagePercent = statistic.routersUsagePercent(adjacencyMatrix, GridXXYYPadraoInjectedFaults, routersUsage);
		mfw.printAll("resultados/GridXXYYPadraoInjectedFaults.txt", "XXYY Padrao Nao Adaptativo", adjacencyMatrix.getLines()*adjacencyMatrix.getColumns(),
				routersUsage, routersUsagePercent, GridXXYYPadraoInjectedFaults);
		System.out.print("XXYYPadraoInjectedFaults OK\n");
	}
}
