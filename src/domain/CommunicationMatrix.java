package domain;

public class CommunicationMatrix {
	private int matrix[][];
	private int tasks;
	
	public CommunicationMatrix(int tasks, int matrix[][]){
		this.matrix = matrix;
		this.tasks = tasks;
	}
	
	public int get(int i, int j){
		return matrix[i][j];
	}
	
	public int[][] getMatrix(){
		return matrix;
	}
	
	public int getTasks(){
		return tasks;
	}
	
	public void printMatrix(int option){
		if(option == 1 || option == 0){
			for(int i = 0; i < tasks; i++){
				for(int j = 0; j < tasks+option; j++){
					System.out.print(matrix[i][j] + " ");
				}
				System.out.print("\n");
			}
		}
		else{
			System.out.print("Opcao invalida!!\n");
		}
	}
}
