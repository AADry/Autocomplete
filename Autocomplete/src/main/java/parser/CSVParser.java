package parser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
/**
 * {@code CSVParser} читает csv файл, составляя массив индексов,
 * чтобы в дальнейшем быстро читать определённые строки из файла
 *
 */
public class CSVParser {
    File csvFile;
    Scanner scanner;
    ArrayList<Integer> lineBytes = new ArrayList<>();
    public CSVParser(File csvFile) {
        this.csvFile = csvFile;
        try {
            scanner = new Scanner(getCsvFile());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        scanner.useDelimiter(",");
    }

    public File getCsvFile() {
        return csvFile;
    }

    public void setCsvFile(File csvFile) {
        this.csvFile = csvFile;
        try {
            scanner = new Scanner(getCsvFile());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        scanner.useDelimiter(",");
    }
    /**
     *  Собирает одну строку из файла с указанными в массиве индексами.
     */
    public String[] getRow(int index){
        index--;
        String row = null;
        String line = "";
        String splitBy = ",";
        String[] tempArr = null;
        try {
            RandomAccessFile raf = new RandomAccessFile(csvFile, "r");
            int p = 0;
            for (int i = 0; i < index; i++) {
                p += lineBytes.get(i);
            }
            raf.seek(p);
            line = raf.readLine();
            tempArr = line.trim().split(splitBy);
            raf.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return tempArr;
    }
    /**
     *  Собирает весь столбец из файла с указанным индексом,
     *  попутно заполняя индексный массив lineBytes.
     */
    public ArrayList<String> getColumn(int index){
        byte[] chunks;
        ArrayList<Integer> bytes = new ArrayList<>();
        ArrayList<String> column = new ArrayList<>();
        String line = "";
        int i = 0;
        String splitBy = ",";
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile)));
            while((line = bufferedReader.readLine()) != null) {
                chunks = line.getBytes(StandardCharsets.UTF_8);
                bytes.add(chunks.length+ 1);
                String[] sRow = line.split(splitBy);
                column.add(sRow[index]);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        lineBytes = bytes;
        return column;
    }
    /**
     *  Собирает все строки из файла с указанными в массиве индексами.
     *
     */
    public ArrayList<String> getRows(ArrayList<Integer> indexes){

        ArrayList<String> rows = new ArrayList<>();
        String line = "";
        try {
            RandomAccessFile raf = new RandomAccessFile(csvFile, "r");
            int p = 0;
            int t = 0;
            int bytesSum = 0;
            for (Integer index : indexes) {
                p = index;
                for (int j = t; j < p; j++) {
                    bytesSum += lineBytes.get(j);
                }
                raf.seek(bytesSum);
                line = raf.readLine();
                line = new String(line.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);;
                rows.add(line.trim());
                t = p;
            }
            raf.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return rows;
    }
    /**
     * Сортирует строки по выбранной колонке sortCol.
     * Сортирует как String, так и численные значения.
     * Результирующий массив содержит в первом столбце строку по которой выполнялась сортировка,
     * во втором столбце непосредственно сами строки
     */
    public static String[][] sortRows(ArrayList<String> rows,int sortCol, String query){
        String header = null;
        ArrayList<String> rowsFiltered = new ArrayList<>();
        for (String t : rows) {
            String[] s = t.split(",");
            header = s[sortCol].replaceAll("\"","").toLowerCase();
            if(header.startsWith(query)) rowsFiltered.add(t);
        }
        String[][] result = new String[rowsFiltered.size()][2];
        for (int i = 0; i < rowsFiltered.size(); i++) {
            String t = rowsFiltered.get(i);
            String[] s = t.split(",");
            header = s[sortCol];
            result[i][0] = header;
            result[i][1] = t;
        }
        Comparator<String> comparator = Comparator.comparing(String::toLowerCase);
        Arrays.sort(result, (a,b) -> comparator.compare(a[0],b[0]));
        return result;
    }
}
