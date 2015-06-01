I think Groovy is amazing! It is so useful and I knew when I first started learning about it that it could be used to make working with JMesa in the controller as natural and easy as having a tag implementation. The following is an example of how much easier defining a [CellEditor](Editors.md) is.

If you are new to Groovy and are interested in learning about it then I would highly recommend getting Groovy In Action and/or Groovy Recipes: Greasing the Wheels of Java!

#### Example ####

I will start by showing you the call to the custom editor. An interesting thing about custom editors is they make a perfect candidate to use the decorator pattern.

```
// the factory derived cell editor
CellEditor editor = factory.createBasicCellEditor();

// create a custom editor by decorating the basic cell editor
PresidentsLinkEditor customEditor = new PresidentsLinkEditor();
customEditor.setCellEditor(editor);
```


Then the fun part. Without Groovy this is how a custom CellEditor would be defined:

```
private static class PresidentsLinkEditor implements CellEditor {
    CellEditor cellEditor;

    public setCellEditor(CellEditor cellEditor) {
        this.cellEditor = cellEditor;
    }

    public Object getValue(Object item, String property, int rowcount) {
        Object value = cellEditor.getValue(item, property, rowcount);
        HtmlBuilder html = new HtmlBuilder();
        html.a().href().quote().append("http://www.whitehouse.gov/history/presidents/").quote().close();
        html.append(value);
        html.aEnd();
        return html.toString();
    }
}
```

Not too bad really as the HtmlBuilder does make things easier. However, wouldn't it be great if you could somehow have the ease of templating, like you do in a jsp, but not have the calling code be aware of any templating engine.

This is where Groovy comes in. Now our custom CellEditor looks like this:

```
class PresidentsLinkEditor implements CellEditor {
    CellEditor cellEditor;

    def getValue(def item, String property, int rowcount) {
        def value = cellEditor.getValue(item, property, rowcount)
        return """
                  <a href="http://www.whitehouse.gov/history/presidents/">
                     $value
                  </a>
               """
    }
}
```

This is my Groovy class that is doing the exact same work as the static inner class. In fact to get this to work I did not change one line of code in my calling class...other than the import declaration. One of the main things to know about Groovy is that Groovy is run as bytecode in the JVM so Groovy and Java get along like beer and cheese. It can be compiled or be used for at-runtime scripting. In my example I compiled it and included it as a jar file.

I will admit that this is kind of a simple example. However as easy as this is, think how easy and useful this would be if it was not so simple. First of all, notice how there is no setter defined for the cellEditor attribute. That is just some Groovy magic. Then, if you look carefully at the href tag you will notice the $value attribute resolves to the def value. Nice huh. And then I can include all the text I want on as many lines as I need without any String concatenation getting in the way. All this and all I am using is the Groovy GString class. With Groovy I have the complete Groovy library as well as Java and any other library I want to use.








