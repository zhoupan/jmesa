The only core dependencies are a few Apache Commons libraries in which JMesa is fortunate enough to be able to utilize. Some advanced functionality will require more jars, as detailed below.

### Requirements ###
  * JDK1.5 (or higher)
  * [jQuery](http://jquery.com) (required as of JMesa 2.2.3)

### Minimum Jars Needed ###
  * commons-beanutils-1.7.0.jar (1.8.0 is recommended)
  * commons-collections-3.0.jar
  * commons-lang-2.2.jar

#### Logging ####

In addition JMesa is using [SLF4J](SLF4J.md). The core jar file is:
  * slf4j-api-1.4.3.jar

_See the [SLF4J](SLF4J.md) page for details about working with the slf4j logging framework._

#### Excel Export ####
  * poi-3.0.1.jar

#### JExcel Export ####
  * jexcel-2.6.6.jar

#### PDF Export ####
  * core-renderer-R8pre1.jar
  * itext-paulo-155.jar
  * tagsoup-1.1.3.jar

#### PDF Export (Using PdfPView) ####
  * itext-paulo-155.jar

#### Tags ####
  * servlet-api-2.4.jar
  * jsp-api-2.0.jar

### Installation ###
First download the [distribution](http://code.google.com/p/jmesa/downloads/list).

In the jmesa.zip file you will find everything you need to get started:
  * jmesa.jar
  * jmesa.css
  * jmesa-pdf.css
  * jmesa.js
  * jquery.jmesa.js
  * jmesa.tld
  * default images
  * source code

The installation is easy. Simply put the jmesa.jar into your WEB-INF/lib directory and you are all ready to start coding your table. Of course to render correctly on the web page you will need to install the Javascript, CSS, and Images. Your project probably has plenty of those resources and should be used consistently.

Note: make sure that you declare the jquery.jmesa.js before the jmesa.js, but also after the jQuery (distribution) file.

If you need to do more customization you might need to set up the [Messages](Messages.md) and [Preferences](Preferences.md).

### Building From Source ###
I know that some developers like to checkout JMesa from [subversion](http://code.google.com/p/jmesa/source) and build it directly to get the latest. For those developers I wrote up an [article](ProjectBuild.md) that explains how to build the project directly from source code.