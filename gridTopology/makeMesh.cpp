#include <iostream>
#include <stdio.h>
#include <sstream>
#include <stdlib.h>

using namespace std;

int main(){
	int** matrix;
	int lines;
	int columns;
	int x;
	int aux;
	cin >> lines;
	cin >> columns;
	x = lines*columns;
	stringstream ss;
	ss << lines << "x" << columns << ".txt";
	string str = ss.str();
	const char* arqName = str.c_str();
	FILE *arq = fopen(arqName, "w");
	fprintf (arq, "%d %d\n\n", lines, columns);
	matrix = (int**) calloc(x, sizeof(int*));
	
	for(int i = 0; i < x; i++){
		matrix[i] = (int*) calloc(x, sizeof(int));
	}
	
	for(int i = 0; i < x; i++){
		for(int j = 0; j < x; j++){
			matrix[i][j] = 0;
		}
	}
	
	aux = columns-1;
	for(int i = 0; i < x-columns; i++){
		matrix[i][++aux] = 1;
	}
	
	aux = columns-1;
	for(int i = 0; i < x-columns; i++){
		matrix[++aux][i] = 1;
	}
	
	for(int i = 0; i < x; i++){
		if(i%columns != columns-1){
			matrix[i][i+1] = 1;
		}		
	}
	
	for(int i = 1; i < x; i++){
		if(i%columns != 0){
			matrix[i][i-1] = 1;
		}		
	}
	
	for(int i = 0; i < x; i++){
		for(int j = 0; j < x; j++){
			fprintf (arq, "%d ", matrix[i][j]);
		}
		if(i+1 < x){
			fprintf (arq, "\n");
		}
	}
}