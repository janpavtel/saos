package pl.edu.icm.saos.api.single.court;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.edu.icm.saos.api.services.exceptions.ControllersEntityExceptionHandler;
import pl.edu.icm.saos.api.services.exceptions.ElementDoesNotExistException;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.repository.CommonCourtRepository;

/**
 * Provides functionality for constructing view for single court.
 * @author pavtel
 */
@Controller
@RequestMapping("/api/courts/{courtId}")
public class CourtController extends ControllersEntityExceptionHandler {

    //******** fields ***********
    @Autowired
    private CommonCourtRepository courtRepository;

    @Autowired
    private SingleCourtSuccessRepresentationBuilder singleCourtSuccessRepresentationBuilder;

    //********* END fields ***********


    //***** business methods *************
    @RequestMapping(value = "", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<Object> showCourt(@PathVariable("courtId") int courtId) throws ElementDoesNotExistException {

        CommonCourt court = courtRepository.findOneAndInitialize(courtId);
        if(court == null){
            throw new ElementDoesNotExistException("Court", courtId);
        }

        Object representation = singleCourtSuccessRepresentationBuilder.build(court);

        return new ResponseEntity<>(representation, HttpStatus.OK);
    }

    //******* END business methods ***************


    //*** setters ****
    public void setCourtRepository(CommonCourtRepository courtRepository) {
        this.courtRepository = courtRepository;
    }

    public void setSingleCourtSuccessRepresentationBuilder(SingleCourtSuccessRepresentationBuilder singleCourtSuccessRepresentationBuilder) {
        this.singleCourtSuccessRepresentationBuilder = singleCourtSuccessRepresentationBuilder;
    }
}
