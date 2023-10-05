package common

const ApiDocsURL string = "/api/docs"
const ValidateAccessTokenURL string = "/api/v1/validateAccessToken"
const ValidateApiKeyURL string = "/api/v1/validateApiKey"
const TdeiAPIEndpoint string = "/api/v1/api"



const HelpHTML string = `<html>
<head>
<style type=text/css>
*{
    transition: all 0.6s;
}

html {
    height: 100%;
}

body{
    font-family: 'Lato', sans-serif;
    color: #888;
    margin: 0;
}

#main{
    display: table;
    width: 100%;
    height: 100vh;
    text-align: center;
}

.fof{
	  display: table-cell;
	  vertical-align: middle;
}

.fof h1{
	  font-size: 50px;
	  display: inline-block;
	  padding-right: 12px;
}
</style>
</head>

<body>
<div id="main">
    	<div class="fof">
        		<h1>Unauthorized access</h1>
        <p>For API documentation please refer <a href="apidoc">TDEI API DOC</a>
        </p>
    	</div>
</div>

</body>
</html>`

