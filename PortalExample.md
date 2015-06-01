In the 2.3.4 release of JMesa there is explicit support for portlets. I want to also personally thank Benjamin Gould for doing all the work to supply this example code as well as all the portlet implementation code!

Right now to run the examples you need to get the jmesa, jmesaWeb, repository, and portal projects from the subversion repository. You can read about how to setup up everything but the portal server by reading the [project build](ProjectBuild.md) page. This page will then describe what you need to do to get the portal up and running.

### Steps ###
  * First check out the portal project from subversion under the same directory as you did jmesa, jmesaWeb, and the repository projects.
  * Build the jmesa project by running the build.groovy script. _Just press the enter key to accept snapshot as the build name_

```
cd jmesa
groovy build.groovy -a lib //if you have not included the jars yet
groovy build.groovy -a dist
```

  * Build the jmesaWeb project by running the build.groovy script. _Just press the enter key to accept snapshot as the build name_

```
cd jmesaWeb
groovy build.groovy -a lib //if you have not included the jars yet
groovy build.groovy -a dist
```

  * Run the Portal server.

```
cd portal
ant run-examples (or just ant)
```

Now point your browser at http://localhost:8080/. Clicking on the pluto context will take you to the portal and clicking on the jmesa context will take you to the servlet examples.

_Note: the jmesaWeb/build.groovy has an extra task called run() which builds the example project and starts the server with it.  The only caveat is that you need to remove jar files that start with jsp and servlet from $GROOVY\_HOME/lib in order to avoid classpath issues. Starting the server from the ant build does not suffer from this problem._

The portal is configured with both the regular portlet example and the AJAX example on one page.  Because of the javascript namespace dilemma that is presented in the group, the regular portlet example does not work when both portlets and rendered on the same page.  Minimizing the AJAX portlet or Maximizing the regular portlet will solve this problem (until the jQuery plugin is finished).

This is a good setup because the Jetty ant task will automatically reload the web app if you compile any classes, so its really easy to try out different things with the examples.  It would be really nice if it could detect (I know there's a way) when jmesa-snapshot.jar is rebuilt so that the changes could be reflected in the examples right away as well.

