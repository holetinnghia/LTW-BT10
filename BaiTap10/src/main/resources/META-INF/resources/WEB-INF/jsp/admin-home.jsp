<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head><script src="/static/app.js"></script></head>
<body onload="loadProductsSorted()">
<h2>Admin Home</h2>
<p>Quản lý Category, Product (AJAX GraphQL)</p>
<ul id="prod-list"></ul>
<hr/>
<label>Category ID:</label><input id="cid" value="1"/> <button onclick="loadProductsByCategory()">Load</button>
<ul id="prod-by-cat"></ul>
</body>
</html>