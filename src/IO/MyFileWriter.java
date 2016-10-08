package IO;

import java.io.FileWriter;
import java.io.IOException;

public class MyFileWriter {
	private FileWriter file;

	public void printAll(String s, String title, int routers, int[] routersUsage, double[] routersUsagePercent, int[][] result, long tempo, int faults) throws IOException{
		file = new FileWriter(s);
		file.write("-- " + title);
		file.close();
		printRoutersUsage(s, routers, routersUsage);
		printRoutersUsagePercent(s, routers, routersUsagePercent);
		printFaults(s, faults);
		printSuccessfulCommunications(s, result);
		printCommunications(s, result);
		printTempo(s, tempo);
	}
	
	public void printRoutersUsage(String s, int routers, int[] routersUsage) throws IOException{
		file = new FileWriter(s, true);
		file.write("\n\n-- Uso dos roteadores [Qtd de comunicações que usam cada roteador]\n\n");
		for(int i = 1; i < routers+1; i++){
			file.write("Router " + i + ": " + routersUsage[i-1]);
			if(i % 5 == 0){
				file.write("\n");
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
		for(int i = 1; i < routers+1; i++){
			String rup = String.format("%.2f", routersUsagePercent[i-1]);
			
			file.write("Router " + i + ": " + rup);
			if(i % 5 == 0){
				file.write("\n");
			}
			else{
				if(i < 10 || routersUsagePercent[i-1] < 10){
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
			for (int j=2; j < communications[i].length; j++){
				if(communications[i][j] == -1){
					file.write("# Não foi encontrado um caminho a partir do router " + communications[i][j-1] + " #");
					break;
				}
				else{
					file.write(communications[i][j] + " ");	
				}
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
