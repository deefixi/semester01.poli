package semester1.practice10;
interface CashbackCapable {
    double MAX_CASHBACK_UAH = 500.0;

    double getCashbackRate();

    default double calculateCashback(double baseAmount) {
        return Math.min(MAX_CASHBACK_UAH, baseAmount * getCashbackRate());
    }
}
//в мене нема світла а я це роблю,я молодца

interface Refundable {
    boolean requestRefund(String reason);
    boolean isRefunded();
}

interface BlockchainVerifiable {
    String getTxHash();
    int getConfirmations();

    default boolean isFinalized() {
        return getConfirmations() >= 12;
    }
}

abstract class AbstractPaymentTransaction {
    private final String txId;
    private final double baseAmountUah;
    private boolean processed;

    protected AbstractPaymentTransaction(String txId, double baseAmountUah) {
        this.txId = txId;
        this.baseAmountUah = baseAmountUah;
        this.processed = false;
    }

    public abstract double calculateFee();
    public abstract String getPaymentChannel();

    public final boolean process() {
        if (baseAmountUah <= 0 || processed) {
            return false;
        }
        processed = true;
        return true;
    }
//бульон
    public final double getTotalCharged() {
        return baseAmountUah + calculateFee();
    }

    public String getTxId() {
        return txId;
    }

    public double getBaseAmountUah() {
        return baseAmountUah;
    }

    public boolean isProcessed() {
        return processed;
    }
}

class PremiumCardPayment extends AbstractPaymentTransaction
        implements CashbackCapable, Refundable {
    private final String maskedPan;
    private boolean refunded;

    public PremiumCardPayment(String txId, double amount, String maskedPan) {
        super(txId, amount);
        this.maskedPan = maskedPan;
        this.refunded = false;
    }

    @Override
    public double calculateFee() {
        return getBaseAmountUah() * 0.01;
    }

    @Override
    public String getPaymentChannel() {
        return "Visa Infinite";
    }

    @Override
    public double getCashbackRate() {
        return 0.03;
    }

    @Override
    public boolean requestRefund(String reason) {
        if (!isProcessed() || refunded || reason == null || reason.isBlank()) {
            return false;
        }
        refunded = true;
        return true;
    }

    @Override
    public boolean isRefunded() {
        return refunded;
    }

    public String getMaskedPan() {
        return maskedPan;
    }
}

class CryptoUsdtPayment extends AbstractPaymentTransaction
        implements CashbackCapable, BlockchainVerifiable {
    private final String txHash;
    private final int confirmations;

    public CryptoUsdtPayment(String txId, double amount, String txHash, int confirmations) {
        super(txId, amount);
        this.txHash = txHash;
        this.confirmations = confirmations;
    }
//я короче молодець да?
    @Override
    public double calculateFee() {
        return 40.0;
    }

    @Override
    public String getPaymentChannel() {
        return "USDT TRC-20";
    }

    @Override
    public double getCashbackRate() {
        return 0.01;
    }

    @Override
    public String getTxHash() {
        return txHash;
    }

    @Override
    public int getConfirmations() {
        return confirmations;
    }
}

final class TaxTreasuryPayment extends AbstractPaymentTransaction implements Refundable {
    private final String taxCode;
    private boolean refunded;

    public TaxTreasuryPayment(String txId, double amount, String taxCode) {
        super(txId, amount);
        this.taxCode = taxCode;
        this.refunded = false;
    }

    @Override
    public double calculateFee() {
        return 0.0;
    }

    @Override
    public String getPaymentChannel() {
        return "ДержКазначейство";
    }

    @Override
    public boolean requestRefund(String reason) {
        if (!isProcessed() || refunded || reason == null || reason.isBlank()) {
            return false;
        }
        refunded = true;
        return true;
    }

    @Override
    public boolean isRefunded() {
        return refunded;
    }

    public String getTaxCode() {
        return taxCode;
    }
}

public class Task2SmartPaymentAndAuditGateway {

    public static void auditTransactions(AbstractPaymentTransaction[] ledger) {
        double totalTurnover = 0.0;
        double totalFees = 0.0;
        double totalCashback = 0.0;

        System.out.println("[1. ПРОВЕДЕННЯ ПАКЕТУ ТРАНЗАКЦІЙ (final process())]:");
        for (AbstractPaymentTransaction tx : ledger) {
            if (tx.process()) {
                totalTurnover += tx.getBaseAmountUah();
                totalFees += tx.calculateFee();
                System.out.printf("  [OK] [%s | %s] Сума: %.2f грн | Комісія: %.2f грн | Всього: %.2f грн%n",
                        tx.getTxId(), tx.getPaymentChannel(), tx.getBaseAmountUah(),
                        tx.calculateFee(), tx.getTotalCharged());
            } else {
                System.out.printf("  [SKIP] %s: платіж не пройшов перевірку або вже оброблений.%n", tx.getTxId());
            }
        }

        System.out.println("\n[2. АВТОМАТИЧНИЙ АУДИТ МОЖЛИВОСТЕЙ (Pattern Matching instanceof)]:");
        for (AbstractPaymentTransaction tx : ledger) {
            if (!tx.isProcessed()) {
                continue;
            }

            if (tx instanceof CashbackCapable cashback) {
                double calculated = tx.getBaseAmountUah() * cashback.getCashbackRate();
                double paid = cashback.calculateCashback(tx.getBaseAmountUah());
                totalCashback += paid;
                System.out.printf("  [CashbackCapable] %s: нараховано %.0f%% = %.2f грн; виплата %.2f грн%n",
                        tx.getTxId(), cashback.getCashbackRate() * 100, calculated, paid);
            }

            if (tx instanceof BlockchainVerifiable blockchain) {
                System.out.printf("  [BlockchainVerifiable] %s: Hash=%s, підтверджень=%d (Finalized: %s)%n",
                        tx.getTxId(), blockchain.getTxHash(), blockchain.getConfirmations(),
                        blockchain.isFinalized());
            }
            
            if (tx instanceof PremiumCardPayment && tx instanceof Refundable refundable) {
                boolean result = refundable.requestRefund("Подвійне списання");
                System.out.printf("  [Refundable] Спроба повернення %s: %s%n",
                        tx.getTxId(), result ? "УСПІШНО ПОВЕРНЕНО" : "ВІДМОВЛЕНО");
            }
        }

        System.out.println("\n[ПІДСУМКОВИЙ ЗВІТ ШЛЮЗУ]:");
        System.out.printf("  Сумарний базовий оборот:  %.2f грн%n", totalTurnover);
        System.out.printf("  Сумарна комісія шлюзу:    %.2f грн%n", totalFees);
        System.out.printf("  Сумарний виплачений кешбек: %.2f грн%n", totalCashback);
    }

    public static void main(String[] args) {
        AbstractPaymentTransaction[] ledger = {
                new PremiumCardPayment("TX-101", 20000.0, "**** **** **** 1234"),
                new CryptoUsdtPayment("TX-102", 8500.0, "0x9f8a...c4", 18),
                new TaxTreasuryPayment("TX-103", 5000.0, "TAX-2026")
        };

        System.out.println("================ FINTECH PAYMENT GATEWAY ================");
        auditTransactions(ledger);
        System.out.println("=========================================================");
    }
}
