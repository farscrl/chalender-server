package ch.chalender.api.controller;


import ch.chalender.api.base.MongoDbBaseTest;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

public class EventControllerTest extends MongoDbBaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldListAllEvents() throws Exception {
        this.mockMvc.perform(get("/api/events"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(16)));
    }
}
