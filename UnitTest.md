JMesa can easily be unit tested. By using the Parameters interface combined with the ParametersBuilder class you can easily throw JMesa into any state you want.

The ParametersBuilder abstracts out the parameters that allows you to set up the table. The following method calls are available:

```
public class ParametersBuilder {
    public void setPage(int page) {}

    public void setMaxRows(int maxRows) {}

    public void addFilter(String property, String value) {}

    public void addSort(String property, Order order) {}
    
    public void addSort(int position, String property, Order order) {}

    public void setExportType(ExportType exportType) {}

    public void setFilterWorksheet() {}

    public void setSaveWorksheet() {}
}
```

This should be pretty straightforward. You can easily set the page, max rows and export. In addition you can add filter or sort objects. However, you might still be wondering how this actually sets the parameters on the (mock) request. For that to happen you need to set the Parameters (interface) on the ParametersBuilder constructor.

Right now there is only one implementation to set up the Parameters, using the Spring framework. However, if you are not using Spring and would still like to unit test your code consider writing your own adapter and then donating the code back. Writing an adapter is very easy as you can see from the [SpringParametersAdapter](http://jmesa.googlecode.com/svn/tags/jmesa-2.3/src/org/jmesa/test/SpringParametersAdapter.java). The real work is done in the builder so the adapter is just a few lines of code to link the mock request with the builder.

#### SpringParametersAdapter Example ####

If you are using Spring you will want to set up the parameters by using the SpringParametersAdapter. The following example shows how you would use the Spring MockHttpServletRequest with the SpringParametersAdapter to make the ParametersBuilder work.

```
MockHttpServletRequest request = new MockHttpServletRequest();
Parameters parameters = new SpringParametersAdapter(request);
ParametersBuilder builder = new ParametersBuilder(ID, parameters);
```

This is all boilerplate code and the only thing to understand is that by setting this up it will allow the ParametersBuilder to build up the parameters on the MockHttpServletRequest. Or, put another way, each time you call a method on the ParametersBuilder the table will be set to that state.

With the ParametersBuilder created the following shows how to sort the first name in ascending order and the last name in descending order.

```
builder.addSort("firstName", Order.ASC);
builder.addSort("lastName", Order.DESC);
```

If you wanted to set the page to page 2 you would do the following:
```
builder.setPage(2)
```

To add a filter you would do this:
```
builder.addFilter("firstName", "geo");
```