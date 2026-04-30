class User {
    String username;
    String password;
    String role;

    int failedAttempts = 0;
    boolean isLocked = false;
    long lockTime = 0;

    AccountManager accountManager = new AccountManager(); //每個人一個帳本

    //預設user
    public User(String username, String password) {
        this(username, password, "user"); //預設角色為一般使用者
    }

    //可自訂角色(admin或user)
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}