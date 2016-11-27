#include <iostream>
#include <cstdlib>
#include <string>
#include <stdio.h>
#include <vector>
using namespace std;

FILE* arq;
int KK;
 
bool contains(vector<int> &v, int b){			// Funcao para saber se o elemento
	for(unsigned int i = 0; i < v.size(); i++){	// ja foi marcado ou não. Usada
		if((v.at(i) == b)){						// para evitar ciclos.
			return true;
		}
	}
	return false;
}

class Graph
{
public:
    int V;    // numero de vertices
	int** matrizAdj; // matriz de adjacencia
    Graph(char* s);
    void caminhosPossiveis(vector<int> &vetor, int begin, int end);
};
 
Graph::Graph(char* s)
{
	int largura, altura;									// Lendo as informacoes do grafo
	arq = fopen(s, "r");									// pelo arquivo.
	fscanf(arq, "%d", &largura);
	fscanf(arq, "%d", &altura);
    this->V = largura*altura;								// Numero de vertices.
    
	matrizAdj = (int**) calloc(largura*altura, sizeof(int*));				// Alocando espaco
    for(int i = 0; i < largura*altura; i++){								// para armazenar a
		matrizAdj[i] = (int*) calloc(largura*altura, sizeof(int));			// matriz de adj.
	}
	
	int aux, i=0, j=0;
    while (fscanf(arq, "%d", &aux) != EOF){		// Atribuindo os valores			
		matrizAdj[i][j] = aux;					// nas posicoes da matriz.
		j++;
		if(j==V){
			i++;
			j=0;
		}
	}
}

void Graph::caminhosPossiveis(vector<int> &vetor, int begin, int end){
	vetor.push_back(begin);	
	
	if(begin == end){	// Caso encontre o destino		
		fprintf (arq, "%dto%d_%d ", vetor.at(0), vetor.at(vetor.size()-1), KK++);								  
		for(unsigned int j = 0; j < vetor.size(); j++){
			fprintf (arq, "%d ",vetor.at(j));
		}
		fprintf (arq, "\n");
	}
	
	for(int i=0; i < V; i++){
		if(matrizAdj[begin][i] != 0 && !contains(vetor, i)){	// Busca um proximo passo na
			caminhosPossiveis(vetor, i, end);					// matriz de adj. e caso encontra
			vetor.pop_back();									// algum que ainda nao foi visitado
		}														// chama a função recursivamente,
	}															// partindo agora do node encontrado.
};																// Na volta ele remove o node do vetor,
																// para que ele possa ser usado nos novos
																// caminhos que ainda serao encontrados.
 
int main()
{
    Graph g("5x6.txt");	// Especificar arquivo da matriz de adjacencia.
 						// Verificar tambem se o arquivo está dentro do template correto!!!
	int rotas = ((g.V-1)*g.V)/2;
	int rotasPossiveis[rotas][2];
	int aux=0;
	int i = 0;
	int j = 1;
	while(aux < rotas){
		rotasPossiveis[aux][0] = i;
		rotasPossiveis[aux][1] = j;
		j++;
		aux++;
		if(j > g.V - 1){
			j = ++i + 1;
		}
	}
	
 	vector<int> vetor;
 	arq = fopen("saida_5x6_all_arq.txt", "w");		// Especificar o arquivo de saida, que contera os caminhos	
	for(int l = 0; l < rotas; l++){
		vetor.clear();
		KK = 0;
		g.caminhosPossiveis(vetor, rotasPossiveis[l][0], rotasPossiveis[l][1]);
		// Especificar 2 nos para encontrar todos os caminhos possiveis entre eles	
	}
    return 0;
}