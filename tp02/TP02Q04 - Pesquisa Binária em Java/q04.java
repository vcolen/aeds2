import java.io.RandomAccessFile;
import java.time.Instant;
import java.util.*;

public class q04 {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        long programStartTime = Instant.now().toEpochMilli();
        int comparisons = 0;

        List<String> list = new ArrayList<String>();

        while (true) {
            String line = scanner.nextLine();

            if (line.equals("FIM")) {
                comparisons++;
                break;
            } else
                comparisons++;

            list.add(line.replaceAll("(.html)", "").replaceAll("_", " "));
        }

        while (true) {
            String line = scanner.nextLine();

            if (line.equals("FIM")) {
                comparisons++;
                break;
            } else
                comparisons++;

            boolean isEqualToLine = false;

            int higher = list.size() - 1;
            int lower = 0;

            while (true) {
                //Binary Search
                int pivot = (higher + lower) / 2;
                String serieName = list.get(pivot);

                int difference = line.compareTo(serieName);

                if (difference == 0) { //they are equal
                    comparisons++;
                    isEqualToLine = true;
                    break;
                } else if (pivot == lower) {
                    comparisons++;
                    if (lower - higher <= 1) {
                        comparisons++;
                        break;
                    }
                } else if (difference > 0) {
                    comparisons++;
                    lower = pivot + 1;
                } else
                    higher = pivot;
            }

            System.out.println(isEqualToLine ? "SIM" : "NÃO");
        }

        long totalRunTime = Instant.now().toEpochMilli() - programStartTime;
        RandomAccessFile log = new RandomAccessFile("matricula_binaria.txt", "rw");
        log.writeChars(String.format("729414\t%d\t%d", totalRunTime, comparisons));
        log.close();
    }
}
