package pl.edu.icm.saos.importer.commoncourt.judgment.process;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.importer.common.correction.ImportCorrectionList;
import pl.edu.icm.saos.importer.common.overwriter.JudgmentOverwriter;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.JudgmentKeyword;

/**
 * @author Łukasz Dumiszewski
 */
@Service("ccSpecificJudgmentOverwriter")
public class CcSpecificJudgmentOverwriter implements JudgmentOverwriter<CommonCourtJudgment> {

    
    
    @Override
    public void overwriteJudgment(CommonCourtJudgment oldJudgment, CommonCourtJudgment newJudgment, ImportCorrectionList correctionList) {
        
        
        overwriteCourt(oldJudgment, newJudgment);
        
        overwriteKeywords(oldJudgment, newJudgment);
    }


  
    //------------------------ PRIVATE --------------------------
    
    private void overwriteCourt(CommonCourtJudgment oldJudgment, CommonCourtJudgment newJudgment) {
        oldJudgment.setCourtDivision(newJudgment.getCourtDivision());
    }

    
    private void overwriteKeywords(CommonCourtJudgment oldJudgment, CommonCourtJudgment newJudgment) {
        for (JudgmentKeyword oldKeyword : oldJudgment.getKeywords()) {
            if (!newJudgment.containsKeyword(oldKeyword)) {
                oldJudgment.removeKeyword(oldKeyword);
            }
        }
        for (JudgmentKeyword keyword : newJudgment.getKeywords()) {
            if (!oldJudgment.containsKeyword(keyword)) {
                oldJudgment.addKeyword(keyword);
            }
        }
    }

  
}
