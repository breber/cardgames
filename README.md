# README [![Build Status](https://travis-ci.org/breber/cardgames.png)](https://travis-ci.org/breber/cardgames)

## Creating a Release

### Export Application APK

* Make sure you have the latest changes from Github
* Update the version number in the Android Manifest
  * If it is a minor release, increment the least-significant number (#.#._)
  * If it is a major release, or we have had a number of minor releases, increment the second least-significant number (#._.0)
  * Once we have had a number of major releases, increment the most-significant number (_.0.0)
* In Android Studio, under the Build menu, choose Generate Signed APK
* On the next screen, choose "Use existing keystore" and change the Location to the location of the "WorthwhileGamesKey" file in Dropbox
  * Ask Brian if you don't already know the password
* On the next screen, choose the Alias "worthwhilegames" and the password is the same as the previous step
* Choose a location for the APK to be exported

### Upload the APK to Google Play

* Visit the Android Developer Console
* Choose the Card Games application
* Go to the APK files tab
* Upload the new APK release
* Make sure the release is "Active"
* Update the "Recent Changes" section of the Product Details tab
* Click "Publish"

### Create a Tag in Github

We should create a tag in Git whenever we publish an update just so that we have a good history of what changes were introduced in which version

* From the Terminal, navigate to the CardGames repository
* From the CardGames repository folder, run the following command to create a tag
* The name of the tag should follow the format "v#.#.#", where the '#' characters are replaced with the actual version from the Android Manifest

    > git tag [name of tag]

* The push the tag to Github

    > git push --tags

### Add the APK file to Dropbox

It is also a good idea to have a copy of the APK from each release, just in case.

* Rename the APK uploaded to the Developer Console to CardGames_v#.#.#.apk, where the '#' characters are replaced with the actual version number
* Move the renamed APK to the Releases directory in the Dropbox folder

## External Libraries Used

* [JmDNS](http://jmdns.sourceforge.net/) for the Android Multicast support
