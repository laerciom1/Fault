package domain;

public class AdjacencyMatrix {
	private int matrix[][];
	private int columns;
	private int lines;
	
	public AdjacencyMatrix(int lines, int columns, int matrix[][]){
		this.matrix = matrix;
		this.lines = lines;
		this.columns = columns;
	}
	
	public int get(int i, int j){
		return matrix[i][j];
	}
	
	public int[][] getMatrix(){
		return matrix;
	}
	
	public int getColumns(){
		return columns;
	}
	
	public int getLines(){
		return lines;
	}
	
	public void printMatrix(){
		for(int i = 0; i < columns*lines; i++){
			for(int j = 0; j < columns*lines; j++){
				System.out.print(matrix[i][j] + " ");
			}
		System.out.print("\n");
		}
	}
}
