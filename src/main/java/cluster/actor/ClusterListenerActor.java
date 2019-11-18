package cluster.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;

public class ClusterListenerActor extends AbstractActor {

    private Cluster cluster = Cluster.get(getContext().system());

    @Override
    public void preStart(){
        System.out.println("Starting cluster listener actor");
        cluster.subscribe(self(), (ClusterEvent.SubscriptionInitialStateMode) ClusterEvent.initialStateAsEvents(), ClusterEvent.ClusterDomainEvent.class);
    }

    @Override
    public void postStop(){
        System.out.println("Stopped cluster listener actor");
        cluster.unsubscribe(self());
    }

    public Receive createReceive(){
        return receiveBuilder()
                .match(ActorRef.class, this::greetActor)
                .match(ClusterEvent.MemberUp.class, m -> System.out.println("Member up: " + m))
                .match(ClusterEvent.MemberRemoved.class, m -> System.out.println("Member removed: " + m))
                .match(ClusterEvent.UnreachableMember.class, m -> System.out.println("Member unreachable: " + m))
                .matchAny(m -> System.out.println("Received message: " + m))
                .build();
    }

    private void greetActor(ActorRef actorRef) {
        actorRef.tell("Aoife", self());
    }
}
