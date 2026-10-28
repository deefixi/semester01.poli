public class Task1CurrencyExchange {
    public static void main(String[] args) {
    double usdAmount = 250.0;
    final double EXCHANGE_RATE = 41.25;
    final double COMMISSION_PERCENT = 1.5;
    double totalUah = usdAmount * EXCHANGE_RATE;
    double commissionUah = totalUah * (COMMISSION_PERCENT / 100.0);
    double netAmountUah = totalUah - commissionUah;
    long cashAmount = Math.round(netAmountUah);

    System.out.println("================ КВИТАНЦІЯ ОБМІНУ ================");
    System.out.println("Сума до обміну:             " + String.format("%.2f", usdAmount) + " USD");
    System.out.println("Курс валют:                 " + String.format("%.2f", EXCHANGE_RATE) + " UAH/USD");
    System.out.println("Нараховано до комісії:      " + String.format("%.2f", totalUah) + " UAH");
    System.out.println("Комісія банку (1.5%):       " + String.format("%.2f", commissionUah) + " UAH");
    System.out.println("--------------------------------------------------");
    System.out.println("Чиста сума до видачі:       " + String.format("%.2f", netAmountUah) + " UAH");
    System.out.println("До видачі готівкою:         " + cashAmount + " UAH");
    System.out.println("==================================================");
    }
}
