package cluster;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import cluster.actor.ClusterListenerActor;
import cluster.actor.ReceiverActor;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.List;

public class ClusterApp {

    public static void main(String[] args){
        if (args.length == 0){
            start(List.of("2551", "2552", "0"));
        } else {
            start(List.of(args));
        }
    }

    private static void start(List<String> ports) {
        ports.forEach(ClusterApp::startPort);
    }

    private static void startPort(String port) {
        System.out.println("Starting on port " + port);
        Config config = ConfigFactory
                .parseString(String.format("akka.remote.netty.tcp.port=%s%n", port))
                .withFallback(ConfigFactory.load());

        ActorSystem actorSystem = ActorSystem.create("ClusterSystem", config);

        ActorRef clusterListenerRef =  actorSystem.actorOf(Props.create(ClusterListenerActor.class), "clusterListenerActor");
        ActorRef receiverRef = actorSystem.actorOf(Props.create(ReceiverActor.class), "receiverActor");

        clusterListenerRef.tell(receiverRef, clusterListenerRef);

    }

}
