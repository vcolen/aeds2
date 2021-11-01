#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

#define MAX_FIELD_SIZE 100

typedef struct
{
    char nome[MAX_FIELD_SIZE];
    char formato[MAX_FIELD_SIZE];
    char duracao[MAX_FIELD_SIZE];
    char pais[MAX_FIELD_SIZE];
    char idioma[MAX_FIELD_SIZE];
    char emissora[MAX_FIELD_SIZE];
    char transmissao[MAX_FIELD_SIZE];
    int num_temporadas;
    int num_episodios;
} Serie;

char *remove_line_break(char *line)
{
    while (*line != '\r' && *line != '\n')
        line++;
    *line = '\0';
    return line;
}

char *freadline(char *line, int max_size, FILE *file)
{
    return remove_line_break(fgets(line, max_size, file));
}

char *readline(char *line, int max_size)
{
    return freadline(line, max_size, stdin);
}

void print_serie(Serie *serie)
{
    printf("%s %s %s %s %s %s %s %d %d\n",
           serie->nome,
           serie->formato,
           serie->duracao,
           serie->pais,
           serie->idioma,
           serie->emissora,
           serie->transmissao,
           serie->num_temporadas,
           serie->num_episodios);
}

// Retorna o tamanho em bytes de um arquivo.
long tam_arquivo(FILE *file)
{
    fseek(file, 0L, SEEK_END);
    long size = ftell(file);
    rewind(file);
    return size;
}

// Retorna todo o conteúdo do arquivo numa string.
char *ler_html(char filename[])
{
    FILE *file = fopen(filename, "r");
    if (!file)
    {
        fprintf(stderr, "Falha ao abrir arquivo %s\n", filename);
        exit(1);
    }
    long tam = tam_arquivo(file);
    char *html = (char *)malloc(sizeof(char) * (tam + 1));
    fread(html, 1, tam, file);
    fclose(file);
    html[tam] = '\0';
    return html;
}

// Utility function to swap values at two indices in an array
void swap(Serie list[], int i, int j)
{
    Serie temp = list[i];
    list[i] = list[j];
    list[j] = temp;
}

// Recursive function to perform selection sort on subarray
void selectionSort(Serie list[], int i, int n)
{
    int min = i;
    for (int j = i + 1; j < n; j++)
    {
        if (list[j].num_temporadas < list[min].num_temporadas || (list[j].num_temporadas == list[min].num_temporadas && strcmp(list[j].nome, list[min].nome) < 0))
        {
            min = j;
        }
    }

    swap(list, min, i);

    if (i + 1 < n)
    {
        selectionSort(list, i + 1, n);
    }
}

int getMaior(Serie *list, int n)
{
    int maior = list[0].num_temporadas;

    for (int i = 0; i < n; i++)
    {
        if (maior < list[i].num_temporadas)
        {
            maior = list[i].num_temporadas;
        }
    }
    return maior;
}

int countingsort(Serie *list, int n)
{
    //Array para contar o numero de ocorrencias de cada elemento
    int tamCount = getMaior(list, n) + 1;
    int count[tamCount];
    Serie ordenado[n];
    int comparisons = 0;

    //Inicializar cada posicao do array de contagem
    for (int i = 0; i < tamCount; count[i] = 0, i++)
        comparisons++;

    //Agora, o count[i] contem o numero de elemento iguais a i
    for (int i = 0; i < n; count[list[i].num_temporadas]++, i++)
        comparisons++;

    //Agora, o count[i] contem o numero de elemento menores ou iguais a i
    for (int i = 1; i < tamCount; count[i] += count[i - 1], i++)
        comparisons++;

    //Ordenando
    for (int i = n - 1; i >= 0; ordenado[count[list[i].num_temporadas] - 1] = list[i], count[list[i].num_temporadas]--, i--)
        comparisons++;

    //Copiando para o array original
    for (int i = 0; i < n; list[i] = ordenado[i], i++)
        comparisons++;

    selectionSort(list, 0, n);

    return comparisons;
}

/**
 * @brief Extrai os textos de uma tag html.
 * 
 * @param html Ponteiro para o caractere que abre a tag '<'.
 * @param texto Ponteiro para onde o texto deve ser colocado.
 * 
 * @return Ponteiro para o texto extraído.
 */
