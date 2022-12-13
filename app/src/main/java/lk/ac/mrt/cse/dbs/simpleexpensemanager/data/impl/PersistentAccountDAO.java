package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {

    private DatabaseHelper DBHelper;

    public PersistentAccountDAO(DatabaseHelper dbHelper) {
        this.DBHelper = dbHelper;
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNumberList = new ArrayList<>();
        SQLiteDatabase database = this.DBHelper.getReadableDatabase();
        String sql_command = "select accountNo from Account_Table";

        Cursor cursor = database.rawQuery(sql_command,null);
        while(cursor.moveToNext()){
            accountNumberList.add(cursor.getString(0));
        }
        cursor.close();
        database.close();
        return accountNumberList;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accountsList = new ArrayList<>();
        SQLiteDatabase database = this.DBHelper.getReadableDatabase();
        String[] columns = new String[]{"accountNo", "bankName", "accountHolderName", "balance"};
        Cursor cursor = database.query("Account_Table",columns,null,null,null,null,null);

        int n1 = cursor.getColumnIndex("accountNo");
        int n2 = cursor.getColumnIndex("bankName");
        int n3 = cursor.getColumnIndex("accountHolderName");
        int n4 = cursor.getColumnIndex("balance");

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Account newAcct = new Account(cursor.getString(n1), cursor.getString(n2), cursor.getString(n3), cursor.getDouble(n4));
            accountsList.add(newAcct);
        }
        cursor.close();
        database.close();
        return accountsList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase database = this.DBHelper.getReadableDatabase();
        String[] columns = new String[]{"accountNo", "bankName", "accountHolderName", "balance"};
        String[] args = { accountNo };
        Cursor cursor = database.query("Account_Table",columns,"accountNo",args,null,null,null);
        if (cursor.getCount() > 0) {
            int n1 = cursor.getColumnIndex("accountNo");
            int n2 = cursor.getColumnIndex("bankName");
            int n3 = cursor.getColumnIndex("accountHolderName");
            int n4 = cursor.getColumnIndex("balance");

            Account newAcct = new Account(cursor.getString(n1), cursor.getString(n2), cursor.getString(n3), cursor.getDouble(n4));
            cursor.close();
            database.close();
            return newAcct;
        }
        else {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase database = this.DBHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("accountNo", account.getAccountNo());
        cv.put("bankName", account.getBankName());
        cv.put("accountHolderName", account.getAccountHolderName());
        cv.put("balance", account.getBalance());
        database.insert("Account_Table", null, cv);
        database.close();
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase database = this.DBHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery("Select * from Account_Table where accountNo = ?", new String[] {accountNo});
        if (cursor.getCount() > 0) {
            database.delete("Account_Table", "accountNo=?", new String[]{accountNo});
        }
        else {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        cursor.close();
        database.close();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase database = this.DBHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery("Select * from Account_Table where accountNo = ?", new String[]{accountNo});
        if (cursor.getCount() > 0) {
            int n1 = cursor.getColumnIndex("balance");
            ContentValues cv = new ContentValues();
            cursor.moveToFirst();

            switch (expenseType) {
                case EXPENSE:
                    System.out.println(n1);
                    double newBal1 = cursor.getDouble(n1) - amount;
                    cv.put("balance", newBal1);
                    database.update("Account_Table", cv, "accountNo=?", new String[]{accountNo});
                    break;
                case INCOME:
                    double newBal2 = cursor.getDouble(n1) + amount;
                    cv.put("balance", newBal2);
                    database.update("Account_Table", cv, "accountNo=?", new String[]{accountNo});
                    break;
            }
        } else {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        cursor.close();
        database.close();
    }
}
