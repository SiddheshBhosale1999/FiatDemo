package com.practice.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.practice.dao.ContactRepository;
import com.practice.dao.UserRepository;
import com.practice.entities.Contact;
import com.practice.entities.User;
import com.practice.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ContactRepository contactRepository;
	 
	// method for adding common data to response
	@ModelAttribute
	public String addCommonData(Model model, Principal principal) {
		String userName = principal.getName();
		System.out.println("userName" + userName);

		// get the user using username(email)
		User user = userRepository.getUserByUserName(userName);

		System.out.println("USER" + user);

		model.addAttribute("user", user);
		return null;

	}

	// dashboard home
	@GetMapping("/index")
	public String dashboard(Model model, Principal principal) {

		model.addAttribute("title", "User Dashboard");
		return "normal/user_dashboard";
	}

	// open add form handler
	@GetMapping("/add-contact")
	public String openAddContact(Model model) {
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());

		return "normal/add_contact_form";
	}

	// processing add contact form
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,
			Principal principal,HttpSession session) {

		try {
			String name = principal.getName();
			User user = this.userRepository.getUserByUserName(name);

			// processing and uploading file
			if (file.isEmpty()) {
				// if the file is empty then try our message
				System.out.println("File is empty.");
			} 
			else {
				// upload the file to folder and update the name to contact
				contact.setImage(file.getOriginalFilename());

				File saveFile = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("Image is uploaded");
			}

			contact.setUser(user);

			user.getContacts().add(contact);

			this.userRepository.save(user);

			System.out.println("DATA" + contact);

			System.out.println("Added to database");
			
			// 🔔 Set success message in session
			session.setAttribute("message", new Message("Your contact is added", "success"));
			
		} catch (Exception e) {
			System.out.println("ERROR" + e.getMessage());
			e.printStackTrace();
			
	        // 🔔 Set error message in session
			session.setAttribute("message", new Message("Something went wrong", "danger"));
		}
		return "normal/add_contact_form";
	}
	
	//view contacts handler
	@GetMapping("/show-contacts")
	public String showContacts(Model m,Principal principal) {
		m.addAttribute("title", "Show User Contacts");
		//contact ki list ko bhejna hai
		String userName = principal.getName();
		
		User user= this.userRepository.getUserByUserName(userName);
		List<Contact> contacts = this.contactRepository.findByUserId(user.getId());
		
		m.addAttribute("contacts", contacts);
		return "normal/show_contacts";
	}
}
