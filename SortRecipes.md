#### How do I sort the table differently than the default? ####

If you need to sort differently than how it is out of the box you can define your own sorting strategy. Just implement the ColumnSort and set it on the TableModel of the API or tags.

Creating your own sorting strategy is fairly easy. As an example here is the
[default](http://code.google.com/p/jmesa/source/browse/tags/jmesa-3.0/src/org/jmesa/core/sort/MultiColumnSort.java) sorting class.

##### API #####
```
tableModel.setColumnSort(new CustomColumnSort());
```

##### Tag #####
```
<jmesa:tableModel columnSort="com.mycompany.CustomColumnSort"
```

#### How do I sort a column that pulls values from two different properties? ####
For example, if you have a bean called model (which is representing database entries) with two properties, phone and mobile. The column will be called "phone" and must have values for "phone" in each row only if "phone" is not null for that database entry. In case the value for "phone" is null, then the value in that row must be that of "mobile". But the JMesa column has a property attribute which points to the bean property so I can't reference both properties in only one column.

The best solution is to have your bean add a new attribute that brings that logic together. In the (above) example you could call it getPhoneOrMobile() and have it do the logic of what it means to pull from one or the other. Then your JMesa column would use that bean property.

For example the column tag would look like this:
```
<jmesa:htmlColumn property="phoneOrMobile" title="Number"/>. 
```

The reason that this is the best solution is because it is simple to use and understand, and allows you to use the out-of-the-box sorting. From JMesa's perspective it is also the most efficient. JMesa only processes and renders the rows and columns that are displayed, and not the entire list. If you are not able to get control of the bean because it is managed elsewhere then you could create a wrapper for it and use that. By wrapper I mean go through and wrap the existing bean with a class that includes the extra columns, and then has the other methods defined as a pass-through to the wrapped class.

#### Why are my characters showing up as "?" on the screen? ####

You need to add the following to the top of the JSP to get it to render correctly:

```
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
```


#### Sorting Boolean versus boolean ####

By default JMesa uses the apache commons beanutils for sorting. Just recently it was brought to my attention that there is a difference between using the Boolean object versus the boolean primitive. The rules seem to be the following:

  * Boolean as an object needs to have a method that starts with 'get'.
  * boolean as a primitive needs to have a method that starts with either 'get' or 'is'.

#### How do I change the table behavior to a single column sort? ####

See the [Action Example](Javascript.md) on the JavaScript page.