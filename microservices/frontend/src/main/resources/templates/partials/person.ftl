<#include "../main.ftl">
<#macro page_body>

<h1><a href="/persons">Persons</a> - ${person.id}</h1>

<form class="form-horizontal">
  <div class="form-group">
    <label class="col-sm-2 control-label">First name</label>
    <div class="col-sm-10">
      <p class="form-control-static">${person.firstName}</p>
    </div>
  </div>
  <div class="form-group">
    <label class="col-sm-2 control-label">Last name</label>
    <div class="col-sm-10">
      <p class="form-control-static">${person.lastName}</p>
    </div>
  </div>
</form>

<a href="/persons/${person.id}/edit">Edit Person</a>

</#macro>
