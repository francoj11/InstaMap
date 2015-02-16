# InstaMap

## What is does?
An Android app that lets you select a location with a given radius in a map and then shows you Instagram pictures that were uploaded near that location.

## How it looks?
Screenshots:

![alt text](https://github.com/francoj11/InstaMap/blob/master/Screenshots/1.png "Screenshot 1")
![alt text](https://github.com/francoj11/InstaMap/blob/master/Screenshots/2.png "Screenshot 2")
![alt text](https://github.com/francoj11/InstaMap/blob/master/Screenshots/3.png "Screenshot 3")

![alt text](https://github.com/francoj11/InstaMap/blob/master/Screenshots/4.png "Screenshot 4")
![alt text](https://github.com/francoj11/InstaMap/blob/master/Screenshots/5.png "Screenshot 5")
![alt text](https://github.com/francoj11/InstaMap/blob/master/Screenshots/6.png "Screenshot 6")


You can see a video of the app running in here:

<a href="http://www.youtube.com/watch?feature=player_embedded&v=_m-wXNs-g8U
" target="_blank"><img src="http://img.youtube.com/vi/_m-wXNs-g8U/0.jpg" 
alt="IMAGE ALT TEXT HERE" width="240" height="180" border="10" /></a>

## How it works? 
First of all, you select a location in a map (Google Maps v2 API), you can change the radius too. After you confirm,
a request is sent to Instagram asking for pictures uploaded near the location you chose, with the given radius (Atention: 
the pictures retrived from Instagram, are pictures uploaded but not necesarily taken near the given location; e.g. this means that someone who lives in Buenos Aires, Argentina goes to New York and take some pictures of the Statue of liberty and then goes back to Argentina, and uploads the picture there, the picture in instagram will have location in Buenos Aires).
When the JSON response arrives, it is parsed and the photos are displayed in a RecyclerView.

## How to compile it?
Just download the project or clone it, and get your own Instagram and Google Maps Keys.

- To get the Instagram Cliend Id, go to http://instagram.com/developer/ and register a new application. Then the Client Id should be set in the PhotosActivity.java variable called "instaClientId".
- To get the Google Maps keys, go to https://console.developers.google.com and a create a new project, then enable the Maps V2 API, and after that, get the keys in the credentials section. You will need the SHA1 certificates of your debug and realese keys (the keys which you use to sign the APK).

And thats all you need, you should be able to compile without problem.

------------------
##Credits:
This projects uses the [Picasso](http://square.github.io/picasso/) library, a powerful image downloading and caching library for Android.

The Logo of the project is from Nick Frost, you can found this logo and more in [HERE] (https://www.iconfinder.com/icons/196750/location_map_marker_navigation_pin_icon#size=128)

-----------------
## Developed by:
Franco Jaramillo - francoj11@gmail.com

## License:
    Copyright 2015 francoj11

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
