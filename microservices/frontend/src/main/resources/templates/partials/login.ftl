<#include "../main.ftl">
<#macro page_body>

<h1>Login</h1>

<p>Credentials are <em>admin@test.com/admin</em> and <em>user@test.com/user</em></p>

<form class="form-horizontal" method="post" action="/login">
  <div class="form-group">
    <label for="emailaddress" class="col-sm-2 control-label">Email address</label>
    <div class="col-sm-10">
    	<input type="email" class="form-control" id="emailaddress" name="emailaddress" placeholder="Enter email">
    </div>
  </div>
  <div class="form-group">
    <label for="password" class="col-sm-2 control-label">Password</label>
    <div class="col-sm-10">
    	<input type="password" class="form-control" id="password" name="password" placeholder="Password">
    </div>
  </div>
  <div class="form-group">
    <div class="col-sm-offset-2 col-sm-10">
      <button type="submit" class="btn btn-default">Submit</button>
    </div>
  </div>
</form>

</#macro>
