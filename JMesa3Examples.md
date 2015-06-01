I have most of the [tutorials](Tutorials.md) converted over now, and you should start there. But it can be very helpful to look through the  [code](Examples.md) in the examples war.

> ### Notes: ###

  * If you still want to build tables by defining the column properties you should use the TableModelUtils.createHtmlTable(). See the [Column Properties](http://code.google.com/p/jmesa/wiki/JMesa3) section.

  * If you are using Groovy you will need to define your CellEditor as an inner class now. The reason is using closures was no longer possible now that the components (table, row, column) are concrete classes instead of an interface. It is easier to build tables with the API when these are concrete classes, but now the polymorphism for the setCellEditor() methods are resolved at compile-time. This means that to use the API Groovy would have to be mandatory. The other reason I thought I could drop closures is that the syntax is almost identical to an inner class when using JMesa.

  * If you are using the tag library then only grab the latest jmesa.tld file if you want to change the TableFacadeTag to the TableModelTag. You will eventually want to move your tables to use the new TableModel, but with the tags you have to do them all at once in your application. But as you can see it is just a small name change.

```
<jmesa:tableFacade />
```

to

```
<jmesa:tableModel />
```