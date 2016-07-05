#  *Inventory App*

Basic inventory app to track the inventory of a retail store, including current stock and supplier information.

## User Stories

Layout:

* [x] The app contains a list of current products and a button to add a new product.
* [x] Each ListItem displays the product name, current quantity, and price. Each list item also allows the user to track a sale of the item.
* [x] The detail layout for each item displays the remainder of the information stored in the database.
* [x] The detail layout contains buttons to modify the current quantity either by tracking a sale or by receiving a shipment.
* [x] The detail layout contains a button to order from the supplier.
* [x] The detail view contains a button to delete the product record entirely.
* [x] The code adheres to all of the following best practices:
  * Text sizes are defined in sp
  * Lengths are defined in dp
  * Padding and margin is used appropriately, such that the views are not crammed up against each other.
* [x] When there is no information to display in the database, the layout displays a TextView with instructions on how to populate the database.

Functionality:

* [x] The listView populates with the current products stored in the table (SQLite)
* [x] The Add product button prompts the user for information about the product and a picture, each of which are then properly stored in the table.
* [x] User input is validated. In particular, empty product information is not accepted.
* [x] The sale button on each list item properly reduces the quantity available by one. 
* [x] Clicking on the rest of each list item sends the user to the detail screen for the correct product.
* [x] The modify quantity buttons in the detail view properly increase and decrease the quantity available for the correct product.
* [x] The "order more" button sends an intent to either a phone app or an email app to contact the supplier using the information stored in the database.
* [x] The delete button prompts the user for confirmation and, if confirmed, deletes the product record entirely and sends the user back to the main activity.

Code Readability:

* [x] All variables, methods, and resource IDs are descriptively named such that another developer reading the code can easily understand their function.
* [x] The code is properly formatted i.e. there are no unnecessary blank lines; there are no unused variables or methods; there is no commented out code. The code also has proper indentation when defining variables and methods.




## Video Walkthrough 

Here's a walkthrough of implemented user stories:

<img src='https://raw.githubusercontent.com/IsabelPalomar/android-product-inventory/3d9528b093f809775d194016d525e49569920546/Inventory.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).


