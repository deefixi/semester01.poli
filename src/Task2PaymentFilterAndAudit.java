import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;
//кстаті я люблю лілії
abstract class Payment {
    private final String id;
    private final String shopName;
    private final double amount;

    public Payment(String id, String shopName, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Сума платежу має бути більшою за 0");
        }
        this.id = id;
        this.shopName = shopName;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public String getShopName() {
        return shopName;
    }

    public double getAmount() {
        return amount;
    }

    public abstract double calculateFee();

    public double getNetAmount() {
        return amount - calculateFee();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payment)) return false;
        Payment other = (Payment) o;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

class CardPayment extends Payment {

    public CardPayment(String id, String shopName, double amount) {
        super(id, shopName, amount);
    }
//якщо чесно я хочу їсти и пити
    @Override
    public double calculateFee() {
        return getAmount() * 0.015;
    }
}

class TransferPayment extends Payment {

    public TransferPayment(String id, String shopName, double amount) {
        super(id, shopName, amount);
    }

    @Override
    public double calculateFee() {
        return getAmount() * 0.02 + 20.0;
    }
}

class PaymentProcessor {
    private ArrayList<Payment> payments = new ArrayList<>();
    private HashSet<String> blockedShops;
    private double maxLimit;

    public PaymentProcessor(HashSet<String> blockedShops, double maxLimit) {
        this.blockedShops = new HashSet<>(blockedShops);
        this.maxLimit = maxLimit;
    }

    public void addPayment(Payment p) {
        if (p != null) {
            payments.add(p);
        }
    }

    public HashSet<String> getBlockedShopsCopy() {
        return new HashSet<>(blockedShops);
    }

    public ArrayList<Payment> getPaymentsCopy() {
        return new ArrayList<>(payments);
    }

    public double getMaxLimit() {
        return maxLimit;
    }

    public int cleanPayments() {
        int removed = 0;
        HashSet<String> seenIds = new HashSet<>();
        Iterator<Payment> it = payments.iterator();
        while (it.hasNext()) {
            Payment p = it.next();
            if (!seenIds.add(p.getId())) {
                System.out.println(String.format(Locale.US,
                        "  ❌ [ДУБЛІКАТ]      %s | %s (%.2f грн) — видалено!",
                        p.getId(), p.getShopName(), p.getAmount()));
                it.remove();
                removed++;
            } else if (blockedShops.contains(p.getShopName())) {
                System.out.println(String.format(Locale.US,
                        "  ❌ [ЧОРНИЙ СПИСОК] %s | %s (%.2f грн) — видалено!",
                        p.getId(), p.getShopName(), p.getAmount()));
                it.remove();
                removed++;
            } else if (p.getAmount() > maxLimit) {
                System.out.println(String.format(Locale.US,
                        "  ❌ [ПОНАД ЛІМІТ]   %s | %s (%.2f грн) — видалено!",
                        p.getId(), p.getShopName(), p.getAmount()));
                it.remove();
                removed++;
            }
        }
        return removed;
    }
//спустя 15 мінутак я кушаю котлетки
    public double getTotalGross() {
        double sum = 0;
        for (Payment p : payments) {
            sum += p.getAmount();
        }
        return sum;
    }

    public double getTotalFees() {
        double sum = 0;
        for (Payment p : payments) {
            sum += p.calculateFee();
        }
        return sum;
    }

    public HashSet<String> getApprovedShops() {
        HashSet<String> shops = new HashSet<>();
        for (Payment p : payments) {
            shops.add(p.getShopName());
        }
        return shops;
    }
}

public class Task2PaymentFilterAndAudit {

    public static void main(String[] args) {
        HashSet<String> blocked = new HashSet<>();
        blocked.add("ScamShop");
        blocked.add("FakeStore");

        PaymentProcessor processor = new PaymentProcessor(blocked, 20000.0);

        processor.addPayment(new CardPayment("P-01", "Rozetka", 4000.0));
        processor.addPayment(new TransferPayment("P-02", "Comfy", 10000.0));
        processor.addPayment(new CardPayment("P-01", "Rozetka", 4000.0));
        processor.addPayment(new CardPayment("P-03", "ScamShop", 5000.0));
        processor.addPayment(new TransferPayment("P-04", "LuxWatch", 25000.0));
        processor.addPayment(new CardPayment("P-05", "Rozetka", 2000.0));
        processor.addPayment(new TransferPayment("P-06", "Allo", 5000.0));

        int incoming = processor.getPaymentsCopy().size();

        System.out.println("================ 🛡️ БАНКІВСЬКИЙ ПЛАТІЖНИЙ ФІЛЬТР 🛡️ ================");
        HashSet<String> blockedCopy = processor.getBlockedShopsCopy();
        System.out.println("Чорний список магазинів [" + blockedCopy.size() + "]: " + blockedCopy);
        System.out.println(String.format(Locale.US,
                "Ліміт одного платежу: %.2f грн | Вхідних платежів: %d",
                processor.getMaxLimit(), incoming));

        System.out.println();
        System.out.println("--- Очищення черги через Iterator.remove() ---");
        int removed = processor.cleanPayments();

        ArrayList<Payment> approved = processor.getPaymentsCopy();

        System.out.println();
        System.out.println("--- Схвалені платежі (залишилось " + approved.size() + ") ---");
        for (Payment p : approved) {
            System.out.println(String.format(Locale.US,
                    "  ✅ %s | %-7s | Сума: %8.2f | Комісія: %6.2f | Нетто: %8.2f грн",
                    p.getId(), p.getShopName(), p.getAmount(), p.calculateFee(), p.getNetAmount()));
        }

        double gross = processor.getTotalGross();
        double fees = processor.getTotalFees();
        HashSet<String> shops = processor.getApprovedShops();

        System.out.println();
        System.out.println("--- Фінансовий підсумок ---");
        System.out.println("Видалено платежів: " + removed + " | Схвалено: " + approved.size());
        System.out.println(String.format(Locale.US, "Загальна сума (брутто): %.2f грн", gross));
        System.out.println(String.format(Locale.US, "Загальна комісія:       %.2f грн", fees));
        System.out.println(String.format(Locale.US, "Чиста сума (нетто):     %.2f грн", gross - fees));
        System.out.println("Унікальні магазини [" + shops.size() + "]: " + shops);
        System.out.println("====================================================================");
    }
}