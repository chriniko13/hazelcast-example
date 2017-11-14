package hazelcast.example.chapter2.client;

import hazelcast.example.chapter2.member.Member;

import java.util.concurrent.TimeUnit;

public class Client {

    public static void main(String[] args) {


        firstExample();

    }

    private static void firstExample() {

        /*
        Multiple Hazelcast instances can also run in a single JVM. This is useful for testing, and is also needed for more complex setups.

        So let's create a cluster of 3 members.
         */
        Member member1 = new Member();
        Member member2 = new Member();
        Member member3 = new Member();


        // --- distributed queue example ---
        System.out.println("--- DISTRIBUTED QUEUE EXAMPLE ---");
        try {
            member1.createDistributedQueue();
            member1.printDistributedQueueElements();
        } catch (InterruptedException e) {
            e.printStackTrace(System.err);
        }

        System.out.println("\n");



        // --- distributed map example ---
        System.out.println("--- DISTRIBUTED MAP EXAMPLE ---");
        member1.createDistributedMap();

        System.out.println("~~~~member 2~~~~");
        member2.printEntriesOfDistributedMap();
        System.out.println("\n");

        System.out.println("~~~~member 3~~~~");
        member3.printEntriesOfDistributedMap();
        System.out.println("\n");

        System.out.println("~~~~member 1~~~~");
        member1.printEntriesOfDistributedMap();

        System.out.println("\n");


        // --- countdown latch example ---
        System.out.println("--- COUNTDOWN LATCH EXAMPLE ---");

        member1.createCountdownLatch();
        member1.proceedToWorkUsingCountdownLatch();

        try {
            TimeUnit.SECONDS.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace(System.err);
        }

        member2.countdownLatchByOne();
        member3.countdownLatchByOne();

        System.out.println("\n");


        // --- release resources example ---
        System.out.println("--- RELEASE RESOURCES EXAMPLE ---");
        member1.releaseResources();
        System.out.println("\n");


        // --- shutdown example ---
        System.out.println("--- SHUTDOWN EXAMPLE ---");
        member1.shutdown();
        member2.shutdown();
        member3.shutdown();

    }
}
