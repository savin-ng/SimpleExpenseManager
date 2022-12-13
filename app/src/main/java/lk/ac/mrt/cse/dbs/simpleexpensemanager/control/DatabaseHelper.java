package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Information
    static final String DB_Name = "200202B";
    static final int DB_Version = 1;

    // Columns for Account Table
    public static final String ACCT_TABLE = "Account_Table";
    public static final String ACCTNO = "accountNo";
    public static final String BANKNAME = "bankName";
    public static final String ACCTHOLDER = "accountHolderName";
    public static final String BAL = "balance";


    private static final String Create_Account_Table = "create table " + ACCT_TABLE + "(" + ACCTNO + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                                        + BANKNAME + " TEXT, " + ACCTHOLDER + " TEXT, " + BAL + " REAL);";

    // Columns for Transaction Table
    public static final String TRANSACT_TABLE = "Transaction_Table";
    public static final String TRANSACT_ID = "Transaction_ID";
    public static final String DATE = "date";
    public static final String EXPENSETYPE = "expenseType";
    public static final String AMOUNT = "amount";

    private static final String Create_Transaction_Table = "create table " + TRANSACT_TABLE + "(" + TRANSACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DATE + " TEXT, " + ACCTNO + " INTEGER, "
            + EXPENSETYPE + " TEXT, " + AMOUNT + " REAL);";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_Name, null, DB_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL(Create_Account_Table);
        DB.execSQL(Create_Transaction_Table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion) {
        DB.execSQL("DROP TABLE IF EXISTS " + ACCT_TABLE);
        DB.execSQL("DROP TABLE IF EXISTS " + TRANSACT_TABLE);
        onCreate(DB);
    }
}
