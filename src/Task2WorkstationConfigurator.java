package semester1.practice09.homework;

import java.util.Arrays;

class Motherboard {
    private final String model;
    private final String socketType;
    private final int maxExpansionSlots;

    public Motherboard(String model, String socketType, int maxExpansionSlots) {
        this.model = model != null ? model : "Standard MB";
        this.socketType = socketType != null ? socketType : "LGA1700";
        this.maxExpansionSlots = Math.max(1, maxExpansionSlots);
    }

    public String getModel() { return model; }
    public String getSocketType() { return socketType; }
    public int getMaxExpansionSlots() { return maxExpansionSlots; }

    public boolean isSocketCompatible(String cpuSocket) {
        if (cpuSocket == null) return false;
        return this.socketType.equalsIgnoreCase(cpuSocket);
    }
}

class PowerSupplyUnit {
    private final String model;
    private final int maxOutputWatts;
    private final double efficiencyCoefficient;

    public PowerSupplyUnit(String model, int maxOutputWatts, double efficiencyCoefficient) {
        this.model = model != null ? model : "Default PSU";
        this.maxOutputWatts = Math.max(100, maxOutputWatts);
        this.efficiencyCoefficient = Math.max(0.80, Math.min(0.96, efficiencyCoefficient));
    }

    public int getSafeContinuousWatts() {
        return (int) (maxOutputWatts * 0.85);
    }

    public double calculateWallPowerDraw(int internalLoadWatts) {
        return internalLoadWatts / efficiencyCoefficient;
    }

    public String getModel() { return model; }
    public double getEfficiencyCoefficient() { return efficiencyCoefficient; }
}

abstract class AbstractHardwareComponent {
    private final String modelName;
    private final double priceUah;

    public AbstractHardwareComponent(String modelName, double priceUah) {
        this.modelName = modelName != null ? modelName : "Generic Part";
        this.priceUah = Math.max(0.0, priceUah);
    }

    public String getModelName() { return modelName; }
    public double getPriceUah() { return priceUah; }

    public abstract int getPowerWatts();
    public abstract double getBenchmarkScore();
    public abstract String getCategory();

    @Override
    public String toString() {
        return String.format("[%s] %s (%.0f Вт | Score: %.1f | Ціна: %.2f грн)",
                getCategory(), modelName, (double)getPowerWatts(), getBenchmarkScore(), priceUah);
    }
}

class ProcessorUnit extends AbstractHardwareComponent {
    private final String socket;
    private final int cores;
    private final double boostClockGhz;
    private final int tdpWatts;

    public ProcessorUnit(String modelName, double priceUah, String socket, int cores, double boostClockGhz, int tdpWatts) {
        super(modelName, priceUah);
        this.socket = socket;
        this.cores = Math.max(1, cores);
        this.boostClockGhz = Math.max(1.0, boostClockGhz);
        this.tdpWatts = Math.max(10, tdpWatts);
    }

    public String getSocket() { return socket; }

    @Override public int getPowerWatts() { return tdpWatts; }
    @Override public double getBenchmarkScore() { return cores * boostClockGhz * 120.0; }
    @Override public String getCategory() { return "CPU"; }
}

class GraphicsAccelerator extends AbstractHardwareComponent {
    private final int vramGb;
    private final int tensorCores;
    private final int boardPowerWatts;

    public GraphicsAccelerator(String modelName, double priceUah, int vramGb, int tensorCores, int boardPowerWatts) {
        super(modelName, priceUah);
        this.vramGb = Math.max(1, vramGb);
        this.tensorCores = Math.max(0, tensorCores);
        this.boardPowerWatts = Math.max(30, boardPowerWatts);
    }

    @Override public int getPowerWatts() { return boardPowerWatts; }
    @Override public double getBenchmarkScore() { return vramGb * 250.0 + tensorCores * 1.5; }
    @Override public String getCategory() { return "GPU"; }
}

class NvmeSsdDrive extends AbstractHardwareComponent {
    private final int capacityGb;
    private final int readSpeedMbSec;
    private final int[] speedSamples;

    public NvmeSsdDrive(String modelName, double priceUah, int capacityGb, int readSpeedMbSec, int[] speedSamples) {
        super(modelName, priceUah);
        this.capacityGb = Math.max(128, capacityGb);
        this.readSpeedMbSec = Math.max(500, readSpeedMbSec);
        this.speedSamples = (speedSamples != null) ? Arrays.copyOf(speedSamples, speedSamples.length) : new int[0];
    }

    public int[] getSpeedSamples() {
        return Arrays.copyOf(speedSamples, speedSamples.length);
    }

    @Override public int getPowerWatts() { return 12; }
    @Override public double getBenchmarkScore() { return readSpeedMbSec * 0.25; }
    @Override public String getCategory() { return "NVMe SSD"; }
}

