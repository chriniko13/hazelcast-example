package hazelcast.example.chapter3.client;

import hazelcast.example.chapter3.member.Member;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

    public static final ExecutorService WORKERS_POOL = Executors.newFixedThreadPool(5);

    public static void main(String[] args) {


        Member member1 = new Member();
        Member member2 = new Member();
        Member member3 = new Member();

        System.out.println("--- ATOMIC LONG EXAMPLE ---");
        member1.atomicLongExample();
        member2.atomicLongExample();
        member3.atomicLongExample();

        System.out.println("\n");

        System.out.println("--- ATOMIC LONG EXAMPLE 2 ---");
        member1.atomicLongExample2();

        System.out.println("\n");

        /*
        System.out.println("--- ID GENERATOR EXAMPLE ---");

        WORKERS_POOL.submit(() -> member1.idGeneratorExample());
        WORKERS_POOL.submit(() -> member2.idGeneratorExample());
        WORKERS_POOL.submit(() -> member3.idGeneratorExample());

        System.out.println("\n");
        */

        System.out.println("--- ATOMIC REFERENCE EXAMPLE ---");
        member1.atomicReferenceExample();
        member2.atomicReferenceExample();
        member3.atomicReferenceExample();

        System.out.println("\n");


        System.out.println("--- LOCK EXAMPLE ---");

        WORKERS_POOL.submit( () -> member1.lockExample());
        WORKERS_POOL.submit( () -> member2.lockExample());
        WORKERS_POOL.submit( () -> member3.lockExample());

        System.out.println("\n");

    }
}
