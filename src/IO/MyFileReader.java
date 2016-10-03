package IO;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import domain.AdjacencyMatrix;
import domain.CommunicationMatrix;

import java.io.FileReader;

public class MyFileReader {
	private BufferedReader bf;
	
	public AdjacencyMatrix readAdjacencyMatrix(String s) throws IOException,FileNotFoundException{
		int lines, columns;
		int[][] matrix;
		String[] aux;
		
		bf = new BufferedReader(new FileReader(s));
		aux = bf.readLine().split(" ");
		lines = Integer.parseInt(aux[0]);
		columns = Integer.parseInt(aux[1]);
		
		bf.readLine(); // Pular a linha em branco.
		
		matrix = new int[lines*columns][lines*columns];
		
		for(int i = 0; i < matrix.length; i++){
			aux = bf.readLine().split(" ");
			for(int j = 0; j < matrix[i].length; j++){
				matrix[i][j] = Integer.parseInt(aux[j]);
			}
		}		
		AdjacencyMatrix am = new AdjacencyMatrix(lines, columns, matrix);
		return am;
	}
	
	public CommunicationMatrix readCommunicationMatrix(String s) throws IOException,FileNotFoundException{
		int tasks = 0;
		int[][] matrix;
		String[] aux;
		
		bf = new BufferedReader(new FileReader(s));
		aux = bf.readLine().split(" ");
		tasks = Integer.parseInt(aux[0]) + 2;
		bf.readLine(); // Pulando a linha da task 0;
		
		matrix = new int[tasks][tasks];
		
		for(int i = 1; i < tasks; i++){
			aux = bf.readLine().split(" ");
			for(int j = 3; j < aux.length; j++){
				matrix[i][Integer.parseInt(aux[j])] = 1;
			}
		}	
		
		CommunicationMatrix cm = new CommunicationMatrix(tasks, matrix);
		return cm;
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException{
		MyFileReader mfr = new MyFileReader();
		mfr.readCommunicationMatrix("communications/1rand9.txt");
	}
}