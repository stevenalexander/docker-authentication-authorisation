<#include "../main.ftl">
<#macro page_body>

<h1>Persons</h1>

<#if persons?has_content>
<ul>
<#list persons as person>
  <li><a href="/persons/${person.id}">${person.firstName} ${person.lastName}</a></li>
</#list>
</ul>
</#if>

<a href="/persons/add">Add Person</a>

</#macro>
