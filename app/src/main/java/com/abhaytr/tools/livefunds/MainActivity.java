package com.abhaytr.tools.livefunds;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.animation.ValueAnimator;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import com.abhaytr.jbeauty.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity
{

    final String storageError = "Something went wrong! Please check your storage or try again after some time. If the issue persists, then kindly contact Mr. Abhay Tripathi by writing to him at 'abhay.triipathi@gmail.com'.";
    final String nonIntError = "Amount can be numeric only!";
    final String nonNegativeError = "Amount has to be greater than 0!";
    final String debt = "#ff7f7f";
    final String credit = "#90EE90";
    final String BG_COLOR = "#212529";
    final String GUIDE = "The functions of the app have been explained below:<br><br><b>'Add New Expense'</b> button will add the amount entered in the text box to your expenses and will update your net funds accordingly.<br><br><b>'Remove Expense'</b> button will remove the amount entered in the text box from your expenses and will update your net funds accordingly.<br><br><b>'Add New Income'</b> button will add the amount entered in the text box to your income and will update your net funds accordingly.<br><br><b>'Remove Income'</b> button will remove the amount entered in the text box from your income and will update your net funds accordingly.<br><br><b>'Reset Expenses'</b> button will reset your expenses to 0 and will update your net funds accordingly.<br><br><b>'Reset Income'</b> button will reset your income to 0 and will update your net funds accordingly.<br><br>Your <b>'Net Funds'</b> are basically calculated as <b>'Your Income - Your Expenses'</b>. If the calculated difference is positive that means you have saved money and hence your net funds will be suffixed with 'Cr', while for the other case, it means you are in debt and hence will be suffixed with 'Dr' (according to Banking Standards).";
    final int DURATION = 1250;
    ImageView menuButton;
    TextView h1;
    TextView h2;
    View h3;
    TextView time;
    TextView income;
    TextView expenses;
    TextView netFunds;
    EditText amountInput;
    TextView addExpenseButton;
    TextView removeExpenseButton;
    TextView addIncomeButton;
    TextView removeIncomeButton;
    TextView resetExpensesButton;
    TextView resetIncomeButton;
    SharedPreferences memory;
    FundsManager fundsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);
        try
        {
            init();
        }
        catch (Exception ex)
        {
            print(storageError);
        }
        setNumber(expenses, 0, fundsManager.getCurrentExpenses());
        setNumber(income, 0, fundsManager.getCurrentIncome());
        setNumber(netFunds, 0, fundsManager.getCurrentFunds());
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("mode", "dark");
        Beauty.start(parameters, rgb_color -> {
            int r = rgb_color[0];
            int g = rgb_color[1];
            int b = rgb_color[2];
            int rgb = Color.rgb(r, g, b);
            runOnUiThread(() -> {
                h1.setBackgroundColor(rgb);
                h2.setBackgroundColor(rgb);
                h3.setBackgroundColor(rgb);
                getWindow().setNavigationBarColor(rgb);
                getWindow().setStatusBarColor(rgb);
            });
        });
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> runOnUiThread(() -> time.setText(getTime())), 0, 1, TimeUnit.SECONDS);
        addExpenseButton.setOnClickListener(v -> {
            int amount;
            try
            {
                amount = Integer.parseInt(getAmount());
                if (amount <= 0)
                {
                    print(nonNegativeError);
                }
                else
                {
                    try
                    {
                        int currentFunds = fundsManager.addExpense(amount);
                        setNumber(expenses, fundsManager.getCurrentExpenses());
                        setNumber(netFunds, currentFunds);
                    }
                    catch (Exception ex)
                    {
                        print(ex.getMessage());
                    }
                }
            }
            catch (NumberFormatException ex)
            {
                print(nonIntError);
            }
        });
        removeExpenseButton.setOnClickListener(v -> {
            int amount;
            try
            {
                amount = Integer.parseInt(getAmount());
                if (amount <= 0)
                {
                    print(nonNegativeError);
                }
                else
                {
                    try
                    {
                        if (fundsManager.getCurrentExpenses() - amount >= 0)
                        {
                            int currentFunds = fundsManager.removeExpense(amount);
                            setNumber(expenses, fundsManager.getCurrentExpenses());
                            setNumber(netFunds, currentFunds);
                        }
                        else
                        {
                            print("Expenses amount can't be negative!");
                        }
                    }
                    catch (Exception ex)
                    {
                        print(storageError);
                    }
                }
            }
            catch (NumberFormatException ex)
            {
                print(nonIntError);
            }
        });
        addIncomeButton.setOnClickListener(v -> {
            int amount;
            try
            {
                amount = Integer.parseInt(getAmount());
                if (amount <= 0)
                {
                    print(nonNegativeError);
                }
                else
                {
                    try
                    {
                        int currentFunds = fundsManager.addIncome(amount);
                        setNumber(income, fundsManager.getCurrentIncome());
                        setNumber(netFunds, currentFunds);
                    }
                    catch (Exception ex)
                    {
                        print(storageError);
                    }
                }
            }
            catch (NumberFormatException ex)
            {
                print(nonIntError);
            }
        });
        removeIncomeButton.setOnClickListener(v -> {
            int amount;
            try
            {
                amount = Integer.parseInt(getAmount());
                if (amount <= 0)
                {
                    print(nonNegativeError);
                }
                else
                {
                    try
                    {
                        if (fundsManager.getCurrentIncome() - amount >= 0)
                        {
                            int currentFunds = fundsManager.removeIncome(amount);
                            setNumber(income, fundsManager.getCurrentIncome());
                            setNumber(netFunds, currentFunds);
                        }
                        else
                        {
                            print("Income amount can't be negative!");
                        }
                    }
                    catch (Exception ex)
                    {
                        print(storageError);
                    }
                }
            }
            catch (NumberFormatException ex)
            {
                print(nonIntError);
            }
        });
        resetIncomeButton.setOnClickListener(v -> {
            try
            {
                final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle(Html.fromHtml("<font color='#FFFFFF'>Reset Income</font>"));
                alert.setMessage(Html.fromHtml("<font color='#FFFFFF'>Are you sure you want to reset your income to 0?</font>"));
                alert.setCancelable(false);
                alert.setPositiveButton("OK", (dialog, whichButton) -> {
                    try
                    {
                        int currentFunds = fundsManager.resetIncome();
                        setNumber(income, fundsManager.getCurrentIncome());
                        setNumber(netFunds, currentFunds);
                    }
                    catch (Exception ex)
                    {
                        print(storageError);
                    }
                });
                alert.setNegativeButton("Cancel", (dialog, whichButton) -> dialog.dismiss());
                AlertDialog dialog = alert.create();
                dialog.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor(BG_COLOR)));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
            }
            catch (Exception ex)
            {
                print(storageError);
            }
        });
        resetExpensesButton.setOnClickListener(v -> {
            try
            {
                final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle(Html.fromHtml("<font color='#FFFFFF'>Reset Expenses</font>"));
                alert.setMessage(Html.fromHtml("<font color='#FFFFFF'>Are you sure you want to reset your expenses to 0?</font>"));
                alert.setCancelable(false);
                alert.setPositiveButton("OK", (dialog, whichButton) -> {
                    try
                    {
                        int currentFunds = fundsManager.resetExpenses();
                        setNumber(expenses, fundsManager.getCurrentExpenses());
                        setNumber(netFunds, currentFunds);
                    }
                    catch (Exception ex)
                    {
                        print(storageError);
                    }
                });
                alert.setNegativeButton("Cancel", (dialog, whichButton) -> dialog.dismiss());
                AlertDialog dialog = alert.create();
                dialog.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor(BG_COLOR)));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
            }
            catch (Exception ex)
            {
                print(storageError);
            }
        });
        if (fundsManager.isFirstRun())
        {
            welcome();
        }
    }

    private void init() throws Exception
    {
        menuButton = findViewById(R.id.menuButton);
        h1 = findViewById(R.id.h1);
        h2 = findViewById(R.id.h2);
        h3 = findViewById(R.id.h3);
        time = findViewById(R.id.time);
        income = findViewById(R.id.income);
        expenses = findViewById(R.id.expenses);
        netFunds = findViewById(R.id.netFunds);
        amountInput = findViewById(R.id.amountInput);
        addExpenseButton = findViewById(R.id.addExpenseButton);
        removeExpenseButton = findViewById(R.id.removeExpenseButton);
        addIncomeButton = findViewById(R.id.addIncomeButton);
        removeIncomeButton = findViewById(R.id.removeIncomeButton);
        resetExpensesButton = findViewById(R.id.resetExpensesButton);
        resetIncomeButton = findViewById(R.id.resetIncomeButton);
        income.setMovementMethod(new ScrollingMovementMethod());
        income.setHorizontallyScrolling(true);
        expenses.setMovementMethod(new ScrollingMovementMethod());
        expenses.setHorizontallyScrolling(true);
        netFunds.setMovementMethod(new ScrollingMovementMethod());
        netFunds.setHorizontallyScrolling(true);
        memory = getApplicationContext().getSharedPreferences("Funds Data", 0);
        fundsManager = new FundsManager(memory);
        PopupMenu popupx = new PopupMenu(MainActivity.this, menuButton);
        popupx.getMenuInflater().inflate(R.menu.popup_menu, popupx.getMenu());
        popupx.setOnMenuItemClickListener(item -> {
            String option = (String) item.getTitle();
            if (option.equals("Help"))
            {
                help();
            }
            else if (option.equals("About"))
            {
                about();
            }
            else if (option.equals("Exit"))
            {
                final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle(Html.fromHtml("<font color='#FFFFFF'>Exit</font>"));
                alert.setMessage(Html.fromHtml("<font color='#FFFFFF'>Do you want to close the app?</font>"));
                alert.setCancelable(false);
                alert.setPositiveButton("OK", (dialog, whichButton) -> finishAffinity());
                alert.setNegativeButton("Cancel", (dialog, whichButton) -> dialog.dismiss());
                AlertDialog dialog = alert.create();
                dialog.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor(BG_COLOR)));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
            }
            return true;
        });
        menuButton.setOnClickListener(view -> {
            popupx.show();
        });
    }

    private String getAmount()
    {
        String amountString = amountInput.getText().toString();
        amountInput.setText("");
        return amountString;
    }

    private void print(String message)
    {
        runOnUiThread(() -> {
            AlertDialog.Builder alertgt = new AlertDialog.Builder(MainActivity.this);
            alertgt.setMessage(Html.fromHtml("<font color='#FFFFFF'>" + message + "</font>"));
            alertgt.setPositiveButton("OK", (dialogry, whichButton) -> dialogry.dismiss());
            final AlertDialog dialogxgu = alertgt.create();
            dialogxgu.show();
            dialogxgu.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor(BG_COLOR)));
            dialogxgu.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);
        });
    }

    private void welcome()
    {
        runOnUiThread(() -> {
            AlertDialog.Builder alertgt = new AlertDialog.Builder(MainActivity.this);
            alertgt.setTitle(Html.fromHtml("<font color='#FFFFFF'>Welcome to LiveFunds App</font>"));
            alertgt.setMessage(Html.fromHtml("<font color='#FFFFFF'>Thank you for installing LiveFunds App. To know about the purpose of the app in detail, you can click on 'About' in the 3-dot menu. " + GUIDE + "</font>"));
            alertgt.setPositiveButton("OK", (dialogry, whichButton) -> dialogry.dismiss());
            final AlertDialog dialogxgu = alertgt.create();
            dialogxgu.show();
            dialogxgu.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor(BG_COLOR)));
            dialogxgu.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);
        });
    }

    private void help()
    {
        runOnUiThread(() -> {
            AlertDialog.Builder alertgt = new AlertDialog.Builder(MainActivity.this);
            alertgt.setTitle(Html.fromHtml("<font color='#FFFFFF'>Help</font>"));
            alertgt.setMessage(Html.fromHtml("<font color='#FFFFFF'>" + GUIDE + "</font>"));
            alertgt.setPositiveButton("OK", (dialogry, whichButton) -> dialogry.dismiss());
            final AlertDialog dialogxgu = alertgt.create();
            dialogxgu.show();
            dialogxgu.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor(BG_COLOR)));
            dialogxgu.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);
        });
    }

    private void about()
    {
        String about_us = "LiveFunds App is an app which can be used for fast and easy budgeting i.e. can help very busy people, like students and employees to keep track of their income and expenses on a day to day basis via easy to use app experience. To get help on how to use the app, kindly press 'Help' button present in the 3-dot menu.<br><br>App Version: v1.0.0<br><br><b>&copy; Abhay Tripathi</b>";
        runOnUiThread(() -> {
            AlertDialog.Builder alertgt = new AlertDialog.Builder(MainActivity.this);
            alertgt.setTitle(Html.fromHtml("<font color='#FFFFFF'>About</font>"));
            alertgt.setMessage(Html.fromHtml("<font color='#FFFFFF'>" + about_us + "</font>"));
            alertgt.setPositiveButton("OK", (dialogry, whichButton) -> dialogry.dismiss());
            final AlertDialog dialogxgu = alertgt.create();
            dialogxgu.show();
            dialogxgu.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor(BG_COLOR)));
            dialogxgu.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);
        });
    }

    private void setNumber(TextView tv, int startNum, int endNum)
    {
        income.scrollTo(0, 0);
        expenses.scrollTo(0, 0);
        netFunds.scrollTo(0, 0);
        ValueAnimator animator = ValueAnimator.ofInt(startNum, endNum);
        animator.setDuration(DURATION);
        animator.addUpdateListener(animation -> {
            if (tv != netFunds)
            {
                tv.setText("₹" + animation.getAnimatedValue().toString());
            }
            else
            {
                String currentStatus = fundsManager.getCurrentStatus();
                String fundsColor = credit;
                if (currentStatus.equals("Dr"))
                {
                    fundsColor = debt;
                }
                tv.setTextColor(Color.parseColor(fundsColor));
                tv.setText("₹" + animation.getAnimatedValue().toString() + " " + currentStatus);
            }
        });
        animator.start();
    }

    private void setNumber(TextView tv, int newNum)
    {
        income.scrollTo(0, 0);
        expenses.scrollTo(0, 0);
        netFunds.scrollTo(0, 0);
        String currentValue = tv.getText().toString();
        String firstValue = currentValue.split(" ")[0];
        ValueAnimator animator = ValueAnimator.ofInt(Integer.parseInt(firstValue.substring(1)), newNum);
        animator.setDuration(DURATION);
        animator.addUpdateListener(animation -> {
            if (tv != netFunds)
            {
                tv.setText("₹" + animation.getAnimatedValue().toString());
            }
            else
            {
                String currentStatus = fundsManager.getCurrentStatus();
                String fundsColor = credit;
                if (currentStatus.equals("Dr"))
                {
                    fundsColor = debt;
                }
                tv.setTextColor(Color.parseColor(fundsColor));
                tv.setText("₹" + animation.getAnimatedValue().toString() + " " + currentStatus);
            }
        });
        animator.start();
    }

    private String getTime()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM dd, yyyy HH:mm:ss a");
        return sdf.format(new Date());
    }

}