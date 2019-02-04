package com.oreilly.sacon.library.controllers;

import com.oreilly.sacon.library.availability.Availability;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AvailabilityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Availability availability;

    @Test
    public void shouldReturnAvailabilityInfoForABook() throws Exception {
        Long id = 1L;
        boolean expectedRating = true;
        when(availability.inStock(anyLong())).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.get("/availability/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().json("{'available':" + expectedRating + "}"));
    }

    @Test
    public void shouldChangeTheAvailabiltyOfABook() throws Exception {
        Long id = 1L;
        mockMvc.perform(MockMvcRequestBuilders.put("/borrow/{id}", id))
                .andExpect(status().isOk());

        verify(availability, atMost(1)).borrow(id);
    }
}