# 314e-asgmt
Spring boot app with gradle as build tool

# How to check top k frequent words
   - The main class(AsgmtApplication) calling one method(findWordsByFreq) and that method takes url as param
   - Pass url param in that method (by default it's - https://www.314e.com/) and run the application 
   - By default top 10 frequent words will be print by application on console by logger  
       - The Top k frequent words where k can be modified from application.properties
       - we can also modify url expand level(depth) from application.properties
   
   Note: 
   - Please pass full url (like-https://www.314e.com/) else program will throw error
   - Please stop and re-run the app after modifying url
   - Program will print the exception for broken pages (like-https://www.314e.com/blog/) and run 
            continue till end.