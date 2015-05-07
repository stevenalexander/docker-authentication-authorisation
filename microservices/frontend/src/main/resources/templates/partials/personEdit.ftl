<#include "../main.ftl">
<#macro page_body>

<h1><a href="/persons">Persons</a> - <#if isNew>new<#else>${person.id}</#if></h1>



<form class="form-horizontal" method="POST">
  <div class="form-group">
    <label for="firstName" class="col-sm-2 control-label">First name</label>
    <div class="col-sm-10">
      <input type="text" class="form-control" id="firstName" name="firstName" placeholder="First name" <#if person??>value="${person.firstName}"</#if>>
    </div>
  </div>
  <div class="form-group">
    <label for="lastName" class="col-sm-2 control-label">Last name</label>
    <div class="col-sm-10">
      <input type="text" class="form-control" id="lastName" name="lastName" placeholder="Last name" <#if person??>value="${person.lastName}"</#if>>
    </div>
  </div>
  <div class="form-group">
    <div class="col-sm-offset-2 col-sm-10">
      <button type="submit" class="btn btn-default">Submit</button>
      <#if errors?? && errors?seq_contains("notAuthorised")>
      <span class="has-error">
        <label class="control-label">You do not have permission to do that</label>
      </span>
      </#if>
    </div>
  </div>
</form>

</#macro>
