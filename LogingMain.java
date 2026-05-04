import java.time.LocalDate;
import java.util.Scanner;

public class LogingMain {
    public static void main(String[] args) {
        UserManager userManager = new UserManager();
        Scanner scanner = new Scanner(System.in);
        userManager.loadFromFile(); //從檔案載入使用者資料

        while (true) {
            System.out.println("請選擇操作: 1.註冊 2.登入 3.登出 4.記帳功能 5.退出");
            if (!scanner.hasNextInt()) {
                System.out.println("請輸入有效選項!");
                scanner.next(); //清除無效輸入
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine(); //清除換行符

            switch (choice) {
                case 1:
                    System.out.print("輸入使用者名稱: ");
                    String regUsername = scanner.nextLine();
                    System.out.print("輸入密碼: ");
                    String regPassword = scanner.nextLine();

                    String regPAsswordConfirm;

                    while (true) {
                        System.out.print("請再次確認密碼: ");
                        regPAsswordConfirm = scanner.nextLine();

                        if (regPassword.equals(regPAsswordConfirm)) {
                            break; //密碼確認成功，跳出迴圈
                        } else {
                            System.out.println("密碼不匹配，請重新輸入!");
                        }
                    }

                    if (userManager.register(regUsername, regPassword)) {
                        System.out.println("註冊成功! 請登入");
                    } else {
                        System.out.println("註冊失敗!");
                    }
                    userManager.saveToFile(); //註冊後儲存資料
                    break;

                case 2:
                    if (userManager.isLoggedIn()) {
                        System.out.println("已經有使用者登入，請先登出!");
                        break;
                    }

                    System.out.print("輸入使用者名稱: ");
                    String loginUsername = scanner.nextLine();

                    while (true) {
                        System.out.print("輸入密碼: ");
                        String loginPassword = scanner.nextLine();

                        boolean success = userManager.login(loginUsername, loginPassword);

                        if (success) {
                            System.out.println("登入成功! 歡迎 " + userManager.getCurrentUser());

                            if (userManager.getCurrentUserRole().equals("admin")) {
                                System.out.println("你為管理者");
                            }
                            break;
                        } else {
                            if (userManager.isAccountLocked(loginUsername)) {
                                break;
                            }

                            System.out.println("密碼錯誤，請重新輸入!");
                        }
                        System.out.println("輸入 q 可取消登入");
                        if (loginPassword.equals("q")) {
                            break;
                        }
                    }
                    break;

                case 3:
                    if (userManager.getCurrentUser().equals("尚未登入")) {
                        System.out.println("沒有使用者登入!");
                        break;
                    }
                    userManager.logout();
                    System.out.println("已登出!");
                    break;

                case 4:
                    if (!userManager.isLoggedIn()) {
                        System.out.println("請先登入!");
                        break;
                    }

                    //進入記帳系統
                    while (true) {
                        System.out.println("請選擇操作: 1.新增紀錄 2.查看紀錄 3.查詢總額 4.返回");

                        int sub = scanner.nextInt();
                        scanner.nextLine(); //清除換行符

                        switch (sub) {
                            case 1:
                                LocalDate date = null;

                                while (true) {
                                    System.out.print("輸入日期 (格式: YYYY-MM-DD): ");
                                    String dateStr = scanner.nextLine();

                                    try {
                                        date = LocalDate.parse(dateStr);
                                        break; //日期解析成功，跳出迴圈
                                    } catch (Exception e) {
                                        System.out.println("日期格式錯誤，請重新輸入!");
                                    }
                                }

                                System.out.print("金額: ");
                                double amount = scanner.nextDouble();
                                scanner.nextLine(); //清除換行符

                                System.out.print("類型 (income/expense): ");
                                String type = scanner.nextLine();

                                System.out.print("說明: ");
                                String description = scanner.nextLine();

                                userManager.getCurrentAccountManager().addTransaction(new Transaction(amount, type,"" ,description, date));
                                break;

                            case 2:
                                userManager.getCurrentAccountManager().showAll();
                                break;
                            
                            case 3:
                                System.out.println("總額: " + userManager.getCurrentAccountManager().getBalance());
                                break;

                            case 4:
                                userManager.getCurrentAccountManager().saveToFile(userManager.getCurrentUser()); //返回前儲存帳本資料
                                break;
                        }
                        if (sub == 4) break; //返回主選單
                    
                    }
                    break;

                case 5:
                    userManager.saveToFile(); //退出前儲存資料
                    System.out.println("退出程式!");
                    scanner.close();
                    return;

                default:
                    System.out.println("無效的選擇!");
            }
            System.out.println("-------------------------------------"); //分隔線，增加可讀性
        }
    }
}
