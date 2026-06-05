<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Ripley - Iniciar Sesión</title>
    <link rel="stylesheet" href="${url.resourcesPath}/css/login.css">
</head>
<body>
    <div class="container">
        <div class="card">
            <div class="logo">
                <img src="${url.resourcesPath}/img/ripley-logo.png" alt="Ripley" />
            </div>

            <#if message?has_content && message.type = "error">
                <div class="error-message">${message.summary}</div>
            </#if>

            <form action="${url.loginAction}" method="post">
                <div class="form-group">
                    <label>Correo electrónico</label>
                    <input type="text" name="username" autofocus autocomplete="username" />
                </div>
                <div class="form-group">
                    <label>Contraseña</label>
                    <input type="password" name="password" autocomplete="current-password" />
                </div>
                <button type="submit">Iniciar Sesión</button>
            </form>
        </div>
    </div>
</body>
</html>