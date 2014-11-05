
/* INSTRUCTION: This is a command line application. 
 * So please execute this template with the following arguments:

		arg[0] = username
		arg[1] = password
		arg[2] = name of the new contact (no spaces)
		arg[3] = family name of the new contact (no spaces)
		arg[4] = email of the new contact
		arg[5] = phone of the new contact
*/

/**
 * @author Fran
 */
 
import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import com.google.gdata.data.extensions.Name;
import com.google.gdata.data.extensions.FullName;
import com.google.gdata.data.extensions.GivenName;
import com.google.gdata.data.extensions.FamilyName;
import com.google.gdata.data.extensions.PhoneNumber;
import com.google.gdata.data.extensions.Email;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Class to manage the contacts of a given account.
 * In this case just a method to create a new contact.
 */

public class Contacts {
	  
	  public static ContactEntry createContact(
			  ContactsService myService, 
			  String user, 
			  String firstName, 
			  String Surname,
			  String personMail,
			  String personPhone){
		  
		  ContactEntry contact = new ContactEntry();
		  
		  Name name = new Name();
		  final String NO_YOMI = null;
		  name.setFullName(new FullName(firstName+" "+Surname, NO_YOMI));
		  name.setGivenName(new GivenName(firstName, NO_YOMI));
		  name.setFamilyName(new FamilyName(Surname, NO_YOMI));
		  contact.setName(name);
		  
		  // Set contact's e-mail addresses.
		  Email primaryMail = new Email();
		  primaryMail.setAddress(personMail);
		  primaryMail.setDisplayName(firstName.substring(0, 1) + ". " + Surname);
		  primaryMail.setRel("http://schemas.google.com/g/2005#home");
		  primaryMail.setPrimary(true);
		  contact.addEmailAddress(primaryMail);
		  
		  // Set contact's phone numbers.
		  PhoneNumber primaryPhoneNumber = new PhoneNumber();
		  primaryPhoneNumber.setPhoneNumber(personPhone);
		  primaryPhoneNumber.setRel("http://schemas.google.com/g/2005#home");
		  primaryPhoneNumber.setPrimary(true);
		  contact.addPhoneNumber(primaryPhoneNumber);
		  
		  ContactEntry createdContact = null;
		  try {
			  // Ask the service to insert the new entry
			  URL postUrl = new URL("https://www.google.com/m8/feeds/contacts/"+user+"@gmail.com/base");
			  createdContact = myService.insert(postUrl, contact);
			  System.out.println("Contact's ID: " + createdContact.getId());
		  }
		  catch(AuthenticationException e) {
	        e.printStackTrace();
	      }
	      catch(MalformedURLException e) {
	        e.printStackTrace();
	      }
	      catch(ServiceException e) {
	        e.printStackTrace();
	      }
	      catch(IOException e) {
	        e.printStackTrace();
	      }
		  
		  // Something went wrong if NULL is returned
		  return createdContact;
	  }
	  
    
    public static void main(String[] args) {
    	
      boolean addIt = true;
      PhoneNumber phone = null;
      
      try {
        
        // Create a new Contacts service
        ContactsService myService = new ContactsService("My Application");
        myService.setUserCredentials(args[0],args[1]);
        
        // Get a list of all entries
        URL metafeedUrl = new URL("http://www.google.com/m8/feeds/contacts/"+args[0]+"@gmail.com/base");
        System.out.println("Getting Contacts entries...\n");
        ContactFeed resultFeed = myService.getFeed(metafeedUrl, ContactFeed.class);
        List<ContactEntry> entries = resultFeed.getEntries();
        
        // Check if the phone is already in our agenda
        for(int i=0; i<entries.size(); i++) {
          ContactEntry entry = entries.get(i);
          
          if(entry.getPhoneNumbers().size() > 0){
        	  phone = entry.getPhoneNumbers().get(0);
        	  
        	  // If the phone its already on the list, don't write it
              if( phone.getPhoneNumber().equals(args[5]) ){
            	  System.out.println("\n DON'T ADD");
            	  addIt = false;
            	  break;
              }
              
          }else{
        	  phone = new PhoneNumber();
          }
          
          System.out.println("\t" + entry.getTitle().getPlainText() + " " + phone.getPhoneNumber());
        }
        System.out.println("\nTotal Entries: "+entries.size());
        
        // Tratamos de crear un contacto
        if( addIt ){
        	System.out.println("\nAdding contact: " + args[2] + " " + args[3]);
        	createContact(myService, args[0], args[2], args[3], args[4], args[5]);
        }else{
        	System.out.println("\nThe contact already exists");
        }
        
      }
      catch(AuthenticationException e) {
        e.printStackTrace();
      }
      catch(MalformedURLException e) {
        e.printStackTrace();
      }
      catch(ServiceException e) {
        e.printStackTrace();
      }
      catch(IOException e) {
        e.printStackTrace();
      }
    }
}
