<#include "../main.ftl">
<#macro page_body>

<h1>Login</h1>

<form>
  <div class="form-group">
    <label for="emailaddress">Email address</label>
    <input type="email" class="form-control" id="emailaddress" placeholder="Enter email">
  </div>
  <div class="form-group">
    <label for="password">Password</label>
    <input type="password" class="form-control" id="password" placeholder="Password">
  </div>
  <button type="submit" class="btn btn-default">Submit</button>
</form>

</#macro>
