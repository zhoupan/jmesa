JMesa has a complete tag library, which is very intuitive and should feel very similar to building a table using the API.

**Note: The tag library uses SimpleTag, which requires JSP 2.0 or above.**

### Example ###
The following will define the html table.

```
<jmesa:tableModel
    id="pres" 
    items="${presidents}"
    exportTypes="csv,excel"
    stateAttr="restore"
    var="bean"
    >
    <jmesa:htmlTable 
        captionKey="presidents.caption" 
        width="600px"
        >		
        <jmesa:htmlRow>	
            <jmesa:htmlColumn property="name.firstName" titleKey="presidents.firstName">
                <a href="http://www.whitehouse.gov/history/presidents/">${bean.name.firstName}</a>
            </jmesa:htmlColumn>
            <jmesa:htmlColumn property="name.lastName" title="Last Name"/>
            <jmesa:htmlColumn property="term"/>
            <jmesa:htmlColumn property="career"/>
            <jmesa:htmlColumn property="born" filterable="false" pattern="MM/yyyy" cellEditor="org.jmesa.view.editor.DateCellEditor"/>
        </jmesa:htmlRow>
    </jmesa:htmlTable> 
</jmesa:tableModel>
```