/**
 * @author Quinn Tao
 * @last updated on Oct 7
 */

package com.server.controller;

import com.server.dao.UserDao;
import com.server.exception.CredentialFailureException;
import com.server.exception.PasswordSameException;
import com.server.exception.UserAlreadyExistsException;
import com.server.exception.UserNotFoundException;
import com.server.model.User;
import com.server.model_assembler.UserModelAssembler;
import com.server.service.JWTAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@CrossOrigin(origins = {"http://localhost:8080","http://localhost:3000"}, exposedHeaders = "*")
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserDao userDao;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserModelAssembler assembler;
    private final JWTAuthService jwtService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserDao userDao, BCryptPasswordEncoder passwordEncoder, UserModelAssembler assembler,
                          @Qualifier("jwtservice") JWTAuthService jwtService) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.assembler = assembler;
        this.jwtService = jwtService;
    }

    @GetMapping("/test-connection")
    public String testConnection() {
        String saltedPassword = passwordEncoder.encode("888");
        User testUser = new User();
        testUser.setPassword(saltedPassword);
        userDao.save(testUser);
        return "Successfully connected";
    }

    @PostMapping("/register")
    public String registerNewUser(@RequestParam String uname, @RequestParam String email,
                                  @RequestParam CharSequence password) {
        logger.info("New user registering");
        String saltedPassword = passwordEncoder.encode(password);
        User newUser = new User(uname, email, saltedPassword);

        /* ===== ===== ==== Check if user already exists ====== ===== ===== */
        logger.info("Checking new user sanity");
        User relatedUser = userDao.findByIdentifier(uname, "uname", -1L);
        if (relatedUser != null)
            throw new UserAlreadyExistsException("user name");
        relatedUser = userDao.findByIdentifier(email, "email", -1L);
        if (relatedUser != null)
            throw new UserAlreadyExistsException("email");
        userDao.save(newUser);
        logger.info("New user registered");
        return "User successfully registered";
    }

    /**
     * This Login needs to return an repsonseEntity in order to include the
     *   generated json web token in the header
     * @return the entity model of user
     */
    @GetMapping("/login")
    @CrossOrigin(allowedHeaders = {"token","public-key"}, origins = "http://localhost:3000", exposedHeaders = {"token","public-key"})
    public ResponseEntity<EntityModel<User>> login(@RequestParam(name="identifier") String identifier,
                                @RequestParam CharSequence password) {

        /* ===== ===== ===== find user ===== ===== ===== */
        logger.info("check user existence");
        User locatedUser = userDao.findByIdentifier(identifier, "uname", (long) -1);
        if (locatedUser == null)
            locatedUser = userDao.findByIdentifier(identifier, "email", (long) -1);
        if (locatedUser == null)
            throw new UserNotFoundException(identifier);

        /* ===== ===== ===== check password ===== ===== ===== */
        logger.info("Verify User");
        if (!passwordEncoder.matches(password, locatedUser.getPassword()))
            throw new CredentialFailureException();

        /* ===== ===== ===== generate jwt ===== ===== ===== */
        logger.info("Generating token");
        JWTAuthService.JWTAuthServiceTokenPackage tokenPackage = jwtService.generateToken(locatedUser);

        /* ===== ===== ===== assemble response ===== ===== ===== */
        logger.info("Assembly response to user:" + locatedUser.getName());
        ResponseEntity<EntityModel<User>> response =
                ResponseEntity.ok().header("token", tokenPackage.getToken()).header("public-key",
                        Arrays.toString(tokenPackage.getPublicKey())).body(assembler.toModel(locatedUser));
        logger.info("response assembled");
        return response;
    }

    @PostMapping("/update")
    public EntityModel<User> updateNameAndEmail(@RequestParam Long uid, @RequestParam(required = false) String uname,
                                                @RequestParam(required = false) String email) {
        User currUser = userDao.findByIdentifier(null, null, uid);
        if (currUser == null)
            throw new UserNotFoundException("unknown"); // uid is hidden
        if (uname != null)
            currUser.setName(uname);
        if (email != null)
            currUser.setEmail(email);
        userDao.updateUser(currUser);
        return assembler.toModel(currUser);
    }

    @PostMapping("/change-password")
    public EntityModel<User> updatePassword(@RequestParam String uname, @RequestParam CharSequence newPassword) {
        User currUser = userDao.findByIdentifier(uname, "uname", (long) -1);
        if (currUser == null)
            throw new UserNotFoundException(uname);
        if (passwordEncoder.matches(newPassword, currUser.getPassword())) {
            throw new PasswordSameException();
        }
        currUser.setPassword(passwordEncoder.encode(newPassword));
        userDao.updateUser(currUser);
        return assembler.toModel(currUser);
    }

    @PostMapping("/delete")
    public void deleteUser(@RequestParam Long uid) {
        User currUser = userDao.findByIdentifier(null, null, uid);
        if (currUser == null)
            throw new UserNotFoundException("unknown");
        userDao.delete(uid);
    }

    @PostMapping("/update-settings")
    public void updateSettings(@RequestHeader String token, @RequestHeader byte[] publicKey,
                               @RequestParam long uid, @RequestParam String serializedUserSettings) {
        User currUser = userDao.findByIdentifier(null, null, uid);
        if (currUser == null) {
            throw new UserNotFoundException("unknown");
        }
    }
}
