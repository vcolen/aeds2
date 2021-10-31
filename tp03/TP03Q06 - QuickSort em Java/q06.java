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

    public static List<Serie> selectionSort(List<Serie> list) {
        List<Serie> serieList = list;

        for (int i = 0; i < (serieList.size() - 1); i++) {
            int low = i;
            for (int j = (i + 1); j < serieList.size(); j++) {
                if (list.get(low).getLanguage().compareTo(list.get(j).getLanguage()) > 0) {
                    low = j;
                    numberOfComparisons++;
                } else if (list.get(low).getLanguage().compareTo(list.get(j).getLanguage()) == 0) {
                    if (list.get(low).getName().compareTo(list.get(j).getName()) > 0)
                        low = j;
                    numberOfComparisons++;
                }
            }
            swap(low, i, serieList);
        }
        return serieList;
    }

    public static List<Serie> insertionSort(List<Serie> list) {
        List<Serie> serieList = list;

        for (int j = 1; j < serieList.size(); j++) {
            Serie key = serieList.get(j);
            int i = j - 1;
            while ((i >= 0) && (serieList.get(i).getName().compareTo(serieList.get(j).getName()) > 0)) {
                numberOfComparisons++;
                serieList.set(i + 1, serieList.get(i));
                i--;
            }
            serieList.set(i + 1, key);
        }
        return serieList;
    }

    public List<Serie> QuickSort(List<Serie> list) {
        quicksort(0, list.size() - 1, list);
        return list;
    }

    private void quicksort(int left, int right, List<Serie> list) {
        int i = left, j = right;
        Serie pivot = list.get((right + left) / 2);
        while (i <= j) {
            while (list.get(i).getCountry().compareTo(pivot.getCountry()) < 0 ||  
            (list.get(i).getCountry().compareTo(pivot.getCountry()) == 0 && 
            list.get(i).getName().compareTo(pivot.getName()) < 0)) {
                numberOfComparisons++;
                i++;
            }

             while (list.get(j).getCountry().compareTo(pivot.getCountry()) > 0 ||  
            (list.get(j).getCountry().compareTo(pivot.getCountry()) == 0 && 
            list.get(j).getName().compareTo(pivot.getName()) > 0)) {
                j--;
                numberOfComparisons++;
            }

            if (i <= j) {
                numberOfComparisons++;
                swap(i, j, list);
                i++;
                j--;
            }
        }
        if (left < j) {
            numberOfComparisons++;
            quicksort(left, j, list);
        }

        if (i < right) {
            numberOfComparisons++;
            quicksort(i, right, list);
        }

    }

    public static int getComparisons() {
        return numberOfComparisons;
    }
}

public class q06 {
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

        Sort sort = new Sort();

        seriesList = sort.QuickSort(seriesList);

        for (Serie serie : seriesList)
            serie.printClass();

        sc.close();

        try {
            RandomAccessFile log = new RandomAccessFile("matrícula_quicksort.txt", "rw");
            long totalRuntime = Instant.now().toEpochMilli() - programStartTime;
            log.writeChars(String.format("729414\t%d\t%d", totalRuntime, Sort.getComparisons()));
            log.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
