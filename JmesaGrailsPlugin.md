This will describe the usage of the JMesa Grails plugin by including it into the sample web application. The plugin is designed to be identical to the JSP tag library. If you are new to the JMesa tags see the [tags](JSPTags.md) page.

# Setup #

## Install Grails ##

Make sure you have Grails 1.0.2 (or higher) installed. If you are new to Grails, see the [installation guide](http://grails.org/Installation) for details.

## Get Sample Web Application ##

  1. Check out the latest soure from SVN repository.
```
 svn checkout http://jmesa.googlecode.com/svn/trunk/jmesaGrailsPluginWeb
```

## Install jmesa plugin ##

```
     $ cd /path/to/jmesaGrailsPluginWeb
     $ grails install-plugin /path/to/grails-jmesa-0.8.zip
```

## Run sample ##

```
 $ grails run-app
```

## Browse sample ##

http://localhost:8080/pluginSample/book/list

http://localhost:8080/pluginSample/book/list2

http://localhost:8080/pluginSample/book/list3

# The magic #

## changes on your application ##

The JMesa grails plugin is very easy to use. After you install the plugin the libs and resources are ready so you do not need to worry about them.

  * The **resources** (js,css,images) were copied to your grails application's "web-app/plugins/jmesa-0.8" folder.

  * The **libs** you can find it in "plugins/jmesa-0.8/lib" folder.

Keep in mind one important thing. The **Preferences** properties file has been customed by the JMesa Grails plugin. The new properties file could be found as "grails-app/conf/jmesa.properties" .

The structure looks like this:
  * grails-app
    * conf
      * jmesa.properties
  * plugins
    * jmesa-0.8
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

The recommended way for the developer to customize the jmesa Preferences is to change the content, but do not change the file path. If you really want to change the Preferences file path, just modify the /plugins/jmesa-0.8/JmesaGrailsPlugin.groovy file.
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

Keep in mind that the Message (Locale config) can be customized too.

## The Demos ##

There are three demo's:

### Demo 1 ###

In this demo, data is paginated by JMesa.

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

Notice how the GSP tags are the same as the JSP tags!

## Demo 2 ##

In this demo, data is paginated manually. We load only the the data that we need for the current page.

### Controller ###
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

### View ###

The view is almost the same as the last demo. Just append a Limit attribute to TableFacade:
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