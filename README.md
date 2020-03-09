# EECS2311

# VennDiagramV1.0

The main usage of this application is to organize and visualize data. The software aims to make categorization of information as easy as possible for the user. How it achieves this is by having a user friendly interface, easily import and export information, being able to move entities around the scene seamlessly, and create delete and edit entries with simple instructions. 

This release contains up-to-date feature and functions for mid release.

**Running**
- Windows - Double click the executable .jar file
- Mac/Linux - Using console, the command is java -jar

**If you want to run the source files from your eclipse IDE**
- Download the project files to your local computer and lunch eclipse IDE
- click file --> import --> existing projects into workspace
- click file --> export --> workspace exports as txt file
- browse and select the project folder from local space and click finish
- run the main class file as java application
- Drag and right click to select multiple items, unfortunately this function can be used only once due to some bugs, we are still fixing

**You may encounter some errors because of missing external jar library file or gradle testFx dependencies**
**For missing library file error**
- Your project file already contains fontawesomefx-8.9 jar file under image folder
- if you still encounter missing library file error then download the "fontawesomefx-8.9" file from following link
                https://jar-download.com/artifacts/de.jensd/fontawesomefx/8.9/source-code
- add this library file to your project by
                - right click the project file from IDE
                - select Build Path --> Configure Build Path --> Add External Jar File and select the downloaded jar file and Apply
                - If your library already has a fontawesomefx-8.9 file remove that first and add it again.
                
**Missing Gradle testFx dependencies error**
- your project in gradle.buid file already has gradle testFx dependencies references
- right click on your project --> Gradle --> Refresh gradle project
- the eclipse IDE automatically download and build the dependencies for your project 
