package models;

import java.util.Map;

public class Expense {
    private final int groupId;
    private final String shareType;
    private final double totalExpense;
    private final Map<String, Double> userExpenses;
    private final String payer;

    public Expense(int groupId, String shareType, double totalExpense, Map<String, Double> userExpenses, String payer) {
        this.groupId = groupId;
        this.shareType = shareType;
        this.totalExpense = totalExpense;
        this.userExpenses = userExpenses;
        this.payer = payer;
    }

    public int getGroupId() {
        return groupId;
    }

    public String getShareType() {
        return shareType;
    }

    public double getTotalExpense() {
        return totalExpense;
    }

    public Map<String, Double> getUserExpenses() {
        return userExpenses;
    }

    public String getPayer() {
        return payer;
    }
}
