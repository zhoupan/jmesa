The Preferences interface is used to avoid hard coding the default attribute values JMesa uses. The default implementation uses a Java [properties](http://java.sun.com/j2se/1.5.0/docs/api/java/util/Properties.html) file. If you need to override any of these default settings you can do so by creating your own jmesa.properties file and then override the values you want. And, just to be clear, you only have to have the values that you want to override in your properties file, or any properties that you want in your custom classes.

To set up the properties file specify a context-param in your /WEB-INF/web.xml file and place the properties wherever you want to like this:

```
<context-param>
  <param-name>jmesaPreferencesLocation</param-name>
  <param-value>/org/jmesaweb/resource/jmesa.properties</param-value>
</context-param>
```

You can also place it relative to the WEB-INF directory:
```
<context-param>
    <param-name>jmesaPreferencesLocation</param-name>
    <param-value>WEB-INF/jmesa.properties</param-value>
</context-param>	
```

**Note:** Be sure to pay attention to the beginning forward slash and the ending .properties syntax. Both are necessary.

#### Customization Example ####

One useful property that you may want to modify is the path to your images directory. The default property setting is as follows:

```
html.imagesPath=/images/table/
```

If you want your images in some other directory just override this property in your custom properties file.

_Here is the complete [listing](http://code.google.com/p/jmesa/source/browse/trunk/jmesa/src/org/jmesa/core/preference/jmesa.properties) of all the default properties._