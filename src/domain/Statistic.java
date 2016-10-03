package domain;

public class Statistic {
	public int[] routersUsage(AdjacencyMatrix am, int[][] results){
		int[] routersUsage = new int[am.getColumns()*am.getLines()];
		for (int i = 0; i < results.length; i++){
			for (int j=2; j < results[i].length; j++){
				if(results[i][j] == -1){
					break;
				}
				routersUsage[results[i][j]-1]++;
			}
		}
		return routersUsage;
	}
	
	public double[] routersUsagePercent(AdjacencyMatrix am, int[][] results, int[]routersUsage){
		double[] routersUsagePercent = new double[am.getColumns()*am.getLines()];
		for(int i = 0; i < am.getLines()*am.getColumns(); i++){
			double x = (double) routersUsage[i]/results.length;
			routersUsagePercent[i] = x*100;
		}
		return routersUsagePercent;
	}
}
