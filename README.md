![image](https://raw.githubusercontent.com/cornos/Atomic/master/src/main/resources/assets/atomic/logo.png)

A 1.17 fabric mod with useful features for enforcing the [Minecraft commercial use guidelines](https://account.mojang.com/documents/commercial_guidelines#:~:text=sell%20entitlements%20that%20affect%20gameplay) on certian Minecraft servers. 

**This is a fork of the last public version of the client, but with ImGui menu**

## Read this bruh

**This is the last public version + ImGui**

**Don't even bother if you have linux. Ask 0x150. He bricked his computer trying to install GLIBC verison 2.29**

**Troubleshooting**

 - if it breaks use .test to reset the imgui instance and it should fix it
 - try not to enable clickgui or imgui while in imgui
 - use clickgui or .config to change binds i dont care to figure out how do keybinds

**This client has been privated. No updates are going to be published anymore.**

If you'd like to get the client in its private form, dm me @ 0x150#0150, and maybe you'll get it.

## Why?
This is the sequel to [cornos](https://cornos.cf), but ported to 1.17, with a new look and functionality. Cornos became a bit stale, so I decided to start this.

## Support
[discor](https://discord.gg/rvC7F798xQ)

## Downloading
You can download this from the `builds` folder. The latest jarfile will always be called "latest.jar". Download and drag into your 1.17+ mods folder to use.
Download in the `builds` folder, or [here](https://github.com/cornos/Atomic/raw/master/builds/latest.jar) for a direct download link

## Installation

### GNU/Linux <!--on top-->
1. Download java 16
   - `sudo apt install openjdk-16-jre` on ubuntu/debian
   - `sudo pacman -s jre-openjdk` on manjaro/arch
   - `echo "you're on your own, good luck"` on gentoo
   <!--tbh i never used fedora so I can't help them-->
2. Extract java 16 to a folder of your choice
3. Tell your 1.17 minecraft instance to use that java 16, if it doesn't automatically do it
4. Install [fabric](https://fabricmc.net/use/) for 1.17
5. Drag the .jar into the `~/.minecraft/mods` folder, no libraries required. You may need to manually paste the path into your file manager. 
6. Launch fabric loader for 1.17 via the minecraft launcher

### Windows
The default launcher should already choose java 16 for the runtime, so you're free from steps 1-3
1. Install [fabric](https://fabricmc.net/use/) for 1.17
2. Drag the .jar into the `%appdata%/.minecraft/mods` folder, no libraries required
3. Launch fabric loader for 1.17 via the minecraft launcher

### Mac
1. Install [fabric](https://fabricmc.net/use/) for 1.17
2. Drag the .jar into the `~/Library/Application Support/minecraft/mods` folder
3. Launch fabric loader for 1.17 via the minecraft launcher

## Stargazers

[![Stargazers over time](https://starchart.cc/cornos/Atomic.svg)](https://starchart.cc/cornos/Atomic)
