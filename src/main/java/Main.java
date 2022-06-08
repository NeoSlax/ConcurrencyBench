import java.util.ArrayList;
import java.util.List;

public class Main {
    private final int SIZE = 10_000_000;
    private final int HALF_SIZE = SIZE / 2;

    private float[] floatList = new float[SIZE];



    public static void main(String[] args) {
        Main main = new Main();
        main.withConcurrency();
        main.withoutConcurrency();
    }

    private void withConcurrency() {
        fillArray();
        long before = System.currentTimeMillis();
        float[] firstPart = new float[HALF_SIZE];
        float[] secondPart = new float[HALF_SIZE];
        System.arraycopy(floatList, 0, firstPart, 0, HALF_SIZE);
        System.arraycopy(floatList, HALF_SIZE, secondPart, 0, HALF_SIZE);
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < firstPart.length; i++) {
                    firstPart[i] = (float) (firstPart[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
                }
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < secondPart.length; i++) {
                    secondPart[i] = (float) (secondPart[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
                }
            }
        });
        thread1.start();
        thread2.start();
        try {
            thread2.join();
            thread1.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.arraycopy(firstPart, 0, floatList, 0, HALF_SIZE);
        System.arraycopy(secondPart, 0, floatList, HALF_SIZE, HALF_SIZE);
        long after = System.currentTimeMillis();
        long resultTime = after - before;
        System.out.println("Async result is " + resultTime);
    }

    private void withoutConcurrency() {
        fillArray();
        long before = System.currentTimeMillis();
        for (int i = 0; i < floatList.length; i++) {
            floatList[i] = (float) (floatList[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
        long resultTime = System.currentTimeMillis() - before;
        System.out.println("Sync result is " + resultTime);

    }

    private void fillArray() {
        for (int i = 0; i < SIZE; i++) {
            floatList[i] = 1;
        }
    }
}
