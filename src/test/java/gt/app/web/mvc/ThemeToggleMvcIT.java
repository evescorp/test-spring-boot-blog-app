package gt.app.web.mvc;

import gt.app.config.Constants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(Constants.SPRING_PROFILE_TEST)
class ThemeToggleMvcIT {

    @Test
    void landingPageIncludesThemeToggleAndInitializer(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(content().string(org.hamcrest.Matchers.containsString("id=\"theme-toggle\"")))
            .andExpect(content().string(org.hamcrest.Matchers.containsString("app-theme")));
    }
}
