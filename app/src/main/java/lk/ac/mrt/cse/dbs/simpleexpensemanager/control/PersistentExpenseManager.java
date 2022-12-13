package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

public class PersistentExpenseManager extends ExpenseManager {
    Context cxt;

    public PersistentExpenseManager(Context cxt) throws ExpenseManagerException {
        this.cxt = cxt;
        setup();
    }

    @Override
    public void setup() throws ExpenseManagerException {

        DatabaseHelper DBHelper = new DatabaseHelper(cxt);

        TransactionDAO persistentTransactionDAO = new PersistentTransactionDAO(DBHelper);
        setTransactionsDAO(persistentTransactionDAO);

        AccountDAO persistentAccountDAO = new PersistentAccountDAO(DBHelper);
        setAccountsDAO(persistentAccountDAO);

    }
}
