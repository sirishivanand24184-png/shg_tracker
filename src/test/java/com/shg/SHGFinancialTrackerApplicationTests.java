package com.shg;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SHGFinancialTrackerApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void dashboardStatsEndpointLoadsSeededData() throws Exception {
        mockMvc.perform(get("/api/dashboard/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.groupName").value("Shakti Mahila Sangha"))
                .andExpect(jsonPath("$.memberCount").isNumber());
    }

    @Test
    void advisoryRecommendationsEndpointReturnsSeededRecommendations() throws Exception {
        mockMvc.perform(get("/api/advisory/recommendations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").exists());
    }

    @Test
    void transactionsEndpointReturnsCollection() throws Exception {
        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").exists())
                .andExpect(jsonPath("$[0].amount").isNumber());
    }
}
