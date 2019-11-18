package cluster.actor;

import akka.actor.AbstractActor;

public class ReceiverActor extends AbstractActor {

    public Receive createReceive(){
        return receiveBuilder()
                .match(String.class, this::printGreeting)
                .build();
    }

    private void printGreeting(String name) {
        System.out.println("Hello, " + name + "!");
    }

}
