
-- Check against list of non-protected endpoints
if  string.find(ngx.var.uri, "^/login$") or
	  string.find(ngx.var.uri, "^/logout$") then
	  return
end

ngx.log(ngx.NOTICE, 'BEGIN access.lua for ' .. ngx.var.uri)

local accessToken = ngx.var.cookie_AccessToken

if not accessToken then
  ngx.log(ngx.WARN, 'No access token!')

  ngx.header["X-Frame-Options"] = 'sameorigin'
  ngx.header["X-XSS-Protection"] = '1'
  ngx.header["X-Content-Type-Options"] = 'nosniff'

  return ngx.exit(401)
end

ngx.log(ngx.INFO, 'accessToken: ' .. accessToken)

local acceptEncodingHeaderTemp = ngx.req.get_headers()["Accept-Encoding"]
ngx.req.clear_header("Accept-Encoding")

local sessionsResult = ngx.location.capture('/sessions/' .. accessToken);
local callerId = sessionsResult.body;
ngx.req.set_header('Accept-Encoding', acceptEncodingHeaderTemp)

print(ngx.INFO, 'sessionsResult.status = ' .. sessionsResult.status)

if sessionsResult.status ~= ngx.HTTP_OK then
  ngx.log(ngx.WARN, 'Nginx was unable to get callerId for '.. accessToken ..' from Sessions microservice')

  ngx.header["X-Frame-Options"] = 'sameorigin'
  ngx.header["X-XSS-Protection"] = '1'
  ngx.header["X-Content-Type-Options"] = 'nosniff'

  return ngx.exit(401)
end

ngx.log(ngx.INFO, 'Successfully verified session for request, callerId = ' .. callerId)

ngx.req.set_header('callerId', callerId)
