The exports will always be handled in the Controller using the TableFacade with the API.

```
TableFacade tableFacade = TableFacadeFactory.createTableFacade(id, request);
tableFacade.setColumnProperties("name.firstName", "name.lastName", "term", "career");
tableFacade.setItems(items);
tableFacade.setExportTypes(response, ExportType.CSV, ExportType.EXCEL);

Limit limit = tableFacade.getLimit();
if (limit.isExported()) {
    tableFacade.render();
    return null;
}
```

_The exports will default to 'table-data.exportType' if no caption is specified._

_One note I would like to make is I have learned from past experience that using a filter to export the table is not a good idea. The support costs are just too high with trying to make it work for different environments. The problem was you had to parse through the JSP file for the sole purpose of generating the table in the correct markup. Doing that made outputting the data in the response very difficult and buggy. It is much easier and safer to handle the exports in the controller. However, if you use the new [TableFacade](TableFacade.md) it makes working with exports pretty trivial._

#### Exports ComponentFactory Option ####

There is now an alternative way to create columns using the API. This technique is completely optional but may be nice in some situations, or you may decide this is a more natural way to create tables. Just be sure to set the table on the facade once the table is created.

```
TableFacade tableFacade = TableFacadeFactory.createTableFacade(id, request);
tableFacade.setItems(items);
tableFacade.setExportTypes(response, ExportType.EXCEL);

Limit limit = tableFacade.getLimit();

if (limit.isExported()) {

    if (limit.getExportType() == ExportType.EXCEL) {
        ExcelComponentFactory factory = new ExcelComponentFactory(tableFacade.getWebContext(), tableFacade.getCoreContext());

        Table table = factory.createTable();

        Row row = factory.createRow();
        row.addColumn(factory.createColumn("name.firstName"));
        row.addColumn(factory.createColumn("name.lastName"));
        row.addColumn(factory.createColumn("term"));
        row.addColumn(factory.createColumn("career"));

        table.setRow(row); // be sure to set the row on the table

        tableFacade.setTable(table);
    }
    
}
```

#### PDF Export ####

  * In general the PDF's has been worked on quite a bit and should work much better now.
  * The PDF export now uses a separate jmesa-pdf.css file. The PDF export was having problems with the standard stylesheet so I split it out.

The default css file is defined in the [Preferences](Preferences.md):

```
pdf.cssLocation=/css/jmesa-pdf.css
```

This means that the application will look for the jmesa-pdf.css in the css folder directly under the web application context. Remember that you can override any default setting by defining your own [Preferences](Preferences.md).

Or you could specify the css using the css attribute and set it at runtime before you call the render method.

```
String exportType = tableFacade.getLimit().getExport().getType();
if (exportType.equals(PDF)) {
    PdfView pdfView = (PdfView)tableFacade.getView();
    pdfView.setCssLocation("/css/jmesa-pdf-landscape.css");
}
tableFacade.render();
```