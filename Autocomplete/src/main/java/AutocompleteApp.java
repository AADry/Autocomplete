import parser.CSVParser;
import trie.Trie;

import java.io.File;
import java.util.*;

public class AutocompleteApp {
    public static void main(String[] args) throws Exception {
        int target = Integer.parseInt(args[0]);
        if(target < 1 || target > 13){
            System.out.println("Столбец не найден");
            System.exit(1);
        }
        File file = new File(AutocompleteApp.class.getClassLoader().getResource("airports.csv").getPath());
        if(!file.exists()) file = new File("airports.csv");
        if(!file.exists()) {
            System.out.println("Файл airports.csv не найден");
            System.exit(1);
        }
        CSVParser csvParser = new CSVParser(file);
        Trie trie = new Trie(6);
        ArrayList<String> col = csvParser.getColumn(target);
        ArrayList<String> rows;
        for (short i = 0; i < col.size(); i++) {
            trie.put(col.get(i).replaceAll("\"", ""), i);
        }
        col = null;
        System.gc();
        Scanner scanner = new Scanner(System.in);
        String in = "";
        while(!in.equals("!quit")){
            System.out.println("Введите строку: ");
            String[][] result = new String[0][0];
            boolean found;
            in = scanner.nextLine().toLowerCase();
            long start = System.nanoTime();
            ArrayList<Integer> indexes = trie.find(in);
            found = (indexes != null);
            if(found){
                rows = csvParser.getRows(indexes);
                result = CSVParser.sortRows(rows,target,in);
            }
            long end = System.nanoTime();
            int r = 0;
            for (int i = 0; i < result.length ; i++) {
                System.out.print(result[i][0] + " [");
                System.out.println(result[i][1] + "]");
                r++;
            }
            if(!in.equals("!quit")) {
                System.out.println("Найдено строк: " + r);
                System.out.println("Время, затраченное на поиск: " + (end - start) / 1000000.0 + "мс");
            }
        }

    }
}