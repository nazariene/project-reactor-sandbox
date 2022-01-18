package ru.nazariene.reactorsandbox;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxTest {

    @Test
    public void fluxTestBasic() {
        Flux<String> flux = Flux.just("One", "Two", "Three")
                .log();

        //verifyComplete actually creates a subscriber
        //Without verifyComplete Flux wont start
        StepVerifier.create(flux)
                .expectNext("One")
                .expectNext("Two")
                .expectNext("Three")
                .verifyComplete();
    }

    @Test
    public void fluxVerifyError() {
        Flux<String> flux = Flux.just("One", "Two", "Three")
                .concatWith(Flux.error(new RuntimeException("Runtime!")))
                .log();

        //We have to specify Verify to actually subscribe to Flux and start it
        StepVerifier.create(flux)
                .expectNext("One")
                .expectNext("Two")
                .expectNext("Three")
                .expectError()
                //.expectErrorMessage("blabla")
                .verify();
    }

    @Test
    public void fluxCountEvents() {
        Flux<String> flux = Flux.just("One", "Two", "Three")
                .concatWith(Flux.error(new RuntimeException("Runtime!")))
                .concatWith(Flux.just("Four"))
                .log();

        StepVerifier.create(flux)
                .expectNextCount(3)
                .expectError()
                .verify();
    }

    @Test
    public void fluxMutate() {
        Flux<String> flux = Flux.just("One", "Two", "Three");

        //Add log inside tests, without tampering the code itself.
        StepVerifier.create(flux.log())
                .expectNext("One")
                .expectNext("Two")
                .expectNext("Three")
                .verifyComplete();

    }
}
