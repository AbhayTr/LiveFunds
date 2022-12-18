package com.abhaytr.tools.livefunds;

import android.content.SharedPreferences;

public class FundsManager
{

    private final SharedPreferences memory;
    private final SharedPreferences.Editor memoryEditor;
    private int currentExpenses = 0;
    private int currentIncome = 0;
    private String currentStatus = "Cr";
    private final String expensesKey = "Expenses";
    private final String incomeKey = "Income";

    public FundsManager(SharedPreferences memory) throws Exception
    {
        this.memory = memory;
        this.memoryEditor = memory.edit();
        this.currentExpenses = getInt(this.expensesKey);
        this.currentIncome = getInt(this.incomeKey);
    }

    private int getInt(String key) throws Exception
    {
        if (!this.memory.contains(key))
        {
            if (!saveInt(key, 0))
            {
                throw new Exception("Memory Error.");
            }
        }
        return this.memory.getInt(key, 0);
    }

    private boolean saveInt(String key, int value)
    {
        try
        {
            this.memoryEditor.putInt(key, value);
            this.memoryEditor.commit();
            return true;
        }
        catch (Exception ex)
        {
            return false;
        }
    }

    public int getCurrentExpenses()
    {
        return this.currentExpenses;
    }

    public int getCurrentIncome()
    {
        return this.currentIncome;
    }

    public int addExpense(int expense) throws Exception
    {
        this.currentExpenses += expense;
        if (!saveInt(this.expensesKey, this.currentExpenses))
        {
            throw new Exception("Memory Error.");
        }
        return getCurrentFunds();
    }

    public int removeExpense(int expense) throws Exception
    {
        this.currentExpenses -= expense;
        if (!saveInt(this.expensesKey, this.currentExpenses))
        {
            throw new Exception("Memory Error.");
        }
        return getCurrentFunds();
    }

    public int addIncome(int income) throws Exception
    {
        this.currentIncome += income;
        if (!saveInt(this.incomeKey, this.currentIncome))
        {
            throw new Exception("Memory Error.");
        }
        return getCurrentFunds();
    }

    public int removeIncome(int income) throws Exception
    {
        this.currentIncome -= income;
        if (!saveInt(this.incomeKey, this.currentIncome))
        {
            throw new Exception("Memory Error.");
        }
        return getCurrentFunds();
    }

    public int resetExpenses() throws Exception
    {
        this.currentExpenses = 0;
        if (!saveInt(this.expensesKey, 0))
        {
            throw new Exception("Memory Error.");
        }
        return getCurrentFunds();
    }

    public int resetIncome() throws Exception
    {
        this.currentIncome = 0;
        if (!saveInt(this.incomeKey, 0))
        {
            throw new Exception("Memory Error.");
        }
        return getCurrentFunds();
    }

    public String getCurrentStatus()
    {
        return this.currentStatus;
    }

    public int getCurrentFunds()
    {
        int currentFunds = this.currentIncome - this.currentExpenses;
        if (currentFunds < 0)
        {
            this.currentStatus = "Dr";
        }
        else
        {
            this.currentStatus = "Cr";
        }
        return Math.abs(currentFunds);
    }

    public boolean isFirstRun()
    {
        if (this.memory.contains("Welcome"))
        {
            return false;
        }
        try
        {
            this.memoryEditor.putInt("Welcome", 1);
            this.memoryEditor.commit();
        }
        catch (Exception ex)
        {
            //No worries...
        }
        return true;
    }

}