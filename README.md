# lsegtask
 Pre-Interview Coding Challenge

 Files are being red from the resources folder and the results are created also in the resources folder under results sub-folder.
 The app exposes a controller with 2 andpoints (2 APIs/Functions)
 To run the app you must make a call(http get) to the second function like this:
 http://localhost:8080/2ndAPI/Function/2
 Last part of the path represents the input parameter that is also validated, that means only numbers 1 and 2 is accepted as stated in the requirement.
 The files that are generated as result will have a timestamp appended to their names and the same folder structure as in the input will be used.
 
 Steps to run the app:
 1. Clone this repo
 2. Navigate to main folder (lsegtask) and open a terminal window(cmd for windows)
 3. Make sure you have at least v17 of java(jdk) installed and JAVA_HOME is set.
 4. This project uses maven wrapper so maven does not need to be installed.
 5. run "./mvnw clean install" command in main folder of this app(where pom.xml is)
 6. if after a while BUILD SUCCESS message appear with green color we are half way there :), at least the unit tests run succesfuly
 7. to start the app run "./mvnw spring-boot:run"
 8. You should see a message like this: Started LsegtaskApplication in 3.712 seconds (process running for 4.622)
 9. Open a browser of your choice and go to this url: http://localhost:8080/2ndAPI/Function/2
 10. You should see some csv data in your browser
 11. navigate to lsegtask\src\main\resources\results
 12. Each exchange will have a folder and inside there are result files and each file is taimestamped, each run(when you hit refresh in your browser) will produce additional results
 13. Thank you!
 
