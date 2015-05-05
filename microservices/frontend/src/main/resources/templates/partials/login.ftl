<#include "../main.ftl">
<#macro page_body>

<h1>Login</h1>

<form class="form-horizontal">
  <div class="form-group">
    <label for="emailaddress" class="col-sm-2 control-label">Email address</label>
    <div class="col-sm-10">
    	<input type="email" class="form-control" id="emailaddress" placeholder="Enter email">
    </div>
  </div>
  <div class="form-group">
    <label for="password" class="col-sm-2 control-label">Password</label>
    <div class="col-sm-10">
    	<input type="password" class="form-control" id="password" placeholder="Password">
    </div>
  </div>
  <div class="form-group">
    <div class="col-sm-offset-2 col-sm-10">
      <button type="submit" class="btn btn-default">Submit</button>
    </div>
  </div>
</form>

</#macro>
