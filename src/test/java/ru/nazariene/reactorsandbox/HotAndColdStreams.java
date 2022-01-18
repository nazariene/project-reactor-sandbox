package ru.nazariene.reactorsandbox;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class HotAndColdStreams {

    @Test
    public void ColdStream() {
        var coldStream = Flux.just("One", "Two", "Three");

        //Each of the subscribers will get identical set of data
        coldStream.subscribe(System.out::println);
        coldStream.subscribe(System.out::println);
    }

    @Test
    public void HotStream() throws InterruptedException {
        var hotStream = Flux.just("One", "Two", "Three", "Four")
                .delayElements(Duration.ofMillis(1000))
                .publish();

        hotStream.connect();

        hotStream.subscribe(number -> System.out.println("Subscriber 1 - " + number));

        //Subscriber 2 will miss out "One" and "Two" events
        Thread.sleep(2000);
        hotStream.subscribe(number -> System.out.println("Subscriber 2 - " + number));

        Thread.sleep(5000);
    }
}
