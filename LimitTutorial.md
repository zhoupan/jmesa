The default way that JMesa deals with [items](Items.md) is to take everything and then perform the filtering and sorting. The nice thing about this is you can get the filtering, sorting, and pagination for free. That works great for small to medium result sets, but starts to fail pretty miserably when the results are large. It really is a judgement call, but I prefer to let profiling make my technical decisions. If you are concerned that you have a performance problem then the best thing to do is put a profiler on it and see for yourself. There are many open source and commercial profilers available to help you make the best decision. So, lets assume that we have discovered a performance problem and need to deal with the pagination ourselves.

To handle the pagination manually means that you only pull back enough results to fit on one page. At the same time you will need to deal with the filtering, sorting and pagination yourself. For the following discussion I am assuming that you are getting the [items](Items.md) from a database and not some other mechanism. Of course the same principles apply no matter what you do so you should easily be able to adapt your code.

Now here is the important part. To get a smaller result set you would create a query pulling data like normal, but limit the results that you get back. Every database has some command that allows you to limit the results retrieved from the database. For MySQL the command is limit, for Oracle it is between, for Sybase it is rowcount and for SQL Server it is top. If you are starting to follow my line of thinking what I am saying is when the user first goes to the table have the query only bring back the first page of result. If the user goes to the next page then return the next page of results.

The savy Sybase or SQL Server developer may be saying, but the rowcount command always starts at the beginning, so when I go to page two I have to start at the beginning. Well, yes, but now you have only returned two pages of results, as opposed to the whole result set. When you go to page three then you will only have pulled back three pages. Other databases, such as MySQL and Oracle, allow you to ask for a given section of rows so then you consistently only pull back the page that you need.

To find out how the user is trying to sort and filter, what page they are going to, and how many results they need to see JMesa has a convenient interface called the [Limit](Limit.md) and is pulled from the [TableFacade](TableFacade.md).

### Example ###

Using the Limit to efficiently pull data from the database only requires you to declare and use the TableFacade. This means that if you are using the JSP Tag library the following example is the minimum that you will need to do. If you are using API then just integrate this code with what you are already doing.

### Controller ###

First create a TableFacade and then pull the Limit.

```
TableFacade tableFacade = TableFacadeFactory.createTableFacade(id, request);
Limit limit = tableFacade.getLimit();
```

The next thing you need to do is find out how the user interacted with the filters on the table. You will need to use this information to find out the total row count. Notice how you only need the filter information, not the sort. The reason is because the sort does not effect the totals rows, but rather just how the table is displayed.

_Note: even though I am using the Limit [example](LimitExample.md) as a reference I am going to take the liberty here to demonstrate what you need to do, rather than show you the specific code approach that I took. The reason is there are many ways to approach this and I do not want you to get bogged down in my implementation. Plus if you are interested in exactly what I did you can just go through the [example](LimitExample.md) yourself. So where I use the word service I am just assuming some generic service that works with a data access object to query the database. You will not find the getTotalRowCount() and getItems() anywhere in my examples. I am just trying to be clear on what you need to do._

```
FilterSet filterSet = limit.getFilterSet();
int totalRows = service.getTotalRowCount(filterSet);  
```

Once you have the total row count then set that back on the Limit. Internally this creates the Limit's RowSelect object, which contains the page and row information.

```
tableFacade.setTotalRows(totalRows);
```

Now use the sort and row information to only pull the rows that you need to display a page worth of data.

```
SortSet sortSet = limit.getSortSet();
int rowStart = limit.getRowSelect().getRowStart();
int rowEnd = limit.getRowSelect().getRowEnd();
Collection items = service.getItems(filterSet, sortSet, rowStart, rowEnd);
```

You now have a Limit object that contains all the right information and a page full of items. The last step then depends on whether you are using the API or the JSP Tags.

##### API: #####

On the API you just need to set the items back on the TableFacade.

```
tableFacade.setItems(items);
```

##### JSP Tag: #####

If you are using the JSP Tags then pass the items in the request, and also pass the Limit as well. Remember, the Limit contains the right information that your tag rendered table will need to know about.

Controller:

```
request.setAttribute("items", items);
request.setAttribute("limit", limit);
```

Tag:

```
<jmesa:tableFacade items="${items}" limit="${limit}" >
```

#### Additional Information ####

If you are using the [State](State.md) feature then you also need to check that you are not coming back to the table for the first time. However, instead of trying to figure that out, what you need to do is find out if the Limit is already complete. A complete Limit means that it already knows the total rows.

```
if (!limit.isComplete()) {
   FilterSet filterSet = limit.getFilterSet();
   int totalRows = service.getTotalRowCount(filterSet);  
   tableFacade.setTotalRows(totalRows);
}

SortSet sortSet = limit.getSortSet();
int rowStart = limit.getRowSelect().getRowStart();
int rowEnd = limit.getRowSelect().getRowEnd();
Collection items = service.getItems(filterSet, sortSet, rowStart, rowEnd);
```