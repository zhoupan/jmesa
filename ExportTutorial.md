JMesa allows you to export your tables in PDF, Excel, and CSV.

What may not seem obvious at first is that you always define your exports in the Controller (or Action if using Struts), even if you are using the tag library. The other way I could have implemented exports is with a servlet filter. However, I learned from a previous project that using a servlet filter is very difficult (near impossible) to get working across all servlet containers and with all frameworks. As it turns out exporting through the Controller has turned out to be the right choice as I have had no reports of exports not working!

In addition to being a stable choice, defining the exports separately in the Controller allows for each export to be customized as desired. For instance, you can redefine the columns to add or remove specific columns based on a specific export (PDF, Excel, or CSV).

Because the exports are just an extension to the core functionality this tutorial is a complement to the [basic tutorial](BasicTutorial.md).


**_Note: If you are using JMesa 3.0 you should be using the [new syntax](ExportTutorialV3.md)._**

### Controller ###

Exports are always declared in the Controller.

#### Example ####

If you remember from the basic example, in its simplest form you can have a working table in just four lines of code. The first line creates a new TableFacade instance. The second line defines the columns that correspond with the item properties. The third line adds the items. Then finally the table is rendered!

```
TableFacade tableFacade = TableFacadeFactory.createTableFacade(id, request);
tableFacade.setColumnProperties("name.firstName", "name.lastName", "term", "career", "born");
tableFacade.setItems(items);
String html = tableFacade.render();
```

With exports we need to first define what types of exports we want. So lets step back a little, add the exports, but forget about rendering the table for a second.

```
TableFacade tableFacade = TableFacadeFactory.createTableFacade(id, request);
tableFacade.setColumnProperties("name.firstName", "name.lastName", "term", "career", "born");
tableFacade.setItems(items);
tableFacade.setExportTypes(response, ExportType.CSV, ExportType.EXCEL, ExportType.PDF);
```

Our table is now defined with the exports. However, at runtime we now need a way to know if we are exporting, or rendering an HTML table. This information is found on the Limit with the boolean method isExported(). So now we do a simple check to see if an export is being done. If it is we create the export table. If not then we create the HTML table and render that.

```
Limit limit = tableFacade.getLimit();
if (limit.isExported()) {
    tableFacade.render();
    return null;
} 

String html = tableFacade.render();
request.setAttribute("presidents", html);
```

_In Spring and Struts, returning null tells the controller that the response stream is being modified directly._

By now we have our exports working but we should still add a caption and fix up the first and last name columns to display better. Doing this is exactly the same as you would for an HTML table. One thing that is not apparent though, is that the caption will also be used as the export file name. If you do not define the caption then the file name will default to "data-table" along with the proper file extension (ie: data-table.pdf).

```
Table table = tableFacade.getTable();
table.setCaption("Presidents");

Row row = table.getRow();

Column firstName = row.getColumn("name.firstName");
firstName.setTitle("First Name");

Column lastName = row.getColumn("name.lastName");
lastName.setTitle("Last Name");
```


The last thing we need to do for an export is write it out to the servlet response stream. This is done when the call to render the table is done.

```
tableFacade.render();
```

### View ###

The last thing we need to do is wire up the export to the jmesa.js script in your view. Most of the time you will just need to create the parameter string and go through your Controller, and is what we are doing here.

```
<script type="text/javascript">
function onInvokeExportAction(id) {
    var parameterString = createParameterStringForLimit(id);
    location.href = '${pageContext.request.contextPath}/presidents.run?' + parameterString;
}
</script>
```

Also be sure to set the export on the Limit in the JavaScript back to null in the onInvokeAction(). The reason you do this is because the export does not force a page refresh and the JavaScript Limit will still think an export is being performed.

```
function onInvokeAction(id) {
    setExportToLimit(id, '');
    ...
}
```

_Be sure to read the [Javascript](Javascript.md) page so that you understand how Javascript is used to interact with the table and what your options are._