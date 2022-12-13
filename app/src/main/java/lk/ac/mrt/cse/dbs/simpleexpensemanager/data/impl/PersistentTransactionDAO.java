package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {

    private DatabaseHelper DBHelper;

    public PersistentTransactionDAO(DatabaseHelper dbHelper) {
        this.DBHelper = dbHelper;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase database = this.DBHelper.getWritableDatabase();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        String expType = null;
        switch (expenseType) {
            case EXPENSE:
                expType = ExpenseType.EXPENSE.name();
                break;
            case INCOME:
                expType = ExpenseType.INCOME.name();
                break;
        }

        ContentValues cv = new ContentValues();
        cv.put("date", formatter.format(date));
        cv.put("accountNo", accountNo);
        cv.put("expenseType", expType);
        cv.put("amount", amount);
        database.insert("Transaction_Table", null, cv);
        database.close();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactionsList = new ArrayList<>();
        SQLiteDatabase database = this.DBHelper.getReadableDatabase();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        String[] columns = new String[]{"date", "accountNo", "expenseType", "amount"};
        Cursor cursor = database.query("Transaction_Table",columns,null,null,null,null,null);

        int n1 = cursor.getColumnIndex("date");
        int n2 = cursor.getColumnIndex("accountNo");
        int n3 = cursor.getColumnIndex("expenseType");
        int n4 = cursor.getColumnIndex("amount");

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Date newDate = null;
            try {
                newDate = formatter.parse(cursor.getString(n1));
            } catch (ParseException e) {e.printStackTrace();}

            ExpenseType expType = ExpenseType.valueOf(cursor.getString(n3));

            Transaction newTransact = new Transaction(newDate, cursor.getString(n2), expType, cursor.getDouble(n4));
            transactionsList.add(newTransact);
        }
        cursor.close();
        database.close();
        return transactionsList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactionsList = this.getAllTransactionLogs();
        int size = transactionsList.size();
        if (size <= limit) {
            return transactionsList;
        }

        return transactionsList.subList(size - limit, size);
    }
}
