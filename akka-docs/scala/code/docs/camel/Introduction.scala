package docs.camel

import akka.actor.{ Props, ActorSystem }
import akka.camel.CamelExtension

object Introduction {
  {
    //#Consumer-mina
    import akka.camel.{ CamelMessage, Consumer }

    class MyEndpoint extends Consumer {
      def endpointUri = "mina:tcp://localhost:6200?textline=true"

      def receive = {
        case msg: CamelMessage ⇒ { /* ... */ }
        case _                 ⇒ { /* ... */ }
      }
    }

    // start and expose actor via tcp
    import akka.actor.{ ActorSystem, Props }

    val system = ActorSystem("some-system")
    val mina = system.actorOf(Props[MyEndpoint])
    //#Consumer-mina
  }
  {
    //#Consumer
    import akka.camel.{ CamelMessage, Consumer }

    class MyEndpoint extends Consumer {
      def endpointUri = "jetty:http://localhost:8877/example"

      def receive = {
        case msg: CamelMessage ⇒ { /* ... */ }
        case _                 ⇒ { /* ... */ }
      }
    }
    //#Consumer
  }
  {
    //#Producer
    import akka.actor.Actor
    import akka.camel.{ Producer, Oneway }
    import akka.actor.{ ActorSystem, Props }

    class Orders extends Actor with Producer with Oneway {
      def endpointUri = "jms:queue:Orders"
    }

    val sys = ActorSystem("some-system")
    val orders = sys.actorOf(Props[Orders])

    orders ! <order amount="100" currency="PLN" itemId="12345"/>
    //#Producer
  }
  {
    //#CamelExtension
    val system = ActorSystem("some-system")
    val camel = CamelExtension(system)
    val camelContext = camel.context
    val producerTemplate = camel.template

    //#CamelExtension
  }
  {
    //#CamelExtensionAddComponent
    // import org.apache.activemq.camel.component.ActiveMQComponent
    val system = ActorSystem("some-system")
    val camel = CamelExtension(system)
    val camelContext = camel.context
    // camelContext.addComponent("activemq", ActiveMQComponent.activeMQComponent("vm://localhost?broker.persistent=false"))
    //#CamelExtensionAddComponent
  }
  {
    //#CamelActivation
    import akka.camel.{ CamelMessage, Consumer }
    import akka.util.duration._

    class MyEndpoint extends Consumer {
      def endpointUri = "mina:tcp://localhost:6200?textline=true"

      def receive = {
        case msg: CamelMessage ⇒ { /* ... */ }
        case _                 ⇒ { /* ... */ }
      }
    }
    val system = ActorSystem("some-system")
    val camel = CamelExtension(system)
    val actorRef = system.actorOf(Props[MyEndpoint])
    // get a future reference to the activation of the endpoint of the Consumer Actor
    val activationFuture = camel.activationFutureFor(actorRef, 10 seconds)
    // or, block wait on the activation
    camel.awaitActivation(actorRef, 10 seconds)
    //#CamelActivation
    //#CamelDeactivation
    system.stop(actorRef)
    // get a future reference to the deactivation of the endpoint of the Consumer Actor
    val deactivationFuture = camel.activationFutureFor(actorRef, 10 seconds)
    // or, block wait on the deactivation
    camel.awaitDeactivation(actorRef, 10 seconds)
    //#CamelDeactivation
  }

}