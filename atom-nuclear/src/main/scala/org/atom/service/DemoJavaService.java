package org.atom.service;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.routing.SmallestMailboxPool;
import org.atom.config.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;

public class DemoJavaService {

    @Autowired
    private ActorSystem actorSystem;
    @Autowired
    private SpringExtension springExtension;

    public void detect() {

        ActorRef routerActorRef = actorSystem.actorOf(springExtension.props("detectActor2")
                .withDispatcher("detect-dispatcher")
                .withRouter(new SmallestMailboxPool(50000)), "detectRouterActor");

        System.out.println("into detect function,resultActorRef=" + routerActorRef.path());


        //routerActorRef.tell(new MapMessage(instance, connectMap), ActorRef.noSender());
    }
}
