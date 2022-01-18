package ru.nazariene.reactorsandbox;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class SchedulersDemo {

    /**
     * publishOn affects the thread where next operators will be executed.
     * Previous operators in the chain are not affected
     * doOnNext will be exectuted in default (parallel or test) thread pool
     * subscribe sout will be executed in boundedElastic thread pool
     */
    @Test
    public void publishOn() {
        Flux.range(0, 10)
                .doOnNext(i -> System.out.println("onNext: " + Thread.currentThread().getName() + ", " + i))
                .map(i -> i *10)
                .publishOn(Schedulers.boundedElastic())
                .subscribe(i -> System.out.println("Sub: " + Thread.currentThread().getName() + ", " + i));
    }

    /**
     * SubscribeOn affect the whole operator chain
     * All operators will be executed in boundedElastic thread pool
     */
    @Test
    public void subscribeOn() {
        Flux.range(0, 10)
                .doOnNext(i -> System.out.println("onNext: " + Thread.currentThread().getName() + ", " + i))
                .map(i -> i *10)
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(i -> System.out.println("Sub: " + Thread.currentThread().getName() + ", " + i));
    }

    /**
     * While subscribeOn affect the whole operator chain, it is overrided by publishOn
     * I.e. subscribeOn will change execution thread until another subscribeOn or publishOn is met
     */
    @Test
    public void publishAndSubscribe() {
        Flux.range(0, 10)
                .doOnNext(i -> System.out.println("onNext: " + Thread.currentThread().getName() + ", " + i))
                .map(i -> i *10)
                .subscribeOn(Schedulers.boundedElastic())
                .publishOn(Schedulers.single())
                .doOnNext(i -> System.out.println("onNext2: " + Thread.currentThread().getName() + ", " + i))
                .subscribe(i -> System.out.println("Sub: " + Thread.currentThread().getName() + ", " + i));
    }
}
