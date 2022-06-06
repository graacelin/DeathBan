![image](https://user-images.githubusercontent.com/72771682/172119067-8b51be6d-2a66-403d-aca9-ad7182cef3c6.png)
 
# DeathBan
DeathBan is a server-side Forge mod for Minecraft, meant to create a hardcore-like experience to SMP servers by adding a configurable ban timer upon player death.  

The mod only needs to be added to the Forge server, so pure vanilla clients can also join a Forge server with DeathBan installed (given the server **only** has server-only mods).

## Configuration
You can change the death ban timer in `config/deathban.toml`. Note that the server does not need to be restarted for the changes to go through, but previous bans will not have their ban times changed.

```
["Configurations for DeathBan Mod"]
	#The week duration of the death ban
	Weeks = 0
	#The day duration of the death ban
	Days = 0
	#The hour duration of the death ban
	Hours = 0
	#The minute duration of the death ban
	Minutes = 0
```

## Screenshots

![image](https://user-images.githubusercontent.com/72771682/172134395-c0679b91-fd49-4d9d-8887-9869a7bc10c5.png)\
A welcome message will be sent to the player upon first time joining after a while


![image](https://user-images.githubusercontent.com/72771682/172136970-00624438-0e3f-4beb-b0d0-035e9ae0fab4.png)\
Custom ban screen after dying

