package org.atom.actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.actor.Status;
import akka.japi.pf.ReceiveBuilder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import scala.PartialFunction;

@Component("detectActor2")
@Scope("prototype")
public class DetectActor2 extends AbstractActor {

    public PartialFunction receive() {
        return ReceiveBuilder.match(String.class, s -> {
            System.out.printf("get %s\n" , s);
            sender().tell("Hi", self());

        }).matchAny(x -> {
            System.out.printf("I dont know what you see in DetectActor,%s", getContext().self().path());
            sender().tell(new Status.Failure(new Exception("I dont know what you see")), self());
        }).build();
    }

    public static Props props(String response) {
        return Props.create(DetectActor2.class, response);
    }
}