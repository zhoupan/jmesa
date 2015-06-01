![http://extremecomponents.googlepages.com/JMesa-2.3.gif](http://extremecomponents.googlepages.com/JMesa-2.3.gif)

### 3D Header ###
If you want the header to have a nice 3D effect (shown above) then add the following three lines (below) to the header styling. The header-bg.gif image is available in the distribution.

```
.jmesa .header td {
    background-image: url(../images/table/header-bg.gif);
    background-repeat: repeat-x;
    white-space: nowrap;
   ...
}
```

### Drop Shadow ###
Drop Shadow option for table, which gives a nice aesthetic effect (shown above). _Note: the effect does not look nice with a table caption._


  * To make it work put the following script in your code to execute when the page is loaded. Here the imagesPath is the path to your images directory. The following imagesPath in the JMesa web site resolves to '/jmesa/images/table/'
  * Also include the following images
    * shadow\_back.gif
    * shadow180.gif
    * shadow.gif
    * corner\_tr.gif
    * corner\_bl.gif

```
<script type="text/javascript">
	$(document).ready(function() {
	   addDropShadow('${imagesPath}');
	});
</script>
```

You can also change how far the dropshadow extends by modifying the dropShadow attribute in the jmesa.css file.

```
.jmesa .dropShadow {
    padding: 10px 14px 14px 10px;
}
```