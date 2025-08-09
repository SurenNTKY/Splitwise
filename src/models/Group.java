package models;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private final int id;
    private final String name;
    private final String type;
    private final User creator;
    private final List<User> members;
    private final List<Expense> expenses;

    public Group(int id, String name, String type, User creator) {
        this.id = id;
        this.name = name;
        this.type = type;
        if (creator == null) {
            throw new IllegalArgumentException("Creator cannot be null");
        }
        this.creator = creator;
        this.members = new ArrayList<>();
        this.expenses = new ArrayList<>();
        this.members.add(creator);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public User getCreator() {
        return creator;
    }

    public List<User> getMembers() {
        return members;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void addMember(User user) {
        this.members.add(user);
    }

    public void addExpense(Expense expense) {
        this.expenses.add(expense);
    }
}
