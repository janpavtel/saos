package pl.edu.icm.saos.persistence.search.dto;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import pl.edu.icm.saos.persistence.model.Judgment;

/**
 * @author pavtel
 */
public class JudgmentSearchFilter extends DatabaseSearchFilter<Judgment> {


    public static class Builder extends DatabaseSearchFilter.Builder<Builder, JudgmentSearchFilter>{

        public Builder() {
            instance = new JudgmentSearchFilter();
        }

        @Override
        protected Builder self() {
            return this;
        }

        public Builder startDate(LocalDate startDate){
            instance.setStartDate(startDate);
            return this;
        }

        public Builder endDate(LocalDate endDate){
            instance.setEndDate(endDate);
            return this;
        }

        public Builder sinceModificationDateTime(DateTime modificationDate){
            instance.setSinceModificationDate(modificationDate);
            return this;
        }

        @Override
        public JudgmentSearchFilter filter() {
            upBy("id");
            return instance;
        }
    }

    public static Builder builder(){
        return new Builder();
    }

    private LocalDate startDate;
    private LocalDate endDate;
    private DateTime sinceModificationDate;

    //------------------------ GETTERS --------------------------

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public DateTime getSinceModificationDate() {
        return sinceModificationDate;
    }


    //------------------------ SETTERS --------------------------

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setSinceModificationDate(DateTime sinceModificationDate) {
        this.sinceModificationDate = sinceModificationDate;
    }
}
