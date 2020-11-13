# b2sLite

b2sLite is a fork of the open source OSRS client RuneLite.

## Extra Features include

* Ability to hide plugins
* More menu entry swapper options
* Impling tracker overlay
* Ground item timers
* Ban list plugin
* Friends/Enemies counter
* Screenshot friends/clan members deaths
* Alchemical Hydra helper
* Chambers of Xerics and Theater of Blood helper plugins
* Inferno plugin (tm)
* Grotesque Guardians plugin
* Time Tracker overlay for herbs/birdhouses
* Shows accumulated EXP from Blast Mining
* Anti-drag (no need to hold down shift)
* Discord presence custom status
* Inventory Price Checker overlay
* Window snapping with custom window chrome
* Additional chat notifications for PM's
* Custom reminders

## Join our discord community [here](https://discord.gg/qSupxuM)!

![](https://runelite.net/img/logo.png)
# runelite [![CI](https://github.com/runelite/runelite/workflows/CI/badge.svg)](https://github.com/runelite/runelite/actions?query=workflow%3ACI+branch%3Amaster) [![Discord](https://img.shields.io/discord/301497432909414422.svg)](https://discord.gg/mePCs8U)

RuneLite is a free, open source OldSchool RuneScape client.

If you have any questions, please join our IRC channel on [irc.rizon.net #runelite](http://qchat.rizon.net/?channels=runelite&uio=d4) or alternatively our [Discord](https://discord.gg/mePCs8U) server.

## Project Layout

- [cache](cache/src/main/java/net/runelite/cache) - Libraries used for reading/writing cache files, as well as the data in it
- [http-api](http-api/src/main/java/net/runelite/http/api) - API for api.runelite.net
- [http-service](http-service/src/main/java/net/runelite/http/service) - Service for api.runelite.net
- [runelite-api](runelite-api/src/main/java/net/runelite/api) - RuneLite API, interfaces for accessing the client
- [runelite-client](runelite-client/src/main/java/net/runelite/client) - Game client with plugins

## Usage

Open the project in your IDE as a Maven project, build the root module and then run the RuneLite class in runelite-client.  
For more information visit the [RuneLite Wiki](https://github.com/runelite/runelite/wiki).

### License

RuneLite is licensed under the BSD 2-clause license. See the license header in the respective file to be sure.

## Contribute and Develop

We've set up a separate document for our [contribution guidelines](https://github.com/runelite/runelite/blob/master/.github/CONTRIBUTING.md).
