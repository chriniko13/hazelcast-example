package hazelcast.example.chapter3.member;

import com.hazelcast.core.*;
import hazelcast.example.chapter3.domain.Holder;

import java.util.Random;

public class Member {

    private final HazelcastInstance hazelcastInstance;

    public Member() {
        hazelcastInstance = Hazelcast.newHazelcastInstance();
    }


    // --- atomic long example ---
    public void atomicLongExample() {

        IAtomicLong myAtomicLong = hazelcastInstance.getAtomicLong("myAtomicLong");

        for (int i = 0; i < 10000; i++) {
            myAtomicLong.incrementAndGet();
        }

        System.out.println(hazelcastInstance.getName() + " --- myAtomicLong == " + myAtomicLong.get());

    }

    // --- atomic long example 2 ---
    public void atomicLongExample2() {

        IAtomicLong myAtomicLong = hazelcastInstance.getAtomicLong("myAtomicLong2");

        IFunction<Long, Long> add2Function = input -> input + 2;

        myAtomicLong.set(1);
        long result = myAtomicLong.apply(add2Function);
        System.out.println("apply.result:" + result);
        System.out.println("apply.value:" + myAtomicLong.get());

        myAtomicLong.set(1);
        myAtomicLong.alter(add2Function);
        System.out.println("alter.value:" + myAtomicLong.get());

        myAtomicLong.set(1);
        result = myAtomicLong.alterAndGet(add2Function);
        System.out.println("alterAndGet.result:" + result);
        System.out.println("alterAndGet.value:" + myAtomicLong.get());

        myAtomicLong.set(1);
        result = myAtomicLong.getAndAlter(add2Function);
        System.out.println("getAndAlter.result:" + result);
        System.out.println("getAndAlter.value:" + myAtomicLong.get());


    }

    // --- id generator example ---
    public void idGeneratorExample() {


        IdGenerator myIdGenerator = hazelcastInstance.getIdGenerator("myIdGenerator");

        for (int k = 1; k < 10; k++) {

            long generatedId = myIdGenerator.newId();
            System.out.println(hazelcastInstance.getName() + " --- generatedId == " + generatedId);

            try {
                Thread.sleep(new Random().nextInt(5) * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
        }

    }

    // --- atomic reference example ---
    public void atomicReferenceExample() {

        IAtomicReference<Object> myAtomRef = hazelcastInstance.getAtomicReference("myAtomRef");


        if (myAtomRef.isNull()) {
            myAtomRef.set(new Holder("Title", "Description"));
        }

        System.out.println(hazelcastInstance.getName() + " --- myAtomRef.value == " + myAtomRef.get());

    }

    // --- lock example ---
    public void lockExample() {

        IAtomicReference<Object> counter1 = hazelcastInstance.getAtomicReference("counter1");
        IAtomicReference<Object> counter2 = hazelcastInstance.getAtomicReference("counter2");

        ILock myLock = hazelcastInstance.getLock("myLock");

        System.out.println(hazelcastInstance.getName() + " --- Started");
        for (int k = 1; k <= 25; k++) {

            if (k % 100 == 0)
                System.out.println(hazelcastInstance.getName() + " --- at: " + k);

            myLock.lock();
            try {
                System.out.println(hazelcastInstance.getName() + " --- lock acquired!");

                if (counter1.isNull()) {
                    counter1.set(1);
                }

                if (counter2.isNull()) {
                    counter2.set(1);
                }

                int num1 = (int) counter1.get();

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }

                int num2 = (int) counter2.get();

                if (num1 - num2 != 0) {
                    System.out.println(hazelcastInstance.getName() + " --- Datarace detected!");
                    throw new IllegalStateException("Datarace detected!");
                } else {

                    counter1.alter((IFunction<Object, Object>) input -> {
                        int num = (int) input;
                        return num + 1;
                    });


                    counter2.alter((IFunction<Object, Object>) input -> {
                        int num = (int) input;
                        return num + 1;
                    });
                }

            } finally {
                myLock.unlock();
                System.out.println(hazelcastInstance.getName() + " --- lock released!");
            }

        }
        System.out.println(hazelcastInstance.getName() + " --- Finished");


    }


    // --- condition example ---


}