char *extrair_texto(char *html, char *texto)
{
    char *start = texto;
    int contagem = 0;
    while (*html != '\0')
    {
        if (*html == '<')
        {
            if (
                (*(html + 1) == 'p') ||
                (*(html + 1) == 'b' && *(html + 2) == 'r') ||
                (*(html + 1) == '/' && *(html + 2) == 'h' && *(html + 3) == '1') ||
                (*(html + 1) == '/' && *(html + 2) == 't' && *(html + 3) == 'h') ||
                (*(html + 1) == '/' && *(html + 2) == 't' && *(html + 3) == 'd'))
                break;
            else
                contagem++;
        }
        else if (*html == '>')
            contagem--;
        else if (contagem == 0 && *html != '"')
        {
            if (*html == '&')
                html = strchr(html, ';');
            else if (*html != '\r' && *html != '\n')
                *texto++ = *html;
        }
        html++;
    }
    *texto = '\0';
    return *start == ' ' ? start + 1 : start;
}

/**
 * @brief Lê o HTML da série e popula os campos da struct.
 * 
 * @param serie Struct Serie que vai receber os dados.
 * @param html String contendo todo o HTML do arquivo.
 */
void ler_serie(Serie *serie, char *html)
{
    char texto[MAX_FIELD_SIZE];

    char *ptr = strstr(html, "<h1");
    extrair_texto(ptr, texto);

    char *parenteses_ptr = strchr(texto, '(');
    if (parenteses_ptr != NULL)
        *(parenteses_ptr - 1) = '\0';

    strcpy(serie->nome, texto);

    ptr = strstr(ptr, "<table class=\"infobox_v2\"");

    ptr = strstr(ptr, "Formato");
    ptr = strstr(ptr, "<td");
    strcpy(serie->formato, extrair_texto(ptr, texto));

    ptr = strstr(ptr, "Duração");
    ptr = strstr(ptr, "<td");
    strcpy(serie->duracao, extrair_texto(ptr, texto));

    ptr = strstr(ptr, "País de origem");
    ptr = strstr(ptr, "<td");
    strcpy(serie->pais, extrair_texto(ptr, texto));

    ptr = strstr(ptr, "Idioma original");
    ptr = strstr(ptr, "<td");
    strcpy(serie->idioma, extrair_texto(ptr, texto));

    ptr = strstr(ptr, "Emissora de televisão original");
    ptr = strstr(ptr, "<td");
    strcpy(serie->emissora, extrair_texto(ptr, texto));

    ptr = strstr(ptr, "Transmissão original");
    ptr = strstr(ptr, "<td");
    strcpy(serie->transmissao, extrair_texto(ptr, texto));

    ptr = strstr(ptr, "N.º de temporadas");
    ptr = strstr(ptr, "<td");
    sscanf(extrair_texto(ptr, texto), "%d", &serie->num_temporadas);

    ptr = strstr(ptr, "N.º de episódios");
    ptr = strstr(ptr, "<td");
    sscanf(extrair_texto(ptr, texto), "%d", &serie->num_episodios);
}

#define MAX_LINE_SIZE 250
#define PREFIXO "/tmp/series/"
// #define PREFIXO "../entrada e saida/tp02/series/"

int isFim(char line[])
{
    return line[0] == 'F' && line[1] == 'I' && line[2] == 'M';
}

void createLog(float timeSpent, int comparisons)
{
    FILE *log_file = fopen("matricula_countingsort.txt", "w");
    fprintf(log_file, "729414\t%f\t%d ", timeSpent, comparisons);
}

int main()
{
    double time_spent = 0.0;
    clock_t begin = clock();

    Serie serie;
    Serie serieList[100];
    size_t tam_prefixo = strlen(PREFIXO);
    char line[MAX_LINE_SIZE];
    strcpy(line, PREFIXO);
    readline(line + tam_prefixo, MAX_LINE_SIZE);
    int i = 0;

    while (!isFim(line + tam_prefixo))
    {
        char *html = ler_html(line);
        ler_serie(&serie, html);
        free(html);
        serieList[i] = serie;
        i++;
        readline(line + tam_prefixo, MAX_LINE_SIZE);
    }

    int comparisons = countingsort(serieList, i);

    for (int j = 0; j < i; j++)
    {
        print_serie(&serieList[j]);
    }

    clock_t end = clock();
    time_spent += (double)(end - begin) / CLOCKS_PER_SEC;

    createLog(time_spent, comparisons);

    return EXIT_SUCCESS;
}
