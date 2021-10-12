/*Class inspirada no trabalho de João Augusto dos Santos Silva, 2021 */
import java.util.*;
import java.io.*;

public class Serie {

    private String name;
    private String format;
    private String length;
    private String country;
    private String language;
    private String broadcaster;
    private String originalStream;
    private int numberOfSeasons;
    private int numberOfEpisodes;

    public Serie() {
        name = "";
        format = "";
        length = "";
        country = "";
        language = "";
        broadcaster = "";
        originalStream = "";
        numberOfSeasons = 0;
        numberOfEpisodes = 0;
    }

    public Serie(String name, String format, String length, String country, String language, String broadcaster,
            String originalStream, int numberOfSeasons, int numberOfEpisodes) {

        this.name = "";
        this.format = "";
        this.length = "";
        this.country = "";
        this.language = "";
        this.broadcaster = "";
        this.originalStream = "";
        this.numberOfSeasons = 0;
        this.numberOfEpisodes = 0;
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

    public int getSeasons() {
        return this.numberOfSeasons;
    }

    public int getEpisodes() {
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

    public void setSeasons(int numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }

    public void setEpisodes(int numberOfEpisodes) {
        this.numberOfEpisodes = numberOfEpisodes;
    }

    public int justInt(String line) {
        String auxString = "";
        for (int i = 0; i < line.length(); i++) {
            // caso o caracter seja um número ele é concatenado a variável auxString
            if (line.charAt(i) >= '0' && line.charAt(i) <= '9') {
                auxString += line.charAt(i);
            } else {
                break;
            }
        }
        return Integer.parseInt(auxString);
    }

    //MARK - Other methods

    public String removeTags(String line) {
       line = line.replaceAll("<.*?>", ""); //removing tags
       line = line.replaceAll("&.*?;", ""); //removing weird stuff 
       return line;
    }

    // retrieving the name of the series
    public String searchName(String fileName) {    
        return fileName.replaceAll("(.html)", "").replaceAll("_", " ");
    }

    public void readClass(String fileName) {
        String file = "./tmp/series/" + fileName;
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader br = new BufferedReader(fileReader);

            this.name = searchName(fileName);

            while (!br.readLine().contains("Formato"));
            this.format = removeTags(br.readLine());

            while (!br.readLine().contains("Duração"));
            this.length = removeTags(br.readLine());

            while (!br.readLine().contains("País de origem"));
            this.country = removeTags(br.readLine());

            while (!br.readLine().contains("Idioma original"));
            this.language = removeTags(br.readLine());

            while (!br.readLine().contains("Emissora de televisão"));
            this.broadcaster = removeTags(br.readLine());

            while (!br.readLine().contains("Transmissão original"));
            this.originalStream = removeTags(br.readLine());

            while (!br.readLine().contains("N.º de temporadas"));
            this.numberOfSeasons = justInt(removeTags(br.readLine()));

            while (!br.readLine().contains("N.º de episódios"));
            this.numberOfEpisodes = justInt(removeTags(br.readLine()));

            this.printClass();
            br.close();

        } catch (FileNotFoundException e) {
            System.out.println("Unable to open file '" + fileName + "'");
        } catch (IOException e) {
            System.out.println("Error reading file '" + fileName + "'");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Serie serie = new Serie();
        String filename;
        do {
            filename = scanner.nextLine();
            if (filename.equals("FIM")) {
                break;
            }
            serie.readClass(filename);
        }while(!filename.equals("FIM"));     
    }
}
