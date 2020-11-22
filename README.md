# 314e-asgmt
    - Spring boot project with gradle as build tool 

# How to check top frequent words 
    - Main class(AsgmtApplication) calling one method(findWordsByFreq) inside run that takes url as param
    - Pass url in that method (by default it's - 314e.com) and run the application 
    - The Top 10 frequant words will be print by application on console by logger
      Optional:  
        - Top k frequent words where k can be modified from application.properties
        - we can also modify url expand level(depth) from application.properties
      Note: 
        - Program will print the execption for broken pages (like-https://www.314e.com/blog/) and run 
        continue till end.