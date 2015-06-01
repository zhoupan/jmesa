The JMesa project uses the Simple Logging Facade for Java (SLF4J). The reason is I think it offers the ability to be an easy, flexible and reliable logging framework.

A while back a JMesa user asked about moving to [SLF4J](http://www.slf4j.org/manual.html). I did some checking and this seems like a much better approach to logging. In addition it offers two ways to work with the commons-logging framework making this an easier buy-in for developers. I also like that the creator of log4j is supporting the library, and his new logging framework has support built in.

More information can be found on the [SLF4J User manual](http://www.slf4j.org/manual.html).

#### Example ####

If you are using Log4j the minimum jar files needed is:

  * slf4j-api-1.4.3.jar
  * slf4j-log4j12-1.4.3.jar
  * log4j-1.2.8.jar

To add support for the commons-logging framework also add:
  * jcl104-over-slf4j-1.4.3.jar

Thats it! Just a few new jar files and you are now using SLF4J.