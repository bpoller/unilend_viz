package io.nowave.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nowave.api.model.VideoEvent;
import io.nowave.api.repository.VideoEventsRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * Created by olivier on 11/03/16.
 */
public class AnalyticsControllerTest {

    MockMvc mockMvc;

    @InjectMocks
    AnalyticsController analyticsController;

    @Mock
    VideoEventsRepository videoEventsRepository;

    @Before
    public void setup() {
        initMocks(this);

        mockMvc = standaloneSetup(analyticsController).setMessageConverters(new MappingJackson2HttpMessageConverter())
                .build();
    }

    @Test
    public void thatAddPlayStart() throws Exception {
        when(videoEventsRepository.save(any(VideoEvent.class))).thenAnswer(new Answer<VideoEvent>() {
            @Override
            public VideoEvent answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                return (VideoEvent) args[0];
            }
        });

        ObjectMapper mapper = new ObjectMapper();
        VideoEvent videoEvent = new VideoEvent("user1", "video1", "event");
        this.mockMvc.perform(post("/api/analytics").content(mapper.writeValueAsString(videoEvent)).contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)).andDo(print()).andExpect(status().isCreated());

        verify(videoEventsRepository).save(any(VideoEvent.class));
    }


}
