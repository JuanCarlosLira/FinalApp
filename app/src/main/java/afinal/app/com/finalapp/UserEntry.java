package afinal.app.com.finalapp;

import android.provider.BaseColumns;

public class UserEntry implements BaseColumns {
    public static final String TABLE_NAME     = "users";
    public static final String COLUMN_ID      = "id";
    public static final String COLUMN_USER    = "user";
    public static final String COLUMN_PSWD    = "password";

    private int id;
    private String user;
    private String password;

    public UserEntry() {
    }

    public UserEntry(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
