class BankAccount {
    private String iban;
    private String ownerName;
    protected double balance;

    public BankAccount(String iban, String ownerName, double initialBalance) {
        this.iban = iban;
        this.ownerName = ownerName;
        this.balance = Math.max(0.0, initialBalance);
    }
    public String getIban() {
        return iban;
    }
    public String getOwnerName() {
        return ownerName;
    }
    public double getBalance() {
        return balance;
    }
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }
    //домианос пицца вкусно
    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        }
        System.out.printf("-> Спроба зняти %.2f грн: ВІДМОВА (недостатньо коштів на балансі: %.2f грн)\n", amount, balance);
        return false;
    }

    @Override
    public String toString() {
        return String.format("Рахунок[%s | Власник: %s | Баланс: %.2f грн]", iban, ownerName, balance);
    }
}
class SavingsAccount extends BankAccount {
    private double interestRate;
    private double minBalance;

    public SavingsAccount(String iban, String ownerName, double initialBalance, double interestRate, double minBalance) {
        super(iban, ownerName, initialBalance);
        this.interestRate = interestRate;
        this.minBalance = minBalance;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public double getMinBalance() {
        return minBalance;
    }

    @Override
    public boolean withdraw(double amount) {
        if (balance - amount >= minBalance) {
            return super.withdraw(amount);
        }
        System.out.printf("-> Спроба зняти %.2f грн: ВІДМОВА (порушення незнижуваного залишку %.2f грн)\n", amount, minBalance);
        return false;
    }

    public void applyInterest() {
        double interest = balance * (interestRate / 100.0);
        deposit(interest);
    }

    @Override
    public String toString() {
        return String.format("SavingsAccount{%s, Ставка: %.1f%%, Мін.залишок: %.2f грн}",
                super.toString(), interestRate, minBalance);
    }
}
class CreditAccount extends BankAccount {
    private double creditLimit;
    private double feePercent;

    public CreditAccount(String iban, String ownerName, double initialBalance, double creditLimit, double feePercent) {
        super(iban, ownerName, initialBalance);
        this.creditLimit = creditLimit;
        this.feePercent = feePercent;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public double getFeePercent() {
        return feePercent;
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) return false;

        if (balance >= amount) {
            return super.withdraw(amount);
        }

        double borrowed = amount - Math.max(0.0, balance);
        double fee = borrowed * (feePercent / 100.0);
        double totalDeduction = amount + fee;

        if (balance - totalDeduction >= -creditLimit) {
            balance -= totalDeduction;
            return true;
        }

        System.out.printf("-> Спроба зняти %.2f грн: ВІДМОВА (перевищено кредитний ліміт %.2f грн)\n", amount, creditLimit);
        return false;
    }

    public double getCurrentDebt() {
        return balance < 0 ? Math.abs(balance) : 0.0;
    }

    @Override
    public String toString() {
        return String.format("CreditAccount{%s, Ліміт: %.2f грн, Борг: %.2f грн}",
                super.toString(), creditLimit, getCurrentDebt());
    }
}

public class BankingHierarchy {
    public static void main(String[] args) {
        System.out.println("================ БАНКІВСЬКА ІЄРАРХІЯ РАХУНКІВ ================");

        System.out.println("1. Ощадний рахунок (початковий стан):");
        SavingsAccount savings = new SavingsAccount("UA101", "Олена Коваль", 5000.0, 12.0, 1000.0);
        System.out.println(savings);

        savings.withdraw(4500.0);

        System.out.println("-> Нарахування річних відсотків (+12%)...");
        savings.applyInterest();
        System.out.println(savings);
        System.out.println();

        System.out.println("2. Кредитний рахунок (початковий стан):");
        CreditAccount credit = new CreditAccount("UA202", "Максим Бондар", 2000.0, 10000.0, 2.0);
        System.out.println(credit);

        System.out.println("-> Покупка на 5000 грн (3000 грн у кредит з комісією 2% = 60 грн)... УСПІШНО!");
        credit.withdraw(5000.0);
        System.out.println(credit);

        System.out.println("==============================================================");
    }
}
