
ngx.log(ngx.NOTICE, 'BEGIN authenticate.lua')

-- Call Authentication microservice to check users credentials
local authenticationResult = ngx.location.capture('/authentication', { method = ngx.HTTP_POST, always_forward_body = true  })

if authenticationResult.status ~= ngx.HTTP_OK then
  ngx.log(ngx.WARN, 'Rejected authentication attempt. Status :' .. authenticationResult.status)

  ngx.header["X-Frame-Options"] = 'sameorigin'
  ngx.header["X-XSS-Protection"] = '1'
  ngx.header["X-Content-Type-Options"] = 'nosniff'

  ngx.exit(401)
end

local callerId = authenticationResult.body

ngx.log(ngx.NOTICE, 'callerId returned by Authentication microservice is: ' .. callerId)

ngx.req.clear_header('content-type')
ngx.header["content-type"] = 'text/plain'

-- Call Session microservice to create a new session for the authenticated user
local sessionPostResult = ngx.location.capture('/sessions', {method = ngx.HTTP_POST, body = callerId})

if sessionPostResult.status ~= ngx.HTTP_OK then
	ngx.log(ngx.ERR, 'Failed to acquire new session token from Session microservice. Status : ' .. sessionPostResult.status)

    ngx.header["X-Frame-Options"] = 'sameorigin'
    ngx.header["X-XSS-Protection"] = '1'
    ngx.header["X-Content-Type-Options"] = 'nosniff'

	ngx.exit(500)
end

local sessionToken = sessionPostResult.body;
ngx.log(ngx.NOTICE, 'Got session token from Session microservice: ' .. sessionToken)

-- Create a cookie for the session so future requests from this users session can be verified
ngx.header["Set-Cookie"] = {"AccessToken=" .. sessionToken .. "; path=/;"}

ngx.log(ngx.INFO, 'Redirecting to root url')

return ngx.redirect('/')
