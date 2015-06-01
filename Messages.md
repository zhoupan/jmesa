The Messages interface is used to retrieve all the locale specific text. The default implementation uses Java [resource bundles](http://java.sun.com/j2se/1.5.0/docs/api/java/util/ResourceBundle.html).

To set up the resource bundle specify a context-param in your /WEB-INF/web.xml file and place the bundle wherever you want to like this:

```
<context-param>
  <param-name>jmesaMessagesLocation</param-name>
  <param-value>org/jmesaweb/resource/jmesaResourceBundle</param-value>
</context-param>
```

**Note:** Be sure to pay attention to the fact that there is no forward slash and no ending .properties syntax. Both should be excluded.

#### Current Locales Supported ####

The current Locales supported include:
  * Brazilian Portuguese (pt\_BR)
  * Catalan (ca)
  * Chinese (zh\_CN)
  * Dutch (nl\_NL)
  * English (default)
  * Finish (fi) --trunk
  * French (fr)
  * German Locale (de)
  * Italian (it\_IT)
  * Korean (ko\_KR)
  * Norwegian (no\_NO)
  * Polish (pl\_PL)
  * Spanish (es\_ES)
  * Spanish (es\_MX)
  * Swedish (sv\_SE)
  * Turkish (tr\_TR)

If you would like to have your native language supported out of the box in JMesa just send me a messages file to _jeff.johnston.mn@gmail.com_ and I will include that right away.

##### Messages File Includes: #####
```
html.statusbar.resultsFound=Results {1} - {2} of {0}.
html.statusbar.noResultsFound=There were no results found.

html.toolbar.tooltip.firstPage=First Page
html.toolbar.tooltip.lastPage=Last Page
html.toolbar.tooltip.prevPage=Previous Page
html.toolbar.tooltip.nextPage=Next Page
html.toolbar.tooltip.filter=Filter
html.toolbar.tooltip.clear=Clear
html.toolbar.tooltip.saveWorksheet=Save Worksheet
html.toolbar.tooltip.filterWorksheet=Filter Worksheet
html.toolbar.tooltip.excel=XLS Export
html.toolbar.tooltip.jexcel=XLS Export
html.toolbar.tooltip.pdf=PDF Export
html.toolbar.tooltip.pdfp=PDF Export
html.toolbar.tooltip.csv=CSV Export
html.toolbar.tooltip.xml=XML Export

html.toolbar.text.firstPage=First
html.toolbar.text.lastPage=Last
html.toolbar.text.nextPage=Next
html.toolbar.text.prevPage=Prev
html.toolbar.text.filter=Filter
html.toolbar.text.clear=Clear
html.toolbar.text.saveWorksheet=Save Worksheet
html.toolbar.text.filterWorksheet=Filter Worksheet
html.toolbar.text.excel=XLS
html.toolbar.text.jexcel=XLS
html.toolbar.text.pdf=PDF
html.toolbar.text.pdfp=PDF
html.toolbar.text.csv=CSV
html.toolbar.text.xml=XML
```