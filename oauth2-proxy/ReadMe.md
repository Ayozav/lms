# Договорённости (открытые? Не верю...)

Короче, договорённости следующие:
1. `auth.localhost` принадлежит только LogTo.
2. `sign-in.localhost` принадлежит только OAuth2-Proxy.
3. `localhost` принадлежит фронту, НО ФРОНТ ОБЯЗАН СДЕЛАТЬ РЕДИРЕКТ НА `sign-in.localhost`!!!

## Мини-гайд для LogTo по OAuth2-Proxy
(Я С ЭТИМ ПЛАКАЛ ОЧЕНЬ ДОЛГО..?.!.?!!!..)

1. Создать Traditional Application
2. Добавить Redirect URI's: http://localhost, http://auth.localhost/oidc/auth, http://auth.localhost/oauth2/callback
3. Копируем App ID, вставляем с config.toml 
4. Копируем Secret 
5. Переходим во вкладку Permissions, добавляем обязательно права!
6. ОБЯЗАТЕЛЬНО СДЕЛАТЬ Sign in & account ПЕРЕД ЗАПУСКОМ! Иначе не взлетит.
7. После настройки страницы входа -- открыть Sign-up and sign-in, промотать в самый низ
8. Unknown session redirect URL установить http://localhost/
