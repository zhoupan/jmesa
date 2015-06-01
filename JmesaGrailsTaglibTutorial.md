This document would introduce you the Jmesa Grails Plugin by sample.
We can play with the Jmesa and Grails together now! To reach that,all you need to do is download and install the plugin.The useage of the plugin taglib is the same to the jsp version, if you are new to Jmesa taglib . Check out the [JSPTags](JSPTags.md) document.

Moving on.

# setup #
## Install Grails ##
Make sure you had Grails 1.0.2 installed.if you are new to Grails,see the installation guide.http://grails.org/Installation
## Get the latest plugin ##
  * Build from source
    1. Check out latest source.
```
     svn checkout http://jmesa.googlecode.com/svn/branches/gsp/jmesaGrailsPlugin jmesaGrailsPlugin 
```
    1. resolve libs
> > > see the jmesaGrailsPlugin/lib/readme.txt,and drop the lib on the list into this directory.
    1. package you built jmesa plugin.
```
     jeffjie:~ jeff$ cd /path/to/jmesaGrailsPlugin
     jeffjie:jmesaGrailsPlugin jeff$ grails package-plugin
```

> after that you can find the plugin archive(grails-jmesa-0.1.zip) at the root of the project path.
  * Download the latest release binary
> You can down load the latest release here.

## Get sample source ##
  1. Check out the latest soure from SVN repository.
```
 svn checkout http://jmesa.googlecode.com/svn/branches/gsp/pluginSample pluginSample
```
  1. resolve libs
> see the pluginSample/lib/readme.txt,and drop the lib on the list into this directory. In this case,just forget it.
## Install jmesa plugin ##
```
     jeffjie:~ jeff$ cd /path/to/pluginSample
     jeffjie:pluginSample jeff$ grails install-plugin /Volumes/work/projects/thirdpart/jmesa/branches/gsp/jmesaGrailsPlugin/grails-jmesa-0.1.zip
```
## Run sample ##
```
 jeffjie:pluginSample jeff$ grails run-app
```
## Browse sample ##
http://localhost:8080/pluginSample/book/list

http://localhost:8080/pluginSample/book/list2

they look the same? yes! that's what I want it to.

# The magic #
## changes on your application ##
The Jmesa grails plugin is very easy to use.After install the plugin,the libs and resource are ready for you,you don't need to worry about them.

The **resources**(js,css,images) was copy to your grails application's "web-app/plugins/jmesa-0.1" folder.

The **libs** you can find it in "plugins/jmesa-0.1/lib" folder.

Almost forget one important file,the **Preferences** config file have been Customed by Jmesa plugin.the new config file could be found as "grails-app/conf/jmesa.properties" .

they look like this:
  * grails-app
    * conf
      * jmesa.properties
  * plugins
    * jmesa-0.1
      * lib
  * web-app
    * plugins
      * images
      * js
      * css

## About Perferences ##
I had the default Perferences changed.I did it for the images and style path.
```
html.imagesPath=/plugins/jmesa-${version}/images/table/
pdf.cssLocation=/plugins/jmesa-${version}/css/jmesa-pdf.css
```
The recommand way for develper to custom jmesa preferences is: change the content,but don't change the file path.If you really want the change the config file path,just modify the /plugins/jmesa-0.1/JmesaGrailsPlugin.groovy file.
```
def doWithWebDescriptor = { xml ->
        def contextParam = xml."context-param"
      contextParam[contextParam.size()-1]+{
        'context-param' {
          'param-name'('jmesaMessagesLocation')
          'param-value'('org/jmesa/core/message/jmesaResourceBundle')
        }
      }+{
        'context-param' {
          'param-name'('jmesaPreferencesLocation')
          'param-value'('/jmesa.properties')
        }
      }
    }
```

You are right,the message can be customed too.

## demos ##
There are two samples,including pageinate and export demo

### demo1 ###
In this demo,datas are paginated by jmesa.

**controller**
```
/** paginated by jmesa*/
    def list = {
        TableFacade tableFacade = new TableFacadeImpl("tag",request)
        def books = Book.list(params)
        if(!books || books.size() == 0){
            books = []
            50.times{ count ->
                def book = new Book(title:"learning Java part${count + 1}",author:"james",releaseTime:new Date())
                book.save()
                books << book
            }
        }
        tableFacade.items = books
        
        Limit limit = tableFacade.limit
        
        if(limit.isExported()){
            tableFacade.setExportTypes response,limit.getExportType()
            tableFacade.setColumnProperties "title","author"
            tableFacade.render()
        }else
            return [bookList : books]
    }
```

**view**
the gsp codes:
```
<form name="bookForm" action="/pluginSample/book/list">
                <jmesa:tableFacade
                    id="tag"
                    items="${bookList}"
                    maxRows="15"
                    exportTypes="csv,excel"
                    stateAttr="restore"
                    var="bean"
                    >
                    <jmesa:htmlTable
                        caption="books"
                        width="600px"
                        >
                        <jmesa:htmlRow>
                            <jmesa:htmlColumn property="title" performFilterAndSort="true" ><a href="#">${bean.title}</a></jmesa:htmlColumn>
                            <jmesa:htmlColumn property="author"/>
                        </jmesa:htmlRow>
                    </jmesa:htmlTable>
                </jmesa:tableFacade>

            </form>
```
the tags are the sam to the jsp tag.right?

## demo2 ##
In this demo,datas are paginated manually,we load the the data we need for the current page only.

### controller ###
```
/**
     * paginate manually
     */
    def list2 = {
        def id = "tag"
        TableFacade tableFacade = new TableFacadeImpl(id,request)
        LimitActionFactory laf = new LimitActionFactoryImpl(id,params)
        def maxRows = laf.maxRows == null ? 15:laf.maxRows
        def page = laf.page

        def books = Book.list(offset:((page - 1) * maxRows), max:maxRows)

        Limit limit = tableFacade.limit

        if(limit.isExported()){
            tableFacade.setExportTypes response,limit.getExportType()
            tableFacade.setColumnProperties "title","author"
            tableFacade.setItems Book.list()
            def count = Book.count()
            limit.setRowSelect new RowSelectImpl(1,count,count)
            tableFacade.render()
        }else{
            limit.setRowSelect new RowSelectImpl(page,maxRows,Book.count())
            return [bookList:books,limit:limit]
        }

    }
```

the view is almost the same.just append an limit attribute to facadeTag:
```
<jmesa:tableFacade
                    id="tag"
                    items="${bookList}"
                    maxRows="15"
                    exportTypes="csv,excel"
                    stateAttr="restore"
                    var="bean"
                    limit="${limit}"
                    >
....
</jmesa:tableFacade>
```

that's Jmesa Grails Plugin.have fun!