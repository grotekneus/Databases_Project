package be.kuleuven.dbproject;

public class User {
    private String passWord;
    private String userName;
    private boolean admin;

    public User(String passWord, String userName, boolean admin) {
        this.passWord = passWord;
        this.userName = userName;
        this.admin = admin;
    }

    public String getPassWord() {
        return passWord;
    }

    public String getUserName() {
        return userName;
    }

    public boolean isAdmin() {
        return admin;
    }
}
