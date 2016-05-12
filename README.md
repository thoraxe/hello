# hello
_A reactive microservice developed in Java._

The hello microservice is developed in Java. It is invocable using the vert.x event bus, and handles:

* messages on `hello` - replies with a _hello_ message
* messages on `hello\chain` - invokes the next service of the chain and replies with the result + a _hello_ message. The invocation to the next service is protected using a circuit breaker.

The detailed instructions to run the Red Hat Reactive MSA demo, can be found at the following repository: https://github.com/redhat-reactive-msa/redhat-reactive-msa

## Execute the hello microservice locally

Open a command prompt and navigate to the root directory of this microservice.
Then to package this microservice run:

```
mvn clean package
```

Type this command to execute the application:

```
java -jar target/hello-0.0.1-SNAPSHOT-fat.jar -cluster -conf src/conf/config.json
```

## Execute on Openshift

Refer to https://github.com/redhat-reactive-msa/redhat-reactive-msa.
