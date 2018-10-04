<html>
<head>
<link href="/style.css" rel="stylesheet" />
</head>
<body>

<form class="tester-form" action="/" method="post" class="form">
            URL:<input type="text" name="url" 
<#if url?has_content>
                        value="${url}"
</#if>                        
            >
            Selector:<input type="text" name="selector" 
<#if selector?has_content>
                        value="${selector}"
</#if>                        
            >
<input type="radio" name="format" value="text" id="radio_text"
<#if format == "text">checked</#if>
><label for="radio_text">Text</label>
<input type="radio" name="format" value="html" id="radio_html"
<#if format == "html">checked</#if>
><label for="radio_html">HTML</label>

</input>
                        <input type="submit" value="Submit">
</form>

<div class="tester-selected">
${selected}
</div>
</body>