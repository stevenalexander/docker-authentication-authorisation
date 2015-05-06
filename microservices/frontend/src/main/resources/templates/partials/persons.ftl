<#include "../main.ftl">
<#macro page_body>

<h1>Persons</h1>

<#if persons?has_content>
<ul>
<#list persons as person>
  <li>${person.firstName} ${person.lastName}</li>
</#list>
</ul>
</#if>

</#macro>
