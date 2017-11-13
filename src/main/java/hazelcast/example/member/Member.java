package hazelcast.example.member;

import com.hazelcast.core.*;
import hazelcast.example.configuration.DistributedConstants;
import hazelcast.example.domain.Student;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Member {

    private final HazelcastInstance hazelcastInstance;

    private static final String PARTITION_KEY;

    static {
        PARTITION_KEY = UUID.randomUUID().toString();
    }

    public Member() {
        hazelcastInstance = Hazelcast.newHazelcastInstance();
    }

    // --- distributed queue example ---
    public void createDistributedQueue() throws InterruptedException {

        IQueue<String> myQueue = hazelcastInstance.getQueue("myQueue@" + PARTITION_KEY);

        for (int i = 0; i < 10; i++) {
            myQueue.put(UUID.randomUUID().toString());
        }

    }

    public void printDistributedQueueElements() throws InterruptedException {

        IQueue<String> myQueue = hazelcastInstance.getQueue("myQueue@" + PARTITION_KEY);

        while (!myQueue.isEmpty()) {
            String result = myQueue.take();
            System.out.println("result from queue = " + result);
        }

    }


    // --- distributed countdownlatch example ---

    public void createCountdownLatch() {

        ICountDownLatch myCountdownLatch = hazelcastInstance.getCountDownLatch(DistributedConstants.COUNT_DOWN_LATCH + "@" + PARTITION_KEY);
        myCountdownLatch.trySetCount(2);

    }

    public void countdownLatchByOne() {

        ICountDownLatch myCountdownLatch = hazelcastInstance.getCountDownLatch(DistributedConstants.COUNT_DOWN_LATCH + "@" + PARTITION_KEY);
        myCountdownLatch.countDown();
        System.out.println(Thread.currentThread().getName() + ", countdownlatch by one...");

    }

    public void proceedToWorkUsingCountdownLatch() {


        try {
            ICountDownLatch myCountdownLatch = hazelcastInstance.getCountDownLatch(DistributedConstants.COUNT_DOWN_LATCH + "@" + PARTITION_KEY);

            System.out.println(Thread.currentThread().getName() + ", waiting in countdown-latch...");

            myCountdownLatch.await(1, TimeUnit.MINUTES);

            System.out.println(Thread.currentThread().getName() + ", passed countdown-latch...will continue with work...");


        } catch (InterruptedException e) {

            System.err.println(Thread.currentThread().getName() + " --- error occurred, message = " + e.getMessage());

        }

    }


    // --- distributed map example ---
    public void createDistributedMap() {

        IMap<Long, Student> myMap = hazelcastInstance.getMap(DistributedConstants.DISTRIBUTED_MAP + "@" + PARTITION_KEY);

        IdGenerator myIdGenerator = hazelcastInstance.getIdGenerator(DistributedConstants.ID_GENERATOR + "@" + PARTITION_KEY);

        for (int i = 0; i < 10; i++) {
            long key = myIdGenerator.newId();
            myMap.put(key, new Student("firstname" + key, "initials" + key, "surname" + key));
        }

    }

    public void printEntriesOfDistributedMap() {

        IMap<Long, Student> myMap = hazelcastInstance.getMap(DistributedConstants.DISTRIBUTED_MAP + "@" + PARTITION_KEY);

        myMap.forEach((key, value) -> System.out.println("entry, key = " + key + ", value = " + value));

    }

    // --- release operation ---
    public void releaseResources() {

        hazelcastInstance.getMap(DistributedConstants.DISTRIBUTED_MAP + "@" + PARTITION_KEY).destroy();
        hazelcastInstance.getCountDownLatch(DistributedConstants.COUNT_DOWN_LATCH + "@" + PARTITION_KEY).destroy();
        hazelcastInstance.getIdGenerator(DistributedConstants.ID_GENERATOR + "@" + PARTITION_KEY).destroy();
        hazelcastInstance.getQueue("myQueue" + "@" + PARTITION_KEY).destroy();

    }


}
