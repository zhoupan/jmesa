#### How can I export my table data to a PDF, Excel, or CSV file? ####

The best place to read about exporting data is found in the [exports page](Exports.md) or the [export tutorial](ExportTutorial.md).

#### How come my PDF export does not work? ####

First make sure that you have your jmesa-pdf.css in a place where the PDF export can find it. The default location is in a folder called css at the top of the web directory.

The global place to change this location is in the [preferences](Preferences.md). What you need to modify is the pdf.cssLocation preference.

```
pdf.cssLocation=/css/jmesa-pdf.css
```

Or you can set it directly in the API for each PDF export.

```
if (tableFacade.getLimit().getExportType() == PDF) {
    PdfView view = (PdfView)tableFacade.getView();
    view.setCssLocation("/somefolder/jmesa-pdf.css");
}
```

#### OK, but my PDF export still does not work? ####

The flying saucer library (xhtmlrenderer) has this bad habit of trying to retrieve the CSS for the PDF by an URL connection. So this can produce many and funny different problems. I already fixed 2 and maybe found a third one soon:

  1. If the web application works with SSL, trying to get the css this way will fail because the application MAY NOT TRUST ITSELF. Solution: The SSL certificate (of Apache or Weblogic/Tomcat) has to be in the repository of the Java VM that runs the app server (in its cacerts, for example). This way the app can get files doing https requests to itself, trusting itself, crazy isn't?

  1. If some kind of http authentication is set in Apache (via .htaccess or httpd.conf) the app MAY NOT be able to retrieve the CSS by an URLconnection. Solution: I can't remember right now cause I don't have the code at this moment but it's some java instruction to tell the app to use http authentication in any URLConnection, if needed, with the user and password set in Apache

#### How can I plug in my own export view? ####
You can create your own view (extending AbstractExportView) and plug it in.

```
if (tableModel.isExporting()) {
   if (tableModel.getExportType().equals(ExportType.PDF)) {
      tableModel.setView(new MyCustomView());
   }   
}
```