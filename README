BlackBerry Client
~~~~~~~~~~~~~~~~~

Setup
=====

BlackBerry OS version used is **7.1.0**.

Should you have an older version of BlackBerry OS, see `Install BlackBerry OS 
7.1.0 in Eclipse`_.

1) Create a Blackberry project called *SurvivorPoolClient* in Eclipse. 

2) Delete the ``src/`` and ``bin/`` folder in the project and copy *all* the files from the
   client jar file.

3) Run BlackBerryApp.java

4) Map the SD card to the folder that contains ``res/`` folder for the
   Administrator client, not the BlackBerry client side. The res folder is found
   in the same folder as the ``Group2Standalone.jar`` file. See `Setup SD Card 
   for BlackBerry Simulator`_ for more detailed instructions.


**Note:**
  The season must be started (Administrator side) to be able to log into the 
  BlackBerry client (warning shows up).

Setup SD Card for BlackBerry Simulator
++++++++++++++++++++++++++++++++++++++

To get BlackBerry to share the data files from the Administrator Client, the 
following needs to be completed:

1) In the Blackberry emulator, click on Simulate->Change SD Card

2) Add the folder that contains the folder 'res'

3) Click on remount SD Card on Startup.

4) Hit the harddrive + button, and data persistence should work.

Install BlackBerry OS 7.1.0 in Eclipse
++++++++++++++++++++++++++++++++++++++

1) Open Eclipse

2) Navigate to Help -> Install New Software

3) Select the BlackBerry software repository

4) Select BlackBerry OS 7.1.0 from the source list and install

5) Restart Eclipse

Specification Testing
=====================

1. Enter his/her userid

   - Starting the app will show the login screen. Enter user id and hit *log in*
	

2. See the current standings 

   - In the main menu, click on *view standings*
	

3. Pick which contestant will be the ultimate winner

   - In the main menu, click on *Make your vote*, then *Vote for Ultimate*
	

4. See which contestants are still remaining, which have been eliminated

   - In the main menu, click on *Make your vote*, then any of the options

     The values in the table show all the contestants and if they have been 
     casted off or not
	

5. Pick which contestant he/she thinks will be eliminated this week/round

   - In the main menu, click on *Make your vote*, then *Vote for this week*


6. See the bonus questions
  
   - In the main menu, click on *Bonus Questions*. Use the *prev* and *next* 
     buttons to switch between questions


7. Answer the bonus questions
  
   - See 1.6, but either select answer from drop down box, or enter in value in 
     the answer text field
    
     Hit *send* to submit the answer


8. During the final week, pick from the remaining 3 contestants, which 
   contestants will win the whole game.
  
   - See 1.5, except instead of Make your vote, it will say *Vote For Finals*
	

9. Upload his/her picks to the internet,the very first week, do NOT allow the 
   user to upload his/her pick, unless the user picks an overall winner.

   - When the user hits *exit*, or *log out* a prompt is given to save. If user 
     has not picked an overall winner in the first week, it alerts user to 
     select an ultimate winner.


10. When all the players (users) have been added by the administrative user
    and the game is about to start, the blackberry side must download all the 
    user IDs to allow the player to logon and somehow store those userids on the
    BlackBerry. There must also be a reset that wipes clean all the userids so 
    that it could be reloaded again. This must be persistent (the user should 
    only have to load the players once).

    - See `Setup SD Card for BlackBerry Simulator`_. 
      One can log out and log back in as any user.

