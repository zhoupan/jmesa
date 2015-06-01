When building behind a proxy that requires authentication there are two things that need to be done in order to checkout the project from subversion and build it successfully.

1) Configure your svn client to use proxy auth when downloading the code as specified at [here](http://vikashazrati.wordpress.com/2009/01/25/http-proxy-sv/).

It is also conveniently copied here...

There is a servers file in svn which is present at the following location:
  * Windows : C:\Documents and Settings\(username)\Application Data\Subversion\servers
  * Linux: /etc/subversion/servers

Here you need to set the proxy server and port settings so that command line SVN can access the external world form the proxy. Uncomment and change the lines necessary.

```
  [global]
  # http-proxy-exceptions = *.exception.com, www.internal-site.org
  http-proxy-host = myproxy.us.com
  http-proxy-port = 8080
  http-proxy-username = username
  http-proxy-password = password
  # http-compression = no
  # http-auth-types = basic;digest;negotiate
  # No http-timeout, so just use the builtin default.
  # No neon-debug-mask, so neon debugging is disabled.
```

If you get something like "_svn: C:\Documents and Settings\(username)\Application Data\Subversion\servers:73: Option expected_", then it means that you have a space at the start of the property which you have uncommented. Make sure that there is no space in the beginning of the property in the servers file.

2) Configure the build.groovy script to instruct ant (AntBuilder) and Ivy, to use the proxy server when trying to resolve dependencies from a remote repository

In your build.groovy script, before any call to ivyresolve(), add the following line to setup the proxy.

```
ant.setproxy(proxyhost:'xxx.xxx.xxx.xxx.',proxyport:'8080',proxyuser:'username',proxypassword:'yourpassword')
```

This will call the [setproxy](http://ant.apache.org/manual/OptionalTasks/setproxy.html) ant task. If you want to get more detail about possible errors use:
```
  ant.record(name:'log.txt',action:'start',loglevel:'debug')
```

The modified dist() function looks like this:
```
  def dist() {
    clean()
    init()
    testInit()
    ant.record(name:'log.txt',action:'start',loglevel:'debug')
    ant.setproxy(proxyhost:'xxx.xxx.xxx.xxx.',proxyport:'8080',proxyuser:'username',proxypassword:'yourpassword')
    ivyresolve()
    ivyretrieve()
    ant.record(name:'log.txt',action:'stop',loglevel:'debug')
    classpaths()
    compile()
    jar()
    ivypublish()
    testClasspaths()
    testCompile()
    junit()
    retro()
    copy()
    zip()
    docs()
  }
```