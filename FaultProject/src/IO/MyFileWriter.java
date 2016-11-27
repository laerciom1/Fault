package IO;

import java.io.FileWriter;
import java.io.IOException;

import domain.Grid;

public class MyFileWriter {
	private FileWriter file;

	public void printAll(String s, String title, Grid grid, int[] routersUsage, double[] routersUsagePercent, long tempo, int[][] result) throws IOException{
		file = new FileWriter(s);
		file.write("-- " + title);
		file.close();
		printRoutersUsage(s, grid.getNumberOfRouters(), routersUsage);
		printRoutersUsagePercent(s, grid.getNumberOfRouters(), routersUsagePercent);
		printFaults(s, grid.getNumberOfFaults());
		printSuccessfulCommunications(s, result);
		printCommunications(s, result);
		printTempo(s, tempo);
	}
	
	public void printRoutersUsage(String s, int routers, int[] routersUsage) throws IOException{
		file = new FileWriter(s, true);
		file.write("\n\n-- Uso dos roteadores [Qtd de comunicações que usam cada roteador]\n\n");
		int count = 0;
		for(int i = 0; i < routers; i++){
			file.write("Router " + i + ": " + routersUsage[i]);
			count++;
			if(count>5){
				file.write("\n");
				count = 0;
			}
			else{
				if(i < 10){
					file.write("		|	");
				}
				else{
					file.write("	|	");
				}
			}
		}
		file.close();
	}
	
	public void printRoutersUsagePercent(String s, int routers, double[] routersUsagePercent) throws IOException{
		file = new FileWriter(s, true);
		file.write("\n\n-- Uso dos roteadores [Qtd de comunicações que usam cada roteador/Total de comunicações]\n\n");
		int count = 0;
		for(int i = 0; i < routers; i++){
			String rup = String.format("%.2f", routersUsagePercent[i]);
			file.write("Router " + i + ": " + rup);
			count++;
			if(count > 5){
				file.write("\n");
				count=0;
			}
			else{
				if(routersUsagePercent[i] > 99){
					file.write("	|	");
				}
				else if(i < 10 || routersUsagePercent[i] < 10){
					file.write("		|	");
				} 
				else{
					file.write("	|	");
				}
			}
		}
		file.close();
	}
	
	public void printSuccessfulCommunications(String s, int[][] communications) throws IOException{
		int misscommunications = 0;
		for (int i = 0; i < communications.length; i++){
			if(communications[i][communications[i].length-1] == -1){
				misscommunications++;
			}
		}
		file = new FileWriter(s, true);
		file.write("\n\n-- " + (communications.length - misscommunications) + " Comunicação tiveram sucesso.");
		file.write("\n-- Porcentagem de falha: " + (double) ((misscommunications*100)/communications.length) + "\n");
		file.close();
	}
	
	public void printCommunications(String s, int[][] communications) throws IOException{
		file = new FileWriter(s, true);
		file.write("\n\n-- Foram avaliadas " + communications.length + " comunicacoes:\n\n");
		for (int i = 0; i < communications.length; i++){
			file.write("App " + communications[i][0] + " to App " + communications[i][1] + " > Path: ");
			if(communications[i][communications[i].length-1] != -1){
				for (int j=2; j < communications[i].length; j++){
					file.write(communications[i][j] + " ");	
				}
				file.write("(" + ((communications[i].length-3)) + " saltos ~ " + ((communications[i].length-3)*3) + " ciclos)");	
			}
			else{
				file.write("Não foi possível encontrar um caminho para realizar essa comunicação");	
			}
			
			file.write("\n");
		}
		file.close();
	}
	
	public void printTempo(String s, long tempo) throws IOException{
		file = new FileWriter(s, true);
		file.write("\n\n-- Tempo de execução: " + tempo + " ms.");
		file.close();
	}
	
	public void printFaults(String s, int faults) throws IOException{
		file = new FileWriter(s, true);
		file.write("\n\n-- Falhas injetadas: " + faults + ".");
		file.close();
	}
}
