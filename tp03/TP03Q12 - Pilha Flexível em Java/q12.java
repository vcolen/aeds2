import java.util.*;
import java.io.*;
import java.time.*;

class Serie {

    private String name;
    private String format;
    private String length;
    private String country;
    private String language;
    private String broadcaster;
    private String originalStream;
    private String numberOfSeasons;
    private String numberOfEpisodes;

    public Serie() {
        name = "";
        format = "";
        length = "";
        country = "";
        language = "";
        broadcaster = "";
        originalStream = "";
        numberOfSeasons = "";
        numberOfEpisodes = "";
    }

    public Serie(String name, String format, String length, String country, String language, String broadcaster,
            String originalStream, String numberOfSeasons, String numberOfEpisodes) {

        this.name = name;
        this.format = format;
        this.length = length;
        this.country = country;
        this.language = language;
        this.broadcaster = broadcaster;
        this.originalStream = originalStream;
        this.numberOfSeasons = numberOfSeasons;
        this.numberOfEpisodes = numberOfEpisodes;
    }

    public void printClass() {
        System.out.println(this.name + " " + this.format + " " + this.length + " " + this.country + " " + this.language
                + " " + this.broadcaster + " " + this.originalStream + " " + this.numberOfSeasons + " "
                + this.numberOfEpisodes);
    }

    public Serie clone() {
        Serie destination = new Serie();
        destination.name = this.name;
        destination.format = this.format;
        destination.length = this.length;
        destination.country = this.country;
        destination.language = this.language;
        destination.broadcaster = this.broadcaster;
        destination.originalStream = this.originalStream;
        destination.numberOfSeasons = this.numberOfSeasons;
        destination.numberOfEpisodes = this.numberOfEpisodes;
        return destination;
    }

    // MARK - Get properties

    public String getName() {
        return this.name;
    }

    public String getFormat() {
        return this.format;
    }

    public String getDuration() {
        return this.length;
    }

    public String getCountry() {
        return this.country;
    }

    public String getLanguage() {
        return this.language;
    }

    public String getBroadcaster() {
        return this.broadcaster;
    }

    public String getStreaming() {
        return this.originalStream;
    }

    public String getSeasons() {
        return this.numberOfSeasons;
    }

    public String getEpisodes() {
        return this.numberOfEpisodes;
    }

    // MARK - Set methods

    public void setName(String name) {
        this.name = name;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setDuration(String length) {
        this.length = length;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setBroadcaster(String broadcaster) {
        this.broadcaster = broadcaster;
    }

    public void setStreaming(String originalStream) {
        this.originalStream = originalStream;
    }

    public void setSeasons(String numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }

    public void setEpisodes(String numberOfEpisodes) {
        this.numberOfEpisodes = numberOfEpisodes;
    }
}

class Read {
    public static String justInt(String line) {
        String auxString = "";
        for (int i = 0; i < line.length(); i++) {
            // caso o caracter seja um número ele é concatenado a variável auxString
            if (line.charAt(i) >= '0' && line.charAt(i) <= '9') {
                auxString += line.charAt(i);
            } else {
                break;
            }
        }
        return auxString;
    }

    public static String removeTags(String line) {
        line = line.replaceAll("<.*?>", ""); // removing tags
        line = line.replaceAll("&.*?;", ""); // removing weird stuff
        line = line.trim();
        line = line.replaceAll("(lista de episódios)", "");
        line = line.replaceAll("(Lista de episódios)", "");
        line = line.replaceAll("(até o momento)", "");

        return line;
    }

    // retrieving the name of the series
    public static String searchName(String fileName) {
        return fileName.replaceAll("(.html)", "").replaceAll("_", " ");
    }

    public static Serie readClass(String fileName) throws IOException {

        String name;
        String format;
        String length;
        String country;
        String language;
        String broadcaster;
        String originalStream;
        String numberOfSeasons;
        String numberOfEpisodes;

        FileReader fileReader = new FileReader("/tmp/series/" + fileName);

        BufferedReader br = new BufferedReader(fileReader);

        name = searchName(fileName);

        while (!br.readLine().contains("Formato"))
            ;
        format = removeTags(br.readLine());

        while (!br.readLine().contains("Duração"))
            ;
        length = removeTags(br.readLine());

        while (!br.readLine().contains("País de origem"))
            ;
        country = removeTags(br.readLine());

        while (!br.readLine().contains("Idioma original"))
            ;
        language = removeTags(br.readLine());

        while (!br.readLine().contains("Emissora de televisão"))
            ;
        broadcaster = removeTags(br.readLine());

        while (!br.readLine().contains("Transmissão original"))
            ;
        originalStream = removeTags(br.readLine());

        while (!br.readLine().contains("N.º de temporadas"))
            ;
        numberOfSeasons = justInt(removeTags(br.readLine()));

        while (!br.readLine().contains("N.º de episódios"))
            ;
        numberOfEpisodes = justInt(removeTags(br.readLine()));

        br.close();
        return new Serie(name, format, length, country, language, broadcaster, originalStream, numberOfSeasons,
                numberOfEpisodes);

    }
}

class Celula {
    public Serie elemento; // Elemento inserido na celula.
    public Celula prox; // Aponta a celula prox.

    /**
     * Construtor da classe.
     */
    public Celula() {

    }

    /**
     * Construtor da classe.
     * 
     * @param elemento int inserido na celula.
     */
    public Celula(Serie elemento) {
        this.elemento = elemento;
        this.prox = null;
    }
}

 class Pilha {
    private Celula topo;

    /**
     * Construtor da classe que cria uma fila sem elementos.
     */
    public Pilha() {
        topo = null;
    }

    /**
     * Insere elemento na pilha (politica FILO).
     * 
     * @param x int elemento a inserir.
     */
    public void inserir(Serie x) {
        Celula tmp = new Celula(x);
        tmp.prox = topo;
        topo = tmp;
        tmp = null;
    }

    /**
     * Remove elemento da pilha (politica FILO).
     * 
     * @return Elemento removido.
     * @trhows Exception Se a sequencia nao contiver elementos.
     */
    public Serie remover() throws Exception {
        if (topo == null) {
            throw new Exception("Erro ao remover!");
        }
        Serie resp = topo.elemento;
        Celula tmp = topo;
        topo = topo.prox;
        tmp.prox = null;
        tmp = null;
        return resp;
    }

    /**
     * Mostra os elementos separados por espacos, comecando do topo.
     */
    public void mostrar() {
        for (Celula i = topo; i != null; i = i.prox)
            i.elemento.printClass();
    }

    public void mostraPilha() {
        mostraPilha(topo);
    }

    private void mostraPilha(Celula i) {
        if (i != null) {
            mostraPilha(i.prox);
            i.elemento.printClass();
        }
    }

}

public class q12 {
    public static void main(String[] args) {
        String line;
        Pilha series = new Pilha();
        Scanner sc = new Scanner(System.in);
        do {
            line = sc.nextLine();
            if (!line.equals("FIM")) {
                try {
                    series.inserir(Read.readClass(line));
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        } while (!line.equals("FIM"));

        int numOfComands = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < numOfComands; i++) {
            line = sc.nextLine();
            switch (line.substring(0, 1)) {
            case "I":
                try {
                    series.inserir(Read.readClass(line.substring(2)));
                } catch (Exception e) {
                    System.out.println(e);
                }
                break;

            case "R":
                try {
                   Serie aux = series.remover();
                   System.out.println("(R) " + aux.getName());
                } catch (Exception e) {
                    System.out.println(e);
                }
                break;
            }
        }

        series.mostrar();

    }
}
