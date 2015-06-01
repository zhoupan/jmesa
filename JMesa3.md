The real change with JMesa 3.0 is to get one clear syntax, and have it so that the order that things get declared does not matter. So let me explain...

I was at a crossroad recently. I knew that I needed to update the documentation on the site, but I also knew that the API in its current form is too hard to document. The reason is that there are so many ways to work with the API now.

But thanks to the 2.5X releases it seemed so obvious on how to get JMesa to what the original vision was. Where I feel that things went off track was when I understood that the order of interacting with the TableFacade mattered too much. That was never my intention and I was not sure what to do about it. But then the template code was introduced and it became obvious on what needed to be done. Plus through the releases we found better ways to build (and work with) tables such as the fluent pattern, support interfaces, etc.... This was the chance to incorporate all that now.

So basically what I have done is take the TableFacade and move it down the chain (meaning it does not get interacted with directly). Then our new class (TableModel) handles the work of running things in the correct order.

But this has in no way been a complete rewrite (more of a re-tweak). I just wanted to get the front end syntax to what it should be, and have it so that you only worry about what you want to do, not how JMesa needs it to be declared. I still really like all the interfaces and implementations. So all of your existing custom classes will work perfectly as nothing is changing at that level of the API. And it was a design goal that everything be backwards compatible so you can move your tables over one by one as you get time.

The other thing I wanted to do (and you can see in the example below) is not make developers care that things like the CellEditor is actually added to the renderer. I mean technically it would still be, but there is no reason why the CellEditor could not just have a method on the column, and then pass through to the editor. This is something that the tag library does and it looks cleaner.

For those using the tag library even less will change. It will just be a name change from tableFacade to tableModel (just to keep the naming the same as the API).

For those using the Limit to pull back one page at a time at minimum you will have a callback functionality so that using the Limit is as easy as it is with the new template code. This is what we have now:

```
tableModel.setPageResults(new PageResults() {
    public int getTotalRows(Limit limit) {   
    }

    public Collection<?> getItems(Limit limit) {
    }
});
```

_For the tag library there is a corresponding TableModelUtils.getItems() method. This is not only convenient, but also takes care of passing the Limit to the tags automatically._

### Example ###

The following is a straightword example, but note that what is cool about the new code is how clean it is, and the fact that order does not matter at all! Granted, it is not wildly different from what it is today, but it is so much cleaner.

```
TableModel tableModel = new TableModel("id", request);
tableModel.setItems(items);
tableModel.addFilterMatcher(new MatcherKey(Date.class, "born"), new DateFilterMatcher("MM/yyyy"));

HtmlTable table = new HtmlTable();
table.setCaption("Presidents");
table.setWidth("600px");

Row row = new HtmlRow();

Column firstName = new HtmlColumn("name.firstName");
firstName.setTitle("First Name");
firstName.setCellEditor(new CellEditor() {
    public Object getValue(Object item, String property, int rowcount) {
        Object value = new BasicCellEditor().getValue(item, property, rowcount);
        HtmlBuilder html = new HtmlBuilder();
        html.a().href().quote().append("http://www.whitehouse.gov/history/presidents/").quote().close();
        html.append(value);
        html.aEnd();
        return html.toString();
    }
});
row.addColumn(firstName);

Column lastName = new HtmlColumn("name.lastName");
lastName.setTitle("Last Name");
row.addColumn(lastName);

Column term = new HtmlColumn("term");
row.addColumn(term);

Column career = new HtmlColumn("career");
row.addColumn(career);

Column born = new HtmlColumn("born");
born.setCellEditor(new DateCellEditor("MM/yyyy"));
row.addColumn(born);

table.setRow(row);

tableModel.setTable(table);

String html = tableModel.render();
```

### Fluent Example ###

Same as above but shows how the columns will use the fluent syntax. We could really use the fluent syntax on the model as well, but I am not sure if that makes as much sense.

```
TableModel tableModel = new TableModel("id", request);
tableModel.setItems(items);
tableModel.addFilterMatcher(new MatcherKey(Date.class, "born"), new DateFilterMatcher("MM/yyyy"));

HtmlTable table = new HtmlTable().caption("Presidents").width("600px");

Row row = new HtmlRow();

Column firstName = new HtmlColumn("name.firstName");
firstName.setTitle("First Name");
firstName.setCellEditor(new CellEditor() {
    public Object getValue(Object item, String property, int rowcount) {
        Object value = new BasicCellEditor().getValue(item, property, rowcount);
        HtmlBuilder html = new HtmlBuilder();
        html.a().href().quote().append("http://www.whitehouse.gov/history/presidents/").quote().close();
        html.append(value);
        html.aEnd();
        return html.toString();
    }
});
row.addColumn(firstName);

Column lastName = new HtmlColumn("name.lastName").title("Last Name");
row.addColumn(lastName);

Column term = new HtmlColumn("term");
row.addColumn(term);

Column career = new HtmlColumn("career");
row.addColumn(career);

Column born = new HtmlColumn("born").cellEditor(new DateCellEditor("MM/yyyy"));
row.addColumn(born);

table.setRow(row);

tableModel.setTable(table);

String html = tableModel.render();
```

### Column Properties ###

I was going to completely leave out the building of tables by starting with column properties, but I could still see it being very convenient. So instead I moved this functionality into the TableModelUtils class.

```
private Table getExportTable() {
    Table table = TableModelUtils.createTable("name.firstName", "name.lastName", "career", "born");
    table.caption("Presidents");

    Row row = table.getRow();

    row.getColumn("name.firstName").title("First Name");
    row.getColumn("name.lastName").title("Last Name");
    row.getColumn("career").filterEditor(new DroplistFilterEditor());
    row.getColumn("born").cellEditor(new DateCellEditor("MM/yyyy"));

    return table;
}

private Table getHtmlTable() {
    HtmlTable htmlTable = TableModelUtils.createHtmlTable("name.firstName", "name.lastName", "career", "born");

    HtmlRow htmlRow = htmlTable.getRow();

    HtmlColumn firstName = htmlRow.getColumn("name.firstName").title("First Name");
    firstName.setCellEditor(new CellEditor() {
        public Object getValue(Object item, String property, int rowcount) {
            Object value = new HtmlCellEditor().getValue(item, property, rowcount);
            HtmlBuilder html = new HtmlBuilder();
            html.a().href().quote().append("http://www.whitehouse.gov/history/presidents/").quote().close();
            html.append(value);
            html.aEnd();
            return html.toString();
        }
    });

    htmlRow.getColumn("name.lastName").title("Last Name");
    htmlRow.getColumn("career").filterEditor(new DroplistFilterEditor());
    htmlRow.getColumn("born").cellEditor(new DateCellEditor("MM/yyyy"));

    return htmlTable;
}


```