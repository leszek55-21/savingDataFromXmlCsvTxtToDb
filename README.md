# britenetExercise
### Console application for reading/parsing xml, csv and txt files and saving data to the database.

###### For small xml files (up to 2000 chars length) application uses DOM based parser. If file is bigger SAX based parser is fired.
###### File needs to have specyfic data format, otherwise won't be parsed and "Wrong file format!" message will be displayed on the console.

###### xml files need to have person tag and at least name and surname tags in it.
###### csv and txt files need to have at least 4 values in line separated by comas

###### other details and conditions are pretty well explained in comments