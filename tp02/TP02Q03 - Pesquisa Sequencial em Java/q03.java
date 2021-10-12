import java.io.RandomAccessFile;
import java.time.Instant;
import java.util.*;

public class q03 {
    public static void main(String[] args) {
        long programStartTime = Instant.now().toEpochMilli();
        Scanner scanner = new Scanner(System.in);
        int comparisons = 0;

        List<String> list = new ArrayList<String>();

        while (true) {
            String line = scanner.nextLine();

            if (line.equals("FIM"))
                break;
            else
                comparisons++;

            list.add(line.replaceAll("(.html)", "").replaceAll("_", " "));
        }

        while (true) {
            String line = scanner.nextLine();

            if (line.equals("FIM"))
                break;
            else {
                comparisons++;
            }

            boolean isEqualToLine = false;

            for (String seriesName : list) {
                if (seriesName.equals(line)) {
                    comparisons++;
                    isEqualToLine = true;
                    break;
                } else {
                    comparisons++;
                }
            }
            System.out.println(isEqualToLine ? "SIM" : "NÃO");
        }

        long totalRuntime = Instant.now().toEpochMilli() - programStartTime;

        try {
            RandomAccessFile log = new RandomAccessFile("matricula_sequencial.txt", "rw");
            log.writeChars(String.format("729414\t%d\t%d", totalRuntime / 1000, comparisons));
            log.close();
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
