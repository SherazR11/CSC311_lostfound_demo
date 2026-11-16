package edu.farmingdale.csc311.lostfound.user;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegistrationController {
    private final AppUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(AppUserRepository repository,
                                  PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String showRegistrationPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        if (username.length() < 3) {
            model.addAttribute("error", "Username at least 3 characters");
            return "register";
        }

        if (password.length() < 6) {
            model.addAttribute("error", "Password at least 6 characters");
            return "register";
        }

        if (repository.existsByUsername(username)) {
            model.addAttribute("error", "Username already taken");
            return "register";
        }

        String hashedPassword = passwordEncoder.encode(password);

        AppUser newUser = new AppUser(username, hashedPassword);
        repository.save(newUser);

        return "redirect:/login?registered";
    }
}
