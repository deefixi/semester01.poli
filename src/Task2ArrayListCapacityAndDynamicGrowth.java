package semester1.practice10.homework;

// я давно хотіла сказати...
class CustomArrayCapacityEmulator<E> {
    private static final int DEFAULT_CAPACITY = 10;

    private Object[] elementData;
    private int size = 0;

    private int resizeCount = 0;
    private int totalElementsCopied = 0;

    public CustomArrayCapacityEmulator() {
        elementData = new Object[DEFAULT_CAPACITY];
    }

    private void ensureCapacityInternal() {
        if (size == elementData.length) {
            int oldCapacity = elementData.length;
            int newCapacity = oldCapacity + (oldCapacity >> 1); // 10 -> 15 -> 22 -> 33 -> 49
            Object[] newBuffer = new Object[newCapacity];
            System.arraycopy(elementData, 0, newBuffer, 0, size);
            elementData = newBuffer;
            resizeCount++;
            totalElementsCopied += size;
        }
    }

    public void add(E element) {
        ensureCapacityInternal();
        elementData[size++] = element;
    }

    public void add(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        ensureCapacityInternal();
        System.arraycopy(elementData, index, elementData, index + 1, size - index);
        elementData[index] = element;
        size++;
    }

    @SuppressWarnings("unchecked")
    public E remove(int index) {
        checkIndex(index);
        E oldElement = (E) elementData[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elementData, index + 1, elementData, index, numMoved);
        }
        elementData[--size] = null;
        return oldElement;
    }
//я леді баг
    @SuppressWarnings("unchecked")
    public E get(int index) {
        checkIndex(index);
        return (E) elementData[index];
    }

    @SuppressWarnings("unchecked")
    public E set(int index, E element) {
        checkIndex(index);
        E oldElement = (E) elementData[index];
        elementData[index] = element;
        return oldElement;
    }

    public int size() {
        return size;
    }

    public int getCapacity() {
        return elementData.length;
    }

    public void trimToSize() {
        if (size < elementData.length) {
            Object[] newBuffer = new Object[size];
            System.arraycopy(elementData, 0, newBuffer, 0, size);
            elementData = newBuffer;
        }
    }

    public int getResizeCount() {
        return resizeCount;
    }

    public int getTotalElementsCopied() {
        return totalElementsCopied;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }
}

public class Task2ArrayListCapacityAndDynamicGrowth {

    public static void main(String[] args) {
        System.out.println("================ ЕМУЛЯТОР ВНУТРІШНЬОЇ БУДОВИ ARRAYLIST (ПІД КАПОТОМ) ================");

        var emulator = new CustomArrayCapacityEmulator<String>();
        System.out.println("[ІНІЦІАЛІЗАЦІЯ]: Створено CustomArrayCapacityEmulator<String> | size = "
                + emulator.size() + " | capacity = " + emulator.getCapacity());

        System.out.println("\n--- Додавання 35 елементів у список ---");
        for (int i = 1; i <= 35; i++) {
            int capacityBefore = emulator.getCapacity();
            emulator.add("Block-" + i);
            int capacityAfter = emulator.getCapacity();

            if (capacityAfter != capacityBefore) {
                System.out.println("  ⚡ [GROW #" + emulator.getResizeCount() + "] Переповнення при додаванні "
                        + i + "-го елемента! Ємність: " + capacityBefore + " -> " + capacityAfter
                        + " (System.arraycopy скопіював " + capacityBefore + " ел.)");
            }
        }
        System.out.println("Стан після додавання 35 елементів: size = " + emulator.size()
                + " | capacity = " + emulator.getCapacity()
                + " (Вільний резерв: " + (emulator.getCapacity() - emulator.size()) + " комірок null)");

        System.out.println("\n--- Тестування вставки add(0, 'HEADER') та видалення remove(5) ---");
        int sizeBeforeInsert = emulator.size();
        emulator.add(0, "HEADER");
        System.out.println("  -> Вставлено 'HEADER' на індекс 0 (зсунуто праворуч " + sizeBeforeInsert
                + " елементів через System.arraycopy)");

        int movedOnRemove = emulator.size() - 5 - 1;
        String removed = emulator.remove(5);
        System.out.println("  -> Видалено елемент '" + removed + "' з індексу 5 (зсунуто ліворуч "
                + movedOnRemove + " елементів, хвіст занулено для GC)");
        System.out.println("  Поточний стан: size = " + emulator.size() + " | capacity = " + emulator.getCapacity()
                + " | Перший: '" + emulator.get(0) + "' | Останній: '" + emulator.get(emulator.size() - 1) + "'");

        System.out.println("\n--- Оптимізація пам'яті trimToSize() ---");
        int capacityBeforeTrim = emulator.getCapacity();
        emulator.trimToSize();
        System.out.println("  -> Викликано trimToSize(): буфер Object[" + capacityBeforeTrim
                + "] стиснуто рівно до Object[" + emulator.getCapacity() + "]!");
        System.out.println("  Фінальна телеметрія: size = " + emulator.size() + " | capacity = "
                + emulator.getCapacity() + " | Кількість розширень: " + emulator.getResizeCount());
        System.out.println("  Всього елементів скопійовано при розширеннях: " + emulator.getTotalElementsCopied());

        System.out.println("=====================================================================================");
    }
}