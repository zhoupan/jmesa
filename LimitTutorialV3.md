The default way that JMesa deals with [items](Items.md) is to take everything and then perform the filtering and sorting. The nice thing about this is you can get the filtering, sorting, and pagination for free. That works great for small to medium result sets, but starts to fail pretty miserably when the results are large. It really is a judgement call, but I prefer to let profiling make my technical decisions. If you are concerned that you have a performance problem then the best thing to do is put a profiler on it and see for yourself. There are many open source and commercial profilers available to help you make the best decision. So, lets assume that we have discovered a performance problem and need to deal with the pagination ourselves.

To handle the pagination manually means that you only pull back enough results to fit on one page. At the same time you will need to deal with the filtering, sorting and pagination yourself. For the following discussion I am assuming that you are getting the [items](Items.md) from a database and not some other mechanism. Of course the same principles apply no matter what you do so you should easily be able to adapt your code.

Now here is the important part. To get a smaller result set you would create a query pulling data like normal, but limit the results that you get back. Every database has some command that allows you to limit the results retrieved from the database. For MySQL the command is limit, for Oracle it is between, for Sybase it is rowcount and for SQL Server it is top. If you are starting to follow my line of thinking what I am saying is when the user first goes to the table have the query only bring back the first page of result. If the user goes to the next page then return the next page of results.

The savy Sybase or SQL Server developer may be saying, but the rowcount command always starts at the beginning, so when I go to page two I have to start at the beginning. Well, yes, but now you have only returned two pages of results, as opposed to the whole result set. When you go to page three then you will only have pulled back three pages. Other databases, such as MySQL and Oracle, allow you to ask for a given section of rows so then you consistently only pull back the page that you need.

To find out how the user is trying to sort and filter, what page they are going to, and how many results they need to see JMesa has a convenient class called the [Limit](Limit.md). To use the Limit to pull back one page of data you just need to implement the PageItems interface.

### Example ###

Using the Limit to efficiently pull data from the database only requires you to implement the PageItems interface. If you are using the API you would set this on the TableModel.

```
tableModel.setItems(new PageItems() {
    public int getTotalRows(Limit limit) {
        PresidentFilter presidentFilter = getPresidentFilter(limit);
        return presidentService.getPresidentsCountWithFilter(presidentFilter);
    }

    public Collection<?> getItems(Limit limit) {
        PresidentFilter presidentFilter = getPresidentFilter(limit);
        PresidentSort presidentSort = getPresidentSort(limit);
        int rowStart = limit.getRowSelect().getRowStart();
        int rowEnd = limit.getRowSelect().getRowEnd();
        return presidentService.getPresidentsWithFilterAndSort(presidentFilter, presidentSort, rowStart, rowEnd);
    }
});
```


The first thing you need to do is find out how the user interacted with the filters on the table. You will need to use this information to return the total row count. Notice how you only need the filter information, not the sort. The reason is because the sort does not effect the totals rows, but rather just how the table is displayed.

The second thing you need to do is return the actual items that you will display on the table. To do this you need to know everything else. This includes what was filtered and sorted, as well as the specific rows.

If you are using the tag library you would call the TableModelUtils.getItems() method and pass it the PageItems implementation.

```
Collection<?> items = TableModelUtils.getItems(id, request, new PageItems() {
    public int getTotalRows(Limit limit) {
        PresidentFilter presidentFilter = getPresidentFilter(limit);
        return presidentService.getPresidentsCountWithFilter(presidentFilter);
    }

    public Collection<?> getItems(Limit limit) {
        PresidentFilter presidentFilter = getPresidentFilter(limit);
        PresidentSort presidentSort = getPresidentSort(limit);
        int rowStart = limit.getRowSelect().getRowStart();
        int rowEnd = limit.getRowSelect().getRowEnd();
        return presidentService.getPresidentsWithFilterAndSort(presidentFilter, presidentSort, rowStart, rowEnd);
    }
});
request.setAttribute("presidents", items);
```

_Note: I am taking the liberty to explain what you need to do, rather than show you the specific code approach that I took. The reason is there are many ways to approach this and I do not want you to get bogged down with my implementation. Plus if you are interested in exactly what I did you can just go through the [limit example](http://code.google.com/p/jmesa/source/browse/trunk/jmesaWeb/src/org/jmesaweb/controller/LimitPresidentController.java) yourself._