The purpose of the Limit class is to know how to limit the table results. The Limit knows how the user interacted with the table with regards to sorting, filtering, paging, max rows to display, and exporting. With this information you will be able to display the requested page filtered and sorted correctly in the most efficient manner possible. For more information see the [limit tutorial](LimitTutorialV3.md).

For instance, the Limit object can tell you what was filtered by calling the FilterSet:
```
FilterSet filterSet = limit.getFilterSet();
```

The Limit object can also tell you what was sorted by calling the SortSet:
```
SortSet sortSet = limit.getSortSet();
```

To find out if a table was exported call the boolean:
```
boolean exported = limit.hasExport();
```

Some of the more interesting information can be found on the RowSelect object:
```
RowSelect rowSelect = limit.getRowSelect();
```

With this object you can find out all sorts of information about the rows to display:
```
int rowStart = rowSelect.getRowStart();

int rowEnd = rowSelect.getRowEnd();

int maxRows = rowSelect.getMaxRows();

int totalRows = rowSelect.getTotalRows();

int page = rowSelect.getPage();
```

### The Limit Exposed ###
The real power of the Limit implementation becomes apparent when you want to put the table into a different state. Here we are adding a Sort object to sort the first name in ascending order. Then we are adding a Filter object to filter the last name. This will also force the table to go right to the second page.
```
Limit limit = new Limit(id);
limit.getSortSet().addSort("name.firstName", Order.ASC);
limit.getFilterSet().addFilter("name.lastName", "a");
limit.getRowSelect().setPage(2);
```

**Note:** If you want to persist a Limit you can use the [State](State.md) interface.