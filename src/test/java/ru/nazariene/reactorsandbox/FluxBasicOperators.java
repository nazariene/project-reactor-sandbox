package ru.nazariene.reactorsandbox;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;

import java.util.List;

public class FluxBasicOperators {

    @Test
    public void fluxBasics() {
        Flux<String> flux = Flux.just("One", "Two", "Three");

        //Until you subscribe - flux is just a function, it's NOT executed!
        flux.subscribe(System.out::println);
    }

    @Test
    public void errorAndLog() {
        //.log() logs Flux events - onNext, onError and onComplete - helpful in debug
        //.concatWith adds elements to Flux
        Flux<String> flux = Flux.just("One", "Two", "Three")
                .concatWith(Flux.error(new RuntimeException("Runtime!")))
                .log()
                .map(String::toUpperCase)
                .log();

        //Log is applied to a particular step, not the whole sequence!

        //Second argument - is error consumer (error events go there)
        flux.subscribe(System.out::println, System.err::println);
    }

    @Test
    public void eventAfterError() {
        Flux<String> flux = Flux.just("One", "Two", "Three")
                .concatWith(Flux.error(new RuntimeException("Runtime!")))
                .concatWith(Flux.just("Four"))
                .log();

        //Four is not printed - once Error is emitted from Flux - that's it.
        flux.subscribe(System.out::println, System.err::println);
    }

    @Test
    public void filter() {
        Flux<String> flux = Flux.fromIterable(List.of("One", "Two", "Three"))
                .log()
                .filter(s -> s.length() < 4)
                .map(String::toUpperCase)
                .log();

        flux.subscribe(System.out::println);
   }


}
