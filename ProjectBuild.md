The following instructions will show you how to build the JMesa project. If you are behind a proxy see these [additional](ProjectBuildBehindProxy.md) settings.

### Setup ###

JMesa is built using [Groovy](http://groovy.codehaus.org/), Ant (via Groovy's AntBuilder) and [Ivy](http://ant.apache.org/ivy/). The first thing you need to do is make sure that you have Groovy installed correctly. Once you have it installed you can check that it is installed by typing the command 'groovy -v' on the command line. You should see something like the following:

```
jeff@jeff-desktop:~/workspace/jmesa$ groovy -v
Groovy Version: 1.1-rc-1 JVM: 1.6.0_03-b05
```

Groovy version 1.0 should work fine, as that is all that is required by JMesa.

Then copy the ivy-1.4.1.jar (or later) into the GROOVY\_HOME/lib directory so that the build can interact with Ivy.

### Build Project ###

Once you have this set up then building everything is easy. Just type in 'groovy build.groovy -a dist'. You then either press the enter key to accept the default name of 'snapshot', or enter in a revision number. This will build the project in a target directory of the JMesa project.

```
jeff@jeff-desktop:~/workspace/jmesa$ groovy build.groovy -a dist
Enter a revision number: [snapshot]
```

_Note: you will get a Swing dialog box asking for your JDK1.4 directory. This is only used to build the retro jar files. You can just safely hit cancel and let the build proceed._

_You also need to have a local copy of the repository to build the project with the groovy script. The main modification you will have to make is to the ivyconf.xml file to ensure that you point to your local repository. Just uncomment the lines that point to the local repository (which you can download from the site at 'http://jmesa.googlecode.com/svn/repository'). You might also have to change the path to be absolute depending on which version of ivy you use._

### Clean Target Directory ###

You can clean the directory by entering in 'groovy build.groovy -a clean'

```
jeff@jeff-desktop:~/workspace/jmesa$ groovy build.groovy -a clean
```

### Import Libraries ###

Lastly, if you want to just get all the jar files and place then in the lib directory of the project then enter in 'groovy build.groovy -a lib'. This is a convenience method so that you can retrieve all the libraries needed to work with your favorite IDE.

```
jeff@jeff-desktop:~/workspace/jmesa$ groovy build.groovy -a lib
```

_Building the JMesa examples project (jmesaWeb) works the same. Just be sure that you build the JMesa project (jmesa) first. The examples project uses an in-memory database so there is nothing more to running the project than checking it out and running it in your favorite servlet container._