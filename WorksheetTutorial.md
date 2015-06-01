A JMesa Worksheet is just how it sounds...you are able to edit values in the table body and have those changes persisted. You are also able to have those changes be reflected as you sort, filter and page and then only be saved when the user is ready to save the changes. The really slick thing about this feature is how natural JMesa can handle it.

At a high level the way it works is changes to the table cell values get persisted in the Worksheet object as the user is making changes (via ajax call). These changes will persist even while filtering, sorting, and paging. Once the user is ready to save the changes they will have to click the (new) save toolbar button. Technically all this does is update a runtime flag ( isSaving() ) on the Worksheet so that you can tell they clicked the button. Then it is up to you to go through the Worksheet, retrieve the changes, and then persist them however you want. If there are errors then you can also add an error to a specific Worksheet column cell and have that redisplay in a way that demonstrates the save did not work for that column cell. If everything looks good then you remove that specific column cell change.

Do keep in mind though that at the point where you are saving the Worksheet the interaction is very manual. You have to retrieve the changes from the Worksheet, do what you need to save the changes, and then remove the column. This is all by design though! What the Worksheet does is keep track of all the changes made, so, just to be clear, the Worksheet only contains the changes.

In addition to being able to save a Worksheet you also have the ability to filter an editable table to only show the changes. To do this the user will have to click on the (new) filter worksheet toolbar button. _Note: if you are using the Limit object to only retrieve one page at a time you will need to code for this worksheet filtering._

Because the worksheet is just an extension to the core functionality this tutorial is a complement to the [basic tutorial](BasicTutorial.md).

![http://jmesa.org/wiki/images/worksheet-save.gif](http://jmesa.org/wiki/images/worksheet-save.gif)

_Also see the [worksheet example](WorksheetExample.md) for more information._

### Setup ###

First you need to set up the servlet in the web.xml file. The servlet is used with AJAX technology to update the Worksheet as the user is interacting with the table. That way there are no page refreshes for the current page someone is editing.

```
<servlet>
    <servlet-name>worksheet</servlet-name>
    <servlet-class>org.jmesa.worksheet.servlet.WorksheetServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>worksheet</servlet-name>
    <url-pattern>*.wrk</url-pattern>
</servlet-mapping>
```

### Setting Worksheet To Be Editable ###

Setting a table to be editable is very easy. To start with you have to set the TableFacade to be editable. This tells JMesa to decorate the CellEditor with the WorksheetEditor. The only other thing you need to do is set the row unique property to be the property that can be used to identify the row. Typically this is also the unique identifier in the database.

#### API ####

Then set the table to be editable:

```
tableFacade.setEditable(true);

// the unique worksheet property to identify the row
row.setUniqueProperty("id"); 
```

To get a checkbox using the API:

```
HtmlColumn chkbox = row.getColumn("chkbox");
chkbox.getCellRenderer().setWorksheetEditor(new CheckboxWorksheetEditor());
chkbox.setTitle("&nbsp;");
chkbox.setFilterable(false);
chkbox.setSortable(false);
```

#### tags ####

```
<jmesa:tableFacade editable="true" >
   <jmesa:htmlRow uniqueProperty="id">

   </jmesa:htmlRow>
</jmesa:tableFacade>
```

To get a checkbox using the tags:

```
 <jmesa:htmlColumn property="chkbox" title="&nbsp;" worksheetEditor="org.jmesa.worksheet.editor.CheckboxWorksheetEditor" filterable="false" sortable="false"/>
```

### Saving Worksheet ###

Remember that saving a worksheet is custom for every table. What the Worksheet offers you is the ability to see what the user changed on the Worksheet.

The first step is to see if the user is saving the worksheet, by clicking on the save worksheet toolbar button, and verifying that the worksheet even has changes to save.

```
TableFacade tableFacade = TableFacadeFactory.createTableFacade(id, request);
tableFacade.setEditable(true);

Worksheet worksheet = tableFacade.getWorksheet();
if (!worksheet.isSaving() || !worksheet.hasChanges()) {
    return;
}
```

The next step is to retrieve all of the domain objects that correspond with the changes. The easiest way to get this list is by using some worksheet utilities to get the property name and values. Then, in my example, I return a list of maps that are keyed by the unique value and have a value of the domain object. This makes it easy to find and update the domain objects to save them back to the database.

```
String uniquePropertyName = WorksheetUtils.getUniquePropertyName(worksheet);
List<String> uniquePropertyValues = WorksheetUtils.getUniquePropertyValues(worksheet);
final Map<String, President> presidents = presidentService.getPresidentsByUniqueIds(uniquePropertyName, uniquePropertyValues);
```

To iterate over the worksheet rows use the WorksheetCallbackHandler. This interface will pass back each row in the worksheet and then remove the columns and rows as things are updated.

```
worksheet.processRows(new WorksheetCallbackHandler() {
    public void process(WorksheetRow worksheetRow) {
    }
}
```

Once in the WorksheetCallbackHandler you will want to do the validation and then save each row.

```
Collection<WorksheetColumn> columns = worksheetRow.getColumns();
for (WorksheetColumn worksheetColumn : columns) {
    String changedValue = worksheetColumn.getChangedValue();

    validateColumn(worksheetColumn, changedValue);
    if (worksheetColumn.hasError()) {
        continue;
    }

     // do the custom saving
}
```

Note, if the column does not contain any errors then be sure to remove any previous errors (as shown below). If you forget to do this your column will be saved but when the JMesa table displays the table will still appear to have errors.
```
private void validateColumn(WorksheetColumn worksheetColumn, String changedValue) {
    if (changedValue.equals("foo")) {
        worksheetColumn.setErrorKey("foo.error");
    } else {
        worksheetColumn.removeError(); // remove previous errors
    }
}
```

To be complete this is my implementation of saving the rows. There are probably many more elegant ways to process this information, but I just wanted to condense my example in one place.

```
String uniqueValue = worksheetRow.getUniqueProperty().getValue();
President president = presidents.get(uniqueValue);
String property = worksheetColumn.getProperty();

try {
    if (worksheetColumn.getProperty().equals("selected")) {
        if (changedValue.equals(CheckboxWorksheetEditor.CHECKED)) {
            PropertyUtils.setProperty(president, property, "y");
        } else {
            PropertyUtils.setProperty(president, property, "n");
        }

    } else {
        PropertyUtils.setProperty(president, property, changedValue);
    }
} catch (Exception ex) {
    throw new RuntimeException("Not able to set the property [" + property + "] when saving worksheet.");
}
        
presidentService.save(president);
```

### Custom Functionality ###

You can read about more custom ways to modify the worksheet in the [recipes](WorksheetRecipes.md) page. One common modification is to display the save button under the table and remove the save and/or filter button from the toolbar.

### Known Issues ###
  * The filter dropdown feature does not work with the Worksheet in IE 6 or 7. The problem is the select element does not float over the worksheet cells.

### FAQ ###
##### How come my errors are not showing up on my table. #####
  * The worksheet will not function correctly if you do a redirect back to the table when there are errors. Just remember that a JMesa table is like an advanced form component so normal form submit rules apply. In this case there are some key attributes that are not getting passed along when doing a redirect.