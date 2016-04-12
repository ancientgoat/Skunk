"# Skunk" 

Rule_A
 AND
Rule_B
 AND
Group
    Rule_D
    OR
    Rule_E

Then

==============================================================

==============================================================
OR : if true stop executing

SCrhist = the most recent SCr collected <1 year from the current admission date
SCradmin = the lowest SCr collected <24 hours from the current admission date
SCricu = the lowest SCr collected <24 hours of the first admission to the ICU (by transfer date) during the current encounter
SCrest = estimated SCr calculated with the MDRD equation (click HERE for calculation)

SCrhist available
then
    Get SCRadmin
    SCRhist < SCradmin
        then
            SCRref = SCRadmin
        else
            SCRref = SCRhist
else
    history of CKD
        then
            ;
        else
            Calculate SCrest

        Get SCricu
        Get SCradmin
        SCrref = min ( SCradmin, SCRest, SCRisu )



