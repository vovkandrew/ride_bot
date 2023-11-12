# ride_bot #

This is *Telegram bot* for creating and booking trips dedicated to drivers/clients usage.

To run this bot:
1) you need a bot registered at Telegram, more here: [Create a new Telegram bot](https://medium.com/shibinco/create-a-telegram-bot-using-botfather-and-get-the-api-token-900ba00e0f39). After registering your Telegram bot, use bot name and token and fill ***bot.username*** and ***bot.token*** properties at [application.properties](src/main/resources/application.properties) file.
2) you need tunneling app to use your bot from Telegram app. I personally recommend [ngrok](https://ngrok.com/). If you are on Windows, just download *ngrok.exe* file (may come being zipped), run *.exe* file, run command: _ngrok.exe http 8090_ (8090 is default port for this app, you are free to use any other, but don't forget to edit ***server.port*** property). After running script command, you should see *Forwarding* row, take the first URI, it should be of such format: *https://58fe-176-36-182-196.ngrok-free.app*. Fill ***bot.webhook-url*** property with this URI.
3) run you PostgreSQL database and correspondingly fill ***spring.datasource.url***, ***spring.datasource.username*** and ***spring.datasource.password*** properties.

Now you are ready to go, run this spring boot app and go to *@your-telegram-bot-name* Telegram bot, type **/start** and explore all features it provides.