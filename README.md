# ΕcoΑΙ
Παρακάτω σας παρουσιάζουμε την ιδέα του ecoai, μίας εφαρμογής-πλατφόρμας διαχείρισης ανακυκλώσιμων υλικών με κοινωνικό, οικολογικό και οικονομικό όφελος, μέσω της επιβράβευσης του πολίτη και εξαργύρωσης πόντων.
Μεσω ενος chatbot ο χρηστης μετά την εγγραφή του, μπορεί να ανεβάσει φωτογραφίες των αντικειμένων προς ανακύκλωση. Η εφαρμογή θα προσδιορίζει αν είναι τα υλικά είναι σωστά προς ανακύκλωση και όχι επικίνδυνα. Θα μπορεί να κάνει και ένα estimation του βάρους. Τα στοιχεία θα πηγαίνουν/αποθηκεύονται στον Δήμο. Επίσης, οι πολίτες θα παίρνουν loyalty points για εξαργύρωση σε εμπορικές κ.α  επιχειρήσεις και ο Δήμος θα γνωρίζει ανα πάσα στιγμή στατιστικά στοιχεία σχετικά με την ανακύκλωση. Ο πολίτης θα μαθαίνει να ανακυκλώνει σωστά μέσω της εφαρμογής αποτρέποντας τη διαλογή επικίνδυνων προς ανακύκλωση υλικών .

Που απευθύνεται και ποια τα ενδεικτικά βήματα εφαρμογής απο ενα Δήμο (αν αφορά ως τελικό χρήστη το δήμο..) (4 bullets)
Απευθύνεται σε Δήμο-πολίτες-εταιρίες που πρεπει να συμπληρώνουν το Η.Μ.Α |Δεν απαιτείται κάποια εγκατάσταση του Δήμου|Ο Δήμος διαχειρίζεται ενα dashboard όπου μπορεί να βλέπει πότε και που γίνεται ανακύκλωση,τι ανακυκλώνεται, κοινά δεδομένα υλικών, και άλλα στατιστικά στοιχεία με τους κατοίκους που τον αφορούν.

Βοηθάει στην σωστή ανακύκλωση|1st level διαλογής πριν τον κάδο ανακύκλωσης|Αποτρέπει την ανακύκλωση υλικών που είναι επικίνδυνα|Ενισχύει την δημιουργία Κοινωνικών συνεταιριστικών επιχειρήσεων|Δίνει χρησιμες πληροφορίες στον Δημο για το πόσα και τι είδους ανακυκλώσιμα υλικά στέλνονται στο σημείο διαλογής.


Technologies used:
Java, Spring, MySQL, Facebook Messanger, DialogFlow, CloudVision, AutoML, Bootstrap/AngularJS, Amazon Lambda, Amazon S3, Amazon API Gateway 

https://dialogflow.com/
https://cloud.google.com/vision/
https://aws.amazon.com/lambda/
https://aws.amazon.com/s3/
https://aws.amazon.com/api-gateway

Installation

1) Create Facebook Developer Account
2) Create a facebook page 
3) Enable messaging for page from step 2
4) Generate application token
5) Import agent to DialogFlow
6) Enable Cloud Vision in Google Cloud
7) Create Credentials for server access
8) Update credentials json (eco*.json) for Java application
9) Deploy Java application to the server with MySQL installed
10) Create AWS S3 bucket with public read access permissions
11) Create AWS lambda with language nodejs and with write permission to S3
12) Update config file with tokens generated on previous steps
13) Package and deploy lambda





