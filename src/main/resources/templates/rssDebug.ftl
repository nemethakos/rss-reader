<html>
<head>
<link href="/style.css" rel="stylesheet" />
</head>
<body>
<div class="rssItem">
<#list rss.rssMediaList as mediaList>
		<img class="media" src="${mediaList.link}">
</#list>
<hr>
	<#if rss.media?has_content>
		<img class="media" src="${rss.media.link}">
	</#if>	
	<div class="title">
		<a href="${rss.link}">${rss.title}</a>
	</div>
	<div class="description">${rss.description}</div>
	<div class="provider">
	<#if rss.providerImage?has_content >
		<img class="providerImage" src="${rss.providerImage}" 
		<#if rss.providerName?has_content>
			title="${rss.providerName}"
		</#if>
	<#elseif rss.providerName?has_content>
		${rss.providerName}
	</#if>
	</div>
	
	<#if rss.publicationDate?has_content>
	<div class="publicationDate">${rss.publicationDate?datetime}</div>
	<#else>
	<div class="publicationDate">No Publication date is given in the RSS feed!</div>
	</#if>
	<#--  
	<xmp class='stringified'>${rss.stringifiedEntry}</xmp>
-->
<div class='stringified'>${rss}</div>

<#list rss.keywordSet as keyword>
		<span class="keyword">
		<span class="keyword-text">${keyword.text}</span>
		<span class="keyword-posTag">${keyword.posTag}</span>
		<span class="keyword-frequency">${keyword.score}</span>
		</span>
	</#list>
	
</div>
</body>