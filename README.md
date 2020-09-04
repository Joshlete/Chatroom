# Chatroom
 A PubNub API based chatroom

CS 458 README
JOSH GIURCULETE

Team: Josh Giurculete

Idea: This app is a chatroom/messaging app

Dependencies: 
- PubNub: Serverless programmable network. This is used to send messages to one or more devices that can make a TCP/IP connection to the internet. Two required information is needed to properly connect to PubNub. The first part needed is a subscribeKey and publishKey in the MainActivity.kt file, which can be created by making a new account on PubNub, however file has the key's created by me so a new one isn't necessary. The second information which is important is selecting a channel, which is set in the MainActivity.kt file(line 77, 85, and 96). However, any string seems to work as a channel. 
Link: https://www.pubnub.com/


- Two or more virtual devices will be needed to confirm proper connection. Simply open up two devices and start typing away.

Setup: There shouldn't be any necessary setup requirements other than building the program with Android Studio and setting up two virtual devices. Should be self contained. Open the ZIP file, open the program from Android Studio, wait for gradle to set up, then launch the program onto two virtual devices.


Notes:
- Many improvements can be made to this project, however small details tend to take up a lot of time. 
	- usernames
	- bubble style listing
	- better color scheme
	
