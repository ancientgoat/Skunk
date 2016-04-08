"# Skunk" 

MyBranch
MyLogic

1 GT 4
OR
1 LT 14


Part 1
* Read data from various data sources based on metadata
   * Metadata 
   * Is this cached?  For how long secs, mins, hours, days.
   * Identity(2) name
   
Part 2 
* Rules based on metadata
   * Values can be filled from data in Part 1 based on Identity(1) name
   * Rule parts have Identity(2) names
   * Rules/Groups of rules end up true or false.
   
Part 3
* Actions
   * Each action has an Identity(3) name
   * Actions may be of various types 
        * Messages - emails, texts, faxes, pages.  Each message type given a template 
        * Messages - Data to populate message template macros can come from Parts 1 or 2 based on identity(1 or 2) names
        * Write to JMS queue
        * Write to database
        * Send a snail mail letter.
        
        
But, wait - there's more!
        
Add a part to TRS based on Part 1
    * Make dynamic rest calls based on meta data (see Part 1)
    * The great thing is this helps to elminate version problems - because each new version
                should run all older meta data snippets.  I have an idea about that.
                
         
   
   