class WorkstationPC {
    private final String buildName;
    private final Motherboard motherboard;
    private final PowerSupplyUnit psu;
    private final AbstractHardwareComponent[] components;
    private int componentCount;
    private ProcessorUnit installedCpu;

    public WorkstationPC(String buildName, Motherboard motherboard, PowerSupplyUnit psu) {
        this.buildName = buildName != null ? buildName : "Default PC";
        this.motherboard = motherboard;
        this.psu = psu;
        this.components = new AbstractHardwareComponent[motherboard.getMaxExpansionSlots()];
        this.componentCount = 0;
    }

    public boolean installComponent(AbstractHardwareComponent comp) {
        if (comp == null) return false;

        if (componentCount >= components.length) {
            System.out.println("❌ Помилка: Немає вільних слотів розширення на материнській платі!");
            return false;
        }

        if (comp instanceof ProcessorUnit) {
            ProcessorUnit cpu = (ProcessorUnit) comp;
            if (!motherboard.isSocketCompatible(cpu.getSocket())) {
                System.out.printf("❌ Відмова: Процесор %s (%s) несумісний із платою %s (Сокет %s)!\n",
                        cpu.getModelName(), cpu.getSocket(), motherboard.getModel(), motherboard.getSocketType());
                return false;
            }
            this.installedCpu = cpu;
        }

        int projectedLoad = getTotalPowerWatts() + comp.getPowerWatts();
        if (projectedLoad > psu.getSafeContinuousWatts()) {
            System.out.printf("❌ Відмова: Компонент %s перевищує безпечний ліміт БЖ (%d Вт)!\n",
                    comp.getModelName(), psu.getSafeContinuousWatts());
            return false;
        }

        components[componentCount++] = comp;
        System.out.printf("✅ Встановлено: %s\n", comp);
        return true;
    }

    public int getTotalPowerWatts() {
        int sum = 0;
        for (int i = 0; i < componentCount; i++) {
            sum += components[i].getPowerWatts();
        }
        return sum;
    }

    public double getTotalSystemScore() {
        double sum = 0;
        for (int i = 0; i < componentCount; i++) {
            sum += components[i].getBenchmarkScore();
        }
        return sum;
    }

    public double getTotalBuildPrice() {
        double sum = 0;
        for (int i = 0; i < componentCount; i++) {
            sum += components[i].getPriceUah();
        }
        return sum;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\n[ПАСПОРТ ЗБІРКИ '%s']:\n", buildName));
        sb.append(String.format("  Материнська плата: %s (Socket: %s, Слотів: %d/%d)\n",
                motherboard.getModel(), motherboard.getSocketType(), componentCount, motherboard.getMaxExpansionSlots()));
        sb.append(String.format("  Блок живлення:     %s (Безпечний ліміт 85%%: %d Вт, ККД: %.0f%%)\n",
                psu.getModel(), psu.getSafeContinuousWatts(), psu.getEfficiencyCoefficient() * 100));

        int internalW = getTotalPowerWatts();
        double wallW = psu.calculateWallPowerDraw(internalW);
        sb.append(String.format("  Внутрішнє споживання: %d Вт (З розетки з урахуванням ККД: %.2f Вт) -> СТАБІЛЬНО!\n", internalW, wallW));
        sb.append(String.format("  Сумарний індекс продуктивності: %.2f балів\n", getTotalSystemScore()));
        sb.append(String.format("  Загальна вартість конфігурації: %.2f грн\n", getTotalBuildPrice()));
        return sb.toString();
    }
}

public class Task2WorkstationConfigurator {
    public static void main(String[] args) {
        System.out.println("================ КОНФІГУРАТОР РОБОЧОЇ СТАНЦІЇ (КОМПОЗИЦІЯ) ================");

        Motherboard mb = new Motherboard("ASUS ROG B650", "AM5", 4);
        PowerSupplyUnit psu = new PowerSupplyUnit("Seasonic 750W Gold", 750, 0.90);
        WorkstationPC pc = new WorkstationPC("DeepLearning-Rig", mb, psu);

        System.out.println("[ПЕРЕВІРКА СУМІСНОСТІ]:");
        ProcessorUnit wrongCpu = new ProcessorUnit("Intel Core i9-14900K", 21000.0, "LGA1700", 24, 6.0, 253);
        pc.installComponent(wrongCpu);

        ProcessorUnit correctCpu = new ProcessorUnit("AMD Ryzen 9 7950X", 23000.0, "AM5", 16, 5.7, 170);
        pc.installComponent(correctCpu);

        GraphicsAccelerator gpu = new GraphicsAccelerator("NVIDIA RTX 4080 Super", 45000.0, 16, 320, 320);
        pc.installComponent(gpu);

        int[] ssdTests = {7100, 6900, 7050};
        NvmeSsdDrive ssd = new NvmeSsdDrive("Samsung 990 Pro 2TB", 10900.0, 2000, 7450, ssdTests);
        pc.installComponent(ssd);

        System.out.println(pc);
        System.out.println("===========================================================================");
    }
}