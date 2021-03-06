package pl.edu.icm.saos.api.single.scdivision;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.edu.icm.saos.api.ApiTestConfiguration;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.common.TestObjectContext;
import pl.edu.icm.saos.persistence.common.TestPersistenceObjectFactory;
import pl.edu.icm.saos.persistence.repository.ScChamberDivisionRepository;

import static org.hamcrest.Matchers.endsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =  {ApiTestConfiguration.class})
@Category(SlowTest.class)
public class ScDivisionControllerTest extends PersistenceTestSupport {

    @Autowired
    private ScDivisionSuccessRepresentationBuilder divisionSuccessRepresentationBuilder;

    @Autowired
    private ScChamberDivisionRepository scChamberDivisionRepository;


    @Autowired
    private TestPersistenceObjectFactory testPersistenceObjectFactory;

    private TestObjectContext testObjectContext;

    private MockMvc mockMvc;

    private String divisionsPath;
    private String chambersPath;


    @Before
    public void setUp(){
        testObjectContext = testPersistenceObjectFactory.createTestObjectContext();
        divisionsPath = "/api/scDivisions/"+testObjectContext.getScFirstDivisionId();
        chambersPath = "/api/scChambers/"+testObjectContext.getScChamberId();

        ScDivisionController scDivisionController = new ScDivisionController();
        scDivisionController.setDivisionSuccessRepresentationBuilder(divisionSuccessRepresentationBuilder);
        scDivisionController.setScChamberDivisionRepository(scChamberDivisionRepository);

        mockMvc =  standaloneSetup(scDivisionController)
                .build();
    }

    @Test
    public void showDivision__it_should_show_all_scDivisions_fields() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(divisionsPath)
                .accept(MediaType.APPLICATION_JSON));


        //then
        actions
                .andExpect(jsonPath("$.data.id").value(testObjectContext.getScFirstDivisionId()))
                .andExpect(jsonPath("$.data.href").value(endsWith(divisionsPath)))
                .andExpect(jsonPath("$.data.name").value(SC_FIRST_DIVISION_NAME))
                .andExpect(jsonPath("$.data.fullName").value(SC_FIRST_DIVISION_FULL_NAME))

                .andExpect(jsonPath("$.data.chamber.id").value(testObjectContext.getScChamberId()))
                .andExpect(jsonPath("$.data.chamber.href").value(endsWith(chambersPath)))
                .andExpect(jsonPath("$.data.chamber.name").value(SC_CHAMBER_NAME))
                ;

    }

    @Test
    public void showDivision__it_should_show_links() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(divisionsPath)
                .accept(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(jsonPath("$.links").isArray())
                .andExpect(jsonPath("$.links[?(@.rel==self)].href[0]").value(endsWith(divisionsPath)))
                .andExpect(jsonPath("$.links[?(@.rel==chamber)].href[0]").value(endsWith(chambersPath)))
        ;
    }

}