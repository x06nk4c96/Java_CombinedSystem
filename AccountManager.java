import java.io.BufferedReader ;
import java.io.File ;
import java.io.FileNotFoundException ;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class AccountManager {
    ArrayList<Transaction> list = new ArrayList<>() ;

    //新增紀錄
    public void addTransaction(Transaction t) {
        list.add(t) ;
    }

    //計算特定類別的總額
    public double getCategoryExpense(String category){
        double categoryTotal = 0 ;
        for (Transaction t : list) {
            if (t.getCategory().equalsIgnoreCase(category) && t.getType().equals("expense")) {
                categoryTotal += t.getAmount();  //expense
            }
        }
        return categoryTotal ; 
    }

    //顯示所有紀錄
    public void showAll() {
        for (int i = 0; i < list.size(); i++) {
            System.out.println((i+1) + ". " + list.get(i).toString());
        }
    }

    //刪除紀錄
    public void deleteTransaction(int index) {
        if (index >=0 && index < list.size()) {
            list.remove(index);
            System.out.println("紀錄已刪除!");
        } else {
            System.out.println("無效的編號!");
        }
    }

    //計算總金額
    public double getBalance() {
        double balance = 0 ;
        for (Transaction t : list) {
            if (t.getType().equals("income")) {
                balance += t.getAmount();  //income
            } else {
                balance -= t.getAmount();  //expense
            }
        }
        return balance ;
    }

    //顯示分類排名
    public void showCategorySummary() {
        HashMap<String, Double> map = new HashMap<>();

        for (Transaction t : list) {
            if (t.getType().equals("expense")) {
                String cat = t.getCategory();
                double amount = t.getAmount();
                
                map.put(cat, map.getOrDefault(cat, 0.0) + amount);
        }
    }

        //將Map轉換為List
        List<Map.Entry<String, Double>> entries = new ArrayList<>(map.entrySet());

        //排序
        Collections.sort(entries, (a, b) -> Double.compare(b.getValue(), a.getValue()));

        //顯示結果
        System.out.println("=== 類別支出排名 ===");
        int rank = 1;
        for(Map.Entry<String, Double> entry : entries) {
            System.out.println(rank + ". " + entry.getKey() + " : " + entry.getValue());
            rank++;
        }
    }

    //查詢月支出
    public double getMonthlyExpense() {
        double total = 0;
        LocalDate now = LocalDate.now();

        for (Transaction t : list) {
            if (t.getType().equals("expense")) {
                if (t.getDate().getMonth() == now.getMonth() && t.getDate().getYear() == now.getYear()) {
                    total += t.getAmount();
                }
            }
        }
        return total;
    }

    //查詢月支出排行
    public void showMonthlyCategorySummary() {
        HashMap<String, Double> map = new HashMap<>();
        LocalDate now = LocalDate.now();

        for(Transaction t : list) {
            if (t.getType().equals("expense")) {
                if (t.getDate().getMonth() == now.getMonth() && t.getDate().getYear() == now.getYear()) {
                    String cat = t.getCategory();
                    double amount = t.getAmount();
                    
                    map.put(cat, map.getOrDefault(cat, 0.0) + amount);
                }
            }
        }

        List<Map.Entry<String, Double>> entries = new ArrayList<>(map.entrySet());

        Collections.sort(entries, (a, b) -> Double.compare(b.getValue(), a.getValue()));

        System.out.println("=== 本月分類支出排名 ===");
        int rank = 1;
        for(Map.Entry<String, Double> entry : entries) {
            System.out.println(rank + ". " + entry.getKey() + " : " + entry.getValue());
            rank++;
        }
    }

    //將紀錄寫入檔案
    public void saveToFile(String username) {
        try {
            File dir = new File ("data");
            if (!dir.exists()) {
                dir.mkdir(); //如果資料夾不存在，則創建
            }
        
            try (FileWriter writer = new FileWriter("data/" + username + ".txt")){
                for (Transaction t : list) {
                    writer.write(
                        t.getDate() + "," + 
                        t.getType() + "," + 
                        t.getCategory() + "," + 
                        t.getAmount() + "," + 
                        t.getDescription() + "\n"
                    );
                }
                System.out.println("資料已儲存!");
            }
        } catch (IOException e) {
            System.err.println("儲存失敗!");
            e.printStackTrace();
        }
    }

    //從檔案讀取紀錄
    public void loadFromFile(String username) {
        list.clear(); //載入前清空現有紀錄

        try {
            BufferedReader reader = new BufferedReader(new FileReader("data/" + username + ".txt"));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                LocalDate date = LocalDate.parse(parts[0]);
                String type = parts[1];
                String category = parts[2];
                double amount = Double.parseDouble(parts[3]);
                String description = parts[4];

                Transaction t = new Transaction(amount, type, category, description, date);
                list.add(t);
            }
            reader.close();
            System.out.println("資料已載入!");
        } catch (FileNotFoundException e) {
            System.out.println("尚無記帳資料");
        } catch (IOException e) {
            System.err.println("載入失敗!");
            e.printStackTrace();
        }
    }
}