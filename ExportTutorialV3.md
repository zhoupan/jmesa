JMesa allows you to export your tables in PDF, Excel, and CSV.

You should also look at the [complete example](http://code.google.com/p/jmesa/source/browse/trunk/jmesaWeb/src/org/jmesaweb/controller/BasicPresidentController.java) to see the example in its full context.

What may not seem obvious at first is that you always define your exports in the Controller (or Action if using Struts), even if you are using the tag library. The other way I could have implemented exports is with a servlet filter. However, I learned from a previous project that using a servlet filter is very difficult (near impossible) to get working across all servlet containers and with all frameworks. As it turns out exporting through the Controller has turned out to be the right choice as I have had no reports of exports not working!

In addition to being a stable choice, defining the exports separately in the Controller allows for each export to be customized as desired. For instance, you can redefine the columns to add or remove specific columns based on a specific export (PDF, Excel, or CSV).

Because the exports are just an extension to the core functionality this tutorial is a complement to the [basic tutorial](BasicTutorialV3.md).

### Controller ###

Exports are always declared in the Controller.

#### Example ####

If you remember from the basic example, in its simplest form you can have a working table in just a few lines of code. The first line creates a new TableModel instance. The second line adds the items. Then lastly we create the table using the non-HTML components (Table, Row, Column).

```
TableModel tableModel = new TableModel(id, request, response);
tableModel.setItems(presidentService.getPresidents());

Table table = new Table();

Row row = new Row();
table.setRow(row);

Column firstName = new Column("name.firstName");
row.addColumn(firstName);

Column lastName = new Column("name.lastName");
row.addColumn(lastName);

Column career = new Column("career");
row.addColumn(career);

Column born = new Column("born");
row.addColumn(born);

tableModel.setTable(table);

tableModel.render();
```

With exports we also need to define what types of exports we want. So lets step back a little, add the exports, but forget about rendering the table for a second.

```
TableModel tableModel = new TableModel(id, request, response);
tableModel.setExportTypes(CSV, JEXCEL, PDFP);
```

Our table is now defined with the exports. However, at runtime we now need a way to know if we are exporting. This information is found on the TableModel with the boolean method isExporting(). So now we do a simple check to see if an export is being done.

```
if (tableModel.isExporting()) {
   tableModel.render();
   return null;
}
```

_In Spring and Struts, returning null tells the controller that the response stream is being modified directly._

By now we have our exports working but we should still add a caption and fix up the first and last name columns to display better. Doing this is exactly the same as you would for an HTML table. One thing that is not apparent though, is that the caption will also be used as the export file name. If you do not define the caption then the file name will default to "data-table" along with the proper file extension (ie: data-table.pdf).

```
Table table = new Table().caption("Presidents");

Row row = new Row();
table.setRow(row);

Column firstName = new Column("name.firstName").title("First Name");
row.addColumn(firstName);

Column lastName = new Column("name.lastName").title("Last Name");
row.addColumn(lastName);

Column career = new Column("career"));
row.addColumn(career);

Column born = new Column("born").cellEditor(new DateCellEditor("MM/yyyy"));
row.addColumn(born);
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

Also be sure to set the export on the Limit in the !Javascript back to null in the onInvokeAction(). The reason you do this is because the export does not force a page refresh and the JavaScript Limit will still think an export is being performed.

```
function onInvokeAction(id) {
    setExportToLimit(id, '');
    ...
}
```

_Be sure to read the [Javascript](Javascript.md) page so that you understand how Javascript is used to interact with the table and what your options are._

### PDF Notes: ###
  * If you want to use the Flying Saucer API to render the PDF export you need to include the jmesa-pdf.css. You also need to create a table using the HTML components (HtmlTable, HtmlRow, and HtmlColumn).
  * The PDFP does not require that you use the HTML components. So use Table, Row, and Column instead of the HtmlTable, HtmlRow, and HtmlColumn.
  * The difference between the PDF and PDFP view is that the PDF export uses the Flying Saucer API and the PDF export uses the iText API directly.
