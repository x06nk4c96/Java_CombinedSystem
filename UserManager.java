import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

class UserManager {
    HashMap<String, User> users = new HashMap<>();

    public void addUser(String username, String password) {
        users.put(username, new User(username, password));
    }

    //註冊
    public boolean register(String username, String password) {
        if (users.containsKey(username)) {
            System.out.println("使用者已存在!");
            return false; //使用者已存在
        }

        String role = "user"; //預設角色為一般使用者
        if ("admin".equals(username)) {
            role = "admin"; //如果使用者名稱是admin，設定角色為管理員
        }

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("使用者名稱和密碼不能為空!");
            return false; //使用者名稱或密碼不能為空
        }

        users.put(username, new User(username, password, role));
        return true; //註冊成功
    }

    public boolean isLoggedIn() {
        return currentUser != null; //如果currentUser不為null，表示有使用者登入
    }

    public String getCurrentUserRole() {
        if (currentUser != null) {
            return currentUser.getRole(); //回傳當前使用者角色
        }
        return "尚未登入"; //沒有使用者登入
    }

    //登入
    User currentUser; //當前登入的使用者

    public boolean login(String username, String password) {
        User user = users.get(username);

        if (user == null) {
            System.out.println("使用者不存在");
            return false;
        }

        //密碼正確
        if (user.getPassword().equals(password)) {
            user.failedAttempts = 0;
            currentUser = user;

            currentUser.getAccountManager().loadFromFile(currentUser.getUsername()); //登入後載入該使用者的記帳資料
            
            return true;
        }

        //檢查是否被鎖
        if (user.isLocked) {
            long currentTime = System.currentTimeMillis();
            long diff = (currentTime - user.lockTime) / 1000;

            if (diff < 30) { //鎖30秒
                System.out.println("帳號已鎖定，請 " + (30 - diff) + "秒後再嘗試");
                return false;
            } else {
                user.isLocked = false;
                user.failedAttempts = 0;
                System.out.println("帳號已解鎖，請重新登入");
            }
        }

        //密碼錯誤
        user.failedAttempts ++;

        System.out.println("密碼錯誤! 次數: " + user.failedAttempts);

        if (user.failedAttempts >=3) {
            user.isLocked = true;
            user.lockTime = System.currentTimeMillis();
            System.out.println("錯誤次數過多，帳好已鎖定!");
        }

        return false;
    }

    public boolean isAccountLocked(String username) {
        User user = users.get(username);
        if (user == null) return false;
        return user.isLocked;
    }

    public AccountManager getCurrentAccountManager() {
        if (currentUser != null) {
            return currentUser.getAccountManager();
        }
        return null;
    }

    //登出
    public void logout() {
        if (currentUser != null) {
            currentUser.getAccountManager().saveToFile(currentUser.getUsername()); //登出前儲存資料
        }
        currentUser = null; //登出，清除當前使用者
    }

    //當前登入的使用者
    public String getCurrentUser() {
        if (currentUser != null) {
            return currentUser.getUsername(); //回傳當前使用者名稱
        }
        return "尚未登入"; //沒有使用者登入
    }

    //儲存使用者資料
    public void saveToFile() {
        try {
            FileWriter writer = new FileWriter("users.txt");

            for (User user : users.values()) {
                writer.write (
                    user.getUsername() + "," + 
                    user.getPassword() + "," + 
                    user.getRole() + "\n");
            }
            writer.close();
            System.out.println("使用者資料已儲存!");
        } catch (IOException e) {
            System.err.println("資料儲存失敗!");
        }
    }

    //讀取資料
    public void loadFromFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("users.txt"));
            String line;

            while ((line = reader.readLine()) != null) {
                String [] parts = line.split(",");

                String username = parts[0];
                String password = parts[1];
                String role = parts[2];

                if (parts.length <3) {
                    continue; //如果資料格式不正確，跳過該行
                }

                users.put(username, new User(username, password, role));
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("尚未有使用者資料");
        }
    }
}
