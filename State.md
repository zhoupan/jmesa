The State interface is used to set and retrieve the table [Limit](Limit.md). Implementations will set the Limit so that it can be retrieved at a later time. This is useful so a user can return to a specific table with it filtered, sorted, and paged exactly like they left it.

All you have to do is set the stateAttr to the parameter to look for in the url, or the attribute in the request, to tip the table off that the Limit in the users session should be used.

#### Example ####

For example in the TableModel just pass in the name of the parameter to use:
```
TableModel tableModel = new TableModel(id, request);
tableModel.setStateAttr("restore");
```

The same goes for using the tags:
```
<jmesa:tableModel
        id="basic" 
        stateAttr="restore"
        ...
        >
```

So, by passing in a parameter of "restore=true" the table is rendered in the same way that you left it. That is really all there is too it!

<img src='http://jmesa.googlecode.com/svn/trunk/jmesa/resources/wiki/state-save.png' />

**Note:** Just be sure that you have a unique table id for each table that you define. Technically the Limit object for each table is stored in the users session keyed by the table id. If the tables are not unique then you will overwrite one [Limit](Limit.md) with another.