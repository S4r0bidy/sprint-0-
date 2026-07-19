<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html>
<head>
  <meta charset="UTF-8" />
  <title>Users Test</title>
</head>
<body>
  <h1>Vue: users/test</h1>
  <p>Accès via: <code>/users/test</code></p>

  <h2>Form POST</h2>
  <form method="post" action="<%= request.getContextPath() %>/users/test">
    <button type="submit">Envoyer (POST)</button>
  </form>
</body>
</html>

