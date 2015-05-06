
ngx.log(ngx.NOTICE, 'BEGIN logout.lua')

local accessToken = ngx.var.cookie_AccessToken

if not accessToken then
	return ngx.redirect('/login')
end

ngx.log(ngx.INFO, 'accessToken: ' .. accessToken)

ngx.req.clear_header("Accept-Encoding")

local sessionsDeleteResult = ngx.location.capture('/sessions/' .. accessToken, { method = ngx.HTTP_DELETE });

if sessionsDeleteResult.status ~= 204 then
  ngx.log(ngx.WARN, 'Nginx was unable to delete '.. accessToken ..' from Sessions microservice')

  return ngx.exit(500)
end

return ngx.redirect('/login')
