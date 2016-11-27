#include <iostream>
#include <cstdlib>
#include <string>
#include <stdio.h>
#include <vector>

#define N 25

using namespace std;

int main()
{
	FILE* arq;
	FILE* resposta;
	arq = fopen("saida_5x5_all.txt", "r");
	int caminhos = 0;
	int vetor[N];
	for(int i = 0; i < N; i++){
		vetor[i] = 0;
	}
	
	int aux;
	string str;
	while (aux != EOF){				// Contando quantas vezes cada
		aux = fgetc(arq);			// elemento do grid aparece
		if(aux == ' '){				// para saber se vai ser um
			while (aux != '\n'){	// elemento basic ou repeat
				aux = fgetc(arq);
				if(aux == ' '){
					int i = atoi(str.c_str());
					vetor[i]++;
					str.clear();
				}
				else{
					str.push_back(aux);	
				}
			}
			if(aux == '\n'){
				caminhos++;
			}
		}
	}
	
	for(int i = 0; i < N; i++){
		cout << vetor[i] << " ";
	}
	
	rewind(arq);
	
	resposta = fopen("ftree_5x5_all.txt", "w");
	fprintf(resposta, "format 8\nfactor on\n\n\nftree VAR_FTREE_NAME\n");
	for(int i = 0; i < N; i++){
		if(vetor[i] > 1){
			fprintf(resposta, "repeat e%d exp(%.1f)\n", i+1, 0.1); // DEFINIR COMO SERA PASSADO A CHANCE DE FALHA DO NODE
		}
		else if(vetor[i] > 0){
			fprintf(resposta, "basic e%d exp(%.1f)\n", i+1, 0.1);
		}
	}
	
	vector<string> ors;
	str.clear();
	int aux1;
	while (aux1 != EOF){
		aux1 = fgetc(arq);
		str.push_back(aux1);
		if(aux1 == ' '){
			fprintf(resposta, "or ");
			ors.push_back(str);
			for(unsigned int i = 0; i < str.size(); i++){
				fprintf(resposta, "%c", str.at(i));
			}
			str.clear();
			while (aux1 != '\n'){
				aux1 = fgetc(arq);
				if(aux1 != 32 && aux1 != 10){
					fprintf(resposta, "e%d ", aux1-47);
				}
			}
			fprintf(resposta, "\n");
		}
	}
	
	fprintf(resposta, "and and0 ");
	for(unsigned int i = 0; i < ors.size(); i++){
		for(unsigned int j = 0; j < ors.at(i).size(); j++){
			fprintf(resposta, "%c", ors.at(i).at(j));
		}
		fprintf(resposta, " ");
	}
	fprintf(resposta, "\nend\n\n\nend\n\n\n\n");
}