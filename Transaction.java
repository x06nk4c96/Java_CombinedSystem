import java.time.LocalDate;

class Transaction {
    double amount ;
    String type;
    String category;  // 新增項目
    String description;
    LocalDate date;   // 記錄日期

    public Transaction(double amount, String type, String category, String description, LocalDate date) {
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.description = description;
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "[" + date + "] " + type + " | " + category + amount + " (" + description + ")";
    }
}
