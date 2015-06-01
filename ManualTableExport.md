_**This tutorial needs to be updated to reflect the JMesa 3.0 API.**_

This tutorial will show you how to do an export in JMesa manually without first displaying a table on your web page. The reason you might want to do this is to leverage JMesa's exporting functionality but do not have requirements to display a table in the browser.

Just recently at work I had this very same requirement and I knew I could use JMesa. The reason this works is because the Limit object was always meant to be manipulated to put the table into any state at will. In this example I create a custom Limit to do a JExcel export.

The first thing we need to do is create a new Limit object that returns all the rows and lets the API know that we want to do a JExcel export.

```
private Limit createLimit() {
    Limit limit = new LimitImpl(CONTESTS);
    int size = contestEntries.size();
    RowSelect rowSelect = new RowSelectImpl(1, size, size);
    limit.setRowSelect(rowSelect);
    limit.setExportType(ExportType.JEXCEL);
    return limit;
}
```

Then we need to invoke our TableFacade like normal. Just be sure to set the Limit on the facade so the API knows what to do.

```
private TableFacade createTableFacade() {
    TableFacade tableFacade = new CustomTableFacade(CONTESTS, request, response, drawing);
    tableFacade.setLimit(createLimit());
    tableFacade.autoFilterAndSort(false);
    tableFacade.setColumnProperties("entryNumber", "entry.firstName", "entry.lastName", "entry.address", 
                "entry.city", "entry.state", "entry.zipcode", "entry.phone", "entry.email", "entry.prize");

    Collections.sort(this.contestEntries, new BeanComparator("entryNumber", new NullComparator()));

    tableFacade.setItems(contestEntries);
    tableFacade.setExportTypes(response, ExportType.JEXCEL);

    Table table = tableFacade.getTable();

    table.setCaption(drawing.getContest().getName());

    Row row = table.getRow();

    Column firstName = row.getColumn("entry.firstName");
    firstName.setTitle("First Name");

    Column lastName = row.getColumn("entry.lastName");
    lastName.setTitle("Last Name");

    // keep formatting columns to display nicely here

    return tableFacade;
}
```

To actually do the export just invoke the table facade and call the render method like normal.

```
void export() {
    TableFacade tableFacade = createTableFacade();
    tableFacade.render();
}
```

That is really all you need to do! However, I also wanted to have a custom file name for the export so I extended the TableFacadeImpl class and passed in my own file name to use. I think a good improvement to JMesa would be to make this easier, but like most things you can customize JMesa however you need.

```
/**
 * Override the renderExport() method so that we can insert a custom file name.
 */
private static class CustomTableFacade extends TableFacadeImpl {

    private HttpServletResponse response;
    private String fileName;

    public CustomTableFacade(String id, HttpServletRequest request, HttpServletResponse response,
            ContestDrawing drawing) {

        super(id, request);
        this.response = response;

        DateTime startDate = drawing.getStartDate();
        DateTime endDate = drawing.getEndDate();
        String contestName = drawing.getContest().getName();
        fileName = contestName + "_" + startDate.toString("MM-dd-yyyy") + "_" + endDate.toString("MM-dd-yyyy") + ".xls";
    }

    @Override
    protected void renderExport(ExportType exportType, View view) {

        try {
            if (exportType == ExportType.JEXCEL) {
                new JExcelViewExporter(view, fileName, response).export();
            } else {
                super.renderExport(exportType, view);
            }
        } catch (Exception e) {
            logger.error("Not able to perform the " + exportType + " export.", e);
        }
    }
}
```