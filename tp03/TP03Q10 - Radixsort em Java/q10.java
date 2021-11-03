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

class Sort {

    private static int numberOfComparisons = 0;

    public static void swap(Integer item1, Integer item2, List<Serie> list) {
        Serie aux = list.get(item1);
        list.set(item1, list.get(item2));
        list.set(item2, aux);
    }

    static int getMax(int arr[], int n) {
        int mx = arr[0];
        for (int i = 1; i < n; i++)
            if (arr[i] > mx)
                mx = arr[i];
        return mx;
    }

    static void countSort(int arr[], int n, int exp, Serie serieArr[]) {
        int output[] = new int[n]; 
        int i;
        int count[] = new int[10];
        Arrays.fill(count, 0);

        for (i = 0; i < n; i++)
            count[(arr[i] / exp) % 10]++;

        for (i = 1; i < 10; i++)
            count[i] += count[i - 1];

        for (i = n - 1; i >= 0; i--) {
            output[count[(arr[i] / exp) % 10] - 1] = arr[i];
            count[(arr[i] / exp) % 10]--;
        }

        for (i = 0; i < n; i++)
            arr[i] = output[i];
    }

    static void radixsort(Serie serieArr[], int numArr[], int n) {
        int m = getMax(numArr, n);

        for (int exp = 1; m / exp > 0; exp *= 10)
            countSort(numArr, n, exp, serieArr);
    }

    public static int getComparisons() {
        return numberOfComparisons;
    }
}

public class q10 {
    public static void main(String[] args) throws IOException {
        String line;
        List<Serie> seriesList = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        long programStartTime = Instant.now().toEpochMilli();

        do {
            line = sc.nextLine();
            if (!line.equals("FIM")) {
                seriesList.add(Read.readClass(line));
            }
        } while (!line.equals("FIM"));

        Serie[] seriesArr = new Serie[seriesList.size()];
        seriesArr = seriesList.toArray(seriesArr);

        // array para salvar os numeros doidoes
        int[] numArr = new int[seriesArr.length];

        Hashtable<Integer, Serie> serieDict = new Hashtable<Integer, Serie>();
        // salvando o numero doidao em um array
        for (int i = 0; i < seriesArr.length; i++) {
            numArr[i] = Integer.parseInt(seriesArr[i].getEpisodes()) * 1000
                    + Integer.parseInt(seriesArr[i].getSeasons());
            serieDict.put(numArr[i], seriesArr[i]);
        }

        Sort sort = new Sort();

        sort.radixsort(seriesArr, numArr, seriesArr.length);

        for(int i = 0; i < seriesArr.length; i++ ) {
            serieDict.get(numArr[i]).printClass();
        }
        
        sc.close();

        try {
            RandomAccessFile log = new RandomAccessFile("matrícula_radixsort.txt", "rw");
            long totalRuntime = Instant.now().toEpochMilli() - programStartTime;
            log.writeChars(String.format("729414\t%d\t%d", totalRuntime, Sort.getComparisons()));
            log.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
