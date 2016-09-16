/*
 * Unit testing of AuthFacade using Arquillian / Wildfly 8
 */
package kz.ya.authcore.facade;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.security.auth.login.LoginException;
import kz.ya.authcore.dao.UserDaoLocal;
import kz.ya.authcore.entity.ApiToken;
import kz.ya.authcore.entity.User;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

/**
 *
 * @author YERLAN
 */
@RunWith(Arquillian.class)
public class AuthFacadeTest {

    @EJB
    private UserDaoLocal userDao;
    @Inject
    private AuthFacadeLocal authFacade;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Deployment
    public static Archive<?> createDeployment() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackage("kz.ya.authcore")
                .addPackage("kz.ya.authcore.dao")
                .addPackage("kz.ya.authcore.dto")
                .addPackage("kz.ya.authcore.entity")
                .addPackage("kz.ya.authcore.facade")
                .addPackage("kz.ya.authcore.util")
                .addAsWebInfResource("META-INF/persistence.xml", "META-INF/persistence.xml");
//                .addAsWebInfResource("META-INF/auth-ds.xml", "auth-ds.xml");
        System.out.println(archive);
        return archive;

        //        JavaArchive archive = ShrinkWrap.create(JavaArchive.class)
//                .addPackage("kz.ya.authcore")
//                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml");
    }

    @Before
    public void setUp() {
        User users = new User("fpi@bk.ru", "123123");
        userDao.save(users);

        User userf = new User("123@gmail.com", "fpifpi");
        userDao.save(userf);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of login method for success result, of class AuthFacade.
     */
    @Test
    public void testLoginSuccess() throws Exception {
        System.out.println("login success");

        String username = "fpi@bk.ru";
        String password = "123123";

        ApiToken result = authFacade.login(username, password);
        assertNotNull(result);
    }

    /**
     * Test of login method for fail result, of class AuthFacade.
     */
    @Test
    public void testLoginFail() throws Exception {
        System.out.println("login fail");

        String username = "123@gmail.com";
        String password = "newPassword";

        thrown.expect(LoginException.class); // test for exception
        ApiToken result = authFacade.login(username, password);
        assertNotNull(result);
    }
}
