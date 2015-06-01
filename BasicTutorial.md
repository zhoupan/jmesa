The following is a walk-through of an example of creating a JMesa table using the [API](TableFacade.md) directly.

You should also look at the [complete example](BasicExample.md) to see the example in its full context. Or look [here](Examples.md) to see all the examples.

**_Note: If you are using JMesa 3.0 you should be using the [new syntax](BasicTutorialV3.md)._**

### Setup ###
To start with grab the latest JMesa [download](http://code.google.com/p/jmesa/downloads/list). In there you will find the following: jmesa.jar, jmesa.css, jmesa-pdf.css, jquery.jmesa.js, jmesa.js, default images, and the source code.

The first thing to do is take the jmesa.jar file and place it in your projects WEB-INF/lib directory. Next copy the jmesa.css and jmesa-pdf.css into a folder named css at the top of the web project. Then copy the jquery.jmesa.js and jmesa.js into a folder named js at the top of the web project. You will also want to grab the latest version of [jQuery](http://www.jquery.com) as JMesa requires that library now. Lastly, copy the default images into a folder named images/table at the top of the web project.

Of course all these files could be placed where ever you want. The places I have been recommending are pretty common, but you could use whatever you want. Here is a visual for you to look at.

  * web
    * css
      * jmesa.css
    * js
      * jmesa.js
    * images
      * table
        * clear.gif
        * filter.gif
        * etc...
    * WEB-INF
      * lib
        * jmesa.jar

Moving these files around to other locations does not require any configuration on the part of JMesa, with the exception of the images. If you would like to use something other than the default images/table directory you need to set up your [Preferences](Preferences.md) and override the html.imagesPath attribute.

```
html.imagesPath=/images/table/
```

### Controller ###
You will always build JMesa tables in the controller when using the API directly. In the Spring framework the controller corresponds with the Controller interface, and in Struts it is called the Action. JMesa is very generic and will work in any type of servlet controller. In fact I like to think of a JMesa table as just another form element. The reason is because it is always placed inside an html form and renders as a component in the same manner as an input field, or dropdown list. It is easy to think it is something more than that, but if you keep in mind the simplicity of it it may help you conceptualize what is happening. In addition I recommend doing a view source on the rendered output to get comfortable with what JMesa is doing.

#### Example ####

In its simplest form you can have a working table in just four lines of code. The first line creates a new TableFacade instance. The second line defines the columns that correspond with the item properties. The third line adds the items. Then finally the table is rendered! You now have a fully functional table that incorporates pagination, sorting, and filtering.

```
TableFacade tableFacade = TableFacadeFactory.createTableFacade(id, request);
tableFacade.setColumnProperties("name.firstName", "name.lastName", "term", "career", "born");
tableFacade.setItems(items);
String html = tableFacade.render();
```


![http://www.jmesa.org/wiki/images/api-example-before.gif](http://www.jmesa.org/wiki/images/api-example-before.gif)

#### Modifications ####

However, as great as this is, it is still not perfect. Here is a list of the changes that we need:
  * It would be nice to have a caption describing the table.
  * There should be a fixed width on the table to ensure it fits correctly on the page.
  * The first two column titles do not display very well.
  * The last column is a date and is not formatted.
  * The first column should link off to a page with more information about that row.

##### Table Caption #####

The first thing we will tackle is adding the table caption. To do that we need to pull the table from the facade. Because we know that this is an html table we first cast it to a HtmlTable. Now we can easily define a caption. _Note: if you want the caption to be Locale specific then use the setCaptionKey() method instead._

```
HtmlTable table = (HtmlTable) tableFacade.getTable();
table.setCaption("Presidents");
```

##### Table Width #####

The width of a table is on the renderer.

```
table.getTableRenderer().setWidth("600px");
```

##### Column Titles #####

We now need to fix the titles of the first two columns. The reason the other column titles display correctly is because the default column title is defined by taking the column property and converting it to a properly formatted title. Or, in other words, it converts a camelcase word to a proper title. The first two columns have nested properties that make it impossible to clean up.

Fixing this is easy. Just pull each column and set the title manually.  _Note: if you want the title to be Locale specific then use the setTitleKey() method instead._

```
HtmlRow row = table.getRow();

HtmlColumn firstName = row.getColumn("name.firstName");
firstName.setTitle("First Name");

HtmlColumn lastName = row.getColumn("name.lastName");
lastName.setTitle("Last Name");
```

##### Column Formatting #####

To format a column you need to add a custom cell editor that knows how to format dates. Luckily JMesa includes an editor called the DateCellEditor, and only requires a format in the constructor.

```
Column born = row.getColumn("born");
born.getCellRenderer().setCellEditor(new DateCellEditor("MM/yyyy"));
```

So that gets us displaying correctly, but in this case looks aren't everything! We also need to filter the column. For that we need to make sure the filter mechanism knows how to format this column as well so that it can successfully find a match.

To do that add a custom filter matching strategy. Although, what may trip you up somewhat is that you need to define this before call the table. The reason is because when you request the table from the facade the facade will [invoke all the objects](TableFacadeCallOrder.md) it needs internally.

Its real simple to get this right though. Just set the filter matcher before you ask for the table. In practice its usually the most clear if you just set all the custom things you need when you create the TableFacade.

```
tableFacade.addFilterMatcher(new MatcherKey(Date.class, "born"), new DateFilterMatcher("MM/yyyy"));
```

##### Column Link #####

The last thing we need to do is add a column link. The way we do this is by adding a custom [CellEditor](Editors.md). The [Editors](Editors.md) article describes this pretty well so I will just let you read more about this there.

```
// Using an anonymous class to implement a custom editor.
firstName.getCellRenderer().setCellEditor(new CellEditor() {
    public Object getValue(Object item, String property, int rowcount) {
        Object value = new BasicCellEditor().getValue(item, property, rowcount);
        HtmlBuilder html = new HtmlBuilder();
        html.a().href().quote().append("http://www.whitehouse.gov/history/presidents/").quote().close();
        html.append(value);
        html.aEnd();
        return html.toString();
    }
});
```


#### Modifications Finished ####

Thats it! We now have a fully functional, and good looking, HTML table!

![http://www.jmesa.org/wiki/images/api-example-after.gif](http://www.jmesa.org/wiki/images/api-example-after.gif)

#### Pass Html To View ####

Once you have the HTML rendered you need to pass the markup to your view technology. This is one of the strengths of rendering your table in the Controller...you can use any view technology to display tables.

```
String html = tableFacade.render();
request.setAttribute("presidents", html);
```

### View ###

In my example I am using JSP to display the table. What the following code is doing is rendering the table inside a form element. The basic.run is what I do to route the request back through the Controller. Generally your form action is going to be the same action that you did to get the table to display in the first place.

In many ways I think it is helpful to think of JMesa as a form component, more like an input or select field. It does not automatically include a form element or deal with parameters passed from other JSP pages. Doing this increases the flexibility quite a bit.

```
<form name="presidentsForm" action="${pageContext.request.contextPath}/basic.run">
    ${presidents}
</form>
```

The last thing we need to do is wire up the JMesa actions to the jmesa.js script. The actions, of course, are the filtering, sorting, paging, and max rows. Most of the time you will just need to create hidden input fields and submit the form, and is what we are doing here.

```
<script type="text/javascript">
function onInvokeAction(id) {
    createHiddenInputFieldsForLimitAndSubmit(id);
}
</script>
```

_Be sure to read the [Javascript](Javascript.md) page so that you understand how Javascript is used to interact with the table and what your options are._