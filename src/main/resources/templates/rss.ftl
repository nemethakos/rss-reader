<html>
<head>
<link href="/style.css" rel="stylesheet" />
</head>
<body>

<form action="/search" method="post" class="form">
            Search term:<input type="text" name="searchTerm" 
<#if searchTerm?has_content>
                        value="${searchTerm}"
</#if>                        
            >
            <input type="submit" value="Submit">
</form>

<#list rssItemList as rss>
<div class="rssItem">
    <a class="find-similar" href="/find-similar/${rss.id}">Find Similar articles</a>
	<#if rss.score?has_content>
		<div class="score">${rss.score}</div>
	</#if>
	

	
	<#if rss.media.link?has_content>
		<img class="media" src="${rss.media.link}">
	<#else>
		<div class="image-placeholder" title="No image"></div>	
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
		>
	<#elseif rss.providerName?has_content>
		${rss.providerName}
	</#if>
	</div>
	<#if rss.publicationDate?has_content>
	<div class="publicationDate">${rss.publicationDate?datetime}</div>
	<#else>
	<div class="publicationDate">No Publication date is given in the RSS feed!</div>
	</#if>
	<a class="debugLink" href="/item/${rss.id}">Debug</a>
	
	<#if rss.searchScore?has_content>
	<#list rss.searchScore.wordList as keyword>
		<span class="keyword">
		<span class="keyword-text">${keyword.text}</span>
		<span class="keyword-posTag">${keyword.posTag}</span>
		<span class="keyword-frequency">${keyword.score}</span>
		</span>
	</#list>
	</#if>
</div>
</#list>
</body>