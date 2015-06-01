_Ayan Dutta from Kolkata, India wrote up the following tutorial. There is also a Struts 1.x examples war file in the [downloads](http://code.google.com/p/jmesa/downloads/list) area._

Following are few topics on which I am going to focus on.
  * Complete example of using JMesa with Struts (Specifically the Ajax enabled version), to help and encourage Struts 1.x users to use JMesa quite easily.
  * Practical example /recipe on the row rendering ( i.e. proper usage of HtmlRowRendererImpl )
  * Proper usage of sorting feature using ColumnSort with examples.
  * I have a generic comparator (I am working on this currently), which deals with some real life data type problems in terms of sorting (for example a String containing a date or number, so the sorting becomes weird) .This comparator is capable enough to handle such frequently faced real life problem scenarios.

### Complete example of using JMesa with Struts (With Ajax ): ###

First I will setup an application configuration so that Struts 1.x and JMesa works together. Then I will write an action class JmesaDispatchAction.java, which will have a method load(). This load() method is everything.

I am using DispatchAction here, intentionally, so that if one wants same action to work for multiple JMesa table less struts mapping and code would be required.

#### Setup ####

Create the structure for any standard struts project. Now place JMesa libraries (i.e. jar files ) inside WEB-INF/lib.

Place jmesa.properties inside WEB-INF. Inside web.xml create its entry like the following:

```
<context-param>
    <param-name>jmesaPreferencesLocation</param-name>
    <param-value>WEB-INF/jmesa.properties</param-value>
</context-param>
```

Place JMesa related css,js and image files inside css,js and images folders respectively, all these folders are under the folder WebContent.

![http://jmesa.org/wiki/images/struts-project-setup.png](http://jmesa.org/wiki/images/struts-project-setup.png)

#### Code the Action class and the JSP: ####

The action class will have two basic blocks inside the load() method:
  * one for normal flow
  * another for Ajax request from the JSP page (with request parameter ajax=true).

Here comes the sample code block...

```
public ActionForward load(ActionMapping mapping
           , ActionForm form
           , HttpServletRequest request
           , HttpServletResponse response) throws Exception {
    String forwardName="load";
    List items = JMesaUtil.loadData(100);
    String id="presidents";
    TableFacade tableFacade = createTableFacade(id, request);
    tableFacade.setItems(items); // set the items
    tableFacade.setStateAttr("restore"); // return to the table in the same state that the user left it.
    String html = getHtml(tableFacade,id);
    // Setting a parameter to signal that this is an Ajax request.
    String ajax = request.getParameter("ajax");
    if (ajax != null && ajax.equals("true"))
    {
         byte[] contents = html.getBytes();
         response.getOutputStream().write(contents);
         return null;
    }
    else
    { // Not using Ajax if invoke the controller for the first time.
         request.setAttribute("presidents", html);
    }
    return mapping.findForward(forwardName);
}
```

In the JSP page named “jmesaStrutsAjaxGrid.jsp” (location WebContent/jmesapages) pay attention to the JavaScript method onInvokeAction , the main Ajaxifying trick is present there.

```
function onInvokeAction(id)
{
    var parameterString = $.jmesa.createParameterStringForLimit(id);
    $.get('${pageContext.request.contextPath}/loadData.do?method=load&ajax=true&' + parameterString, 
        function(data) {
            $("#presidents").html(data)
    });
}
```

### Row Rendering Recipe: ###

Suppose you want to highlight a particular row with a specific color for a specific value,
this is the how you would do it, Read on...

This is an ideal case for using a Custom HtmlRowRenderer which will extend
HtmlRowRendererImpl.

Now we would need to override the default public Object render (Object item, int
rowcount) method here for our specific requirement.

In this case the data list contains a DTO(Data Transfer Object) object which has a boolean
attribute “ok”. If its has a “true” value, we need to highlight the row with green color.

Here's the code for it

```
public class CustomHtmlRowRenderer extends HtmlRowRendererImpl
{
    public Object render(Object item, int rowcount)
    {
        HtmlBuilder html = new HtmlBuilder();
        html.tr(1);
        html.id(getCoreContext().getLimit().getId() + "_row" + rowcount);
        //Get the actual value of ok field
        Object okValueObj = ItemUtils.getItemValue(item, "ok");
        String valueStr = String.valueOf(okValueObj).toLowerCase();
        if (valueStr.contains("true"))
        {
            html.style("background-color:green");
        }
        else
        {
            html.style(getStyle());
        }
        html.styleClass(getStyleClass(rowcount));
        html.append(getRowEvents(item, rowcount));
        html.close();
        return html.toString();
    }
}
```

Now the Tablefacade object needs to be informed about this custom renderer. We do this using the following lines of code in the getHtml method.

```
HtmlTable table = (HtmlTable) tableFacade.getTable();
HtmlRow row = table.getRow();
/*
 * Override and customize the rendering of the table facade
 */
CustomHtmlRowRenderer customHtmlRowRenderer = new CustomHtmlRowRenderer();
row.setRowRenderer(customHtmlRowRenderer);
```

Here's how the grid was like before the row rendering customization

![http://jmesa.org/wiki/images/struts-rendering_before.png](http://jmesa.org/wiki/images/struts-rendering_before.png)

After row rendering customization

![http://jmesa.org/wiki/images/struts-rendering_after.png](http://jmesa.org/wiki/images/struts-rendering_after.png)

### Custom Sorting Recipe: ###

How to sort a table according to my own choice using Jmesa ?

Jmesa comes with its own nice way of sorting. But for specific custom sorting of your own, you need to write your own “customized” Sorting class. It will be a class that will implement ColumnSort interface and implement the sortItems method of the said interface.

Remember sortItems () method is the place where you apply all sorting related
customizations.

The code is written below to sort the data on two properties age and displaydate.

```
public class CustomColumnSort implements ColumnSort
{
    public Collection<?> sortItems(Collection<?> items, Limit limit)
    {
        ComparatorChain chain = new ComparatorChain();
        SortSet sortSet = limit.getSortSet();
        Iterator sortItr= sortSet.getSorts().iterator();
        while(sortItr.hasNext())
        {
            Sort sort =(Sort)sortItr.next();
            GenericComparator genericComparator = null;
            if (sort.getOrder() == Order.ASC)
            {
                if(sort.getProperty().equalsIgnoreCase("age"))
                {
                    genericComparator= new GenericComparator(sort.getProperty(),true,GenericComparator.NUMERIC_STRING);
                }
                if(sort.getProperty().equalsIgnoreCase("displayDate"))
                {
                    genericComparator= new GenericComparator(sort.getProperty(),true,GenericComparator.DATE_STRING);
                }
            }
            else if (sort.getOrder() == Order.DESC)
            {
             /*
              * For descending sort one extra parameter sorting mode (value false)
              * is passed to the GenericComparator.
              * By default its value is true.
              *
              */
                if(sort.getProperty().equalsIgnoreCase("age"))
                {
                    genericComparator= new GenericComparator(sort.getProperty(),true,GenericComparator.NUMERIC_STRING,false);
                }    
                if(sort.getProperty().equalsIgnoreCase("displayDate"))
                {
                    genericComparator= new GenericComparator(sort.getProperty(),true,GenericComparator.DATE_STRING, false);
                }
            }
             /*
              * This is the main place where the Generic comparator is added to the
              * ComparatorChain for sorting
              *
              */
              chain.addComparator(genericComparator);
        }
        if (chain.size() > 0)
        {
            Collection((List<?>) items, chain);
        }
        return items;
    }
}
```

Now we need to inform the HtmlTable object about this custom sort object. Inside getHtml we do this using the following lines of code.

```
/*
 * Override and customize the sorting of the table facade
 */
ColumnSort customColumnSort = new CustomColumnSort();
tableFacade.setColumnSort(customColumnSort);
```

### Handle real life sorting issues with Generic Comparator: ###

There are times when you'd want to write one and only comparator which will handle all java data types for sorting (sounds interesting!! read on further..). Even if you have that kind of generic comparator, there are some situations where even that generic comparator will fail. It may sound strange, but I am going to show you such situations, arising out of the overuse of String for specifying other data types, especially faced by Struts developers.

For example you have a list of DTO objects which you want to sort. Now the DTO has a displaydate field which is of type String but displays a Date. Now when the user sorts on date and expects the data sorted by date, the user would be in for a big surprise! Since the date data is actually a String value.

I would call these particular field types as hybrid data type, just for the sake of our convenience.

We can say displaydate had shown date, but it is of type string, so it is a hybrid data type and it of type Date String. Similarly we may face problems with Numeric String, Boolean String, etc.

Here comes my brain child custom Generic Comparator. Just inform the comparator about the hybrid data type and let it do its sorting job.

Inside sorItems method of CustomColumnSort I have used generic comparator.

For proper java data type you just need to create the comparator like

```
GenericComparator comparator= new GenericComparator(<String fieldNameToBeSorted>)
```

For hybrid java data type you need to create the comparator like

```
public GenericComparator(<String fieldNameToBeSorted>,<boolean hybridDataType>,<String hybridDataTypeName>)
```

Inside CustomColumnSort I have shown how to deal with hybrid data type like the following

```
GenericComparator genericComparator= new GenericComparator(sort.getProperty(),true,GenericComparator.NUMERIC_STRING);
```

If it was a normal data type the code would have been like

```
GenericComparator genericComparator= new GenericComparator(sort.getProperty());
```