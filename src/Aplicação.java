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
		AdjacencyMatrix adjacencyMatrix = mfr.readAdjacencyMatrix("gridTopology/6x6.txt");
		//adjacencyMatrix.printMatrix();
		CommunicationMatrix communicationMatrix = mfr.readCommunicationMatrix("communications/rand0000.txt");
		//communicationMatrix.printMatrix(0);
		Grid grid = new Grid(adjacencyMatrix);
		/*{
			grid.injectFaultByNode(2);
			grid.injectFaultByNode(7);
			grid.injectFaultByNode(12);
			grid.injectFaultByNode(17);
			grid.injectFaultByNode(22);
		}
		{
			grid.allocateAppByRouter(1, 10);
			grid.allocateAppByRouter(0, 14);
		}*/
		int faults = grid.injectFaultsByPercentage(30);
		grid.allocateAppsRand(communicationMatrix.getTasks());
		//grid.allocateAppsSeq(communicationMatrix.getTasks());
		grid.printAll();
		//grid.printCommunications(communicationMatrix);
		
		long tempo;
		
		tempo = System.currentTimeMillis();
		int[][] GridXYPadraoInjectedFaults = grid.XYPadrao(communicationMatrix);
		tempo = System.currentTimeMillis()-tempo;
		routersUsage = statistic.routersUsage(adjacencyMatrix, GridXYPadraoInjectedFaults);
		routersUsagePercent = statistic.routersUsagePercent(adjacencyMatrix, GridXYPadraoInjectedFaults, routersUsage);
		mfw.printAll("resultados/GridXYPadraoInjectedFaults.txt", "XY Padrão não adaptativo", adjacencyMatrix.getLines()*adjacencyMatrix.getColumns(),
				routersUsage, routersUsagePercent, GridXYPadraoInjectedFaults, tempo, faults);
		System.out.print("XYPadrao OK\n");
		
		
		tempo = System.currentTimeMillis();
		int[][] GridXXYYPadraoInjectedFaults = grid.XXYYPadrao(communicationMatrix);
		tempo = System.currentTimeMillis()-tempo;
		routersUsage = statistic.routersUsage(adjacencyMatrix, GridXXYYPadraoInjectedFaults);
		routersUsagePercent = statistic.routersUsagePercent(adjacencyMatrix, GridXXYYPadraoInjectedFaults, routersUsage);
		mfw.printAll("resultados/GridXXYYPadraoInjectedFaults.txt", "XXYY Padrão não adaptativo", adjacencyMatrix.getLines()*adjacencyMatrix.getColumns(),
				routersUsage, routersUsagePercent, GridXXYYPadraoInjectedFaults, tempo, faults);
		System.out.print("XXYYPadraoInjectedFaults OK\n");
	}
}
